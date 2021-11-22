package com.wispr.wispr.util;

public class ImageExtension {


    public static String fileExtension(String path) {
        char[] filepath = path.toCharArray();
        String output = "";
        for (int i = filepath.length - 1; i >= filepath.length - 3; i--) {
            if (filepath[i] != '.') {
                output += String.valueOf(filepath[i]);
            }
        }
        output = output + ".";
        filepath = output.toCharArray();
        String newoutput = "";
        for (int i = filepath.length - 1; i >= 0; i--) {
            newoutput = newoutput + String.valueOf(filepath[i]);
        }

        return newoutput;
    }
}
