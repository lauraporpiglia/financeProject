package com.mentoring.mentoringprj.util;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

class NumberToWordsTest {

    private NumberToWord digitConverter = null;

    @BeforeEach
    void setUp() {
        digitConverter = new NumberToWord();
    }

    @Test
    void testConvertIntegerToWords_Zero() {
        assertEquals("zero", digitConverter.convertIntegerToWords(0));
        assertEquals("zero", digitConverter.convertIntegerToWords(00));
    }

    @Test
    void testConvertIntegerToWords_SingleDigits() {
        assertEquals("one", digitConverter.convertIntegerToWords(1));
        assertEquals("nine", digitConverter.convertIntegerToWords(9));
    }

    @Test
    void testConvertIntegerToWords_Teens() {
        assertEquals("eleven", digitConverter.convertIntegerToWords(11));
        assertEquals("nineteen", digitConverter.convertIntegerToWords(19));
    }

    @Test
    void testConvertIntegerToWords_Tens() {
        assertEquals("twenty", digitConverter.convertIntegerToWords(20));
        assertEquals("ninety", digitConverter.convertIntegerToWords(90));
    }

    @Test
    void testConvertIntegerToWords_Hundreds() {
        assertEquals("one hundred", digitConverter.convertIntegerToWords(100));
        assertEquals("five hundred", digitConverter.convertIntegerToWords(500));
        assertEquals("nine hundred", digitConverter.convertIntegerToWords(900));
    }

    @Test
    void testConvertIntegerToWords_Thousands() {
        assertEquals("one thousand", digitConverter.convertIntegerToWords(1000));
        assertEquals("five thousand", digitConverter.convertIntegerToWords(5000));
        assertEquals("nine thousand", digitConverter.convertIntegerToWords(9000));
    }

    @Test
    void testConvertIntegerToWords_ComplexNumbers() {
        assertEquals("two hundred thirty four", digitConverter.convertIntegerToWords(234));
        assertEquals("seven thousand eight hundred ninety", digitConverter.convertIntegerToWords(7890));
        assertEquals("twelve thousand three hundred forty five", digitConverter.convertIntegerToWords(12345));
    }

    @ParameterizedTest
    @CsvSource({
            "0, zero",
            "1, one",
            "2, two",
            "3, three",
            "4, four",
            "5, five",
            "6, six",
            "7, seven",
            "8, eight",
            "9, nine"
    })
    void testConvertDigitToWord(int input, String expectedOutput) {
        assertEquals(expectedOutput, digitConverter.convertNumberToWords(input));
    }

    /*@todo fix, it doesnt work */
    @Disabled
     void testConvertNumberToWords() {
        // Example test cases for converting decimal numbers to words
        assertEquals("zero", digitConverter.convertNumberToWords(0.0));
        assertEquals("one point(?) two", digitConverter.convertNumberToWords(1.2));
        assertEquals("two point three four", digitConverter.convertNumberToWords(2.34));
        assertEquals("three point four five", digitConverter.convertNumberToWords(3.45));
        assertEquals("four point six seven", digitConverter.convertNumberToWords(4.67));
        assertEquals("five point eight nine", digitConverter.convertNumberToWords(5.89));
    }

}
