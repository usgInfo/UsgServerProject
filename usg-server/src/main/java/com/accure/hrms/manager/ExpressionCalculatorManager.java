/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.accure.hrms.manager;

import java.util.Stack;
import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;

/**
 *
 * @author Asif
 */
public class ExpressionCalculatorManager {

    public static int evaluate(String expression) {
        char[] tokens = expression.toCharArray();

        // Stack for numbers: 'values'
        Stack<Integer> values = new Stack<Integer>();

        // Stack for Operators: 'ops'
        Stack<Character> ops = new Stack<Character>();

        for (int i = 0; i < tokens.length; i++) {
            // Current token is a whitespace, skip it
            if (tokens[i] == ' ') {
                continue;
            }

            // Current token is a number, push it to stack for numbers
            if (tokens[i] >= '0' && tokens[i] <= '9') {
                StringBuffer sbuf = new StringBuffer();
                // There may be more than one digits in number
                while (i < tokens.length && tokens[i] >= '0' && tokens[i] <= '9') {
                    sbuf.append(tokens[i++]);
                }
                values.push(Integer.parseInt(sbuf.toString()));
            } // Current token is an opening brace, push it to 'ops'
            else if (tokens[i] == '(') {
                ops.push(tokens[i]);
            } // Closing brace encountered, solve entire brace
            else if (tokens[i] == ')') {
                while (ops.peek() != '(') {
                    values.push(applyOp(ops.pop(), values.pop(), values.pop()));
                }
                ops.pop();
            } // Current token is an operator.
            else if (tokens[i] == '+' || tokens[i] == '-'
                    || tokens[i] == '*' || tokens[i] == '/') {
                // While top of 'ops' has same or greater precedence to current
                // token, which is an operator. Apply operator on top of 'ops'
                // to top two elements in values stack
                while (!ops.empty() && hasPrecedence(tokens[i], ops.peek())) {
                    values.push(applyOp(ops.pop(), values.pop(), values.pop()));
                }

                // Push current token to 'ops'.
                ops.push(tokens[i]);
            }
        }

        // Entire expression has been parsed at this point, apply remaining
        // ops to remaining values
        while (!ops.empty()) {
            values.push(applyOp(ops.pop(), values.pop(), values.pop()));
        }

        // Top of 'values' contains result, return it
        return values.pop();
    }

    // Returns true if 'op2' has higher or same precedence as 'op1',
    // otherwise returns false.
    public static boolean hasPrecedence(char op1, char op2) {
        if (op2 == '(' || op2 == ')') {
            return false;
        }
        if ((op1 == '*' || op1 == '/') && (op2 == '+' || op2 == '-')) {
            return false;
        } else {
            return true;
        }
    }

    // A utility method to apply an operator 'op' on operands 'a' 
    // and 'b'. Return the result.
    public static int applyOp(char op, int b, int a) {
        switch (op) {
            case '+':
                return a + b;
            case '-':
                return a - b;
            case '*':
                return a * b;
            case '/':
                if (b == 0) {
                    throw new UnsupportedOperationException("Cannot divide by zero");
                }
                return a / b;
        }
        return 0;
    }

    public static double calculateTheValue(String str) throws ScriptException {
        ScriptEngineManager mgr = new ScriptEngineManager();
        ScriptEngine engine = mgr.getEngineByName("JavaScript");
//        String foo = "10 + 2 * 0.5";
//        //System.out.println(engine.eval(str));
        Object ob = engine.eval(str);
        String st = ob.toString();
        double d = Double.parseDouble(st);
        return d;
    }
//     public static void main(String[] args) {
//        //System.out.println(ExpressionCalculatorManager.evaluate("10 + 2 * 0.5"));
//        //System.out.println(ExpressionCalculatorManager.evaluate("100 * 2 + 12"));
//        //System.out.println(ExpressionCalculatorManager.evaluate("100 * ( 2 + 12 )"));
//        //System.out.println(ExpressionCalculatorManager.evaluate("100 * ( 2 + 12 ) / 14"));
//    }

    public static double round(double num, double multi) {
//        double multi = (double) multipleOf;
        double dd = Math.floor((num + multi / 2) / multi) * multi;
//        return Math.floor((num + (double) multipleOf / 2) / multipleOf) * multipleOf;
        return dd;
    }

    public static void main(String[] args) throws Exception {
        //System.out.println(round(3005.26, 0.5));
//        //System.out.println("12 round to 6: " + round(13, 6));
//        //System.out.println("11 round to 7: " + round(11, 7));
//        //System.out.println("5 round to 2: " + round(5, 2));
//        //System.out.println("6.2 round to 2: " + round(6.2, 2));
//        ScriptEngineManager mgr = new ScriptEngineManager();
//        ScriptEngine engine = mgr.getEngineByName("JavaScript");
//        String foo = "10.867 + 2 * 0.5";
//        //System.out.println(engine.eval(foo));
//        Object ob = engine.eval(foo);
//        String st = ob.toString();
//        Double d = Double.parseDouble(st);
//        double roundOff = Math.round(d);
//        //System.out.println(roundOff);
    }
}
