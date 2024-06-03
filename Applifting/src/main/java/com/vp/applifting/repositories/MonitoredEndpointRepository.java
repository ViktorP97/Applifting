package com.vp.applifting.repositories;

import java.util.List;
import com.vp.applifting.models.MonitoredEndpoint;
import com.vp.applifting.models.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MonitoredEndpointRepository extends JpaRepository<MonitoredEndpoint, Long> {
  List<MonitoredEndpoint> findByOwner(User owner);
}
