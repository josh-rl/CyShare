package com.project.CyShare.app;

import io.swagger.annotations.ApiModelProperty;

import javax.persistence.*;

/**
 * User entity class.
 * Holds id number, userName, password, full name, and is linked to a car object and location.
 * @author Hugo Alvarez Valdivia
 */

@Entity
public class User {

    /**
     * id: Number assigned to each user that uniquely
     * identifies them.
     */
    @ApiModelProperty(value = "id", example = "1")
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private long id;

    /**
     * userName: String created by the user to identify
     * their account.
     */
    @ApiModelProperty(value = "userName", example = "hugoalval")
    @Column(nullable = false)
    private String userName;

    /**
     * password: String used to get access to the user
     * account.
     */
    @ApiModelProperty(value = "password", example = "12345pass")
    @Column(nullable = false)
    private String password;

    /**
     * fullName: User's full name.
     */
    @ApiModelProperty(value = "fullName", example = "Hugo Alvarez")
    @Column(nullable = false)
    private String fullName;

    /**
     * car: Car object that keeps information like
     * make, model, year, color, license plate.
     */
    @ApiModelProperty(value = "car")
    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "car_id")
    private Car car;

    /**
     * map: Map object that keeps location information
     * in the form of coordinates (latitude and longitude).
     */
    @ApiModelProperty(value = "map")
    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "map_id")
    private Maps map;

    /**
     * role: Role determines what kind of access the
     * user has at a given time. ADMIN, DRIVER, PASSENGER.
     */
    @ApiModelProperty(value = "role")
    @Enumerated(EnumType.STRING)
    private Role role;

    /**
     * User Constructor
     * @param userName Username
     * @param password Password
     * @param fullName Full name
     * @param role Role
     */
    public User(String userName, String password, String fullName, Role role)
    {
        this.userName = userName;
        this.password = password;
        this.fullName = fullName;
        this.role = role;
    }

    /**
     * Empty User constructor. Used to test setters.
     */
    public User(){ }

    /**
     * Method used to find the id of a user.
     * @return unique ID of a given user object.
     */
    public long getId() {
        return id;
    }

    /**
     * Set/change id for the given user.
     * @param userID Given user's unique id.
     */
    public void setId(long userID) {
        this.id = userID;
    }

    /**
     * Method used to find the userName of a given User object.
     * @return username for the given User object.
     */
    public String getUserName()
    {
        return userName;
    }

    /**
     * Used to change the username of an account
     * that already exists.
     * @param newUserName New username
     */
    public void setUserName(String newUserName)
    {
        this.userName = newUserName;
    }

    /**
     * Method used to find the password of a given User.
     * @return Password saved for the given user.
     */
    public String getPassword()
    {
        return password;
    }

    /**
     * Updates a password for an existing user.
     * @param newPassword New Password.
     */
    public void setPassword(String newPassword)
    {
        this.password = newPassword;
    }

    /**
     * Method used to get the full name of a given User.
     * @return Full name of a given user
     */
    public String getFullName() {
        return fullName;
    }

    /**
     * Updates the full name of a given user.
     * @param fullName Full name.
     */
    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    /**
     * Method used to return the car currently assigned to a given User.
     * @return Car owned by a given user.
     */
    public Car getCar() {
        return car;
    }

    /**
     * Update/add car to a given user.
     * @param car Car object.
     */
    public void setCar(Car car) {
        this.car = car;
    }

    /**
     * Method used to return the current role of a user.
     * @return Current role for the given user.
     */
    public Role getRole() {
        return role;
    }

    /**
     * Updating/adding role to a given user.
     * @param role Role for user.
     */
    public void setRole(Role role)
    {
        this.role = role;
    }

    /**
     * Method used to return the User's current location.
     * @return User's current location.
     */
    public Maps getMaps() {
        return map;
    }

    /**
     * Set/change user's location
     * @param maps Location coordinates.
     */
    public void setMaps(Maps maps) {
        this.map = maps;
    }

    /**
     * toString method for testing purposes.
     * @return A formatted string with user information.
     */
    @Override
    public String toString()
    {
        return "userId: " + this.id + "\nuserName: " + this.userName + "\npassword: " + this.password
                + "\nfullName: " + this.fullName + "\nrole: " + this.role + "\ncar: " + this.car + "\n";
    }


}
