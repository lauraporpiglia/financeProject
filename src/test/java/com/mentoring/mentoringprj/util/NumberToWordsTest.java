package com.mentoring.mentoringprj.util;

import com.mentoring.mentoringprj.exceptions.TooRichException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class NumberToWordsTest {

  private final static String  LABEL_POUND ="pounds";
  private final static String  LABEL_P ="p";

    private NumberToWord digitConverter = null;

    @BeforeEach
    void setUp() {
        digitConverter = new NumberToWord();
    }

    @Test
    void testconverToWords_Zero() throws Exception {
        assertEquals("zero " + LABEL_POUND + " zero "+LABEL_P, digitConverter.converToWords(0));
    }

    @Test
    void testconverToWords_SingleDigits() throws Exception {
        assertEquals("one " + LABEL_POUND + " zero "+LABEL_P, digitConverter.converToWords(1));
        assertEquals("nine " + LABEL_POUND + " zero "+LABEL_P, digitConverter.converToWords(9));
    }

    @Test
    void testconverToWords_Teens() throws Exception {
        assertEquals("eleven " + LABEL_POUND + " zero "+LABEL_P, digitConverter.converToWords(11));
        assertEquals("nineteen " + LABEL_POUND + " zero "+LABEL_P, digitConverter.converToWords(19));
    }

    @Test
    void testConversionForNumbersWithNonZeroDecimal() throws Exception {
        assertEquals("fifty five " + LABEL_POUND + " zero "+LABEL_P, digitConverter.convertDoubleToWords("55", "pound"));
        assertEquals("seventy two " + LABEL_POUND + " zero "+LABEL_P, digitConverter.converToWords(72));
    }

    @Test
    void testconverToWords_Tens() throws Exception {
        assertEquals("twenty " + LABEL_POUND + " zero "+LABEL_P, digitConverter.converToWords(20));
        assertEquals("ninety " + LABEL_POUND + " zero "+LABEL_P, digitConverter.converToWords(90));
    }

    @Test
    void testconverToWords_Hundreds() throws Exception {
        assertEquals("one hundred " + LABEL_POUND + " zero "+LABEL_P, digitConverter.converToWords(100));
        assertEquals("five hundred " + LABEL_POUND + " zero "+LABEL_P, digitConverter.converToWords(500));
        assertEquals("nine hundred " + LABEL_POUND + " zero "+LABEL_P, digitConverter.converToWords(900));
    }

    @Test
    void testconverToWords_Thousands() throws Exception {
        assertEquals("one thousand " + LABEL_POUND + " zero "+LABEL_P, digitConverter.converToWords(1000));
        assertEquals("five thousand " + LABEL_POUND + " zero "+LABEL_P, digitConverter.converToWords(5000));
        assertEquals("nine thousand " + LABEL_POUND + " zero "+LABEL_P, digitConverter.converToWords(9000));
    }

    @Test
    void testconverToWords_ComplexNumbers() throws Exception {
        assertEquals("two hundred thirty four " + LABEL_POUND + " zero "+LABEL_P, digitConverter.converToWords(234));
        assertEquals("seven thousand eight hundred ninety " + LABEL_POUND + " zero "+LABEL_P, digitConverter.converToWords(7890));
        assertEquals("twelve thousand three hundred forty five " + LABEL_POUND + " zero "+LABEL_P, digitConverter.converToWords(12345));
    }

    @Test
    void testTooRichException() {
        TooRichException exception = assertThrows(TooRichException.class, () -> digitConverter.converToWords(2000000));
        assertEquals("An error occurred, contact customer service", exception.getMessage());
    }

    @ParameterizedTest
    @CsvSource({
            "0, zero " + LABEL_POUND + " zero "+LABEL_P,
            "1, one " + LABEL_POUND + " zero "+LABEL_P,
            "2, two " + LABEL_POUND + " zero "+LABEL_P,
            "3, three " + LABEL_POUND + " zero "+LABEL_P,
            "4, four " + LABEL_POUND + " zero "+LABEL_P,
            "5, five " + LABEL_POUND + " zero "+LABEL_P,
            "6, six " + LABEL_POUND + " zero "+LABEL_P,
            "7, seven " + LABEL_POUND + " zero "+LABEL_P,
            "8, eight " + LABEL_POUND + " zero "+LABEL_P,
            "9, nine " + LABEL_POUND + " zero "+LABEL_P
    })
    void testConvertDigitToWord(int input, String expectedOutput) throws Exception {
        assertEquals(expectedOutput, digitConverter.converToWords(input));
    }

    @Test
    void testConvertDoubleToWords() throws Exception {
        assertEquals("zero " + LABEL_POUND + " zero "+LABEL_P, digitConverter.convertDoubleToWords("0.0", "pound"));
        assertEquals("one " + LABEL_POUND + " twenty "+LABEL_P, digitConverter.convertDoubleToWords("1.20", "pound"));
        assertEquals("ten " + LABEL_POUND + " ten "+LABEL_P, digitConverter.convertDoubleToWords("10.10", "pound"));

        assertEquals("two " + LABEL_POUND + " thirty four "+LABEL_P, digitConverter.convertDoubleToWords("2.34", "pound"));
        assertEquals("three " + LABEL_POUND + " forty five "+LABEL_P, digitConverter.convertDoubleToWords("3.45", "pound"));
        assertEquals("four " + LABEL_POUND + " sixty seven "+LABEL_P, digitConverter.convertDoubleToWords("4.67", "pound"));
        assertEquals("five " + LABEL_POUND + " eighty nine "+LABEL_P, digitConverter.convertDoubleToWords("5.89", "pound"));
    }

}
