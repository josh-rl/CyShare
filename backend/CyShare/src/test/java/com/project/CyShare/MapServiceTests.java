package com.project.CyShare;
import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;

import com.project.CyShare.app.Car;
import com.project.CyShare.repository.CarsRepository;
import com.project.CyShare.service.UserService;
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

public class MapServiceTests {
    private UserService service;
    //private NyService myService;
    @InjectMocks
    UserService usrService;

    @Mock
    UsersRepository repo;

    @Before
    public void init() {
        MockitoAnnotations.openMocks(this);


    }
    @Test
    public void getList()
    {

    }


}
