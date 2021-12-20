package com.project.CyShare.controller;


import com.project.CyShare.app.*;
import com.project.CyShare.service.BookingService;
import com.project.CyShare.service.MapService;
import com.project.CyShare.service.PassengerBookingService;
import com.project.CyShare.service.UserService;
import io.swagger.annotations.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

/**
 * Controller used to perform tasks on current booking orders.
 * @author Bhuwan Joshi
 */

@Api(tags = "BookingController", value = "BookingController", description = "This controller " +
        " is used to create or modify booking details.")
@RestController
public class PassengerBookingController {
    /**
     * initialize a bookingService.
     */
    @Autowired
    PassengerBookingService service;
    @Autowired
    UserService usrService;
    @Autowired
    MapService mapService;
    @Autowired
    BookingService bookService;

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
    @GetMapping("/PassengerBooking/all")
    ArrayList<PassengerBookingDetails> getAllBooking()
    {
        return (ArrayList<PassengerBookingDetails>) service.findAll();
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
    @GetMapping("/PassengerBooking/{name}")
    List<PassengerBookingDetails> getDetailsByName(@PathVariable String name)
    {
        return service.findByname(name);
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
    @PostMapping("/PassengerBooking/add")
    String addPassengerBooking(@RequestBody PassengerBookingDetails data) throws Exception {

        PassengerBookingDetails booking = data;

        if(booking==null) return "Won't able to add";
        if(booking.getFullName()==null)
        {
        booking.setFullName("John Doe");
        }
        List<Maps> map = booking.getLocation();
        for(Maps m: map)
        {m.setPassbook(booking);}
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
    @PostMapping("/PassengerBooking/add/maps/{name}/{dateTime}")
    String addMaps(@RequestBody List<Maps> map,@PathVariable String name, @PathVariable String dateTime)
    {  if(name.equals("")||name==null) return "Unable to add";
        List<PassengerBookingDetails> usr = service.findByname(name);
        PassengerBookingDetails booking=null;
        for(PassengerBookingDetails b : usr)
        {String cmp=b.getDate()+b.getTime();

            if(cmp.equals(dateTime))
                booking=b;

        }
        if(booking==null) return "Unable to find user and add";
        service.save(booking);
        for(Maps m:map)
        {m.setPassbook(booking);}
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
    @PutMapping("/PassengerBooking/status/{id}/{status}")
    String addStatus(@PathVariable long user,  @PathVariable Status status )
    {
        PassengerBookingDetails booking = service.findById(user);
        booking.setIsBooked(status);
        service.save(booking);
        return "Added Succesfully";
    }

    @PutMapping("/PassengerBooking/delete/{id1}")
    String deleteBooking(@PathVariable long id1)
    {
        service.deleteBooking(id1);
        return "Booking Delete";
    }

    @PutMapping("/PassengerBooking/delete")
    String deleteAllBooking()
    {
        service.deleteAllBooking();
        return "All Booking Deleted";
    }


//    @PutMapping("/booking/add/{id1}/target/{id2}")
//    String addUser(@PathVariable long id2,@PathVariable long id1) {
//        try {
//            BookingDetails booking = service.findById(id1);
//            BookingDetails book = service.findById(id2);
//            booking.setRequests(book);
//            book.setBookings(booking);
//            service.save(booking);
//            return "Added " + book.getName() + "From the Booking to" + booking.getName();
//        } catch (Exception e) {
//            return "Unable to Add";
//        }
//    }

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
