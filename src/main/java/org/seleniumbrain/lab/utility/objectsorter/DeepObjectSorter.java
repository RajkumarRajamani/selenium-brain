package org.seleniumbrain.lab.utility.objectsorter;

import lombok.SneakyThrows;
import org.seleniumbrain.lab.utility.json.core.JsonBuilder;

import java.lang.reflect.Field;
import java.util.*;
import java.util.stream.Collectors;

public class DeepObjectSorter {


    public static void main(String[] args) throws IllegalAccessException {

        DeepObjectSorter sorter = new DeepObjectSorter();

//        List<String> list = new ArrayList<>() {
//            {
//                add("Rajkumar");
//                add("Deepika");
//                add("Jayagandhi");
//                add("Rajamani");
//                add("Mukilan");
//                add("Mukundhan");
//            }
//        };
//
//        System.out.println(list);
//
//        List<Person> itEmployees = new ArrayList<>() {
//            {
//                add(Person.builder().name("Rajkumar").age(null).build());
//                add(Person.builder().name("Deepika").age("").build());
//                add(Person.builder().name("Jayagandhi").age("55").build());
//                add(Person.builder().name("Rajamani").age("66").build());
//                add(Person.builder().name("Aadhi").age("66").build());
//            }
//        };
//
//        List<Person> managementEmployees = new ArrayList<>() {
//            {
//                add(Person.builder().name("Rajiv").age("55").build());
//                add(Person.builder().name("Mukilan").age("30").build());
//                add(Person.builder().name("Mukundan").age("26").build());
//                add(Person.builder().name("Padma").age("66").build());
//            }
//        };
//
//        Employee employee = Employee.builder()
//                .names(list)
//                .itEmployees(itEmployees)
//                .managementEmployees(managementEmployees)
//                .locations(new HashSet<>() {{
//                    add("Bangalore");
//                    add("Aavadi");
//                    add("Chennai");
//                }})
//                .empGroups(new HashMap<>() {
//                    {
//                        put("Mech", itEmployees.get(0));
//                        put("IT", itEmployees.get(2));
//                        put("Civil", itEmployees.get(1));
//                        put("Aviation", itEmployees.get(3));
//                        put("Box", itEmployees.get(1));
//                    }
//                })
//                .build();
//
//
//

        ComplexPojo complexPojo = new ComplexPojo();
        complexPojo.setId(1);
        complexPojo.setName("John Doe");
        complexPojo.setSalary(50000.0);

        Address address = new Address();
        address.setStreet("123 Main St");
        address.setCity("Anytown");
        address.setState("CA");
        address.setZipCode(12345);
        complexPojo.setAddress(address);

        List<String> hobbies = new ArrayList<>();
        hobbies.add("Reading");
        hobbies.add("Coding");
        hobbies.add("Hiking");
        complexPojo.setHobbies(hobbies);

        Set<Integer> luckyNumbers = new HashSet<>();
        luckyNumbers.add(13);
        luckyNumbers.add(7);
        luckyNumbers.add(21);
        complexPojo.setLuckyNumbers(luckyNumbers);

        Map<String, Employer> employees = new HashMap<>();
        Employer employee1 = new Employer();
        employee1.setEmployeeId(101);
        employee1.setEmployeeName("Alice");
        employee1.setEmployeeSalary(40000.0);
        employees.put("Alice", employee1);

        Employer employee2 = new Employer();
        employee2.setEmployeeId(102);
        employee2.setEmployeeName("Bob");
        employee2.setEmployeeSalary(45000.0);
        employees.put("Bob", employee2);
        employees.put("Xen", employee2);
        employees.put("Zeb", employee2);
        employees.put("Ove", employee2);
        employees.put("Aev", employee2);

        complexPojo.setEmployees(employees);

        sorter.sortCollectionsOfObject(complexPojo);

        System.out.println(JsonBuilder.transformPojoToJsonNode(complexPojo).toPrettyString());
    }

    @SneakyThrows
    public void sortCollectionsOfObject(Object object) {


        if (Objects.nonNull(object)) {

            if (object instanceof List<?>) {
                @SuppressWarnings("unchecked")
                List<Object> list = (List<Object>) object;

                if (!list.isEmpty()) {
                    Class<?> listItemType = list.get(0).getClass();

                    if (!this.isPrimitiveWrapper(listItemType)) {
                        for (Object listItem : list) {
                            sortCollectionsOfObject(listItem); // recursive - to look for object members and sort its collection objects.
                        }
                    }
                    list.sort(Comparator.comparing(Object::toString));
                }
            } else if (object instanceof Set<?>) {
                @SuppressWarnings("unchecked")
                Set<Object> set = (Set<Object>) object;

                if (!set.isEmpty()) {
                    List<Object> list = new ArrayList<>(set);
                    Class<?> listItemType = list.get(0).getClass();

                    if (!this.isPrimitiveWrapper(listItemType)) {
                        for (Object listItem : list) {
                            sortCollectionsOfObject(listItem); // recursive - to look for object members and sort its collection objects.
                        }
                    }
                    list.sort(Comparator.comparing(Object::toString));
                    set.clear();
                    set.addAll(list);
                }
            } else if (object instanceof Map<?, ?>) {

                @SuppressWarnings("unchecked")
                Map<Object, Object> map = (Map<Object, Object>) object;

                if (!map.isEmpty()) {
                    LinkedHashMap<Object, Object> linkedMap = map.entrySet().stream()
                            .peek(entry -> {
                                if (!isPrimitiveWrapper(entry.getKey().getClass()))
                                    sortCollectionsOfObject(entry.getKey());

                                if (!isPrimitiveWrapper(entry.getValue().getClass()))
                                    sortCollectionsOfObject(entry.getValue());
                            })
                            .sorted(Comparator.comparing(entry -> entry.getKey().toString()))
                            .collect(Collectors.toMap(
                                    Map.Entry::getKey,
                                    Map.Entry::getValue,
                                    (oldKey, newKey) -> oldKey,
                                    LinkedHashMap::new
                            ));
                    map.clear();
                    map.putAll(linkedMap);
                }
            } else {
                Class<?> type = object.getClass();
                if (!type.isPrimitive() && !isPrimitiveWrapper(type) && !type.isEnum()) {
                    Field[] fields = type.getDeclaredFields();

                    for (Field field : fields) {
                        field.setAccessible(true);
                        Object value = field.get(object);

                        if (!field.getType().isPrimitive() && !type.isEnum()) {
                            sortCollectionsOfObject(value);
                        }
                    }
                }
            }


        }

    }

    private boolean isPrimitiveWrapper(Class<?> clazz) {
        return clazz == Boolean.class || clazz == Byte.class || clazz == Character.class ||
                clazz == Short.class || clazz == Integer.class || clazz == Long.class ||
                clazz == Float.class || clazz == Double.class;
    }


}
