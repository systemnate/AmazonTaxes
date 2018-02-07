package com.itsridic.amazon;

public class TaxEntity
{
  protected String jurisdictionLevel;
  protected String jurisdictionName;
  protected double taxAmount;
  protected double taxedJurisdictionTaxRate;
  protected String taxType;
  protected String taxCalculationReasonCode;
  protected double taxableAmount;
  protected double nonTaxableAmount;
  
  public TaxEntity(String jurisdictionLevel, String jurisdictionName, String taxAmount, String taxedJurisdictionTaxRate, String taxType, String taxCalculationReasonCode, String taxableAmount, String nonTaxableAmount)
  {
    this.jurisdictionLevel = jurisdictionLevel;
    this.jurisdictionName = jurisdictionName;
    this.taxAmount = Double.valueOf(taxAmount).doubleValue();
    this.taxedJurisdictionTaxRate = Double.valueOf(taxedJurisdictionTaxRate).doubleValue();
    this.taxType = taxType;
    this.taxCalculationReasonCode = taxCalculationReasonCode;
    this.taxableAmount = Double.valueOf(taxableAmount).doubleValue();
    this.nonTaxableAmount = Double.valueOf(nonTaxableAmount).doubleValue();
  }
  
  public String toString()
  {
    return "$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$\nJurisdiction Level    : " + this.jurisdictionLevel + "\n" + "Jurisdiction Name     : " + this.jurisdictionName + "\n" + "Tax Amount            : " + this.taxAmount + "\n" + "Jurisdiction Tax Rate : " + this.taxedJurisdictionTaxRate + "\n" + "Tax Type              : " + this.taxType + "\n" + "Tax Calculation Reason: " + this.taxCalculationReasonCode + "\n" + "Taxable Amount        : " + this.taxableAmount + "\n" + "Non-Taxable Amount    : " + this.nonTaxableAmount + "\n" + "$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$\n";
  }
}
