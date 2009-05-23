package com.dialogy.odb.test.vo.country;

public class City {
	private String name;
	private Country country;
	public City(){
		
	}
	public City(String name){
		this.name = name;
	}

	public Country getCountry() {
		return country;
	}

	public void setCountry(Country country) {
		this.country = country;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String toString() {
		return name + " - Country = " + (country!=null?country.getName():"null");
	}
}
