package com.project.CyShare.controller;

import com.project.CyShare.app.Car;
import com.project.CyShare.app.Maps;
import com.project.CyShare.repository.CarsRepository;
import com.project.CyShare.repository.MapsRepository;
import com.project.CyShare.service.MapService;
import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Controller made to perform different tasks regarding user location.
 * @author Bhuwan Joshi
 */

@Api(tags = "MapController", value = "MapController", description = "This controller was made to perform certain" +
        " actions with User Location (i.e., add, update, or delete user's current location).")
@RestController
public class MapController {

    /**
     * Initialize a mapService
     */
    @Autowired
    MapService mapService;

    /**
     * This mapping returns all currently saved locations.
     * @return List with all locations saved.
     */
    @GetMapping(path = "/Maps")
    List<Maps> getAllMaps() {
        return mapService.findAll();
    }

    /**
     * This mapping returns a location by its id number.
     * @param id id number of the location to find.
     * @return Map object.
     */
    @GetMapping(path = "/Maps/{id}")
    Maps getMapsId(int id) {
        return mapService.findById(id);
    }

    /**
     * This mapping adds a new location to the database.
     * @param maps Map object.
     * @return Success/failure message.
     */
    @PostMapping(path = "/Maps")
    public String createMap(@RequestBody Maps maps) {
        if (maps == null) {
            return "Error, coordinates could not be added (maps == null)";
        }
        mapService.save(maps);
        return "Coordinates successfully added.";
    }

    /**
     * This mapping updates the current location of a user.
     * @param id Map id number.
     * @param mapToUpdate New Map object (coordinates).
     * @return Success/failure message.
     */
    @PutMapping(path = "/Maps/{id}")
    public String updateMaps(@PathVariable long id, @RequestBody Maps mapToUpdate) {
        Maps newMaps = mapService.findById(id);
        if (newMaps == null) {
            return "Error, coordinates not found.";
        }
        newMaps.setLatitude(mapToUpdate.getLatitude());
        newMaps.setLongitude(mapToUpdate.getLongitude());
        mapService.save(newMaps);
        return "Successfully updated maps.";
    }

    /**
     * This mapping deletes a location from the database.
     * @param id map id number.
     * @return Success/failure message.
     */
    @DeleteMapping(path = "/Maps/{id}")
    public String deleteMap(@PathVariable long id) {
        Maps map = mapService.findById(id);
        if (map == null) {
            return "Error, coordinates not found.";
        }
        mapService.deleteById(id);
        return "Maps deleted successfully";
    }
}