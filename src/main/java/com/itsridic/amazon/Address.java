package com.itsridic.amazon;

class Address {
	protected String  city;
	protected String  state;
	protected String  zip;
	protected String  country;
	
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
