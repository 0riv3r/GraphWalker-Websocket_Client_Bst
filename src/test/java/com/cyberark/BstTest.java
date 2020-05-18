package com.cyberark;


import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;

import java.io.IOException;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Random;
import java.util.Set;
import java.util.Stack;

import org.graphwalker.core.machine.ExecutionContext;
import org.graphwalker.java.annotation.GraphWalker;
import org.graphwalker.java.test.TestExecutionException;
import org.graphwalker.java.test.TestExecutor;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

//@GraphWalker(value = "random(edge_coverage(100))", start = "e_Init")
@GraphWalker(value = "random(time_duration(20))", start = "e_Init")
public class BstTest  extends ExecutionContext implements BstModel {

    private static final Logger logger = LoggerFactory.getLogger(BstTest.class);
    
    private WebSocketClient client = new WebSocketClient();

    private Bst<Integer> bst;
    private ArrayList<Integer> vals; // values to be inserted in the tree
    private ArrayList<Integer> fakeVals; // values that are not inserted in the tree
    private HashSet<Integer> inTree; // the current values in the tree, use set to avoid duplicates (like the tree
                                     // does)
    private Stack<Integer> nodesStack; // used to pop leaves in deletion test, in order to delete leaves
    private Random rand;
    private boolean boolResult;

    @Override
    public void e_Add() {
        System.out.println("e_Add");
        int val = vals.get(rand.nextInt(vals.size()));
        bst.add(val);
        if (inTree.add(val)) // Add to the stack only if succeeded to add to the set
            nodesStack.push(val);

        String jsToSetData = "vals=" + String.valueOf(inTree.size()) + ";";
        System.out.println("in v_MenuDispatcher, jsToSetData:  " + jsToSetData);
        client.setData(jsToSetData);
    }

    @Override
    public void e_Find() {
        System.out.println("e_Find");

        System.out.println("bst.nodes: " + Arrays.toString(bst.nodes().toArray()));
        System.out.println("inTree: " + Arrays.toString(inTree.toArray()));
        System.out.println("nodesStack: " + Arrays.toString(nodesStack.toArray()));

        // convert HashSet to an array to fetch element by random index
        Integer[] arrInTreeVals = inTree.toArray(new Integer[inTree.size()]);
        int randomIndex = rand.nextInt(inTree.size());
        boolResult = bst.find(arrInTreeVals[randomIndex]);
    }

    @Override
    public void e_FindFakeVal() {
        System.out.println("e_FindFakeVal");
        boolResult = bst.find(fakeVals.get(rand.nextInt(fakeVals.size())));
    }

    @Override
    public void e_Init() {
        System.out.println("e_Init");

        client.run();
        client.startMachine(Paths.get("src/test/resources/com/cyberark/BstModel.json"));

        bst = new Bst<Integer>();
        vals = new ArrayList<Integer>(Arrays.asList(1, 3, 4, 6, 7, 8, 10, 13, 14));
        fakeVals = new ArrayList<Integer>(Arrays.asList(21, 23, 24, 26, 27, 28, 30, 33, 34));
        inTree = new HashSet<Integer>();
        nodesStack = new Stack<Integer>();
        rand = new Random();
        boolResult = false;
    }

    @Override
    public void v_Init() {
        System.out.println("v_Init");
        assertNotNull(bst);
        assertNotNull(vals);
        assertNotNull(fakeVals);
        assertNotNull(inTree);
        assertNotNull(rand);
        assertEquals(false, boolResult);

        // System.out.println( "bst.nodes: " + Arrays.toString(bst.nodes().toArray()));
        // System.out.println( "inTree: " + Arrays.toString(inTree.toArray()));
        // System.out.println( "nodesStack: " + Arrays.toString(nodesStack.toArray()));
    }

    @Override
    public void e_Delete() {
        System.out.println("e_Delete");

        System.out.println("bst.nodes: " + Arrays.toString(bst.nodes().toArray()));
        System.out.println("inTree: " + Arrays.toString(inTree.toArray()));
        System.out.println("nodesStack: " + Arrays.toString(nodesStack.toArray()));

        // The last inserted value is a leaf and should be deleted
        int valToDelete = nodesStack.pop();
        inTree.remove(valToDelete);
        bst.delete(valToDelete);

        String jsToSetData = "vals=" + String.valueOf(inTree.size()) + ";";
        System.out.println("in v_MenuDispatcher, jsToSetData:  " + jsToSetData);
        client.setData(jsToSetData);
    }

    @Override
    public void e_GetNodes() {
        System.out.println("e_GetNodes");
        Set<Integer> expectedNodes = new HashSet<Integer>();
        expectedNodes.addAll(inTree);
        if (expectedNodes.size() == bst.nodes().size()) {
            expectedNodes.removeAll(bst.nodes());
            boolResult = (expectedNodes.size() == 0);
        } else {
            boolResult = false;
        }
    }

    @Override
    public void e_ToMenu() {
        System.out.println("e_ToMenu");
        // throw new RuntimeException( "e_ToMenu is not implemented yet!" );
        String jsToSetData = "vals=" + String.valueOf(inTree.size()) + ";";
        System.out.println("in v_MenuDispatcher, jsToSetData:  " + jsToSetData);
        client.setData(jsToSetData);
    }

    @Override
    public void v_Added() {
    // public void v_Added(WebSocketClient gw_wsc) {
        System.out.println("v_Added");

        System.out.println("bst.nodes: " + Arrays.toString(bst.nodes().toArray()));
        System.out.println("inTree: " + Arrays.toString(inTree.toArray()));
        System.out.println("nodesStack: " + Arrays.toString(nodesStack.toArray()));
       
         /**
         *  Set the model's data - number of vals to the current number of nodes in the test 
         *  This is because the model doesn't know if a value was added or not
         *  A value will not be added if it is a duplicate
         *  The number of vals in the model must be synced with the number of nodes in the test
         *  Otherwise, the model's guards will not function as they should
         *  The model's guard for instance will enable e_Delete when the tree is empty
         * */ 
        String jsToSetData = "vals=" + String.valueOf(inTree.size()) + ";";
        System.out.println("in v_Added, jsToSetData:  " + jsToSetData);
        client.setData(jsToSetData);

        assertEquals(inTree.size(), bst.nodes().size());
    }

    @Override
    public void v_Found() {
        System.out.println("v_Found");
        assertTrue(boolResult, "Find failed!");
    }

    @Override
    public void v_NotFound() {
        System.out.println("v_NotFound");
        assertFalse(boolResult, "Found a faked value!");
    }

    @Override
    public void v_Deleted() {
        System.out.println("v_Deleted");

        System.out.println("bst.nodes: " + Arrays.toString(bst.nodes().toArray()));
        System.out.println("inTree: " + Arrays.toString(inTree.toArray()));
        System.out.println("nodesStack: " + Arrays.toString(nodesStack.toArray()));

        String jsToSetData = "vals=" + String.valueOf(inTree.size()) + ";";
        System.out.println("in v_MenuDispatcher, jsToSetData:  " + jsToSetData);
        client.setData(jsToSetData);

        assertEquals(inTree.size(), bst.nodes().size());
    }

    @Override
    public void v_NodesList() {
        System.out.println("v_NodesList");
        assertTrue(boolResult);
    }

    @Override
    public void v_Start() {
        System.out.println("v_Start");
    }

    @Override
    public void v_MenuDispatcher() {
        System.out.println("v_MenuDispatcher");
        // throw new RuntimeException( "v_MenuDispatcher is not implemented yet!" );
        String jsToSetData = "vals=" + String.valueOf(inTree.size()) + ";";
        System.out.println("in v_MenuDispatcher, jsToSetData:  " + jsToSetData);
        client.setData(jsToSetData);
    }

    // *******************************************************************
    // ****     run GraphWalker online test as WebSocket service      ****
    // *******************************************************************

    // # in another terminal/PuTTy , lunch the GraphWalker Websocket service and load the model:

    /*
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


        $ java -jar ../lib/graphwalker-cli-4.3.0-SNAPSHOT.jar --debug all online

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
    */

}
