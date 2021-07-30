package com.foundation.mbta.data.dao;

import com.foundation.mbta.data.MbtaDataApplication;
import com.foundation.mbta.data.model.Facility;
import com.foundation.mbta.data.reader.MbtaAPIReader;
import com.foundation.mbta.data.repo.FacilityRepository;
import com.foundation.mbta.data.repo.StopRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ContextConfiguration;

import java.util.List;
import java.util.stream.Collectors;

import static org.junit.Assert.*;


@DataJpaTest
@ContextConfiguration(classes={MbtaDataApplication.class, MbtaAPIReader.class})
class FacilityRepositoryTest {

    @Autowired
    private FacilityRepository facilityRepo;

    @Autowired
    private StopRepository stopRepo;

    @Test
    public void whenAddEscalator_thenFound(){

        List<Facility> begin = facilityRepo.findAll();
        assertEquals(0, begin.size());

        Facility escalator = new Facility("f1", "s1", "ESCALATOR");

        facilityRepo.saveAndFlush(escalator);
        List<Facility> after = facilityRepo.findAll();
        assertEquals( 1, after.size());
        Facility f1 = after.get(0);
        assertNotNull(f1);
        assertEquals("ESCALATOR", f1.getType());
    }

    @Test
    public void whenDeleteFacility_thenNoneFound() {
        Facility vending = new Facility("f2", "s1", "VENDING_MACHINE");

        facilityRepo.saveAndFlush(vending);
        List<Facility> after = facilityRepo.findAll();
        assertEquals( 1, after.size());


        facilityRepo.deleteById("f2");
        List<Facility> begin = facilityRepo.findAll();
        assertEquals(0, begin.size());
    }

    @Test
    public void whenAddList_thenSuccess() {
        Facility v1 = new Facility("f3", "s3", "VENDING_MACHINE");
        Facility e1 = new Facility("f4", "s0", "ESCALATOR");
        Facility v2 = new Facility("f5", "s2", "VENDING_MACHINE");
        Facility v3 = new Facility("f6", "s1", "VENDING_MACHINE");

        List facilities = List.of(v1, e1, v2, v3);
        facilityRepo.saveAllAndFlush(facilities);

        List<Facility> retList = facilityRepo.findAll();
        assertEquals(4, retList.size());

        List<String> containsId = retList.stream().map(x->x.getId())
                .filter(y->y.equals("f3")).collect(Collectors.toList());

        assertEquals(containsId.size(),1);

    }

    @Test
    public void whenFindById_thenSuccess() {
        Facility v1 = new Facility("id21", "s1", "ESCALATOR");
        facilityRepo.saveAndFlush(v1);

        try {
            Facility v2 = facilityRepo.findById("id21").get();
            assertTrue(Boolean.TRUE);
        } catch(Exception e) {
            fail(e.getMessage());
        }

        try {
            Facility v3 = facilityRepo.findById("id99").get();
        } catch(Exception e) {
            assertTrue(Boolean.TRUE);
        }
    }


}