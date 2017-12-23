package com.example.android.calculator;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import android.widget.TextView;






import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;




public class MainActivity extends AppCompatActivity {
    private SensorManager mSensorManager;
    private Sensor mAccelerometer;
    private ShakerMethod mShakeDetector;
Button plus,minus,multiply,divide;
public static  TextView input_et;
public static TextView result_tv;
    double res=0,a=0,previousAnswer=0;
   // boolean set=false;
public static char operation='0';
    public static String expression;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mSensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        mAccelerometer = mSensorManager
                .getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        mShakeDetector = new ShakerMethod();
        mShakeDetector.setOnShakeListener(new ShakerMethod.OnShakeListener() {

            @Override
            public void onShake(int count) {
                handleShakeEvent(count);
            }
        });
        expression="";
        setContentView(R.layout.activity_main);
        plus=(Button)findViewById(R.id.add);
        minus=(Button)findViewById(R.id.subtract);
        multiply=(Button)findViewById(R.id.multiply);
        divide=(Button)findViewById(R.id.divide);
        input_et=(TextView)findViewById(R.id.input_et);
        result_tv=(TextView)findViewById(R.id.result_tv);
    }
    public void handleShakeEvent(int count){
        input_et.setText("0.0");
        result_tv.setText("0.0");
        expression="";
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
    public void display(View view) throws ScriptException {
        String modifiedExpression = expression;
        for(int i=0;i<expression.length();i++){
            if(expression.charAt(i)=='^'){
               modifiedExpression=modifiedExpression.replace(modifiedExpression.substring(i-1,i+2),
                       "Math.pow("+modifiedExpression.charAt(i-1)+","+modifiedExpression.charAt(i+1)+")");
            }
        }
        switch(operation){
            case '+':
                res+=a;
                break;
            case '-':
                res-=a;
                break;
            case '*':
                res*=a;
                break;
            case '/':
                res/=a;
                break;
            case '^':
                res=Math.pow(res,a);
            default:
                break;
        }
        try {
            ScriptEngine engine = new ScriptEngineManager().getEngineByName("rhino");
            res = (double) engine.eval(modifiedExpression);
            a = res;
            result_tv.setText("" + res);
            input_et.setText("0.0");
        }
        catch (Exception e){
            return;
        }
    }

    public void addToMemory(View view){
        expression="";
        previousAnswer=res;
        result_tv.setText("Previous Answer "+previousAnswer);
        res=0;
        input_et.setText(Double.toString(res));
    }
    public void subtractFromMemory(View view){
        expression+= previousAnswer+" ";
        input_et.setText(expression);
    }


    public void power(View view){
        if(!(expression.charAt(expression.length()-1)=='^')) {
            res = a;
            result_tv.setText("" + res);
            a = 0;
            operation = '^';
            expression+=operation;

            input_et.setText(" " + expression);
        }

    }
    public void openBrace(View view){
        if(!(expression.charAt(expression.length()-1)=='(')) {
            res = a;
            result_tv.setText("" + res);
            //set=true;
            a = 0;
            expression+="(";
            input_et.setText(" " + expression);
        }

    }
    public void closeBrace(View view){
        if(!(expression.charAt(expression.length()-1)==')')) {
            res = a;
            result_tv.setText("" + res);
            //set=true;
            a = 0;
            expression+=")";
            input_et.setText(" " + expression);
        }

    }


    public void clear(View view){
        res=0;
        a=0;
        expression="";
        //set=false;
        operation= '0';
        result_tv.setText(""+res);
        input_et.setText(" "+expression);
    }
    public void add(View view){
        if(!(expression.charAt(expression.length()-1)=='+')) {
            res = a;
            result_tv.setText("" + res);
            //set=true;

            //  Toast.makeText(this,"Plus button clicked",Toast.LENGTH_SHORT).show();
            //  a=Float.parseFloat(input_et.getText().toString());
            a=0;
            operation = '+';
            expression+=operation;
            input_et.setText(" " + expression);
        }
    }
    public void subtract(View view){
       if(!(expression.charAt(expression.length()-1)=='-')) {
           res = a;
           result_tv.setText("" + res);
           //set=true;

           //  Toast.makeText(this,"Plus button clicked",Toast.LENGTH_SHORT).show();
           // a=Float.parseFloat(input_et.getText().toString());
           a = 0;
           operation = '-';
           expression+=operation;
           input_et.setText(" " + expression);
       }
    }
    public void multiply(View view){
        if(!(expression.charAt(expression.length()-1)=='*')) {
            res = a;
            result_tv.setText("" + res);
            //set=true;
            //  Toast.makeText(this,"Plus button clicked",Toast.LENGTH_SHORT).show()
            a = 0;
            operation = '*';
            expression+=operation;
            input_et.setText(" " + expression);
        }
    }
    public void divide(View view){
        if(!(expression.charAt(expression.length()-1)=='/')) {
            res = a;
            result_tv.setText("" + res);
            //set=true;
            //  Toast.makeText(this,"Plus button clicked",Toast.LENGTH_SHORT).show();
            //  a=Float.parseFloat(input_et.getText().toString());
            a = 0;
            operation = '/';
            expression+=operation;

            input_et.setText(" " + expression);
        }
    }
    public void value1(View view){
        //a*=10;
        a=1;
        expression+=(int)a;
        input_et.setText(" "+expression);
    }
    public void value2(View view){
        //a*=10;
        a=2;
        expression+=(int)a;

        input_et.setText(" "+expression);
    }
    public void value3(View view){
        //a*=10;
        a=3;
        expression+=(int)a;

        input_et.setText(" "+expression);
    }
    public void value4(View view){
        //a*=10;
        a=4;
        expression+=(int)a;

        input_et.setText(" "+expression);
    }
    public void value5(View view){
        //a*=10;
        a=5;
        expression+=(int)a;

        input_et.setText(" "+expression);
    }
    public void value6(View view){
        //a*=10;
        a=6;
        expression+=(int)a;

        input_et.setText(" "+expression);
    }
    public void value7(View view){
        //a*=10;
        a=7;
        expression+=(int)a;

        input_et.setText(" "+expression);
    }
    public void value8(View view){
        //a*=10;
        a=8;
        expression+=(int)a;

        input_et.setText(" "+expression);
    }
    public void value9(View view){
        //a*=10;
        a=9;
        expression+=(int)a;

        input_et.setText(" "+expression);
    }
    public void value0(View view){
        //a*=10;
        expression+='0';
        input_et.setText(" "+expression);
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


}
