Local startup:

Setup postgres manually or run in console command below:

docker-compose up

Run maven:

mvn clean install

    Using IDE
Run Application.class


    In console

java -jar target/drone-test-application-0.1.0.jar


    Running as docker container

Build an image: 

docker build -t drone-application:latest .

If you want to connect to localhost (postgres) from container, you should change localhost to host.docker.internal in PostgresProperties.class or use applied external configuration

Running container:

    Windows

To use externalized configuration you should enable file sharing in docker (Settings-Resources-File sharing) and choose directory with config file (path/to/project/config)

Then run command:

docker run --add-host host.docker.internal:host-gateway -p 8080:8080 -v %cd%/config:/config -e spring.config.additional-location=config/external-configuration.yml drone-application:latest

    Linux/MacOS
docker run --add-host host.docker.internal:host-gateway -p 8080:8080 -v $(pwd)/config:/config -e spring.config.additional-location=config/external-configuration.yml drone-application:latest


Swagger url: http://localhost:8080/swagger-ui/index.html