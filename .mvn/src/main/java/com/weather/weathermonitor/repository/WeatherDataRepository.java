package com.weather.weathermonitor.repository;

import java.util.List;

import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import com.weather.weathermonitor.model.WeatherData;

@Repository
public interface WeatherDataRepository extends PagingAndSortingRepository<WeatherData, String>{
	public List<WeatherData> findByName(String name);
}
