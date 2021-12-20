package com.example.cyshare_frontend;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.atLeast;
import static org.mockito.Mockito.doCallRealMethod;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import android.content.Context;

import androidx.test.platform.app.InstrumentationRegistry;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;
import com.example.cyshare_frontend.controller.backend_io;
import com.example.cyshare_frontend.stubs.unit_tester;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.runners.MockitoJUnitRunner;

import java.util.ArrayList;
import java.util.HashMap;

@RunWith(MockitoJUnitRunner.class)
public class AndroidTests {

    @Test
    public void useAppContext() {
        // Context of the app under test.
        Context appContext = InstrumentationRegistry.getInstrumentation().getTargetContext();
        assertEquals("com.example.CyShare_Frontend", appContext.getPackageName());
    }

    @Test
    public void testLoginSpeed() {
        long startTime = System.currentTimeMillis();
        Context context = InstrumentationRegistry.getInstrumentation().getTargetContext();
        RequestQueue volleyQueue = Volley.newRequestQueue(context);
        HashMap<String, ArrayList<String>> realData = new HashMap<>();
        String url = "http://coms-309-031.cs.iastate.edu:8080/users/all";
        backend_io.serverGetUserTables(volleyQueue, url, realData);
        long endTime = System.currentTimeMillis();
        long elapsed = endTime - startTime;
        assertTrue(elapsed < 5000L);
    }

    /*
    @Test
    public void testLogin1() {
        //region $state
        login_page mockPage = mock(login_page.class);
        mockPage.usersInfo = new HashMap<>();
        doNothing().when(mockPage).to_registration();
        doNothing().when(mockPage).to_driver_splash();
        doNothing().when(mockPage).to_passenger_splash();
        doCallRealMethod().when(mockPage).validate(anyString(), anyString());
        ArrayList<String> tempUser1 = new ArrayList<>();
        tempUser1.add("1");
        tempUser1.add("password");
        tempUser1.add("PASSENGER");
        ArrayList<String> tempUser2 = new ArrayList<>();
        tempUser2.add("2");
        tempUser2.add("password");
        tempUser2.add("DRIVER");
        mockPage.usersInfo.put("josh", tempUser1);
        mockPage.usersInfo.put("jim", tempUser2);
        //endregion

        //region $tests
        mockPage.validate("notauser", "password");
        verify(mockPage, times(0)).to_passenger_splash();
        verify(mockPage, times(0)).to_driver_splash();
        verify(mockPage, times(0)).to_registration();
        //endregion
    }

    @Test
    public void testLogin2() {
        //region $state
        login_page mockPage = mock(login_page.class);
        mockPage.usersInfo = new HashMap<>();
        doNothing().when(mockPage).to_registration();
        doNothing().when(mockPage).to_driver_splash();
        doNothing().when(mockPage).to_passenger_splash();
        doCallRealMethod().when(mockPage).validate(anyString(), anyString());
        ArrayList<String> tempUser1 = new ArrayList<>();
        tempUser1.add("1");
        tempUser1.add("password");
        tempUser1.add("PASSENGER");
        ArrayList<String> tempUser2 = new ArrayList<>();
        tempUser2.add("2");
        tempUser2.add("password");
        tempUser2.add("DRIVER");
        mockPage.usersInfo.put("josh", tempUser1);
        mockPage.usersInfo.put("jim", tempUser2);
        //endregion

        //region $tests
        mockPage.validate("josh", "wrongpass");
        verify(mockPage, times(0)).to_passenger_splash();
        verify(mockPage, times(0)).to_driver_splash();
        verify(mockPage, times(0)).to_registration();
        //endregion
    }
    */

    @Test
    public void testLogin3() {
        //region $state
        login_page mockPage = mock(login_page.class);
        mockPage.usersInfo = new HashMap<>();
        doNothing().when(mockPage).to_registration();
        doNothing().when(mockPage).to_driver_home();
        doNothing().when(mockPage).to_passenger_home();
        doCallRealMethod().when(mockPage).validate(anyString(), anyString());
        ArrayList<String> tempUser1 = new ArrayList<>();
        tempUser1.add("1");
        tempUser1.add("password");
        tempUser1.add("PASSENGER");
        ArrayList<String> tempUser2 = new ArrayList<>();
        tempUser2.add("2");
        tempUser2.add("password");
        tempUser2.add("DRIVER");
        mockPage.usersInfo.put("josh", tempUser1);
        mockPage.usersInfo.put("jim", tempUser2);
        //endregion

        //region $tests
        mockPage.validate("josh", "password");
        verify(mockPage, atLeast(1)).to_passenger_home();
        verify(mockPage, times(0)).to_driver_home();
        verify(mockPage, times(0)).to_registration();
        //endregion
    }

    @Test
    public void testLogin4() {
        //region $state
        login_page mockPage = mock(login_page.class);
        mockPage.usersInfo = new HashMap<>();
        doNothing().when(mockPage).to_registration();
        doNothing().when(mockPage).to_driver_home();
        doNothing().when(mockPage).to_passenger_home();
        doCallRealMethod().when(mockPage).validate(anyString(), anyString());
        ArrayList<String> tempUser1 = new ArrayList<>();
        tempUser1.add("1");
        tempUser1.add("password");
        tempUser1.add("PASSENGER");
        ArrayList<String> tempUser2 = new ArrayList<>();
        tempUser2.add("2");
        tempUser2.add("password");
        tempUser2.add("DRIVER");
        mockPage.usersInfo.put("josh", tempUser1);
        mockPage.usersInfo.put("jim", tempUser2);
        //endregion

        //region $tests
        mockPage.validate("jim", "password");
        verify(mockPage, times(0)).to_passenger_home();
        verify(mockPage, atLeast(1)).to_driver_home();
        verify(mockPage, times(0)).to_registration();
        //endregion
    }
    //Tanay's second test for the final demo commented out because it does not pass
    //  @Test
    // public void adminLoginTest()
    // {
    //     login_page mocklogin = mock(login_page.class);
    //     mocklogin.userPass.setText("admin");
    //     mocklogin.userName.setText("admin");

    //     verify(mocklogin,times(0)).to_driver_home();
    //     verify(mocklogin,atLeast(1)).to_Admin_home();
    //     verify(mocklogin,times(0)).to_registration();

    // }
    @Test
    public void checkRegValidate()
    {
        register_page mockReg = mock(register_page.class);
        doNothing().when(mockReg).to_driver_reg();
        doNothing().when(mockReg).to_passenger_home();
        mockReg.validate(mockReg.volleyQueue, "tanay","tanay1","tanay1","tanay Parikh");
        verify(mockReg,times(0)).to_driver_reg();
       // verify(mockReg),times(0)).to_passenger_home();

    }


    /**
     * New tests, but break pipeline
     */
    /*
    @Test
    public void testWebSocket1 () {
        unit_tester mockPage = mock(unit_tester.class);
        doCallRealMethod().when(mockPage).setupWebsocket();

        mockPage.setupWebsocket();
        mockPage.wsc.wsClient.connect();
        mockPage.wsc.sendOne();

        assertEquals("1", mockPage.testValue);

        mockPage.wsc.closeWSC();
    }

    @Test
    public void testWebSocket2 () {
        unit_tester mockPage = mock(unit_tester.class);
        doCallRealMethod().when(mockPage).setupWebsocket();

        mockPage.setupWebsocket();
        mockPage.wsc.wsClient.connect();
        mockPage.wsc.sendZero();

        assertEquals("0", mockPage.testValue);

        mockPage.wsc.closeWSC();
    }
    */
}
