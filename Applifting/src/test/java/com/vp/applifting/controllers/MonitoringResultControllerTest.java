package com.vp.applifting.controllers;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import com.vp.applifting.models.MonitoringResult;
import com.vp.applifting.models.MonitoredEndpoint;
import com.vp.applifting.models.User;
import com.vp.applifting.repositories.UserRepository;
import com.vp.applifting.services.MonitoringResultService;
import com.vp.applifting.services.MonitoredEndpointService;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

@ExtendWith(MockitoExtension.class)
public class MonitoringResultControllerTest {

  @Mock
  private MonitoringResultService resultService;

  @Mock
  private MonitoredEndpointService endpointService;

  @Mock
  private UserRepository userRepository;

  @InjectMocks
  private MonitoringResultController controller;

  @Test
  public void getResultsTest() {
    String token = "Bearer testToken";
    Long endpointId = 1L;
    User owner = new User();
    owner.setId(1L);
    MonitoredEndpoint endpoint = new MonitoredEndpoint();
    endpoint.setId(endpointId);
    endpoint.setOwner(owner);
    List<MonitoringResult> results = new ArrayList<>();

    when(userRepository.findByAccessToken("testToken")).thenReturn(Optional.of(owner));
    when(endpointService.findById(eq(endpointId))).thenReturn(endpoint);
    when(resultService.findTop10ByMonitoredEndpoint(eq(endpoint))).thenReturn(results);

    ResponseEntity<List<MonitoringResult>> response = controller.getResults(token, endpointId);

    assertNotNull(response);
    assertEquals(HttpStatus.OK, response.getStatusCode());
    assertNotNull(response.getBody());
    assertEquals(results, response.getBody());
  }
}