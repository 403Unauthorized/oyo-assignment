# OYO Assignment - Top Score Ranking

I'm gonna use gradle wrapper for build and testing.

# How to Build

> Since there is only one config file for dev environment, I will only provide the command for dev.

```shell
./gradlew clean build
```

# How to Run

After executed above build command, then we can run our application.

```shell
# Enable JMX remote port and set spring profile to dev
java -jar -Dcom.sun.management.jmxremote.rmi.port=5000 -Dcom.sun.management.jmxremote=true -Dcom.sun.management.jmxremote.port=5000 -Dcom.sun.management.jmxremote.ssl=false -Dcom.sun.management.jmxremote.authenticate=false -Dcom.sun.management.jmxremote.local.only=false -Djava.rmi.server.hostname=localhost -Dspring.profiles.active=dev ./build/libs/web-0.0.1-SNAPSHOT.jar
```

# How to Test

```shell
./gradlew test
```