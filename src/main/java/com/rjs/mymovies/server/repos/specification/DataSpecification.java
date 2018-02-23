package com.rjs.mymovies.server.repos.specification;

import com.rjs.mymovies.server.repos.filter.*;
import org.springframework.data.jpa.domain.Specification;

import javax.persistence.criteria.*;
import java.util.Collection;
import java.util.Date;
import java.util.logging.Logger;

public class DataSpecification<E> implements Specification<E> {
    private static final Logger LOGGER = Logger.getLogger(DataSpecification.class.getName());

    private DataFilter dataFilter;

    public DataSpecification(DataFilter dataFilter) {
        this.dataFilter = dataFilter;
    }

    @Override
    public Predicate toPredicate(Root<E> root, CriteriaQuery<?> criteriaQuery, CriteriaBuilder criteriaBuilder) {
        Path fieldPath = root.get(dataFilter.field);

        if (dataFilter.value != null) {
            switch (dataFilter.operator) {
                case AtomicDataFilter.EQ_OPERATOR:
                    return criteriaBuilder.equal(fieldPath, dataFilter.value);

                case AtomicDataFilter.N_EQ_OPERATOR:
                    return criteriaBuilder.notEqual(fieldPath, dataFilter.value);
            }

            // More extensive filtering
            Class fieldClass = root.get(dataFilter.field).getJavaType();

            if (fieldClass == String.class) {
                switch (dataFilter.operator) {
                    case StringDataFilter.LIKE_OPERATOR:
                        return criteriaBuilder.like(fieldPath, "%" + dataFilter.value + "%");
                }
            }
            else if (fieldClass == Integer.class || fieldClass == int.class) {
                switch (dataFilter.operator) {
                    case NumericDateDataFilter.GT_OPERATOR:
                        return criteriaBuilder.greaterThan(fieldPath, (Integer) dataFilter.value);

                    case NumericDateDataFilter.GTE_OPERATOR:
                        return criteriaBuilder.greaterThanOrEqualTo(fieldPath, (Integer) dataFilter.value);

                    case NumericDateDataFilter.LT_OPERATOR:
                        return criteriaBuilder.lessThan(fieldPath, (Integer) dataFilter.value);

                    case NumericDateDataFilter.LTE_OPERATOR:
                        return criteriaBuilder.lessThanOrEqualTo(fieldPath, (Integer) dataFilter.value);
                }
            }
            else if (fieldClass == Long.class || fieldClass == long.class) {
                switch (dataFilter.operator) {
                    case NumericDateDataFilter.GT_OPERATOR:
                        return criteriaBuilder.greaterThan(fieldPath, (Long) dataFilter.value);

                    case NumericDateDataFilter.GTE_OPERATOR:
                        return criteriaBuilder.greaterThanOrEqualTo(fieldPath, (Long) dataFilter.value);

                    case NumericDateDataFilter.LT_OPERATOR:
                        return criteriaBuilder.lessThan(fieldPath, (Long) dataFilter.value);

                    case NumericDateDataFilter.LTE_OPERATOR:
                        return criteriaBuilder.lessThanOrEqualTo(fieldPath, (Long) dataFilter.value);
                }
            }
            else if (fieldClass == Float.class || fieldClass == float.class) {
                switch (dataFilter.operator) {
                    case NumericDateDataFilter.GT_OPERATOR:
                        return criteriaBuilder.greaterThan(fieldPath, (Float) dataFilter.value);

                    case NumericDateDataFilter.GTE_OPERATOR:
                        return criteriaBuilder.greaterThanOrEqualTo(fieldPath, (Float) dataFilter.value);

                    case NumericDateDataFilter.LT_OPERATOR:
                        return criteriaBuilder.lessThan(fieldPath, (Float) dataFilter.value);

                    case NumericDateDataFilter.LTE_OPERATOR:
                        return criteriaBuilder.lessThanOrEqualTo(fieldPath, (Float) dataFilter.value);
                }
            }
            else if (fieldClass == Double.class || fieldClass == double.class) {
                switch (dataFilter.operator) {
                    case NumericDateDataFilter.GT_OPERATOR:
                        return criteriaBuilder.greaterThan(fieldPath, (Double) dataFilter.value);

                    case NumericDateDataFilter.GTE_OPERATOR:
                        return criteriaBuilder.greaterThanOrEqualTo(fieldPath, (Double) dataFilter.value);

                    case NumericDateDataFilter.LT_OPERATOR:
                        return criteriaBuilder.lessThan(fieldPath, (Double) dataFilter.value);

                    case NumericDateDataFilter.LTE_OPERATOR:
                        return criteriaBuilder.lessThanOrEqualTo(fieldPath, (Double) dataFilter.value);
                }
            }
            else if (fieldClass == Date.class) {
                switch (dataFilter.operator) {
                    case NumericDateDataFilter.GT_OPERATOR:
                        return criteriaBuilder.greaterThan(fieldPath, (Date) dataFilter.value);

                    case NumericDateDataFilter.GTE_OPERATOR:
                        return criteriaBuilder.greaterThanOrEqualTo(fieldPath, (Date) dataFilter.value);

                    case NumericDateDataFilter.LT_OPERATOR:
                        return criteriaBuilder.lessThan(fieldPath, (Date) dataFilter.value);

                    case NumericDateDataFilter.LTE_OPERATOR:
                        return criteriaBuilder.lessThanOrEqualTo(fieldPath, (Date) dataFilter.value);
                }
            }
            else if (dataFilter.value instanceof Collection) {
                switch (dataFilter.operator) {
                    case CollectionDataFilter.IN_OPERATOR:
                        CriteriaBuilder.In inBuilder = criteriaBuilder.in(fieldPath);
                        Collection coll = (Collection) dataFilter.value;

                        for (Object obj : coll) {
                            inBuilder.value(obj);
                        }

                        return inBuilder;

                    case CollectionDataFilter.CONTAINS_OPERATOR:
                        Collection valueCollection = (Collection) dataFilter.value;

                        if (!valueCollection.isEmpty()) {
                            Predicate[] predicates = new Predicate[valueCollection.size()];
                            int idx = 0;

                            for (Object valueObj : valueCollection) {
//                                predicates[idx++] = criteriaBuilder.equal(fieldPath, valueObj);
                                predicates[idx++] = criteriaBuilder.isMember(valueObj, fieldPath);
                            }

                            return predicates.length > 1 ? criteriaBuilder.or(predicates) : predicates[0];
//                            return criteriaBuilder.isMember(valueCollection, fieldPath);
                        }

                        return null;
                }
            }
        }
        else {
            switch (dataFilter.operator) {
                case AtomicDataFilter.EQ_OPERATOR:
                    return criteriaBuilder.isNull(fieldPath);

                case AtomicDataFilter.N_EQ_OPERATOR:
                    return criteriaBuilder.isNotNull(fieldPath);
            }
        }

        return null;
    }
}
