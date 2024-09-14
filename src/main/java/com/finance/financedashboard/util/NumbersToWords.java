package com.finance.financedashboard.util;

import com.finance.financedashboard.exceptions.*;
import org.springframework.stereotype.Component;

import java.util.function.Function;
import java.util.HashMap;
import java.util.Map;

@Component
public class NumbersToWords {
    private static final String LABEL_POUND = "pounds";
    private static final String LABEL_P = "p";
    private static final int MAX_SUPPORTED_NUMBER = 1000000;
    private static final int THOUSAND = 1000;
    private static final int HUNDRED = 100;

    private static final Map<String, String[]> currencyLabels = new HashMap<>();
    static {
        currencyLabels.put("pound", new String[]{LABEL_POUND, LABEL_P});
        // Add support for other currencies here
    }

    private static final Function<Integer, String> getUnitWords = number -> {
        String[] units = {"zero", "one", "two", "three", "four", "five", "six", "seven", "eight", "nine"};
        return units[number];
    };

    private static final Function<Integer, String> getTensWords = number -> {
        String[] tens = {"", "ten", "twenty", "thirty", "forty", "fifty", "sixty", "seventy", "eighty", "ninety"};
        return tens[number];
    };

    private static final Function<Integer, String> getTeensWords = number -> {
        String[] teens = {"", "eleven", "twelve", "thirteen", "fourteen", "fifteen", "sixteen", "seventeen", "eighteen", "nineteen"};
        return teens[number];
    };

    public String convertDoubleToWords(String numberStr, String currency) throws CurrencyUnsupportedException, TooRichException {
        if (!currencyLabels.containsKey(currency)) {
            throw new CurrencyUnsupportedException("Currency not supported");
        }
        String[] curr = currencyLabels.get(currency);
        if (!numberStr.contains(".")) {
            numberStr = numberStr.concat(".00");
        }
        String[] splittedNum = numberStr.split("\\.");
        int intPart = Integer.parseInt(splittedNum[0]);
        int decimalPart = Integer.parseInt(splittedNum[1]);
        return convertIntegerToWords(intPart) + " " + curr[0] + " " + convertIntegerToWords(decimalPart) + " " + curr[1];
    }

    public String convertToWords(int number) throws CurrencyUnsupportedException, TooRichException {
        return convertDoubleToWords(String.valueOf(number), "pound");
    }

    private String convertIntegerToWords(int number) throws TooRichException {
        if (number == 0) {
            return "zero";
        }
        if (number >= MAX_SUPPORTED_NUMBER) {
            throw new TooRichException("Number too large to be converted");
          //  return "Number too large to be converted";
        }
        String words = "";
        if (number >= THOUSAND) {
            words += convertIntegerToWords(number / THOUSAND) + " thousand ";
            number %= THOUSAND;
        }
        if (number >= HUNDRED) {
            words += getUnitWords.apply(number / HUNDRED) + " hundred ";
            number %= HUNDRED;
        }
        if (number % 10 == 0 && number >= 10) {
            words += getTensWords.apply(number / 10) + " ";
        }
        if (number % 10 != 0 && number > 20) {
            words += getTensWords.apply(number / 10) + " ";
            number %= 10;
        }
        if (number >= 11 && number <= 19) {
            words += getTeensWords.apply(number - 10) + " ";
            number = 0;
        }
        if (number > 0 && number <= 9) {
            words += getUnitWords.apply(number) + " ";
        }
        return words.trim();
    }
}