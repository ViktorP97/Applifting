package com.vp.applifting.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import java.time.LocalDateTime;

@Entity
public class MonitoringResult {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;
  private LocalDateTime dateOfCheck;
  private Integer returnedHttpStatusCode;
  @Column(columnDefinition = "TEXT")
  private String returnedPayload;
  @ManyToOne
  @JsonIgnore
  private MonitoredEndpoint monitoredEndpoint;

  public MonitoringResult() {

  }
  public MonitoringResult(LocalDateTime dateOfCheck, Integer returnedHttpStatusCode,
      String returnedPayload) {
    this.dateOfCheck = dateOfCheck;
    this.returnedHttpStatusCode = returnedHttpStatusCode;
    this.returnedPayload = returnedPayload;
  }

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public LocalDateTime getDateOfCheck() {
    return dateOfCheck;
  }

  public void setDateOfCheck(LocalDateTime dateOfCheck) {
    this.dateOfCheck = dateOfCheck;
  }

  public Integer getReturnedHttpStatusCode() {
    return returnedHttpStatusCode;
  }

  public void setReturnedHttpStatusCode(Integer returnedHttpStatusCode) {
    this.returnedHttpStatusCode = returnedHttpStatusCode;
  }

  public String getReturnedPayload() {
    return returnedPayload;
  }

  public void setReturnedPayload(String returnedPayload) {
    this.returnedPayload = returnedPayload;
  }

  public MonitoredEndpoint getMonitoredEndpoint() {
    return monitoredEndpoint;
  }

  public void setMonitoredEndpoint(MonitoredEndpoint monitoredEndpoint) {
    this.monitoredEndpoint = monitoredEndpoint;
  }
}
