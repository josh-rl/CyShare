package com.project.CyShare.controller;


import com.project.CyShare.Tools.BookingGenerator;
import com.project.CyShare.Tools.CarGenerator;
import com.project.CyShare.Tools.UserGenerator;
import com.project.CyShare.app.*;
import com.project.CyShare.service.BookingService;
import com.project.CyShare.service.MapService;
import com.project.CyShare.service.PassengerBookingService;
import com.project.CyShare.service.UserService;
import io.swagger.annotations.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Controller used to perform tasks on current booking orders.
 * @author Bhuwan Joshi
 */

@Api(tags = "BookingController", value = "BookingController", description = "This controller " +
        " is used to create or modify booking details.")
@RestController
public class BookingController {
    /**
     * initialize a bookingService.
     */
    @Autowired
    BookingService service;
    @Autowired
    UserService usrService;
    @Autowired
    MapService mapService;
    @Autowired
    PassengerBookingService passService;

    /**
     * This mapping returns a list of all bookings saved.
     * @return List of all bookings.
     */
    @ApiOperation(value = "Get list of all bookings saved.", response = Iterable.class)
    @ApiImplicitParam("No input parameters, just call using path booking/all")
    @ApiResponses(value =
            {
                    @ApiResponse(code = 200, message = "Success!!"),
                    @ApiResponse(code = 401, message = "Not authorized!"),
                    @ApiResponse(code = 403, message = "Forbidden!"),
                    @ApiResponse(code = 404, message = "Oops... Not found")
            })
    @GetMapping("/booking/all")
    ArrayList<BookingDetails> getAllBooking()
    {
        return (ArrayList<BookingDetails>) service.findAll();
    }

    /**
     * This mapping gets the booking details by name.
     * @param name Booking name.
     * @return BookingDetails object.
     */
    @ApiOperation(value = "Get booking details by name.")
    @ApiImplicitParam(name = "name", value = "Booking name", paramType = "string")
    @ApiResponses(value =
            {
                    @ApiResponse(code = 401, message = "Not authorized!"),
                    @ApiResponse(code = 403, message = "Forbidden!"),
                    @ApiResponse(code = 404, message = "Oops... not found")
            })
    @GetMapping("/booking/{name}")
    List<BookingDetails> getDetailsByName(@PathVariable String name)
    {
        return service.findByname(name);
    }
    @GetMapping("/booking/travellers/{id}")
   List<PassengerBookingDetails> getCloserBookings(@PathVariable long id)
    {
        BookingDetails b = service.findById(id);
        List<PassengerBookingDetails> toReturn = new ArrayList<>();
        double srclat1 = b.getLocation().get(0).getLatitude();
        double srclon1 = b.getLocation().get(0).getLongitude();
        double destlat1 = b.getLocation().get(1).getLatitude();
        double destlon1 = b.getLocation().get(1).getLongitude();
        for(PassengerBookingDetails booking:passService.findAll())
        {   if(booking.isBooked()==null||booking.isBooked()!=Status.BOOKED) {
            if (booking.getDate().equals(b.getDate())) {
                if (b.getBId() == booking.getBId()) continue;
                double srclat2 = booking.getLocation().get(0).getLatitude();
                double srclon2 = booking.getLocation().get(0).getLongitude();
                double destlat2 = booking.getLocation().get(1).getLatitude();
                double destlon2 = booking.getLocation().get(1).getLongitude();
                if (service.distance(srclat1, srclat2, srclon1, srclon2) <= 50) {
                    if (service.distance(destlat1, destlat2, destlon1, destlon2) <= 50) {
                        toReturn.add(booking);
                    }
                }
            }
        }
        }
        return toReturn;

    }

    /**
     * This mapping is used to add a new booking into the database.
     * @param data Booking details to add.
     * @return Success/failure message.
     */
    @ApiOperation(value = "Add a new booking entity to the database.")
    @ApiResponses(value =
            {
                    @ApiResponse(code = 401, message = "Not authorized!"),
                    @ApiResponse(code = 403, message = "Forbidden!"),
                    @ApiResponse(code = 404, message = "Oops... not found")
            })
    @PostMapping("/booking/add")
    String addBooking(@RequestBody BookingDetails data) throws Exception {

        BookingDetails booking = data;

        if(booking==null) return "Won't able to add";
        if(booking.getFullName()==null)
        {booking.setFullName("John Doe");}
     List<Maps> map = booking.getLocation();
      for(Maps m: map)
        {m.setBook(booking);}
        booking.setLocation(map);
        service.save(booking);


        return "Booking Created Successfully";
    }

    /**
     * This mapping is used to assign a location to a given user.
     * @param map Location to be assigned.
     * @return Success/failure message.
     */
    @ApiOperation(value = "Assign location to a given user.")
    @ApiResponses(value =
            {
                    @ApiResponse(code = 401, message = "Not authorized!"),
                    @ApiResponse(code = 403, message = "Forbidden!"),
                    @ApiResponse(code = 404, message = "Oops... not found")
            })
    @ApiImplicitParam(name = "userName", value = "userName of the user that needs location updated.")
    @PostMapping("/booking/add/maps/{name}/{dateTime}")
    String addMaps(@RequestBody List<Maps> map,@PathVariable String name, @PathVariable String dateTime)
    {
        if(name.equals("")||name==null) return "Unable to add";
        List<BookingDetails> usr = service.findByname(name);
        BookingDetails booking=null;
        for(BookingDetails b : usr)
        {String cmp=b.getDate()+b.getTime();

            if(cmp.equals(dateTime))
                booking=b;

        }
        if(booking==null) return "Unable to find user and add";
        service.save(booking);
        for(Maps m:map)
        {m.setBook(booking);}
        booking.setLocation(map);
        service.save(booking);

        return "Maps added to the Booking";
    }

    /**
     * This mapping is used to set the status of a booking.
     * @param user userName.
     * @param status new booking status.
     * @return Success/failure message.
     */
    @ApiOperation(value = "Set status for a given booking entity.")
    @ApiImplicitParams(value =
            {
                    @ApiImplicitParam(name = "user", value = "userName", dataType = "string"),
                    @ApiImplicitParam(name = "status", value = "status to assign", dataType = "Status")
            })
    @ApiResponses(value =
            {
                    @ApiResponse(code = 401, message = "Not authorized!"),
                    @ApiResponse(code = 403, message = "Forbidden!"),
                    @ApiResponse(code = 404, message = "Oops... not found")
            })
    @PutMapping("/booking/status/{id}/{status}")
    String addStatus(@PathVariable long user,  @PathVariable Status status )
    {
        BookingDetails booking = service.findById(user);
        booking.setIsBooked(status);
        service.save(booking);
        return "Added Succesfully";
    }
//    @PutMapping("/booking/delete/{id1}/target/{id2}")
//    String deleteUser(@PathVariable long id2,@PathVariable long id1)
//    {   BookingDetails booking = service.findById(id1);
//        PassengerBookingDetails book = passService.findById(id2);
//        booking.getRequests().remove(book);
//        book.setBookings(null);
//        service.save(booking);
//        return "User removed form the Booking";
//    }

    @PutMapping("/booking/delete/{id1}")
    String deleteBooking(@PathVariable long id1)
    {
        service.deleteBooking(id1);
        return "Booking Delete";
    }

    @PutMapping("/booking/delete")
    String deleteAllBooking()
    {
        service.deleteAllBooking();
        return "All Booking Deleted";
    }

    @PutMapping("/booking/add/{id1}/target/{id2}")
    String addUser(@PathVariable long id2,@PathVariable long id1) {
        try {
            BookingDetails booking = service.findById(id1);
            PassengerBookingDetails book = passService.findById(id2);
            booking.setIsBooked(Status.BOOKED);
            book.setBooked(Status.BOOKED);
            booking.setRequests(book);
           book.setBookings(booking);
            service.save(booking);
            passService.save(book);
            return "Added " + book.getName() + "From the Booking to" + booking.getName();
        } catch (Exception e) {
return "Unable to Add";
        }
    }
    @GetMapping("/Booking/Driver/{id}")
   List< BookingDetails> getDriver(@PathVariable long id)
    {
        long bid = passService.findById(id).getBookings().getBId();
        List<BookingDetails> toReturn = new ArrayList<>();
        toReturn.add(service.findById(bid));
        return toReturn;


    }
    @GetMapping("/Booking/Passenger/{id}")
    List<PassengerBookingDetails> getPassengers(@PathVariable long id)
    {
        return (service.findById(id)).getRequests();
    }


//    @PostMapping("/booking/dev/add/{amount}")
//    public String addFakeBooking(@PathVariable int amount) throws IOException {
//        if (amount <= 0)
//            return "Error, not booking generated.";
//
//        UserGenerator userGenerator = new UserGenerator();
//        CarGenerator carGenerator = new CarGenerator();
//        BookingGenerator bookingGenerator = new BookingGenerator();
//        List<BookingDetails> bookingDetailsList = new ArrayList<>();
//
//        for (int i = 0; i < amount; i++)
//        {
//            User driver = userGenerator.generateDriver();
//            User passenger = userGenerator.generatePassenger();
//            driver.setCar(carGenerator.generate(1).get(0));
//            passenger.setCar(carGenerator.generate(1).get(0));
//            List<BookingDetails> temp = bookingGenerator.generate(driver, passenger);
//            bookingDetailsList.add(temp.get(0));
//            bookingDetailsList.add(temp.get(1));
//        }
//
//        service.saveAll(bookingDetailsList);
//        return "Bookings generated successfully.";
    
//    }

}
