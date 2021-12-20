package com.project.CyShare;

import static com.project.CyShare.app.Role.DRIVER;
import static com.project.CyShare.app.Role.PASSENGER;
import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.*;

import java.util.ArrayList;
import java.util.List;

import com.project.CyShare.Tools.PasswordEncoder;
import com.project.CyShare.app.*;
import com.project.CyShare.controller.BookingController;
import com.project.CyShare.repository.BookingRepository;
import com.project.CyShare.repository.PassengerBookingRepository;
import com.project.CyShare.service.BookingService;
import com.project.CyShare.service.PassengerBookingService;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.project.CyShare.repository.UsersRepository;
import com.project.CyShare.service.UserService;
import org.mockito.junit.MockitoJUnitRunner;

/**
 * @author Hugo Alvarez Valdivia
 */
@RunWith(MockitoJUnitRunner.class)
public class BookingTests {

    @InjectMocks
    BookingService bookingServicee;
    @InjectMocks
    PassengerBookingService passengerBookingService;


    @InjectMocks
    BookingController book = new BookingController();

    @Mock
    BookingRepository repo;
    @Mock
    PassengerBookingRepository passengerbookingRepository;

    @Before
    public void init() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void getClosestTest() throws Exception {

        PassengerBookingDetails p = new PassengerBookingDetails();
        BookingDetails b = new BookingDetails();
        p.setFullName("Hugo");
        p.setRole(PASSENGER);


        Maps map = new Maps(41.7508391, -93.63191309999999);
        Maps map1 = new Maps(41.7508391, -93.63191309999999);
        List<Maps> mapslist = new ArrayList<>();
        mapslist.add(map);
        mapslist.add(map1);
        p.setDate("10/12/2010");
        b.setFullName("Bhuwan");
        p.setLocation(mapslist);
        b.setRole(DRIVER);
        b.setLocation(mapslist);
        b.setDate("10/12/2010");
        b.setBId(1);
        p.setBId(2);
        List<BookingDetails> boo = new ArrayList<>();
        List<PassengerBookingDetails> poo = new ArrayList<>();
        boo.add(b);
        poo.add(p);
        bookingServicee.passengerBookingService = passengerBookingService;
        //when(repo.findAll()).thenReturn(boo);
        //when(repo.findById(1)).thenReturn(b);
        when(passengerbookingRepository.findAll()).thenReturn(poo);
        //when(bookingServicee.findAll()).thenReturn(boo);
        when(bookingServicee.findById(1)).thenReturn(b);
        when(passengerBookingService.findAll()).thenReturn(poo);
       // when(passengerBookingService.findById(2)).thenReturn(p);
        assertEquals(bookingServicee.getCloserBookings(1),poo);

    }



}
