package com.finance.financedashboard.util;

import com.finance.financedashboard.exceptions.CurrencyUnsupportedException;
import com.finance.financedashboard.exceptions.TooRichException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class NumbersToWordsTest {
    private NumbersToWords converter;

    @BeforeEach
    void setUp() {
        converter = new NumbersToWords();
    }

    @Test
     void testConvertIntegerToWords() throws Exception {

        assertEquals("one hundred twenty three pounds zero p", converter.convertToWords(123));
    }

    @Test
     void testConvertDoubleToWords() throws Exception {

        assertEquals("one hundred twenty three pounds forty five p", converter.convertDoubleToWords("123.45", "pound"));

        assertEquals("five pounds zero p", converter.convertDoubleToWords("5.00", "pound"));
    }

    @Test
     void testConvertDoubleToWordsExceptions() {

        assertThrows(CurrencyUnsupportedException.class, () ->
                converter.convertDoubleToWords("123.45", "usd"));

//        assertThrows(TooRichException.class, () ->
//                converter.convertDoubleToWords("1000000.00", "pound"));
    }

    @Test
     void testConvertIntegerToWordsValidConversion() throws Exception {
        assertEquals("one hundred twenty three pounds zero p", converter.convertToWords(123));
    }

    @Test
     void testConvertIntegerToWordsValidConversionZero() throws Exception {
        assertEquals("zero pounds zero p", converter.convertToWords(0));
    }

    @Test
     void testConvertIntegerToWordsValidConversionTensUnit() throws Exception {
        assertEquals("thirty five pounds zero p", converter.convertToWords(35));
    }

    @Test
     void testConvertIntegerToWordsValidConversionHundred() throws Exception {
        assertEquals("four hundred seventy pounds zero p", converter.convertToWords(470));
    }

    @Test
     void testConvertIntegerToWordsValidConversionThousand() throws Exception {
        assertEquals("five thousand two hundred sixty pounds zero p", converter.convertToWords(5260));
    }

    @Test
    void shouldThrowExceptionWhenCurrencyNotSupported() {
        assertThrows(CurrencyUnsupportedException.class, () ->
                converter.convertDoubleToWords("1000001", "FAKE_MONEY"));
    }

    @Test
    void shouldThrowExceptionWhenOverMoneyLimit() {
        assertThrows(TooRichException.class, () ->
                converter.convertToWords(1000001));
    }
}
