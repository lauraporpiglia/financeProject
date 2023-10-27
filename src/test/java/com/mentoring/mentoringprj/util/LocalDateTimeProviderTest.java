package com.mentoring.mentoringprj.util;

import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

class LocalDateTimeProviderTest {
    @Test
    void provides_different_dates() {

        //arrange
        LocalDateTimeProvider timeProvider = new LocalDateTimeProvider();
        LocalDateTime firstDate = timeProvider.now();

        //act
        LocalDateTime secondDate = timeProvider.now().plusSeconds(1L);

        //assert
        assertThat(firstDate).isNotEqualTo(secondDate);
    }
}