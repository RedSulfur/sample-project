package com.spring.german.repository;

import com.spring.german.entity.Restaurants;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface RestaurantsRepository extends JpaRepository<Restaurants, Long> {
    @Query(value = "select restaurant from restaurants where foods @> CAST('{beef burger, pizza}' AS varchar[])", nativeQuery = true)
    List<String> getRestaurantsThatOfferSpecificFood();
}
