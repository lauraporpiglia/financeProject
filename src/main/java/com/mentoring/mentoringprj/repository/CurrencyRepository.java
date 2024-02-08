package com.mentoring.mentoringprj.repository;

import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Repository;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.util.Map;

@Repository
public class CurrencyRepository {


    private final RestTemplate restTemplate;
    private final String baseUrl;
    private final WebClient webClient;


    public CurrencyRepository(RestTemplate restTemplate, @Value("${currency.url}") String baseUrl) {
        this.restTemplate = restTemplate;
        this.baseUrl = baseUrl;
        this.webClient = WebClient.create(baseUrl);
    }

    public double getExchangeRate(String baseCurrency, String targetCurrency) {
        // Double exchangeRate = 0.0;
        ResponseEntity<ExchangeRateResponse> entity = restTemplate.getForEntity(
                baseUrl + "?apikey=fca_live_uF7SpRH0aQOxw2SsNNbXVKT9JbKHKab9RkK17ZuC&base_currency="
                        + baseCurrency + "&currencies=" + targetCurrency, ExchangeRateResponse.class);
//        if (entity.getBody() != null)
        Double exchangeRate = entity.getBody().getData().get(targetCurrency);

        return exchangeRate;
    }

    public double getExchangeRateW(String baseCurrency, String targetCurrency) {
        Mono<ExchangeRateResponse> response = webClient.get()
                .uri(uriBuilder ->
                        uriBuilder
                                .queryParam("apikey")
                                .build()
                )
                .retrieve()
                .bodyToMono(ExchangeRateResponse.class);

        ExchangeRateResponse rate = response.block();
        return rate.getData().get(targetCurrency);
    }

    @Data
    static class ExchangeRateResponse {
        private Map<String, Double> data;
    }

}
