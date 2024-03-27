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