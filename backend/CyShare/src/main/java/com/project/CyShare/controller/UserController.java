package com.project.CyShare.controller;

import com.project.CyShare.Tools.CarGenerator;
import com.project.CyShare.Tools.UserGenerator;
import com.project.CyShare.app.Car;
import com.project.CyShare.app.Maps;
import com.project.CyShare.app.Role;
import com.project.CyShare.app.User;
import com.project.CyShare.repository.CarsRepository;
import com.project.CyShare.repository.UsersRepository;
import com.project.CyShare.service.CarService;
import com.project.CyShare.service.MapService;
import com.project.CyShare.service.UserService;
import io.swagger.annotations.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * This controller is used to perform actions regarding users (add, delete, find, assign car, etc.)
 * @author Hugo Alvarez Valdivia
 * @author Bhuwan Joshi
 */

@Api(tags = "UserController", value = "UserController", description = "This controller to perform different actions related to users" +
        " (i.e., add or delete a user, assign a car to a user, etc).")
@RestController
public class UserController {

    /**
     * Initialize a user service.
     */
    @Autowired
    UserService userService;

    /**
     * Initialize a car service.
     */
    @Autowired
    CarService carService;

    /**
     * Initialize a map service.
     */
    @Autowired
    MapService mapService;



    /**
     * Mapping used to return a list of all users saved in the database.
     * @return List of all users.
     */
    @ApiOperation(value = "Get list of users in the database.", response = Iterable.class)
    @ApiImplicitParam("No input parameters, just call using path users/all")
    @ApiResponses(value =
            {
                    @ApiResponse(code = 200, message = "Success!!"),
                    @ApiResponse(code = 401, message = "Not authorized!"),
                    @ApiResponse(code = 403, message = "Forbidden!"),
                    @ApiResponse(code = 404, message = "Oops... Not found")
            })
    @GetMapping("users/all")
    public List<User> GetAllUsers() throws Exception {
        return userService.findAll();
    }

    /**
     * This post mapping is used to add a new user via path
     * variables. Only here for demonstration purposes. For this
     * project we'll be using JSON body anyways.
     * @param n Username
     * @param p Password
     * @return Success message
     */
    @ApiOperation(value = "Add a new user to database passing arguments in the path.",
            response = String.class)
    @ApiImplicitParams(value = {
                    @ApiImplicitParam(name = "n", value = "userName"),
                    @ApiImplicitParam(name = "p", value = "password"),
                    @ApiImplicitParam(name = "r", value = "role") })
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Successfully added a user"),
            @ApiResponse(code = 401, message = "Not authorized!"),
            @ApiResponse(code = 403, message = "Forbidden!"),
            @ApiResponse(code = 404, message = "Oops... not found")
    })
    @PostMapping("users/add/{n}/{p}/{r}")
    String AddUserByPath(@PathVariable String n, @PathVariable String p, @PathVariable Role r) throws Exception {
        User newUser = new User();
        newUser.setUserName(n);
        newUser.setPassword(p);
        newUser.setRole(r);
        userService.save(newUser);
        return "User added successfully.";
    }

    /**
     * Post mapping that will utilize a JSON body request to add a
     * new user into the database.
     * @param newUser User objects to be added to database.
     * @return Success/failure message.
     */
    @ApiOperation(value = "Add a new user using JSON.")
    @ApiResponses(value =
            {
                    @ApiResponse(code = 200, message = "User added successfully"),
                    @ApiResponse(code = 201, message = "Created successfully!"),
                    @ApiResponse(code = 401, message = "Not authorized!"),
                    @ApiResponse(code = 403, message = "Forbidden"),
                    @ApiResponse(code = 404, message = "Oops... not found")
            })
    @ApiParam(name = "newUser", value = "User object parsed from JSON string.")
    @PostMapping("users/add")
    public String AddUser(@RequestBody User newUser) throws Exception {
        if (newUser == null) {
            return "Error: User = null";
        }
        userService.save(newUser);
        return "User(s) added successfully";
    }

    /**
     * Delete mapping to remove a user from the database.
     * @param user User to be deleted.
     * @return Success/failure message.
     */
    @ApiOperation(value = "Remove user from the existing database.")
    @ApiResponses(value =
            {
                    @ApiResponse(code = 200, message = "User deleted successfully"),
                    @ApiResponse(code = 401, message = "Not authorized!"),
                    @ApiResponse(code = 403, message = "Forbidden!"),
                    @ApiResponse(code = 404, message = "Oops... not found")
            })
    @DeleteMapping("users/remove")
    public String RemoveUser(@RequestBody User user) {
        if (user == null) {
            return "Error: users = null";
        }
        userService.delete(user);
        return "Users deleted successfully";
    }

    /**
     * This mapping is used to find all users that have a certain role.
     * For example, can be used to return all current DRIVERS or PASSENGERS.
     * @param role Role to search
     * @return List of all users that have that role assigned.
     */
    @ApiOperation(value = "Get a list of all users associated with a given role.")
    @ApiResponses(value =
            {
                    @ApiResponse(code = 401, message = "Not authorized!"),
                    @ApiResponse(code = 403, message = "Forbidden!"),
                    @ApiResponse(code = 404, message = "Oops... not found")
            })
    @GetMapping("users/role/{role}")
    public List<User> GetUsersByRole(@PathVariable Role role) {
        try {
            return userService.findByRole(role);
        } catch (Exception e) {
            return null;
        }
    }

    /**
     * This method changes the current role for the given user.
     * @param userName UserName for user to make changes.
     * @param role   New role for said user.
     * @return Success/failure message.
     */
    @ApiOperation(value = "Update the role linked to a user (i.e., update from driver to passenger or vice versa).")
    @ApiImplicitParams(value =
            {
                    @ApiImplicitParam(name = "userId", value = "id number for user to make changes.",
                            paramType = "long"),
                    @ApiImplicitParam(name = "role", value = "Role to be assigned (ADMIN, DRIVER," +
                            " PASSENGER).", paramType = "Role", dataTypeClass = Role.class)
            })
    @ApiResponses(value =
            {
                    @ApiResponse(code = 401, message = "Not authorized!"),
                    @ApiResponse(code = 403, message = "Forbidden!"),
                    @ApiResponse(code = 404, message = "Oops... not found")
            })
    @PutMapping("users/{userName}/{role}")
    public String updateRole(@PathVariable String userName, @PathVariable String role) throws Exception {

        if (role == null)
            return "Error: role = null";

        User temp = userService.findByUserName(userName);
        if (temp == null)
            return "Error: user not found.";

        switch (role)
        {
            case "ADMIN":
                temp.setRole(Role.ADMIN);
                userService.save(temp);
                break;
            case "PASSENGER":
                temp.setRole(Role.PASSENGER);
                userService.save(temp);
                break;
            case "DRIVER":
                temp.setRole(Role.DRIVER);
                userService.save(temp);
                break;
        }
        return "Role updated successfully";
    }

    /**
     * This controller is used to keep the current location of a user
     * updated in the database.
     * @param userId id number of the user to be updated.
     * @param mapId id number of the location to be assigned.
     * @throws Exception Exception
     */
    @ApiOperation(value = "Update the current location of a given user.")
    @ApiImplicitParams(value =
            {
                    @ApiImplicitParam(name = "userId", value = "id number of the user to be updated.", paramType = "long"),
                    @ApiImplicitParam(name = "mapId", value = "id of the location to be assigned.", paramType = "long")
            })
    @ApiResponses(value =
            {
                    @ApiResponse(code = 401, message = "Not authorized!!"),
                    @ApiResponse(code = 403, message = "Forbidden!"),
                    @ApiResponse(code = 404, message = "Oops... not found")
            })
    @PutMapping("/users/{userId}/Maps/{mapId}")
    public void updateLocation(@PathVariable long userId, @PathVariable long mapId) throws Exception {
        User user = userService.findById(userId);
        Maps maps = mapService.findById(mapId);

        user.setMaps(maps);
        userService.save(user);
    }

    /**
     * This controller returns a user object just by inputting the userName
     * @param userName userName of the user to be found.
     * @return User object
     * @throws Exception Exception
     */
    @ApiOperation(value = "Return a given user by inputting the userName")
    @ApiImplicitParam(name = "userName", value = "userName of the user to be found", paramType = "string")
    @ApiResponses(value =
            {
                    @ApiResponse(code = 401, message = "Not authorized!!"),
                    @ApiResponse(code = 403, message = "Forbidden!"),
                    @ApiResponse(code = 404, message = "Oops... not found")
            })
    @GetMapping("/users/userName/{userName}")
    public User getUser(@PathVariable String userName) throws Exception {
        return userService.getUserByName(userName);
    }


    /**
     * This method assigns an already existing car to a user using path variables
     * @param carId  Car to assign.
     * @param userId User for the car.
     * @return Success/failure message.
     */
    @ApiOperation("Assign a car object to a user object")
    @ApiImplicitParams(value =
            {
                    @ApiImplicitParam(name = "userId", value = "id number of the user that will get the car.",
                            paramType = "long"),
                    @ApiImplicitParam(name = "carId", value = "id number of the car to be assigned.",
                            paramType = "long")
            })
    @ApiResponses(value =
            {
                    @ApiResponse(code = 401, message = "Not authorized!!"),
                    @ApiResponse(code = 403, message = "Forbidden!"),
                    @ApiResponse(code = 404, message = "Oops... not found")
            })
    @PutMapping("/users/{userId}/cars/{carId}")
    public String assignCarPath(@PathVariable long userId, @PathVariable long carId) throws Exception {
        User newUser = userService.findById(userId);
        Car newCar = carService.findById(carId);
        if (newUser == null || newCar == null) {
            return "Error: User or Car do not exist.";
        }
        newUser.setCar(newCar);
        userService.save(newUser);
        return "Car assigned Successfully";
    }

    /**
     * This is an alternative to the assign car by path controller. It takes
     * the userName instead of the id number.
     * @param car Car entity to assign.
     * @param userName userName of user that will get the car assigned.
     * @return Success/failure message.
     * @throws Exception Exception
     */
    @ApiOperation(value = "Alt Car assignment: Assign a car to a user by inputting the userName")
    @ApiImplicitParams(value =
            {
                    @ApiImplicitParam(name = "car", value = "Car object to assign.", paramType = "Car"),
                    @ApiImplicitParam(name = "userName", value = "userName of user that will get the" +
                            " car assigned.", paramType = "string")
            })
    @ApiResponses(value =
            {
                    @ApiResponse(code = 401, message = "Not authorized!!"),
                    @ApiResponse(code = 403, message = "Forbidden!"),
                    @ApiResponse(code = 404, message = "Oops... not found")
            })
    @PutMapping(path = "/users/addcar/{userName}")
    public String assignCar(@RequestBody Car car,@PathVariable String userName) throws Exception {
        User usr = userService.getUserByName(userName);
        if (usr == null || car == null) {
            return "Error: User or Car do not exist.";
        }
        usr.setCar(car);
        userService.save(usr);
        return"Car assigned Successfully";
    }

    /**
     * This controller is used to add randomly generated Users
     * to the databases. Created to test performance of all look-up-type
     * User services (i.e., findAll, findById, findByUserName, etc.)
     * @param amount Amount of users to be generated.
     * @return Success/failure message.
     * @throws IOException Exception
     */
    @PostMapping("users/dev/add/{amount}")
    public String addFakeUsers(@PathVariable int amount) throws IOException {
        if (amount <= 0)
            return "No user(s) generated.";

        UserGenerator userGenerator = new UserGenerator();
        userService.saveAll(userGenerator.generate(amount));
        return "User(s) generated successfully.";
    }

    @PostMapping("users/cars/dev/add/{amount}")
    public String addFakeUsersWithCars(@PathVariable int amount) throws IOException
    {
        if (amount <= 0)
            return "No user(s) generated.";

        UserGenerator userGenerator = new UserGenerator();
        CarGenerator carGenerator = new CarGenerator();
        List<User> userList = new ArrayList<>();

        for (int i = 0; i < amount; i++)
        {
            User user = userGenerator.generate(1).get(0);
            Car car = carGenerator.generate(1).get(0);
            user.setCar(car);
            userList.add(user);
        }

        userService.saveAll(userList);
        return "User(s) generated successfully";

    }
}
