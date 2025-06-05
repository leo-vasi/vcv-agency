package com.leo.vcv.repository;

import com.leo.vcv.model.Accommodation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.List;

@Repository
public interface AccommodationRepository extends JpaRepository<Accommodation, Long> {

    @Query("SELECT a FROM Accommodation a WHERE " +
            "(:hotelName IS NULL OR a.hotelName LIKE %:hotelName%) AND " +
            "(:minCategory IS NULL OR a.category >= :minCategory) AND " +
            "(:maxDailyRate IS NULL OR a.dailyRate <= :maxDailyRate) AND " +
            "(:hasBreakfast IS NULL OR a.hasBreakfast = :hasBreakfast) AND " +
            "(:hasPool IS NULL OR a.hasPool = :hasPool) AND " +
            "(:hasWifi IS NULL OR a.hasWifi = :hasWifi)")
    List<Accommodation> findByCriteria(
            @Param("hotelName") String hotelName,
            @Param("minCategory") Integer minCategory,
            @Param("maxDailyRate") BigDecimal maxDailyRate,
            @Param("hasBreakfast") Boolean hasBreakfast,
            @Param("hasPool") Boolean hasPool,
            @Param("hasWifi") Boolean hasWifi);

    List<Accommodation> findByCategoryGreaterThanEqual(Integer minCategory);

    @Query("SELECT a FROM Accommodation a WHERE a.dailyRate BETWEEN :minRate AND :maxRate")
    List<Accommodation> findByDailyRateRange(
            @Param("minRate") BigDecimal minRate,
            @Param("maxRate") BigDecimal maxRate);

    List<Accommodation> findByHasWifiTrueOrderByDailyRateAsc();
}
