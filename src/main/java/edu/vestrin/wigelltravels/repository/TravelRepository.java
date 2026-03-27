package edu.vestrin.wigelltravels.repository;

import edu.vestrin.wigelltravels.entity.Travel;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface TravelRepository extends JpaRepository<Travel, Long> {
}
