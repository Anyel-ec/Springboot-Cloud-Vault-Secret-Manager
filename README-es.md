# Springboot Vault Secret Manager

Este proyecto es una demostración de cómo integrar **Spring Boot** con **HashiCorp Vault** para gestionar secretos de manera segura.

---

## Prerrequisitos

- [Docker](https://www.docker.com/get-started) instalado y configurado.
- Conocimientos básicos de configuración con archivos `bootstrap.properties` o `application.yml`.

---

## Configuración de Vault mediante Docker

Sigue estos pasos para levantar un servidor de Vault local utilizando Docker:

1. **Descargar la imagen de Vault:**

   ```bash
   docker pull hashicorp/vault
   ```

2. **Iniciar un contenedor de Vault en modo de desarrollo:**

   ```bash
   docker run --cap-add=IPC_LOCK \
              -e 'VAULT_DEV_ROOT_TOKEN_ID=myroot' \
              -e 'VAULT_DEV_LISTEN_ADDRESS=0.0.0.0:8200' \
              -p 8200:8200 \
              --name=dev-vault \
              -d hashicorp/vault
   ```

   - **Opciones importantes:**
      - `--cap-add=IPC_LOCK`: Permite a Vault bloquear memoria para mayor seguridad.
      - `-e 'VAULT_DEV_ROOT_TOKEN_ID=myroot'`: Define el token raíz (`myroot`) en modo desarrollo.
      - `-e 'VAULT_DEV_LISTEN_ADDRESS=0.0.0.0:8200'`: Configura el servidor para escuchar en todas las interfaces de red.
      - `-p 8200:8200`: Expone el puerto 8200 para que las aplicaciones puedan conectarse a Vault.
      - `--name=dev-vault`: Asigna un nombre al contenedor.
      - `-d`: Inicia el contenedor en segundo plano.

3. **Identificar el token raíz (`Root Token`):**

   Cuando el servidor Vault se inicia en modo desarrollo, en la salida de logs se muestra el `Root Token`, que debe utilizarse para configurar las aplicaciones que accedan a Vault.

   Ejemplo de salida:

   ```plaintext
   2024-12-26 09:05:00 Root Token: hvs.TOKEN
   ```

   En este caso, el token sería `hvs.TOKEN`.

   **Nota:** Este token es visible solo durante la inicialización. Si pierdes este token, puedes reiniciar el contenedor o definir un token personalizado con la variable de entorno `VAULT_DEV_ROOT_TOKEN_ID`.

4. **Crear secretos en Vault:**

   Accede al contenedor y crea secretos:

   ```bash
   docker exec -it dev-vault vault kv put secret/myapp username=myuser password=mypassword
   ```

   Esto crea un secreto en Vault bajo el camino `secret/myapp` con las claves `username` y `password`.

5. **Leer los secretos desde Vault:**

   ```bash
   docker exec -it dev-vault vault kv get secret/myapp
   ```

---

## Configuración en Spring Boot

Asegúrate de tener las siguientes configuraciones en tu archivo `bootstrap.properties` o `application.yml`, reemplazando `VAULT_ROOT_TOKEN` por tu token raíz identificado previamente:

```properties
spring.application.name=myapp
spring.cloud.vault.host=localhost
spring.cloud.vault.port=8200
spring.cloud.vault.token=hvs.TOKEN
spring.cloud.vault.scheme=http

spring.cloud.vault.kv.version=2
spring.cloud.vault.authentication=token
logging.level.org.springframework.cloud=DEBUG
```

---

## Configuración del `pom.xml`

Aquí está el contenido del archivo `pom.xml` necesario para este proyecto:

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

## Comandos útiles para Docker

- **Detener el contenedor de Vault:**

  ```bash
  docker stop dev-vault
  ```

- **Eliminar el contenedor de Vault:**

  ```bash
  docker rm dev-vault
  ```

- **Eliminar la imagen de Vault (opcional):**

  ```bash
  docker rmi hashicorp/vault
  ```

---

## Recursos adicionales

- [Documentación oficial de HashiCorp Vault](https://www.vaultproject.io/)
- [Spring Cloud Vault](https://spring.io/projects/spring-cloud-vault)
