package com.weather.weathermonitor.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.weather.weathermonitor.model.WeatherData;
import com.weather.weathermonitor.repository.WeatherDataRepository;

@Service
public class MonitorServiceImpl implements MonitorService {

	@Autowired
	WeatherDataRepository weatherDataRepository;
	@Override
	public List<WeatherData> getDataCity(int pageIndex, int pageSize){
		Pageable paging = PageRequest.of(pageIndex, pageSize);
		Page<WeatherData> pagedResult = weatherDataRepository.findAll(paging);
		if (pagedResult.hasContent()) {
			return pagedResult.getContent();
		} else {
			return new ArrayList<WeatherData>();
		}
	}

}