
Websocket API:
https://github.com/GraphWalker/graphwalker-project/wiki/Websocket-API

--------------------------------------------------------------------------------------------


**************************************************************
****     run GraphWalker online test as REST service      ****
**************************************************************

# in another terminal/PuTTy , lunch the GraphWalker Websocket service and load the model:

    Execute
    # If required, make sure to use the correct java:
    $ export JAVA_HOME=`/usr/libexec/java_home -v 1.8`
    $ cd workspace/GraphWalker-websocket_client/
    $ java -jar ../lib/graphwalker-cli-4.2.0.jar online --port 8887 --service WEBSOCKET

    $ java -jar ../lib/graphwalker-cli-4.2.0.jar --debug all online --port 9999 --service WEBSOCKET

# In VS-Code run:
$ mvn clean test

---------------------------------------------------------------------------------------------

Setup
=====

add to pom.xml:
---------------

    <dependency>
        <groupId>org.java-websocket</groupId>
        <artifactId>Java-WebSocket</artifactId>
        <version>1.3.4</version>
    </dependency>




vvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvv

    $ java -jar ../lib/graphwalker-cli-4.2.0.jar -d all online -s RESTFUL -m src/main/resources/com/cyberark/BstModel.json "random(edge_coverage(100))"
    OR:
    $ java -jar ../lib/graphwalker-cli-4.2.0.jar -d all online -s RESTFUL -m src/main/resources/com/cyberark/BstModel.json "random(edge_coverage(100) && vertex_coverage(100))"
    OR:
    $ java -jar ../lib/graphwalker-cli-4.2.0.jar -d all online -s RESTFUL -m src/main/resources/com/cyberark/BstModel.json "random(time_duration(10))"


REST APIs:
https://github.com/GraphWalker/graphwalker-project/wiki/Rest-API-overview

Postman
=======
The Postman data is at lib/GraphWalker.postman_collection.json
you can copy its content and paste in your Postman import.


CLI HTTP client
===============
https://httpie.org/

CentOS:
$ yum install httpie

Mac:
$ brew install httpie

$ http GET  localhost:8887/graphwalker/hasNext

$ http GET  localhost:8887/graphwalker/getNext

$ http GET  localhost:8887/graphwalker/getData

$ http PUT  localhost:8887/graphwalker/restart



# in VS-Code run:
-----------------
Execute
# If required, make sure to use the correct java:
$ export JAVA_HOME=`/usr/libexec/java_home -v 1.8`
$ mvn clean compile exec:java -Dexec.mainClass="com.cyberark.BstTest"

or:
$ mvn clean
$ mvn compile
$ mvn exec:java -Dexec.mainClass="com.cyberark.BstTest"


=====================================================================

In * Terminal * :
-----------------
$ export JAVA_HOME=`/usr/libexec/java_home -v 1.8`
$ pwd
/Users/oferr/workspace/GraphWalker-websocket_client

$ java -jar ../lib/graphwalker-cli-4.2.0.jar online
or
$ java -jar ../lib/graphwalker-cli-4.2.0.jar  --debug all online
or
$ java -jar graphwalker-cli-4.2.0.jar online --port 9999 --service WEBSOCKET

In * VS-Code * :
----------------
$ export JAVA_HOME=`/usr/libexec/java_home -v 1.8`
$ mvn clean test


Debugging:
----------
The launch json for debugging when running unit-test is:

{
    "type": "java",
    "name": "Debug (Attach)",
    "projectName": "graphwalker_websocket_client",
    "request": "attach",
    "hostName": "localhost",
    "port": 8000
}

In VS-Code:
$ mvnDebug "-DforkCount=0" test

And launch the 'Debug(Attach)' at the Run/Debug pannel




=======================================================================================
Execution output:
=================

- Get data
- Got message: {"data":{"vals":"0"},"success":true,"command":"getData"}
- Have next step?
- Got message: {"success":true,"hasNext":true,"command":"hasNext"}
 Get next step
- Got message: {"elementId":"9a6b8be2-3e0c-4e8d-bb38-21107b66a4d3",
                "visitedCount":1,"stopConditionFulfillment":0.25,"data":{"vals":"1"},
                "modelId":"cbca8c67-6d0c-40b4-8afb-032b069a4bc1","totalCount":6,
                "command":"visitedElement"}
- Got message: {"elementId":"9a6b8be2-3e0c-4e8d-bb38-21107b66a4d3",
                "modelId":"cbca8c67-6d0c-40b4-8afb-032b069a4bc1","success":true,
                "name":"e_Add","command":"getNext"}
- Get data
- Got message: {"data":{"vals":"1"},"success":true,"command":"getData"}
- Have next step?

