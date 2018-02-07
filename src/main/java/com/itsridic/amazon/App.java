package com.itsridic.amazon;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;
import java.text.ParseException;
import java.util.ArrayList;
import javax.swing.JFileChooser;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVRecord;

class App implements Runnable {
	public static void main(String[] args) {
		SimpleFrame frame = new SimpleFrame();
		frame.setDefaultCloseOperation(3);
		frame.setVisible(true);
		JFileChooser fileChooser = new JFileChooser();
		fileChooser.setCurrentDirectory(new File(System.getProperty("user.home")));
		int result = fileChooser.showOpenDialog(frame);
		if (result == 0) {
			File file = fileChooser.getSelectedFile();
			Reader in = null;
			try {
				in = new FileReader(file);
			} catch (FileNotFoundException e2) {
				e2.printStackTrace();
			}
			Iterable<CSVRecord> records = null;
			try {
				records =
						CSVFormat.EXCEL.withHeader(new String[] { "Order_ID", "Order_Date", "Shipment_ID",
								"Shipment_Date", "Tax_Calculated_Date (UTC)", "Posted_Date", "Transaction_Item_ID",
								"Marketplace", "Merchant_ID", "Fulfillment", "ASIN", "SKU", "Product_Description",
								"Transaction_Type", "Tax_Collection_Model", "Tax_Collection_Responsible_Party",
								"Product_Tax_Code", "Quantity", "Currency", "Buyer_Exemption_Code",
								"Buyer_Exemption_Domain", "Buyer_Exemption_Certificate_Id", "Display_Price",
								"Display_Price_Tax_Inclusive", "TaxExclusive_Selling_Price", "Total_Tax",
								"Financial_Component", "Ship_From_City", "Ship_From_State", "Ship_From_Country",
								"Ship_From_Postal_Code", "Ship_From_Tax_Location_Code", "Ship_To_City", "Ship_To_State",
								"Ship_To_Country", "Ship_To_Postal_Code", "Ship_To_Location_Code", "Return_FC_City",
								"Return_FC_State", "Return_FC_Country", "Return_FC_Postal_Code",
								"Return_FC_Tax_Location_Code", "Taxed_Location_Code", "Tax_Address_Role",
								"Jurisdiction_Level", "Jurisdiction_Name", "Rule_Reason_Code", "Display_Promo_Amount",
								"Display_Promo_Tax_Inclusive", "Is_Customer_3P_Funding_Aware", "isPromoApplied",
								"postPromoTaxableBasis", "prePromoTaxableBasis", "Promo_Amount_Basis",
								"Promo_ID_Domain", "promoAmountTax", "Promotion_Identifier", "Promo_Rule_Reason_Code",
								"Promo_Tax_Price_Type", "Tax_Amount", "Taxed_Jurisdiction_Tax_Rate", "Tax_Type",
								"Tax_Calculation_Reason_Code", "NonTaxable_Amount", "Taxable_Amount",
								"Order_Tax_Amount", "Order_NonTaxable_Amount", "Order_Taxable_Amount" })
								.withSkipHeaderRecord(true).parse(in);
			} catch (IOException e1) {
				e1.printStackTrace();
			}
			OrderEntity currentOrderEntity = null;

			ArrayList<OrderEntity> orders = new ArrayList<OrderEntity>();
			for (CSVRecord record : records) {
				if (record.get("Tax_Collection_Model").equals("MarketplaceFacilitator")) {
					continue;
				}
				TaxEntity te = new TaxEntity(record.get("Jurisdiction_Level"), record.get("Jurisdiction_Name"),
						record.get("Tax_Amount"), record.get("Taxed_Jurisdiction_Tax_Rate"), record.get("Tax_Type"),
						record.get("Tax_Calculation_Reason_Code"), record.get("Taxable_Amount"),
						record.get("NonTaxable_Amount"));
				if (!record.get("Order_ID").isEmpty()) {
					Address fromAddress = new Address(record.get("Ship_From_City"),
							Address.resolveStateName(record.get("Ship_From_State")),
							record.get("Ship_From_Postal_Code"), record.get("Ship_From_Country"));
					Address toAddress = new Address(record.get("Ship_To_City"),
							Address.resolveStateName(record.get("Ship_To_State")),

							record.get("Ship_To_Postal_Code"), record.get("Ship_To_Country"));
					try {
						currentOrderEntity = new OrderEntity(record.get("Order_ID"), record.get("Order_Date"),
								record.get("Shipment_ID"), record.get("Shipment_Date"),
								record.get("Tax_Calculated_Date (UTC)"), fromAddress, toAddress);
					} catch (ParseException e) {
						e.printStackTrace();
					}
					currentOrderEntity.taxes.add(te);
					orders.add(currentOrderEntity);
				} else {
					currentOrderEntity.taxes.add(te);
				}
			}
			TaxReport.taxTotals(orders);
			TaxReport.taxByAuthority(orders);
		}
		System.exit(1);
	}

	@Override
	public void run() {
		// TODO Auto-generated method stub

	}
}
