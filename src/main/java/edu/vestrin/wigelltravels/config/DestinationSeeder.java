package edu.vestrin.wigelltravels.config;

import edu.vestrin.wigelltravels.entity.Destination;
import edu.vestrin.wigelltravels.repository.DestinationRepository;
import edu.vestrin.wigelltravels.service.DestinationService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Component
@Order(2)
public class DestinationSeeder implements CommandLineRunner {

    private final static Logger logger = LoggerFactory.getLogger(DestinationSeeder.class);

    private final DestinationRepository destinationRepo;

    public DestinationSeeder(DestinationRepository destinationRepo) {
        this.destinationRepo = destinationRepo;
    }

    @Override
    public void run(String... args) throws Exception {
        seed();
    }

    private void seed() {
        if (destinationRepo.count() == 0) {
            logger.info("Initierar databasen med destinationer...");

            try {
                List<Destination> destinationsToSave = new ArrayList<>();

                destinationsToSave.add(new Destination(
                        "Comfort Hotel", "Umeå", "Sweden", BigDecimal.valueOf(2999.99)));

                destinationsToSave.add(new Destination(
                        "Grand Hotel", "Stockholm", "Sweden", BigDecimal.valueOf(5499.00)));

                destinationsToSave.add(new Destination(
                        "Eiffel Views", "Paris", "France", BigDecimal.valueOf(8999.50)));

                destinationsToSave.add(new Destination(
                        "Colosseum Resort", "Rome", "Italy", BigDecimal.valueOf(7500.00)));

                destinationsToSave.add(new Destination(
                        "Alps Chalet", "Innsbruck", "Austria", BigDecimal.valueOf(6200.75)));

                // Spara ner alla till databasen i en batch
                destinationRepo.saveAll(destinationsToSave);

                logger.info("Databasen seedad med 5 destinationer!");
            } catch (Exception e) {
                logger.error("Fel under seedning av destinationer: {}", e.getMessage());
            }
        } else {
            logger.info("Databasen innehåller redan destinationer. Skippar seeding.");
        }
    }
}
