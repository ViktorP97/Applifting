package com.vp.applifting.controllers;

import com.vp.applifting.dtos.MonitoredEndpointDto;
import com.vp.applifting.models.MonitoredEndpoint;
import com.vp.applifting.models.User;
import com.vp.applifting.repositories.UserRepository;
import com.vp.applifting.services.MonitoredEndpointService;
import com.vp.applifting.services.MonitoringService;
import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/endpoints")
public class MonitoredEndpointController {

  private final MonitoredEndpointService endpointService;

  private final UserRepository userRepository;

  private final MonitoringService monitoringService;

  @Autowired
  public MonitoredEndpointController(MonitoredEndpointService endpointService,
      UserRepository userRepository, MonitoringService monitoringService) {
    this.endpointService = endpointService;
    this.userRepository = userRepository;
    this.monitoringService = monitoringService;
  }

  @PostMapping
  public ResponseEntity<MonitoredEndpoint> createEndpoint(@RequestHeader("Authorization") String token, @RequestBody MonitoredEndpointDto endpointDto) {
    Optional<User> ownerOptional = userRepository.findByAccessToken(token.substring(7));
    if (ownerOptional.isEmpty()) {
      return ResponseEntity.status(403).build();
    }
    User owner = ownerOptional.get();
    MonitoredEndpoint endpoint = endpointService.save(owner, endpointDto);
    monitoringService.monitorSingleEndpoint(endpoint);
    return ResponseEntity.ok(endpoint);
  }

  @GetMapping
  public ResponseEntity<List<MonitoredEndpoint>> listEndpoints(@RequestHeader("Authorization") String token) {
    Optional<User> ownerOptional = userRepository.findByAccessToken(token.substring(7));
    if (ownerOptional.isEmpty()) {
      return ResponseEntity.status(403).build();
    }
    User owner = ownerOptional.get();
    return ResponseEntity.ok(endpointService.findByOwner(owner));
  }

  @PutMapping("/{id}")
  public ResponseEntity<MonitoredEndpoint> updateEndpoint(@RequestHeader("Authorization") String token, @PathVariable Long id, @RequestBody MonitoredEndpointDto updatedEndpointDto) {
    Optional<User> ownerOptional = userRepository.findByAccessToken(token.substring(7));
    if (ownerOptional.isEmpty()) {
      return ResponseEntity.status(403).build();
    }
    User owner = ownerOptional.get();
    MonitoredEndpoint endpoint = endpointService.findById(id);
    if (endpoint == null || !endpoint.getOwner().equals(owner)) {
      return ResponseEntity.status(403).build();
    }
    return ResponseEntity.ok(endpointService.update(id, owner, updatedEndpointDto));
  }

  @DeleteMapping("/{id}")
  public ResponseEntity<Void> deleteEndpoint(@RequestHeader("Authorization") String token, @PathVariable Long id) {
    Optional<User> ownerOptional = userRepository.findByAccessToken(token.substring(7));
    if (ownerOptional.isEmpty()) {
      return ResponseEntity.status(403).build();
    }
    User owner = ownerOptional.get();
    MonitoredEndpoint endpoint = endpointService.findById(id);
    if (endpoint == null || !endpoint.getOwner().equals(owner)) {
      return ResponseEntity.status(403).build();
    }
    endpointService.delete(owner, endpoint);
    return ResponseEntity.ok().build();
  }
}
