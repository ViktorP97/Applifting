package com.vp.applifting.dtos;

public class MonitoredEndpointDto {

  private String name;
  private String url;
  private Integer monitoredInterval;

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

  public Integer getMonitoredInterval() {
    return monitoredInterval;
  }

  public void setMonitoredInterval(Integer monitoredInterval) {
    this.monitoredInterval = monitoredInterval;
  }
}
