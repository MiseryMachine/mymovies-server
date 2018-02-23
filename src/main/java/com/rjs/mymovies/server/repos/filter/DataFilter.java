package com.rjs.mymovies.server.repos.filter;

import org.apache.commons.lang3.StringUtils;

public abstract class DataFilter {
    public final String field;
    public final String operator;
    public final Object value;

    public DataFilter(String field, String operator, Object value) {
        validate(field, operator, value);

        this.field = field;
        this.operator = operator;
        this.value = value;
    }

    protected void validate(String field, String operator, Object value) throws IllegalArgumentException {
        if (StringUtils.isBlank(field)) {
            throw new IllegalArgumentException("Field must be provided.");
        }

        if (StringUtils.isBlank(operator)) {
            throw new IllegalArgumentException("Operator must be provided.");
        }
    }
}
