# Spring-Training-Ecommerce

Learning Ecommerce Development Activities Using Spring.

These notes cover the project setup done so far: a Maven project, the `pom.xml`, and the first Spring application entry points.

## Prerequisites

- Java 17 (the `pom.xml` sets `<java.version>17</java.version>`)
- Maven 3.6 or later
- An IDE that can import a Maven project (IntelliJ IDEA, Eclipse, or VS Code with the Java extension)

Check the tools:

```bash
java -version
mvn -version
```

## Project layout

The runnable lab lives in `spring-training/spring-core-lab`.

```text
spring-training/spring-core-lab/
├── pom.xml
└── src/main/
    ├── java/com/xbbg/springlab/
    │   ├── SpringlabApplication.java   # Spring Boot entry point
    │   ├── XmlApplication.java         # XML application context
    │   ├── JavaConfigApplication.java  # Java configuration context
    │   └── AppConfig.java              # @Configuration class
    └── resources/
        └── applicationContext.xml      # XML bean definitions
```

Maven expects Java under `src/main/java` and config under `src/main/resources`. Tests, when added, go in `src/test/java`.

## 1. Create the Maven project

From the repo root, the module folder is:

```bash
mkdir -p spring-training/spring-core-lab
cd spring-training/spring-core-lab
```

Maven identifies the project from `pom.xml` in that folder. Coordinates used here:

| Field | Value |
| --- | --- |
| groupId | `com.xbbg` |
| artifactId | `springlab` |
| version | `0.0.1-SNAPSHOT` |
| Java package | `com.xbbg.springlab` |

## 2. Set up `pom.xml`

The build file is `spring-training/spring-core-lab/pom.xml`. It has four parts.

**Parent.** `spring-boot-starter-parent` `4.1.2` supplies dependency versions, the Java compiler level, and the default Spring Boot build. Child dependencies do not repeat a version when the parent already manages it.

**Coordinates.** `groupId`, `artifactId`, and `version` name this module. Maven installs the built jar as `springlab-0.0.1-SNAPSHOT.jar`.

**Dependencies** already declared:

| Dependency | What it is for |
| --- | --- |
| `spring-boot-starter-web` | Spring MVC and the embedded web server |
| `spring-boot-starter-data-jpa` | JPA and Spring Data repositories |
| `spring-boot-starter-validation` | Bean validation (`jakarta.validation`) |
| `h2` (`runtime`) | In-memory database used while the app runs |
| `spring-boot-starter-test` (`test`) | JUnit and Spring test support |

**Build plugin.** `spring-boot-maven-plugin` packages an executable jar and points `mainClass` at `com.xbbg.springlab.SpringlabApplication`.

After any `pom.xml` change, refresh the Maven project in the IDE, or run:

```bash
mvn -q dependency:resolve
```

## 3. Build and run

Run these commands from `spring-training/spring-core-lab`.

```bash
mvn clean compile
mvn test
mvn spring-boot:run
```

`mvn clean package` writes the jar to `target/`. Run that jar with:

```bash
java -jar target/springlab-0.0.1-SNAPSHOT.jar
```

`target/` is generated output. Do not commit it.

The Boot app starts from `SpringlabApplication`, which is marked `@SpringBootApplication` and calls `SpringApplication.run(...)`. With the web starter on the classpath, the embedded server listens on port `8080`.

## 4. Other entry points already in the lab

These two classes load a Spring context without starting the Boot web server. Their bean definitions are still empty.

**XML.** `XmlApplication` loads `src/main/resources/applicationContext.xml` with `ClassPathXmlApplicationContext`. Bean definitions go inside that `<beans>` file.

**Java config.** `JavaConfigApplication` loads `AppConfig`, which is annotated `@Configuration`, through `AnnotationConfigApplicationContext`. Beans are declared as `@Bean` methods on `AppConfig`.

Run either class from the IDE by executing its `main` method. The Boot plugin in `pom.xml` only launches `SpringlabApplication`.

## What is in place so far

- Maven module with Spring Boot parent and Java 17
- Web, JPA, validation, H2, and test starters in `pom.xml`
- Boot main class wired as the packaged `mainClass`
- Empty XML context and empty Java `@Configuration` class for the next lab steps
