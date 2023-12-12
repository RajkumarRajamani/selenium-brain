package org.seleniumbrain.lab.utility;

import com.opencsv.bean.CsvToBeanBuilder;
import lombok.Builder;
import lombok.Data;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Stream;

/**
 * <a href="https://opencsv.sourceforge.net/">refer OpenCsv documentation</a>
 */
public class CSVUtility {

    public static List<?> readCsv(String fileName, Class<?> type) throws FileNotFoundException {
        return new CsvToBeanBuilder<>(new FileReader(fileName))
                .withType(type)
                .withQuoteChar('"')
                .withSeparator(',')
                .withIgnoreEmptyLine(true)
                .withOrderedResults(true)
                .build().parse();
    }

    public static List<?> readCsv(char delimiter, String fileName, Class<?> type) throws FileNotFoundException {
        return new CsvToBeanBuilder<>(new FileReader(fileName))
                .withType(type)
                .withSeparator(delimiter)
                .build().parse();
    }



    public static void main(String[] args) throws Exception {

        String fileName = "src/main/resources/monitor.csv";
        List<String[]> result = CSVUtility.readCsv(fileName, 1);

        int listIndex = 0;
        for (String[] arrays : result) {
            System.out.println("\nString[" + listIndex++ + "] : " + Arrays.toString(arrays));

            int index = 0;
            for (String array : arrays) {
                System.out.println(index++ + " : " + array);
            }
        }

        CSVUtility.readCsvToRecords(fileName).forEach(System.out::println);

        System.out.println();
        System.out.println();
        CSVUtility.readCsv(fileName, Model.class).forEach(System.out::println);
    }

    public static List<String[]> readCsv(String fileName) throws Exception {
        return new CsvSimpleParser().readFile(Paths.get(fileName).toFile(), 0);
    }

    public static List<String[]> readCsv(String fileName, int skipLinesUntil) throws Exception {
        return new CsvSimpleParser().readFile(Paths.get(fileName).toFile(), skipLinesUntil);
    }

    public static List<CsvRecord> readCsvToRecords(String fileName) throws Exception {
        List<String[]> listOfCsvStringArray = new CsvSimpleParser().readFile(Paths.get(fileName).toFile(), 0);
        return convertToCsvRecords(listOfCsvStringArray);
    }

    public static List<String[]> readCsv(char delimiter, String fileName) throws Exception {
        return new CsvSimpleParser(delimiter).readFile(Paths.get(fileName).toFile(), 0);
    }

    public static List<String[]> readCsv(char delimiter, String fileName, int skipLinesUntil) throws Exception {
        return new CsvSimpleParser(delimiter).readFile(Paths.get(fileName).toFile(), skipLinesUntil);
    }

    public static List<CsvRecord> readCsvToRecords(char delimiter, String fileName) throws Exception {
        List<String[]> listOfCsvStringArray = new CsvSimpleParser(delimiter).readFile(Paths.get(fileName).toFile(), 0);
        return convertToCsvRecords(listOfCsvStringArray);
    }

    private static List<CsvRecord> convertToCsvRecords(List<String[]> listOfCsvStringArray) {
        List<CsvRecord> csvRecords = new LinkedList<>();
        int listIndex = 0;
        for (String[] array : listOfCsvStringArray) {
            if (listIndex > 0) {
                int arrayIndex = 0;
                for (String token : array) {
                    CsvRecord record = CsvRecord.builder()
                            .position(arrayIndex)
                            .columnName(listOfCsvStringArray.get(0)[arrayIndex])
                            .value(token).build();
                    csvRecords.add(record);
                    arrayIndex++;
                }
            }
            listIndex++;
        }
        return csvRecords;
    }

    @Data
    @Builder
    public static class CsvRecord {
        private int position;
        private String columnName;
        private String value;
    }

    public static class CsvSimpleParser {

        private static char DEFAULT_SEPARATOR = ',';
        private static final char DOUBLE_QUOTES = '"';
        private static final char DEFAULT_QUOTE_CHAR = DOUBLE_QUOTES;
        private static final String NEW_LINE = "\n";

        private boolean isMultiLine = false;
        private String pendingField = "";
        private String[] pendingFieldLine = new String[]{};

        public CsvSimpleParser() {
        }

        public CsvSimpleParser(char delimiter) {
            DEFAULT_SEPARATOR = delimiter;
        }

        public List<String[]> readFile(File csvFile) throws Exception {
            return readFile(csvFile, 0);
        }

        public List<String[]> readFile(File csvFile, int skipLine)
                throws Exception {

            List<String[]> result = new ArrayList<>();
            int indexLine = 1;

            try (BufferedReader br = new BufferedReader(new FileReader(csvFile))) {

                String line;
                while ((line = br.readLine()) != null) {

                    if (indexLine++ <= skipLine) {
                        continue;
                    }

                    String[] csvLineInArray = parseLine(line);

                    if (isMultiLine) {
                        pendingFieldLine = joinArrays(pendingFieldLine, csvLineInArray);
                    } else {
                        if (pendingFieldLine != null && pendingFieldLine.length > 0) {
                            // joins all fields and add to list
                            result.add(joinArrays(pendingFieldLine, csvLineInArray));
                            pendingFieldLine = new String[]{};
                        } else {
                            // if dun want to support multiline, only this line is required.
                            result.add(csvLineInArray);
                        }
                    }
                }
            }

            return result;
        }

        public String[] parseLine(String line) throws Exception {
            return parseLine(line, DEFAULT_SEPARATOR);
        }

        public String[] parseLine(String line, char separator) throws Exception {
            return parse(line, separator, DEFAULT_QUOTE_CHAR).toArray(String[]::new);
        }

        private List<String> parse(String line, char separator, char quoteChar)
                throws Exception {

            List<String> result = new ArrayList<>();

            boolean inQuotes = false;
            boolean isFieldWithEmbeddedDoubleQuotes = false;

            StringBuilder field = new StringBuilder();

            for (char c : line.toCharArray()) {

                if (c == DOUBLE_QUOTES) {               // handle embedded double quotes ""
                    if (isFieldWithEmbeddedDoubleQuotes) {

                        if (!field.isEmpty()) {       // handle for empty field like "",""
                            field.append(DOUBLE_QUOTES);
                            isFieldWithEmbeddedDoubleQuotes = false;
                        }

                    } else {
                        isFieldWithEmbeddedDoubleQuotes = true;
                    }
                } else {
                    isFieldWithEmbeddedDoubleQuotes = false;
                }

                if (isMultiLine) {                      // multiline, add pending from the previous field
                    field.append(pendingField).append(NEW_LINE);
                    pendingField = "";
                    inQuotes = true;
                    isMultiLine = false;
                }

                if (c == quoteChar) {
                    inQuotes = !inQuotes;
                } else {
                    if (c == separator && !inQuotes) {  // if find separator and not in quotes, add field to the list
                        result.add(field.toString());
                        field.setLength(0);             // empty the field and ready for the next
                    } else {
                        field.append(c);                // else append the char into a field
                    }
                }

            }

            //line done, what to do next?
            if (inQuotes) {
                pendingField = field.toString();        // multiline
                isMultiLine = true;
            } else {
                result.add(field.toString());           // this is the last field
            }

            return result;

        }

        private String[] joinArrays(String[] array1, String[] array2) {
            return Stream.concat(Arrays.stream(array1), Arrays.stream(array2))
                    .toArray(String[]::new);
        }
    }
}
