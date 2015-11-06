package com.itsridic.amazon;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

public class OrderEntity {
	protected String orderId;
	protected Date orderDate;
	protected String shipmentId;
	protected Date shipmentDate;
	protected Date taxCalculatedDateUTC;
	protected Address shippedFrom;
	protected Address shippedTo;
	protected ArrayList<TaxEntity> taxes;

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

	public String toString() {
		String returnString = 
				 "*********************************************\n" 
				+ "Order ID      : " + orderId              + "\n"
				+ "Order Date    : " + orderDate            + "\n" 
				+ "Shipment ID   : " + shipmentId           + "\n" 
				+ "Tax Calc. Date: " + taxCalculatedDateUTC + "\n" 
				+ "Shipment Date : " + shipmentDate         + "\n" 
				+ "Shipped From  : " + shippedFrom          + "\n" 
				+ "Shipped To    : " + shippedTo            + "\n"
				+ "*********************************************\n";
		for (int i = 0; i < this.taxes.size(); i++) {
			returnString += this.taxes.get(i);
		}
		return returnString;
	}
}