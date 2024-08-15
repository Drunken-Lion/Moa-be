package com.moa.moa.api.address.address.domain.persistence;

import com.moa.moa.api.address.address.domain.entity.Address;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AddressRepository extends JpaRepository<Address, Long> {
}
