package com.vp.applifting.services;

import com.vp.applifting.models.MonitoredEndpoint;
import com.vp.applifting.models.MonitoringResult;
import com.vp.applifting.repositories.MonitoredEndpointRepository;
import com.vp.applifting.repositories.MonitoringResultRepository;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class MonitoringService {

  private final MonitoredEndpointRepository monitoredEndpointRepository;
  private final MonitoringResultRepository monitoringResultRepository;
  private final ScheduledExecutorService scheduler;

  @Autowired
  public MonitoringService(MonitoredEndpointRepository monitoredEndpointRepository,
      MonitoringResultRepository monitoringResultRepository, ScheduledExecutorService scheduler) {
    this.monitoredEndpointRepository = monitoredEndpointRepository;
    this.monitoringResultRepository = monitoringResultRepository;
    this.scheduler = scheduler;
  }

  public void startMonitoring() {
    List<MonitoredEndpoint> monitoredEndpoints = monitoredEndpointRepository.findAll();

    for (MonitoredEndpoint endpoint : monitoredEndpoints) {
      monitorSingleEndpoint(endpoint);
    }
  }

  public void monitorSingleEndpoint(MonitoredEndpoint endpoint) {
    long monitoringIntervalMillis = endpoint.getMonitoredInterval() * 1000;

    LocalDateTime now = LocalDateTime.now();
    LocalDateTime nextCheckTime = now.plusSeconds(endpoint.getMonitoredInterval());
    long initialDelayMillis = Duration.between(now, nextCheckTime).toMillis();

    scheduler.scheduleAtFixedRate(() -> {
      ResponseEntity<String> response = sendRequest(endpoint.getUrl());
      int statusCode = response.getStatusCode().value();
      String payload = response.getBody();
      LocalDateTime dateOfCheck = LocalDateTime.now();

      MonitoringResult result = new MonitoringResult();
      result.setDateOfCheck(dateOfCheck);
      result.setReturnedHttpStatusCode(statusCode);
      result.setReturnedPayload(payload);
      result.setMonitoredEndpoint(endpoint);

      monitoringResultRepository.save(result);
    }, initialDelayMillis, monitoringIntervalMillis, TimeUnit.MILLISECONDS);
  }

  private ResponseEntity<String> sendRequest(String url) {
    RestTemplate restTemplate = new RestTemplate();
    return restTemplate.getForEntity(url, String.class);
  }
}