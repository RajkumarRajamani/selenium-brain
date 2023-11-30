package org.seleniumbrain.lab.easyreport.assertions;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.seleniumbrain.lab.easyreport.exception.EasyReportException;
import org.seleniumbrain.lab.easyreport.pojo.Failures;
import org.seleniumbrain.lab.easyreport.pojo.KnownFailures;

import java.io.File;
import java.io.FileInputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Slf4j
public class KnownFailuresReader {

    private static Map<String, String> knownFailureMap = new HashMap<>();

    public static void main(String[] args) {
        Map<String, String> map = readKnownFailures();
        System.out.println(map);
    }

    @SneakyThrows
    public static Map<String, String> readKnownFailures() {
        if(knownFailureMap.isEmpty()) {
            String fileNameToFind = "known-failures.yml";
            String rootDirectoryPath = String.join(System.getProperty("file.separator"), System.getProperty("user.dir"), "src", "");
            File rootDirectory = new File(rootDirectoryPath);


            try(Stream<Path> walkStream = Files.walk(rootDirectory.toPath())) {
                walkStream.filter(p -> p.toFile().isFile())
                        .filter(f -> f.toString().endsWith(fileNameToFind))
                        .findFirst()
                        .ifPresentOrElse(KnownFailuresReader::read, KnownFailuresReader::exceptionLog);
            }
        }
        return knownFailureMap;
    }

    private static void exceptionLog() {
        log.error("Unable to find known-failure.yaml file in any folder under src. You can create a new file 'known-failures.yml' anywhere under src with below template,\n" +
                "knownFailures:\n" +
                "  - label: apple\n" +
                "    trackingId: 1234\n" +
                "    description: new known failure");
    }

    private static void read(Path file) {
        try {
            ObjectMapper mapper = new ObjectMapper(new YAMLFactory());
            mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
            Failures failures = mapper.readValue(new FileInputStream(file.toFile()), Failures.class);
            System.out.println(failures);
            knownFailureMap = failures.getKnownFailures().stream().collect(Collectors.toMap(KnownFailures::getLabel, KnownFailures::getTrackingId));
        } catch (Exception e) {
            throw new EasyReportException("Exception while reading Known Failures list from yaml file - " + file.toAbsolutePath().toString(), e);
        }
    }
}
