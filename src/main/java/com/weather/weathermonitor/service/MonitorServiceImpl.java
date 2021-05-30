package com.weather.weathermonitor.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.weather.weathermonitor.model.User;
import com.weather.weathermonitor.model.WeatherData;
import com.weather.weathermonitor.model.WeatherMonitor;
import com.weather.weathermonitor.repository.RoleRepository;
import com.weather.weathermonitor.repository.UserRepository;
import com.weather.weathermonitor.repository.WeatherDataRepository;

@Service
public class MonitorServiceImpl implements MonitorService{

	@Autowired
	UserRepository userRepository;
	
	@Autowired
	RoleRepository roleRepository;
	
	@Autowired
	PasswordEncoder encoder;
	
	@Autowired 
	RestTemplate restTemplate;
	
	@Autowired
	WeatherDataRepository weatherDataRepository;
	
	@Override
	public void updateUser(int id, User user) {
		User userInDb = userRepository.findById(id).get();
		userInDb.setPassword(encoder.encode(user.getPassword()));
		userRepository.save(userInDb);
		
		
	}

	@Override
	public void deleteUser(int id) {
		userRepository.deleteById(id);
		
	}

	@Override
	public User getUserById(int id) {
		return userRepository.findById(id).get();
	}

	public List<WeatherData> getDataCity() {
		List<WeatherData> listAll = weatherDataRepository.findAll();
		return listAll;
	}

	
}