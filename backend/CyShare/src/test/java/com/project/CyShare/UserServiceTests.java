package com.project.CyShare;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.*;

import java.util.ArrayList;
import java.util.List;

import com.project.CyShare.Tools.PasswordEncoder;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.project.CyShare.app.Role;
import com.project.CyShare.app.User;
import com.project.CyShare.repository.UsersRepository;
import com.project.CyShare.service.UserService;
import org.mockito.junit.MockitoJUnitRunner;

/**
 * @author Hugo Alvarez Valdivia
 */
@RunWith(MockitoJUnitRunner.class)
public class UserServiceTests {

    @InjectMocks
    UserService usrService;

    @Mock
    UsersRepository repo;

    @Before
    public void init() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void getUserByIdTest() throws Exception {
        when(repo.findById(1)).thenReturn(new User("hugoalval", "4SWQP2oQq3k=", "Hugo A", Role.ADMIN));
        User usr = usrService.findById(1);
        assertEquals("hugoalval", usr.getUserName());
        assertEquals("1234", usr.getPassword());
        assertEquals("Hugo A", usr.getFullName());
        assertEquals(Role.ADMIN, usr.getRole());
    }

    @Test
    public void getAllUserTest() throws Exception {
        List<User> list = new ArrayList<User>();
        User usrOne = new User("bhuwan1", "4SWQP2oQq3k=", "Bhuwan J", Role.ADMIN);
        User usrTwo = new User("josh1", "4SWQP2oQq3k=", "Josh L", Role.DRIVER);
        User usrThree = new User("tanay1", "4SWQP2oQq3k=", "Tanay P", Role.PASSENGER);

        list.add(usrOne);
        list.add(usrTwo);
        list.add(usrThree);

        when(repo.findAll()).thenReturn(list);

        List<User> usrList = usrService.findAll();

        assertEquals(3, usrList.size());
        verify(repo, times(1)).findAll();

    }

    @Test
    public void getUsersByRoleTest() throws Exception {
        ArrayList<User> arr = new ArrayList<User>();
        User usrOne = new User("bhuwan1", "4SWQP2oQq3k=", "Bhuwan J", Role.ADMIN);
        User usrTwo = new User("josh1", "4SWQP2oQq3k=", "Josh L", Role.DRIVER);
        User usrThree = new User("tanay1", "4SWQP2oQq3k=", "Tanay P", Role.PASSENGER);
        User usrFour = new User("hugo1", "4SWQP2oQq3k=", "Hugo A", Role.ADMIN);

        arr.add(usrOne);
        arr.add(usrTwo);
        arr.add(usrThree);
        arr.add(usrFour);

        //usrService.saveAll(arr);


        when(usrService.findByRole(Role.ADMIN)).thenReturn(arr);

        List<User> list = usrService.findByRole(Role.ADMIN);
        assertEquals(2, list.size());
        assertEquals("bhuwan1", list.get(0).getUserName());
        assertEquals("hugo1", list.get(1).getUserName());
    }

    @Test
    public void CheckHashing() throws Exception {
        String hashed = "4SWQP2oQq3k=";
        String Unhashed = "1234";

        PasswordEncoder pr = new PasswordEncoder();


        assertEquals(hashed, pr.encrypt(Unhashed));
        assertEquals(Unhashed, pr.decrypt(hashed));
    }


    @Test
    public void TestDeletebyid() throws Exception {

        User usrOne = new User("bhuwan1", "4SWQP2oQq3k=", "Bhuwan J", Role.ADMIN);
        usrOne.setId(1);

        usrService.deleteById(1);
        verify(repo, times(1)).deleteById(1);


    }

    @Test
    public void DeleteAllTest() {
        ArrayList<User> arr = new ArrayList<User>();
        User usrOne = new User("bhuwan1", "4SWQP2oQq3k=", "Bhuwan J", Role.ADMIN);
        usrOne.setId(1);
        User usrTwo = new User("josh1", "4SWQP2oQq3k=", "Josh L", Role.DRIVER);
        User usrThree = new User("tanay1", "4SWQP2oQq3k=", "Tanay P", Role.PASSENGER);
        User usrFour = new User("hugo1", "4SWQP2oQq3k=", "Hugo A", Role.ADMIN);

        arr.add(usrOne);
        arr.add(usrTwo);
        arr.add(usrThree);
        arr.add(usrFour);

        usrService.deleteAll(arr);

        verify(repo, times(1)).deleteAll(arr);


    }
    @Test
    public void getUserbyTrie() throws Exception {
        User usrOne = new User("bhuwan1", "4SWQP2oQq3k=", "Bhuwan J", Role.ADMIN);
        ArrayList<User> arr = new ArrayList<User>();
        usrOne.setId(1);
        arr.add(usrOne);
        usrService.trie.insert(usrOne.getUserName(),usrOne.getId());
        when(repo.findById(1)).thenReturn(usrOne);
        when(usrService.findById(1)).thenReturn(usrOne);
        User usr = usrService.getUserByName("bhuwan1");
        assertEquals("bhuwan1", usr.getUserName());
        assertEquals(Role.ADMIN, usr.getRole());




    }
}
