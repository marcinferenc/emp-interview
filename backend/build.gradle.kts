plugins {
	java
	id("org.springframework.boot") version "4.1.0"
	id("io.spring.dependency-management") version "1.1.7"
}

java {
	toolchain {
		languageVersion = JavaLanguageVersion.of(21)
	}
}

val integrationTest = sourceSets.create("integrationTest") {
	java.srcDir("src/integrationTest/java")
	resources.srcDir("src/integrationTest/resources")
	compileClasspath += sourceSets.main.get().output + configurations.testRuntimeClasspath.get()
	runtimeClasspath += output + compileClasspath
}

val apiTest = sourceSets.create("apiTest") {
	java.srcDir("src/apiTest/java")
	resources.srcDir("src/apiTest/resources")
	compileClasspath += sourceSets.main.get().output + configurations.testRuntimeClasspath.get()
	runtimeClasspath += output + compileClasspath
}

dependencies {
	implementation("org.springframework.boot:spring-boot-starter-actuator")
	implementation("org.springframework.boot:spring-boot-starter-cache")
	implementation("org.springframework.boot:spring-boot-starter-data-jpa")
	implementation("org.springframework.boot:spring-boot-flyway")
	implementation("org.springframework.boot:spring-boot-starter-webmvc")
	compileOnly("org.projectlombok:lombok")
	runtimeOnly("org.flywaydb:flyway-database-postgresql")
	runtimeOnly("org.postgresql:postgresql")
	annotationProcessor("org.projectlombok:lombok")
	testImplementation("org.springframework.boot:spring-boot-starter-actuator-test")
	testImplementation("org.springframework.boot:spring-boot-starter-data-jpa-test")
	testImplementation("org.springframework.boot:spring-boot-starter-webmvc-test")
	testCompileOnly("org.projectlombok:lombok")
	testRuntimeOnly("org.junit.platform:junit-platform-launcher")
	testAnnotationProcessor("org.projectlombok:lombok")

	// Source: https://mvnrepository.com/artifact/org.apache.commons/commons-lang3
	implementation("org.apache.commons:commons-lang3:3.20.0")

	// Source: https://mvnrepository.com/artifact/org.mockito/mockito-core
	testImplementation("org.mockito:mockito-core:5.23.0")

	// Source: https://mvnrepository.com/artifact/org.apache.httpcomponents.client5/httpclient5
	implementation("org.apache.httpcomponents.client5:httpclient5:5.6.1")

	implementation("com.github.ben-manes.caffeine:caffeine")
}

configurations[integrationTest.implementationConfigurationName].extendsFrom(configurations.testImplementation.get())
configurations[integrationTest.runtimeOnlyConfigurationName].extendsFrom(configurations.testRuntimeOnly.get())
configurations[integrationTest.compileOnlyConfigurationName].extendsFrom(configurations.testCompileOnly.get())
configurations[integrationTest.annotationProcessorConfigurationName].extendsFrom(configurations.testAnnotationProcessor.get())

configurations[apiTest.implementationConfigurationName].extendsFrom(configurations.testImplementation.get())
configurations[apiTest.runtimeOnlyConfigurationName].extendsFrom(configurations.testRuntimeOnly.get())
configurations[apiTest.compileOnlyConfigurationName].extendsFrom(configurations.testCompileOnly.get())
configurations[apiTest.annotationProcessorConfigurationName].extendsFrom(configurations.testAnnotationProcessor.get())

tasks.withType<Test> {
	useJUnitPlatform()
}

tasks.register<Test>("integrationTest") {
	description = "Runs integration tests against the dev PostgreSQL database."
	group = "verification"
	testClassesDirs = integrationTest.output.classesDirs
	classpath = integrationTest.runtimeClasspath
	shouldRunAfter(tasks.test)
//	environment("POSTGRES_URL", "jdbc:postgresql://localhost:5432/emp_coupons_dev")
//	environment("POSTGRES_USER", "emp_coupons_usr")
//	environment("POSTGRES_PASSWORD", "postgres@emp_c0up0ns")
//	environment("IPINFO_API_KEY", "test-api-key")
//	environment("IPINFO_IP_ADDRESS_OVERRIDE", "127.0.0.1")
	useJUnitPlatform()
}

tasks.register<Test>("apiTest") {
	description = "Runs API tests against a Spring Boot HTTP context."
	group = "verification"
	testClassesDirs = apiTest.output.classesDirs
	classpath = apiTest.runtimeClasspath
	shouldRunAfter(tasks.test)
//	environment("POSTGRES_URL", "jdbc:postgresql://localhost:5432/emp_coupons_dev")
//	environment("POSTGRES_USER", "emp_coupons_usr")
//	environment("POSTGRES_PASSWORD", "postgres@emp_c0up0ns")
//	environment("IPINFO_API_KEY", "test-api-key")
//	environment("IPINFO_IP_ADDRESS_OVERRIDE", "127.0.0.1")
	useJUnitPlatform()
}

tasks.check {
	dependsOn(tasks.named("integrationTest"))
	dependsOn(tasks.named("apiTest"))
}

tasks.register("printPostgresEnvVars") {
	doLast {
		println(System.getenv("POSTGRES_URL"))
		println(System.getenv("POSTGRES_USER"))
		println(System.getenv("POSTGRES_PASSWORD"))
	}
}
