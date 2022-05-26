# ipfs-resizer

## Description

ipfs-resizer is a services that resizes images stored in IPFS.

## Build

jar build

```bash
mvn clean package spring-boot:repackage
```

## Run

jar

```bash
java -jar target/ipfs-resizer-<version>.jar
```

## Local sample links

- Swagger UI: http://localhost:8080/swagger-ui/index.html
- Allowed sizes: http://localhost:8080/api/v1/allowed_sizes
- Sample Image: http://localhost:8080/api/v1/resized?cid=QmSzSfwKt4WpwJvBWmoLtfoCup9QgVQDyTw7zoMTvmiQjd&size=200
- Sample GIF: http://localhost:8080/api/v1/resized?cid=QmUM8YVj2YCrC9mG4TaKapSM8Z5T3MqhQ9eJqDKdgzX5nM&size=200
- Sample Video: http://localhost:8080/api/v1/resized?cid=QmeTK7t2JHUuXVGQh3TX5dUXh7dNUg1JoT7BYd83ZoQcb5&size=200

## Sample links

- Swagger UI: http://ipfs-resizer.ledgerwise.io/swagger-ui/index.html
- Allowed sizes: http://ipfs-resizer.ledgerwise.io/api/v1/allowed_sizes
- Sample Image: http://ipfs-resizer.ledgerwise.io/api/v1/resized?cid=QmSzSfwKt4WpwJvBWmoLtfoCup9QgVQDyTw7zoMTvmiQjd&size=200
- Sample GIF: http://ipfs-resizer.ledgerwise.io/api/v1/resized?cid=QmUM8YVj2YCrC9mG4TaKapSM8Z5T3MqhQ9eJqDKdgzX5nM&size=200
- Sample Video: http://ipfs-resizer.ledgerwise.io/api/v1/resized?cid=QmeTK7t2JHUuXVGQh3TX5dUXh7dNUg1JoT7BYd83ZoQcb5&size=200
