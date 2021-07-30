package com.foundation.mbta.data.reader;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.foundation.mbta.data.repo.FacilityRepository;
import com.foundation.mbta.data.repo.LineRepository;
import com.foundation.mbta.data.repo.StopRepository;
import com.foundation.mbta.data.repo.RouteRepository;
import com.foundation.mbta.data.model.Facility;
import com.foundation.mbta.data.model.Line;
import com.foundation.mbta.data.model.Route;
import com.foundation.mbta.data.model.Stop;
import com.foundation.mbta.data.response.MbtaArrayResponse;
import com.foundation.mbta.data.response.MbtaResponseData;
import com.google.gson.Gson;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClient.ResponseSpec;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Reader class. Uses MBTA API endpoints to fetch Route, Line, Stop, and Facility information.
 */
@Component
@Slf4j
public class MbtaAPIReader {

    WebClient webClient;

    @Autowired
    LineRepository lineRepository;

    @Autowired
    RouteRepository routeRepository;

    @Autowired
    StopRepository stopRepository;

    @Autowired
    FacilityRepository facilityRepository;

    @Value("${api-key}")
    String apiKey;

    String baseUrl;
    ObjectMapper mapper;

    /**
     * Initializes reader class.
     * - Creates a webClient for reading from endpoints
     * - Creates a Jackson object mapper for converting between JSON and objects
     *
     * @param baseUrl The baseUrl for querying
     */
    public void initialize(String baseUrl) {
        // Client that reads data from MBTA API
        webClient = WebClient.builder().baseUrl(baseUrl)
                .defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .build();
        mapper = new ObjectMapper();
        mapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);

    }

    /**
     * Loads all data
     */
    public void loadData() {
        this.loadLines();
        this.loadRoutes();
        this.loadStops();
        this.loadFacilities();

        log.info("Finished loading MBTA data, and persisting to database");
    }

    /**
     * The webclient invokes endpoints to fetch Lines information
     *
     * @return ResponseSpec the response object
     */
    public ResponseSpec getLinesData() {
        ResponseSpec responseSpec = this.webClient.get()
                .uri(uriBuilder -> uriBuilder
                        .path("/lines/")
                        .queryParam("page[offset]", "0")
                        .queryParam("page[limit]", "300")
                        .queryParam("fields[line]", "id", "short_name", "long_name")
                        .queryParam("include", "routes")
                        .queryParam("api_key", apiKey)
                        .build()).retrieve();
        return responseSpec;
    }

    /**
     * Fetches Line data and persists to db.
     */
    public void loadLines() {
        List<Line> lines = new ArrayList<>();
        // TODO: Response to object mapping mechanism could be improved.
        ResponseSpec responseSpec = getLinesData();
        String response = responseSpec.bodyToMono(String.class).block();
        log.info("Got Line information from MBTA API");

        Gson gson = new Gson();
        MbtaArrayResponse mbtaArrayResponse = gson.fromJson(response, MbtaArrayResponse.class);

        Line line = null;

        for (MbtaResponseData data : mbtaArrayResponse.getData()) {
            Map attributes = data.getAttributes();
            line = mapper.convertValue(attributes, Line.class);
            line.setId(data.getId());
            lines.add(line);
        }
        lineRepository.saveAllAndFlush(lines);
        log.info("Persisted {} items of Line data", lines.size());
    }

    /**
     * The webclient invokes endpoints to fetch Routes information
     *
     * @return ResponseSpec the response object
     */
    public ResponseSpec getRoutesData() {

        ResponseSpec responseSpec = this.webClient.get()
                .uri(uriBuilder -> uriBuilder
                        .path("/routes/")
                        .queryParam("page[offset]", "0")
                        .queryParam("page[limit]", "200")
                        .queryParam("fields[route]", "short_name,long_name")
                        .queryParam("filter[type]", "0,1")
                        .queryParam("api_key", apiKey)
                        .build()).retrieve();
        return responseSpec;
    }

    /**
     * Fetches MBTA subway data and persists to db.
     */
    public void loadRoutes() {

        List<Route> routes = new ArrayList<>();

        ResponseSpec responseSpec = getRoutesData();

        String response = responseSpec.bodyToMono(String.class).block();
        log.info("Got Route information from MBTA API");

        Gson gson = new Gson();
        MbtaArrayResponse mbtaArrayResponse = gson.fromJson(response, MbtaArrayResponse.class);

        Route route = null;

        for (MbtaResponseData data : mbtaArrayResponse.getData()) {
            Map attributes = data.getAttributes();
            route = mapper.convertValue(attributes, Route.class);
            route.setId(data.getId());
            routes.add(route);
        }
        routeRepository.saveAllAndFlush(routes);
        log.info("Persisted {} items of Route data", routes.size());
    }

    /**
     * Fetches Facility data and persists to db.
     */
    public void loadFacilities() {
        getFacilityInfo("FARE_VENDING_MACHINE");
        getFacilityInfo("ESCALATOR");
    }

    /**
     * The webclient invokes endpoints to fetch Facility information
     *
     * @return ResponseSpec the response object
     */
    public ResponseSpec getFacilityData(String type) {
        ResponseSpec responseSpec = this.webClient.get()
                .uri(uriBuilder -> uriBuilder
                        .path("/facilities/")
                        .queryParam("page[offset]", "0")
                        .queryParam("page[limit]", "800")
                        .queryParam("include", "stop")
                        .queryParam("filter[type]", type)
                        .queryParam("fields[facility]", "short_name")
                        .queryParam("api_key", apiKey)
                        .build()).retrieve();
        return responseSpec;
    }

    public void getFacilityInfo(String type) {
        List<Facility> facilities = new ArrayList<>();

        ResponseSpec responseSpec = getFacilityData(type);

        String response = responseSpec.bodyToMono(String.class).block();
        log.info("Got {} information from MBTA API", type);

        Gson gson = new Gson();
        MbtaArrayResponse mbtaArrayResponse = gson.fromJson(response, MbtaArrayResponse.class);

        List<Stop> stops = stopRepository.findAll();
        List<String> stopIds = stops.stream().map(k -> k.getId()).collect(Collectors.toList());

        for (MbtaResponseData data : mbtaArrayResponse.getData()) {
            Facility facility = createFacility(data, type);
            if (facility != null && stopIds.contains(facility.getStop_id())) {
                facilities.add(facility);
            }
        }
        facilityRepository.saveAllAndFlush(facilities);
        log.info("Persisted {} items of {} data", facilities.size(), type);
    }

    /**
     * Helper method to create Facility object from response data
     *
     * @param data The Response data.
     * @param type Th Facility type
     * @return The Facility object.
     */
    private Facility createFacility(MbtaResponseData data, String type) {
        Map relationships = data.getRelationships();

        Facility facility = new Facility();
        facility.setId(data.getId());
        facility.setType(type);
        Map stop = (Map) relationships.get("stop");
        Map stopData = (Map) stop.get("data");
        if (stopData != null) {
            String stopId = (String) stopData.get("id");
            facility.setStop_id(stopId);
        } else {
            return null;
        }
        return facility;
    }

    /**
     * The webclient invokes endpoints to fetch Stops information
     *
     * @return ResponseSpec the response object
     */
    public ResponseSpec getStopsData(String routeId) {

        ResponseSpec responseSpec = this.webClient.get()
                .uri(uriBuilder -> uriBuilder
                        .path("/stops/")
                        .queryParam("page[offset]", "0")
                        .queryParam("page[limit]", "100")
                        .queryParam("fields[stop]", "description,name,location_type,municipality")
                        .queryParam("include", "parent_station,route")
                        .queryParam("filter[route]", routeId)
                        .queryParam("filter[location_type]", "1")
                        .queryParam("api_key", apiKey)
                        .build()).retrieve();
        return responseSpec;
    }

    /**
     * Fetches Line data and persists to db.
     */
    public void loadStops() {

        List<Route> routes = routeRepository.findAll();
        List<Stop> stops = new ArrayList<>();

        // Toss up on making 8 calls, or one call with a lot of data.
        for (Route route : routes) {
            String routeId = route.getId();

            ResponseSpec responseSpec = getStopsData(routeId);

            String response = responseSpec.bodyToMono(String.class).block();
            log.info("Got Stop information for route {} from MBTA API", routeId);

            Gson gson = new Gson();
            MbtaArrayResponse mbtaArrayResponse = gson.fromJson(response, MbtaArrayResponse.class);

            // TODO candidate for Mapstruct
            for (MbtaResponseData data : mbtaArrayResponse.getData()) {
                Stop stop = createStop(data, routeId);
                stops.add(stop);
            }
        }
        stopRepository.saveAllAndFlush(stops);
        log.info("Persisted {} items of Stop data", stops.size());
    }

    /**
     * Helper method to create Stop entity from response data
     *
     * @param data    The response data
     * @param routeId The route id
     * @return The Stop object
     */
    private Stop createStop(MbtaResponseData data, String routeId) {
        Stop stop = new Stop();
        Map attributes = data.getAttributes();
        stop.setId(data.getId());
        stop.setName((String) attributes.get("name"));
        stop.setMunicipality((String) attributes.get("municipality"));
        stop.setRoute_id(routeId);
        return stop;
    }

    public void setBaseUrl(String baseUrl) {
        this.baseUrl = baseUrl;
    }
}
