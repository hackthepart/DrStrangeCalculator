package com.example.android.calculator;

import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.IntentSender;
import android.content.ServiceConnection;
import android.content.SharedPreferences;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.content.res.AssetManager;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.database.DatabaseErrorHandler;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.hardware.Sensor;
import android.hardware.SensorManager;
import android.net.Uri;
import android.os.Handler;
import android.os.Looper;
import android.os.UserHandle;
import android.support.annotation.IntDef;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresPermission;
import android.support.annotation.StringDef;
import android.support.annotation.StyleRes;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Display;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import org.w3c.dom.Text;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Stack;

public class MainActivity extends AppCompatActivity {
    Button plus,minus,multiply,divide,modulus,power,log_X,mod_X,lBracket,rBracket,back,sqRoot;
    TextView input_et;
    TextView result_tv;
    double Int_of_a,res=0;
    String a="";
    boolean set=false;
    char operation='0';
    private SensorManager mSensorManager;
    private Sensor mAccelerometer;
    private ShakeDetector mShakeDetector;
    static Context context;
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
       //handling the shake event
        mSensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        mAccelerometer = mSensorManager
                .getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        mShakeDetector = new ShakeDetector();
        mShakeDetector.setOnShakeListener(new ShakeDetector.OnShakeListener() {

            @Override
            public void onShake(int count) {
                clearByShake();
                //Toast.makeText(MainActivity.this, "shaking!!!!!!!!", Toast.LENGTH_LONG).show();
            }
        });

    }
    @Override
    public void onResume() {
        super.onResume();
        mSensorManager.registerListener(mShakeDetector, mAccelerometer,	SensorManager.SENSOR_DELAY_UI);
    }

    @Override
    public void onPause() {
        mSensorManager.unregisterListener(mShakeDetector);
        super.onPause();
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
    //evaluating expression on button click
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
                    if (pos < str.length()) throw new RuntimeException("Unexpected: " + (char) ch);
                    return x;
                }

                double parseExpression() {
                    double x = parseTerm();
                    for (; ; ) {
                            if (eat('+')) x += parseTerm(); // addition
                            else if (eat('-')) x -= parseTerm(); // subtraction
                            else return x;

                    }
                }

                double parseTerm() {
                    double x = parseFactor();
                    for (; ; ) {
                            if (eat('*')) x *= parseFactor(); // multiplication
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
                            if (func.equals("sqrt")) x = Math.sqrt(x);//square root
                            else if (func.equals("abs")) x = Math.abs(x);//absolute value
                            else if (func.equals("log")) x = Math.log(x);// log value
                            else if (func.equals("exp")) x = Math.exp(x);// exponential value
                            else if (func.equals("sin")) x = Math.sin(Math.toRadians(x));// sine value
                            else if (func.equals("cos")) x = Math.cos(Math.toRadians(x));// cos value
                            else if (func.equals("tan")) x = Math.tan(Math.toRadians(x));// tan value
                            else throw new RuntimeException("Unknown function: " + func);
                        } else {
                            throw new RuntimeException("Unexpected: " + (char) ch);
                        }
                        if (eat('^')) x = Math.pow(x, parseFactor()); // exponentiation

                        return x;
                }
                }.parse();
            }
// method called when = button pressed
    public void display(View view){
            res = eval(a);
            a = "" + res;
            result_tv.setText(a);
            input_et.setText(a);
        }
    // clear result and input
    public void clear(View view){
        res=0;
        a="";
        result_tv.setText(""+res);
        input_et.setText(""+a);
    }

    public void clearByShake(){
        res=0;
        a="";
        set=false;
        operation= '0';
        result_tv.setText(""+res);
        input_et.setText(""+a);
    }
    // methods for creating mathematical expression as per clicks
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
        if(l>1){
            if (((a.charAt(l - 2)) == 'g') || ((a.charAt(l - 2)) == 'd') || (a.charAt(l - 2) == 's') || (a.charAt(l - 2) == 'n'))
                a = a.substring(0, l - 4);
            else if ((a.charAt(l - 2)) == 't')
                a = a.substring(0, l - 5);
            else
                a = a.substring(0, l - 1);
            input_et.setText("" + a);
        }
        if(l==1)
            input_et.setText("");
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
