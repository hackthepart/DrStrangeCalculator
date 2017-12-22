package com.example.android.calculator;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import org.w3c.dom.Text;

public class MainActivity extends AppCompatActivity {
Button plus,minus,multiply,divide,modulus,power,log_X,mod_X,lBracket,rBracket,back,sqRoot;
TextView input_et;
TextView result_tv;
double Int_of_a,res=0;
    String a="";
    boolean set=false;
char operation='0';
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        plus=(Button)findViewById(R.id.add);
        minus=(Button)findViewById(R.id.subtract);
        multiply=(Button)findViewById(R.id.multiply);
        divide=(Button)findViewById(R.id.divide);
        input_et=(TextView)findViewById(R.id.input_et);
        result_tv=(TextView)findViewById(R.id.result_tv);
        modulus=(Button)findViewById(R.id.modulus);
        power=(Button)findViewById(R.id.power);
        log_X=(Button)findViewById(R.id.log_X);
        mod_X=(Button)findViewById(R.id.absolute);
        lBracket=(Button)findViewById(R.id.openBracket);
        rBracket=(Button)findViewById(R.id.closeBracket);
        back=(Button)findViewById(R.id.back);
        sqRoot=(Button)findViewById(R.id.root);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
        public static double eval(final String str) {
            return new Object() {
                int pos = -1, ch;

                void nextChar() {
                    ch = (++pos < str.length()) ? str.charAt(pos) : -1;
                }
                boolean eat(int charToEat) {
                    while (ch == ' ') nextChar();
                    if (ch == charToEat) {
                        nextChar();
                        return true;
                    }
                    return false;
                }
                double parse() {
                    nextChar();
                    double x = parseExpression();
                    if (pos < str.length()) throw new RuntimeException("Unexpected: " + (char)ch);
                    return x;
                }

                // Grammar:
                // expression = term | expression `+` term | expression `-` term
                // term = factor | term `*` factor | term `/` factor
                // factor = `+` factor | `-` factor | `(` expression `)`
                //        | number | functionName factor | factor `^` factor

                double parseExpression() {
                    double x = parseTerm();
                    for (;;) {
                        if      (eat('+')) x += parseTerm(); // addition
                        else if (eat('-')) x -= parseTerm(); // subtraction
                        else return x;
                    }
                }

                double parseTerm() {
                    double x = parseFactor();
                    for (;;) {
                        if      (eat('*')) x *= parseFactor(); // multiplication
                        else if (eat('/')) x /= parseFactor();// division
                        else if (eat('%')) x %= parseFactor();
                        else return x;
                    }
                }

                double parseFactor() {
                    if (eat('+')) return parseFactor(); // unary plus
                    if (eat('-')) return -parseFactor(); // unary minus

                    double x;
                    int startPos = this.pos;
                    if (eat('(')) { // parentheses
                        x = parseExpression();
                        eat(')');
                    } else if ((ch >= '0' && ch <= '9') || ch == '.') { // numbers
                        while ((ch >= '0' && ch <= '9') || ch == '.') nextChar();
                        x = Double.parseDouble(str.substring(startPos, this.pos));
                    } else if (ch >= 'a' && ch <= 'z') { // functions
                        while (ch >= 'a' && ch <= 'z') nextChar();
                        String func = str.substring(startPos, this.pos);
                        x = parseFactor();
                        if (func.equals("sqrt")) x = Math.sqrt(x);
                        else if (func.equals("abs")) x = Math.abs(x);
                        else if (func.equals("log")) x = Math.log(x);
                        else if (func.equals("exp")) x = Math.exp(x);
                        else if (func.equals("sin")) x = Math.sin(Math.toRadians(x));
                        else if (func.equals("cos")) x = Math.cos(Math.toRadians(x));
                        else if (func.equals("tan")) x = Math.tan(Math.toRadians(x));
                        else throw new RuntimeException("Unknown function: " + func);
                    } else {
                        throw new RuntimeException("Unexpected: " + (char)ch);
                    }

                    if (eat('^')) x = Math.pow(x, parseFactor()); // exponentiation

                    return x;
                }
            }.parse();
        }

    public void display(View view){
        res = eval(a);
        a=""+res;
        result_tv.setText(a);
        input_et.setText(a);
    }
    public void clear(View view){
        res=0;
        a="";
        set=false;
        operation= '0';
        result_tv.setText(""+res);
        input_et.setText(""+a);
    }
    public void decimal(View view){
        a=a+".";
        input_et.setText(""+a);
    }
    public void add(View view){
        a=a+"+";
        input_et.setText(""+a);
    }
    public void subtract(View view){
        a=a+"-";
        input_et.setText(""+a);
    }
    public void multiply(View view){
        a=a+"*";
        input_et.setText(""+a);
    }
    public void divide(View view){
        a=a+"/";
        input_et.setText(""+a);
    }
    public void power(View view){
        a=a+"^";
        input_et.setText(""+a);
    }
    public void root(View view){
        a=a+"sqrt(";
        input_et.setText(""+a);
    }
    public void openBracket(View view){
        a=a+"(";
        input_et.setText(""+a);
    }
    public void cosine(View view){
        a=a+"cos(";
        input_et.setText(""+a);
    }
    public void sine(View view){
        a=a+"sin(";
        input_et.setText(""+a);
    }
    public void tangent(View view){
        a=a+"tan(";
        input_et.setText(""+a);
    }
    public void exponent(View view){
        a=a+"exp(";
        input_et.setText(""+a);
    }
    public void closeBracket(View view){
        a=a+")";
        input_et.setText(""+a);
    }
    public void back(View view){
        int l=a.length();
        try {
            if (((a.charAt(l - 2)) == 'g') || ((a.charAt(l - 2)) == 'd') || (a.charAt(l - 2) == 's') || (a.charAt(l - 2) == 'n'))
                a = a.substring(0, l - 4);
            else if ((a.charAt(l - 2)) == 't')
                a = a.substring(0, l - 5);
            else
                a = a.substring(0, l - 1);
            input_et.setText("" + a);
        }
        catch (Exception e){

        }
    }
    public void modulus(View view){
        a=a+"%";
        input_et.setText(""+a);
    }
    public void log_X(View view){
        a=a+"log(";
        input_et.setText(""+a);
    }
    public void absolute(View view){
        a=a+"abs(";
        input_et.setText(""+a);
    }
    public void value1(View view){
        a=a+"1";
        input_et.setText(""+a);
    }
    public void value2(View view){
        a=a+"2";
        input_et.setText(""+a);
    }
    public void value3(View view){
        a=a+"3";
        input_et.setText(""+a);
    }
    public void value4(View view){
        a=a+"4";
        input_et.setText(""+a);
    }
    public void value5(View view){
        a=a+"5";
        input_et.setText(""+a);
    }
    public void value6(View view){
        a=a+"6";
        input_et.setText(""+a);
    }
    public void value7(View view){
        a=a+"7";
        input_et.setText(""+a);
    }
    public void value8(View view){
        a=a+"8";
        input_et.setText(""+a);
    }
    public void value9(View view){
        a=a+"9";
        input_et.setText(""+a);
    }
    public void value0(View view){
        a=a+"0";
        input_et.setText(""+a);
    }


}
