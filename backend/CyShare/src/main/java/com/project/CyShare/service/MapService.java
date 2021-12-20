package com.project.CyShare.service;

import com.project.CyShare.app.Maps;
import com.project.CyShare.repository.MapsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * @author Bhuwan Joshi
 */
@Service
public class MapService
{
    /**
     * Initialize a maps repository.
     */
    @Autowired
    private MapsRepository mapsRepository;

    /**
     * Method used to find all maps saved.
     * @return List of Maps objects.
     */
    public List<Maps> findAll()
    {
        Iterable<Maps> it = mapsRepository.findAll();
        ArrayList<Maps> arrayList = new ArrayList<Maps>((Collection<? extends Maps>) it);
//        it.forEach(arrayList::add);
        return arrayList;
    }

    /**
     * Method used to find maps by its id number.
     * @param id Maps id number.
     * @return Maps object.
     */
    public Maps findById(long id)
    {
        return mapsRepository.findById(id);
    }

    /**
     * Method used to save a new Maps object into the database.
     * @param maps Maps object to save.
     */
    public void save(Maps maps)
    {
        mapsRepository.save(maps);
    }

    /**
     * Method used to save multiple maps objects into the database.
     * @param maps List of Maps objects.
     */
    public void saveAll(List<Maps> maps)
    {
        mapsRepository.saveAll(maps);
    }

    /**
     * Method used to delete maps from the database using its id number.
     * @param id Maps id number.
     */
    public void deleteById(long id)
    {
        mapsRepository.deleteById(id);
    }
}
