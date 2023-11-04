package com.mentoring.mentoringprj.util;

import com.mentoring.mentoringprj.exceptions.TooRichException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class NumberToWordsTest {

    private NumberToWord digitConverter = null;

    @BeforeEach
    void setUp() {
        digitConverter = new NumberToWord();
    }

    @Test
    void testconverToWords_Zero() throws Exception {
        assertEquals("zero pounds zero p", digitConverter.converToWords(0));
    }

    @Test
    void testconverToWords_SingleDigits() throws Exception {
        assertEquals("one pounds zero p", digitConverter.converToWords(1));
        assertEquals("nine pounds zero p", digitConverter.converToWords(9));
    }

    @Test
    void testconverToWords_Teens() throws Exception {
        assertEquals("eleven pounds zero p", digitConverter.converToWords(11));
        assertEquals("nineteen pounds zero p", digitConverter.converToWords(19));
    }

    @Test
    void testConversionForNumbersWithNonZeroDecimal() throws Exception {
        assertEquals("fifty five pounds zero p", digitConverter.convertDoubleToWords("55", "pound"));
        assertEquals("seventy two pounds zero p", digitConverter.converToWords(72));
    }

    @Test
    void testconverToWords_Tens() throws Exception {
        assertEquals("twenty pounds zero p", digitConverter.converToWords(20));
        assertEquals("ninety pounds zero p", digitConverter.converToWords(90));
    }

    @Test
    void testconverToWords_Hundreds() throws Exception {
        assertEquals("one hundred pounds zero p", digitConverter.converToWords(100));
        assertEquals("five hundred pounds zero p", digitConverter.converToWords(500));
        assertEquals("nine hundred pounds zero p", digitConverter.converToWords(900));
    }

    @Test
    void testconverToWords_Thousands() throws Exception {
        assertEquals("one thousand pounds zero p", digitConverter.converToWords(1000));
        assertEquals("five thousand pounds zero p", digitConverter.converToWords(5000));
        assertEquals("nine thousand pounds zero p", digitConverter.converToWords(9000));
    }

    @Test
    void testconverToWords_ComplexNumbers() throws Exception {
        assertEquals("two hundred thirty four pounds zero p", digitConverter.converToWords(234));
        assertEquals("seven thousand eight hundred ninety pounds zero p", digitConverter.converToWords(7890));
        assertEquals("twelve thousand three hundred forty five pounds zero p", digitConverter.converToWords(12345));
    }

    @Test
    void testTooRichException() {
        TooRichException exception = assertThrows(TooRichException.class, () -> digitConverter.converToWords(2000000));
        assertEquals("An error occurred, contact customer service", exception.getMessage());
    }

    @ParameterizedTest
    @CsvSource({
            "0, zero pounds zero p",
            "1, one pounds zero p",
            "2, two pounds zero p",
            "3, three pounds zero p",
            "4, four pounds zero p",
            "5, five pounds zero p",
            "6, six pounds zero p",
            "7, seven pounds zero p",
            "8, eight pounds zero p",
            "9, nine pounds zero p"
    })
    void testConvertDigitToWord(int input, String expectedOutput) throws Exception {
        assertEquals(expectedOutput, digitConverter.converToWords(input));
    }

    @Test
    void testConvertDoubleToWords() throws Exception {
        assertEquals("zero pounds zero p", digitConverter.convertDoubleToWords("0.0", "pound"));
        assertEquals("one pounds twenty p", digitConverter.convertDoubleToWords("1.20", "pound"));
        assertEquals("ten pounds ten p", digitConverter.convertDoubleToWords("10.10", "pound"));

        assertEquals("two pounds thirty four p", digitConverter.convertDoubleToWords("2.34", "pound"));
        assertEquals("three pounds forty five p", digitConverter.convertDoubleToWords("3.45", "pound"));
        assertEquals("four pounds sixty seven p", digitConverter.convertDoubleToWords("4.67", "pound"));
        assertEquals("five pounds eighty nine p", digitConverter.convertDoubleToWords("5.89", "pound"));
    }

}
