package com.comviva.api.async;

import org.apache.log4j.Logger;

import javax.servlet.AsyncContext;
import javax.servlet.AsyncEvent;
import javax.servlet.AsyncListener;
import javax.servlet.annotation.WebListener;
import java.io.IOException;
import java.io.PrintWriter;

@WebListener
public class AppAsyncListener implements AsyncListener {
    public static final Logger LOGGER = Logger.getLogger(AppAsyncListener.class);
     private PrintWriter out;

    @Override
    public void onComplete(AsyncEvent asyncEvent) throws IOException {
        LOGGER.info("AppAsyncListener onComplete");
    }

    @Override
    public void onError(AsyncEvent asyncEvent) throws IOException {
        LOGGER.error("AppAsyncListener onError");
        AsyncContext asyncCtx;
        try {
            asyncCtx = asyncEvent.getAsyncContext();
            out = asyncCtx.getResponse().getWriter();
            StringBuilder sb = new StringBuilder();
            sb.append("{");
            sb.append("\"StatusCode\": \"1\",");
            sb.append("\"StatusMessage\": \"Error in processing your request\"");
            sb.append("}");
            out.println(sb.toString());
        } catch (Exception e) {
            LOGGER.error("AppAsyncListener onError Exception - ", e);
        }
    }

    @Override
    public void onStartAsync(AsyncEvent asyncEvent) throws IOException {
        LOGGER.info("AppAsyncListener onStartAsync");
    }

    @Override
    public void onTimeout(AsyncEvent asyncEvent) throws IOException {
        LOGGER.error("AppAsyncListener onTimeout");
        AsyncContext asyncCtx;
        try {
            asyncCtx = asyncEvent.getAsyncContext();
            out = asyncCtx.getResponse().getWriter();
            StringBuilder sb = new StringBuilder();
            sb.append("{");
            sb.append("\"StatusCode\": \"2\",");
            sb.append("\"StatusMessage\": \"Timeout on processing your request\"");
            sb.append("}");
            out.println(sb.toString());
        } catch (Exception e) {
            LOGGER.error("AppAsyncListener onTimeout Exception - ", e);
        }
    }

}
