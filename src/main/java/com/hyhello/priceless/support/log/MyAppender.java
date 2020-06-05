package com.hyhello.priceless.support.log;

import ch.qos.logback.classic.encoder.PatternLayoutEncoder;
import ch.qos.logback.classic.spi.ILoggingEvent;
import ch.qos.logback.classic.spi.LoggingEvent;
import ch.qos.logback.core.AppenderBase;
import org.slf4j.MarkerFactory;

//注册回调
public class MyAppender extends AppenderBase<ILoggingEvent>{

    private  PatternLayoutEncoder encoder;

    @Override
    public void start() {
        if (this.encoder == null) {
            addError("No encoder set for the appender named ["+ name +"].");
            return;
        }

        super.start();
    }

    public void append(ILoggingEvent event) {
        // output the events as formatted by our layout
        byte[] bytes;
        if (event instanceof LoggingEvent){
            LoggingEvent loggingEvent = (LoggingEvent) event;
            loggingEvent.setMarker(MarkerFactory.getMarker("SYSTEM"));
            event = loggingEvent;
        }
        bytes = this.encoder.encode(event);
        LogObservable.getInstance().update(event.getMarker(),new String(bytes));

    }

    public PatternLayoutEncoder getEncoder() {
        return encoder;
    }

    public void setEncoder(PatternLayoutEncoder encoder) {
        this.encoder = encoder;
    }

}
