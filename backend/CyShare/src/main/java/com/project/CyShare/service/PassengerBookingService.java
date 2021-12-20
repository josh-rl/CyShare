package com.project.CyShare.service;

import com.project.CyShare.Tools.Trie;
import com.project.CyShare.app.BookingDetails;
import com.project.CyShare.app.Car;
import com.project.CyShare.app.PassengerBookingDetails;
import com.project.CyShare.app.Role;
import com.project.CyShare.repository.BookingRepository;
import com.project.CyShare.repository.PassengerBookingRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.awt.print.Book;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * @author Bhuwan Joshi
 */

@Service
public class PassengerBookingService
{
    /**
     * initialize a booking repository.
     */
    @Autowired
    private PassengerBookingRepository passengerbookingRepository;

    /**
     * Method used to find all current bookings.
     * @return List of all bookings.
     */
    public List<PassengerBookingDetails> findAll()
    {

        List<PassengerBookingDetails> list = new ArrayList<PassengerBookingDetails>((Collection<? extends PassengerBookingDetails>) passengerbookingRepository.findAll());
        return list;
    }

    /**
     * Method used to find booking by name.
     * @param name Name.
     * @return BookingDetails object.
     */
    public List<PassengerBookingDetails> findByname(String name)
    {
        List<PassengerBookingDetails>  list = findAll();
        List<PassengerBookingDetails>  toreturn= new ArrayList<>();

        for(PassengerBookingDetails b: list)
        {
            if(b.getName().equals(name)) toreturn.add(b);
        }
        return toreturn;
    }

    public PassengerBookingDetails findById(long id)
    {
        return passengerbookingRepository.findById(id);
    }
    public PassengerBookingDetails findBynameAndDate(String name,String date)
    {
        List<PassengerBookingDetails>  list = findAll();
        for(PassengerBookingDetails b: list)
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
    public void save(PassengerBookingDetails booking)
    {
        passengerbookingRepository.save(booking);
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
        passengerbookingRepository.deleteById(id);
    }
    public void deleteAllBooking()
    {
        for(PassengerBookingDetails b : passengerbookingRepository.findAll())
            passengerbookingRepository.deleteById(b.getBId());
    }

    public void saveAll(List<PassengerBookingDetails> bookings)
    {
        passengerbookingRepository.saveAll(bookings);
    }
}


