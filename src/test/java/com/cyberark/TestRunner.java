package com.cyberark;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.Test;

public class TestRunner {

    private BstTest testApp;
    private List<String> excludes;

    public TestRunner() {
        testApp = new BstTest();
        excludes = new ArrayList<String>();
        excludes.add("v_Start");
    }

    public void executeMethod(String methodName) {

        System.out.println(">>>>>>  methodName:" + methodName + "!");
        System.out.println(">>>>>>  excludes:" + excludes + "!");
        if(!excludes.contains(methodName)) {

            Method method = null;
            try {
                method = testApp.getClass().getMethod(methodName);
            } catch (SecurityException e) {
                e.printStackTrace();
            } catch (NoSuchMethodException e) {
                e.printStackTrace();
            }

            try {
                method.invoke(testApp);
            } catch (IllegalArgumentException e) {
                e.printStackTrace();
            }
            catch (IllegalAccessException e) {
                e.printStackTrace();
            }
            catch (InvocationTargetException e) {
                e.printStackTrace();
            }
        }
    }

    @Test
    public void runFunctionalTest() {

        GraphWalkerWebSocketClient gw_wsc = new GraphWalkerWebSocketClient();
        gw_wsc.run();
        gw_wsc.loadModel(Paths.get("src/test/resources/com/cyberark/BstModel.json"));
        while (gw_wsc.hasNext()) {
            gw_wsc.getNext();
            gw_wsc.getData();
        }
        gw_wsc.close();
    }
    
}