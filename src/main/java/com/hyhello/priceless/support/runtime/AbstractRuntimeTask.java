package com.hyhello.priceless.support.runtime;

import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.function.Consumer;

@Slf4j
public abstract class AbstractRuntimeTask {

    public abstract String getCommand();

    public abstract long getTimeoutMillis();
}