package com.project.CyShare.Tools;

import com.github.javafaker.Faker;
import com.project.CyShare.app.BookingDetails;
import com.project.CyShare.app.Role;
import com.project.CyShare.app.Status;
import com.project.CyShare.app.User;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class BookingGenerator
{
    public BookingGenerator() {}

//    public List<BookingDetails> generate(User driver, User passenger)
//    {
//        Faker faker1 = new Faker();
//        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
//        SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm:ss");
//        Date date = new Date();
//        List<BookingDetails> bookingDetailsList = new ArrayList<>();
//
//        BookingDetails bookingDriver = new BookingDetails();
//        bookingDriver.setName(driver.getUserName());
//        bookingDriver.setDate(dateFormat.format(date));
//        bookingDriver.setTime(timeFormat.format(date));
//        bookingDriver.setRole(Role.DRIVER);
//        bookingDriver.setFullName(driver.getFullName());
//        bookingDriver.setIsBooked(Status.BOOKED);
//
//        BookingDetails bookingPassenger = new BookingDetails();
//        bookingPassenger.setName(passenger.getUserName());
//        bookingPassenger.setDate(dateFormat.format(date));
//        bookingPassenger.setTime(timeFormat.format(date));
//        bookingPassenger.setRole(Role.PASSENGER);
//        bookingPassenger.setFullName(passenger.getFullName());
//        bookingPassenger.setIsBooked(Status.BOOKED);
//
//        bookingDriver.setBookings(bookingPassenger);
//        bookingDetailsList.add(bookingDriver);
//        bookingDetailsList.add(bookingPassenger);
//
//        return bookingDetailsList;
//    }
}
