package com.weather.weathermonitor.service;

import com.weather.weathermonitor.model.User;

public interface MonitorService {
	public void updateUser(int id,User user);
	public void deleteUser(int id);
	public User getUserById(int id);
}
