package com.project.CyShare.controller;

import com.project.CyShare.Tools.CarGenerator;
import com.project.CyShare.app.Car;
import com.project.CyShare.service.CarService;
import io.swagger.annotations.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;

/**
 * This controller is used to perform certain operations regarding cars
 * (i.e., add, delete, or find a car).
 * @author Hugo Alvarez Valdivia
 */

@Api(tags = "CarController", value = "CarController", description = "This controller is used to perform certain operations" +
        " regarding cars (i.e., add, delete, or find a car).")
@RestController
public class CarController
{
    /**
     * Initialize a car service.
     */
    @Autowired
    CarService carService;

    /**
     * This mapping returns all cars currently saved in the database.
     * @return All cars saved.
     */
    @ApiOperation(value = "Return a list of all cars currently saved in the database.")
    @ApiImplicitParam(value = "No parameters needed, just use cars/all path")
    @ApiResponses(value =
            {
                    @ApiResponse(code = 401, message = "Not authorized!!"),
                    @ApiResponse(code = 403, message = "Forbidden"),
                    @ApiResponse(code = 404, message = "Oops... not found")
            })
    @GetMapping("cars/all")
    public List<Car> getAllCars() {
        return carService.findAll();
    }

    /**
     * Find a car by its id number.
     * @param id id of the car to be found.
     * @return Car object requested.
     */
    @ApiOperation(value = "Get a car by its id number.")
    @ApiImplicitParam(name = "id", value = "id number of the desired car.", paramType = "long")
    @ApiResponses(value =
            {
                    @ApiResponse(code = 401, message = "Not authorized!!"),
                    @ApiResponse(code = 403, message = "Forbidden"),
                    @ApiResponse(code = 404, message = "Oops... not found")
            })
    @GetMapping(path = "/cars/{id}")
    public Car getCarById(@PathVariable long id)
    {
        return carService.findById(id);
    }

    /**
     * This mapping adds a new Car entity into the database.
     * @param car Car to be added.
     * @return Success/failure message.
     */
    @ApiOperation(value = "Add a new car entity into the database.")
    @ApiResponses(value =
            {
                    @ApiResponse(code = 401, message = "Not authorized!"),
                    @ApiResponse(code = 403, message = "Forbidden!"),
                    @ApiResponse(code = 404, message = "Oops... not found")
            })
    @PostMapping("cars/add")
    public String createCar(@RequestBody Car car) {
        if (car == null) {
            return "Error, car could not be added (car == null)";
        }
        carService.save(car);
        return "Car(s) successfully added.";
    }

//    @PutMapping("cars/update/{id}/")
//    String updateCar(@PathVariable long id, @RequestBody Car newCarInfo) {
//        Car carToUpdate = carService.findById(id);
//        if (carToUpdate == null) {
//            return "Error, car not found.";
//        }
//        carToUpdate.setUser(newCarInfo.getUser());
//        carToUpdate.setMake(newCarInfo.getMake());
//        carToUpdate.setModel(newCarInfo.getModel());
//        carToUpdate.setYear(newCarInfo.getYear());
//        carToUpdate.setColor(newCarInfo.getColor());
//        carToUpdate.setLicensePlate(newCarInfo.getLicensePlate());
//        final Car updatedCar = carService.save(carToUpdate);
//        return "Successfully updated car.";
//    }

    /**
     * This mapping deletes a car from the database using its id
     * number to find it.
     * @param id id number of the car to be deleted.
     * @return Success/failure message.
     */
    @ApiOperation(value = "Delete a car entity from the database using its id number to find it")
    @ApiImplicitParam(name = "id", value = "Car id number", dataType = "long")
    @ApiResponses(value =
            {
                    @ApiResponse(code = 401, message = "Not authorized!"),
                    @ApiResponse(code = 403, message = "Forbidden!"),
                    @ApiResponse(code = 404, message = "Oops... not found")
            })
    @DeleteMapping("cars/delete/{id}")
    public String deleteCar(@PathVariable long id) {
        Car car = carService.findById(id);
        if (car == null) {
            return "Error, car not found.";
        }
        carService.deleteById(id);
        return "Car deleted successfully";
    }

    @PostMapping("cars/dev/add/{amount}")
    public String addFakeCars(@PathVariable int amount) throws IOException {
        if (amount == 0)
            return "No car(s) generated.";

        CarGenerator carGenerator = new CarGenerator();
        carService.saveAll(carGenerator.generate(amount));
        return "Car(s) generated successfully.";

    }
}