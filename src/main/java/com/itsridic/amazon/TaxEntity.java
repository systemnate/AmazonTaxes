package com.itsridic.amazon;

public class TaxEntity {
	private String jurisdictionLevel;
	private String jurisdictionName;
	private double taxAmount;
	private double taxedJurisdictionTaxRate;
	private String taxType;
	private String taxCalculationReasonCode;
	private double taxableAmount;
	private double nonTaxableAmount;
	
	public TaxEntity(String jurisdictionLevel, String jurisdictionName, String taxAmount,
			String taxedJurisdictionTaxRate, String taxType, String taxCalculationReasonCode, String taxableAmount,
			String nonTaxableAmount) {
		this.jurisdictionLevel = jurisdictionLevel;
		this.jurisdictionName = jurisdictionName;
		this.taxAmount = Double.valueOf(taxAmount);
		this.taxedJurisdictionTaxRate = Double.valueOf(taxedJurisdictionTaxRate);
		this.taxType = taxType;
		this.taxCalculationReasonCode = taxCalculationReasonCode;
		this.taxableAmount = Double.valueOf(taxableAmount);
		this.nonTaxableAmount = Double.valueOf(nonTaxableAmount);
	}
	
	public String toString() {
		return "$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$\n" +
			   "Jurisdiction Level    : " + jurisdictionLevel + "\n" + 
			   "Jurisdiction Name     : " + jurisdictionName + "\n" +
			   "Tax Amount            : " + taxAmount + "\n" +
			   "Jurisdiction Tax Rate : " + taxedJurisdictionTaxRate + "\n" +
			   "Tax Type              : " + taxType + "\n" +
			   "Tax Calculation Reason: " + taxCalculationReasonCode + "\n" +
			   "Taxable Amount        : " + taxableAmount + "\n" +
			   "Non-Taxable Amount    : " + nonTaxableAmount + "\n" +
			   "$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$\n";
	}
}
