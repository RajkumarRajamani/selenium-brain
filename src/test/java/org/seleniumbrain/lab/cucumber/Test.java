package org.seleniumbrain.lab.cucumber;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.seleniumbrain.lab.utility.json.core.JsonBuilder;
import org.seleniumbrain.lab.utility.json.core.NodeValueType;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.Charset;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

public class Test {

    private enum LOB {
        TERRORISM, CASUALTY, PRCB;
    }

    public static void main(String[] args) throws MalformedURLException {
        String str = "&, \", <, >";
        str = StringUtils.replaceEach(str,
                new String[]{"&", "\"", "<", ">"},
                new String[]{"&amp;", "&quot;", "&lt;", "&gt;"});
        System.out.println(str);

//        ChromeOptions browserOptions = new ChromeOptions();
//        browserOptions.setPlatformName("Windows 11");
//        browserOptions.setBrowserVersion("117");
//
//        Map<String, Object> sauceOptions = new HashMap();
//        sauceOptions.put("username", "oauth-rajoviyaa.s-b07bf");
//        sauceOptions.put("accessKey", "3d34e79c-04b9-4b03-88f4-d813048065a1");
//        sauceOptions.put("build", "selenium-build-AH1I6");
//        sauceOptions.put("name", "saucelab-training-test");
//
//        browserOptions.setCapability("sauce:options", sauceOptions);
//
//        URL url = new URL("https://ondemand.eu-central-1.saucelabs.com:443/wd/hub");
//        RemoteWebDriver driver = new RemoteWebDriver(url, browserOptions);
//
//        driver.get("https://www.google.com");
//        try {
//            Process processA = Runtime.getRuntime().exec("whoami /upn");
//            String resultA = IOUtils.toString(processA.getInputStream(), Charset.defaultCharset());
//            System.out.println(resultA.trim());
//
//            Process processB = Runtime.getRuntime().exec("whoami");
//            String resultB = IOUtils.toString(processB.getInputStream(), Charset.defaultCharset());
//            System.out.println(resultB.trim());
//            System.out.println(System.getProperty("user.name"));
//
//
//        } catch (IOException e) {
//            e.printStackTrace();
//        }

//        try {
//            // Get current logged-in user name
//            String userName = getLoggedInUserName();
//            System.out.println("User Name: " + userName);
//
//            // Get email address associated with the user
//            String email = getUserEmailAddress(userName);
//            System.out.println("Email Address: " + email);
//        } catch (IOException | InterruptedException e) {
//            e.printStackTrace();
//        }

//        String s = JsonBuilder.getObjectBuilder()
//                .append("personal.name", "rajkumar")
//                .append("personal.workDetails", "", NodeValueType.ARRAY_STYLE_JSON.getType())
//                .append("personal.empHistory", null)
//                .buildAsJsonNode()
//                .toPrettyString();
//        System.out.println(s);
//
//        String printFormat = "%-50s %-10s";
//        Map<String, String> map = new HashMap<>() {
//            {
//                put("Company Name1", "Cognizant");
//                put("Company Name2", "TCS");
//                put("Company Name3", "Infosys");
//            }
//        };
//        map.forEach((key, value) -> System.out.printf(printFormat + "%n", key, value));
//
//        System.out.println("CasualtyBusiness".contains("Casualty"));
//        System.out.println(LOB.TERRORISM.name());
//        System.out.println(LOB.valueOf("TERRORISM"));
//        String val = LOB.TERRORISM.name() + "|adsd|sadfs|234|sdfs";
//        System.out.println(val.split("\\|")[0]);
//        System.out.println(val.split("\\|")[1]);
//        System.out.println(val.split("\\|")[2]);
//
//        String[] words = {"fruit", "TERRORISM", "Casualty"};
//        for (String word : words) {
//            String capitalizedText = StringUtils.capitalize(word.toLowerCase());
//            System.out.println(capitalizedText);
//        }
//
//        System.out.println(LocalDateTime.now().format(DateTimeFormatter.ofPattern("dd-MM-yyyy")));
    }

    private static boolean flag = true;
    public void threadMethod() {
        Thread notifyThread = new Thread(() -> {
            while(flag) {
                try {
                    Thread.sleep(10000);
                    System.out.println("Notifying all threads...");
                    notifyAll();
                } catch (Exception ignored) {

                }
            }
        });

        notifyThread.start();

        try {
            Thread.sleep(120000);
            flag = false;
        } catch(Exception e) {

        }
    }

    private static String getLoggedInUserName() throws IOException, InterruptedException {
        Process process = Runtime.getRuntime().exec("whoami");
        process.waitFor();

        BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
        String userName = reader.readLine();
        reader.close();

        return userName.trim(); // Trim to remove any leading or trailing whitespace
    }

    private static String getUserEmailAddress(String userName) throws IOException, InterruptedException {
        // This command retrieves the email address associated with the user's account
        String command = "dscl . -read /Users/" + userName + " EMailAddress";

        Process process = Runtime.getRuntime().exec(new String[]{"/bin/bash", "-c", command});
        process.waitFor();

        BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
        String line;
        StringBuilder email = new StringBuilder();
        while ((line = reader.readLine()) != null) {
            email.append(line.trim());
        }
        reader.close();

        return email.toString().replace("EMailAddress: ", "");
    }
}
