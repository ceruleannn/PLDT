package com.hyhello.priceless.support.runtime;

import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.util.List;
import java.util.function.Consumer;

@Slf4j
public abstract class AbstractRuntimeTask implements Runnable{

    public abstract String getcmd();
    public abstract long getTimeoutMillis();
    public abstract Consumer<List<String>> getCallback();

    public void run() {
        try {
            RuntimeSupport.exec(getcmd(), getTimeoutMillis(), getCallback());
        } catch (IOException e) {
            log.error("IOException in AbstractRuntimeTask", e);
        }
    }
}
