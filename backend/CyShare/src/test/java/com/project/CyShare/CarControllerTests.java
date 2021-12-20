package com.project.CyShare;

import com.project.CyShare.Tools.CarGenerator;
import com.project.CyShare.app.Car;
import com.project.CyShare.controller.CarController;
import com.project.CyShare.repository.CarsRepository;
import com.project.CyShare.service.CarService;
import com.project.CyShare.service.UserService;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.MockitoJUnitRunner;
import static org.mockito.Mockito.*;
import static org.junit.Assert.assertEquals;
import org.mockito.junit.MockitoJUnitRunner;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@RunWith(MockitoJUnitRunner.class)
public class CarControllerTests
{
    @InjectMocks
    CarController carController = new CarController();

    @Mock
    CarService carService;

    @Mock
    UserService userService;

    @Mock
    CarsRepository carsRepository;

    @Before
    public void init()
    {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void createCarTest()
    {
        Car car  = new Car("Mazda", "3", 2010, "Gray" , "aaabbb111");
        car.setId(1);
        assertEquals("Car(s) successfully added.",carController.createCar(car));
        car = null;
        assertEquals("Error, car could not be added (car == null)", carController.createCar(car));
    }

    @Test
    public void deleteCarTest()
    {
        Car car  = new Car("Mazda", "3", 2010, "Gray" , "aaabbb111");
        car.setId(1);
        when(carService.findById(car.getId())).thenReturn(car);
        assertEquals("Car deleted successfully", carController.deleteCar(car.getId()));
    }

    @Test
    public void getAllCarsTest() throws IOException
    {
        CarGenerator carGenerator = new CarGenerator();
        List<Car> cars = carGenerator.generate(5);
        when(carService.findAll()).thenReturn(cars);
        assertEquals(cars, carController.getAllCars());
    }

    @Test
    public void getCarById()
    {
        Car car  = new Car("Mazda", "3", 2010, "Gray" , "aaabbb111");
        car.setId(1);
        when(carService.findById(1)).thenReturn(car);
        assertEquals(car, carController.getCarById(1));
    }

    @Test
    public void saveAllTest()
    {
        List<Car> carList = new ArrayList<>();
        when(carsRepository.saveAll(Mockito.anyList())).thenAnswer(i -> i.getArguments()[0]);
        assertEquals(carList, carsRepository.saveAll(carList));
    }
}
