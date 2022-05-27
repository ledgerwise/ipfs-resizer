# ipfs-resizer

## Description

ipfs-resizer is a services that resizes images, animated gifs and videos stored in IPFS (For the video functionality to work, ffmpeg executable should be installed).

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

## Configuration

Configuration of the service parameters can be done through env variables or parameters:

- Port where the service will be running

  - `SERVER_PORT`

  - `-Dserver.port`

- IPFS node endpoint (e.g. `https://cloudflare-ipfs.com/ipfs/`)

  - `IPFS_ENDPOINT`

  - `-DipfsEndpoint`

- Allowed sizes (Comma separated numbers. e.g. `200,300`)

  - `ALLOWED_SIZES`

  - `-DallowedSizes`

## Local sample links

- Swagger UI: http://localhost:8080/swagger-ui/index.html
- Allowed sizes: http://localhost:8080/api/v1/allowed_sizes
- Sample Image: http://localhost:8080/api/v1/resized?cid=QmSzSfwKt4WpwJvBWmoLtfoCup9QgVQDyTw7zoMTvmiQjd&size=200
- Sample GIF: http://localhost:8080/api/v1/resized?cid=QmUM8YVj2YCrC9mG4TaKapSM8Z5T3MqhQ9eJqDKdgzX5nM&size=200
- Sample Video: http://localhost:8080/api/v1/resized?cid=QmSXorBcDdxwE3kVznUPCUy6xbReQ9qzJEugPfiY614SGR/Front/Grail/DC_GRAIL_BATMAN-REBIRTH_ANIMATION_1.mp4&size=370

## Sample links

- Swagger UI: http://ipfs-resizer.ledgerwise.io/swagger-ui/index.html
- Allowed sizes: http://ipfs-resizer.ledgerwise.io/api/v1/allowed_sizes
- Sample Image: http://ipfs-resizer.ledgerwise.io/api/v1/resized?cid=QmSzSfwKt4WpwJvBWmoLtfoCup9QgVQDyTw7zoMTvmiQjd&size=200
- Sample GIF: http://ipfs-resizer.ledgerwise.io/api/v1/resized?cid=QmUM8YVj2YCrC9mG4TaKapSM8Z5T3MqhQ9eJqDKdgzX5nM&size=200
- Sample Video: http://ipfs-resizer.ledgerwise.io/api/v1/resized?cid=QmSXorBcDdxwE3kVznUPCUy6xbReQ9qzJEugPfiY614SGR/Front/Grail/DC_GRAIL_BATMAN-REBIRTH_ANIMATION_1.mp4&size=370
