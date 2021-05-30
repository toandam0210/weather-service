package com.weather.weathermonitor.schedule;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import com.weather.weathermonitor.model.WeatherData;
import com.weather.weathermonitor.model.WeatherMonitor;
import com.weather.weathermonitor.repository.WeatherDataRepository;

@Component
public class MyJob{
	
	@Autowired 
	RestTemplate restTemplate;
	
	@Autowired
	WeatherDataRepository weatherDataRepository;
	
	@Scheduled(fixedRate = 500)
	public void addWeatherDataToDb() throws InterruptedException{
		String url = "http://localhost:8080/api/user/weather";
		ResponseEntity<List<WeatherMonitor>> responseEntiry = restTemplate.exchange(url, HttpMethod.GET,null,new ParameterizedTypeReference<List<WeatherMonitor>>() {
		});
		List<WeatherMonitor> listAll = responseEntiry.getBody();
		List<WeatherData> list = new ArrayList<>();
		for (WeatherMonitor weatherMonitor : listAll) {
			for(int i = 0;i<listAll.size();i++) {
				WeatherData weatherData = new WeatherData();
				weatherData.setName(listAll.get(i).getName());
				weatherData.setHumidity(listAll.get(i).getHumidity());
				weatherData.setRainchance(listAll.get(i).getRainchance());
				weatherData.setTemperature(listAll.get(i).getTemperature());
				list.add(weatherData);
			}
		} 
		for (WeatherData weatherData : list) {
			weatherDataRepository.save(weatherData);
		}
	}
	@Scheduled(cron = "*/5 * * * * *")
	public void deleteWeatherDataFromDb() throws InterruptedException{
		weatherDataRepository.deleteAll();
	}
}