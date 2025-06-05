package com.leo.vcv.repository;

import com.leo.vcv.model.InternationalTravel;
import com.leo.vcv.model.NationalTravel;
import com.leo.vcv.model.Travel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface TravelRepository extends JpaRepository<Travel, Long> {

    @Query("SELECT t FROM NationalTravel t WHERE t.travelType = 'NATIONAL'")
    List<NationalTravel> findAllByTravelTypeNational();

    @Query("SELECT t FROM InternationalTravel t WHERE t.travelType = 'INTERNATIONAL'")
    List<InternationalTravel> findAllByTravelTypeInternational();

    List<Travel> findAllByTravelType(Travel.TravelType type);
}