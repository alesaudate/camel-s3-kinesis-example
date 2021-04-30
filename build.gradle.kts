import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

val camelVersion = "3.8.0"

plugins {
	id("org.springframework.boot") version "2.4.5"
	id("io.spring.dependency-management") version "1.0.11.RELEASE"
	kotlin("jvm") version "1.4.32"
	kotlin("plugin.spring") version "1.4.32"
}

group = "com.github.alesaudate"
version = "0.0.1-SNAPSHOT"
java.sourceCompatibility = JavaVersion.VERSION_11

repositories {
	mavenCentral()
}

dependencies {

	//Camel
	implementation("org.apache.camel.springboot:camel-spring-boot-starter:$camelVersion")
	implementation("org.apache.camel.springboot:camel-aws2-kinesis-starter:$camelVersion")
	implementation("org.apache.camel.springboot:camel-aws2-s3-starter:$camelVersion")
	implementation("org.apache.camel.springboot:camel-bindy-starter:$camelVersion")

	implementation("org.jetbrains.kotlin:kotlin-reflect")
	implementation("org.jetbrains.kotlin:kotlin-stdlib-jdk8")
	testImplementation("org.springframework.boot:spring-boot-starter-test")
}

tasks.withType<KotlinCompile> {
	kotlinOptions {
		freeCompilerArgs = listOf("-Xjsr305=strict")
		jvmTarget = "11"
	}
}

tasks.withType<Test> {
	useJUnitPlatform()
}
