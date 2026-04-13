package edu.vestrin.wigelltravels.integration;

import com.groupc.shared.client.CurrencyClient;
import edu.vestrin.wigelltravels.repository.DestinationRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class CurrencyClientIntegrationTest {

    @Test
    void shouldFetchRealExchangeRateFromFrankfurter() {
        CurrencyClient client = new CurrencyClient("http://localhost:8580");
        double rate = client.getExchangeRate("SEK", "PLN");
        assertTrue(rate > 0);
    }

    @Test
    void shouldReturnOneForSameCurrency() {
        CurrencyClient client = new CurrencyClient("http://localhost:8580");
        double rate = client.getExchangeRate("PLN", "PLN");
        assertEquals(1.0, rate);
    }
}
