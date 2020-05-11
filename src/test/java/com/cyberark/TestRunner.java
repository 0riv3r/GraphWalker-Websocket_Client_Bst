package com.cyberark;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

import org.json.JSONObject;
import org.junit.jupiter.api.Test;

public class TestRunner {

    private BstTest testApp;
    private List<String> excludes;
    static private GraphWalkerWebSocketClient gw_wsc = new GraphWalkerWebSocketClient();;

    public TestRunner() {
        testApp = new BstTest();
        excludes = new ArrayList<String>();
        excludes.add("v_Start");
    }

    public void executeStateData(JSONObject stateData) {
        System.out.println(">>>>>>  stateData - vals = " + stateData.getString("vals"));
    }

    public void executeStateMethod(String methodName) {

        // System.out.println(">>>>>>  methodName:" + methodName);
        // System.out.println(">>>>>>  excludes:" + excludes);
        if(!excludes.contains(methodName)) {
 
            Method method = null;
            try {
                /**
                 * 'v_Added' is a different method since it sends data to the model
                 * It does it by calling the 'setData' method of GraphWalkerWebSocketClient
                 * For this, it has to get the GraphWalkerWebSocketClient object as parameter.
                 * All the other test methods don't get any parameters
                 */
                if(methodName.compareTo("v_Added") == 0) {
                    method = testApp.getClass().getMethod(methodName, GraphWalkerWebSocketClient.class);
                }
                else {
                    method = testApp.getClass().getMethod(methodName);
                }
            } catch (SecurityException e) {
                e.printStackTrace();
            } catch (NoSuchMethodException e) {
                e.printStackTrace();
            }

            try {
                if(methodName.compareTo("v_Added") == 0) {
                    method.invoke(testApp, gw_wsc);
                }
                else {
                    method.invoke(testApp);
                }
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
        gw_wsc.run();
        gw_wsc.loadModel(Paths.get("src/test/resources/com/cyberark/BstModel.json"));
        while (gw_wsc.hasNext()) {
            gw_wsc.getNext();
            gw_wsc.getData();
        }
        gw_wsc.close();
    }
    
}