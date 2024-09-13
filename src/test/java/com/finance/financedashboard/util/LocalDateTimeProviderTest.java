package com.finance.financedashboard.util;

import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;

class LocalDateTimeProviderTest {
    @Test
    void provides_different_dates() {

        //arrange
        LocalDateTimeProvider timeProvider = new LocalDateTimeProvider();
        LocalDateTime firstDate = timeProvider.now();

        //act
        LocalDateTime secondDate = timeProvider.now();

        //assert
        assertThat(firstDate).isNotEqualTo(secondDate);
    }
}