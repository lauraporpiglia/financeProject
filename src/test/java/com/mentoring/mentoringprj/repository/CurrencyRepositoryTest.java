package com.mentoring.mentoringprj.repository;

import okhttp3.mockwebserver.MockResponse;
import okhttp3.mockwebserver.MockWebServer;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class CurrencyRepositoryTest {
    private static final String TARGET_CURRENCY = "USD";
    private static final String BASE_CURRENCY = "GBP";
    @Mock
    private RestTemplate restTemplate;

    private MockWebServer mockWebServer;
    private CurrencyRepository repo;

    @BeforeEach
    void setup() throws Exception {
        mockWebServer = new MockWebServer();
        mockWebServer.start();
        String baseUrl = "http://localhost:%s".formatted(mockWebServer.getPort());
        repo = new CurrencyRepository(restTemplate, baseUrl);
    }

    @AfterEach
    void tearDown() throws Exception {
        mockWebServer.shutdown();
    }

    @Test
    void shouldReturnExchangeRate() {

        CurrencyRepository.ExchangeRateResponse exchangeRateResponse = new CurrencyRepository.ExchangeRateResponse();
        exchangeRateResponse.setData(Map.of(TARGET_CURRENCY, 1.0));

        when(restTemplate.getForEntity("http://localhost?apikey=fca_live_uF7SpRH0aQOxw2SsNNbXVKT9JbKHKab9RkK17ZuC&base_currency=GBP&currencies=USD"
                , CurrencyRepository.ExchangeRateResponse.class))
                .thenReturn(ResponseEntity.ok(exchangeRateResponse));

        CurrencyRepository repo = new CurrencyRepository(restTemplate, "http://localhost");

        double result = repo.getExchangeRate(BASE_CURRENCY, TARGET_CURRENCY);

        assertThat(result).isEqualTo(1);
    }

    //an IT would have been clearer and more robust, how?
    @Test
    void shouldReturnExchangeRateW() {


        mockWebServer.enqueue(new MockResponse()
                .setBody("""
                         {
                             "data": {
                               "%s": 0.55
                             }
                           }
                        """.formatted(TARGET_CURRENCY))
                .addHeader("Content-Type", "application/json")
        );


        double result = repo.getExchangeRateW(BASE_CURRENCY, TARGET_CURRENCY);

        assertThat(result).isEqualTo(.55);
    }

    // @todo: cover customexception for a null amount

}