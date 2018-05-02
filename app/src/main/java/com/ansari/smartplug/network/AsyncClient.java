package com.ansari.smartplug.network;

import java.io.BufferedInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.lang.Override;
import java.lang.String;
import java.lang.Void;
import java.net.Socket;
import java.net.UnknownHostException;

import android.os.AsyncTask;
import android.util.Log;

import com.ansari.smartplug.App;
import com.ansari.smartplug.fragments.DemoFragment;
import com.ansari.smartplug.interfaces.OnSwitchReadListener;

public class AsyncClient {

    private String dstAddress;
    private int dstPort;

    private OutputStream out = null;
    private DataOutputStream dos = null;
    private InputStream input = null;
    private byte[] recData = new byte[1024];
    private Socket socket;
    private byte[] cmd;
    private boolean doWork;

    public AsyncClient(String addr, int port) {
        this.dstAddress = addr;
        this.dstPort = port;
        doWork = true;

        new Thread(new Runnable() {
            @Override
            public void run() {

                try {
                    socket = new Socket(dstAddress, dstPort);
                    socket.setSoTimeout(1000000);

                    out = socket.getOutputStream();
                    dos = new DataOutputStream(out);

                    while (true) {

                        if (doWork) {

                            if (cmd != null && cmd.length > 0) {

                                dos.flush();
                                dos.write(cmd, 0, cmd.length);

                                input = new BufferedInputStream(socket.getInputStream());

                                input.read(recData);

                                String str = new String(recData, "UTF-8");

                                if (str.contains("SW=")) {

                                    String[] parts = str.replace("SW=", "").split(",");
                                    if (parts.length > 0) {

                                        final boolean[] results = new boolean[2];
                                        results[0] = parts[0].equals("1");
                                        results[1] = parts[1].equals("1");

                                        App.getInstance().runOnUiThread(new Runnable() {
                                            @Override
                                            public void run() {

                                                for (OnSwitchReadListener onDeviceDateTimeWriteListener : App
                                                        .getInstance().getUIListeners(
                                                                OnSwitchReadListener.class))
                                                    onDeviceDateTimeWriteListener.onSwitchRead(results[0], results[1]);

                                            }
                                        });

                                    }
                                }
                               cmd = null;
                            }


                        } else
                            break;


                    }

                } catch (UnknownHostException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();

                } catch (Exception e) {
                    e.printStackTrace();

                } finally {
                    finish();
                }

            }
        }).start();


    }


    public void startSendAction(NetConfig.TYPE type) {
        cmd = getCommandBytes(type);
        Log.v("TAG", type.toString());

    }


    private byte[] getCommandBytes(NetConfig.TYPE type) {

        byte[] dtsData;

        switch (type) {
            case Forward:
                dtsData = NetConfig.CMD_MOVE_FORWARD.getBytes();
                break;
            case Backward:
                dtsData = NetConfig.CMD_MOVE_BACKWARD.getBytes();
                break;
            case Right:
                dtsData = NetConfig.CMD_MOVE_RIGTH.getBytes();
                break;
            case Left:
                dtsData = NetConfig.CMD_MOVE_LEFT.getBytes();
                break;
            case ReadSwitch:
                dtsData = NetConfig.CMD_READ_SWITCH.getBytes();
                break;
            default:
                dtsData = null;

        }
        return dtsData;
    }

    public void finish() {

        doWork = false;
        try {
            if (socket != null) {
                socket.close();
            }
            if (out != null) {
                out.flush();
                out.close();
            }

            if (dos != null) {
                dos.flush();
                dos.close();
            }


            if (input != null) {
                input.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
