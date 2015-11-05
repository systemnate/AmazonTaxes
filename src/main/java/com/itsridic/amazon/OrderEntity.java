package com.itsridic.amazon;

import java.io.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import org.apache.commons.csv.*;
import org.apache.commons.math3.util.Precision;

public class OrderEntity {
	private String orderId;
	private Date orderDate;
	private String shipmentId;
	private Date shipmentDate;
	private Date taxCalculatedDateUTC;
	private Address shippedFrom;
	private Address shippedTo;
	private ArrayList<TaxEntity> taxes;

	public OrderEntity(String orderId, String orderDate, String shipmentId, String shipmentDate,
			String taxCalculatedDateUTC, Address shippedFrom, Address shippedTo) throws ParseException {

		this.orderId = orderId;
		this.orderDate = new SimpleDateFormat("yyyy-MM-dd").parse(orderDate.replaceAll("\\+.+", "").toString());
		this.shipmentId = shipmentId;
		this.shipmentDate = new SimpleDateFormat("yyyy-MM-dd").parse(shipmentDate.replaceAll("\\+.+", "").toString());
		this.taxCalculatedDateUTC = new SimpleDateFormat("yyyy-MM-dd")
				.parse(taxCalculatedDateUTC.replaceAll("\\+.+", "").toString());
		this.shippedFrom = shippedFrom;
		this.shippedTo = shippedTo;
		this.taxes = new ArrayList<TaxEntity>();
	}

	public void taxReport(ArrayList<TaxEntity> taxes) {

	}

	public static TreeMap<String, Double> taxTotals(ArrayList<OrderEntity> orders) {
		// This will hold the state along with the total collected (e.g "TX",
		// 1.75)
		TreeMap<String, Double> stateMapping = new TreeMap<>();
		for (OrderEntity order : orders) {
			// If the state does not exist in the map add the state along with
			// the total tax for that order
			if (!stateMapping.containsKey(order.shippedTo.state)) {
				double totalTaxes = 0;
				for (TaxEntity tax : order.taxes) {
					totalTaxes += tax.taxAmount;
				}
				stateMapping.put(order.shippedTo.state, totalTaxes);
			}
			// If it already exists, add the current totalTax for the order to
			// the existing one
			else {
				double totalTaxes = stateMapping.get(order.shippedTo.state);
				for (TaxEntity tax : order.taxes) {
					totalTaxes += tax.taxAmount;
				}
				stateMapping.put(order.shippedTo.state, totalTaxes);
			}
		}
		return stateMapping;
	}

	public static TreeMap<String, TreeMap<String, ArrayList<Double>>> taxByAuthority(ArrayList<OrderEntity> orders) {
		TreeMap<String, TreeMap<String, ArrayList <Double>>> taxAuthorities = new TreeMap<>();
		TreeMap<String, ArrayList<Double>> tempMap;
		ArrayList<Double> taxes;
		for (OrderEntity order : orders) {
			if (!taxAuthorities.containsKey(order.shippedTo.state)) {
				tempMap = new TreeMap<>();
				// State not yet present. Add everything from that tax list to
				// tempMap
				for (TaxEntity tax : order.taxes) {
					String authority = tax.jurisdictionLevel + " " + tax.jurisdictionName;
					double taxAmount = tax.taxAmount;
					double taxableAmount = tax.taxableAmount;
					if (!authority.contains("NOT APPLICABLE")) {
						taxes = new ArrayList<>();
						taxes.add(taxAmount);
						taxes.add(taxableAmount);
						tempMap.put(authority, taxes);
					}
				}
				taxAuthorities.put(order.shippedTo.state, tempMap);
			} else {
				// State already present. Add existing values to current values.
				tempMap = taxAuthorities.get(order.shippedTo.state);
				for (TaxEntity tax : order.taxes) {
					String authority = tax.jurisdictionLevel + " " + tax.jurisdictionName;
					if (tempMap.containsKey(authority)) {
						double taxAmount = tempMap.get(authority).get(0) + tax.taxAmount;
						double taxableAmount = tempMap.get(authority).get(1) + tax.taxableAmount;
						if (!authority.contains("NOT APPLICABLE")) {
							taxes = new ArrayList<>();
							taxes.add(taxAmount);
							taxes.add(taxableAmount);
							tempMap.put(authority, taxes);
						}
					} else {
						double taxAmount = tax.taxAmount;
						double taxableAmount = tax.taxableAmount;
						if (!authority.contains("NOT APPLICABLE")) {
							taxes = new ArrayList<>();
							taxes.add(taxAmount);
							taxes.add(taxableAmount);
							tempMap.put(authority, taxes);
						}
					}
				}
				taxAuthorities.put(order.shippedTo.state, tempMap);
			}
		}
		return taxAuthorities;
	}

	public String toString() {
		String returnString = "*********************************************\n" + "Order ID      : " + orderId + "\n"
				+ "Order Date    : " + orderDate + "\n" + "Shipment ID   : " + shipmentId + "\n" + "Tax Calc. Date: "
				+ taxCalculatedDateUTC + "\n" + "Shipment Date : " + shipmentDate + "\n" + "Shipped From  : "
				+ shippedFrom + "\n" + "Shipped To    : " + shippedTo + "\n"
				+ "*********************************************\n";
		for (int i = 0; i < this.taxes.size(); i++) {
			returnString += this.taxes.get(i);
		}
		return returnString;
	}

	// OrderEntity(String orderId, String orderDate, String shipmentId, String
	// shipmentDate, String taxCalculatedDateUTC,
	// Address shippedFrom, Address shippedTo
	public static void main(String[] args) throws ParseException, IOException {

		// Open CSV File
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

		// Points to the current order in the loop
		OrderEntity currentOrderEntity = null;
		// Holds all orders
		ArrayList<OrderEntity> orders = new ArrayList<OrderEntity>();

		for (CSVRecord record : records) {
			// Each row contains a tax entry that will need added
			TaxEntity te = new TaxEntity(record.get("Jurisdiction_Level"), record.get("Jurisdiction_Name"),
					record.get("Tax_Amount"), record.get("Taxed_Jurisdiction_Tax_Rate"), record.get("Tax_Type"),
					record.get("Tax_Calculation_Reason_Code"), record.get("Taxable_Amount"),
					record.get("NonTaxable_Amount"));
			// When the Order_ID is present, the entry contains the order
			// information
			if (!record.get("Order_ID").isEmpty()) {
				// Instantiate OrderEntity Object
				Address fromAddress = new Address(record.get("Ship_From_City"), record.get("Ship_From_State"),
						record.get("Ship_From_Postal_Code"), record.get("Ship_From_Country"));
				Address toAddress = new Address(record.get("Ship_To_City"), record.get("Ship_To_State"),

						record.get("Ship_To_Postal_Code"), record.get("Ship_To_Country"));
				currentOrderEntity = new OrderEntity(record.get("Order_ID"), record.get("Order_Date"),
						record.get("Shipment_ID"), record.get("Shipment_Date"), record.get("Tax_Calculated_Date (UTC)"),
						fromAddress, toAddress);
				currentOrderEntity.taxes.add(te);
				orders.add(currentOrderEntity);
			}
			// When the Order_ID is not present, just add the tax information to
			// the current order
			else {
				currentOrderEntity.taxes.add(te);
			}
		}

		TreeMap<String, Double> taxTotal = taxTotals(orders);
		System.out.println("***************************************");
		System.out.println("TOTAL TAX COLLECTED FOR EACH STATE:");
		System.out.println("***************************************");
		for (Map.Entry<String, Double> entry : taxTotal.entrySet()) {
			System.out.printf("%-5s %.2f\n", entry.getKey(), Precision.round(entry.getValue(), 2));
		}
		System.out.println("***************************************");
		System.out.println("BREAKDOWN BY EACH STATE:");
		System.out.println("***************************************");
		TreeMap<String, TreeMap<String, ArrayList<Double>>> taxAuthority = taxByAuthority(orders);

		for (Map.Entry<String, TreeMap<String, ArrayList<Double>>> stateTax : taxAuthority.entrySet()) {
			System.out.println(stateTax.getKey());
			for (Map.Entry<String, ArrayList<Double>> detail : stateTax.getValue().entrySet()) {
				System.out.printf("%-55s %.2f\t%.2f\n", detail.getKey(), detail.getValue().get(0), detail.getValue().get(1));
			}
			System.out.println("------------------------------------------------------------------------------");
		}
	}
}