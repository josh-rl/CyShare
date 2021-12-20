package com.project.CyShare.repository;

import com.project.CyShare.app.Car;
import com.project.CyShare.app.Maps;
import com.project.CyShare.app.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.CrudRepository;

import javax.transaction.Transactional;

/**
 * @author Bhuwan Joshi
 */
public interface MapsRepository extends CrudRepository<Maps, Long>
{
    /**
     * Find maps by its id.
     * @param id maps id number.
     * @return Maps Object.
     */
    Maps findById(long id);

    /**
     * Delete maps by its id number.
     * @param id maps id number.
     */
    @Transactional
    void deleteById(long id);
}
