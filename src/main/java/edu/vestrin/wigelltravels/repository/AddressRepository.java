package edu.vestrin.wigelltravels.repository;

import edu.vestrin.wigelltravels.entity.Address;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AddressRepository extends JpaRepository<Address, Long> {
}
