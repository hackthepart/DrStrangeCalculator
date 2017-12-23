package com.example.android.calculator;

import android.os.Bundle;
import android.support.annotation.MainThread;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Toast;

import java.util.Stack;

public class postfix extends AppCompatActivity{
    // A utility function to return precedence of a given operator
    // Higher returned value means higher precedence
    static int Prec(char ch) {
        switch (ch) {
            case '+':
            case '-':
                return 1;

            case '*':
            case '/':
                return 2;

            case '^':
                return 3;
            case '(':
                return 4;
        }
        return -1;
    }

    // The main method that converts given infix expression
    // to postfix expression.
    public static String toPostfix(String originalExp) {
        String postfixExp = "";
        int x=0;
        char y=' ';
        Stack<Character> opStack = new Stack<>();
        for (char c : originalExp.toCharArray()) {
            if (c >= '0' && c <= '9'){
                postfixExp = postfixExp + c;
                if (y=='(')
            x++;}
            /*else if (c=='(')
            {
                postfixExp+='(';
                y='(';
            }*/
            else if (c==')')
            {
                y=' ';
                int i=1;
                postfixExp+=' ';
                while (!opStack.isEmpty()&&i<=x-1)
                {if(opStack.peek()=='('){opStack.pop();i--;}else postfixExp+=opStack.pop();i++;}
                x=0;
                postfixExp+=')';
                opStack.pop();
            }
            else if (isoperator(c)) {
                postfixExp = postfixExp + ' ';
                if (c=='('){y='(';
                    postfixExp+='(';}
                while (!opStack.isEmpty()&&Prec(c)<=Prec(opStack.peek())&&opStack.peek()!='(')
                {
                    postfixExp+=opStack.pop();
                }
                opStack.push(c);
                postfixExp+=' ';
            }
        }

        while (!opStack.empty()) {
            postfixExp += ' ';
            if (opStack.peek()=='(')
                opStack.pop();
            else
            postfixExp += opStack.pop();
            postfixExp += ' ';
        }
        //Toast.makeText(super.getBaseContext(), postfixExp,Toast.LENGTH_LONG).show();
        return postfixExp;
    }
    private static boolean isoperator(char c) {
        switch (c) {
            case '+':case '-':case '*':case '/':case '^':case '(':
                return true;
            default:
                return false;
        }
    }

}