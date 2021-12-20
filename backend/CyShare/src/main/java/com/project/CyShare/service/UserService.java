package com.project.CyShare.service;

import com.project.CyShare.Tools.PasswordEncoder;
import com.project.CyShare.Tools.Trie;
import com.project.CyShare.app.Role;
import com.project.CyShare.app.User;
import com.project.CyShare.repository.UsersRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * @author Hugo Alvarez Valdivia
 * @author Bhuwan Joshi
 */
@Service
public class UserService {

    /**
     * Initialize a user repository
     */
    @Autowired
    private UsersRepository usersRepository;

    public static Trie trie = new Trie();
    /**
     * Method used to find a user by id number.
     * @param id User id number.
     * @return User object
     * @throws Exception Exception
     */
    public User findById(long id) throws Exception
    {
        User usr = userCopy(usersRepository.findById(id));
        PasswordEncoder pe = new PasswordEncoder();
        usr.setPassword(pe.decrypt(usr.getPassword()));
        return usr;
    }

    /**
     * Method used to find a User in the database by userName.
     * @param userName User's userName
     * @return User Object.
     * @throws Exception Exception
     */
    public User getUserByName(String userName) throws Exception {
        if(trie.getSize()==0)
        {
            List<User> n = findAll();
            for(User usr:n)
            {
                trie.insert(usr.getUserName(),usr.getId());
            }
        }
        long id = trie.search("Bhuwan");
      return  findById(trie.search(userName));

    }

    public User findByUserName(String userName)
    {
        return usersRepository.findByUserName(userName);
    }


    /**
     * Method used to find all users by a give role (i.e., all users with DRIVER role).
     * @param role Role to search.
     * @return List of User objects with that role assigned.
     */
    public List<User> findByRole(Role role)
    {
        List<User> usersByRole = new ArrayList<User>();
        Iterable<User> it = usersRepository.findAll();
        ArrayList<User> arrayList = new ArrayList<User>((Collection<? extends User>) it);

        for (User usr : arrayList)
        {
            if (usr.getRole() == role)
            {
                usersByRole.add(usr);
            }

        }
        return usersByRole;
    }

    /**
     * Method used to find all users saved into the database.
     * @return List of User objects.
     * @throws Exception Exception
     */
    public List<User> findAll() throws Exception {

        Iterable<User> it = usersRepository.findAll();
        List<User> arr = new ArrayList<User>();
        ArrayList<User> arrayList = new ArrayList<User>((Collection<? extends User>) it);
        PasswordEncoder pe = new PasswordEncoder();
        for (User usr : arrayList) {
            User user = userCopy(usr);
            user.setPassword(pe.decrypt(usr.getPassword()));
            arr.add(user);
        }
        return arr;
    }

    /**
     * Method used to save a new User into the database.
     * @param user User Object to be saved.
     * @throws Exception Exception
     */
    public void save(User user) throws Exception {

        PasswordEncoder p = new PasswordEncoder();
        User usr = userCopy(user);

        usr.setPassword(p.encrypt(user.getPassword()));
        usersRepository.save(usr);
        long id = usr.getId();
        trie.insert(usr.getUserName(),usr.getId());
    }

    /**
     * Method used to delete a User from the database by id number.
     * @param id User id number.
     */
    public void deleteById(long id) {

        usersRepository.deleteById(id);
    }

    /**
     * Method used to delete a given User object from the database.
     * @param user User object to be deleted.
     */
    public void delete(User user)
    {
        usersRepository.delete(user);
    }

    /**
     * Method used to delete multiple users from the database.
     * @param users List of User objects to be deleted.
     */
    public void deleteAll(List<User> users) {
        usersRepository.deleteAll(users);
    }

    /**
     * Method used to create a copy of a User object.
     * @param usr User to be copied.
     * @return New User object.
     */
    public User userCopy(User usr) {
        User user = new User();
        user.setId(usr.getId());
        user.setFullName(usr.getFullName());
        user.setUserName(usr.getUserName());
        user.setPassword(usr.getPassword());
        user.setRole(usr.getRole());
        user.setCar(usr.getCar());
        user.setMaps(usr.getMaps());
        return user;
    }

    public void saveAll(List<User> users) {

        usersRepository.saveAll(users);
        for(User usr : users)
        {
         trie.insert(usr.getUserName(),usr.getId());
        }
    }
}
