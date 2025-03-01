# kafka-learning

Simple example on a Spring Boot app which receives API requests, sends content to a Kafka topic and consumes it

## Installation instructions

**Warning! This example is built on OSX and relies on zsh to work properly. However it's easy enough to change the scripts to use BASH by changing the shebang line to #!/bin/sh**

- Download the latest Apache Kafka (Scala) binary distribution [here](https://kafka.apache.org/downloads) and unzip it to a folder of your choice (we will call it KAFKA_HOME)
- Setup an environment variable in your profile (~/.zprofile for zsh) called _KAFKA_HOME_ and point it to the root folder where you downloaded the Kafka binary distribution
- Install [brew](https://brew.sh)
- Install Kafka with brew by running the command:

```
  brew install kafka
```

- Ensure kafka has been installed successfully by running the command

```
  kakfa-topics
```

You should be an output similar to the one below

```
  kafka-topics
Create, delete, describe, or change a topic.
Option                                   Description
------                                   -----------
--alter                                  Alter the number of partitions,
                                           replica assignment, and/or
                                           configuration for the topic.
--at-min-isr-partitions                  if set when describing topics, only
...Etc.
```

- Ensure you have Java 11+ installed. You can verify this by running the command:

```
java -version
```

## Running instructions

- Open a command prompt under this project and point it to \*/src/main/scripts
- Execute the following command:

```
./wrapper.zsh
```

This command will execute the _./start-kafka-server.zsh_ file which starts the kakfa server in Kraft mode (i.e. without Zookeper as this is on the sunsetting path) in the background and then will execute the _./create-topic.zsh_ file to create the topic _first_topic_ in your cluster. Please see below on how to kill the background process once done.

- At this point your Kafka server should be up and running in the background on localhost:9092
- Start the KafkaLearningApplication.java Spring Boot app. This will startup a Servlet container on port 8080
- The Kafka configuration resides in the _application.properties_ file and you can change it if you want (e.g. to point to an existing Kafka cluster)
- You can then use Postman or other tool to perform a POST request to the following URL:

```
http://localhost:8080/http://localhost:8080/api/v1/kafka/publish
```

With the following body (it's important that you ensure it's a JSON request with the exact format - not content - described below):

```
{
  "firstName": "e.g. Your Name",
  "lastName": "e.g. Your Surname"
}
```

Observe the console output and you will notice that your request gets processed by the Spring Boot controller, it invokes a Kafka publisher to send the message to the topic and a Spring consumer consumes that message and prints the output

## To stop the background Kafka process

**WARNING: The execution of this script is at your own peril! Please make sure it's safe to execute it. The author doesn't accept any responsibilities for the background process that might be killed in an uncontrolled way.**

The script retrieves the Kafka background PID with the following command:

```
ps -efa | grep kafka.Kafka | grep -v grep |awk '{ print $2 }'
```

And then it kills the retrieved PID with -9

To stop the background process, execute the following script:

```
./stop-background-process.zsh
```

Alternatively you can use the ps -efa command to identify the PID yourself
