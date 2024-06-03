package com.vp.applifting.controllers;

import com.vp.applifting.models.MonitoredEndpoint;
import com.vp.applifting.models.MonitoringResult;
import com.vp.applifting.models.User;
import com.vp.applifting.repositories.UserRepository;
import com.vp.applifting.services.MonitoredEndpointService;
import com.vp.applifting.services.MonitoringResultService;
import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/results")
public class MonitoringResultController {

  private final MonitoringResultService resultService;

  private final MonitoredEndpointService endpointService;

  private final UserRepository userRepository;


  @Autowired
  public MonitoringResultController(MonitoringResultService resultService,
      MonitoredEndpointService endpointService, UserRepository userRepository) {
    this.resultService = resultService;
    this.endpointService = endpointService;
    this.userRepository = userRepository;
  }

  @GetMapping("/{endpointId}")
  public ResponseEntity<List<MonitoringResult>> getResults(@RequestHeader("Authorization") String token, @PathVariable Long endpointId) {
    Optional<User> ownerOptional = userRepository.findByAccessToken(token.substring(7));
    if (ownerOptional.isEmpty()) {
      return ResponseEntity.status(403).build();
    }
    User owner = ownerOptional.get();
    MonitoredEndpoint endpoint = endpointService.findById(endpointId);
    if (endpoint == null || !endpoint.getOwner().equals(owner)) {
      return ResponseEntity.status(403).build();
    }
    return ResponseEntity.ok(resultService.findTop10ByMonitoredEndpoint(endpoint));
  }
}
