package common;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by Mateusz on 17.12.2016.
 */
public class ServerRequest {
    public static final String NO_CONNECTION = "No device connected to application";
    public static final String CONNECTED_DEVICE = "Connected device: ";
    public static final String CONNECT = "CONNECT";
    public static final String DISCONNECT = "DISCONNECT";
    public static final String SERVER_SHUTDOWN = "SERVER SHUTDOWN";
    public static final String CHECK_USERNAME = "CHECK USERNAME";
    public static final String END = "END";
    public static final String OK = "OK";
    public static final String TRUE = "TRUE";
    public static final String FALSE = "FALSE";
    public static final String START_RACE = "START";
    public static final String END_RACE = "FINISH";
    public static final String USERNAME = "USERNAME";

    public static List<String> fieldsValue(){

        List<String> allValues = new ArrayList<>();

        for (Field f : ServerRequest.class.getFields()){
            try {
               allValues.add(f.get(null).toString());
            } catch (IllegalAccessException e) {
               e.printStackTrace();
            }
        }

        return allValues;
    }
}
