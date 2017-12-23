package com.example.android.calculator;

import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import com.google.android.gms.appindexing.Action;
import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.appindexing.Thing;
import com.google.android.gms.common.api.GoogleApiClient;
import java.util.Stack;

public class MainActivity extends AppCompatActivity {
    Button plus, minus, multiply, divide, power;
    TextView input_et;
    String str="";
    static String postfi;
    TextView result_tv;
    double a = 0, res = 0;
    boolean set = false;
    char operation = '0';
    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
    private GoogleApiClient client;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        plus = (Button) findViewById(R.id.add);
        minus = (Button) findViewById(R.id.subtract);
        multiply = (Button) findViewById(R.id.multiply);
        divide = (Button) findViewById(R.id.divide);
        power = (Button) findViewById(R.id.power);
        input_et = (TextView) findViewById(R.id.input_et);
        result_tv = (TextView) findViewById(R.id.result_tv);
        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client = new GoogleApiClient.Builder(this).addApi(AppIndex.API).build();
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

    public void display(View view) {
        Double xe =  EvaluateString.evaluate(str);
        Toast.makeText(MainActivity.this,postfi,Toast.LENGTH_LONG).show();
        result_tv.setText("" + xe);
    }

    public void clear(View view) {
        res = 0;
        a = 0;
        set = false;
        operation = '0';
        result_tv.setText("" + res);
        input_et.setText("");
        str="";
    }

    public void add(View view) {
        //  Toast.makeText(this,"Plus button clicked",Toast.LENGTH_SHORT).show();
        //  a=Float.parseFloat(input_et.getText().toString());
        operation = '+';
        str=str+operation;
        input_et.setText(str);
    }

    public void subtract(View view) {
        //  Toast.makeText(this,"Plus button clicked",Toast.LENGTH_SHORT).show();
        // a=Float.parseFloat(input_et.getText().toString());
        operation = '-';
        str=str+operation;
        input_et.setText(str);
    }

    public void multiply(View view) {
        //  Toast.makeText(this,"Plus button clicked",Toast.LENGTH_SHORT).show()
        operation = '*';
        str=str+operation;
        input_et.setText(str);
    }

    public void divide(View view) {
        //  Toast.makeText(this,"Plus button clicked",Toast.LENGTH_SHORT).show();
        //  a=Float.parseFloat(input_et.getText().toString());
        operation = '/';
        str=str+operation;
        input_et.setText(str );
    }

    public void power(View view) {
        operation = '^';
        str=str+operation;
        input_et.setText(str);
    }

    public void value1(View view) {
        str=str+"1";
        input_et.setText(str);
    }

    public void value2(View view) {
        str=str+"2";
        input_et.setText(str);
    }

    public void value3(View view) {
        str=str+"3";
        input_et.setText(str);
    }

    public void value4(View view) {
        str=str+"4";
        input_et.setText(str);
    }

    public void value5(View view) {
        str=str+"5";
        input_et.setText(str);
    }

    public void value6(View view) {
        str=str+"6";
        input_et.setText(str);
    }

    public void value7(View view) {
        str=str+"7";
        input_et.setText(str);
    }

    public void value8(View view) {
        str=str+"8";
        input_et.setText(str);
    }

    public void value9(View view) {
        str=str+"9";
        input_et.setText(str);
    }

    public void value0(View view) {
        str=str+"0";
        input_et.setText(str);
    }
    public void lbrace(View view)
    {
        str=str+"(";
        input_et.setText(str);
    }
    public void rbrace(View view)
    {
        str=str+")";
        input_et.setText(str);
    }
    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
    public Action getIndexApiAction() {
        Thing object = new Thing.Builder()
                .setName("Main Page") // TODO: Define a title for the content shown.
                // TODO: Make sure this auto-generated URL is correct.
                .setUrl(Uri.parse("http://[ENTER-YOUR-URL-HERE]"))
                .build();
        return new Action.Builder(Action.TYPE_VIEW)
                .setObject(object)
                .setActionStatus(Action.STATUS_TYPE_COMPLETED)
                .build();
    }

    @Override
    public void onStart() {
        super.onStart();

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client.connect();
        AppIndex.AppIndexApi.start(client, getIndexApiAction());
    }

    @Override
    public void onStop() {
        super.onStop();

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        AppIndex.AppIndexApi.end(client, getIndexApiAction());
        client.disconnect();
    }

    public static class EvaluateString {
        public static double evaluate(String expression) {
            postfi = postfix.toPostfix(expression);
            char[] tokens = postfi.toCharArray();

            // Stack for numbers: 'values'
            Stack<Double> values = new Stack<Double>();
            for (int i = 0; i < tokens.length; i++) {
                // Current token is a whitespace, skip it
                if (tokens[i] == ' ')
                    continue;
                if (tokens[i]=='(')
                {int j;
                    for (j=i+1;j<tokens.length&&tokens[j]!=')';j++){
                // Current token is a number, push it to stack for numbers
                if (tokens[j]==' ')
                {
                    continue;
                }
                    if (tokens[j] >= '0' && tokens[j] <= '9') {
                    StringBuffer sbuf = new StringBuffer();
                    // There may be more than one digits in number
                    while (j < tokens.length && tokens[j] >= '0' && tokens[j] <= '9')
                        sbuf.append(tokens[j++]);
                    values.push(Double.parseDouble(sbuf.toString()));
                }
else
                {
                    values.push(applyOp(tokens[j],values.pop(),values.pop()));
                }
                }
                    i=j;
                }
                else if (tokens[i] >= '0' && tokens[i] <= '9') {
                    StringBuffer sbuf = new StringBuffer();
                    // There may be more than one digits in number
                    while (i < tokens.length && tokens[i] >= '0' && tokens[i] <= '9')
                        sbuf.append(tokens[i++]);
                    values.push(Double.parseDouble(sbuf.toString()));
                }
                else
                {
                    values.push(applyOp(tokens[i],values.pop(),values.pop()));
                }
                // Top of 'values' contains result, return it\

            }
            return values.pop();
        }

        // A utility method to apply an operator 'op' on operands 'a'
        // and 'b'. Return the result.
        public static double applyOp(char op, double b, double a) {
            switch (op) {
                case '+':
                    return a + b;
                case '-':
                    return a - b;
                case '*':
                    return a * b;
                case '/':
                    return a / b;
                case '^':
                    int i;double x=1;
                    for (i=1;i<=b;i++)
                        x=x*a;
                    return x;
            }
            return 0;
        }
    }
}
