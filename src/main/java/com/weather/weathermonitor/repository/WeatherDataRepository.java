package com.weather.weathermonitor.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.weather.weathermonitor.model.WeatherData;

@Repository
public interface WeatherDataRepository extends JpaRepository<WeatherData, String>{
	public List<WeatherData> findByHumidity(int humidity);
	public List<WeatherData> findByName(String name);
	public List<WeatherData> findByRainchance(int rainChance);
	public List<WeatherData> findBytemperature(int temperature);
}
