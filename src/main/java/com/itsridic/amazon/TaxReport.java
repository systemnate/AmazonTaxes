package com.itsridic.amazon;

import java.util.ArrayList;
import java.util.Map;
import java.util.TreeMap;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.math3.util.Precision;

public class TaxReport {
	public static void taxTotals(ArrayList<OrderEntity> orders) {
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
		System.out.println("***************************************");
		System.out.println("TOTAL TAX COLLECTED FOR EACH STATE:");
		System.out.println("***************************************");
		for (Map.Entry<String, Double> entry : stateMapping.entrySet()) {
			System.out.printf("%-5s %.2f\n", entry.getKey(), Precision.round(entry.getValue(), 2));
		}
	}

	public static void taxByAuthority(ArrayList<OrderEntity> orders) {
		TreeMap<String, TreeMap<String, ArrayList <Double>>> taxAuthorities = new TreeMap<>();
		TreeMap<String, ArrayList<Double>> tempMap;
		ArrayList<Double> taxes;
		for (OrderEntity order : orders) {
			double taxAmount = 0;
			double taxableAmount = 0;
			double nonTaxableAmount = 0;
			double taxedJurisdictionTaxRate = 0;
			if (!taxAuthorities.containsKey(order.shippedTo.state)) {
				tempMap = new TreeMap<>();
				// State not yet present. Add everything from that tax list to
				// tempMap
				for (TaxEntity tax : order.taxes) {
					String authority = tax.jurisdictionLevel + " " + tax.jurisdictionName + "(" + tax.taxCalculationReasonCode + ")";
					taxAmount = tax.taxAmount;
					taxableAmount = tax.taxableAmount;
					nonTaxableAmount = tax.nonTaxableAmount;
					taxedJurisdictionTaxRate = tax.taxedJurisdictionTaxRate;
					if (!authority.contains("NOT APPLICABLE")) {
						taxes = new ArrayList<>();
						taxes.add(taxAmount);
						taxes.add(taxableAmount);
						taxes.add(nonTaxableAmount);
						taxes.add(taxedJurisdictionTaxRate);
						tempMap.put(authority, taxes);
					}
				}
				taxAuthorities.put(order.shippedTo.state, tempMap);
			} else {
				// State already present. Add existing values to current values.
				tempMap = taxAuthorities.get(order.shippedTo.state);
				for (TaxEntity tax : order.taxes) {
					String authority = tax.jurisdictionLevel + " " + tax.jurisdictionName + "(" + tax.taxCalculationReasonCode + ")";
					if (tempMap.containsKey(authority)) {
						taxAmount = tempMap.get(authority).get(0) + tax.taxAmount;
						taxableAmount = tempMap.get(authority).get(1) + tax.taxableAmount;
						nonTaxableAmount = tempMap.get(authority).get(2) + tax.nonTaxableAmount;
						taxedJurisdictionTaxRate = tax.taxedJurisdictionTaxRate;
					} else {
						taxAmount = tax.taxAmount;
						taxableAmount = tax.taxableAmount;
						nonTaxableAmount = tax.nonTaxableAmount;
						taxedJurisdictionTaxRate = tax.taxedJurisdictionTaxRate;
					}
					if (!authority.contains("NOT APPLICABLE")) {
						taxes = new ArrayList<>();
						taxes.add(taxAmount);
						taxes.add(taxableAmount);
						taxes.add(nonTaxableAmount);
						taxes.add(taxedJurisdictionTaxRate);
						tempMap.put(authority, taxes);
					}					
				}
				taxAuthorities.put(order.shippedTo.state, tempMap);
			}
		}
		System.out.println("***************************************");
		System.out.println("BREAKDOWN BY EACH STATE:");
		System.out.println("***************************************");
		System.out.printf("%-70s %-8s\t%-8s\t%-8s\t%-8s\n", "Jurisdiction", "TaxAmnt", "TaxableAmt", "NonTaxAmt", "Tax%");

		for (Map.Entry<String, TreeMap<String, ArrayList<Double>>> stateTax : taxAuthorities.entrySet()) {
			System.out.println(stateTax.getKey());
			for (Map.Entry<String, ArrayList<Double>> detail : stateTax.getValue().entrySet()) {
				System.out.printf("%-70s %-8.2f\t%-8.2f\t%-8.2f\t%-8.5f\n", detail.getKey(), detail.getValue().get(0),
						detail.getValue().get(1), detail.getValue().get(2), detail.getValue().get(3));
			}
			System.out.println(StringUtils.repeat("-", 120));
		}
	}
}
