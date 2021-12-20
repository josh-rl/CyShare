package com.project.CyShare;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;

import com.project.CyShare.app.Car;
import com.project.CyShare.repository.CarsRepository;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.project.CyShare.app.Role;
import com.project.CyShare.app.User;
import com.project.CyShare.repository.UsersRepository;
import com.project.CyShare.service.CarService;
import org.mockito.junit.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class CarServiceTests
{

    @InjectMocks
    CarService carService;

    @Mock
    CarsRepository repo;

    @Before
    public void init()
    {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void getCarByIdTest()
    {
        when(repo.findById(1)).thenReturn(new Car("Toyota","Corolla",2018,"Green","COMSNERD"));

        Car car = carService.findById(1);

        assertEquals("Toyota", car.getMake());
        assertEquals("Corolla", car.getModel());
        assertEquals(2018, car.getYear());
        assertEquals("Green", car.getColor());
        assertEquals("COMSNERD", car.getLicensePlate());
    }

    @Test
    public void getAllCarsTest()
    {

        List<Car> list = new ArrayList<Car>();
        Car carOne = new Car("Toyota","Corolla",2018,"Green","COMSNERD");
        Car carTwo = new Car("Toyota","Camary",2021,"White","DUMB");
        Car carThree = new Car("HONDA","ACCORD",2020,"Black","BRO");

        list.add(carOne);
        list.add(carTwo);
        list.add(carThree);

        when(carService.findAll()).thenReturn(list);

        List<Car> carList = carService.findAll();

        assertEquals(3, carList.size());
        verify(repo, times(1)).findAll();
    }

}