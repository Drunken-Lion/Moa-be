package com.moa.moa.api.address.address;

import com.moa.moa.api.address.address.domain.entity.Address;
import com.moa.moa.api.address.address.domain.persistence.AddressRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
@RequiredArgsConstructor
public class AddressProcessor {
    private final AddressRepository addressRepository;

    public Optional<Address> findAddressByIdAndDeletedAtIsNull(Long id) {
        return addressRepository.findAddressByIdAndDeletedAtIsNull(id);
    }
}
