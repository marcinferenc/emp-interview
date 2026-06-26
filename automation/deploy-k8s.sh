#!/usr/bin/env zsh

set -euo pipefail

SCRIPT_DIR="${0:A:h}"
PROJECT_DIR="${SCRIPT_DIR:h}"
CHART_DIR="${PROJECT_DIR}/automation/helm/emp"
RELEASE_NAME="${RELEASE_NAME:-emp-interview}"
NAMESPACE="${NAMESPACE:-$(awk -F': *' '/^namespace:/ {print $2; exit}' "${CHART_DIR}/values.yaml")}"
PORT_FORWARD="${PORT_FORWARD:-false}"

cd "${PROJECT_DIR}" || exit

docker build -f backend/Dockerfile -t emp-backend .
docker build -f persistence/Dockerfile -t emp-persistence .

CURRENT_CONTEXT="$(kubectl config current-context)"

case "${CURRENT_CONTEXT}" in
  kind-*)
    KIND_CLUSTER="${CURRENT_CONTEXT#kind-}"
    kind load docker-image emp-backend:latest --name "${KIND_CLUSTER}"
    kind load docker-image emp-persistence:latest --name "${KIND_CLUSTER}"
    ;;
  minikube)
    minikube image load emp-backend:latest
    minikube image load emp-persistence:latest
    ;;
esac

helm upgrade --install "${RELEASE_NAME}" "${CHART_DIR}" \
  --namespace "${NAMESPACE}" \
  --create-namespace \
  --set namespace="${NAMESPACE}"

kubectl rollout status deployment/emp-persistence --namespace "${NAMESPACE}"
kubectl rollout status deployment/emp-backend --namespace "${NAMESPACE}"

echo "Backend service is available inside the cluster at emp-backend.${NAMESPACE}.svc.cluster.local:8081"
echo "To expose it locally, run:"
echo "kubectl port-forward service/emp-backend 8081:8081 --namespace ${NAMESPACE}"

if [[ "${PORT_FORWARD}" == "true" ]]; then
  kubectl port-forward service/emp-backend 8081:8081 --namespace "${NAMESPACE}"
fi
