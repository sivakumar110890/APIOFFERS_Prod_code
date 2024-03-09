package com.comviva.api.j4u.model;

import java.util.ArrayList;
import java.util.List;

public class RankingFormulae {
    
	private boolean aaEligible;
    private String prefPayMethod;
    private float aValue;
    private long airtimeBalance;
    private long aaBalance;
    private List<OfferParams> offerParams;
    private String cellId;
    private String poolId;

    public String getPoolId() {
        return poolId;
    }

    public void setPoolId(String poolId) {
        this.poolId = poolId;
    }

    public String getCellId() {
        return cellId;
    }

    public void setCellId(String cellId) {
        this.cellId = cellId;
    }

    public boolean isAaEligible() {
        return aaEligible;
    }

    public void setAaEligible(boolean aaEligible) {
        this.aaEligible = aaEligible;
    }

    public String getPrefPayMethod() {
        return prefPayMethod;
    }

    public void setPrefPayMethod(String prefPayMethod) {
        this.prefPayMethod = prefPayMethod;
    }

    public float getaValue() {
        return aValue;
    }

    public void setaValue(float aValue) {
        this.aValue = aValue;
    }

    public float getAirtimeBalance() {
        return airtimeBalance;
    }

    public void setAirtimeBalance(long airtimeBalance) {
        this.airtimeBalance = airtimeBalance;
    }

    public float getAaBalance() {
        return aaBalance;
    }

    public void setAaBalance(long aaBalance) {
        this.aaBalance = aaBalance;
    }

    public List<OfferParams> getOfferParams() {
        return new ArrayList<>(offerParams);
    }

    public void setOfferParams(List<OfferParams> offerParams) {
        this.offerParams = new ArrayList<>(offerParams);
    }
    
    @Override
	public String toString() {
		return "RankingFormulae [aaEligible=" + aaEligible + ", prefPayMethod=" + prefPayMethod + ", aValue=" + aValue
				+ ", airtimeBalance=" + airtimeBalance + ", aaBalance=" + aaBalance + ", offerParams=" + offerParams
				+ ", cellId=" + cellId + ", poolId=" + poolId + "]";
	}


}
