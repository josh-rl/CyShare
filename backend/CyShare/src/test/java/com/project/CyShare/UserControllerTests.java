package com.project.CyShare;


import com.project.CyShare.Tools.UserGenerator;
import com.project.CyShare.app.Car;
import com.project.CyShare.app.Role;
import com.project.CyShare.app.User;
import com.project.CyShare.controller.UserController;
import com.project.CyShare.repository.UsersRepository;
import com.project.CyShare.service.CarService;
import com.project.CyShare.service.UserService;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import static org.mockito.Mockito.*;
import static org.junit.Assert.assertEquals;
import org.mockito.junit.MockitoJUnitRunner;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@RunWith(MockitoJUnitRunner.class)
public class UserControllerTests
{
    @InjectMocks
    UserController userController = new UserController();

    @Mock
    UserService userService;

    @Mock
    CarService carService;

    @Before
    public void init()
    {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void addUserTest() throws Exception
    {
        User user = new User("hugoalval", "1234", "Hugo A", Role.ADMIN);
        user.setId(1);
        assertEquals("User(s) added successfully", userController.AddUser(user));
    }

    @Test
    public void deleteUserTest() throws Exception
    {
        User user = new User("hugoalval", "1234", "Hugo A", Role.ADMIN);
        user.setId(1);

        assertEquals("User(s) added successfully", userController.AddUser(user));
        verify(userService, times(1)).save(user);
        assertEquals("Users deleted successfully", userController.RemoveUser(user));
        verify(userService, times(1)).save(user);
    }

    @Test
    public void assignCarTest() throws Exception
    {
        User user = new User("hugoalval", "1234", "Hugo A", Role.ADMIN);
        Car car = new Car("Mazda", "3", 2010, "Gray", "aaabbb");

        when(userService.getUserByName(user.getUserName())).thenReturn(user);
        doNothing().when(userService).save(user);
        assertEquals("Car assigned Successfully", userController.assignCar(car, user.getUserName()));
    }

    @Test
    public void assignCarPathTest() throws Exception
    {
        User user = new User("hugoalval", "1234", "Hugo A", Role.ADMIN);
        user.setId(1);
        Car car = new Car("Mazda", "3", 2010, "Gray", "aaabbb");
        car.setId(1);

        System.out.println(user);

        when(userService.findById(user.getId())).thenReturn(user);
        when(carService.findById(car.getId())).thenReturn(car);
        doNothing().when(userService).save(user);
        assertEquals("Car assigned Successfully", userController.assignCarPath(1,1));
        System.out.println(user);
    }

    @Test
    public void updateRoleTest() throws Exception
    {
        User user = new User("hugoalval", "1234", "Hugo A", Role.ADMIN);
        user.setId(1);
        System.out.println(user);
        when(userService.findByUserName(user.getUserName())).thenReturn(user);

        assertEquals("Role updated successfully", userController.updateRole(user.getUserName(), "PASSENGER"));
        System.out.println(user);
    }

    @Test
    public void getAllUsers() throws Exception
    {
        UserGenerator userGenerator = new UserGenerator();
        List<User> users = userGenerator.generate(10);
        when(userService.findAll()).thenReturn(users);
        assertEquals(users, userController.GetAllUsers());
    }
}
