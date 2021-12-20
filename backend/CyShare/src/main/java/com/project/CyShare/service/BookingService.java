package com.project.CyShare.service;

import com.project.CyShare.Tools.Trie;
import com.project.CyShare.app.*;
import com.project.CyShare.repository.BookingRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PathVariable;

import java.awt.print.Book;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * @author Bhuwan Joshi
 */

@Service
public class BookingService
{
    /**
     * initialize a booking repository.
     */
    @Autowired
    private BookingRepository bookingRepository;

    @Autowired
    public PassengerBookingService passengerBookingService;


    /**
     * Method used to find all current bookings.
     * @return List of all bookings.
     */
    public List<BookingDetails> findAll()
    {

        List<BookingDetails> list = new ArrayList<BookingDetails>((Collection<? extends BookingDetails>) bookingRepository.findAll());
        return list;
    }

    /**
     * Method used to find booking by name.
     * @param name Name.
     * @return BookingDetails object.
     */
    public List<BookingDetails> findByname(String name)
    {
        List<BookingDetails>  list = findAll();
        List<BookingDetails>  toreturn= new ArrayList<>();

        for(BookingDetails b: list)
        {
            if(b.getName().equals(name)) toreturn.add(b);
        }
        return toreturn;
    }

    public BookingDetails findById(long id)
    {
       return bookingRepository.findById(id);
    }
    public BookingDetails findBynameAndDate(String name,String date)
    {
        List<BookingDetails>  list = findAll();
        for(BookingDetails b: list)
        {
            if(b.getName().equals(name))
            {
               if( b.getDate().equals(date)) return b;
            }
        }
        return null;
    }

    /**
     * Method used to save booking details into the database.
     * @param booking BookingDetails object.
     */
    public void save(BookingDetails booking)
    {
        bookingRepository.save(booking);
    }


    public  double distance(double lat1,
                                  double lat2, double lon1,
                                  double lon2)
    {

        // The math module contains a function
        // named toRadians which converts from
        // degrees to radians.
        lon1 = Math.toRadians(lon1);
        lon2 = Math.toRadians(lon2);
        lat1 = Math.toRadians(lat1);
        lat2 = Math.toRadians(lat2);

        // Haversine formula
        double dlon = lon2 - lon1;
        double dlat = lat2 - lat1;
        double a = Math.pow(Math.sin(dlat / 2), 2)
                + Math.cos(lat1) * Math.cos(lat2)
                * Math.pow(Math.sin(dlon / 2),2);

        double c = 2 * Math.asin(Math.sqrt(a));

        // Radius of earth in kilometers. Use 3956
        // for miles
        double r = 3956;

        // calculate the result
        return(c * r);
    }
    public void deleteBooking(long id)
    {
        bookingRepository.deleteById(id);
    }
    public void deleteAllBooking()
    {
        for(BookingDetails b : bookingRepository.findAll())
        bookingRepository.deleteById(b.getBId());
    }

    public void saveAll(List<BookingDetails> bookings)
    {
        bookingRepository.saveAll(bookings);
    }


    public List<PassengerBookingDetails> getCloserBookings( long id)
    {

        BookingDetails b = findById(id);
        List<PassengerBookingDetails> toReturn = new ArrayList<>();
        double srclat1 = b.getLocation().get(0).getLatitude();
        double srclon1 = b.getLocation().get(0).getLongitude();
        double destlat1 = b.getLocation().get(1).getLatitude();
        double destlon1 = b.getLocation().get(1).getLongitude();
        for (PassengerBookingDetails booking : passengerBookingService.findAll()) {
            if (booking.isBooked() == null || booking.isBooked() != Status.BOOKED) {
                if (booking.getDate().equals(b.getDate())) {
                    if (b.getBId() == booking.getBId()) continue;
                    double srclat2 = booking.getLocation().get(0).getLatitude();
                    double srclon2 = booking.getLocation().get(0).getLongitude();
                    double destlat2 = booking.getLocation().get(1).getLatitude();
                    double destlon2 = booking.getLocation().get(1).getLongitude();
                    if (distance(srclat1, srclat2, srclon1, srclon2) <= 50) {
                        if (distance(destlat1, destlat2, destlon1, destlon2) <= 50) {
                            toReturn.add(booking);
                        }
                    }
                }
            }
        }
        return toReturn;



    }
    }
