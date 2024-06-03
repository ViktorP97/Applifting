package com.vp.applifting.services;

import com.vp.applifting.dtos.MonitoredEndpointDto;
import com.vp.applifting.models.MonitoringResult;
import com.vp.applifting.repositories.MonitoringResultRepository;
import com.vp.applifting.repositories.UserRepository;
import java.time.LocalDateTime;
import java.util.List;
import com.vp.applifting.models.MonitoredEndpoint;
import com.vp.applifting.models.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.vp.applifting.repositories.MonitoredEndpointRepository;
import org.springframework.transaction.annotation.Transactional;

@Service
public class MonitoredEndpointService {

  private final MonitoredEndpointRepository repository;

  private final MonitoringResultRepository monitoringResultRepository;

  private final UserRepository userRepository;

  @Autowired
  public MonitoredEndpointService(MonitoredEndpointRepository repository,
      MonitoringResultRepository monitoringResultRepository, UserRepository userRepository) {
    this.repository = repository;
    this.monitoringResultRepository = monitoringResultRepository;
    this.userRepository = userRepository;
  }

  public MonitoredEndpoint save(User owner, MonitoredEndpointDto endpointDto) {
    MonitoredEndpoint endpoint = new MonitoredEndpoint();
    endpoint.setName(endpointDto.getName());
    endpoint.setMonitoredInterval(endpointDto.getMonitoredInterval());
    endpoint.setDateOfCreation(LocalDateTime.now());
    endpoint.setUrl(endpointDto.getUrl());
    endpoint.setOwner(owner);
    return repository.save(endpoint);
  }

  public MonitoredEndpoint update(Long id, User owner, MonitoredEndpointDto endpointDto) {
    MonitoredEndpoint existingEndpoint = repository.findById(id).orElse(null);

    if (existingEndpoint == null) {
      return null;
    }
    existingEndpoint.setName(endpointDto.getName());
    existingEndpoint.setMonitoredInterval(endpointDto.getMonitoredInterval());
    existingEndpoint.setOwner(owner);
    existingEndpoint.setUrl(endpointDto.getUrl());
    return repository.save(existingEndpoint);
  }

  public List<MonitoredEndpoint> findByOwner(User owner) {
    return repository.findByOwner(owner);
  }


  @Transactional
  public void delete(User owner, MonitoredEndpoint endpoint) {
    List<MonitoringResult> monitoringResults = endpoint.getMonitoringResults();

    if (monitoringResults != null && !monitoringResults.isEmpty()) {
      monitoringResultRepository.deleteAll(monitoringResults);
    }
    owner.getMonitoredEndpoints().remove(endpoint);
    repository.delete(endpoint);
    userRepository.save(owner);
  }

  public MonitoredEndpoint findById(Long id) {
    return repository.findById(id).orElse(null);
  }
}
