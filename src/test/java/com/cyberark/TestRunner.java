package com.cyberark;

import static org.hamcrest.CoreMatchers.is;

import org.graphwalker.java.test.Executor;
import org.graphwalker.java.test.Result;
import org.graphwalker.websocket.WebSocketServer;

import org.graphwalker.core.generator.SingletonRandomGenerator;
import org.graphwalker.core.machine.ExecutionContext;
import org.graphwalker.java.annotation.BeforeExecution;
import org.graphwalker.java.annotation.GraphWalker;
import org.graphwalker.java.test.TestExecutionException;
import org.graphwalker.java.test.TestExecutor;

import org.java_websocket.WebSocket;
import org.junit.jupiter.api.Test;
import java.io.IOException;

public class TestRunner {

    @Test
    public void testRun() throws IOException {

        Executor executor = new TestExecutor(BstTest.class);

        WebSocketServer server = new WebSocketServer(8887, executor.getMachine());
        server.start();

        Result result = executor.execute(true);
        if (result.hasErrors()) {
            for (String error : result.getErrors()) {
                System.out.println(error);
            }
        }
        System.out.println("Done: [" + result.getResults().toString(2) + "]");
    }
    
}