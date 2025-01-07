# Tweet Demo

Este es un proyecto basado en **Micronaut** que simula una red social donde los usuarios pueden crear cuentas, seguir a otros usuarios y publicar tweets. Está diseñado para ofrecer una API RESTful que permite la creación de usuarios, la publicación de tweets y la interacción entre usuarios (seguir a otros).

Repositorio: [https://github.com/charles7aponte/tweet-demo.git](https://github.com/charles7aponte/tweet-demo.git)

## Requisitos

Antes de ejecutar este proyecto, asegúrate de tener instalados los siguientes programas:

- [Docker](https://www.docker.com/get-started)
- [Docker Compose](https://docs.docker.com/compose/install/)
- [JDK 17](https://adoptium.net/)
- [Gradle](https://gradle.org/install/) (si no usas Docker)

## Clonación del Proyecto

Para comenzar, clona el repositorio en tu máquina local:

```bash
git clone https://github.com/charles7aponte/tweet-demo.git
cd tweet-demo
```

## Instalación y Configuración

```bash
./gradlew build
```

 Ejecutar el proyecto en Docker
Para ejecutar el proyecto junto con sus dependencias (Redis y MongoDB), sigue estos pasos:

Asegúrate de tener los contenedores de Redis y MongoDB configurados en el archivo docker-compose-test.yml.
Usa Docker Compose para iniciar los contenedores:


```bash
docker-compose -f docker-compose-test.yml up
```
Luego, ejecuta el contenedor de tu aplicación con el archivo Dockerfile proporcionado:

```bash
docker build -t tweet-uala .
docker run -p 8080:8080 tweet-uala
```
Esto ejecutará el servicio en el puerto 8080.


## Acceder a la aplicación

Una vez que la aplicación esté corriendo, puedes acceder a la API REST en:
```
http://localhost:8080
```

También podrás ver la documentación de la API generada por Swagger en:
```
http://localhost:8080/swagger
```

## Rutas de la API

Las rutas de la API están documentadas en la [Wiki del repositorio](https://github.com/charles7aponte/tweet-demo/wiki/Rutas-de-la-API).
