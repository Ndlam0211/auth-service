package com.example.ecommerce.specifications;

import org.springframework.data.jpa.domain.Specification;

import javax.persistence.criteria.Predicate;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;


public class BaseSpecification<T> {
    // Search by keyword in multiple fields
    public static <T> Specification<T> keywordSpec(String keyword, String... fields) {
        return (root, query, criteriaBuilder) -> {
            if (keyword == null || keyword.isEmpty()) {
                return criteriaBuilder.conjunction(); // where 1=1
            }

            Predicate[] predicates = new Predicate[fields.length]; // mang predicate nay chua cac cau dieu kien where
            for (int i = 0; i < fields.length; i++) {
                // where lower(field) like %keyword%
                predicates[i] = criteriaBuilder.like(
                        criteriaBuilder.lower(root.get(fields[i])),
                        "%" + keyword.toLowerCase() + "%"
                );
            }

            return criteriaBuilder.or(predicates); // where lower(field1) like %keyword% or lower(field2) like %keyword% or ...
        };
    }

    // Search by multiple simple filters: id=1, name=abc, ...
    public static <T> Specification<T> simpleFilterSpec(Map<String, String> filters){
        return (root, query, criteriaBuilder) -> {
            return criteriaBuilder.and(filters.entrySet().stream()
                    .map(entry -> criteriaBuilder.equal(root.get(entry.getKey()), entry.getValue())).toArray(Predicate[]::new)); // where id=1 and name=abc and ...
        };
    }

    // Search by multiple complex filters: age[lt]=10, price[gt]=100, ...
    public static <T> Specification<T> complexFilterSpec(Map<String, Map<String,String>> filters){
        return (root, query, criteriaBuilder) -> {
            List<Predicate> predicates = filters.entrySet().stream()
                    .flatMap(entry -> entry.getValue().entrySet().stream()
                            .map(condition -> {
                                String field = entry.getKey();
                                String operator = condition.getKey();
                                String value = condition.getValue();

                                switch(operator.toLowerCase()){
                                    case "eq":
                                        return criteriaBuilder.equal(root.get(field), value);
                                    case "lt":
                                        return criteriaBuilder.lessThan(root.get(field), value);
                                    case "lte":
                                        return criteriaBuilder.lessThanOrEqualTo(root.get(field),value);
                                    case "gt":
                                        return criteriaBuilder.greaterThan(root.get(field), value);
                                    case "gte":
                                        return criteriaBuilder.greaterThanOrEqualTo(root.get(field),value);
                                    case "in":
                                        List<String> values = Arrays.asList(value.split(",")); // value = "1,2,3,4" => values = ["1","2","3","4"]
                                        return root.get(field).in(values);
                                    default:
                                        throw new IllegalArgumentException("Operator not supported: " + operator);
                                }
                            })
                    )
                    .collect(Collectors.toList());

                return criteriaBuilder.and(predicates.toArray(new Predicate[]{})); // where age<10 and price>100 and ...
        };
    }
}
