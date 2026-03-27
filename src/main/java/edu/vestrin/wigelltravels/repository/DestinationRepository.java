package edu.vestrin.wigelltravels.repository;

import edu.vestrin.wigelltravels.entity.Destination;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DestinationRepository extends JpaRepository<Destination, Long> {
}
