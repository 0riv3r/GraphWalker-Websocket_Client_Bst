
Websocket API:
https://github.com/GraphWalker/graphwalker-project/wiki/Websocket-API

=====================================================================================================

Setup
=====

add to pom.xml:
---------------

    <dependency>
        <groupId>org.java-websocket</groupId>
        <artifactId>Java-WebSocket</artifactId>
        <version>1.3.4</version>
    </dependency>

=====================================================================

In the following test log, a few line-prints that I sent to the Terminal. you can see how the setData manage to keep the Model in sync:

e_ToMenu
>>>>>>  stateData - vals = 6
v_MenuDispatcher
>>>>>>  stateData - vals = 6
e_Delete
bst.nodes: [1, 3, 4, 8, 13, 14]
inTree: [1, 3, 4, 8, 13, 14]
nodesStack: [13, 14, 3, 4, 1, 8]
>>>>>>  stateData - vals = 5
v_Deleted
bst.nodes: [1, 3, 4, 13, 14]
inTree: [1, 3, 4, 13, 14]
nodesStack: [13, 14, 3, 4, 1]
>>>>>>  stateData - vals = 5
e_ToMenu
>>>>>>  stateData - vals = 5
v_MenuDispatcher
>>>>>>  stateData - vals = 5
e_GetNodes
>>>>>>  stateData - vals = 5
v_NodesList
>>>>>>  stateData - vals = 5
e_ToMenu
>>>>>>  stateData - vals = 5
v_MenuDispatcher
>>>>>>  stateData - vals = 5
e_Add
>>>>>>  stateData - vals = 6
v_Added
bst.nodes: [1, 3, 4, 13, 14]
inTree: [1, 3, 4, 13, 14]
nodesStack: [13, 14, 3, 4, 1]
in v_Added, jsToSetData:  vals=5;
>>>>>>  stateData - vals = 5
e_ToMenu
>>>>>>  stateData - vals = 5
v_MenuDispatcher
>>>>>>  stateData - vals = 5
e_FindFakeVal
>>>>>>  stateData - vals = 5
v_NotFound
>>>>>>  stateData - vals = 5
e_ToMenu
>>>>>>  stateData - vals = 5
v_MenuDispatcher
>>>>>>  stateData - vals = 5
e_Delete
bst.nodes: [1, 3, 4, 13, 14]
inTree: [1, 3, 4, 13, 14]
nodesStack: [13, 14, 3, 4, 1]

-----------------------------
Explanation:

- stateData here represents the Model's nodes counter, which is 6 at the begining here and then change to 5 after e_Delete call.

>>>>>>  stateData - vals = 6
e_Delete
bst.nodes: [1, 3, 4, 8, 13, 14]
inTree: [1, 3, 4, 8, 13, 14]
nodesStack: [13, 14, 3, 4, 1, 8]
>>>>>>  stateData - vals = 5

- the test method 'e_Add' is executed and a random value is chosen to add to Bst.
In this case the randomly chosen value is already in Bst and is not being added (rejected by Bst)
therefore the number of nodes in Bst is not changed after this 'add' method execution.

- But we see that the Model doesn't know that the 'add' is rejected, and it increase its nodes counter by one to 6:

>>>>>>  stateData - vals = 5
e_Add
>>>>>>  stateData - vals = 6

- the test updates the model through the 'seData' API with the correct number of nodes.
it sends to the model the javascript statement "vals=6;" that sets the value of the model's variable 'vals' to 6.
Then we see that the model is now updated and in sync with the correct number of nodes

>>>>>>  stateData - vals = 6
v_Added
bst.nodes: [1, 3, 4, 13, 14]
inTree: [1, 3, 4, 13, 14]
nodesStack: [13, 14, 3, 4, 1]
in v_Added, jsToSetData:  vals=5;
>>>>>>  stateData - vals = 5

=====================================================================

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

*******************************************************************
****     run GraphWalker online test as Websocket service      ****
*******************************************************************

# in another terminal/PuTTy , lunch the GraphWalker Websocket service and load the model:
-----------------------------------------------------------------------------------------

    # If required, make sure to use the correct java:
    $ export JAVA_HOME=`/usr/libexec/java_home -v 1.8`

    $ cd workspace/GraphWalker-websocket_client/

    $ java -jar ../lib/graphwalker-cli-4.2.0.jar online --port 8887 --service WEBSOCKET

    $ java -jar ../lib/graphwalker-cli-4.2.0.jar --debug all online --port 9999 --service WEBSOCKET

    
    $ java -jar ../lib/graphwalker-cli-4.3.0-SNAPSHOT.jar --debug all online

    $ export JAVA_HOME=`/usr/libexec/java_home -v 1.8`
    $ java -jar ../lib/graphwalker-studio-4.3.0-SNAPSHOT.jar 


# In VS-Code run:
-----------------

# If required, make sure to use the correct java:
$ export JAVA_HOME=`/usr/libexec/java_home -v 1.8`

$ mvn clean test

$ export JAVA_HOME=`/usr/libexec/java_home -v 1.8`
$ mvn clean graphwalker:generate-test-sources test

=====================================================================================================

Run the player at:
------------------
file:///Users/oferr/workspace/GraphWalker_Workshop-Websocket_Client_Bst/ws_player/index.html?wsURI=localhost:8887



=====================================================================================================

