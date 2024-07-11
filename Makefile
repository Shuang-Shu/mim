VERSION=1.0-SNAPSHOT

build:
	mvn clean compile

package:
	mvn clean package -DskipTests

userimage:
	cd mim-user;docker build --build-arg VERSION=$(VERSION) -t mim-user:$(VERSION) .

# messageimage: TODO

image:
	make package userimage

run:
	./consul/docker_start.sh
	docker run -d --name mim-user -p 28080:28080 mim-user:$(VERSION)

.PHONY: 