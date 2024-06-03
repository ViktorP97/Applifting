package com.vp.applifting.controllers;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import com.vp.applifting.dtos.MonitoredEndpointDto;
import com.vp.applifting.models.MonitoredEndpoint;
import com.vp.applifting.models.User;
import com.vp.applifting.repositories.UserRepository;
import com.vp.applifting.services.MonitoredEndpointService;
import com.vp.applifting.services.MonitoringService;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

@ExtendWith(MockitoExtension.class)
class MonitoredEndpointControllerTest {

  @Mock
  private MonitoredEndpointService endpointService;

  @Mock
  private UserRepository userRepository;

  @Mock
  private MonitoringService monitoringService;

  @InjectMocks
  private MonitoredEndpointController endpointController;

  @Test
  public void createEndpointTest() {
    String token = "Bearer testToken";
    User owner = new User();
    MonitoredEndpointDto endpointDto = new MonitoredEndpointDto();
    endpointDto.setName("Test Endpoint");
    endpointDto.setUrl("http://example.com");
    endpointDto.setMonitoredInterval(60);

    when(userRepository.findByAccessToken("testToken")).thenReturn(Optional.of(owner));
    when(endpointService.save(eq(owner), any(MonitoredEndpointDto.class))).thenAnswer(invocation -> {
      MonitoredEndpointDto dto = invocation.getArgument(1);
      MonitoredEndpoint endpoint = new MonitoredEndpoint();
      endpoint.setName(dto.getName());
      endpoint.setUrl(dto.getUrl());
      endpoint.setMonitoredInterval(dto.getMonitoredInterval());
      return endpoint;
    });

    ResponseEntity<MonitoredEndpoint> response = endpointController.createEndpoint(token, endpointDto);

    assertNotNull(response);
    assertEquals(HttpStatus.OK, response.getStatusCode());
    assertNotNull(response.getBody());
    assertEquals(endpointDto.getName(), response.getBody().getName());
    assertEquals(endpointDto.getUrl(), response.getBody().getUrl());
    assertEquals(endpointDto.getMonitoredInterval(), response.getBody().getMonitoredInterval());
  }

  @Test
  public void updateEndpointTest() {
    String token = "Bearer testToken";
    Long endpointId = 1L;

    User testUser = new User();
    testUser.setId(1L);
    testUser.setUsername("testUser");

    MonitoredEndpointDto updatedEndpointDto = new MonitoredEndpointDto();
    updatedEndpointDto.setName("Updated Endpoint");
    updatedEndpointDto.setMonitoredInterval(10);
    updatedEndpointDto.setUrl("http://updated-example.com");

    MonitoredEndpoint existingEndpoint = new MonitoredEndpoint();
    existingEndpoint.setId(endpointId);
    existingEndpoint.setName("Old Endpoint");
    existingEndpoint.setMonitoredInterval(5);
    existingEndpoint.setUrl("http://example.com");
    existingEndpoint.setOwner(testUser);

    when(userRepository.findByAccessToken("testToken")).thenReturn(Optional.of(testUser));
    when(endpointService.findById(endpointId)).thenReturn(existingEndpoint);
    when(endpointService.update(endpointId, testUser, updatedEndpointDto)).thenReturn(existingEndpoint);

    ResponseEntity<MonitoredEndpoint> response = endpointController.updateEndpoint(token, endpointId, updatedEndpointDto);

    assertEquals(HttpStatus.OK, response.getStatusCode());
    assertEquals(existingEndpoint, response.getBody());
  }

  @Test
  public void listEndpointsTest() {
    String token = "Bearer testToken";
    User owner = new User();
    MonitoredEndpoint endpoint1 = new MonitoredEndpoint();
    endpoint1.setId(1L);
    endpoint1.setName("Test Endpoint 1");
    endpoint1.setUrl("http://example1.com");
    endpoint1.setMonitoredInterval(60);
    MonitoredEndpoint endpoint2 = new MonitoredEndpoint();
    endpoint2.setId(2L);
    endpoint2.setName("Test Endpoint 2");
    endpoint2.setUrl("http://example2.com");
    endpoint2.setMonitoredInterval(120);
    List<MonitoredEndpoint> endpoints = Arrays.asList(endpoint1, endpoint2);

    when(userRepository.findByAccessToken("testToken")).thenReturn(Optional.of(owner));
    when(endpointService.findByOwner(eq(owner))).thenReturn(endpoints);

    ResponseEntity<List<MonitoredEndpoint>> response = endpointController.listEndpoints(token);

    assertNotNull(response);
    assertEquals(HttpStatus.OK, response.getStatusCode());
    assertNotNull(response.getBody());
    assertEquals(2, response.getBody().size());
  }

  @Test
  public void deleteEndpointTest() {
    String token = "Bearer testToken";
    Long id = 1L;
    User owner = new User();
    MonitoredEndpoint endpointToDelete = new MonitoredEndpoint();
    endpointToDelete.setId(id);
    endpointToDelete.setName("Test Endpoint");
    endpointToDelete.setUrl("http://example.com");
    endpointToDelete.setMonitoredInterval(60);
    endpointToDelete.setOwner(owner);

    when(userRepository.findByAccessToken("testToken")).thenReturn(Optional.of(owner));
    when(endpointService.findById(eq(id))).thenReturn(endpointToDelete);

    ResponseEntity<Void> response = endpointController.deleteEndpoint(token, id);

    assertNotNull(response);
    assertEquals(HttpStatus.OK, response.getStatusCode());
  }
}