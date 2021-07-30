package com.foundation.mbta.data;

/**
 * Entry application. Invokes Reader class that loads data from the MBTA API.
 */

import com.foundation.mbta.data.reader.MbtaAPIReader;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@Slf4j
public class MbtaDataApplication implements CommandLineRunner {

    @Autowired
    MbtaAPIReader mr;

    @Value("${run-loader}")
    boolean runLoader;

    @Value("${base-url}")
    String baseUrl;

    public static void main(String[] args) {
        SpringApplication.run(MbtaDataApplication.class, args);
    }

    public void run(String... args) {
        mr.initialize(baseUrl);
        if(runLoader) {
            mr.loadData();
            System.exit(0);
        }
    }

}
