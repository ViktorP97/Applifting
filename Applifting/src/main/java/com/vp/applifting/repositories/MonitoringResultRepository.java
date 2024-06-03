package com.vp.applifting.repositories;

import java.util.List;
import com.vp.applifting.models.MonitoredEndpoint;
import com.vp.applifting.models.MonitoringResult;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MonitoringResultRepository extends JpaRepository<MonitoringResult, Long> {

  List<MonitoringResult> findTop10ByMonitoredEndpointOrderByDateOfCheckDesc(
      MonitoredEndpoint monitoredEndpoint);
}
