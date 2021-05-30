package com.weather.weathermonitor.model;



public class WeatherMonitor{
	private String name;
	private int temperature;
	private int humidity;
	private int rainchance;
	public WeatherMonitor() {
		// TODO Auto-generated constructor stub
	}
	public WeatherMonitor(String name, int temperature, int humidity, int rainchance) {
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