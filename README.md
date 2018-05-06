# State-of-the-world
The utility of this project is to visualize the world most unstable coutries on a map in real time 
the data is provided from RSS Feed 
### Technologies

this project uses a number of open source projects to work properly:

* [Docker](https://www.docker.com/) - to perform operating-system-level virtualization also known as containerization
* [Apache Hadoop](http://hadoop.apache.org/) - framework that allows for the distributed processing of large data sets across clusters of computers using simple programming models.
* [Apache Kafka](https://kafka.apache.org/) - an open-source stream-processing software platform
* [Apache Spark](https://spark.apache.org/) - an open-source cluster-computing framework
* [node.js](https://nodejs.org) - evented I/O for the backend
* [Express](https://expressjs.com/) - fast node.js network app framework
* [Socket.IO](https://socket.io/) - a JavaScript library for realtime web applications
* [jQuery](http://jquery.com/) - avaScript library designed to simplify the client-side scripting of HTML 

### Installation

#### 1/ Build the docker image 

```sh
$ git clone https://github.com/liliasfaxi/hadoop-cluster-docker
$ cd hadoop-cluster-docker
$ ./build-image.sh
$ sudo ./start-container.sh
```
Apache Hadoop, Apache kafka, Apache Spark are by default installed in the docker image

#### 2/ copy the appropiriate directories into the docker container

```sh
$ git clone https://github.com/alibouhlel/State-of-the-world.git
$ cd State-of-the-world
$ docker cp kafkaProducer hadoop-master:root
$ docker cp visualization hadoop-master:root
$ docker cp streaming-1.0-SNAPSHOT-jar-with-dependencies.jar hadoop-master:root
```

#### 3/ preparations
1- execute the docker container and start the kafka service
```sh
$ docker exec -it hadoop-master bash
$  ./start-kafka-zookeeper.sh
```
2- get the dependencies to start the server
```sh
$ cd visualization
$ npm install

```
3- compile the java classes so we can execute the kafka producer
```sh
$ cd kafkaProducer
$ javac -cp "$KAFKA_HOME/libs/*":. *.java
```
#### 3/ Run this application
we'll need 3 terminal
the first one will be used to start spark 

```sh
$ docker exec -it hadoop-master bash
$ spark-submit --class SparkKafka
             --master local[2]
             streaming-1.0-SNAPSHOT-jar-with-dependencies.jar
             localhost:2181 test kafkatopic 1 >> out
```
the second terminal will be used to start the node server
```sh
$ cd visualization
$ node app.js
```
and finally we'll use the third terminal to start kafka producer
```sh
$ cd kafkaProducer
$ java -cp "$KAFKA_HOME/libs/*":. RssFeedProducer kafkatopic
```
open your navigator on 172.18.0.2:3000 to visualize the map, you'll find red cercles on top of the unstable countries 







