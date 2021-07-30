package com.foundation.mbta.service.controllers;

import com.foundation.mbta.service.repo.RouteRepository;
import com.foundation.mbta.service.repo.StopRepository;
import com.foundation.mbta.service.model.Route;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;


@RestController
@RequestMapping("/mbta")
public class MbtaController {

    @Autowired
    RouteRepository routeRepository;

    @Autowired
    StopRepository stopRepository;

    /**
     * Gets the list of all subway routes.
     *
     * @return List of subway route long names.
     */
    @GetMapping("/routes")
    @ResponseBody
    public List<String> getRoutes() {
        List<Route> routes = routeRepository.findAll();
        // Extract long name
        List<String> longNames = routes.stream().map(x->x.getLong_name()).collect(Collectors.toList());
        return longNames;
    }

    /**
     * Gets the route(s) with the maximum number of stops.
     *
     * @return List of route info
     */
    @GetMapping("/mostStops")
    @ResponseBody
    public List<Object> mostStops() {
        List<Object> info = routeRepository.findRouteWithMaxStops();
        return info;
    }

    /**
     * Gets the list of stops that have 2 more routes on it.
     *
     * @return List of Stop info
     */
    @GetMapping("/stop2OrMoreRoutes")
    @ResponseBody
    public List<Object> stop2OrMoreRoutes() {
        List<Object> info = stopRepository.stop2OrMoreRoutes();
        return info;
    }

    /**
     * Gets the stop(s) with the maximum number of escalators
     *
     * @return List of stop info
     */
    @GetMapping("/mostEscalators")
    @ResponseBody
    public List<Object> mostEscalators() {
        List<Object> info = stopRepository.mostEscalators();
        return info;
    }

    /**
     * Identifies the number of subway stops in a city.
     *
     * @param name Name of city, defaults to 'Somerville'.
     * @return String information for city stops
     */
    @GetMapping("/cityNumStops")
    @ResponseBody
    public String cityNumStops(@RequestParam(defaultValue = "Somerville") String name) {
        List<Object> info = stopRepository.cityStops(name);
        String ret = null;

        if(info != null) {
            Object cityInfo = (Object) info.get(0);
            ret = String.format("City '%s' has %s stops", name, String.valueOf(cityInfo));
        }
        return ret;
    }

    /**
     * Gets the route(s) with the fewest number of stops
     *
     * @return List of route and stops info
     */
    @GetMapping("/minStops")
    @ResponseBody
    public List<Object> minStops() {
        List<Object> info = routeRepository.findRouteWithMinStops();
        return info;
    }

    // TODO
    @GetMapping("/vendingMachinesStops")
    @ResponseBody
    public String vendingMachinesAtStops() {
        return "TO BE IMPLEMENTED";
    }
}
