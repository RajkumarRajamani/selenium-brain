package org.seleniumbrain.lab.core.api;

import io.cucumber.spring.ScenarioScope;
import org.seleniumbrain.lab.core.exception.ApiException;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Map;

@Component
@ScenarioScope
public class SpringRestTestTemplate {

    private final TestRestTemplate restTemplate = new TestRestTemplate();

    public ResponseEntity<String> postMethodForResponse(String url, String data, HttpHeaders headers) {
        try {
            HttpEntity<String> entity = new HttpEntity<>(data, headers);
            return restTemplate.exchange(url, HttpMethod.POST, entity, String.class);
        } catch(Exception e) {
            throw new ApiException("Exception at Spring-Template POST API Call.", e);
        }
    }

    public ResponseEntity<String> postMethodWithFormDataForResponse(String url, MultiValueMap<String, Object> multiValueMap, HttpHeaders headers) {
        try {
            HttpEntity<MultiValueMap<String, Object>> entity = new HttpEntity<>(multiValueMap, headers);
            return restTemplate.exchange(url, HttpMethod.POST, entity, String.class);
        } catch(Exception e) {
            throw new ApiException("Exception at Spring-Template POST API Call with FormData.", e);
        }
    }

    public ResponseEntity<String> getMethodWithRequestPayloadForResponse(String url, String data, HttpHeaders headers) {
        try {
            HttpEntity<String> entity = new HttpEntity<>(data, headers);
            return restTemplate.exchange(url, HttpMethod.GET, entity, String.class);
        } catch(Exception e) {
            throw new ApiException("Exception at Spring-Template GET API Call with Request Payload.", e);
        }
    }

    public ResponseEntity<String> getMethodWithQueryParameterForResponse(String url, Map<String, String> queryParams, HttpHeaders headers) {
        try {
            HttpEntity<String> entity = new HttpEntity<>(headers);
            return restTemplate.exchange(url, HttpMethod.GET, entity, String.class, queryParams);
        } catch(Exception e) {
            throw new ApiException("Exception at Spring-Template GET API Call with QueryParams.", e);
        }
    }

    public ResponseEntity<String> getMethodForResponse(String url, HttpHeaders headers) {
        try {
            HttpEntity<String> entity = new HttpEntity<>(headers);
            return restTemplate.exchange(url, HttpMethod.GET, entity, String.class);
        } catch(Exception e) {
            throw new ApiException("Exception at Spring-Template POST API Call", e);
        }
    }

    public HttpEntity<byte[]> getFileInByteArray(String filePath, String fileName) throws IOException {
        String contentType = "";
        MultipartFile file = getMultiPartFile(filePath, fileName);
        LinkedMultiValueMap<String, String> headerMap = new LinkedMultiValueMap<>();
        headerMap.add("Content-disposition", "form-data; name=file; fileName=" + file.getOriginalFilename());
        headerMap.add("Content-type", contentType);
        HttpEntity<byte[]> doc = new HttpEntity<>(file.getBytes(), headerMap);
        return doc;
    }

    private MultipartFile getMultiPartFile(String filePath, String fileName) {
        File file = new File(filePath);
        String contentType = "";

        byte[] content = null;
        try(FileInputStream fis = new FileInputStream(file)) {
            content = fis.readAllBytes();
        } catch(Exception e) {
            // swallows exception
        }
        return new MockMultipartFile(fileName, fileName, contentType, content);
    }
}
