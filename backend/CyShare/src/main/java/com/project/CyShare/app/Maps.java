package com.project.CyShare.app;

import com.fasterxml.jackson.annotation.JsonIgnore;
import io.swagger.annotations.ApiModelProperty;

import javax.persistence.*;
import java.awt.print.Book;

/**
 * Map entity class that is used to hold location coordinates in the form of
 * latitude and longitude. It's linked to a User entity.
 * @author Bhuwan Joshi
 */
@Entity
public class Maps
{
    /**
     * Number assigned to each map that uniquely identifies them
     */
    @ApiModelProperty(value = "id", example = "1")
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private long id;

    /**
     * The latitude of the Coordinate
     */
    @ApiModelProperty(value = "latitude", example = "42.025")
    @Column(name = "latitude", nullable = false)
    private double Latitude;

    /**
     * The longitude of the Coordinate
     */
    @ApiModelProperty(value = "longitude", example = "-93.664")
    @Column(name = "longitude", nullable = false)
    private double Longitude;


    /**
     * The user Associated with the map
     */
    @ApiModelProperty(value = "user")
    @OneToOne(mappedBy = "map")
    @JsonIgnore
    private User user;

    public PassengerBookingDetails getPassbook() {
        return passbook;
    }

    public void setPassbook(PassengerBookingDetails passbook) {
        this.passbook = passbook;
    }

    /**
     * The booking associated with the map, as a user can have multiple maps
     * For source and destination
     */
    @ApiModelProperty(value = "book")
    @ManyToOne(fetch = FetchType.LAZY,cascade = CascadeType.ALL)
    @JoinColumn(name = "booking_Id ")
    @JsonIgnore

    private BookingDetails book;
    @ApiModelProperty(value = "book")
    @ManyToOne(fetch = FetchType.LAZY,cascade = CascadeType.ALL)
    @JoinColumn(name = "Passbooking_Id ")
    @JsonIgnore
    private PassengerBookingDetails passbook;

    /**
     * Gets the booking Details associated with the map.
     * @return booking associated with the current map.
     */
    public BookingDetails getBook() {
        return book;
    }

    /**
     * Sets the Booking details for the user
     * @param book Booking details to set
     */
    public void setBook(BookingDetails book) {
        this.book = book;
    }

    /**
     *  Constructor for initialising the Latitude and the Longitude
     * @param Longitude Longitude in the form of String
     * @param Latitude Latitude in the form of String
     */
    public Maps(double Latitude, double Longitude)
    {
        this.Latitude = Latitude;
        this.Longitude = Longitude;
    }

    /**
     * Empty Constructor to test setter methods.
     */
    public Maps() { }

    /**
     *  Gets the Id of the map
     * @return The ID as a number
     */
    public long getId() { return id; }

    /**
     * Sets the Id of the map
     * @param id Id as a number
     */
    public void setId(long id) { this.id = id; }

    /**
     * Gets the Latitude of the Map
     * @return Latitude as a String
     */
    public double getLatitude()
    {
        return Latitude;
    }

    /**
     * Set the Longitude of the map
     * @param Latitude in the Form of String
     */
    public void setLongitude(double Longitude)
    {
        this.Longitude = Longitude;
    }

    /**
     * Gets the Longitude of the Map
     * @return Longitude in the form of String
     */
    public double getLongitude()
    {
        return Longitude;
    }

    /**
     * Sets the Longitude
     * @param Longitude in the Form of String
     */

    public void setLatitude(double Latitude)
    {
        this.Latitude = Latitude;
    }

    /**
     * Gets the User related to the Map
     * @return the User connected to the  map
     */

    public User getUser()
    {
        return user;
    }

    /**
     * Sets the User related to the Map
     * @param user User to set according to the Map
     */
    public void setUser(User user)
    {
        this.user = user;
    }





}

