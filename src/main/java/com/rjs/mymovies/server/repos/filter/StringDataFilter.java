package com.rjs.mymovies.server.repos.filter;

public class StringDataFilter extends AtomicDataFilter {
    public static final String LIKE_OPERATOR = "LIKE";

    public StringDataFilter(String field, String operator) {
        this(field, operator, null);
    }

    public StringDataFilter(String field, String operator, Object value) {
        super(field, operator, value);
    }

    @Override
    protected void validate(String field, String operator, Object value) throws IllegalArgumentException {
        super.validate(field, operator, value);

        if (value == null) {
            String badOperator = null;

            switch (operator) {
                case LIKE_OPERATOR:
                    badOperator = operator;

                    break;
            }

            if (badOperator != null) {
                throw new IllegalArgumentException("Cannot use " + badOperator + " operator with a null value.");
            }
        }
    }
}
