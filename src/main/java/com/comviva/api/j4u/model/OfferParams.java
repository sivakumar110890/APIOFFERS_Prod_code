package com.comviva.api.j4u.model;

public class OfferParams {

    private float expectedValue;
    private long offerPrice;
    private String offerId;
    private float bValue;
    private float cValue;
    private float rfValue;
    private String productDescription;

    public float getExpectedValue() {
        return expectedValue;
    }

    public void setExpectedValue(float expectedValue) {
        this.expectedValue = expectedValue;
    }

    public long getOfferPrice() {
        return offerPrice;
    }

    public void setOfferPrice(long offerPrice) {
        this.offerPrice = offerPrice;
    }

    public String getOfferId() {
        return offerId;
    }

    public void setOfferId(String offerId) {
        this.offerId = offerId;
    }

    public float getbValue() {
        return bValue;
    }

    public void setbValue(float bValue) {
        this.bValue = bValue;
    }

    public float getcValue() {
        return cValue;
    }

    public void setcValue(float cValue) {
        this.cValue = cValue;
    }

    public float getRfValue() {
        return rfValue;
    }

    public void setRfValue(float d) {
        this.rfValue = d;
    }

    public String getProductDescription() {
        return productDescription;
    }

    public void setProductDescription(String productDescription) {
        this.productDescription = productDescription;
    }

    @Override public String toString() {
        return "OfferParams{" +
                        "expectedValue=" + expectedValue +
                        ", offerPrice=" + offerPrice +
                        ", offerId='" + offerId + '\'' +
                        ", bValue=" + bValue +
                        ", cValue=" + cValue +
                        ", rfValue=" + rfValue +
                        ", productDescription='" + productDescription + '\'' +
                        '}';
    }
}
