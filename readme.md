how to build and run locally

1. dependencies: postgres / gradle / java21 / IPInfo API KEY / IP address value e.g. 188.122.0.88
2. local bare-metal run: two DBs - one for app and one for test: **emp_coupons** / **emp_coupons_dev**
3. optionally: docker / kubernetes cluster / helm / deploy script
4. IPInfo service returns no country code for lower IP address classes.
   To overcome that, use "dev" spring profile which overrides the IP address by the one given
in the env variable.

DB setup:
```
create database emp_coupons;
create database emp_coupons_dev;
create role emp_coupons_usr with login password 'your_password_here';
```

#set auth method (pg_hba file) to scram-sha-256;

```
brew services restart postgresql@17
alter user emp_coupons_usr with password 'your_password_here';

grant all privileges on database emp_coupons     to emp_coupons_usr;
grant all privileges on database emp_coupons_dev to emp_coupons_usr;

#on both DBs
GRANT ALL PRIVILEGES ON SCHEMA public TO emp_coupons_usr;
```

running tests:
```
export POSTGRES_URL=jdbc:postgresql://localhost:5432/emp_coupons_dev \
&& export POSTGRES_USER=emp_coupons_usr \
&& export POSTGRES_PASSWORD=postgres@emp_c0up0ns \
&& export IPINFO_API_KEY=2c1f2fe3c5c410 \
&& export IPINFO_IP_ADDRESS_OVERRIDE=188.122.0.88 \
&& export spring_profiles_active="dev" \
&& ./gradlew clean build
```

#app configuration
(use config templates in intellij)
```
POSTGRES_URL=jdbc:postgresql://localhost:5432/emp_coupons;
POSTGRES_USER=emp_coupons_usr;
POSTGRES_PASSWORD=your_password_here;
IPINFO_API_KEY=yourApiKey
```

building Docker image
`docker build -f backend/Dockerfile -t emp-interview-backend .`

deploying to Kubernetes:
— sets up port forwarding to local K8S cluster
`export PORT_FORWARD=true && ./deploy-k8s.sh`


curl samples:
**CREATE OPERATION**
```
curl --header "Content-Type: application/json" \
--request POST \
--data '{"couponCode":"edge","countryCode":"pl", "claimLimitCount": 3}' \
http://localhost:8080/create
```

**CLAIM operation**
```
curl --header "Content-Type: application/json" \
  --request POST \
  --data '{"userEmailId":"abc@def.com","couponCode":"edge"}' \
  http://localhost:8080/claim -v
```
