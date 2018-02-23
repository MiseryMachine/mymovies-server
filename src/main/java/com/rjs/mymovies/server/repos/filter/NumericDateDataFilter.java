package com.rjs.mymovies.server.repos.filter;

public class NumericDateDataFilter extends AtomicDataFilter {
    public static final String GT_OPERATOR = ">";
    public static final String GTE_OPERATOR = ">=";
    public static final String LT_OPERATOR = "<";
    public static final String LTE_OPERATOR = "<=";

    public NumericDateDataFilter(String field, String operator) {
        this(field, operator, null);
    }

    public NumericDateDataFilter(String field, String operator, Object value) {
        super(field, operator, value);
    }

    @Override
    protected void validate(String field, String operator, Object value) throws IllegalArgumentException {
        super.validate(field, operator, value);

        if (value == null) {
            String badOperator = null;

            switch (operator) {
                case GT_OPERATOR:
                case GTE_OPERATOR:
                case LT_OPERATOR:
                case LTE_OPERATOR:
                    badOperator = operator;

                    break;
            }

            if (badOperator != null) {
                throw new IllegalArgumentException("Cannot use " + badOperator + " operator with a null value.");
            }
        }
    }
}
