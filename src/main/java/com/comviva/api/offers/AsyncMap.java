package com.comviva.api.offers;

import javax.servlet.AsyncContext;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class AsyncMap {

    private Map<String, AsyncContext> mGetOffersAsync = new ConcurrentHashMap<String, AsyncContext>();
    private Map<String, AsyncContext> mActivateOffersAsync = new ConcurrentHashMap<String, AsyncContext>();

    public static AsyncMap instance() {
        return InstanceHolder.INSTANCE;
    }

    public AsyncContext getActOffer(String key) {
        return this.mActivateOffersAsync.get(key);
    }

    public void putActOffer(String key, AsyncContext value) {
        this.mActivateOffersAsync.put(key, value);
    }

    public void removeActOffer(String key) {
        this.mActivateOffersAsync.remove(key);
    }

    public AsyncContext get(String key) {
        return this.mGetOffersAsync.get(key);
    }

    public void put(String key, AsyncContext value) {
        this.mGetOffersAsync.put(key, value);
    }

    public void remove(String key) {
        this.mGetOffersAsync.remove(key);
    }

    private static class InstanceHolder {
        public static final AsyncMap INSTANCE = new AsyncMap();
    }

}
