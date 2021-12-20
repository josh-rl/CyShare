package com.project.CyShare;

import com.project.CyShare.app.Maps;
import com.project.CyShare.controller.MapController;
import com.project.CyShare.service.MapService;
import com.project.CyShare.service.UserService;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.MockitoJUnitRunner;
import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class MapControllerTests
{
    @InjectMocks
    MapController mapController = new MapController();

    @Mock
    MapService mapService;

    @Mock
    UserService userService;

    @Before
    public void init()
    {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void createMapTest() throws Exception
    {
        Maps map = new Maps(11111.11, 22222.22);
        map.setId(1);
        assertEquals("Coordinates successfully added.", mapController.createMap(map));
        map = null;
        assertEquals("Error, coordinates could not be added (maps == null)", mapController.createMap(map));
    }

    @Test
    public void updateMapsTest() throws Exception
    {
        Maps map = new Maps(11111.11, 22222.22);
        map.setId(1);
        System.out.println("Before:" + map);
        when(mapService.findById(map.getId())).thenReturn(map);
        assertEquals("Successfully updated maps.", mapController.updateMaps(map.getId(),new Maps(3333.33, 4444.44)));
        System.out.println("After:" + map);
    }

    @Test
    public void deleteMapTest() throws Exception
    {
        Maps map = new Maps(11111.11, 22222.22);
        map.setId(1);
        when(mapService.findById(map.getId())).thenReturn(map);
        assertEquals("Maps deleted successfully", mapController.deleteMap(map.getId()));
    }
}
