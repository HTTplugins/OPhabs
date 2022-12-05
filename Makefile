build-and-run: clean-and-build run
clean-and-build:
	mvn clean install
build:
	mvn install

clean:
	mvn clean

run:
	cp /home/alfonso/Projects/OPhabs/target/original*.jar /home/alfonso/.minecraft/plugins/OPhabs.jar

default: build
