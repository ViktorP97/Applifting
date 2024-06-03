package com.vp.applifting.models;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import java.time.LocalDateTime;
import java.util.List;

@Entity
public class MonitoredEndpoint {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;
  private String name;
  private String url;
  private LocalDateTime dateOfCreation;
  private Integer monitoredInterval;
  @ManyToOne
  @JsonBackReference
  private User owner;

  @OneToMany(mappedBy = "monitoredEndpoint", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
  @JsonIgnore
  private List<MonitoringResult> monitoringResults;

  public MonitoredEndpoint() {

  }
  public MonitoredEndpoint(String name, String url, LocalDateTime dateOfCreation,
      Integer monitoredInterval) {
    this.name = name;
    this.url = url;
    this.dateOfCreation = dateOfCreation;
    this.monitoredInterval = monitoredInterval;
  }

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public String getUrl() {
    return url;
  }

  public void setUrl(String url) {
    this.url = url;
  }

  public LocalDateTime getDateOfCreation() {
    return dateOfCreation;
  }

  public void setDateOfCreation(LocalDateTime dateOfCreation) {
    this.dateOfCreation = dateOfCreation;
  }
  public Integer getMonitoredInterval() {
    return monitoredInterval;
  }

  public void setMonitoredInterval(Integer monitoredInterval) {
    this.monitoredInterval = monitoredInterval;
  }

  public User getOwner() {
    return owner;
  }

  public void setOwner(User owner) {
    this.owner = owner;
  }

  public List<MonitoringResult> getMonitoringResults() {
    return monitoringResults;
  }

  public void setMonitoringResults(List<MonitoringResult> monitoringResults) {
    this.monitoringResults = monitoringResults;
  }
}
