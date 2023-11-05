package com.mentoring.mentoringprj.util;

import com.mentoring.mentoringprj.exceptions.CurrencyUnsupportedException;
import com.mentoring.mentoringprj.exceptions.TooRichException;
import org.springframework.stereotype.Component;

@Component
public class NumberToWord {
    static final String LABEL_POUND = "pounds";
    static final String LABEL_P = "p";
    public String convertDoubleToWords(String numberStr, String currency) throws TooRichException, IllegalArgumentException, CurrencyUnsupportedException {
        String[] curr ;
        if (currency.equalsIgnoreCase("pound")) {
            curr = new String[]{LABEL_POUND,LABEL_P};
        }
        else{
            throw new CurrencyUnsupportedException("Currency not supported");
        }
        if (!numberStr.contains(".")) {
            numberStr = numberStr.concat(".00");
        }

        String[] splittedNum = numberStr.split("\\.");
        int intPart = Integer.parseInt(splittedNum[0]);
        int decimalPart = Integer.parseInt(splittedNum[1]);
        String result = "";

        String intPartWords = convertIntegerToWords(intPart);
        String decimalPartWords = convertIntegerToWords(decimalPart);

        return result + intPartWords +
                " " +
                curr[0] +
                " " +
                decimalPartWords +
                " " +
                curr[1];
    }

    public String converToWords(int number) throws TooRichException, CurrencyUnsupportedException {
        return convertDoubleToWords(String.valueOf(number), "pound");
    }


    public String convertIntegerToWords(int number) throws TooRichException {

        String[] units = {"zero", "one", "two", "three", "four", "five", "six", "seven", "eight", "nine"};
        String[] teens = {"", "eleven", "twelve", "thirteen", "fourteen", "fifteen", "sixteen", "seventeen", "eighteen", "nineteen"};
        String[] tens = {"", "ten", "twenty", "thirty", "forty", "fifty", "sixty", "seventy", "eighty", "ninety"};

        String words = "";
        if (number == 0) {
            return "zero";
        }
        if (number >= 1000000) {
            throw new TooRichException("An error occurred, contact customer service");
        }
        if (number >= 1000) {
            words += convertIntegerToWords(number / 1000) + " thousand ";
            number %= 1000;
        }
        if (number >= 100) {
            words += units[number / 100] + " hundred ";
            number %= 100;
        }
        if (number % 10 == 0 && number >= 10 ) { //(&& number <= 90) always T
            words += tens[number / 10] + " ";
        }
        if (number % 10 != 0 && number > 20) {
            words += tens[number / 10] + " ";
            number %= 10;
        }
        if (number >= 11 && number <= 19) {
            words += teens[number - 10] + " ";
            number = 0; // teen number already accounted
        }

        if (number > 0 && number <= 9) {
            words += units[number] + " ";
        }

        return words.trim();
    }
}