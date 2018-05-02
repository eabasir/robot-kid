package com.ansari.smartplug.network;

/**
 * Created by Eabasir on 3/30/2016.
 */
public class  NetConfig {


    public final static String DEVICE_IP = "192.168.11.254";
    public final static int DEVICE_PORT = 3000;


    public final static String CMD_MOVE_RIGTH = "0000WMRF";
    public final static String CMD_MOVE_LEFT = "0000WMLF";
    public final static String CMD_MOVE_FORWARD = "0000WMFF";
    public final static String CMD_MOVE_BACKWARD = "0000WMBF";

    public final static String CMD_READ_SWITCH = "0000RS";



    public  enum TYPE {
        Forward,
        Right,
        Left,
        Backward,
        ReadSwitch


    }


}
