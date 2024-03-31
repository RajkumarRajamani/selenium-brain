# SeleniumBrain

### Description

A framework used for Web UI and API automation testing using selenium. This framework is designed by
leveraging features of Cucumber-Spring.
It helps any QA tester to design their application test cases in Standard Gherkin Language to provide better
User Acceptance Level documentation of business requirements.

### Tools & Technologies

| Tools/Technologies                                                                                                                                 |                Name                |       Version       |
|:---------------------------------------------------------------------------------------------------------------------------------------------------|:----------------------------------:|:-------------------:|
| <img src="https://user-images.githubusercontent.com/25181517/117201156-9a724800-adec-11eb-9a9d-3cd0f67da4bc.png" alt="JDK" width="40" />           |              **JDK**               |        `17+`        |
| <img src="https://user-images.githubusercontent.com/25181517/117207242-07d5a700-adf4-11eb-975e-be04e62b984b.png" alt="Maven" width="40" />         |             **Maven**              |       `3.8.9`       |
| <img src="https://user-images.githubusercontent.com/25181517/190229463-87fa862f-ccf0-48da-8023-940d287df610.png" alt="Lombok" width="40" />        |             **Lombok**             |      `Latest`       |
| <img src="https://user-images.githubusercontent.com/25181517/184103699-d1b83c07-2d83-4d99-9a1e-83bd89e08117.png" alt="Selenium" width="40" />      |            **Selenium**            |      `4.16.1`       |
| <img src="https://user-images.githubusercontent.com/25181517/184117353-4b437677-c4bb-4f4c-b448-af4920576732.png" alt="Cucumber" width="40" />      |            **Cucumber**            |      `7.15.0`       |
| <img src="https://user-images.githubusercontent.com/25181517/183891303-41f257f8-6b3d-487c-aa56-c497b880d0fb.png" alt="Spring Boot" width="40" />   |          **Spring Boot**           |       `3.0.9`       |
| <img src="https://user-images.githubusercontent.com/25181517/192107858-fe19f043-c502-4009-8c47-476fc89718ad.png" alt="RestAssured" width="40" />   |          **RestAssured**           |       `5.3.1`       |
| <img src="https://howtodoinjava.com/wp-content/uploads/2016/08/Super-CSV.png" alt="OpenCSV" width="40" />                                          |            **OpenCSV**             |       `5.7.1`       |
| <img src="https://user-images.githubusercontent.com/25181517/192108890-200809d1-439c-4e23-90d3-b090cf9a4eea.png" alt="IntelliJ Idea" width="40" /> | **Recommended IDE I   - IntelliJ** | `Community Edition` |
| <img src="https://user-images.githubusercontent.com/25181517/192108891-d86b6220-e232-423a-bf5f-90903e6887c3.png" alt="VS Code" width="40" />       |  **Recommended IDE II - VS Code**  | `Community Edition` |
| <img src="https://user-images.githubusercontent.com/25181517/192108892-6e9b5cdf-4e35-4a70-ad9a-801a93a07c1c.png" alt="Eclipse" width="40" />       | **Recommended IDE III - Eclipse**  | `Community Edition` |

> [!NOTE]
> Note: Use only the above versions of dependencies
---

## Table of Contents

1. #### Project Setup
2. #### Initializer
3. #### Cucumber-Spring
4. #### Configuration Readers
    1. Framework Specific [SeleniumConfig]
    2. Application Specific [Application Under Test - AUT Configs]
5. #### Selenium WebDriver Factory
6. #### WebElement Validators
7. #### Page Object Repository Setup
8. #### Utilities
    1. CSV handler
    2. Date handler
    3. JSON handler
    4. File Utils
    5. Retry Mechanism
    6. Numeric Text Comparator
9. #### Reports [Easy Cucumber Report]
10. #### Test Script Definitions
11. #### Quick Start

---

### 1. Project Setup

Project can be easily setup by following below simple steps,

1. Go to GitHub repository <>
2. Copy the project url
3. Open `cmd` window [on Windows] or `terminal` window [on Mac]
4. Navigate to any desired folder where you want to maintain the project
5. Use the below git commands in its sequential order to clone the project from GitHub Remote repository
    ```
    => git clone <remore repository url
    => git fetch
    => git merge
    ```
6. Launch your favorite IDE [IntelliJ Idea is recommended]
7. Open the project cloned above into your IDE
8. Wait until all maven dependencies are downloaded and the setup is complete
9. Once the project is imported, ensure that all below required plugins are installed on your IDE
    1. Gherkin Plugin
    2. Json Viewer
    3. lombok

> [!IMPORTANT]
> Ensure that you have installed JDK and your favorite IDE [IntelliJ/VS Code/Eclipse] on your system before following
> above steps
---

### 2. Initializer

You could ignite the execution with the below few simple steps. As the framework follows Cucumber
style creation of test scripts, basically we need to initiate the execution in a conventional way of
how we usually run any other cucumber project.

1. Go to `src/test/java` and expand `org.seleniumbrain.lab.cucumber.init`
2. Open the cucumber runner file `CucumberRunnerTest.java`
3. Right-Click on the file or editor and choose `Run "CucumberRunnerTest.java"` option

> [!TIP]
> You can customize the runner file by gluing exact location details for feature files, step definitions package
> and tag information
---

### 3. Cucumber-Spring

#### a. About Spring

Spring is a vast subject to discus about it here. However, we have to mention here how much we extract from Spring
into this cucumber project and how it is beneficial to our automation project.

Spring has a core feature called Inversion of Control [IoC] or Dependency Injection.

Spring contains an application context where it stores all the objects created bounded with its scope.
In our automation code, wherever we need an instance from spring's application context, we can
simply call the object by using `@Autowired` annotation.

#### Steps to make a class as Spring Bean and make CucumberContext aware of it

1. Create a class in any package under `src/main/java`
2. Annotate with spring stereotype `@Component` annotation.
   This tells spring that a bean object needs to be created and stored in CucumberContext for future usage.
3. Annotate with cucumber scope `@ScenarioScope` annotation.
   This defines the scope or life of bean.
4. The above steps will be more than enough to let Spring create an instance/bean of
   the respective class in its application context memory

> [!NOTE]
>
> - [X] Spring provides multiple bean scopes such as `@Singleton`, `@Prototype` and few other types
    > related to http request [those are not intended for testers].
>
>
> - [X] However, cucumber-spring integration also provides us a special bean scope called `@ScenarioScope`.
>
>
> - [X] `@ScenarioScope` keeps the life span of the bean until the current cucumber scenario is alive.
    > i.e., the scenario scoped bean is created and stored into Cucumber-Spring Application context [CucumberContext]
    > at the beginning of every cucumber scenario and destroyed at the end of scenario execution.
>
>
> - [X] For every scenario, `@ScenarioScope` beans will be created and destroyed. This gives
    > an advantage for testers to keep the state of Gherkin steps stored and pass the data to next steps
    > without being using any static instances and making code complex.
>
>
> - [X] It helps us to avoid using `new` keyword everytime we need to create an instance of a certain
    > reusable bean.
   ---   

#### b. Spring Integration with Cucumber Project

We can include spring features with our plain cucumber project by following below few simple steps.

1. Create a class with any name of your desire in any package under `src/main/java` and annotate it with Spring's
   annotations `@SpringBootApplication` and `@ComponentScan`.
   ```java
   import org.springframework.boot.autoconfigure.SpringBootApplication;
   import org.springframework.context.annotation.ComponentScan;
   
   @SpringBootApplication
   @ComponentScan("org.seleniumbrain")
   public class SeleniumBrainCucumberTestExecutionEngine {
   }
   ```

   > [!NOTE]
   > - [X] `@SpringBootApplication` - Tells cucumber to run the project as spring application.
   >
   > - [X] `@ComponentScan` - Tells cucumber to allow spring to scan through all mentioned packages for
       `@Component` bean classes to create bean and store it into CucumberContext

2. Create another class with any name of your desire in any package
   under `src/main/java` [usually under same package as followed in above step]
   and annotate it with Spring's annotations `` and ``

   ```java
   import io.cucumber.spring.CucumberContextConfiguration;
   import org.springframework.boot.test.context.SpringBootTest;
   
   @CucumberContextConfiguration
   @SpringBootTest(classes = {SeleniumBrainCucumberSpringConfiguration.class, SeleniumBrainCucumberTestExecutionEngine.class})
   public class SeleniumBrainCucumberSpringConfiguration {
   }
   ```

   > [!NOTE]
   > - [X] `@CucumberContextConfiguration` - Tells cucumber to create a cucumber context [container]
       to store all beans that are created by Spring
   >
   >
   > - [X] `@SpringBootTest` - Tells cucumber to look for beans. In our project, we have mentioned
       `SeleniumBrainCucumberSpringConfiguration.class` and `SeleniumBrainCucumberTestExecutionEngine.class` to scan
       through for beans.

By following the above two steps, while initiating cucumber execution from runner file, will first read the above
configurations
and run it like a spring project execution enabling all Spring Features internally.

### 4. Configuration Readers

We are maintaining two types of configurations

- SeleniumConfiguration
- AUT Configurations

#### 1. Selenium Configurations

It contains configurations related to the framework and selenium tool and is maintained
at `src/main/resources/configs/selenium-config.yml`

Below are the types of configurations being maintained in the yml file.

1. Test Lab [LocalLab/Grid/SauceLab/Perfecto/Docker]

   > [!IMPORTANT]
   > Currently only **LocalLab** is supported. Other Lab options are under Development.

2. Every Lab contains all possible configurations for browsers
3. Selenium Timeouts
4. Test Environment Name [on which we run execution] : Testers are requested to mention name of the application
   configuration yaml file name. Usually, it is recommended
   to name application configuration file name as `dev.yml` or `qa.yml` or `uat.yml`
5. Output Folders: If not provided, the framework will choose a default path as defined
   in `org.seleniumbrain.lab.core.config.SeleniumConfigReader.java`

`selenium-config.yml` file is mapped to its equivalent java object class `SeleniumConfigurations.java`
using `ObjectMapper.java` class from a jackson library.

**Usages**

You can access selenium configuration details present in `selenium-config.yml` file, from anywhere in the project, by
directly using `SeleniumConfigReader.java` class name itself.

``` java
SeleniumConfigReader.getConfigs();
```

#### 2. AUT Configurations

It contains configuration related to the application Under Test [AUT] and is maintained
at `src/main/resources/configs/aut-configs`

It is recommended to maintain the name of the application-related configuration yaml file as the environment name
itself. For Example,
to maintain application configuration for a dev region, create a file `dev.yml` and maintain all configurations related
to AUT.
Similarly, for other environments like QA or UAT, it could be `qa.yml` and `uat.yml` respectively.

However, it is not mandated that the config file naming should be as above. You can maintain any name as you desire.

Point is -- whatever the name that we provide for the application configuration yaml file, the same name should be given
in `selenium-config.yml` file
for property `environment` without extension. For Example,

``` yaml
test:
  app:
    environment: dev
```

Framework, when started, will start looking for file under `src/main/resources/configs/aut-configs` package with the
extension 'yml' and read it and map it to
`org.seleniumbrain.lab.core.config.pojo.AUTConfiguration`

**Usage**

You can access application-specific configurations details present in `<environment-name>.yml` file,
from anywhere in the project,
by directly using
`AUTConfigReader.java` class

``` java
AUTConfigReader.get();
```

---

### 5. Selenium WebDriver Factory

We require an instance of Selenium WebDriver for any desired browser to automate any Web UI applications.
All we need to do is to use an instance of `WebDriverUtils.java` class as it has rich options of all basic web driver
operations.

Testers do not need to worry about writing snippets for initializing an instance for `WebDriver.java` class.
They can simply provide all browser configurations in `selenium-config.yml` file itself and the remaining task
will be taken care by framework itself to create an instance of the browser based on the given configurations.

It has multiple reusable methods as in below table.

| Method Name                             | Usage                                                                                                                     |
|:----------------------------------------|:--------------------------------------------------------------------------------------------------------------------------|
| getDriver()                             | returns an instance of `WebDriver.java` class                                                                             |
| isWebDriverNull()                       | returns if an instance of WebDriver class is null or not any given instance                                               |
| createNewSession()                      | opens new browser window of any given browser                                                                             |
| launchUrl(String url)                   | loads any given url link in the browser                                                                                   |
| launchUrlInNewWindow(String url)        | opens given URL in new Browser Window                                                                                     |
| getCurrentUrl()                         | returns current url                                                                                                       |
| getCurrentWindow()                      | returns the id of current Window                                                                                          |
| getWindowsList()                        | returns `Set<String>` of IDs of all windows                                                                               |
| refreshPage()                           | it refreshes the page and wait until page load is complete                                                                |
| maximizeWindow()                        | it maximizes the current window                                                                                           |
| minimizeWindow                          | it minimizes the current window                                                                                           |
| navigateBack()                          | navigates to previous page                                                                                                |
| navigateForward()                       | navigates to next page                                                                                                    |
| fullScreen()                            | makes the browser to full scren                                                                                           |
| clearCookies(String namedCookie)        | clears the cookie of given name                                                                                           |
| closeCurrentWindow()                    | close current window                                                                                                      |
| quiteWebDriver()                        | quite entire browser session                                                                                              |
| attachScreenshot(String screenshotName) | attaches a screenshot of a web page to current cucumber step                                                              |
| attachStepLogInfo(String logMsg)        | attaches a given step log as an evidence with current cucumber step                                                       |
| getScreenshotAsImage(String imageName)  | takes screenshot of current web page and stores it in a .png format under output folder and returns the path of the image |
| getScreenshotInBytes()                  | takes screenshot of current web page and returns in `byte[]` array format                                                 |
| findElementBy(By by)                    | with given `By` locator, it returns an element if identified on WebPage. Else returns null                                |
| findElements(By by)                     | with given `By` locator, it returns list of elements if identified on WebPage. Else returns an empty Collections list.    |

Similar to above `WebDriverUtils.java` class, the framework provides rich methods of all possible `WebDriverWait.java`
methods.

**Usage**

As `WebDriverUtils.java` and `WebDriverWaits.java` are defined as a spring bean component, we do not need to create an
instance using `new` key-word,
instead, we can request `CucumberContext` to return an instance using `@Autowired` annotation and access all of its
reusable methods.

``` java
@Autowired
public WebDriverUtils webDriverUtils;

@Autowired
public WebDriverWaits webDriverWaits;

public void createNewBrowserSessionAndLaunchUrl(String url) {
    webDriverUtils.createNewSession();
    webDriverUtils.launchUrl(url);
   
    WebElement brokerName = webDriverUtils.findElement(By.xpath("//p[@id='brokerName']"));
    WebElement brokerEmail = webDriverWaits.untilElementToBeClickable(By.xpath("//p[@id='brokerEmail']")); 
}
```

### 6. WebElement Validators

As UI automation ultimately aims at validating UI web elements, for example, validating element text,
editable/read-only, enabled/disabled, displayed/not-displayed,
this framework is enriched with few validator classes for some common UI elements. Below are some of the common elements
validators.

- `CommonElementValidator.java`
- `TextBoxValidator.java`
- `AutoPrefixedTextBoxValidator.java`
- `CheckboxValidator.java`
- `CollapsibleValidator.java`
- `DropdownValidator.java`

Each of the above validator classes is provided with some of the common validations and some validations specific to the
element type. Please explore the methods
in each class as they are self-explanatory.

In the interest of documentation, mentioning the validation methods from `TextBoxValidator.java` class as below.

| Method Name                                                                             | Usage                                                                                                                                                                                                                                                                                                                                                                                                         |
|:----------------------------------------------------------------------------------------|:--------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------|
| takeScreenshot(String caption)                                                          | takes screenshot of web page while validating the element                                                                                                                                                                                                                                                                                                                                                     |
| peek(BaseElement elementType, Consumer<BaseElement> consumer)                           | use this method when we want to perform something on this element or any other random operation before performing next validation chain on the element                                                                                                                                                                                                                                                        |
| pause(long milliseconds)                                                                | pause the execution while the element is being validated                                                                                                                                                                                                                                                                                                                                                      |
| isDisplayed()                                                                           | validates if given element is displayed on UI within max timeout set on the framework configuration                                                                                                                                                                                                                                                                                                           |
| isDisplayed(long maxTimeoutInSeconds)                                                   | validates if given element is displayed on UI within given `maxTimeOoutInSeconds`                                                                                                                                                                                                                                                                                                                             |
| isEnabled()                                                                             | validates if given element is enabled on UI                                                                                                                                                                                                                                                                                                                                                                   |
| isNotEnabled()                                                                          | validates if given element is not enabled on UI                                                                                                                                                                                                                                                                                                                                                               |
| isNotDisplayed(By elementLocatedBy, long maxTImeoutInSeconds)                           | validates if element located by `By elementLocatedBy` is not displayed within given `long maxTimeoutInSeconds`                                                                                                                                                                                                                                                                                                |
| isDisabled()                                                                            | validates if given element is disabled on UI                                                                                                                                                                                                                                                                                                                                                                  |
| isDisabledUsingClassAttribute()                                                         | validates if given element is disabled when `class` attribute does not contain `disabled` value                                                                                                                                                                                                                                                                                                               |
| isDisabledUsingClassAttribute(String classAttributeKey)                                 | validates if given element is disabled when `class` attribute does not contain given key                                                                                                                                                                                                                                                                                                                      |
| isNotDisabledUsingClassAttribute()                                                      | validates if given element is not disabled when `class` attribute does not contain `disabled`  value                                                                                                                                                                                                                                                                                                          |
| isNotDisabledUsingClassAttribute(String classAttributeKey)                              | validates if given element is not disabled when `class` attribute does not contain given key                                                                                                                                                                                                                                                                                                                  |
| isEditable()                                                                            | validates if given element is editable in nature                                                                                                                                                                                                                                                                                                                                                              |
| isNotEditable()                                                                         | validates if given element is not editable in nature                                                                                                                                                                                                                                                                                                                                                          |
| isMatching(String expectedValue)                                                        | validates if given element text is matching `expectedValue                                                                                                                                                                                                                                                                                                                                                    |
| isTextLengthAllowedTill(int expectedAllowedLength)                                      | validates if given element allows to enter text of length equal to `expectedAllowedLength`                                                                                                                                                                                                                                                                                                                    |
| isTextLengthBeyondLimitShowsError(int expectedAllowedLength, WebElement uiErrorElement) | validates if given element shows `uiErrorElement` while updating the field with more than allowed character length `expectedAllowedLength`                                                                                                                                                                                                                                                                    |
| isErrorDisplayedWhenTextMatches(String matchingText, By uiErrorElement)                 | validates if given element shows `uiErrorElement` when element's pre-populated text matches given `matchingText`                                                                                                                                                                                                                                                                                              |
| isErrorDisplayedForInvalidValueUpdate(String invalidInputText, By uiErrorElement)       | validates if given element shows `uiErrorElement` while updating the field with `invalidInputText`                                                                                                                                                                                                                                                                                                            |
| isEditableNumberFields()                                                                | validates if given element is editable and allows only numbers                                                                                                                                                                                                                                                                                                                                                |
| isThousandSeparated(String numberInput, int decimalPrecision, Locale locale)            | validates if given element displays the updated numeric value with thousand separator and decimal precision of given decimal limits according to the `Locale`                                                                                                                                                                                                                                                 |
| apply(WebElement elementToValidate, String elementName)                                 | All above validations are performed only while calling this method by supplying `elementToValidate` and `elementName`. This method does not add validations into `Assertions` and hence not reflected in "Easy Cucumber HTML Report". From this method, We can get either boolean result of success/failure or list of errors occurred.                                                                       |
| applyAndAssert(WebElement elementToValidate, String elementName, Assertions assertions) | All above validations are performed only wheile calling this method by supplying `elementToValidate`, `elementName` and `Assertions` object. This method do add all validations into `Assertions` object and therefore failures are reported in "Easy Cucumber HTML Report" with elementName as identifier. From this method, We can get either boolean result of success/failure or list of errors occurred. |

**Usage**

Assume that you are trying to perform below validations on any desired element.

- to check if an element is displayed
- to check if an element is editable
- to check if an element is showing the updated text with a thousand-separator format of UK region with 3 decimal precisions

First when we do not want to add above validations into assertions,

``` java
@Autowired
private TextBoxValidator textBoxValidator;

@FindBy(xpath = "//input[@id='Limit-Amount-data-test-id'])
private WebElement limitAmountField;


public void validateIfLimitFieldShowsThousandSeparatorWithThreeDecimals(String limitInputAmount) {

    // below lines carry out each validations lined up in the method chaining below and returns either boolean or `Set<String> errors`.
    ValidatorOutput output = textBoxValidator.isDisplayed()
                                            .isEditable()
                                            .isThousandSeparated(limitInputAmount, 3, Locale.UK)
                                            .apply(this.getLimitAmountField(), "Limit Amount");

    boolean areAllValidationsPassed = output.isPassed();
    
    if(areAllValidationsPassed) {
        ... // rest of the code
    }
}
 ```

 On the other hand, when we do prefer to add all validations into assertions,

``` java
@Autowired
private TextBoxValidator textBoxValidator;

@FindBy(xpath = "//input[@id='Limit-Amount-data-test-id'])
private WebElement limitAmountField;


public void validateIfLimitFieldShowsThousandSeparatorWithThreeDecimals(String limitInputAmount) {

    Assertions assertions = new Assertions();

    // below lines carry out each validations lined up in the method chaining below and add them up to assertions by default to write them on the easy-cucumber-html-report.
    // Also, we can retrieve boolean result or `Set<String> errors`.
    ValidatorOutput output = textBoxValidator.isDisplayed()
                                            .isEditable()
                                            .isThousandSeparated(limitInputAmount, 3, Locale.UK)
                                            .apply(this.getLimitAmountField(), "Limit Amount", assertions);

    
    ... // rest of the code

    assertions.assertAll();
}
 ```

 **Special Note on peek() method**

 If you like to perform certain operation at any point, while chaining multiple validation methods, it can be 
 achieved by simply using `peek()` method.

 It accepts `BaseElement` and `Consumer<BaseElement>` as argument. If you want to perform some operation on
 the element being validated, before you start that validation, simply use it as below.

 For Example, the element that you are trying to validate is an `<input>` html element, and before checking
 thousand-separator format, assume you want to perform some operations either on that field itself or any generic operation to meet the criteria of validation, then the snippet goes as below, 

 ``` java

@Autowired
private TextBoxValidator textBoxValidator;

@Autowired
private TextBox textBox; // it represents a text box type of UI element customized in this framework

@FindBy(xpath = "//input[@id='Limit-Amount-data-test-id'])
private WebElement limitAmountField;


public void validateIfLimitFieldShowsThousandSeparatorWithThreeDecimals(String limitInputAmount) {

    Assertions assertions = new Assertions();

    // below lines carry out each validations lined up in the method chaining below and add them up to assertions by default to write them on the easy-cucumber-html-report.
    // Also, we can retrieve boolean result or `Set<String> errors`.
    ValidatorOutput output = textBoxValidator.isDisplayed()
                                            .isEditable()
                                            .peek( 
                                                textBox, // instance of element of type `BaseElement` being peeked upon inside next argument `Consumer<BaseElement>`
                                                txtBox -> {
                                                    // ... write anything you want here
                                                }
                                            ) // this peek method will be peformed first before proceeding to next method
                                            .takeScreenshot("Performed some operation before checking thousand separator validation") // you can add screenshot in between into cucumber step
                                            .pause(5000) // you can hault the execution for sometime in between in case next validation in sequence requires some time to take effect on the element
                                            .isThousandSeparated(limitInputAmount, 3, Locale.UK)
                                            .apply(this.getLimitAmountField(), "Limit Amount", assertions);

    
    ... // rest of the code

    assertions.assertAll();
}

 ```

 **Explanation**

 In the above code, when the method `apply` is executed in its chain, then the web element returned by `this.getLimitAmountField()` is undergoing `isDisplayed()` validation first, followed by `isEditable()` validation and before starting `isThousandSeperated()` validation, it is going to perform something which is written
in `Consumer<BaseElement>` inside peek method. Then it takes screenshot of outcome of the peek method and sleeps for 5 seconds and finally `isThousandSeparated()` validation will be performed on the web element.


### 7. Page Object Repository Setup

You can create a page object repository class and initialize the web elements by annotating the class with
`@PageObjects`. This annotation not only initializes the PageObjectFactory elements and also makes the class as a spring/cucumber-spring context bean component, so you can retrieve it with `@Autowired` annotation

Everytime you create a page object repository class, do follow below important steps to utilize most of the framework's features,

1. Annotate it with `@PageObjects` to make it like Spring bean and initialize its `@FindBy` web elements as PageObjectFacgtory
2. Annotate it with `@Data` to use lombok service which gives easy way to generate all `getters` and `setters`
of all memebers
3. Extend the class `BaseObjectRepository` which gives reference object all beans available in this framework, so you do not need to use `@Autowired` in all the classes
4. Implement an interface `PageObjectRepository<classNameOfPageObjectRepositryImplementingThisInterface>` which force us to implement some of the very useful methods

> [!NOTE]
> For more information, please explore clases in below two packages
> * org.seleniumbrain.lab.core.selenium.pageobjectmodel
> * org.seleniumbrain.lab.snippet


**Example**

``` java
@Slf4J
@Data
@PageObjects
public class WillisQuoteSubmissionpageOR extends BaseObjectRepository implements PageObjectRepository<WillisQuoteSubmissionpageOR> {

    @FindBy (xpath = "//p[@id='abcd']")
    private WebElement brokeName();

}

```

> [!IMPORTANT]
> Always create page object repository classes in any package under `src/main/java`. However, it is recommended to use `snippet` package under `src/main/java` for creating any Object Repositories for maintanability.

### 8. Utilities

Framework provides some utilities that makes testers life easier. Some of the urilities are as follows

#### 1. CSV handler

It provides few resuable methods to handle csv files.

#### 2. Date handler

It provides few resuable methods to handle data string values.

#### 3. JSON handler

It provides more rich methods to handle more challenging requirement on json.

4. File Utils
5. Retry Mechanism
6. Numeric Text Comparator
