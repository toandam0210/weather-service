package com.weather.weathermonitor.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

@Entity
@Table(name = "weatherdata", uniqueConstraints = { 
		@UniqueConstraint(columnNames = "name")},schema = "public")
public class WeatherData{
	@Id
	private String name;
	@Column
	private int temperature;
	@Column
	private int humidity;
	@Column
	private int rainchance;
	public WeatherData() {
		// TODO Auto-generated constructor stub
	}
	public WeatherData(String name, int temperature, int humidity, int rainchance) {
		super();
		this.name = name;
		this.temperature = temperature;
		this.humidity = humidity;
		this.rainchance = rainchance;
	}

	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public int getTemperature() {
		return temperature;
	}
	public void setTemperature(int temperature) {
		this.temperature = temperature;
	}
	public int getHumidity() {
		return humidity;
	}
	public void setHumidity(int humidity) {
		this.humidity = humidity;
	}
	public int getRainchance() {
		return rainchance;
	}
	public void setRainchance(int rainchance) {
		this.rainchance = rainchance;
	}
	
}