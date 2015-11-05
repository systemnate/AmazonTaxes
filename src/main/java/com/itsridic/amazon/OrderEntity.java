package com.itsridic.amazon;

import java.io.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import org.apache.commons.csv.*;

public class OrderEntity {
  private String  orderId;
  private Date    orderDate;
  private String  shipmentId;
  private Date    shipmentDate;
  private Date    taxCalculatedDateUTC;
  private Address shippedFrom;
  private Address shippedTo;
  private ArrayList taxes;

  public OrderEntity(String orderId, String orderDate, String shipmentId, String shipmentDate, String taxCalculatedDateUTC,
		Address shippedFrom, Address shippedTo) throws ParseException {
		
	this.orderId = orderId;
	this.orderDate = new SimpleDateFormat("yyyy-MM-dd").parse(orderDate.replaceAll("\\+.+", "").toString());
	this.shipmentId = shipmentId;
	this.shipmentDate = new SimpleDateFormat("yyyy-MM-dd").parse(shipmentDate.replaceAll("\\+.+", "").toString());
	this.taxCalculatedDateUTC = new SimpleDateFormat("yyyy-MM-dd").parse(taxCalculatedDateUTC.replaceAll("\\+.+", "").toString());
	this.shippedFrom = shippedFrom;
	this.shippedTo = shippedTo;
	this.taxes = new ArrayList<TaxEntity>();
}

public String toString() {
    String returnString =
    		"*********************************************\n" + 
            "Order ID      : " + orderId      + "\n"         +
    		"Order Date    : " + orderDate    + "\n"         + 
    		"Shipment ID   : " + shipmentId   + "\n"         +
    		"Tax Calc. Date: " + taxCalculatedDateUTC + "\n" +
    		"Shipment Date : " + shipmentDate + "\n"         +
    		"Shipped From  : " + shippedFrom  + "\n"         +
    		"Shipped To    : " + shippedTo    + "\n"         +
            "*********************************************\n";
    for (int i = 0; i < this.taxes.size(); i++) {
    	returnString += this.taxes.get(i);
    }
    return returnString;
}
//OrderEntity(String orderId, String orderDate, String shipmentId, String shipmentDate, String taxCalculatedDateUTC,
//Address shippedFrom, Address shippedTo
  public static void main(String[] args) throws ParseException, IOException {
	  Reader in = new FileReader("October.csv");
		Iterable<CSVRecord> records = CSVFormat.EXCEL
				.withHeader("Order_ID", "Order_Date", "Shipment_ID", "Shipment_Date", "Tax_Calculated_Date (UTC)",
						"Posted_Date", "Transaction_Item_ID", "Marketplace", "Merchant_ID", "Fulfillment", "ASIN",
						"SKU", "Product_Description", "Transaction_Type", "Product_Tax_Code", "Quantity", "Currency",
						"Buyer_Exemption_Code", "Buyer_Exemption_Domain", "Buyer_Exemption_Certificate_Id",
						"Display_Price", "Display_Price_Tax_Inclusive", "TaxExclusive_Selling_Price", "Total_Tax",
						"Financial_Component", "Ship_From_City", "Ship_From_State", "Ship_From_Country",
						"Ship_From_Postal_Code", "Ship_From_Tax_Location_Code", "Ship_To_City", "Ship_To_State",
						"Ship_To_Country", "Ship_To_Postal_Code", "Ship_To_Location_Code", "Return_FC_City",
						"Return_FC_State", "Return_FC_Country", "Return_FC_Postal_Code", "Return_FC_Tax_Location_Code",
						"Taxed_Location_Code", "Tax_Address_Role", "Jurisdiction_Level", "Jurisdiction_Name",
						"Rule_Reason_Code", "Display_Promo_Amount", "Display_Promo_Tax_Inclusive",
						"Is_Customer_3P_Funding_Aware", "isPromoApplied", "postPromoTaxableBasis",
						"prePromoTaxableBasis", "Promo_Amount_Basis", "Promo_ID_Domain", "promoAmountTax",
						"Promotion_Identifier", "Promo_Rule_Reason_Code", "Promo_Tax_Price_Type", "Tax_Amount",
						"Taxed_Jurisdiction_Tax_Rate", "Tax_Type", "Tax_Calculation_Reason_Code", "NonTaxable_Amount",
						"Taxable_Amount", "Order_Tax_Amount", "Order_NonTaxable_Amount", "Order_Taxable_Amount")
				.withSkipHeaderRecord(true).parse(in);
	  String currentOid = "";
	  OrderEntity currentOrderEntity = null;
	  ArrayList<OrderEntity> orders = new ArrayList<OrderEntity>();
	  for (CSVRecord record : records) {
		  if (!record.get("Order_ID").isEmpty()) {
			  // Instantiate OrderEntity Object
			  Address fromAddress = new Address(record.get("Ship_From_City"), record.get("Ship_From_State"), record.get("Ship_From_Postal_Code"), record.get("Ship_From_Country"));
			  Address toAddress = new Address(record.get("Ship_To_City"), record.get("Ship_To_State"), record.get("Ship_To_Postal_Code"), record.get("Ship_To_Country"));
			  currentOrderEntity = new OrderEntity(record.get("Order_ID"),record.get("Order_Date"),record.get("Shipment_ID"),
					  record.get("Shipment_Date"),record.get("Tax_Calculated_Date (UTC)"),fromAddress, toAddress);
			  orders.add(currentOrderEntity);
		  }
		  else {
			  // It's blank...create a Tax object and attach to OrderEntity
			  TaxEntity te = new TaxEntity(record.get("Jurisdiction_Level"), record.get("Jurisdiction_Name"), 
					  record.get("Tax_Amount"), record.get("Taxed_Jurisdiction_Tax_Rate"), record.get("Tax_Type"),
					  record.get("Tax_Calculation_Reason_Code"), record.get("Taxable_Amount"), record.get("NonTaxable_Amount"));
			  currentOrderEntity.taxes.add(te);
		  }
	  }
	  for (OrderEntity e : orders) {
		  System.out.println(e);
	  }
  }
} 