package org.fergoeqs.specification;

import org.fergoeqs.dto.FilterConditionDTO;

import org.fergoeqs.model.Organization;
import org.fergoeqs.model.OrganizationType;

import jakarta.persistence.criteria.*;
import jakarta.ejb.Stateless;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

@Stateless
public class OrganizationSpecifications {

    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ISO_LOCAL_DATE_TIME;

    public Predicate buildPredicate(Root<Organization> root, CriteriaBuilder cb,
                                    List<FilterConditionDTO> filters) {
        if (filters == null || filters.isEmpty()) {
            return cb.conjunction();
        }

        List<Predicate> predicates = new ArrayList<>();

        for (FilterConditionDTO filter : filters) {
            Predicate predicate = buildFilterPredicate(root, cb, filter);
            if (predicate != null) {
                predicates.add(predicate);
            }
        }

        return predicates.isEmpty() ? cb.conjunction() : cb.and(predicates.toArray(new Predicate[0]));
    }

    private Predicate buildFilterPredicate(Root<Organization> root, CriteriaBuilder cb,
                                           FilterConditionDTO filter) {
        try {
            Path<?> fieldPath = getFieldPath(root, filter.field());
            Class<?> fieldType = fieldPath.getJavaType();

            System.out.println("Filter field: " + filter.field() + ", type: " + fieldType + ", value: " + filter.value());

            return switch (filter.operator().toLowerCase()) {
                case "eq" -> buildEqualsPredicate(cb, fieldPath, filter.value(), fieldType);
                case "ne" -> buildNotEqualsPredicate(cb, fieldPath, filter.value(), fieldType);
                case "gt" -> buildComparisonPredicate(cb, fieldPath, filter.value(), fieldType, "gt");
                case "gte" -> buildComparisonPredicate(cb, fieldPath, filter.value(), fieldType, "gte");
                case "lt" -> buildComparisonPredicate(cb, fieldPath, filter.value(), fieldType, "lt");
                case "lte" -> buildComparisonPredicate(cb, fieldPath, filter.value(), fieldType, "lte");
                case "like" -> buildLikePredicate(cb, fieldPath, filter.value());
                case "in" -> buildInPredicate(cb, fieldPath, filter.value(), fieldType);
                case "between" -> buildBetweenPredicate(cb, fieldPath, filter.value(), fieldType);
                default -> throw new IllegalArgumentException("Unsupported operator: " + filter.operator());
            };
        } catch (Exception e) {
            e.printStackTrace();
            throw new IllegalArgumentException(
                    String.format("Invalid filter condition: %s %s %s. Error: %s",
                            filter.field(), filter.operator(), filter.value(), e.getMessage()), e);
        }
    }

    private Path<?> getFieldPath(Root<Organization> root, String field) {
        String[] parts = field.split("\\.");
        Path<?> path = root;

        for (String part : parts) {
            if ("postalAddress".equals(part)) {
                path = path.get("postalAddress");
            } else if ("coordinates".equals(part)) {
                path = path.get("coordinates");
            } else {
                path = path.get(part);
            }
        }

        return path;
    }

    private Predicate buildEqualsPredicate(CriteriaBuilder cb, Path<?> fieldPath, Object value, Class<?> fieldType) {
        if (fieldType == OrganizationType.class) {
            OrganizationType enumValue = OrganizationType.valueOf(value.toString());
            return cb.equal(fieldPath, enumValue);
        } else if (fieldType == String.class) {
            return cb.equal(fieldPath, value.toString());
        } else if (fieldType == Integer.class || fieldType == int.class) {
            return cb.equal(fieldPath, Integer.valueOf(value.toString()));
        } else {
            return cb.equal(fieldPath, convertValue(value, fieldType));
        }
    }

    private Predicate buildNotEqualsPredicate(CriteriaBuilder cb, Path<?> fieldPath, Object value, Class<?> fieldType) {
        if (fieldType == OrganizationType.class) {
            OrganizationType enumValue = OrganizationType.valueOf(value.toString());
            return cb.notEqual(fieldPath, enumValue);
        } else {
            return cb.notEqual(fieldPath, convertValue(value, fieldType));
        }
    }

    @SuppressWarnings("unchecked")
    private Predicate buildComparisonPredicate(CriteriaBuilder cb, Path<?> fieldPath,
                                               Object value, Class<?> fieldType, String operator) {
        if (!Comparable.class.isAssignableFrom(fieldType)) {
            throw new IllegalArgumentException("Field is not comparable for " + operator + " operation");
        }

        Comparable<?> convertedValue = (Comparable<?>) convertValue(value, fieldType);

        return switch (operator) {
            case "gt" -> cb.greaterThan((Path<Comparable>) fieldPath, (Comparable) convertedValue);
            case "gte" -> cb.greaterThanOrEqualTo((Path<Comparable>) fieldPath, (Comparable) convertedValue);
            case "lt" -> cb.lessThan((Path<Comparable>) fieldPath, (Comparable) convertedValue);
            case "lte" -> cb.lessThanOrEqualTo((Path<Comparable>) fieldPath, (Comparable) convertedValue);
            default -> throw new IllegalArgumentException("Unsupported comparison operator: " + operator);
        };
    }

    private Predicate buildLikePredicate(CriteriaBuilder cb, Path<?> fieldPath, Object value) {
        if (fieldPath.getJavaType() != String.class) {
            throw new IllegalArgumentException("Field is not a string for like operation");
        }

        String stringValue = value.toString();
        return cb.like((Path<String>) fieldPath, "%" + stringValue + "%");
    }

    @SuppressWarnings("unchecked")
    private Predicate buildInPredicate(CriteriaBuilder cb, Path<?> fieldPath, Object value, Class<?> fieldType) {
        if (!(value instanceof List<?>)) {
            throw new IllegalArgumentException("IN operator requires a list of values");
        }

        List<?> values = (List<?>) value;
        CriteriaBuilder.In<Object> inClause = cb.in(fieldPath);

        for (Object val : values) {
            inClause.value(convertValue(val, fieldType));
        }

        return inClause;
    }

    @SuppressWarnings("unchecked")
    private Predicate buildBetweenPredicate(CriteriaBuilder cb, Path<?> fieldPath, Object value, Class<?> fieldType) {
        if (!(value instanceof List<?>)) {
            throw new IllegalArgumentException("BETWEEN operator requires a list of two values");
        }

        List<?> range = (List<?>) value;
        if (range.size() != 2) {
            throw new IllegalArgumentException("BETWEEN operator requires exactly two values");
        }

        if (!Comparable.class.isAssignableFrom(fieldType)) {
            throw new IllegalArgumentException("Field is not comparable for between operation");
        }

        Comparable<?> lower = (Comparable<?>) convertValue(range.get(0), fieldType);
        Comparable<?> upper = (Comparable<?>) convertValue(range.get(1), fieldType);

        return cb.between((Path<Comparable>) fieldPath, (Comparable) lower, (Comparable) upper);
    }

    private Object convertValue(Object value, Class<?> targetType) {
        if (value == null) {
            return null;
        }

        if (targetType.isAssignableFrom(value.getClass())) {
            return value;
        }

        String stringValue = value.toString();

        System.out.println("Converting value: " + stringValue + " to type: " + targetType);

        try {
            if (targetType == String.class) {
                return stringValue;
            } else if (targetType == Integer.class || targetType == int.class) {
                return Integer.valueOf(stringValue);
            } else if (targetType == Long.class || targetType == long.class) {
                return Long.valueOf(stringValue);
            } else if (targetType == Double.class || targetType == double.class) {
                return Double.valueOf(stringValue);
            } else if (targetType == Float.class || targetType == float.class) {
                return Float.valueOf(stringValue);
            } else if (targetType == LocalDateTime.class) {
                return LocalDateTime.parse(stringValue, DATE_FORMATTER);
            } else if (targetType == OrganizationType.class) {
                return OrganizationType.valueOf(stringValue);
            } else {
                throw new IllegalArgumentException("Unsupported target type: " + targetType);
            }
        } catch (Exception e) {
            System.err.println("Error converting value " + stringValue + " to " + targetType + ": " + e.getMessage());
            throw e;
        }
    }

    public List<Order> buildOrders(CriteriaBuilder cb, Root<Organization> root,
                                   List<org.fergoeqs.dto.SortOptionDTO> sortOptions) {
        List<Order> orders = new ArrayList<>();

        if (sortOptions == null || sortOptions.isEmpty()) {
            return orders;
        }

        for (org.fergoeqs.dto.SortOptionDTO sortOption : sortOptions) {
            try {
                Path<?> fieldPath = getFieldPath(root, sortOption.field());
                String direction = sortOption.direction() != null ? sortOption.direction().toLowerCase() : "asc";

                if ("desc".equals(direction)) {
                    orders.add(cb.desc(fieldPath));
                } else {
                    orders.add(cb.asc(fieldPath));
                }
            } catch (Exception e) {
                System.err.println("Error creating order for field " + sortOption.field() + ": " + e.getMessage());
            }
        }

        return orders;
    }
}