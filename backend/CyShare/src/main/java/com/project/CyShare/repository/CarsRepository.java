package com.project.CyShare.repository;

import com.project.CyShare.app.Car;
import org.springframework.data.repository.CrudRepository;
import javax.transaction.Transactional;

/**
 * @author Hugo Alvarez Valdivia
 */

public interface CarsRepository extends CrudRepository<Car, Long>
{
    /**
     * Find car by its id number.
     * @param id car id number.
     * @return Car object.
     */
    Car findById(long id);

    /**
     * Delete car by its id number.
     * @param id car id number.
     */
    @Transactional
    void deleteById(long id);
}
