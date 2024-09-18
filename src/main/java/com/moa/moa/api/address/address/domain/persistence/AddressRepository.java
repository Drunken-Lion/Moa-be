package com.moa.moa.api.address.address.domain.persistence;

import com.moa.moa.api.address.address.domain.entity.Address;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface AddressRepository extends JpaRepository<Address, Long> {
    Optional<Address> findAddressByIdAndDeletedAtIsNull(Long id);
}
