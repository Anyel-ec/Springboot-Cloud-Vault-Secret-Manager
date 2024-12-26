# Springboot Vault Secret Manager

This project demonstrates how to integrate **Spring Boot** with **HashiCorp Vault** for secure secrets management.

---

## Prerequisites

- [Docker](https://www.docker.com/get-started) installed and configured.
- Basic knowledge of configuration using `bootstrap.properties` or `application.yml` files.

---

## Configuring Vault with Docker

Follow these steps to set up a local Vault server using Docker:

1. **Pull the Vault image:**

   ```bash
   docker pull hashicorp/vault
   ```

2. **Start a Vault container in development mode:**

   ```bash
   docker run --cap-add=IPC_LOCK \
              -e 'VAULT_DEV_ROOT_TOKEN_ID=myroot' \
              -e 'VAULT_DEV_LISTEN_ADDRESS=0.0.0.0:8200' \
              -p 8200:8200 \
              --name=dev-vault \
              -d hashicorp/vault
   ```

    - **Key options:**
        - `--cap-add=IPC_LOCK`: Allows Vault to lock memory for increased security.
        - `-e 'VAULT_DEV_ROOT_TOKEN_ID=myroot'`: Sets the root token (`myroot`) in development mode.
        - `-e 'VAULT_DEV_LISTEN_ADDRESS=0.0.0.0:8200'`: Configures the server to listen on all network interfaces.
        - `-p 8200:8200`: Exposes port 8200 for applications to connect to Vault.
        - `--name=dev-vault`: Assigns a name to the container.
        - `-d`: Runs the container in the background.

3. **Identify the root token:**

   When the Vault server starts in development mode, the `Root Token` is displayed in the logs. Use this token to configure applications that connect to Vault.

   Example output:

   ```plaintext
   2024-12-26 09:05:00 Root Token: hvs.TU_TOKEN
   ```

   In this case, the token is `hvs.TU_TOKEN`.

   **Note:** This token is only visible during initialization. If you lose it, restart the container or define a custom token using the `VAULT_DEV_ROOT_TOKEN_ID` environment variable.


5. **Create secrets in Vault:**

   Access the container and create secrets:

   ```bash
   docker exec -it dev-vault vault kv put secret/myapp username=myuser password=mypassword
   ```

   This creates a secret in Vault at the path `secret/myapp` with the keys `username` and `password`.

6. **Read secrets from Vault:**

   ```bash
   docker exec -it dev-vault vault kv get secret/myapp
   ```

## *Example UI*
   *Login*
   ![image](https://github.com/user-attachments/assets/5fc04101-ad4e-4d68-b038-58237c94d171)
   *Secret*
   ![image](https://github.com/user-attachments/assets/3d55a203-07fa-43df-8ede-d7cfdefb11e3)
   ![image](https://github.com/user-attachments/assets/a9994131-1b69-4dc1-ae09-4c05b3c793ed)
   *Controller*
   ![image](https://github.com/user-attachments/assets/a2c6791c-ad93-4ed2-bdf8-f1d5268ae786)
---

## Spring Boot Configuration

Ensure the following settings are present in your `bootstrap.properties` or `application.yml` file, replacing `VAULT_ROOT_TOKEN` with the identified root token:

```properties
spring.application.name=myapp
spring.cloud.vault.host=localhost
spring.cloud.vault.port=8200
spring.cloud.vault.token=TU_TOKEN
spring.cloud.vault.scheme=http

spring.cloud.vault.kv.version=2
spring.cloud.vault.authentication=token
logging.level.org.springframework.cloud=DEBUG
```

---

## Maven `pom.xml` Configuration

Here is the `pom.xml` file for this project:

```xml
<project xmlns="http://maven.apache.org/POM/4.0.0" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
         xsi:schemaLocation="http://maven.apache.org/POM/4.0.0 https://maven.apache.org/xsd/maven-4.0.0.xsd">
    <modelVersion>4.0.0</modelVersion>
    <parent>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-starter-parent</artifactId>
        <version>3.4.1</version>
        <relativePath/>
    </parent>
    <groupId>top.anyel</groupId>
    <artifactId>vault</artifactId>
    <version>0.0.1-SNAPSHOT</version>
    <packaging>war</packaging>
    <name>Springboot-Vault-Secret-Manager</name>
    <description>Springboot-Vault-Secret-Manager</description>

    <properties>
        <java.version>17</java.version>
    </properties>

    <dependencyManagement>
        <dependencies>
            <dependency>
                <groupId>org.springframework.cloud</groupId>
                <artifactId>spring-cloud-dependencies</artifactId>
                <version>2024.0.0</version>
                <type>pom</type>
                <scope>import</scope>
            </dependency>
        </dependencies>
    </dependencyManagement>

    <dependencies>
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-web</artifactId>
        </dependency>
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-test</artifactId>
            <scope>test</scope>
        </dependency>
        <dependency>
            <groupId>org.springframework.cloud</groupId>
            <artifactId>spring-cloud-starter-vault-config</artifactId>
        </dependency>
        <dependency>
            <groupId>org.projectlombok</groupId>
            <artifactId>lombok</artifactId>
            <optional>true</optional>
        </dependency>
    </dependencies>

    <build>
        <plugins>
            <plugin>
                <groupId>org.springframework.boot</groupId>
                <artifactId>spring-boot-maven-plugin</artifactId>
            </plugin>
        </plugins>
    </build>
</project>
```

---

## Useful Docker Commands

- **Stop the Vault container:**

  ```bash
  docker stop dev-vault
  ```

- **Remove the Vault container:**

  ```bash
  docker rm dev-vault
  ```

- **Remove the Vault image (optional):**

  ```bash
  docker rmi hashicorp/vault
  ```

---

## Additional Resources

- [Official HashiCorp Vault Documentation](https://www.vaultproject.io/)
- [Spring Cloud Vault](https://spring.io/projects/spring-cloud-vault)
