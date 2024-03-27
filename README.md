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
6. #### Page Object Repository Setup
    1. Base Object Repository
    2. Usage
7. #### Utilities
    1. CSV handler
    2. Date handler
    3. JSON handler
    4. File Utils
    5. Retry Mechanism
    6. Numeric Text Comparator
8. #### Reports [Easy Cucumber Report]
9. #### Test Script Definitions
10. #### Quick Start

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
> Ensure that you have installed JDK and your favorite IDE [IntelliJ/VS Code/Eclipse] on your system before following above steps
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

   1. Create a class with any name of your desire in any package under `src/main/java` and annotate it with Spring's annotations `@SpringBootApplication` and `@ComponentScan`. 
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
   
   2. Create another class with any name of your desire in any package under `src/main/java` [usually under same package as followed in above step]
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
         `SeleniumBrainCucumberSpringConfiguration.class` and `SeleniumBrainCucumberTestExecutionEngine.class` to scan through for beans.
   
   By following the above two steps, while initiating cucumber execution from runner file, will first read the above configurations
   and run it like a spring project execution enabling all Spring Features internally.

   #### c. Configuration Readers

   We are maintaining two types of configurations
   
   - SeleniumConfiguration
   - AUT Configurations

   #### 1. Selenium Configurations
   
   It contains configurations related to the framework and selenium tool and is maintained at `src/main/resources/configs/selenium-config.yml`
   
   Below are the types of configurations being maintained in the yml file.
   
   1. Test Lab [LocalLab/Grid/SauceLab/Perfecto/Docker]

      > [!IMPORTANT]
      > Currently only **LocalLab** is supported. Other Lab options are under Development.

   2. Every Lab contains all possible configurations for browsers
   3. Selenium Timeouts
   4. Test Environment Name [on which we run execution] : Testers are requested to mention name of the application configuration yaml file name. Usually, it is recommended 
      to name application configuration file name as `dev.yml` or `qa.yml` or `uat.yml`
   5. Output Folders: If not provided, the framework will choose a default path as defined in `org.seleniumbrain.lab.core.config.SeleniumConfigReader.java`

   `selenium-config.yml` file is mapped to its equivalent java object class `SeleniumConfigurations.java` using `ObjectMapper.java` class from a jackson library.
   
   **Usages**
   
You can access selenium configuration details present in `selenium-config.yml` file, from anywhere in the project, by directly using `SeleniumConfigReader.java` class name itself.
``` java
SeleniumConfigReader.getConfigs();
```

#### 2. AUT Configurations

It contains configuration related to the application Under Test [AUT] and is maintained at `src/main/resources/configs/aut-configs`

It is recommended to maintain the name of the application-related configuration yaml file as the environment name itself. For Example,
to maintain application configuration for a dev region, create a file `dev.yml` and maintain all configurations related to AUT.
Similarly, for other environments like QA or UAT, it could be `qa.yml` and `uat.yml` respectively.

However, it is not mandated that the config file naming should be as above. You can maintain any name as you desire.

Point is -- whatever the name that we provide for the application configuration yaml file, the same name should be given in `selenium-config.yml` file
for property `environment` without extension. For Example,
``` yaml
test:
  app:
    environment: dev
```

Framework, when started, will start looking for file under `src/main/resources/configs/aut-configs` package with the extension 'yml' and read it and map it to
`org.seleniumbrain.lab.core.config.pojo.AUTConfiguration`

**Usage**

You can access application specific configurations details present in `<environment-name>.yml` file, from anywhere in the project, by directly using
`AUTConfigReader.java` class
``` java
AUTConfigReader.get();
```

---

### 5. Selenium WebDriver Factory

