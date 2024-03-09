package com.comviva.api.j4u.model;

import java.io.UnsupportedEncodingException;
import java.util.List;

public class InboundUSSDMessage {

    private String clobString;
    private int incomingLabel;
    private String prodIds;
    private String selProdId;

    public String getClobString() {
        return clobString;
    }

    public void setClobString(String clobString) throws UnsupportedEncodingException {
        this.clobString = clobString.replaceAll("��|`|���", "'");
    }

    public int getIncomingLabel() {
        return incomingLabel;
    }

    public void setIncomingLabel(int incomingLabel) {
        this.incomingLabel = incomingLabel;
    }

    public String getProdIds() {
        return prodIds;
    }

    public void setProdIds(List<String> prodIds) {
        this.prodIds = prodIds.toString();
    }

    public String getSelProdId() {
        return selProdId;
    }

    public void setSelProdId(String selProdId) {
        this.selProdId = selProdId;
    }

}