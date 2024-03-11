package org.seleniumbrain.lab.core.api;

import io.cucumber.spring.ScenarioScope;
import io.restassured.RestAssured;
import io.restassured.config.LogConfig;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.io.output.WriterOutputStream;
import org.seleniumbrain.lab.utility.PathBuilder;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.FileWriter;
import java.io.PrintStream;
import java.nio.charset.StandardCharsets;
import java.util.*;

@Slf4j
@Component
@ScenarioScope
public class RestAssuredTestTemplate {

    private File getApiLogFile() {
        return new File(PathBuilder.getApiLogOutputFolder() + "log.txt");
    }

    public synchronized Response getMethodForResponse(String url,
                                                      Map<String, String> headers,
                                                      Map<String, Object> queryParams) {
        Response response = null;
        final RequestSpecification requestSpecification;

        try(FileWriter fileWriter = new FileWriter(getApiLogFile());
            PrintStream printStream = new PrintStream(new WriterOutputStream(fileWriter, StandardCharsets.UTF_8), true)) {
            RestAssured.config = RestAssured.config().logConfig(LogConfig.logConfig().defaultStream(printStream));
            requestSpecification = RestAssured.given()
                    .headers(headers)
                    .queryParams(queryParams)
                    .log().all().relaxedHTTPSValidation();
            response = requestSpecification.get(url);
        } catch (Exception e) {
            log.info("Exception at Rest-Template GET API Call. ", e);
        }
        return response;
    }

    public synchronized Response getMethodWithPayloadForResponse(String url,
                                                                 Map<String, String> headers,
                                                                 String requestPayload) {
        Response response = null;
        final RequestSpecification requestSpecification;

        try(FileWriter fileWriter = new FileWriter(getApiLogFile());
            PrintStream printStream = new PrintStream(new WriterOutputStream(fileWriter, StandardCharsets.UTF_8), true)) {
            RestAssured.config = RestAssured.config().logConfig(LogConfig.logConfig().defaultStream(printStream));
            requestSpecification = RestAssured.given()
                    .headers(headers)
                    .queryParams(new HashMap<>())
                    .body(requestPayload)
                    .log().all().relaxedHTTPSValidation();
            response = requestSpecification.get(url);
        } catch (Exception e) {
            log.info("Exception at Rest-Template GET With Payload API Call. ", e);
        }
        return response;
    }

    public synchronized Response postMethodForResponse(String url,
                                                       Map<String, String> headers,
                                                       String requestPayload) {
        Response response = null;
        final RequestSpecification requestSpecification;

        try(FileWriter fileWriter = new FileWriter(getApiLogFile());
            PrintStream printStream = new PrintStream(new WriterOutputStream(fileWriter, StandardCharsets.UTF_8), true)) {
            RestAssured.config = RestAssured.config().logConfig(LogConfig.logConfig().defaultStream(printStream));
            requestSpecification = RestAssured.given()
                    .headers(headers)
                    .queryParams(new HashMap<>())
                    .body(requestPayload)
                    .log().all().relaxedHTTPSValidation();
            response = requestSpecification.post(url);
        } catch (Exception e) {
            log.info("Exception at Rest-Template POST API Call. ", e);
        }
        return response;
    }

    public synchronized Response postMethodWithMultiPartFilesUploadForResponse(String url,
                                                                               Map<String, String> headers,
                                                                               Map<String, String> queryParams,
                                                                               Map<String, String> formParams,
                                                                               List<String> filesPath,
                                                                               String controlName) {
        Response response = null;
        final RequestSpecification requestSpecification;
        final String defaultFileControlName = "file";

        try(FileWriter fileWriter = new FileWriter(getApiLogFile());
            PrintStream printStream = new PrintStream(new WriterOutputStream(fileWriter, StandardCharsets.UTF_8), true)) {

            String fileControlName = Objects.nonNull(controlName) && !controlName.isBlank() ? controlName : defaultFileControlName;
            RestAssured.config = RestAssured.config().logConfig(LogConfig.logConfig().defaultStream(printStream));
            requestSpecification = RestAssured.given()
                    .headers(headers)
                    .queryParams(queryParams)
                    .formParams(formParams)
                    .log().all().relaxedHTTPSValidation();

            filesPath.forEach(path -> {
                String contentType = Arrays.stream(MimeTypes.values())
                        .filter(mime -> FilenameUtils.getExtension(path).equalsIgnoreCase(mime.toString()))
                        .findFirst().orElse(MimeTypes.JSON)
                        .type();
                requestSpecification.multiPart(
                        contentType,
                        new File(path),
                        contentType
                );
            });

            response = requestSpecification.post(url);
        } catch (Exception e) {
            log.info("Exception at Rest-Template MultiPart-POST API Call. ", e);
        }
        return response;
    }

    public synchronized Response putMethodForResponse(String url,
                                                      Map<String, String> headers,
                                                      String requestPayload) {
        Response response = null;
        final RequestSpecification requestSpecification;

        try(FileWriter fileWriter = new FileWriter(getApiLogFile());
            PrintStream printStream = new PrintStream(new WriterOutputStream(fileWriter, StandardCharsets.UTF_8), true)) {
            RestAssured.config = RestAssured.config().logConfig(LogConfig.logConfig().defaultStream(printStream));
            requestSpecification = RestAssured.given()
                    .headers(headers)
                    .queryParams(new HashMap<>())
                    .body(requestPayload)
                    .log().all().relaxedHTTPSValidation();
            response = requestSpecification.put(url);
        } catch (Exception e) {
            log.info("Exception at Rest-Template PUT API Call. ", e);
        }
        return response;
    }

}
