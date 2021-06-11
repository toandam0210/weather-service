package com.weather.weathermonitor.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.weather.weathermonitor.model.WeatherData;
import com.weather.weathermonitor.repository.WeatherDataRepository;
import com.weather.weathermonitor.service.MonitorService;

@RestController
@RequestMapping("/api")
public class WeatherController {
	@Autowired
	MonitorService monitorService;

	@Autowired
	WeatherDataRepository weatherDataRepo;

	@GetMapping("weather")
	public ResponseEntity<List<WeatherData>> getAllCity(@RequestParam(defaultValue = "0") int pageIndex,
			@RequestParam(defaultValue = "3") int pageSize) {
		return new ResponseEntity<>(monitorService.getDataCity(pageIndex, pageSize), HttpStatus.OK);
	}

	@GetMapping("/weather/search")
	public ResponseEntity<List<WeatherData>> searchByName(@RequestParam String name) {
		return new ResponseEntity<>(weatherDataRepo.findByName(name), HttpStatus.OK);
	}

}