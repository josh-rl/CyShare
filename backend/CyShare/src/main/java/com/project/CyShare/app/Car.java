package com.project.CyShare.app;

import com.fasterxml.jackson.annotation.JsonIgnore;
import io.swagger.annotations.ApiModelProperty;

import javax.persistence.*;

/**
 * Car entity that holds car information as well as a link to a User entity (owner).
 * @author Hugo Alvarez Valdivia
 */

@Entity
public class Car
{
    /**
     * id: Number assigned to each car entity that
     * uniquely identifies them.
     */
    @ApiModelProperty(value = "id", example = "1")
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id")
    private long id;

    /**
     * make: Car manufacturer.
     */
    @ApiModelProperty(value = "make", example = "Mercedes-Benz")
    @Column(name = "make", nullable = false)
    private String make;

    /**
     * model: Specific car model.
     */
    @ApiModelProperty(value = "model", example = "C63 AMG")
    @Column(name = "model", nullable = false)
    private String model;

    /**
     * year: Car model year.
     */
    @ApiModelProperty(value = "year", example = "2013")
    @Column(name = "year", nullable = false)
    private int year;

    /**
     * color: Car color.
     */
    @ApiModelProperty(value = "color", example = "Silver")
    @Column(name = "color", nullable = false)
    private String color;

    /**
     * licensePlate: License plate number.
     */
    @ApiModelProperty(value = "licensePlate", example = "6TRJ244")
    @Column(name = "licensePlate", nullable = false)
    private String licensePlate;

    /**
     * user: The car's owner. OneToOne
     * relationship with User entity.
     */
    @ApiModelProperty(value = "user")
    @OneToOne(mappedBy = "car")
    @JsonIgnore
    private User user;

    /**
     * Basic Car Constructor
     * @param make Car make
     * @param model Car model
     * @param year Car year
     * @param color Car color
     * @param licensePlate License plate
     */
    public Car(String make, String model, int year, String color,String licensePlate)
    {
        this.make=make;
        this.model=model;
        this.year=year;
        this.color=color;
        this.licensePlate = licensePlate;
    }

    /**
     * Empty constructor. Used to test set methods.
     */
    public Car() { }

    /**
     * Method used to find the id number of a given car.
     * @return Unique ID of a given car entity.
     */
    public long getId() { return id; }

    /**
     * Set/change id for a given car.
     * @param id Given car's unique Id.
     */
    public void setId(long id) { this.id = id; }

    /**
     * Method used to get the make of the given car.
     * @return Make of the given car.
     */
    public String getMake()
    {
        return make;
    }

    /**
     * Set/change car make.
     * @param make Car make.
     */
    public void setMake(String make)
    {
        this.make = make;
    }

    /**
     * Method used to get the model of the given car.
     * @return Model of the given car.
     */
    public String getModel()
    {
        return model;
    }

    /**
     * Set/change car model.
     * @param model Model to set.
     */
    public void setModel(String model)
    {
        this.model = model;
    }

    /**
     * Method used to get the model year of a given car.
     * @return Car model year.
     */
    public int getYear()
    {
        return year;
    }

    /**
     * Set/change car model year.
     * @param year Year to set.
     */
    public void setYear(int year)
    {
        this.year = year;
    }

    /**
     * Method used to get the color of the given car.
     * @return Car color.
     */
    public String getColor()
    {
        return color;
    }

    /**
     * Set/change car color.
     * @param color Color to set.
     */
    public void setColor(String color)
    {
        this.color = color;
    }

    /**
     * Method used to find the license plate of the given car.
     * @return License plate for the given car.
     */
    public String getLicensePlate(){ return licensePlate; }

    /**
     * Set/change license plate.
     * @param licensePlate License plate to set.
     */
    public void setLicensePlate(String licensePlate){ this.licensePlate = licensePlate; }

    /**
     * Method used to find the owner of the given car.
     * @return Car owner.
     */
    public User getUser()
    {
        return user;
    }

    /**
     * Set/change owner of a given car.
     * @param user Owner to set.
     */
    public void setUser(User user)
    {
        this.user = user;
    }

    /**
     * toString method for testing purposes.
     * @return A formatted string with car information.
     */
    @Override
    public String toString()
    {
        return "\n\tcarId: " + this.id +  "\n\tmake: " + this.make + "\n\tmodel: " + this.model + "\n\tyear: " + this.year
                + "\n\tcolor: " + this.color + "\n\tlicensePlate: " + this.licensePlate + "\n";
    }

}
