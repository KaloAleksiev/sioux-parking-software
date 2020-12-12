package com.twilio.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.twilio.models.Driver;

public interface IDriverRepository extends JpaRepository<Driver, Long> {
    public Driver getByLicensePlate(String licensePlate);
}
