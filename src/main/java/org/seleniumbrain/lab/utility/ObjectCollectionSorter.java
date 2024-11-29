package org.seleniumbrain.lab.utility;

import java.lang.reflect.*;
import java.util.*;

public class ObjectCollectionSorter {

    public static void sortCollections(Object obj) throws IllegalAccessException {
        if (obj == null) return;

        Class<?> clazz = obj.getClass();
        Field[] fields = clazz.getDeclaredFields();

        for (Field field : fields) {
            field.setAccessible(true); // Make private fields accessible

            Object value = field.get(obj);

            if (value == null) continue;

            // Check for collection types
            if (value instanceof List<?>) {
                @SuppressWarnings("unchecked")
                List<Object> list = (List<Object>) value;
                list.sort(null); // Sort using natural order (ensure elements implement Comparable)
            } else if (value instanceof Set<?>) {
                @SuppressWarnings("unchecked")
                Set<Object> set = (Set<Object>) value;
                List<Object> sortedList = new ArrayList<>(set);
                sortedList.sort(null); // Sort using natural order
                set.clear();
                set.addAll(sortedList);
            } else if (value instanceof Map<?, ?>) {
                @SuppressWarnings("unchecked")
                Map<Object, Object> map = (Map<Object, Object>) value;
                List<Map.Entry<Object, Object>> entryList = new ArrayList<>(map.entrySet());
//                entryList.sort(Map.Entry.comparingByKey()); // Sort by key (or modify to sort by value)
                map.clear();
                for (Map.Entry<Object, Object> entry : entryList) {
                    map.put(entry.getKey(), entry.getValue());
                }
            } else if (!field.getType().isPrimitive() && !field.getType().isEnum()) {
                // If it's a custom object, recurse into its fields
                sortCollections(value);
            }
        }
    }

    public static void main(String[] args) throws IllegalAccessException {
        // Sample Usage
        NestedObject nested = new NestedObject();
        ParentObject parent = new ParentObject(nested);

        System.out.println("Before Sorting:");
        System.out.println(parent);

        sortCollections(parent);

        System.out.println("\nAfter Sorting:");
        System.out.println(parent);
    }
}

// Example Parent and Nested Objects
class ParentObject {
    List<String> strings = Arrays.asList("banana", "apple", "cherry");
    Set<Integer> numbers = new HashSet<>(Arrays.asList(3, 1, 2));
    Map<String, Integer> map = new HashMap<>();
    NestedObject nestedObject;

    public ParentObject(NestedObject nestedObject) {
        this.nestedObject = nestedObject;
        map.put("three", 3);
        map.put("one", 1);
        map.put("two", 2);
    }

    @Override
    public String toString() {
        return "ParentObject{" +
                "strings=" + strings +
                ", numbers=" + numbers +
                ", map=" + map +
                ", nestedObject=" + nestedObject +
                '}';
    }
}

class NestedObject {
    List<Double> decimals = Arrays.asList(3.3, 1.1, 2.2);

    @Override
    public String toString() {
        return "NestedObject{" +
                "decimals=" + decimals +
                '}';
    }
}
