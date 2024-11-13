package org.seleniumbrain.lab.cucumber.stepdefinitions;

import com.azure.security.keyvault.secrets.SecretClient;
import io.cucumber.datatable.DataTable;
import io.cucumber.java.DataTableType;
import io.cucumber.java.en.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.seleniumbrain.lab.core.cucumber.spring.configure.ScenarioState;
import org.seleniumbrain.lab.core.selenium.pageobjectmodel.SharedStateKey;
import org.seleniumbrain.lab.easyreport.assertions.Assertions;
import org.seleniumbrain.lab.core.selenium.driver.factory.DriverFactory;
import org.seleniumbrain.lab.core.selenium.driver.factory.WebDriverUtils;
import org.seleniumbrain.lab.snippet.BaseObjectRepository;
import org.seleniumbrain.lab.snippet.pagerepositories.demo.SwagLabHomePageOR;
import org.seleniumbrain.lab.snippet.pagerepositories.demo.SwagLabLoginPageOR;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.*;

public class LoginFeature_StepDefinitions extends BaseObjectRepository {


    @Autowired
    private DriverFactory driverFactory;

    @Autowired
    private WebDriverUtils driverUtils;

    @Autowired
    private SecretClient azureKeyVaulet;

    @Autowired
    private SwagLabLoginPageOR loginPageOR;

    @Autowired
    private SwagLabHomePageOR homePageOR;

    @Autowired
    private ScenarioState scenarioState;

    String a = "sd";
    @Given("user launch app")
    public void userLaunchApp() {
        System.out.println("User Launched the application");
//        driverFactory.getDriver().get("https://www.saucedemo.com/");
    }

    @When("they login with valid credentials")
    public void userLogin() {
        System.out.println("user login with valid credentials");
        Assertions assertions = new Assertions();
//        assertions.addKnownFailureLabels("UserName Field", "ID1000");
//        try {
//            Thread.sleep(new Random().nextInt(10000, 15000));
//        } catch (Exception ignored) {}
//        homePageOR
//                .withAssertion(assertions, homePageOR)
//                .enterUserName("standard_user")
//                .enterPassword("secret_sauce")
//                .login()
//                .openSideMenu();
//                .navigateToAboutPage();
//        try {
//            driverFactory.getDriver().get("https://www.saucedemo.com/" + scenarioState.getText(SharedStateKey.dummyText));
//        } catch (Exception ignored) {}
        driverUtils.attachStepLogInfo("C:\\abc\\asdjk\\asdj\\sdskjd\\jdj.txt");
        assertions.assertAll();
    }

    @Then("they are directed to home page")
    public void directedToHomePage() {
        System.out.println("user is directed to home page");
        System.out.println("Scenario State Shared Value: " + scenarioState.getCacheText().get(SharedStateKey.NAME));
    }

    // Register DataTableType to map a row to TestData object
    @DataTableType
    public TestData mapToTestData(Map<String, String> row) {
        TestData testData = new TestData();
        testData.setEventType(row.get("eventType"));
        testData.setCode(row.get("code"));
        testData.setUmr(row.get("umr"));
        testData.setPolicy(row.get("policy"));
        return testData;
    }

    @Given("Step 1")
    public void testStep(DataTable dataTable) {

//        System.out.println(request.requestFileName);
//        request.getDocumentNames().forEach(System.out::println);
//        List<Map<String, String>> rows = dataTable.asMaps(String.class, String.class);
//
//        Request request = new Request();
//        List<String> documentNames = new ArrayList<>();
//
//        for (Map<String, String> row : rows) {
//            if (row.get("requestFileName") != null && !row.get("requestFileName").isEmpty()) {
//                request.setRequestFileName(row.get("requestFileName"));
//            }
//            if (row.get("documentNames") != null && !row.get("documentNames").isEmpty()) {
//                documentNames.add(row.get("documentNames"));
//            }
//        }
//        request.setDocumentNames(documentNames);
//
//        // Use the request object as needed
//        System.out.println("Request File Name: " + request.getRequestFileName());
//        System.out.println("Document Names: " + request.getDocumentNames());

//        Assertions assertions = new Assertions();

//        this.assertions = assertions;
        try {
            List<TestData> list = dataTable.asList(TestData.class);
            System.out.println(list);
//            assertions.assertEqualsTo("UserName Field", 1, 2, "Not Equal Failed", "Equal Passed");
        } finally {
            assertions.assertAll();
        }

    }

//    @DataTableType
//    public Request requestEntry(Map<String, String> entries) {
//        Request request = Request.builder()
//                .documentNames(new ArrayList<>())
//                .build();
//
//        entries.forEach((key, value) -> System.out.println("REQUEST NAME: " + key + " ### DOCS : " + value));
//
//        request.setRequestFileName(entries.keySet().stream().filter(item -> !item.isBlank()).findFirst().get());
//        request.setDocumentNames(entries.values().stream().toList());
//
////        for(Map.Entry<String, String> entry : entries.entrySet()) {
////            if(Objects.nonNull(entry.) && !entry.get("requestFileName").isBlank())
////                request.setRequestFileName(entry.get("requestFileName"));
////
////            if(Objects.nonNull(entry.get("documentNames")) && !entry.get("documentNames").isBlank())
////                request.getDocumentNames().add(entry.get("documentNames"));
////        }
//
//        return request;
//    }

    @Data
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    public static class Request {
        private String requestFileName;
        private List<String> documentNames;
    }
}
