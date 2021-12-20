package com.project.CyShare.app;


import com.fasterxml.jackson.annotation.JsonIgnore;

import io.swagger.annotations.ApiModelProperty;


import javax.persistence.*;
import java.awt.print.Book;
import java.util.ArrayList;
import java.util.List;

/**
 * Booking entity designed to hold all details regarding a new booking order.
 * @author Bhuwan Joshi
 */

@Entity
public class PassengerBookingDetails {

    /**
     * id: Number assigned to each booking that uniquely
     * identifies it.
     */
    @ApiModelProperty(value = "BId", example = "1")
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "BId")
    private long BId;

    /**
     *  Method used to check whether a booking is scheduled.
     * @return Booking status: NOT, BOOKED, or REJECTED.
     */
    public Status getIsBooked() {
        return isBooked;
    }

    /**
     *  Method used to update/set booking status.
     * @param isBooked Booking status: NOT, BOOKED, or REJECTED.
     */
    public void setIsBooked(Status isBooked) {
        this.isBooked = isBooked;
    }



    /**
     * Source and Destination coordinates.
     */
    @ApiModelProperty(value = "Location")
    @OneToMany(mappedBy = "passbook",cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Maps> Location = new ArrayList<>();


    public BookingDetails getBookings() {
        return bookings;
    }

    public void setBookings(BookingDetails bookings) {
        this.bookings = bookings;
    }

    @ManyToOne(fetch = FetchType.EAGER,cascade = CascadeType.ALL)
    @JoinColumn(name = "Requests")
    @JsonIgnore
    private BookingDetails bookings;



    /**
     * The userName For the particular user
     */
    @ApiModelProperty(value = "name", example = "John")
    @Column
    private String name;

    /**
     * The date of the booking
     */
    @ApiModelProperty(value = "date", example = "10/12/2021")
    @Column
    private String date;

    /**
     * The role of the user
     */
    @ApiModelProperty(value = "role", example = "DRIVER")
    @Column
    private Role role;

    /**
     * Method used to return the Booking status of the user
     * @return Booking status
     */
    public Status isBooked() {
        return isBooked;
    }

    /**
     * Sets The Status of the user, depending on the client
     * NOT,BOOKED,REJECTED
     * @param booked: Gets the type of Booking Status
     */
    public void setBooked(Status booked) {
        isBooked = booked;
    }

    /**
     * The time of the booking.
     */
    @Column
    @ApiModelProperty(value = "time", example = "14:02:31")
    private String time;

    /**
     * The status of the user Represented by the Enums
     */
    @Column
    private Status isBooked;

    /**
     * The Booking Id from the Database
     * @return The booking Id From the database.
     */
    public long getBId() {
        return BId;
    }

    /**
     * The methods return the list of the Location.
     * @return The List of source and destination of The user
     */

    public List<Maps> getLocation() {
        return Location;
    }

    /**
     * THe method sets the desired location based of on the client
     * @param location: Gets the location form the controller
     */

    public void setLocation(List<Maps> location) {
        Location  = new ArrayList<>();
        Location.add(location.get(0));
        Location.add(location.get(1));
    }


    /**
     * Sets the Booking Id of the User.
     * @param BId The booking Id from the client
     */
    public void setBId(long BId) {
        this.BId = BId;
    }

    //    public Maps getDest() {
//        return dest;
//    }
//
//    public void setDest(Maps dest) {
//        this.dest = dest;
//    }
//
//    public Maps getSrc() {
//        return src;
//    }
//
//    public void setSrc(Maps src) {
//        this.src = src;
//    }
    @Column
    String FullName;

    public String getFullName() {
        return FullName;
    }

    public void setFullName(String fullName) {
        FullName = fullName;
    }

    /**
     * Returns the user name of The user
     * @return user name of the user.
     */
    public String getName() {
        return name;
    }

    /**
     * Gets the form the client and set's it for the current user
     * @param name The user  name from the user
     */

    public void setName(String name) {
        this.name = name;
    }

    /**
     * Returns the date of the current booking
     * @return the date
     */
    public String getDate() {
        return date;
    }

    /**
     * Sets the date for the current user
     * @param date: the date from the user
     */
    public void setDate(String date) {
        this.date = date;
    }

    /**
     * Gets the role of user
     * @return Role in the form of an Enum
     */
    public Role getRole() {
        return role;
    }

    /**
     * Sets the role for the user
     * @param role Role in the form of an Enum
     */
    public void setRole(Role role) {
        this.role = role;
    }

    /**
     * Gets the time from the Database
     * @return time in the form of String
     */
    public String getTime() {
        return time;
    }

    /**
     * Sets the time form the user.
     * @param time in the form of String.
     */
    public void setTime(String time) {
        this.time = time;
    }
}
