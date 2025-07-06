plugins {
    id("java")
    id("war")
}

group = "com.github.AmitSureshChandra"
version = "1.0"

repositories {
    mavenCentral()
}

dependencies {
    implementation("javax.servlet:javax.servlet-api:4.0.1")
    implementation("com.fasterxml.jackson.core:jackson-databind:2.15.2")
    implementation("org.springframework:spring-web:5.3.21")
    testImplementation(platform("org.junit:junit-bom:5.10.0"))
    testImplementation("org.junit.jupiter:junit-jupiter")
}

tasks.test {
    useJUnitPlatform()
}

tasks.war {
    archiveFileName.set("webapp.war")
}