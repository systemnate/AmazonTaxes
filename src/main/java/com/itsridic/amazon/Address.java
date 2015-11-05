package com.itsridic.amazon;

class Address {
	private String  city;
	private String  state;
	private String  zip;
	private String  country;
	
	public Address(String city, String state, String zip, String country) {
		this.city = city;
		this.state = state;
		this.zip = zip;
		this.country = country;
	}
	
	public String toString() {
		return city + ", " + state + "  " + zip;
	}
}
