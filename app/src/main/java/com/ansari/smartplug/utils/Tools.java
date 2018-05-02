package com.ansari.smartplug.utils;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;

/**
 * Created by Eabasir on 3/29/2016.
 */
public class Tools {

    static SimpleDateFormat _DateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");

    //    public static byte[] hexStringToByteArray(String s) {
//        try {
//            int len = s.length();
//            byte[] data = new byte[len / 2];
//
//            for (int i = 0; i < len; i += 2) {
//                data[i / 2] = (byte) ((Character.digit(s.charAt(i), 16) << 4) + Character.digit(s.charAt(i + 1), 16));
//            }
//            return data;
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//        return null;
//    }
    public static byte[] hexStringToByteArray(String s) {
        byte[] b = new byte[s.length() / 2];
        for (int i = 0; i < b.length; i++) {
            int index = i * 2;
            int v = Integer.parseInt(s.substring(index, index + 2), 16);
            b[i] = (byte) v;
        }
        return b;
    }

    final protected static char[] hexArray = {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'A', 'B', 'C', 'D', 'E', 'F'};

    public static String byteArrayToHexString(byte[] bytes) {
        char[] hexChars = new char[bytes.length * 2];
        int v;

        for (int j = 0; j < bytes.length; j++) {
            v = bytes[j] & 0xFF;
            hexChars[j * 2] = hexArray[v >>> 4];
            hexChars[j * 2 + 1] = hexArray[v & 0x0F];
        }

        return new String(hexChars);
    }

    public static int unsignedIntFromByteArray(byte[] bytes) {
        int res = 0;
        if (bytes == null)
            return res;

        for (int i = 0; i < bytes.length; i++) {
            res = (res * 10) + ((bytes[i] & 0xff));
        }
        return res;
    }


    public static boolean[] bytesToBooleans(byte[] bytes) {
        boolean[] bools = new boolean[bytes.length * 8];
        byte[] pos = new byte[]{(byte) 0x80, 0x40, 0x20, 0x10, 0x8, 0x4, 0x2, 0x1};

        for (int i = 0; i < bytes.length; i++) {
            for (int j = i * 8, k = 0; k < 8; j++, k++) {
                bools[j] = (bytes[i] & pos[k]) != 0;
            }
        }

        return bools;
    }

    public static int TwoByteToShort(byte[] input) {
        int i = ((input[0] << 8) & 0x0000ff00) | (input[1] & 0x000000ff);
        return i;
    }

    public static int OneByteToShort(byte[] input) {
        int i = ((input[0] & 0x000000ff));
        return i;
    }

    public static byte[] intToByteArray(int input) {
        return new byte[]{
                (byte) (input >>> 24),
                (byte) (input >>> 16),
                (byte) (input >>> 8),
                (byte) input};
    }


//    public static String bytesToHex(byte[] bytes) {
//        char[] hexChars = new char[bytes.length * 2];
//        for (int j = 0; j < bytes.length; j++) {
//            int v = bytes[j] & 0xFF;
//            hexChars[j * 2] = hexArray[v >>> 4];
//            hexChars[j * 2 + 1] = hexArray[v & 0x0F];
//        }
//        return new String(hexChars);
//    }

    public static Date byteArrayToDateTime(byte[] input, int mode) {
        Date date;

        int Second = -1;
        int Minute = -1;
        int Hour = -1;
        int Day = -1;
        int Month = -1;
        int Year = -1;

        String strSecond = "";
        String strMinute = "";
        String strHour = "";
        String strDay = "";
        String strMonth = "";

        byte in;

        try {

            in = input[0];
            Second = in & 0xFF;

            in = input[1];
            Minute = in & 0xFF;

            in = input[2];
            Hour = in & 0xFF;

            in = input[3];
            Day = in & 0xFF;

            in = input[4];
            Month = in & 0xFF;

            if (mode == 8) {
                byte[] slice = Arrays.copyOfRange(input, 5, 7);
                Year = TwoByteToShort(slice);
            }
            if (mode == 6) {
                byte[] slice = new byte[2];
                slice[1] = input[5];
                slice[0] = (byte) 0x07;
                Year = TwoByteToShort(slice);


            }

            strSecond = Second < 10 ? "0" + Second : Second + "";
            strMinute = Minute < 10 ? "0" + Minute : Minute + "";
            strHour = Hour < 10 ? "0" + Hour : Hour + "";
            strDay = Day < 10 ? "0" + Day : Day + "";
            strMonth = Month < 10 ? "0" + Month : Month + "";


            date = _DateFormat.parse(Year + "/" + strMonth + "/" + strDay + " " + strHour + ":" + strMinute + ":" + strSecond);
            return date;
        } catch (ParseException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }

    public static String DateTimeToString8Mode(int Year, int Month, int Day, int Hour, int Minute, int Second) {
        byte[] query = new byte[8];

        byte b;

        b = (byte) (Second);
        query[0] = b;

        b = (byte) (Minute);
        query[1] = b;

        b = (byte) (Hour);
        query[2] = b;

        b = (byte) (Day);
        query[3] = b;

        b = (byte) (Month);
        query[4] = b;


        b = (byte) (Year >> 8);
        query[5] = b;

        b = (byte) (Year /*>> 0*/);
        query[6] = b;


        return byteArrayToHexString(query);
    }

    public static String DateTimeToString6Mode(int Year, int Month, int Day, int Hour, int Minute, int Second) {
        byte[] query = new byte[6];

        byte b;

         b = (byte) (Second);
        query[0] = b;

        b = (byte) (Minute);
        query[1] = b;

        b = (byte) (Hour);
        query[2] = b;

        b = (byte) (Day);
        query[3] = b;

        b = (byte) (Month);
        query[4] = b;

        b = (byte) (Year);
        query[5] = b;


        return byteArrayToHexString(query);
    }


    public static Date getDateFromString(String s) {

        try

        {

            return _DateFormat.parse(s);

        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static String getStringFromDate(Date date) {
        try {

            return _DateFormat.format(date);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }


}
