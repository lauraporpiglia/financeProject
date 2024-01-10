package com.mentoring.mentoringprj.util;


import com.mentoring.mentoringprj.exceptions.TooRichException;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;

class NumbersToWordsTest {

    @Test
    public void testConvertIntegerToWords() {
        NumbersToWords converter = new NumbersToWords();

        try {
            String result = converter.convertIntegerToWords(123);
            assertEquals("one hundred twenty three", result);
        } catch (TooRichException e) {
            fail("TooRichException should not be thrown");
        }
    }
}