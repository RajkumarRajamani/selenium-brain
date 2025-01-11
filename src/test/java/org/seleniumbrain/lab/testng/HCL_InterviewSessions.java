package org.seleniumbrain.lab.testng;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;

public class HCL_InterviewSessions {

    public static void main(String[] args) {
        List<Integer> list = Arrays.asList(10, 2, 4, 7, 5, 14, 11, 10, 1, 3);
        int targetSum = 28;

        List<List<Integer>> finalList = extractSubList_thatSumsTo(targetSum, list);

        finalList.forEach(System.out::println);
    }

    /**
     * From a given list of integers, find all possible sublists that sum up to the given target number.
     * <pre>
     *     For Example, extract all sublists from below original list that sums value equal to 13.
     *     {@code
     *     List<Integer> list = Arrays.asList(10, 2, 4, 7, 5, 14, 11, 10, 1, 3);
     *     int targetSum = 13;
     *     }
     *
     *     This method will return {@code [[1, 2, 10], [2, 4, 7], [2, 11], [3, 10]]}
     * </pre>
     *
     * @param targetValue  target value that requires n number of objects to be summed from a given list
     * @param originalList original list of integers where the sub list is extracted from
     * @return possible lists of objects
     */
    public static List<List<Integer>> extractSubList_thatSumsTo(int targetValue, List<Integer> originalList) {
        // to store all sublist that sums the target value
        List<List<Integer>> subLists = new ArrayList<>();

        for (int i = 0; i < originalList.size(); i++) {
            List<Integer> subList = new ArrayList<>();
            subList.add(originalList.get(i));

            for (int j = 0; j < originalList.size(); j++) {
                if (j != i) { // do not consider the same element.
                    int sum = subList.stream().reduce(0, Integer::sum) + originalList.get(j);
                    if (sum <= targetValue) {
                        subList.add(originalList.get(j));
                    }
                }
            }

            // check if the sublist with the same group of objects is already found. if yes, do not repeat it.
            // maintain a unique list
            if (subList.stream().reduce(0, Integer::sum) == targetValue) {
                subList.sort(Comparator.naturalOrder());
                if (subLists.stream()
                        .peek(lst -> lst.sort(Comparator.naturalOrder()))
                        .noneMatch(lst -> lst.equals(subList))) {
                    subLists.add(subList);
                }
            }
        }
        return subLists;
    }

}
