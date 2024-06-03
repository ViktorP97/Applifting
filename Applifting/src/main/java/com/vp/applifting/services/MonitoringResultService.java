package com.vp.applifting.services;

import java.util.List;
import com.vp.applifting.models.MonitoredEndpoint;
import com.vp.applifting.models.MonitoringResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.vp.applifting.repositories.MonitoringResultRepository;

@Service
public class MonitoringResultService {

  private final MonitoringResultRepository repository;

  @Autowired
  public MonitoringResultService(MonitoringResultRepository repository) {
    this.repository = repository;
  }

  public List<MonitoringResult> findTop10ByMonitoredEndpoint(MonitoredEndpoint endpoint) {
    return repository.findTop10ByMonitoredEndpointOrderByDateOfCheckDesc(endpoint);
  }
}
