SPRING_BOOT_IMAGE=spring-auth-api

jwt-secret:
	openssl rand -base64 64

install:
	mvn dependency:resolve

compile:
	rm -rf target && mvn clean compile

# builds clean version of the package using maven (target/store-1.0.0.jar)
build:
	mvn clean package

# runs the java build (untested / env vars required)
run:
	java -jar target/store-1.0.0.jar

# use maven to run the springboot application
dev:
	./mvnw spring-boot:run

docker-build:
	docker build -t ${SPRING_BOOT_IMAGE} .

# primarily used for PROD image testing	
docker-run:
	docker run \
	--env-file .env \
	-p 8080:8080 \
	-e SPRING_PROFILES_ACTIVE=prod \
	${SPRING_BOOT_IMAGE}


