package com.mentoring.mentoringprj.util;

import com.mentoring.mentoringprj.exceptions.CurrencyUnsupportedException;
import com.mentoring.mentoringprj.exceptions.TooRichException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class NumbersToWordsTest {
    private NumbersToWords converter;

    @BeforeEach
    void setUp() throws Exception {
        converter = new NumbersToWords();
    }

    @Test
    public void testConvertIntegerToWords() {
        try {
            String result = converter.convertIntegerToWords(123);
            assertEquals("one hundred twenty three", result);
        } catch (TooRichException e) {
            fail("TooRichException should not be thrown");
        }
    }

    @Test
    public void testConvertDoubleToWords() throws TooRichException, CurrencyUnsupportedException {

        assertEquals("one hundred twenty three pounds forty five p", converter.convertDoubleToWords("123.45", "pound"));

        assertEquals("five pounds zero p", converter.convertDoubleToWords("5.00", "pound"));
    }

    @Test
    public void testConvertDoubleToWordsExceptions() throws TooRichException, CurrencyUnsupportedException {

        assertThrows(CurrencyUnsupportedException.class, () ->
                converter.convertDoubleToWords("123.45", "usd"));

        assertThrows(TooRichException.class, () ->
                converter.convertDoubleToWords("1000000.00", "pound"));
    }

    @Test
    public void testConvertIntegerToWordsValidConversion() throws TooRichException {
        assertEquals("one hundred twenty three", converter.convertIntegerToWords(123));
    }

    @Test
    public void testConvertIntegerToWordsValidConversionZero() throws TooRichException {
        assertEquals("zero", converter.convertIntegerToWords(0));
    }

    @Test
    public void testConvertIntegerToWordsValidConversionTensUnit() throws TooRichException {
        assertEquals("thirty five", converter.convertIntegerToWords(35));
    }

    @Test
    public void testConvertIntegerToWordsValidConversionHundred() throws TooRichException {
        assertEquals("four hundred seventy", converter.convertIntegerToWords(470));
    }

    @Test
    public void testConvertIntegerToWordsValidConversionThousand() throws TooRichException {
        assertEquals("five thousand two hundred sixty", converter.convertIntegerToWords(5260));
    }

    @Test
    void shouldThrowExceptionWhenCurrencyNotSupported() throws Exception {
        assertThrows(CurrencyUnsupportedException.class, () ->
                converter.convertDoubleToWords("1000001", "FAKE_MONEY"));
    }

    @Test
    void shouldThrowExceptionWhenOverMoneyLimit() throws Exception {
        assertThrows(TooRichException.class, () ->
                converter.convertIntegerToWords(1000001));
    }
}
