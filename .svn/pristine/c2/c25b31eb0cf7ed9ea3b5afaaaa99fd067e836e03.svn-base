package com.comviva.api.j4u.model;

import org.json.JSONObject;

public class HTTPResponse {

    private int status;
    private String response;
    private String output;
    private long responseTime;
    private String sentTime;
    private String recvTime;
    private JSONObject responseJSON;

    public HTTPResponse() {
        super();
    }

    public HTTPResponse(int status, String response) {
        super();
        this.status = status;
        this.response = response;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getResponse() {
        return response;
    }

    public void setResponse(String response) {
        this.response = response;
    }

    public String getOutput() {
        return output;
    }

    public void setOutput(String output) {
        this.output = output;
    }

    public long getResponseTime() {
        return responseTime;
    }

    public void setResponseTime(long responseTime) {
        this.responseTime = responseTime;
    }

    public String getSentTime() {
        return sentTime;
    }

    public void setSentTime(String sentTime) {
        this.sentTime = sentTime;
    }

    public String getRecvTime() {
        return recvTime;
    }

    public void setRecvTime(String recvTime) {
        this.recvTime = recvTime;
    }

    @Override
    public String toString() {
        return "HTTPResponse [status=" + status + ", response=" + response + ", output=" + output + ", responseTime="
                        + responseTime + ", sentTime=" + sentTime + ", recvTime=" + recvTime + ", responseJSON=" + responseJSON
                        + "]";
    }

    public JSONObject getResponseJSON() {
        return responseJSON;
    }

    public void setResponseJSON(JSONObject responseJSON) {
        this.responseJSON = responseJSON;
    }
}