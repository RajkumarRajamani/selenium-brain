package org.seleniumbrain.lab.testng;

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
    PreferenceTest test = new PreferenceTest();
    test.setPreference();
  }
}