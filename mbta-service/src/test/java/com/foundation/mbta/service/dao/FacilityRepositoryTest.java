package com.foundation.mbta.service.dao;

import com.foundation.mbta.service.model.Facility;
import com.foundation.mbta.service.repo.FacilityRepository;
import com.foundation.mbta.service.repo.StopRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import java.util.List;
import static org.junit.Assert.assertEquals;


@DataJpaTest
class FacilityRepositoryTest {

    @Autowired
    StopRepository stopRepo;

    @Autowired
    FacilityRepository facilityRepo;

    @Test
    void whenGetMostEscalators_thenExpectS1() {

        Facility f1 = new Facility("f1","s1","ESCALATOR");
        facilityRepo.saveAndFlush(f1);

        List<Facility> facilities = facilityRepo.findAll();
        assertEquals(1, facilities.size());

        Facility fac1 = facilities.get(0);
        assertEquals("ESCALATOR", fac1.getType());

        List<Object> s  = stopRepo.mostEscalators();
        Object[] obj = (Object[]) s.get(0);
        assertEquals("s1",obj[0]);
    }

}