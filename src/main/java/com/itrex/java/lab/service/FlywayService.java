package com.itrex.java.lab.service;

import static com.itrex.java.lab.properties.Properties.H2_PASSWORD;
import static com.itrex.java.lab.properties.Properties.H2_URL;
import static com.itrex.java.lab.properties.Properties.H2_USER;
import static com.itrex.java.lab.properties.Properties.MIGRATIONS_LOCATION;

import org.flywaydb.core.Flyway;

public class FlywayService {

    private Flyway flyway;

    public FlywayService() {
        inti();
    }

    public void migrate() {
        flyway.migrate();
    }

    public void clean() {
        flyway.clean();
    }

    private void inti() {
        flyway = Flyway.configure()
          .dataSource(H2_URL, H2_USER, H2_PASSWORD)
          .locations(MIGRATIONS_LOCATION)
          .load();
    }

}
