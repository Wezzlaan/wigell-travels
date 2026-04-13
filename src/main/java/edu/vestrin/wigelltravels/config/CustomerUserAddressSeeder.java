package edu.vestrin.wigelltravels.config;

import edu.vestrin.wigelltravels.entity.Address;
import edu.vestrin.wigelltravels.service.KeycloakUserService;
import edu.vestrin.wigelltravels.entity.Customer;
import edu.vestrin.wigelltravels.repository.AddressRepository;
import edu.vestrin.wigelltravels.repository.CustomerRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
@Order(1)
public class CustomerUserAddressSeeder implements CommandLineRunner {

    private final Logger logger = LoggerFactory.getLogger(CustomerUserAddressSeeder.class);

    private final CustomerRepository customerRepo;
    private final KeycloakUserService keycloakUserService;

    public CustomerUserAddressSeeder(CustomerRepository customerRepo, KeycloakUserService keycloakUserService) {
        this.customerRepo = customerRepo;
        this.keycloakUserService = keycloakUserService;
    }

    @Override
    public void run(String... args) throws Exception {
        seed();
    }

    private void seed() {
        if (customerRepo.count() == 0) {
            logger.info("Initierar databasen med medlemmar, Keycloak-användare och adresser...");

            try {
                List<Customer> customersToSave = new ArrayList<>();

                customersToSave.add(createCustomerSetup(
                        "Kvisten@dev.com", "Kvisten97", "Kalle", "Qvist",
                        "19970514-8765", "+46 73 132 43 54",
                        new Address("Sweden", "Uppsala", "Storavägen 9", "11111")));

                customersToSave.add(createCustomerSetup(
                        "Danielsson@dev.com", "Stoffe76", "Christoffer", "Danielsson",
                        "19760101-6789", "+46 70 234 56 78",
                        new Address("Sweden", "Umeå", "Lillavägen 45", "11115")));

                customersToSave.add(createCustomerSetup(
                        "Svensson@dev.com", "Svenne88", "Sven", "Svensson",
                        "19880505-1234", "+46 76 123 45 67",
                        new Address("Sweden", "Stockholm", "Vasagatan 1", "11120")));

                customersToSave.add(createCustomerSetup(
                        "Andersson@dev.com", "Andan99", "Anna", "Andersson",
                        "19710802-4321", "+46 73 123 45 67",
                        new Address("Sweden", "Göteborg", "Avenyn 2", "41136")));

                customersToSave.add(createCustomerSetup(
                        "Lindgren@dev.com", "Linden01", "Lisa", "Lindgren",
                        "20010922-5432", "+46 70 123 45 67",
                        new Address("Sweden", "Malmö", "Stortorget 3", "21122")));

                customerRepo.saveAll(customersToSave);

                logger.info("Databas och Keycloak seedad!");
            } catch (Exception e) {
                logger.error("Fel under seedning: {}", e.getMessage());
            }
        } else {
            logger.info("Databasen innehåller redan medlemmar. Skippar seeding.");
        }
    }

    private Customer createCustomerSetup(String email, String username,
                                         String firstName, String lastName,
                                         String socSecNum, String phoneNum,
                                         Address address) {

        String keycloakId = keycloakUserService.createUserKeycloak(
                email, username, "password123", firstName, lastName);

        var customer = new Customer();
        customer.setFirstName(firstName);
        customer.setLastName(lastName);
        customer.setSocSecNum(socSecNum);
        customer.setPhoneNum(phoneNum);
        customer.setKeycloakId(keycloakId);

        customer.getAddresses().add(address);

        return customer;
    }
}
