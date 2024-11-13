package org.seleniumbrain.lab.utility.polling;

import org.apache.commons.lang3.StringUtils;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;

public class VersionComparator {

    public static boolean compareVersions(String version1, String version2) {
        List<String> version1Parts = Arrays.asList(version1.split("\\."));
        List<String> version2Parts = Arrays.asList(version2.split("\\."));

        int minLength = Math.min(version1Parts.size(), version2Parts.size());

        for (int i = 0; i < minLength; i++) {
            String v1Part = version1Parts.get(i).replaceAll("0+$", "");
            String v2Part = version2Parts.get(i).replaceAll("0+$", "");

            int v1Int = v1Part.isBlank() ? 0 : Integer.parseInt(v1Part);
            int v2Int = v2Part.isBlank() ? 0 : Integer.parseInt(v2Part);

            if (v1Int != v2Int) {
                return false;
            }
        }

        // If one version has more parts, it's considered greater
        return version1Parts.size() == version2Parts.size();
    }

    public static void main(String[] args) {
        String[] testCases = {
//                "8.9.1", "8.10.1",
//                "null", "null",
//                "23", "",
//                "", "",
//                "", "45",
//                "0.09.5", "0.10.1",
//                "1", "1", // it is same
//                "001.0", "1.0", // it is same
//                "0010.0", "1.0", // it is not same
//                "1.1.2.3", "1.1",
//                "1.3.2", "1.3.54.2",
//                "1.0", "1",
//                "1.000", "1.0",
//                "1.0.0.1", "1.0.0.1",
                "1.01.1.1", "1.01.01.1",
//                "1.0.1", "1.0",
//                "1.0.1", "1.0.0",
//                "5.324", "5.3240",
//                "5.00324", "5.003240",
//                "5.0324", "5.03240",
//                "50.0324", "500.03240",
//                "500.0324", "50.03240"
        };

        for (int i = 0; i < testCases.length; i += 2) {
            String version1 = testCases[i];
            String version2 = testCases[i + 1];

            int result = compare(version1, version2);
            System.out.println(version1 + " equals " + version2 + ": " + result);
//            System.out.println(compare(version1, version2));
        }

//        System.out.println(compareVal("00126000", "001260", false));
    }

    /**
     * <pre>It compares two numbers in text format. The numbers can be of two types,
     * 1. Ordinary Numbers [with or without single decimal point like 1, 1.0, 34598.0029]
     * 2. Version Numbers [with multiple "." separator like 1.0.2]
     *
     * Logics: with an example - LeftString ["0010.9.89.03"] and ReftString ["10.9.89.030"]
     * 1. It splits the given numbers [left and right value] with "." and converts it into an array of number text
     * 2. First index value will always be whole number. It considers trailing zeros, not prefix zeros. It is compared as pure long value
     *      i.e., 0010 and 10 will be same.
     * 3. After first index and before last index element, all intermediate elements are compared against each char
     * 4. For Last index element, prefix zeros are considered but trailing zeros are ignored
     * </pre>
     *
     * @param leftString
     * @param rightString
     * @return
     */
    public static int compare(String leftString, String rightString) {

        leftString = Objects.nonNull(leftString) ? leftString : "";
        rightString = Objects.nonNull(rightString) ? rightString : "";

        if (leftString.equals(rightString)) return 0;

        if (!leftString.isBlank() && rightString.isBlank()) return 1;
        if (leftString.isBlank() && !rightString.isBlank()) return -1;

        String[] leftStringArr = leftString.split("\\.");
        int leftStringArrLen = leftStringArr.length;

        String[] rightStringArr = rightString.split("\\.");
        int rightStringArrLen = rightStringArr.length;

        int arrLengthDiff = Math.abs(leftStringArrLen - rightStringArrLen);

        if (arrLengthDiff == 0) {
            int result1 = 0;
            for (int i = 0; i <= leftStringArrLen - 1; i++) { // can use rightStringArrLen also here coz both the length are equal
                if (i == 0) {
                    long value_atIndex_ofLeftArray = Long.parseLong(leftStringArr[0]);
                    long value_atIndex_ofRightArray = Long.parseLong(rightStringArr[0]);
                    if (value_atIndex_ofLeftArray != value_atIndex_ofRightArray) {
                        result1 = Long.compare(value_atIndex_ofLeftArray, value_atIndex_ofRightArray);
                        break;
                    }
                }

                if (i > 0 && i <= leftStringArrLen - 2) { // after first element and before last element in an array
                    result1 = deepCompare(leftStringArr[i], rightStringArr[i], false);
                    if (result1 != 0)
                        break;
                }

                if (i == leftStringArrLen - 1) {
                    result1 = deepCompare(leftStringArr[i], rightStringArr[i], false);
                }
            }
            return result1;
        } else if (arrLengthDiff == 1) {
            int result2 = 0;
            int loopCount = Math.min(leftStringArrLen, rightStringArrLen);

            for (int i = 0; i <= loopCount - 1; i++) {
                if (i == 0) {
                    long value_atIndex_ofLeftArray = Long.parseLong(leftStringArr[0]);
                    long value_atIndex_ofRightArray = Long.parseLong(rightStringArr[0]);
                    if (value_atIndex_ofLeftArray != value_atIndex_ofRightArray) {
                        result2 = Long.compare(value_atIndex_ofLeftArray, value_atIndex_ofRightArray);
                        break;
                    }
                } else {
                    result2 = deepCompare(leftStringArr[i], rightStringArr[i], true);
                    if (result2 != 0)
                        break;
                }
            }

            if (result2 == 0) {
                if (leftStringArrLen > rightStringArrLen) {
                    long value_atLastIndex_ofLeftArray = Long.parseLong(leftStringArr[leftStringArrLen - 1]);
                    result2 = value_atLastIndex_ofLeftArray > 0 ? 1 : value_atLastIndex_ofLeftArray == 0 ? 0 : -1;
                }

                if (rightStringArrLen > leftStringArrLen) {
                    long value_atLastIndex_ofRightArray = Long.parseLong(rightStringArr[rightStringArrLen - 1]);
                    result2 = value_atLastIndex_ofRightArray > 0 ? -1 : value_atLastIndex_ofRightArray == 0 ? 0 : 1;
                }
            }

            return result2;
        } else {
            if (leftStringArrLen > rightStringArrLen) return 1;
            else if (rightStringArrLen > leftStringArrLen) return -1;
            else return 0;
        }
    }

    private static int deepCompare(String leftString, String rightString, boolean exactMatch) {
        int leftStringLen = leftString.length();
        int rightStringLen = rightString.length();

        int minLen = Math.min(leftStringLen, rightStringLen);

        int result = 0;
        for (int i = 0; i < minLen; i++) {
            int leftStrCharVal_atIndex = Character.getNumericValue(leftString.toCharArray()[i]);
            int rightStrCharVal_atIndex = Character.getNumericValue(rightString.toCharArray()[i]);

            if (leftStrCharVal_atIndex != rightStrCharVal_atIndex) {
                result = Integer.compare(leftStrCharVal_atIndex, rightStrCharVal_atIndex);
                break;
            }
        }

        if (leftStringLen == rightStringLen)
            return result;

        if (result == 0) {
            if (leftStringLen > rightStringLen) {
                if (exactMatch) {
                    return 1;
                } else {
                    boolean isLeftGreater = leftString.substring(minLen).chars()
                            .map(Character::getNumericValue)
                            .anyMatch(num -> num > 0);
                    return isLeftGreater ? 1 : 0;
                }
            } else {
                if (exactMatch) {
                    return -1;
                } else {
                    boolean isRightGreater = rightString.substring(minLen).chars()
                            .map(Character::getNumericValue)
                            .anyMatch(num -> num > 0);
                    return isRightGreater ? -1 : 0;
                }
            }
        } else {
            return result;
        }

        //
    }
}