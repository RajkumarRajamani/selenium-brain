package org.seleniumbrain.lab.api.restassured;

import io.restassured.RestAssured;
import io.restassured.config.LogConfig;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import org.apache.commons.io.output.WriterOutputStream;
import org.seleniumbrain.lab.config.SeleniumConfigReader;
import org.seleniumbrain.lab.utility.PathBuilder;

import java.io.File;
import java.io.FileWriter;
import java.io.PrintStream;
import java.nio.charset.StandardCharsets;
import java.util.Map;

public class RestAssuredTestTemplate {

    private File getApiLogFile() {
        return new File(PathBuilder.getApiLogOutputFolder() + "log.txt");
    }

    public synchronized Response getMethodForResponse(String url, Map<String, String> headers, Map<String, Object> queryParams) {
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

        }
        return response;
    }

}
