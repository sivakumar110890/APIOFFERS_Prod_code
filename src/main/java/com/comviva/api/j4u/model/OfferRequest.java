package com.comviva.api.j4u.model;

/**
 * @author chandra.tekam
 */
public class OfferRequest {
    private String username;
    private String password;
    private Integer msisdn; //MSISDN
    private String category;
    private String channel;
    private String Language;
    private String RefNum;

    public OfferRequest() {
        super();
        // TODO Auto-generated constructor stub
    }

    public OfferRequest(String username, String password, Integer msisdn, String category, String channel,
                    String language, String refNum) {
        super();
        this.username = username;
        this.password = password;
        this.msisdn = msisdn;
        this.category = category;
        this.channel = channel;
        Language = language;
        RefNum = refNum;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Integer getMsisdn() {
        return msisdn;
    }

    public void setMsisdn(Integer msisdn) {
        this.msisdn = msisdn;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getChannel() {
        return channel;
    }

    public void setChannel(String channel) {
        this.channel = channel;
    }

    public String getLanguage() {
        return Language;
    }

    public void setLanguage(String language) {
        Language = language;
    }

    public String getRefNum() {
        return RefNum;
    }

    public void setRefNum(String refNum) {
        RefNum = refNum;
    }

    @Override
    public String toString() {
        return "OfferRequest [username=" + username + ", password=" + password + ", msisdn=" + msisdn + ", category="
                        + category + ", channel=" + channel + ", LANGUAGE=" + Language + ", REF_NUM=" + RefNum + "]";
    }

}
