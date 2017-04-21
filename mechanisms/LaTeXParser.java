package mechanisms;

import java.util.*;

@SuppressWarnings("SpellCheckingInspection")
public class LaTeXParser {
    interface Token {
    }

    enum Functions implements Token {
        SIN(1), COS(1), TAN(1), CSC(1), SEC(1), COT(1),
        ASIN(1), ACOS(1), ATAN(1), ACOT(1),
        SINH(1), COSH(1), TANH(1), CSCH(1), SECH(1), COTH(1),
        ASINH(1), ACOSH(1), ATANH(1), ACSCH(1), ASECH(1), ACOTH(1),
        LN(1), LOG(1), LOG2(1), LOGN(2), EXP(1), SQRT(1),
        ABS(1), FLOOR(1), CEIL(1), MOD_F(2);
        int args;

        Functions(int args) {
            this.args = args;
        }
    }

    enum Operators implements Token {
        ADD(Association.LEFT, 0, 2), SUBTRACT(Association.LEFT, 0, 2),
        MULTIPLY(Association.LEFT, 5, 2), DIVIDE(Association.LEFT, 5, 2), MOD_OP(Association.LEFT, 5, 2),
        POWER(Association.RIGHT, 10, 2), FACTORIAL(Association.LEFT, 10, 1);
        int precedence, args;

        enum Association {
            LEFT, RIGHT
        }

        Association assoc;

        Operators(Association assoc, int precedence, int args) {
            this.assoc = assoc;
            this.precedence = precedence;
            this.args = args;
        }
    }

    enum Constants implements Token {
        PI, MIN_PI,
        E, MIN_E
    }

    enum Others implements Token {
        COMMA, LEFT_PARENTHESIS, RIGHT_PARENTHESIS
    }

    static class Number implements Token {
        String number;

        Number(String number) {
            this.number = number;
        }

        @Override
        public String toString() {
            return number;
        }
    }

    static class Variable implements Token {
        String variable;

        Variable(String v) {
            variable = v;
        }

        @Override
        public String toString() {
            return variable;
        }
    }

    private static Map<String, Token> tokenMap = new HashMap<>();

    static {
        tokenMap.put("sin", Functions.SIN);
        tokenMap.put("cos", Functions.COS);
        tokenMap.put("tan", Functions.TAN);
        tokenMap.put("tg", Functions.TAN);
        tokenMap.put("csc", Functions.CSC);
        tokenMap.put("cosec", Functions.CSC);
        tokenMap.put("sec", Functions.SEC);
        tokenMap.put("ctan", Functions.COT);
        tokenMap.put("cot", Functions.COT);
        tokenMap.put("ctg", Functions.COT);

        tokenMap.put("asin", Functions.ASIN);
        tokenMap.put("arsin", Functions.ASIN);
        tokenMap.put("arcsin", Functions.ASIN);
        tokenMap.put("acos", Functions.ACOS);
        tokenMap.put("arcos", Functions.ACOS);
        tokenMap.put("arccos", Functions.ACOS);
        tokenMap.put("atan", Functions.ATAN);
        tokenMap.put("arctan", Functions.ATAN);
        tokenMap.put("atg", Functions.ATAN);
        tokenMap.put("arctg", Functions.ATAN);
        tokenMap.put("actan", Functions.ACOT);
        tokenMap.put("arctan", Functions.ACOT);
        tokenMap.put("actg", Functions.ACOT);
        tokenMap.put("arcctg", Functions.ACOT);
        tokenMap.put("acot", Functions.ACOT);
        tokenMap.put("arccot", Functions.ACOT);

        tokenMap.put("sinh", Functions.SINH);
        tokenMap.put("cosh", Functions.COSH);
        tokenMap.put("tanh", Functions.TANH);
        tokenMap.put("tgh", Functions.TANH);
        tokenMap.put("csch", Functions.CSCH);
        tokenMap.put("cosech", Functions.CSCH);
        tokenMap.put("sech", Functions.SECH);
        tokenMap.put("coth", Functions.COTH);
        tokenMap.put("ctanh", Functions.COTH);
        tokenMap.put("ctgh", Functions.COTH);

        tokenMap.put("asinh", Functions.ASINH);
        tokenMap.put("arsinh", Functions.ASINH);
        tokenMap.put("arcsinh", Functions.ASINH);
        tokenMap.put("acosh", Functions.ACOSH);
        tokenMap.put("arcosh", Functions.ACOSH);
        tokenMap.put("accosh", Functions.ACOSH);
        tokenMap.put("atanh", Functions.ATANH);
        tokenMap.put("arctanh", Functions.ATANH);
        tokenMap.put("atgh", Functions.ATANH);
        tokenMap.put("arctgh", Functions.ATANH);
        tokenMap.put("acsch", Functions.ACSCH);
        tokenMap.put("arcsch", Functions.ACSCH);
        tokenMap.put("arccsch", Functions.ACSCH);
        tokenMap.put("acosech", Functions.ACSCH);
        tokenMap.put("arcosech", Functions.ACSCH);
        tokenMap.put("arccosech", Functions.ACSCH);
        tokenMap.put("asech", Functions.ASECH);
        tokenMap.put("arsech", Functions.ASECH);
        tokenMap.put("arcsech", Functions.ASECH);
        tokenMap.put("acoth", Functions.ACOTH);
        tokenMap.put("arcoth", Functions.ACOTH);
        tokenMap.put("arccoth", Functions.ACOTH);
        tokenMap.put("actanh", Functions.ACOTH);
        tokenMap.put("arcctanh", Functions.ACOTH);
        tokenMap.put("actgh", Functions.ACOTH);
        tokenMap.put("arcctgh", Functions.ACOTH);

        tokenMap.put("ln", Functions.LN);
        tokenMap.put("log10", Functions.LOG);
        tokenMap.put("log2", Functions.LOG2);
        tokenMap.put("log", Functions.LOGN);
        tokenMap.put("exp", Functions.EXP);
        tokenMap.put("sqrt", Functions.SQRT);

        tokenMap.put("abs", Functions.ABS);
        tokenMap.put("floor", Functions.FLOOR);
        tokenMap.put("ceil", Functions.CEIL);
        tokenMap.put("mod", Functions.MOD_F);

        tokenMap.put("+", Operators.ADD);
        tokenMap.put("-", Operators.SUBTRACT);
        tokenMap.put("*", Operators.MULTIPLY);
        tokenMap.put("/", Operators.DIVIDE);
        tokenMap.put("^", Operators.POWER);
        tokenMap.put("!", Operators.FACTORIAL);
        tokenMap.put("#", Operators.MOD_OP);

        tokenMap.put("(", Others.LEFT_PARENTHESIS);
        tokenMap.put(")", Others.RIGHT_PARENTHESIS);
        tokenMap.put(",", Others.COMMA);

        tokenMap.put("pi", Constants.PI);
        tokenMap.put("-pi", Constants.MIN_PI);
        tokenMap.put("e", Constants.E);
        tokenMap.put("-e", Constants.MIN_E);
    }

    private static Stack<Token> operatorStack = new Stack<>();
    private static Stack<Token> postFix = new Stack<>();

    public static String parseToLaTeX(String expression) throws LaTeXParserException {
        postFix.clear();
        operatorStack.clear();
        parseToPostFix(expression);
        return parseTokens();
    }

    private static void parseToPostFix(String infix) throws LaTeXParserException {
        infix = infix.replace(" ", "");
        if (infix.isEmpty()) return;
        if (infix.contains(")(")) throw new LaTeXParserException("Add * between parentheses");
        List<String> tokens = tokenize(infix);
//        System.out.println(tokens);
        for (int i = 0; i < tokens.size() - 1; ++i) {
            String s = tokens.get(i);
            Token token = tokenMap.get(s);
            if (token == null) {
                try {
                    //noinspection ResultOfMethodCallIgnored
                    Double.parseDouble(s);
                    token = new Number(s);
                } catch (NumberFormatException e) {
                    if (s.length() == 1) {
                        if (Character.isLetter(s.charAt(0))) token = new Variable(s);
                        else throw new LaTeXParserException("Unexpected token: \"" + s + "\"");
                    } else {
                        if(s.charAt(0)=='-' && s.length()==2 && Character.isLetter(s.charAt(1))) {
                            token = new Variable(s);
                        } else throw new LaTeXParserException("Unexpected token: \"" + s + "\"");
                    }
                }
            }
            String s2 = tokens.get(i + 1);
            Token nextToken = tokenMap.get(s2);
            if (nextToken == null) {
                try {
                    //noinspection ResultOfMethodCallIgnored
                    Double.parseDouble(s2);
                    nextToken = new Number(s2);
                } catch (NumberFormatException e) {
                    if (s2.length() == 1) {
                        if (Character.isLetter(s2.charAt(0))) nextToken = new Variable(s2);
                        else throw new LaTeXParserException("Unexpected token: \"" + s2 + "\"");
                    } else {
                        if(s2.charAt(0)=='-' && s2.length()==2 && Character.isLetter(s2.charAt(1))) {
                            nextToken = new Variable(s2);
                        } else throw new LaTeXParserException("Unexpected token: \"" + s2 + "\"");
                    }
                }
            }
            if ((token instanceof Number || token instanceof Variable || token instanceof Constants)
                    && nextToken == Others.LEFT_PARENTHESIS)
                throw new LaTeXParserException("Add a '*' between an operand and a left parenthesis");
            if (token == Others.RIGHT_PARENTHESIS &&
                    (nextToken instanceof Number || nextToken instanceof Variable || nextToken instanceof Constants))
                throw new LaTeXParserException("Add a '*' between a right parenthesis and an operand");
        }
        for (String s : tokens) {
            Token token = tokenMap.get(s);
            if (token == null) {
                try {
                    //noinspection ResultOfMethodCallIgnored
                    Double.parseDouble(s);
                    postFix.push(new Number(s));
                } catch (NumberFormatException e) {
                    if (s.length() == 1) {
                        if (Character.isLetter(s.charAt(0))) postFix.push(new Variable(s));
                        else throw new LaTeXParserException("Unexpected token: \"" + s + "\"");
                    } else {
                        if(s.charAt(0)=='-' && s.length()==2 && Character.isLetter(s.charAt(1))) {
                            postFix.push(new Variable(s));
                        } else throw new LaTeXParserException("Unexpected token: \"" + s + "\"");
                    }
                }
            } else if (token instanceof Constants) {
                postFix.push(token);
            } else if (token instanceof Functions) {
                operatorStack.push(token);
            } else if (token instanceof Operators) {
                Operators op = (Operators) token;
                while (!operatorStack.empty() && operatorStack.peek() instanceof Operators) {
                    Operators op2 = (Operators) operatorStack.peek();
                    if (op.assoc == Operators.Association.LEFT && op.precedence <= op2.precedence)
                        postFix.push(operatorStack.pop());
                    else if (op.assoc == Operators.Association.RIGHT && op.precedence < op2.precedence)
                        postFix.push(operatorStack.pop());
                    else break;
                }
                operatorStack.push(op);
            } else if (token instanceof Others) {
                if (token == Others.COMMA) {
                    boolean leftParenthesisExists = false;
                    while (!operatorStack.empty()) {
                        if (operatorStack.peek() == Others.LEFT_PARENTHESIS) {
                            leftParenthesisExists = true;
                            break;
                        } else postFix.push(operatorStack.pop());
                    }
                    if (!leftParenthesisExists)
                        throw new LaTeXParserException("Misplaced comma (,) or mismatched parentheses");
                } else if (token == Others.LEFT_PARENTHESIS) {
                    operatorStack.push(token);
                } else if (token == Others.RIGHT_PARENTHESIS) {
                    boolean leftParenthesisExists = false;
                    while (!operatorStack.empty()) {
                        if (operatorStack.peek() == Others.LEFT_PARENTHESIS) {
                            leftParenthesisExists = true;
                            break;
                        } else postFix.push(operatorStack.pop());
                    }
                    if (!leftParenthesisExists)
                        throw new LaTeXParserException("Mismatched parentheses");
                    else {
                        postFix.push(operatorStack.pop());
                        postFix.push(Others.RIGHT_PARENTHESIS);
                        if (!operatorStack.empty() && operatorStack.peek() instanceof Functions)
                            postFix.push(operatorStack.pop());
                    }
                }
            }
        }
        while (!operatorStack.empty()) {
            if (operatorStack.peek() == Others.LEFT_PARENTHESIS)
                throw new LaTeXParserException("Mismatched parentheses");
            postFix.push(operatorStack.pop());
        }
    }

    private static String parseTokens() throws LaTeXParserException {
        return parseTokens(false);
    }

    private static String parseTokens(boolean ignoreParentheses) throws LaTeXParserException {
        if (postFix.isEmpty()) return "";
        Token token = postFix.pop();
        if (token instanceof Others) {
            postFix.pop();
            if (!ignoreParentheses) return "\\left(" + parseTokens() + "\\right)";
            else return parseTokens();
        } else if (token instanceof Number) {
            return ((Number) token).number;
        } else if (token instanceof Variable) {
            return ((Variable) token).variable;
        } else if (token instanceof Constants) {
            if (token == Constants.PI) return "\\pi";
            else if (token == Constants.MIN_PI) return "-\\pi";
            else if (token == Constants.E) return "e";
            else if (token == Constants.MIN_E) return "-e";
        } else if (token instanceof Operators) {
            if (((Operators) token).args == 2) {
                String operator = "";
                String right, left;
                switch ((Operators) token) {
                    case POWER:
                    case DIVIDE:
                        right = parseTokens(true);
                        left = parseTokens(true);
                        break;
                    default:
                        right = parseTokens();
                        left = parseTokens();
                }
                if ((left.isEmpty() || right.isEmpty() ))
                    throw new LaTeXParserException("Insufficient arguments for operator: " + token);
//                else if (right.isEmpty() && token == Operators.SUBTRACT)
//                    throw new LaTeXParserException("Insufficient arguments for operator: " + token);
                if (token == Operators.ADD) operator = "+";
                else if (token == Operators.SUBTRACT) operator = "-";
                else if (token == Operators.MULTIPLY) operator = "\\cdot ";
                else if (token == Operators.MOD_OP) operator = "\\bmod ";
                else if (token == Operators.POWER) operator = "^";
                else if (token == Operators.DIVIDE) {
                    return "\\frac{" + left + "}{" + right + "}";
                }
                return "{" + left + "}" + operator + "{" + right + "}";
            } else if (((Operators) token).args == 1) {
                if (token == Operators.FACTORIAL)
                    return parseTokens() + "!";
            }
        } else if (token instanceof Functions) {
            if (((Functions) token).args == 1) {
                switch ((Functions) token) {
                    case SIN:
                        return "\\sin" + parseTokens();
                    case COS:
                        return "\\cos" + parseTokens();
                    case TAN:
                        return "\\tan" + parseTokens();
                    case CSC:
                        return "\\csc" + parseTokens();
                    case SEC:
                        return "\\sec" + parseTokens();
                    case COT:
                        return "\\cot" + parseTokens();
                    case ASIN:
                        return "\\arcsin" + parseTokens();
                    case ACOS:
                        return "\\arccos" + parseTokens();
                    case ATAN:
                        return "\\arctan" + parseTokens();
                    case ACOT:
                        return "\\arccot" + parseTokens();
                    case SINH:
                        return "\\sinh" + parseTokens();
                    case COSH:
                        return "\\cosh" + parseTokens();
                    case TANH:
                        return "\\tanh" + parseTokens();
                    case CSCH:
                        return "\\csch" + parseTokens();
                    case SECH:
                        return "\\sech" + parseTokens();
                    case COTH:
                        return "\\coth" + parseTokens();
                    case ASINH:
                        return "\\arcsinh" + parseTokens();
                    case ACOSH:
                        return "\\arccosh" + parseTokens();
                    case ATANH:
                        return "\\arctanh" + parseTokens();
                    case ACSCH:
                        return "\\arccsch" + parseTokens();
                    case ASECH:
                        return "\\arcsech" + parseTokens();
                    case ACOTH:
                        return "\\arccoth" + parseTokens();
                    case LN:
                        return "\\ln" + parseTokens();
                    case LOG:
                        return "\\log" + parseTokens();
                    case LOG2:
                        return "\\log_{2}" + parseTokens();
                    case EXP:
                        return "\\exp" + parseTokens();
                    case SQRT:
                        return "\\sqrt {" + parseTokens(true) + "}";
                    case ABS:
                        return "\\left|" + parseTokens(true) + "\\right|";
                    case FLOOR:
                        return "\\left\\lfloor " + parseTokens(true) + "\\right\\rfloor ";
                    case CEIL:
                        return "\\left\\lceil " + parseTokens(true) + "\\right\\rceil ";
                }
            } else if (((Functions) token).args == 2) {
                switch ((Functions) token) {
                    case LOGN:
                        String right = parseTokens();
                        String left = parseTokens(true);
                        if (left.isEmpty() || right.isEmpty())
                            throw new LaTeXParserException("Insufficient arguments for log(a,b)");
                        return "\\log_{" + left + "}" + right;
                    case MOD_F:
                        String right2 = parseTokens(true);
                        String left2 = parseTokens(true);
                        if (left2.isEmpty() || right2.isEmpty())
                            throw new LaTeXParserException("Insufficient arguments for mod(a,b)");
                        return "\\bmod(" + left2 + "," + right2 + ")";
                }
            }
        } else {
            throw new LaTeXParserException("Unexpected token: " + token);
        }
        return "";
    }

    private static List<String> tokenize(String s) {
//        return Arrays.asList(s.split("(?=[-+*/^#(),!])|(?<=[^-+*/^#,!][-+*/^#,!])|(?<=[()])"));
        return Arrays.asList(s.split("(?=[-+*/^#(),!])|(?<=[^-+*/^#,!(][-+*/^#,!])|(?<=[()])"));
//        return Arrays.asList(s.split("(?<=[-+*/^#(),!])|(?=[-+*/^#(),!])"));
    }
}
