package com.project.CyShare.repository;

import com.project.CyShare.app.BookingDetails;
import com.project.CyShare.app.PassengerBookingDetails;
import org.springframework.data.repository.CrudRepository;

import javax.transaction.Transactional;

public interface PassengerBookingRepository  extends CrudRepository<PassengerBookingDetails, Long> {
    /**
     * Find booking details by id number.
     * @param id booking id number.
     * @return BookingDetails object.
     */
    PassengerBookingDetails findById(long id);

    /**
     * Delete booking details by id number.
     * @param id booking id number.
     */
    @Transactional
    void deleteById(long id);
}
