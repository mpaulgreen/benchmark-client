## Running the application in dev mode

You can run your application in dev mode that enables live coding using:
```
mvn compile quarkus:dev
```
## Packaging and running the application

The application can be packaged using:
```
mvn clean package
```



# To build the image and push it to quay registry

```mvn  package -Dquarkus.container-image.build=true``` <br>

```docker image tag mrigankapaul/benchmark-client:1.0.0-SNAPSHOT quay.io/mpaulgreen/benchmark-client:1``` <br>

```docker push quay.io/mpaulgreen/benchmark-client:1``` <br>

