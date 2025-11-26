/*
expressions.py — Infix <-> Postfix conversions (with file helpers)

- infix_to_postfix(expr: str) -> str
- postfix_to_infix(expr: str) -> str
Both handle unary minus by representing it as "~" in postfix form.

File helpers:
- convert_file(mode, in_path, out_path)
  mode in {"infix2postfix", "postfix2infix"}
  Reads one expression per line and writes converted form per line.
*/

// Expressions.java - Convert between infix and postfix
import java.util.ArrayList;
import java.util.Stack;
import java.io.*;

public class Expressions {

    // Check if character is operator
    private static boolean isOperator(String op) {
        return op.equals("+") || op.equals("-") || op.equals("*") ||
                op.equals("/") || op.equals("^") || op.equals("~");
    }

    // Get operator priority
    private static int getPriority(String op) {
        if (op.equals("~"))
            return 5; // unary minus
        if (op.equals("^"))
            return 4;
        if (op.equals("*") || op.equals("/"))
            return 3;
        if (op.equals("+") || op.equals("-"))
            return 2;
        return 0;
    }

    // Check if operator is right associative
    private static boolean isRightAssociative(String op) {
        return op.equals("^") || op.equals("~");
    }

    // Split expression into tokens (numbers, letters, operators)
    private static ArrayList<String> tokenize(String expr) {
        ArrayList<String> tokens = new ArrayList<>();
        String current = "";

        for (int i = 0; i < expr.length(); i++) {
            char ch = expr.charAt(i);

            if (ch == ' ' || ch == '\t') {
                // skip spaces
                continue;
            } else if (Character.isLetterOrDigit(ch) || ch == '_' || ch == '.') {
                // build number or variable name
                current += ch;
            } else {
                // operator or parenthesis
                if (!current.isEmpty()) {
                    tokens.add(current);
                    current = "";
                }
                tokens.add(String.valueOf(ch));
            }
        }
        if (!current.isEmpty()) {
            tokens.add(current);
        }
        return tokens;
    }

    // Convert INFIX to POSTFIX
    public static String infixToPostfix(String infix) {
        ArrayList<String> tokens = tokenize(infix);
        ArrayList<String> output = new ArrayList<>();
        Stack<String> stack = new Stack<>();
        String lastType = null;

        for (String token : tokens) {
            // Check if it's a number or variable
            if (Character.isLetterOrDigit(token.charAt(0))) {
                output.add(token);
                lastType = "OPERAND";
            }
            // Check for operators
            else if (token.equals("+") || token.equals("-") ||
                    token.equals("*") || token.equals("/") || token.equals("^")) {

                // Check if minus is unary (like -5 or -A)
                String op = token;
                if (token.equals("-") && (lastType == null ||
                        lastType.equals("OP") || lastType.equals("("))) {
                    op = "~"; // special symbol for unary minus
                }

                // Pop operators with higher priority
                while (!stack.isEmpty() && !stack.peek().equals("(")) {
                    String top = stack.peek();
                    int topPriority = getPriority(top);
                    int opPriority = getPriority(op);

                    if (topPriority > opPriority ||
                            (topPriority == opPriority && !isRightAssociative(op))) {
                        output.add(stack.pop());
                    } else {
                        break;
                    }
                }
                stack.push(op);
                lastType = "OP";
            }
            // Left parenthesis
            else if (token.equals("(")) {
                stack.push("(");
                lastType = "(";
            }
            // Right parenthesis
            else if (token.equals(")")) {
                while (!stack.isEmpty() && !stack.peek().equals("(")) {
                    output.add(stack.pop());
                }
                if (stack.isEmpty()) {
                    throw new RuntimeException("Mismatched parentheses");
                }
                stack.pop(); // remove '('
                lastType = ")";
            }
        }

        // Pop remaining operators
        while (!stack.isEmpty()) {
            if (stack.peek().equals("(") || stack.peek().equals(")")) {
                throw new RuntimeException("Mismatched parentheses");
            }
            output.add(stack.pop());
        }

        // Join with spaces
        String result = "";
        for (int i = 0; i < output.size(); i++) {
            result += output.get(i);
            if (i < output.size() - 1) {
                result += " ";
            }
        }
        return result;
    }

    // Helper class for building infix expression
    private static class ExprNode {
        String expr;
        int priority;
        int operandCount; // 0 for operand, 1 for unary, 2 for binary

        ExprNode(String expr, int priority, int operandCount) {
            this.expr = expr;
            this.priority = priority;
            this.operandCount = operandCount;
        }
    }

    // Add parentheses if needed
    private static String addParens(ExprNode child, int parentPriority,
            boolean isRightSide, String op) {
        if (child.operandCount == 0) {
            return child.expr; // operand, no parens needed
        }

        boolean needParens = false;
        if (child.priority < parentPriority) {
            needParens = true;
        } else if (child.priority == parentPriority) {
            if ((op.equals("-") || op.equals("/")) && isRightSide) {
                needParens = true;
            } else if (op.equals("^") && !isRightSide) {
                needParens = true;
            }
        }

        if (needParens) {
            return "(" + child.expr + ")";
        }
        return child.expr;
    }

    // Convert POSTFIX to INFIX
    public static String postfixToInfix(String postfix) {
        String[] tokens = postfix.split("\\s+");
        Stack<ExprNode> stack = new Stack<>();

        for (String token : tokens) {
            if (isOperator(token)) {
                if (token.equals("~")) {
                    // Unary minus
                    if (stack.isEmpty()) {
                        throw new RuntimeException("Not enough operands for ~");
                    }
                    ExprNode operand = stack.pop();
                    String expr = "-" + addParens(operand, getPriority(token), true, token);
                    stack.push(new ExprNode(expr, getPriority(token), 1));
                } else {
                    // Binary operator
                    if (stack.size() < 2) {
                        throw new RuntimeException("Not enough operands for " + token);
                    }
                    ExprNode right = stack.pop();
                    ExprNode left = stack.pop();
                    String leftExpr = addParens(left, getPriority(token), false, token);
                    String rightExpr = addParens(right, getPriority(token), true, token);
                    String expr = leftExpr + " " + token + " " + rightExpr;
                    stack.push(new ExprNode(expr, getPriority(token), 2));
                }
            } else {
                // Operand (number or variable)
                stack.push(new ExprNode(token, 100, 0));
            }
        }

        if (stack.size() != 1) {
            throw new RuntimeException("Invalid postfix expression");
        }
        return stack.pop().expr;
    }

    // Convert file (one expression per line)
    public static void convertFile(String mode, String inputFile, String outputFile) {
        try {
            BufferedReader reader = new BufferedReader(new FileReader(inputFile));
            BufferedWriter writer = new BufferedWriter(new FileWriter(outputFile));
            String line;
            int lineNum = 1;

            while ((line = reader.readLine()) != null) {
                line = line.trim();
                if (line.isEmpty()) {
                    writer.write("");
                    writer.newLine();
                    continue;
                }

                try {
                    String result;
                    if (mode.equals("infix2postfix")) {
                        result = infixToPostfix(line);
                    } else {
                        result = postfixToInfix(line);
                    }
                    writer.write(result);
                } catch (Exception e) {
                    writer.write("# Error on line " + lineNum + ": " + e.getMessage());
                }
                writer.newLine();
                lineNum++;
            }

            reader.close();
            writer.close();
        } catch (IOException e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    // Main - demo
    public static void main(String[] args) {
        System.out.println("Demo infix -> postfix");
        System.out.println("A * ( B + C ) - D / E -> " +
                infixToPostfix("A * ( B + C ) - D / E"));
        System.out.println("3 + 4 * 2 / ( 1 - 5 ) ^ 2 ^ 3 -> " +
                infixToPostfix("3 + 4 * 2 / ( 1 - 5 ) ^ 2 ^ 3"));
        System.out.println("-A + B -> " + infixToPostfix("-A + B"));

        System.out.println("\nDemo postfix -> infix");
        System.out.println("A B C + * D E / - -> " +
                postfixToInfix("A B C + * D E / -"));
        System.out.println("3 4 2 * 1 5 - 2 3 ^ ^ / + -> " +
                postfixToInfix("3 4 2 * 1 5 - 2 3 ^ ^ / +"));
        System.out.println("A ~ B + -> " + postfixToInfix("A ~ B +"));
    }
}