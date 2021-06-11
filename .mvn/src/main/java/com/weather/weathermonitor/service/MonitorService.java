package com.weather.weathermonitor.service;

import java.util.List;

import com.weather.weathermonitor.model.WeatherData;

public interface MonitorService {
	 List<WeatherData> getDataCity(int pageIndex, int pageSize);
}
