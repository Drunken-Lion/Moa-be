package com.moa.moa.api.place.liftticket.domain.persistence;

import com.moa.moa.api.place.amenity.domain.entity.Amenity;
import com.moa.moa.api.place.liftticket.domain.entity.LiftTicket;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LiftTicketRepository extends JpaRepository<LiftTicket, Long> {
}
