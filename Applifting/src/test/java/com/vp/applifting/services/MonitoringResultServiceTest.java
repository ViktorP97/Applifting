package com.vp.applifting.services;

import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;

import com.vp.applifting.models.MonitoringResult;
import com.vp.applifting.models.MonitoredEndpoint;
import com.vp.applifting.repositories.MonitoringResultRepository;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;

@ExtendWith(MockitoExtension.class)
public class MonitoringResultServiceTest {

  @Mock
  private MonitoringResultRepository repository;

  @InjectMocks
  private MonitoringResultService service;

  @Test
  public void testFindTop10ByMonitoredEndpoint() {
    MonitoredEndpoint endpoint = new MonitoredEndpoint();
    List<MonitoringResult> expectedResult = new ArrayList<>();

    when(repository.findTop10ByMonitoredEndpointOrderByDateOfCheckDesc(endpoint)).thenReturn(expectedResult);

    List<MonitoringResult> actualResult = service.findTop10ByMonitoredEndpoint(endpoint);

    assertEquals(expectedResult, actualResult);
  }
}