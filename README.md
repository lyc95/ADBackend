# GetFitWithHenry

## Running on local machine

**Initial Set Up:** 
    
> Ensure that Docker, Docker Compose and Android Studio is install in the local machine
  - [Docker Installation Guide](https://docs.docker.com/get-docker/)
  - [Docker Compose Installation Guide](https://docs.docker.com/compose/install/)
  - [Android Studio Installation Guide](https://developer.android.com/studio)

*Note: Docker compose is automatically installed when installing [Docker Desktop](https://www.docker.com/products/docker-desktop/).
        
>Before starting docker compose

- Free up local ports 8080, 5000, 3000, 3001, 3306
- 3306 is usually used by MySQL server
  - [Stopping MySQL server on Linux and Windows](https://www.tutorialspoint.com/starting-and-stopping-mysql-server)
  - [Stopping MySQL server on MacOS](https://wpbeaches.com/restart-start-stop-mysql-server-from-command-line-macos-linux/) or you may stop MySQL through (System Preference -> MySQL -> Stop MySQL Server)


## Running procedure
Running procedure for Windows, MacOS (Intel/M1) & Linux (Ubuntu) are the same.
Using Terminal, enter ADBackend file direction. Ensure that **docker-compose.yml** file is in the directory. You may use command 'ls' to verify.
```bash
  ls
```

#### Pulling docker image and running container with docker compose
```bash
  docker compose up
```

#### Verify if the containers are running
```bash
  docker ps -a
```

#### Stopping docker containers (ensure you are in the same directory)
```bash
  docker compose down
```

-or-

You may stop the docker compose through docker desktop. (Force stop: ^c)

*Note: **DO NOT** include '-' in docker compose cmd (i.e. docker-compose up). Running with hypen will generate '_' in container name which makes url invalid. 

----

## Android Application

1. Open GetFitWithHenry android with Android Studio, we recommend to use **API 29** for the emulated android device.
2. Nagivate to Constants.java file (App/Java/model/Constants.java) and update the ip address.
```java
public class Constants {
    public static String javaURL = "${host}:8080";
    public static String reactURL = "${host}:3000/android";
}
```

| Parameter | Type     | Description                  |
| :-------- | :------- | :-------------------------   |
| `host`    | `string` | Your machine's IPv4 address. |

3. Run android application.

## React Web Application
In your browser, enter your host ip with the stated port number. (e.g. 192.XX.XX.X:3000)

- To access **user** react application:
> ${host}:3000 

- To access **admin** react application:
> ${host}:3001

| Parameter | Type     | Description                  |
| :-------- | :------- | :-------------------------   |
| `host`    | `string` | Your machine's IPv4 address. |


