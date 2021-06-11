package com.weather.weathermonitor.model;


import javax.persistence.Embeddable;

@Embeddable
public class City{
	private String name;
	
	public City() {
		// TODO Auto-generated constructor stub
	}
	
	public City(String name) {
		super();
		this.name = name;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

}