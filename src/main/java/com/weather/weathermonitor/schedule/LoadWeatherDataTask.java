package com.weather.weathermonitor.schedule;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;

import com.weather.weathermonitor.model.WeatherData;
import com.weather.weathermonitor.model.WeatherMonitor;
import com.weather.weathermonitor.repository.WeatherDataRepository;

@Component
public class LoadWeatherDataTask {

	@Autowired
	RestTemplate restTemplate;

	@Autowired
	WeatherDataRepository weatherDataRepository;
	@Value("${weather.url}")
	private String url;

	@Scheduled(fixedRateString  = "${fixRate}")
	@Transactional(rollbackFor = {RuntimeException.class})
	public void addWeatherDataToDb() throws InterruptedException {
		ResponseEntity<List<WeatherMonitor>> responseEntiry = restTemplate.exchange(url, HttpMethod.GET, null,
				new ParameterizedTypeReference<List<WeatherMonitor>>() {
				});
		List<WeatherMonitor> listAll = responseEntiry.getBody();
		List<WeatherData> list = new ArrayList<>();
		for (WeatherMonitor weatherMonitor : listAll) {
			WeatherData weatherData = new WeatherData();
			weatherData.setName(weatherMonitor.getCity().getName());
			
			weatherData.setHumidity(weatherMonitor.getHumidity());
			weatherData.setRainchance(weatherMonitor.getRainchance());
			weatherData.setTemperature(weatherMonitor.getTemperature());
			list.add(weatherData);
		}

		if (list.size() != 0) {
			weatherDataRepository.deleteAll();
			weatherDataRepository.saveAll(list);
		}
		
	}

}