package com.weather.weathermonitor.controller;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.weather.weathermonitor.model.ERole;
import com.weather.weathermonitor.model.Role;
import com.weather.weathermonitor.model.User;
import com.weather.weathermonitor.model.WeatherData;
import com.weather.weathermonitor.payload.JwtResponse;
import com.weather.weathermonitor.payload.LoginRequest;
import com.weather.weathermonitor.payload.MessageResponse;
import com.weather.weathermonitor.payload.SignupRequest;
import com.weather.weathermonitor.repository.UserRepository;
import com.weather.weathermonitor.repository.WeatherDataRepository;
import com.weather.weathermonitor.security.jwt.JwtUtils;
import com.weather.weathermonitor.security.service.UserDetailsImpl;
import com.weather.weathermonitor.repository.RoleRepository;
import com.weather.weathermonitor.service.MonitorServiceImpl;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api")
public class MonitorController{
	@Autowired
	AuthenticationManager authenticationManager;
	
	@Autowired
	UserRepository userRepository;
	
	@Autowired
	RoleRepository roleRepository;
	
	@Autowired
	MonitorServiceImpl monitorServiceImpl;
	
	@Autowired
	PasswordEncoder encoder;
	
	@Autowired
	JwtUtils jwtUtils;
	@Autowired
	WeatherDataRepository weatherDataRepo;
	
	@PostMapping("/login")
	public ResponseEntity<?> authenticateUser(@Valid @RequestBody LoginRequest loginRequest) {
		Authentication authentication = authenticationManager.authenticate(
				new UsernamePasswordAuthenticationToken(loginRequest.getUsername(), loginRequest.getPassword()));
		SecurityContextHolder.getContext().setAuthentication(authentication);
		String jwt = jwtUtils.generateJwtToken(authentication);
		UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();		
		List<String> roles = userDetails.getAuthorities().stream()
				.map(item -> item.getAuthority())
				.collect(Collectors.toList());

		return ResponseEntity.ok(new JwtResponse(jwt, 
												 userDetails.getId(), 
												 userDetails.getUsername(), 
												 roles));
	}
	@PostMapping("/signup")
	public ResponseEntity<?> registerUser(@Valid @RequestBody SignupRequest signUpRequest) {
		if (userRepository.existsByUsername(signUpRequest.getUsername())) {
			return ResponseEntity
					.badRequest()
					.body(new MessageResponse("Error: Username is already taken!"));
		}



		// Create new user's account
		User user = new User(signUpRequest.getUsername(), 
							 encoder.encode(signUpRequest.getPassword()));

		Set<String> strRoles = signUpRequest.getRole();
		Set<Role> roles = new HashSet<>();

		if (strRoles == null) {
			Role userRole = roleRepository.findByName(ERole.ROLE_USER)
					.orElseThrow(() -> new RuntimeException("Error: Role is not found."));
			roles.add(userRole);
		} else {
			strRoles.forEach(role -> {
				switch (role) {
				case "admin":
					Role adminRole = roleRepository.findByName(ERole.ROLE_ADMIN)
							.orElseThrow(() -> new RuntimeException("Error: Role is not found."));
					roles.add(adminRole);

					break;
				default:
					Role userRole = roleRepository.findByName(ERole.ROLE_USER)
							.orElseThrow(() -> new RuntimeException("Error: Role is not found."));
					roles.add(userRole);
				}
			});
		}

		user.setRoles(roles);
		userRepository.save(user);

		return ResponseEntity.ok(new MessageResponse("User registered successfully!"));
	}
	 @PutMapping({"updateUser/{userId}"})
	 public ResponseEntity<User> updateUser(@PathVariable("userId") int userId, @RequestBody User user) {
		 monitorServiceImpl.updateUser(userId, user);
		 return new ResponseEntity<>(monitorServiceImpl.getUserById(userId), HttpStatus.OK);
	    }
	 @DeleteMapping({"deleteUser/{userId}"})
	 public ResponseEntity<String> deleteUser(@PathVariable("userId") int userId) {
		 monitorServiceImpl.deleteUser(userId);
		 return new ResponseEntity<>("True", HttpStatus.OK);
	    }
	 @GetMapping("/user/weather")
	 public ResponseEntity<List<WeatherData>> getAllCity(){
			return new  ResponseEntity<>(monitorServiceImpl.getDataCity(), HttpStatus.OK);
		}
	 @GetMapping("/user/searchName")
	 public ResponseEntity<List<WeatherData>> searchByName(@RequestParam String name){
		 return new ResponseEntity<>(weatherDataRepo.findByName(name),HttpStatus.OK);
	 }


}