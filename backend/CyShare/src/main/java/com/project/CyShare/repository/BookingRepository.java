package com.project.CyShare.repository;

import com.project.CyShare.app.BookingDetails;

import org.springframework.data.repository.CrudRepository;

import javax.transaction.Transactional;

/**
 * @author Bhuwan Joshi
 */

public interface BookingRepository extends CrudRepository<BookingDetails, Long>
{
    /**
     * Find booking details by id number.
     * @param id booking id number.
     * @return BookingDetails object.
     */
    BookingDetails findById(long id);

    /**
     * Delete booking details by id number.
     * @param id booking id number.
     */
    @Transactional
    void deleteById(long id);
}
