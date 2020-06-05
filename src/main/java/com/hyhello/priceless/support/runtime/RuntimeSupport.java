package com.hyhello.priceless.support.runtime;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

public class RuntimeSupport {

    public static void exec(String command, long timeoutMills, Consumer<List<String>> c) throws IOException{ 
        c.accept(exec(command, timeoutMills));
    }
    
    public static List<String> exec(String command, long timeoutMills) throws IOException{
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
            lines.add(line);
        }
        
        return lines;
    }
}
