package com.itsridic.amazon;

import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;
import java.text.ParseException;
import java.util.ArrayList;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVRecord;

public class App {
	public static void main(String[] args) throws IOException, ParseException {

		// Load CSV File
		Reader in = new FileReader("October.csv");

		// Create Objects from CSV File
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
		TaxReport.taxTotals(orders);
		TaxReport.taxByAuthority(orders);
	}
}
