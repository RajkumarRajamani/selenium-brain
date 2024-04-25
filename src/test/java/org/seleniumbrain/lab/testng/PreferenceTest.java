package org.seleniumbrain.lab.testng;

import org.seleniumbrain.lab.core.selenium.validator.Validator;
import org.seleniumbrain.lab.utility.date.DateFormats;

import java.text.NumberFormat;
import java.text.ParseException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Currency;
import java.util.Locale;
import java.util.prefs.BackingStoreException;
import java.util.prefs.Preferences;

public class PreferenceTest {
  private static Preferences prefs;

  public void setPreference() throws BackingStoreException {
    // This will define a node in which the preferences can be stored
    prefs = Preferences.userRoot().node(this.getClass().getName());
    String ID1 = "Test1";
    String ID2 = "Test2";
    String ID3 = "Test3";
    String ID4 = "Test4";

    // First we will get the values
    // Define a boolean value
    System.out.println(prefs.getBoolean(ID1, true));
    // Define a string with default "Hello World
    System.out.println(prefs.get(ID2, "Hello World"));
    // Define a integer with default 50
    System.out.println(prefs.getInt(ID3, 50));
    System.out.println(prefs.getInt(ID4, 58));

    // now set the values
    prefs.putBoolean(ID1, false);
    prefs.put(ID2, "Hello Europaa");
    prefs.putInt(ID3, 453);

//     Delete the preference settings for the first value
    prefs.remove(ID1);
//    prefs.removeNode();
  }

  public static void main(String[] args) throws BackingStoreException {
//    PreferenceTest test = new PreferenceTest();
//    test.setPreference();
    System.out.println(Validator.getNumberWithThousandSeparator(55.0078, 2, Locale.UK));
    decimalDigits(34.984);
    decimalDigits(34.30);
    decimalDigits(34.0039);
    decimalDigits(34.0059);
    decimalDigits(34.0069);
    decimalDigits(34.003);
    decimalDigits(34.000);

    System.out.println();

    Double val = 11912000000.00005;
    decimalDigits(val);
    decimalDigits(34.300000);
    decimalDigits(34.000039);
    decimalDigits(34.000059);
    decimalDigits(34.00006);
    decimalDigits(34.00003);
    decimalDigits(34.000000);


    System.out.println(DateTimeFormatter.ofPattern(DateFormats.DD_MM_YYYY_SLASH.getFormat()).format(LocalDateTime.now().plusDays(30)));
    System.out.println(Double.parseDouble("0"));

    String input = "USD 10,000.78";
    String numbersOnly = input.replaceAll("[^0-9.]", ""); // Remove anything that is not a digit or a decimal point
    System.out.println("Numbers only: " + numbersOnly);


    double number = 10000000.78;
    String countryCode = "US"; // Country code (ISO 3166-1 alpha-2 code), e.g., "US", "GB", "FR", "DE", etc.

    // Create Locale for the given country code
    Locale locale = new Locale(countryCode);

    // Get number format for the locale
    NumberFormat numberFormat = NumberFormat.getNumberInstance(locale);

    // Format the number
    String formattedNumber = numberFormat.format(number);

    System.out.println("Formatted number for " + countryCode + ": " + formattedNumber);

    Double value = 4000389 * 20 * 0.01;
    System.out.println(value.toString());
    System.out.println(Validator.getNumberWithThousandSeparator(value, 5, Locale.UK));

//    test(); // gist.github.com/bradtraversy
  }



  public static void test() {
    String amountString = "EUR 1 000 000 000,89"; // Example input amount string
    String countryCode = "FR"; // Default country code (e.g., France)

    // Extract numerical part from the input string
    String numericPart = amountString.replaceAll("[^0-9,]", "");

    // Replace thousands separator with standard format
    numericPart = numericPart.replace(',', '.');

    // Parse the numerical part to a number
    double amount;
    try {
      Locale locale = new Locale("", countryCode);
      NumberFormat numberFormat = NumberFormat.getNumberInstance(locale);
      amount = numberFormat.parse(numericPart).doubleValue();
    } catch (ParseException e) {
      // Handle parsing exception
      amount = 0.0; // Default value if parsing fails
      e.printStackTrace();
    }

    // Get currency format for the locale
    Locale locale = new Locale("", countryCode);
    NumberFormat currencyFormat = NumberFormat.getCurrencyInstance(locale);

    // Format the number
    String formattedAmount = currencyFormat.format(amount);

    System.out.println("Formatted amount for " + countryCode + ": " + formattedAmount);
  }

  public static void decimalDigits(double val) {
//    double val = 34.584;

    // Extracting the decimal portion
    double decimalPortion = val - (long) val;

    // Extracting the first two digits of the decimal portion
    int firstTwoDigits = (int) (decimalPortion * 100000);

    // Checking if the first two digits are greater than 0
    if (firstTwoDigits < 5) {
      String result = Validator.getNumberWithThousandSeparator(val, 0, Locale.UK);
      System.out.println(val + " The first two digits of the decimal portion are not greater than 0(" + firstTwoDigits + ")  = " + result);
    } else {

      String result = Validator.getNumberWithThousandSeparator(val, 4, Locale.UK);
      System.out.println(val + " The first two digits of the decimal portion are greater than 0(" + firstTwoDigits + ") = " + result);
    }
  }
}