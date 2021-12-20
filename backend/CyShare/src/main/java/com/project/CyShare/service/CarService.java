package com.project.CyShare.service;

import com.project.CyShare.app.Car;


import java.util.ArrayList;
import java.util.List;

import com.project.CyShare.repository.CarsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author Hugo Alvarez Valdivia
 */
@Service
public class CarService {

    /**
     * Initialize a car repository.
     */
    @Autowired
    private CarsRepository carsRepository;

    /**
     * Method used to find all currently saved cars in the database.
     *
     * @return List of all cars.
     */
    public List<Car> findAll() {
        Iterable<Car> it = carsRepository.findAll();
        ArrayList<Car> list = new ArrayList<Car>();
        it.forEach(list::add);
        return list;
    }

    /**
     * Method used to find a car by its id number.
     *
     * @param id Car id number.
     * @return Car object.
     */
    public Car findById(long id) {
        return carsRepository.findById(id);
    }

    /**
     * Method used to save a new car into the database.
     *
     * @param car Car object to be saved.
     */
    public void save(Car car) {
        carsRepository.save(car);
    }

    /**
     * Method to save multiple new cars into the database.
     *
     * @param cars List of Car objects.
     */
    public void saveAll(List<Car> cars) {
        carsRepository.saveAll(cars);
    }

    /**
     * Method used to delete a car from the database by its id number.
     *
     * @param id Car id number.
     */
    public void deleteById(long id) {
        carsRepository.deleteById(id);
    }


}

