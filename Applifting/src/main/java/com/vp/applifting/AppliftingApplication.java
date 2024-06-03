package com.vp.applifting;

import com.vp.applifting.models.User;
import com.vp.applifting.repositories.UserRepository;
import com.vp.applifting.services.MonitoringService;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class AppliftingApplication implements CommandLineRunner {

	private final UserRepository userRepository;

	private final MonitoringService monitoringService;

	@Autowired
	public AppliftingApplication(UserRepository userRepository, MonitoringService monitoringService) {
		this.userRepository = userRepository;
		this.monitoringService = monitoringService;
	}

	public static void main(String[] args) {
		SpringApplication.run(AppliftingApplication.class, args);
	}

	@Override
	public void run(String... args) throws Exception {
		if (userRepository.count() == 0) {
			userRepository.save(new User("Applifting", "info@applifting.cz", "93f39e2f-80de-4033-99ee-249d92736a25"));
			userRepository.save(new User("Batman", "batman@example.com", "dcb20f8a-5657-4f1b-9f7f-ce65739b359e"));
		}
	}

	@PostConstruct
	public void init() {
		monitoringService.startMonitoring();
	}
}
