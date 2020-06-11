package com.hyhello.priceless.support.runtime;

import lombok.extern.slf4j.Slf4j;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

@Slf4j
public class RuntimeSupport {

    public static List<String> exec(String command, long timeoutMills, Consumer<List<String>> c) throws IOException{

        log.debug("Runtime Start , command = : " + command);

        Process process = Runtime.getRuntime().exec(command);
        process.getOutputStream();

        InputStream fisc = null;
        InputStreamReader isrs = null;
        BufferedReader brs = null;
        fisc = process.getInputStream();
        isrs = new InputStreamReader(fisc);
        brs = new BufferedReader(isrs);

        List<String> lines = new ArrayList<String>();
        String line;
        while ((line = brs.readLine()) != null) {
            log.debug("Runtime executing: " + line);
            lines.add(line);
        }

        log.debug("Runtime complete , command = : " + command);

        if (c != null){
            c.accept(lines);
        }
        return lines;
    }

    public static List<String> exec(String command, long timeoutMills) throws IOException{
        return exec(command, timeoutMills, null);
    }

    public static List<String> exec(AbstractRuntimeTask runtimeTask) throws IOException{
        return exec(runtimeTask.getCommand(), runtimeTask.getTimeoutMillis(), null);
    }
}
