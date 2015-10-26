package com.geophoto2.jaroslav.mapapp;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

/**
 * Created by 7 on 24.10.2015.
 */
public class ReaderDataFrom {
    public static String readItFromInputStream(BufferedInputStream stream) throws IOException {

        BufferedReader reader = new BufferedReader(new InputStreamReader(stream, "UTF-8"));
        StringBuilder buffer = new StringBuilder();
        String line;

        while ((line = reader.readLine()) != null) {
            buffer.append(line);
        }

        return buffer.toString();
    }
}
