package com.rjs.mymovies.server.repos.filter;

public class CollectionDataFilter extends AtomicDataFilter {
    public static final String IN_OPERATOR = "IN";
    public static final String CONTAINS_OPERATOR = "CONTAINS";

    public CollectionDataFilter(String field, String operator) {
        this(field, operator, null);
    }

    public CollectionDataFilter(String field, String operator, Object value) {
        super(field, operator, value);
    }

    @Override
    protected void validate(String field, String operator, Object value) throws IllegalArgumentException {
        super.validate(field, operator, value);


        if (value == null) {
            String badOperator = null;

            switch (operator) {
                case IN_OPERATOR:
                case CONTAINS_OPERATOR:
                    badOperator = operator;

                    break;
            }

            if (badOperator != null) {
                throw new IllegalArgumentException("Cannot use " + badOperator + " operator with a null value.");
            }
        }
    }
}
