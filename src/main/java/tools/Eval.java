/*
 * Copyright 2008  Reg Whitton
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package tools;

import java.math.*;
import java.util.Map;

public final class Eval {

    public static enum Type {

        ARITHMETIC("arithmetic"), BOOLEAN("boolean");
        final String name;

        Type(String name) {
            this.name = name;
        }
    }

    public static final class Tokeniser {

        static final Character START_NEW_EXPRESSION = Character.valueOf('(');
        private final String string;
        private int position;
        private Operator pushedBackOperator = null;

        Tokeniser(String string) {
            this.string = string;
            this.position = 0;
        }

        int getPosition() {
            return this.position;
        }

        void setPosition(int position) {
            this.position = position;
        }

        void pushBack(Operator operator) {
            this.pushedBackOperator = operator;
        }

        Operator getOperator(char endOfExpressionChar) {
            /* Use any pushed back operator. */
            if (this.pushedBackOperator != null) {
                Operator operator = this.pushedBackOperator;
                this.pushedBackOperator = null;
                return operator;
            }

            /* Skip whitespace */
            final int len = this.string.length();
            char ch = 0;
            while (this.position < len && Character.isWhitespace(ch = this.string.charAt(this.position))) {
                this.position++;
            }
            if (this.position == len) {
                if (endOfExpressionChar == 0) {
                    return Operator.END;
                } else {
                    throw new RuntimeException("missing " + endOfExpressionChar);
                }
            }

            this.position++;
            if (ch == endOfExpressionChar) {
                return Operator.END;
            }

            switch (ch) {
                case '+': {
                    return Operator.ADD;
                }
                case '-': {
                    return Operator.SUB;
                }
                case '/': {
                    return Operator.DIV;
                }
                case '%': {
                    return Operator.REMAINDER;
                }
                case '*': {
                    return Operator.MUL;
                }
                case '?': {
                    return Operator.TERNARY;
                }
                case '>': {
                    if (this.position < len && this.string.charAt(this.position) == '=') {
                        this.position++;
                        return Operator.GE;
                    }
                    return Operator.GT;
                }
                case '<': {
                    if (this.position < len) {
                        switch (this.string.charAt(this.position)) {
                            case '=':
                                this.position++;
                                return Operator.LE;
                            case '>':
                                this.position++;
                                return Operator.NE;
                        }
                    }
                    return Operator.LT;
                }
                case '=': {
                    if (this.position < len && this.string.charAt(this.position) == '=') {
                        this.position++;
                        return Operator.EQ;
                    }
                    throw new RuntimeException("use == for equality at position " + this.position);
                }
                case '!': {
                    if (this.position < len && this.string.charAt(this.position) == '=') {
                        this.position++;
                        return Operator.NE;
                    }
                    throw new RuntimeException(
                            "use != or <> for inequality at position " + this.position);
                }
                case '&': {
                    if (this.position < len && this.string.charAt(this.position) == '&') {
                        this.position++;
                        return Operator.AND;
                    }
                    throw new RuntimeException("use && for AND at position " + this.position);
                }
                case '|': {
                    if (this.position < len && this.string.charAt(this.position) == '|') {
                        this.position++;
                        return Operator.OR;
                    }
                    throw new RuntimeException("use || for OR at position " + this.position);
                }
                default: {
                    /* Is this an identifier name for an operator function? */
                    if (Character.isUnicodeIdentifierStart(ch)) {
                        int start = this.position - 1;
                        while (this.position < len && Character.isUnicodeIdentifierPart(this.string.charAt(this.position))) {
                            this.position++;
                        }

                        String name = this.string.substring(start, this.position);
                        if (name.equals("pow")) {
                            return Operator.POW;
                        }
                    }
                    throw new RuntimeException("operator expected at position " + this.position + " instead of '" + ch + "'");
                }
            }
        }

        /**
         * Called when an operand is expected next.
         *
         * @return one of:
         * <UL>
         * <LI>a {@link BigDecimal} value;</LI>
         * <LI>the {@link String} name of a variable;</LI>
         * <LI>{@link Tokeniser#START_NEW_EXPRESSION} when an opening
         * parenthesis is found: </LI>
         * <LI>or {@link Operator} when a unary operator is found in front of an
         * operand</LI>
         * </UL>
         *
         * @throws RuntimeException if the end of the string is reached
         * unexpectedly.
         */
        Object getOperand() {
            /* Skip whitespace */
            final int len = this.string.length();
            char ch = 0;
            while (this.position < len && Character.isWhitespace(ch = this.string.charAt(this.position))) {
                this.position++;
            }
            if (this.position == len) {
                throw new RuntimeException(
                        "operand expected but end of string found");
            }

            if (ch == '(') {
                this.position++;
                return START_NEW_EXPRESSION;
            } else if (ch == '-') {
                this.position++;
                return Operator.NEG;
            } else if (ch == '+') {
                this.position++;
                return Operator.PLUS;
            } else if (ch == '.' || Character.isDigit(ch)) {
                return getBigDecimal();
            } else if (Character.isUnicodeIdentifierStart(ch)) {
                int start = this.position++;
                while (this.position < len && Character.isUnicodeIdentifierPart(this.string.charAt(this.position))) {
                    this.position++;
                }

                String name = this.string.substring(start, this.position);
                /* Is variable name actually a keyword unary operator? */
                if (name.equals("abs")) {
                    return Operator.ABS;
                } else if (name.equals("int")) {
                    return Operator.INT;
                } else if (name.equals("ceil")) {
                    return Operator.CEIL;
                } else if (name.equals("floor")) {
                    return Operator.FLOOR;
                }
                /* Return variable name */
                return name;
            }
            throw new RuntimeException("operand expected but '" + ch + "' found");
        }

        private BigDecimal getBigDecimal() {
            final int len = this.string.length();
            final int start = this.position;
            char ch;

            while (this.position < len && (Character.isDigit(ch = this.string.charAt(this.position)) || ch == '.')) {
                this.position++;
            }

            /* Optional exponent part including another sign character. */
            if (this.position < len && ((ch = this.string.charAt(this.position)) == 'E' || ch == 'e')) {
                this.position++;
                if (this.position < len && ((ch = this.string.charAt(this.position)) == '+' || ch == '-')) {
                    this.position++;
                }
                while (this.position < len && Character.isDigit(ch = this.string.charAt(this.position))) {
                    this.position++;
                }
            }
            return new BigDecimal(this.string.substring(start, this.position));
        }

        @Override
        public String toString() {
            return this.string.substring(0, this.position) + ">>>" + this.string.substring(this.position);
        }
    }

    public static enum Operator {

        /**
         * End of string reached.
         */
        END(-1, 0, null, null, null) {

                    @Override
                    BigDecimal perform(BigDecimal value1, BigDecimal value2,
                            BigDecimal value3) {
                        throw new RuntimeException("END is a dummy operation");
                    }
                },
        /**
         * condition ? (expression if true) : (expression if false)
         */
        TERNARY(0, 3, "?", null, null) {

                    @Override
                    BigDecimal perform(BigDecimal value1, BigDecimal value2,
                            BigDecimal value3) {
                        return (value1.signum() != 0) ? value2 : value3;
                    }
                },
        /**
         * &amp;&amp;
         */
        AND(0, 2, "&&", Type.BOOLEAN, Type.BOOLEAN) {

                    @Override
                    BigDecimal perform(BigDecimal value1, BigDecimal value2,
                            BigDecimal value3) {
                        return value1.signum() != 0 && value2.signum() != 0 ? BigDecimal.ONE
                                : BigDecimal.ZERO;
                    }
                },
        /**
         * ||
         */
        OR(0, 2, "||", Type.BOOLEAN, Type.BOOLEAN) {

                    @Override
                    BigDecimal perform(BigDecimal value1, BigDecimal value2,
                            BigDecimal value3) {
                        return value1.signum() != 0 || value2.signum() != 0 ? BigDecimal.ONE
                                : BigDecimal.ZERO;
                    }
                },
        /**
         * &gt;
         */
        GT(1, 2, ">", Type.BOOLEAN, Type.ARITHMETIC) {

                    @Override
                    BigDecimal perform(BigDecimal value1, BigDecimal value2,
                            BigDecimal value3) {
                        return value1.compareTo(value2) > 0 ? BigDecimal.ONE
                                : BigDecimal.ZERO;
                    }
                },
        /**
         * &gt;=
         */
        GE(1, 2, ">=", Type.BOOLEAN, Type.ARITHMETIC) {

                    @Override
                    BigDecimal perform(BigDecimal value1, BigDecimal value2,
                            BigDecimal value3) {
                        return value1.compareTo(value2) >= 0 ? BigDecimal.ONE
                                : BigDecimal.ZERO;
                    }
                },
        /**
         * &lt;
         */
        LT(1, 2, "<", Type.BOOLEAN, Type.ARITHMETIC) {

                    @Override
                    BigDecimal perform(BigDecimal value1, BigDecimal value2,
                            BigDecimal value3) {
                        return value1.compareTo(value2) < 0 ? BigDecimal.ONE
                                : BigDecimal.ZERO;
                    }
                },
        /**
         * &lt;=
         */
        LE(1, 2, "<=", Type.BOOLEAN, Type.ARITHMETIC) {

                    @Override
                    BigDecimal perform(BigDecimal value1, BigDecimal value2,
                            BigDecimal value3) {
                        return value1.compareTo(value2) <= 0 ? BigDecimal.ONE
                                : BigDecimal.ZERO;
                    }
                },
        /**
         * ==
         */
        EQ(1, 2, "==", Type.BOOLEAN, Type.ARITHMETIC) {

                    @Override
                    BigDecimal perform(BigDecimal value1, BigDecimal value2,
                            BigDecimal value3) {
                        return value1.compareTo(value2) == 0 ? BigDecimal.ONE
                                : BigDecimal.ZERO;
                    }
                },
        /**
         * != or &lt;&gt;
         */
        NE(1, 2, "!=", Type.BOOLEAN, Type.ARITHMETIC) {

                    @Override
                    BigDecimal perform(BigDecimal value1, BigDecimal value2,
                            BigDecimal value3) {
                        return value1.compareTo(value2) != 0 ? BigDecimal.ONE
                                : BigDecimal.ZERO;
                    }
                },
        /**
         * +
         */
        ADD(2, 2, "+", Type.ARITHMETIC, Type.ARITHMETIC) {

                    @Override
                    BigDecimal perform(BigDecimal value1, BigDecimal value2,
                            BigDecimal value3) {
                        return value1.add(value2);
                    }
                },
        /**
         * -
         */
        SUB(2, 2, "-", Type.ARITHMETIC, Type.ARITHMETIC) {

                    @Override
                    BigDecimal perform(BigDecimal value1, BigDecimal value2,
                            BigDecimal value3) {
                        return value1.subtract(value2);
                    }
                },
        /**
         * /
         */
        DIV(3, 2, "/", Type.ARITHMETIC, Type.ARITHMETIC) {

                    @Override
                    BigDecimal perform(BigDecimal value1, BigDecimal value2,
                            BigDecimal value3) {
                        return value1.divide(value2, MathContext.DECIMAL128);
                    }
                },
        /**
         * %
         */
        REMAINDER(3, 2, "%", Type.ARITHMETIC, Type.ARITHMETIC) {

                    @Override
                    BigDecimal perform(BigDecimal value1, BigDecimal value2,
                            BigDecimal value3) {
                        return value1.remainder(value2, MathContext.DECIMAL128);
                    }
                },
        /**
         * *
         */
        MUL(3, 2, "*", Type.ARITHMETIC, Type.ARITHMETIC) {

                    @Override
                    BigDecimal perform(BigDecimal value1, BigDecimal value2,
                            BigDecimal value3) {
                        return value1.multiply(value2);
                    }
                },
        /**
         * -negate
         */
        NEG(4, 1, "-", Type.ARITHMETIC, Type.ARITHMETIC) {

                    @Override
                    BigDecimal perform(BigDecimal value1, BigDecimal value2,
                            BigDecimal value3) {
                        return value1.negate();
                    }
                },
        /**
         * +plus
         */
        PLUS(4, 1, "+", Type.ARITHMETIC, Type.ARITHMETIC) {

                    @Override
                    BigDecimal perform(BigDecimal value1, BigDecimal value2,
                            BigDecimal value3) {
                        return value1;
                    }
                },
        /**
         * abs
         */
        ABS(4, 1, " abs ", Type.ARITHMETIC, Type.ARITHMETIC) {

                    @Override
                    BigDecimal perform(BigDecimal value1, BigDecimal value2,
                            BigDecimal value3) {
                        return value1.abs();
                    }
                },
        /**
         * pow
         */
        POW(4, 2, " pow ", Type.ARITHMETIC, Type.ARITHMETIC) {

                    @Override
                    BigDecimal perform(BigDecimal value1, BigDecimal value2,
                            BigDecimal value3) {
                        try {
                            return value1.pow(value2.intValueExact());
                        } catch (ArithmeticException ae) {
                            throw new RuntimeException("pow argument: " + ae.getMessage());
                        }
                    }
                },
        /**
         * int
         */
        INT(4, 1, "int ", Type.ARITHMETIC, Type.ARITHMETIC) {

                    @Override
                    BigDecimal perform(BigDecimal value1, BigDecimal value2,
                            BigDecimal value3) {
                        return new BigDecimal(value1.toBigInteger());
                    }
                },
        CEIL(4, 1, "ceil ", Type.ARITHMETIC, Type.ARITHMETIC) {

                    @Override
                    BigDecimal perform(BigDecimal value1, BigDecimal value2,
                            BigDecimal value3) {
                        return new BigDecimal(Math.ceil(value1.doubleValue()));
                    }
                },
        FLOOR(4, 1, "floor ", Type.ARITHMETIC, Type.ARITHMETIC) {

                    @Override
                    BigDecimal perform(BigDecimal value1, BigDecimal value2,
                            BigDecimal value3) {
                        return new BigDecimal(Math.floor(value1.doubleValue()));
                    }
                },
        /**
         * No operation - used internally when expression contains only a
         * reference to a variable.
         */
        NOP(4, 1, "", Type.ARITHMETIC, Type.ARITHMETIC) {

                    @Override
                    BigDecimal perform(BigDecimal value1, BigDecimal value2,
                            BigDecimal value3) {
                        return value1;
                    }
                };
        final int precedence;
        final int numberOfOperands;
        final String string;
        final Type resultType;
        final Type operandType;

        Operator(final int precedence, final int numberOfOperands,
                final String string, final Type resultType, final Type operandType) {
            this.precedence = precedence;
            this.numberOfOperands = numberOfOperands;
            this.string = string;
            this.resultType = resultType;
            this.operandType = operandType;
        }

        abstract BigDecimal perform(BigDecimal value1, BigDecimal value2,
                BigDecimal value3);
    }

    public static final class Operation {

        final Type type;
        final Operator operator;
        final Object operand1;
        final Object operand2;
        final Object operand3;

        private Operation(Type type, Operator operator, Object operand1,
                Object operand2, Object operand3) {
            this.type = type;
            this.operator = operator;
            this.operand1 = operand1;
            this.operand2 = operand2;
            this.operand3 = operand3;
        }

        static Operation nopOperationfactory(Object operand) {
            return new Operation(Operator.NOP.resultType, Operator.NOP, operand,
                    null, null);
        }

        static Object unaryOperationfactory(Operator operator, Object operand) {
            validateOperandType(operand, operator.operandType);

            /*
             * If values can already be resolved then return result instead of
             * operation
             */
            if (operand instanceof BigDecimal) {
                return operator.perform((BigDecimal) operand, null, null);
            }
            return new Operation(operator.resultType, operator, operand, null, null);
        }

        static Object binaryOperationfactory(Operator operator, Object operand1,
                Object operand2) {
            validateOperandType(operand1, operator.operandType);
            validateOperandType(operand2, operator.operandType);

            /*
             * If values can already be resolved then return result instead of
             * operation
             */
            if (operand1 instanceof BigDecimal && operand2 instanceof BigDecimal) {
                return operator.perform((BigDecimal) operand1,
                        (BigDecimal) operand2, null);
            }
            return new Operation(operator.resultType, operator, operand1, operand2,
                    null);
        }

        static Object tenaryOperationFactory(Operator operator, Object operand1,
                Object operand2, Object operand3) {
            validateOperandType(operand1, Type.BOOLEAN);
            validateOperandType(operand2, Type.ARITHMETIC);
            validateOperandType(operand3, Type.ARITHMETIC);

            /*
             * If values can already be resolved then return result instead of
             * operation
             */
            if (operand1 instanceof BigDecimal) {
                return ((BigDecimal) operand1).signum() != 0 ? operand2 : operand3;
            }
            return new Operation(Type.ARITHMETIC, operator, operand1, operand2,
                    operand3);
        }

        BigDecimal eval(Map<String, BigDecimal> variables) {
            switch (this.operator.numberOfOperands) {
                case 3:
                    return this.operator.perform(evaluateOperand(this.operand1,
                            variables), evaluateOperand(this.operand2, variables),
                            evaluateOperand(this.operand3, variables));
                case 2:
                    return this.operator.perform(evaluateOperand(this.operand1,
                            variables), evaluateOperand(this.operand2, variables),
                            null);
                default:
                    return this.operator.perform(evaluateOperand(this.operand1,
                            variables), null, null);
            }
        }

        private BigDecimal evaluateOperand(Object operand,
                Map<String, BigDecimal> variables) {
            if (operand instanceof Operation) {
                return ((Operation) operand).eval(variables);
            } else if (operand instanceof String) {
                BigDecimal value;
                if (variables == null || (value = variables.get(operand)) == null) {
                    throw new RuntimeException("no value for variable \"" + operand + "\"");
                }
                return value;
            } else {
                return (BigDecimal) operand;
            }
        }

        /**
         * Validate that where operations are combined together that the types
         * are as expected.
         */
        private static void validateOperandType(Object operand, Type type) {
            Type operandType;

            if (operand instanceof Operation && (operandType = ((Operation) operand).type) != type) {
                throw new RuntimeException("cannot use " + operandType.name + " operands with " + type.name + " operators");
            }
        }

        @Override
        public String toString() {
            switch (this.operator.numberOfOperands) {
                case 3:
                    return "(" + this.operand1 + this.operator.string + this.operand2 + ":" + this.operand3 + ")";
                case 2:
                    return "(" + this.operand1 + this.operator.string + this.operand2 + ")";
                default:
                    return "(" + this.operator.string + this.operand1 + ")";
            }
        }
    }

    public static class Compiler {

        private final Tokeniser tokeniser;

        Compiler(String expression) {
            this.tokeniser = new Tokeniser(expression);
        }

        Operation compile() {
            Object expression = compile(null, null, 0, (char) 0, -1);

            /*
             * If expression is a variable name or BigDecimal constant value then we
             * need to put into a NOP operation.
             */
            if (expression instanceof Operation) {
                return (Operation) expression;
            }
            return Operation.nopOperationfactory(expression);
        }

        private Object compile(Object preReadOperand, Operator preReadOperator,
                int nestingLevel, char endOfExpressionChar, int terminatePrecedence) {
            Object operand = preReadOperand != null ? preReadOperand
                    : getOperand(nestingLevel);
            Operator operator = preReadOperator != null ? preReadOperator
                    : this.tokeniser.getOperator(endOfExpressionChar);

            while (operator != Operator.END) {
                if (operator == Operator.TERNARY) {
                    Object operand2 = compile(null, null, nestingLevel, ':', -1);
                    Object operand3 = compile(null, null, nestingLevel,
                            endOfExpressionChar, -1);
                    operand = Operation.tenaryOperationFactory(operator, operand,
                            operand2, operand3);
                    operator = Operator.END;
                } else {
                    Object nextOperand = getOperand(nestingLevel);
                    Operator nextOperator = this.tokeniser.getOperator(endOfExpressionChar);
                    if (nextOperator == Operator.END) {
                        /* We are at the end of the expression */
                        operand = Operation.binaryOperationfactory(operator,
                                operand, nextOperand);
                        operator = Operator.END;
                        if (preReadOperator != null && endOfExpressionChar != 0) {
                            /* The bracket also terminates an earlier expression. */
                            this.tokeniser.pushBack(Operator.END);
                        }
                    } else if (nextOperator.precedence <= terminatePrecedence) {
                        /*
                         * The precedence of the following operator effectively
                         * brings this expression to an end.
                         */
                        operand = Operation.binaryOperationfactory(operator,
                                operand, nextOperand);
                        this.tokeniser.pushBack(nextOperator);
                        operator = Operator.END;
                    } else if (operator.precedence >= nextOperator.precedence) {
                        /* The current operator binds tighter than any following it */
                        operand = Operation.binaryOperationfactory(operator,
                                operand, nextOperand);
                        operator = nextOperator;
                    } else {
                        /*
                         * The following operator binds tighter so compile the
                         * following expression first.
                         */
                        operand = Operation.binaryOperationfactory(operator,
                                operand, compile(nextOperand, nextOperator,
                                        nestingLevel, endOfExpressionChar,
                                        operator.precedence));
                        operator = this.tokeniser.getOperator(endOfExpressionChar);
                        if (operator == Operator.END && preReadOperator != null && endOfExpressionChar != 0) {
                            /* The bracket also terminates an earlier expression. */
                            this.tokeniser.pushBack(Operator.END);
                        }
                    }
                }
            }
            return operand;
        }

        private Object getOperand(int nestingLevel) {
            Object operand = this.tokeniser.getOperand();
            if (operand == Tokeniser.START_NEW_EXPRESSION) {
                operand = compile(null, null, nestingLevel + 1, ')', -1);
            } else if (operand instanceof Operator) {
                /* Can get unary operators when expecting operand */
                return Operation.unaryOperationfactory((Operator) operand,
                        getOperand(nestingLevel));
            }
            return operand;
        }
    }
    /**
     * The root of the tree of arithmetic operations.
     */
    private final Operation rootOperation;

    /**
     * Construct an {@link Expression} that may be used multiple times to
     * evaluate the expression using different sets of variables. This holds the
     * results of parsing the expression to minimise further work.
     *
     * @param expression the arithmetic expression to be parsed.
     */
    public Eval(String expression) {
        this.rootOperation = new Compiler(expression).compile();
    }

    /**
     * Evaluate the expression with the given set of values.
     *
     * @param variables the values to use in the expression.
     * @return the result of the evaluation
     */
    public BigDecimal eval(Map<String, BigDecimal> variables) {
        return this.rootOperation.eval(variables);
    }

    /**
     * Evaluate the expression which does not reference any variables.
     *
     * @return the result of the evaluation
     */
    public BigDecimal eval() {
        return this.eval((Map<String, BigDecimal>) null);
    }

    /**
     * A convenience method that constructs an {@link Expression} and evaluates
     * it.
     *
     * @param expression the expression to evaluate.
     * @param variables the values to use in the evaluation.
     * @return the result of the evaluation
     */
    public static BigDecimal eval(String expression,
            Map<String, BigDecimal> variables) {
        return new Eval(expression).eval(variables);
    }

    /**
     * A convenience method that constructs an {@link Expression} that
     * references no variables and evaluates it.
     *
     * @param expression the expression to evaluate.
     * @return the result of the evaluation
     */
    public static BigDecimal eval(String expression) {
        return new Eval(expression).eval();
    }

    /**
     * Creates a string showing expression as it has been parsed.
     */
    @Override
    public String toString() {
        return this.rootOperation.toString();
    }
}
