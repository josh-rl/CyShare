package com.project.CyShare.Tools;

import com.github.javafaker.Faker;
import com.project.CyShare.app.Car;
import com.project.CyShare.app.Role;
import com.project.CyShare.app.User;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class UserGenerator
{

    public UserGenerator() throws IOException
    {

    }

    public List<User> generate(int amount) throws IOException {

        List<User> userList = new ArrayList<>();

        for (int i = 0; i < amount; i++)
        {
            User user = new User();
            Faker faker = new Faker();
            String password = new PasswordGenerator().generate(8);
            String firstName = faker.name().firstName();
            String lastName = faker.name().lastName();

            int number = (Math.random() <= 0.5) ? 1 : 2;
            if (number == 1)
                user.setRole(Role.DRIVER);
            if (number == 2)
                user.setRole(Role.PASSENGER);

            user.setFullName(firstName + " " + lastName);
            user.setUserName(firstName.toLowerCase() + "." + lastName.toLowerCase());
            user.setPassword(password);
            userList.add(user);
        }
        return userList;
    }

    public User generateDriver()
    {
        User user = new User();
        Faker faker = new Faker();
        String password = new PasswordGenerator().generate(8);
        String firstName = faker.name().firstName();
        String lastName = faker.name().lastName();

        user.setRole(Role.DRIVER);
        user.setFullName(firstName + " " + lastName);
        user.setUserName(firstName.toLowerCase() + "." + lastName.toLowerCase());
        user.setPassword(password);

        return user;
    }

    public User generatePassenger()
    {
        User user = new User();
        Faker faker = new Faker();
        String password = new PasswordGenerator().generate(8);
        String firstName = faker.name().firstName();
        String lastName = faker.name().lastName();

        user.setRole(Role.PASSENGER);
        user.setFullName(firstName + " " + lastName);
        user.setUserName(firstName.toLowerCase() + "." + lastName.toLowerCase());
        user.setPassword(password);

        return user;
    }
}
