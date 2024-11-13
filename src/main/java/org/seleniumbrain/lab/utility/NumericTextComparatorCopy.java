package org.seleniumbrain.lab.utility;

public class NumericTextComparatorCopy {

    /**
     * Compares one version string to another version string by dotted ordinals.
     * eg. "1.0" > "0.09" ; "0.9.5" < "0.10",
     * also "1.0" < "1.0.0" but "1.0" == "01.00"
     *
     * @param left  the left hand version string
     * @param right the right hand version string
     * @return 0 if equal, -1 if thisVersion &lt; comparedVersion and 1 otherwise.
     */
    public static int compare(String left, String right) {
        if (left.equals(right)) {
            return 0;
        }
        String regEx = "(\\.0+|(\\.\\d*?[1-9])0+)$";
        int leftStart = 0, rightStart = 0, result;
        do {
            int leftEnd = left.indexOf('.', leftStart);
            int rightEnd = right.indexOf('.', rightStart);

            // left
            String leftTxt = leftEnd < 0
                    ? left.substring(leftStart)
                    : left.substring(leftStart, leftEnd);
            if(left.endsWith("." + leftTxt))
                leftTxt = leftTxt.replaceAll("0+$", "");
            Integer leftValue = Integer.parseInt(leftTxt);

            // right
            String rightTxt = rightEnd < 0
                    ? right.substring(rightStart)
                    : right.substring(rightStart, rightEnd);
            if(right.endsWith("." + rightTxt))
                rightTxt = rightTxt.replaceAll("0+$", "");
            Integer rightValue = Integer.parseInt(rightTxt);


            result = leftValue.compareTo(rightValue);
            leftStart = leftEnd + 1;
            rightStart = rightEnd + 1;
        } while (result == 0 && leftStart > 0 && rightStart > 0);
        if (result == 0) {
            if (leftStart > rightStart) {
                return containsNonZeroValue(left, leftStart) ? 1 : 0;
            }
            if (leftStart < rightStart) {
                return containsNonZeroValue(right, rightStart) ? -1 : 0;
            }
        }
        return result;
    }

    private static boolean containsNonZeroValue(String str, int beginIndex) {
        for (int i = beginIndex; i < str.length(); i++) {
            char c = str.charAt(i);
            if (c != '0' && c != '.') {
                return true;
            }
        }
        return false;
    }

    public static void main(String[] args) {
        System.out.println(NumericTextComparatorCopy.compare("1", "1"));
        System.out.println(NumericTextComparatorCopy.compare("1.10", "1.01"));
        System.out.println(NumericTextComparatorCopy.compare("1.40.40", "1.40.40"));
        System.out.println(NumericTextComparatorCopy.compare("1.0", "1"));
        System.out.println(NumericTextComparatorCopy.compare("134", "1345"));
        System.out.println(NumericTextComparatorCopy.compare("8.545", "8.5450"));
        System.out.println(NumericTextComparator.compare("8.09.00", "8.9"));
    }
}