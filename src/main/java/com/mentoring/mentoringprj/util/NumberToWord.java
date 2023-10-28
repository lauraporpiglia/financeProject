package com.mentoring.mentoringprj.util;

import org.springframework.stereotype.Component;


@Component
public class NumberToWord {


        public String convertNumberToWords(double number) {
            int intPart = (int) number;
            int decimalPart = (int) ((number - intPart) * 100); // Assuming 2 decimal places
            String result="";

            String intPartWords = convertIntegerToWords(intPart);
            String decimalPartWords = convertIntegerToWords(decimalPart);

            if (intPartWords.isEmpty()) {
                 result=decimalPartWords;
            } else {
                if (decimalPartWords.isEmpty()) {
                    result= intPartWords;
                } else {
                    result= intPartWords + decimalPartWords ;
                }
            }
            if(result.endsWith("zero"))
                result=result.substring(0,result.length()-4);

            return result;
        }


    public String convertIntegerToWords(int number) {
        if (number == 0) {
            return "zero";
        }

        String[] units = {"", "one", "two", "three", "four", "five", "six", "seven", "eight", "nine"};
        String[] teens = {"", "eleven", "twelve", "thirteen", "fourteen", "fifteen", "sixteen", "seventeen", "eighteen", "nineteen"};
        String[] tens = {"", "ten", "twenty", "thirty", "forty", "fifty", "sixty", "seventy", "eighty", "ninety"};

        String words = "";
        if (number >= 1000) {
            words += convertIntegerToWords(number / 1000) + " thousand ";
            number %= 1000;
        }
        if (number >= 100) {
            words += units[number / 100] + " hundred ";
            number %= 100;
        }
        if (number >= 20) {
            words += tens[number / 10] + " ";
            number %= 10;
        }
        if (number >= 11 && number <= 19) {
            words += teens[number - 10] + " ";
            number = 0; // Since we've already accounted for the teen number
        }
        if (number > 0) {
            words += units[number] + " ";
        }

        return words.trim();
    }
    }

