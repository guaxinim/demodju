# tika-quickstart project

## Running the application in dev mode

You can run your application in dev mode that enables live coding using:
```
./mvnw quarkus:dev
```

## Parsing a PDF

```bash
curl -X POST -H "Content-type: application/pdf" --data-binary @sentenca.pdf http://localhost:8080/parse/text
```

Change `sentenca.pdf` to your pdf path.

## Packaging and running the application

The application is packageable using `./mvnw package`.
It produces the executable `tika-quickstart-1.0-SNAPSHOT-runner.jar` file in `/target` directory.
Be aware that it’s not an _über-jar_ as the dependencies are copied into the `target/lib` directory.

The application is now runnable using `java -jar target/tika-quickstart-1.0-SNAPSHOT-runner.jar`.

## Creating a native executable

You can create a native executable using: `./mvnw package -Pnative`.

Or you can use Docker to build the native executable using: `./mvnw package -Pnative -Dquarkus.native.container-build=true`.

You can then execute your binary: `./target/tika-quickstart-1.0-SNAPSHOT-runner`

If you want to learn more about building native executables, please consult https://quarkus.io/guides/building-native-image-guide .