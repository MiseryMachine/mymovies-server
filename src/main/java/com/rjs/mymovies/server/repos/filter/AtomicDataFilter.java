package com.rjs.mymovies.server.repos.filter;

public class AtomicDataFilter extends DataFilter {
    public static final String EQ_OPERATOR = "=";
    public static final String N_EQ_OPERATOR = "!=";

    public AtomicDataFilter(String field, String operator) {
        this(field, operator, null);
    }

    public AtomicDataFilter(String field, String operator, Object value) {
        super(field, operator, value);
    }
}
