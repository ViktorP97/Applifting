package com.vp.applifting.services;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import com.vp.applifting.dtos.MonitoredEndpointDto;
import com.vp.applifting.models.MonitoredEndpoint;
import com.vp.applifting.models.User;
import com.vp.applifting.repositories.MonitoredEndpointRepository;
import com.vp.applifting.repositories.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@ExtendWith(MockitoExtension.class)
class MonitoredEndpointServiceTest {

  @Mock
  private MonitoredEndpointRepository endpointRepository;

  @Mock
  private UserRepository userRepository;

  @InjectMocks
  private MonitoredEndpointService endpointService;

  private User testUser;

  @BeforeEach
  void setUp() {
    testUser = new User();
    testUser.setId(1L);
    testUser.setUsername("testUser");
  }

  @Test
  public void saveEndpointTest() {
    MonitoredEndpointDto endpointDto = new MonitoredEndpointDto();
    endpointDto.setName("Test Endpoint");
    endpointDto.setMonitoredInterval(10);
    endpointDto.setUrl("http://example.com");

    MonitoredEndpoint savedEndpoint = new MonitoredEndpoint();
    savedEndpoint.setName("Test Endpoint");
    savedEndpoint.setMonitoredInterval(10);
    savedEndpoint.setUrl("http://example.com");
    savedEndpoint.setOwner(testUser);
    savedEndpoint.setDateOfCreation(LocalDateTime.now());

    when(endpointRepository.save(any(MonitoredEndpoint.class))).thenReturn(savedEndpoint);

    MonitoredEndpoint result = endpointService.save(testUser, endpointDto);

    assertEquals(savedEndpoint.getName(), result.getName());
    assertEquals(savedEndpoint.getMonitoredInterval(), result.getMonitoredInterval());
    assertEquals(savedEndpoint.getUrl(), result.getUrl());
    assertEquals(savedEndpoint.getOwner(), result.getOwner());
    assertEquals(savedEndpoint.getDateOfCreation().getDayOfYear(), result.getDateOfCreation().getDayOfYear());
  }

  @Test
  public void updateEndpointTest() {
    MonitoredEndpointDto updateDto = new MonitoredEndpointDto();
    updateDto.setName("Updated Name");
    updateDto.setMonitoredInterval(10);
    updateDto.setUrl("http://updated-example.com");

    MonitoredEndpoint existingEndpoint = new MonitoredEndpoint();
    existingEndpoint.setId(1L);
    existingEndpoint.setName("Old Name");
    existingEndpoint.setMonitoredInterval(5);
    existingEndpoint.setUrl("http://example.com");
    existingEndpoint.setDateOfCreation(LocalDateTime.now());
    existingEndpoint.setOwner(testUser);

    when(endpointRepository.findById(1L)).thenReturn(Optional.of(existingEndpoint));
    when(endpointRepository.save(any(MonitoredEndpoint.class))).thenAnswer(invocation -> invocation.getArgument(0));

    MonitoredEndpoint updatedEndpoint = endpointService.update(1L, testUser, updateDto);

    assertEquals("Updated Name", updatedEndpoint.getName());
    assertEquals(10, updatedEndpoint.getMonitoredInterval());
    assertEquals("http://updated-example.com", updatedEndpoint.getUrl());
    assertEquals(testUser, updatedEndpoint.getOwner());
  }

  @Test
  public void findEndpointByOwnerTest() {
    MonitoredEndpoint endpoint1 = new MonitoredEndpoint();
    endpoint1.setId(1L);
    endpoint1.setName("Endpoint 1");
    endpoint1.setOwner(testUser);

    MonitoredEndpoint endpoint2 = new MonitoredEndpoint();
    endpoint2.setId(2L);
    endpoint2.setName("Endpoint 2");
    endpoint2.setOwner(testUser);

    List<MonitoredEndpoint> endpoints = Arrays.asList(endpoint1, endpoint2);

    when(endpointRepository.findByOwner(testUser)).thenReturn(endpoints);

    List<MonitoredEndpoint> result = endpointService.findByOwner(testUser);

    assertEquals(2, result.size());
    assertEquals("Endpoint 1", result.get(0).getName());
    assertEquals("Endpoint 2", result.get(1).getName());
    assertEquals(testUser, result.get(0).getOwner());
    assertEquals(testUser, result.get(1).getOwner());
  }

  @Test
  public void findByIdTest() {
    MonitoredEndpoint testEndpoint = new MonitoredEndpoint();
    testEndpoint.setId(1L);
    testEndpoint.setName("Test Endpoint");
    testEndpoint.setMonitoredInterval(10);
    testEndpoint.setUrl("http://example.com");
    testEndpoint.setOwner(testUser);
    testEndpoint.setDateOfCreation(LocalDateTime.now());

    when(endpointRepository.findById(1L)).thenReturn(Optional.of(testEndpoint));

    MonitoredEndpoint foundEndpoint = endpointService.findById(1L);

    assertNotNull(foundEndpoint);
    assertEquals(testEndpoint.getId(), foundEndpoint.getId());
    assertEquals(testEndpoint.getName(), foundEndpoint.getName());
    assertEquals(testEndpoint.getMonitoredInterval(), foundEndpoint.getMonitoredInterval());
    assertEquals(testEndpoint.getUrl(), foundEndpoint.getUrl());
    assertEquals(testEndpoint.getOwner(), foundEndpoint.getOwner());
    assertEquals(testEndpoint.getDateOfCreation().getDayOfYear(), foundEndpoint.getDateOfCreation().getDayOfYear());
  }
}