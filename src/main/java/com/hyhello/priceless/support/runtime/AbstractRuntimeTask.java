package com.hyhello.priceless.support.runtime;

import java.io.IOException;
import java.util.List;
import java.util.function.Consumer;


public abstract class AbstractRuntimeTask {

    public abstract String getcmd();
    public abstract long getTimeoutMillis();
    public abstract Consumer<List<String>> getCallback();

    public void doexec() throws IOException{
        RuntimeSupport.exec(getcmd(), getTimeoutMillis(), getCallback());
    }
}
