package com.example.android.calculator;

import android.content.Context;
import android.content.res.Configuration;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.udojava.evalex.Expression;
import java.text.DecimalFormat;

public class MainActivity extends AppCompatActivity {
    Button plus,minus,multiply,divide,mod,power,bOpen,bClose;
    TextView input_et;
    TextView result_tv;
    double a=0,res=0;
    boolean brackets=false;

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        if(newConfig.orientation==Configuration.ORIENTATION_LANDSCAPE) {
            setContentView(R.layout.activity_main_land);
        }else if(newConfig.orientation==Configuration.ORIENTATION_PORTRAIT){
            setContentView(R.layout.activity_main);
        }
        getIds();
    }

    int countBrac =0;
    char operation='0';
    char prevopr='0';
    boolean decSet=false;
    double mem=0;
    double decFact=1;
    String prevRes;
    DecimalFormat decimalFormat;

    private float lastX=0;
    private float lastY=0;
    private float lastZ=0;
    private int directioncounter=0;

    SensorManager sensorManager;

    private static  int MIN_DIRECTION_CHANGE=12; // Minimum number of direction changes to be counted as a proper shake
    private static  int MIN_FORCE=9;

    private float currentAcc;
    private float prevAcc;
    private float shake;

    SensorEventListener sensorEventListener=new SensorEventListener() {
        @Override
        public void onSensorChanged(SensorEvent sensorEvent) {
            float x = sensorEvent.values[0];
            float y = sensorEvent.values[1];
            float z = sensorEvent.values[2];

            //Calculate Movement
            float change_in_position = Math.abs(x + y + z - (lastX + lastY + lastZ));

            if (change_in_position > MIN_FORCE) {

                directioncounter++;

                lastX = x;
                lastY = y;
                lastZ = z;

                if (directioncounter >= MIN_DIRECTION_CHANGE) {
                    clearAll();
                    resetShakeParameters();
                }
            }
        }

        @Override
        public void onAccuracyChanged(Sensor sensor, int i) {

        }
    };

    private void getIds(){
        plus= (Button)findViewById(R.id.add);
        minus= (Button)findViewById(R.id.subtract);
        multiply= (Button)findViewById(R.id.multiply);
        divide= (Button)findViewById(R.id.divide);
        mod= (Button) findViewById(R.id.mod);
        power= (Button) findViewById(R.id.power);
        bOpen= (Button) findViewById(R.id.bracOpen);
        bClose= (Button) findViewById(R.id.bracClose);
        input_et= (TextView)findViewById(R.id.input_et);
        result_tv= (TextView)findViewById(R.id.result_tv);

        decimalFormat=new DecimalFormat("#.#################");

        sensorManager=(SensorManager)this.getSystemService(Context.SENSOR_SERVICE);
        sensorManager.registerListener(sensorEventListener,sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER),
                SensorManager.SENSOR_DELAY_NORMAL);

        currentAcc=SensorManager.GRAVITY_EARTH;
        prevAcc=SensorManager.GRAVITY_EARTH;
        shake=0.00f;
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getIds();
    }

    @Override
    protected void onResume() {
        super.onResume();
        sensorManager.registerListener(sensorEventListener,
                sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER),
                SensorManager.SENSOR_DELAY_UI);
    }

    @Override
    protected void onPause() {
        sensorManager.unregisterListener(sensorEventListener);
        super.onPause();
        finish();
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

        if(!brackets) {
            performOperation(operation);
        }else{
            if(countBrac >0 && a>0){
                if(isNumInt(a))
                    result_tv.append(" "+decimalFormat.format(a)+" )");
                else
                    result_tv.append(" "+a+" )");
                a=0;
                resetDecimal();
                input_et.setText("0");
                countBrac--;
            }
            for(int i = 0; i< countBrac; ++i)
                result_tv.append(")");

            String str=result_tv.getText().toString();
            str=str.replace("√","SQRT");
            Log.e("String is",str);
            Expression expr=new Expression(str);

            try {
                res = expr.eval().doubleValue();
            }catch(Expression.ExpressionException e){
                Toast.makeText(this,"Invalid Input",Toast.LENGTH_SHORT).show();
                //clearAll();
            }
            brackets=false;
        }
        operation = '0';
        prevopr = '0';
        prevRes="0";
        a = 0;
        resetDecimal();
        input_et.setText("0");
        if(isNumInt(res))
            result_tv.setText(decimalFormat.format(res));
        else
            result_tv.setText(""+decimalFormat.format(res));
    }

    public void clear(View view){
        clearAll();
    }
    public void add(View view){
        prevopr = operation;
        prevRes=result_tv.getText().toString();
        operation = '+';
        String str=result_tv.getText().toString();
        if(a==0&&(str.charAt(str.length()-1)=='/'||str.charAt(str.length()-1)=='*'||str.charAt(str.length()-1)=='%')){
            result_tv.append(" (+");
            brackets=true;
        }
        else if(!brackets) {
            if (prevopr != '0') {
                performOperation(prevopr);
            } else {
                if(res==0)
                    res = Double.parseDouble(input_et.getText().toString());
                else if(a!=0)
                    res=a;
            }
            //  Toast.makeText(this,"Plus button clicked",Toast.LENGTH_SHORT).show();
            //  a=Float.parseFloat(input_et.getText().toString());
            if(isNumInt(res))
                result_tv.setText(decimalFormat.format(res)+" "+operation);
            else
                result_tv.setText(""+decimalFormat.format(res)+" "+operation);
        }else{
            if(isNumInt(a))
                result_tv.append(decimalFormat.format(a)+" +");
            else
                result_tv.append(""+a+" +");
        }
        input_et.setText("0");
        a = 0;
        resetDecimal();
    }


    public void subtract(View view){
        prevopr = operation;
        operation = '-';
        prevRes=result_tv.getText().toString();
        String str=result_tv.getText().toString();
        if(a==0&&(str.charAt(str.length()-1)=='/'||str.charAt(str.length()-1)=='*'||str.charAt(str.length()-1)=='%')){
            result_tv.append(" (-");
            brackets=true;
        }
        else if(!brackets) {
            if (prevopr != '0') {
                performOperation(prevopr);
            } else {
                if(res==0)
                    res = Double.parseDouble(input_et.getText().toString());
                else if(a!=0)
                    res=a;
            }
            //  Toast.makeText(this,"Plus button clicked",Toast.LENGTH_SHORT).show();
            // a=Float.parseFloat(input_et.getText().toString());
            if(isNumInt(res))
                result_tv.setText(decimalFormat.format(res)+" "+operation);
            else
                result_tv.setText(""+res+" "+operation);
        }else{
            if(isNumInt(a))
                result_tv.append(decimalFormat.format(a)+" -");
            else
                result_tv.append(""+a+" -");
        }
        a = 0;
        input_et.setText("0");
        resetDecimal();
    }
    public void multiply(View view){
        prevopr = operation;
        operation = '*';
        prevRes=result_tv.getText().toString();
        if(!brackets) {
            if (prevopr != '0') {
                performOperation(prevopr);
            } else {
                if(res==0)
                    res = Double.parseDouble(input_et.getText().toString());
                else if(a!=0)
                    res=a;
            }
            //  Toast.makeText(this,"Plus button clicked",Toast.LENGTH_SHORT).show()
            if(isNumInt(res))
                result_tv.setText(decimalFormat.format(res)+" "+operation);
            else
                result_tv.setText(""+res+" "+operation);
        }else{
            if(isNumInt(a))
                result_tv.append(decimalFormat.format(a)+" *");
            else
                result_tv.append(""+a+" *");
        }
        a = 0;
        input_et.setText("0");
        resetDecimal();
    }
    public void divide(View view){
        prevopr = operation;
        operation = '/';
        prevRes=result_tv.getText().toString();
        if(!brackets) {
            if (prevopr != '0') {
                performOperation(prevopr);
            } else {
                if(res==0)
                    res = Double.parseDouble(input_et.getText().toString());
                else if(a!=0)
                    res=a;
            }
            //  Toast.makeText(this,"Plus button clicked",Toast.LENGTH_SHORT).show();
            //  a=Float.parseFloat(input_et.getText().toString());
            if(isNumInt(res))
                result_tv.setText(decimalFormat.format(res)+" "+operation);
            else
                result_tv.setText(""+res+" "+operation);
        }else{
            if(isNumInt(a))
                result_tv.append(decimalFormat.format(a)+" +");
            else
                result_tv.append(""+a+" /");
        }
        a = 0;
        input_et.setText("0");
        resetDecimal();
    }

    public void modulo(View view) {
        prevopr = operation;
        operation = '%';
        prevRes=result_tv.getText().toString();
        if(!brackets) {
            if (prevopr != '0') {
                performOperation(prevopr);
            } else {
                if(res==0)
                    res = Double.parseDouble(input_et.getText().toString());
                else if(a!=0)
                    res=a;
            }
            //  Toast.makeText(this,"Plus button clicked",Toast.LENGTH_SHORT).show();
            //  a=Float.parseFloat(input_et.getText().toString());
            if(isNumInt(res))
                result_tv.setText(decimalFormat.format(res)+" "+operation);
            else
                result_tv.setText(""+res+" "+operation);
        }else{
            if(isNumInt(a))
                result_tv.append(decimalFormat.format(a)+" +");
            else
                result_tv.append(""+a+" %");
        }
        a = 0;
        input_et.setText("0");
        resetDecimal();
    }

    public void power(View view) {
        prevopr = operation;
        operation = '^';
        prevRes=result_tv.getText().toString();
        if(!brackets) {
            if (prevopr != '0') {
                performOperation(prevopr);
            } else {
                if(res==0)
                    res = Double.parseDouble(input_et.getText().toString());
                else if(a!=0)
                    res=a;
            }
            //  Toast.makeText(this,"Plus button clicked",Toast.LENGTH_SHORT).show();
            //  a=Float.parseFloat(input_et.getText().toString());
            if(isNumInt(res))
                result_tv.setText(decimalFormat.format(res)+" "+operation);
            else
                result_tv.setText(""+res+" "+operation);
        }else{
            if(isNumInt(a))
                result_tv.append(decimalFormat.format(a)+" +");
            else
                result_tv.append(""+a+" ^");
        }
        a = 0;
        input_et.setText("0");
        resetDecimal();
    }

    public void decimal(View view) {
        decSet=true;
        input_et.append(".");
    }

    public void sqroot(View view) {
        prevRes=result_tv.getText().toString();
        if(result_tv.getText().toString().equals("0")||operation=='0')
            result_tv.setText("√(");
        else
            result_tv.append("√(");
        brackets=true;
        countBrac++;
    }

    public void storeMem(View view) {
        mem=res;
    }

    public void retMem(View view) {
        /*if(a==0){
            a=mem;
            displayNum();
        }else{
            a=mem;
            if(isNumInt(a))
                input_et.append(decimalFormat.format(a));
            else
                input_et.append(""+a);
        }*/
        a=mem;
        input_et.setText(""+a);
    }

    public void bracOpened(View view) {
        prevRes=result_tv.getText().toString();
        result_tv.append(" (");
        brackets=true;
        countBrac++;
    }

    public void bracClosed(View view) {
        prevRes=result_tv.getText().toString();
        if(a!=0) {
            if (isNumInt(a))
                result_tv.append(" " + decimalFormat.format(a) + " )");
            else
                result_tv.append(" " + a + " )");
        }else{
            result_tv.append(" )");
        }
        a=0;
        resetDecimal();
        input_et.setText("0");
        countBrac--;
    }

    public void value1(View view){
        if(!decSet) {
            a *= 10;
            a += 1;
        }else{
            decFact*=10;
            a+=(1/decFact);
        }
        displayNum();
    }
    public void value2(View view){
        if(!decSet) {
            a *= 10;
            a += 2;
        }else{
            decFact*=10;
            a+=(2/decFact);
        }
        displayNum();
    }
    public void value3(View view){
        if(!decSet) {
            a *= 10;
            a += 3;
        }else{
            decFact*=10;
            a+=(3/decFact);
        }
        displayNum();
    }
    public void value4(View view){
        if(!decSet) {
            a *= 10;
            a += 4;
        }else{
            decFact*=10;
            a+=(4/decFact);
        }
        displayNum();
    }
    public void value5(View view){
        if(!decSet) {
            a *= 10;
            a += 5;
        }else{
            decFact*=10;
            a+=(5/decFact);
        }
        displayNum();
    }
    public void value6(View view){
        if(!decSet) {
            a *= 10;
            a += 6;
        }else{
            decFact*=10;
            a+=(6/decFact);
        }
        displayNum();
    }
    public void value7(View view) {
        if(!decSet) {
            a *= 10;
            a += 7;
        }else{
            decFact*=10;
            a+=(7/decFact);
        }
        displayNum();
    }
    public void value8(View view){
        if(!decSet) {
            a *= 10;
            a += 8;
        }else{
            decFact*=10;
            a+=(8/decFact);
        }
        displayNum();
    }
    public void value9(View view){
        if(!decSet) {
            a *= 10;
            a += 9;
        }else{
            decFact*=10;
            a+=(9/decFact);
        }
        displayNum();
    }
    public void value0(View view){
        if(!decSet) {
            a *= 10;
        }else{
            decFact*=10;
        }
        displayNum();
    }

    private void performOperation(char opr) {
        switch(opr){
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
                try {
                    res /= a;
                }catch(ArithmeticException e){
                    ;
                }
                break;
            case '%':
                try {
                    res %= a;
                }catch(ArithmeticException e){
                    ;
                }
                break;
            case '^':
                if(res!=0){
                    res=Math.pow(res,a);
                }else{
                    clearAll();
                }
                break;
            default:
                break;
        }
    }

    private void clearAll() {
        res=0;
        a=0;
        decSet=false;
        decFact=1;
        operation= '0';
        prevopr='0';
        result_tv.setText("0");
        input_et.setText("0");
        brackets=false;
    }

    public boolean isNumInt(double n)
    {
        if(n-(int)n==0)
            return true;
        else
            return false;
    }

    private void displayNum(){
        if(isNumInt(a))
            input_et.setText(decimalFormat.format(a));
        else
            input_et.setText(""+a);
    }

    private void resetDecimal(){
        decSet=false;
        decFact=1;
    }

    public void back(View view) {
        if(a!=0){
            String str=input_et.getText().toString();
            if(str.length()==1){
                input_et.setText("0");
                a=0;
            }else {
                char ch=str.charAt(str.length()-1);
                str = str.substring(0, str.length() - 1);
                input_et.setText(str);
                a = Double.parseDouble(str);
                if(ch=='.'){
                    resetDecimal();
                }
            }
        }else{
            if(!result_tv.getText().toString().equals("0")) {
                result_tv.setText(prevRes);
                String str=result_tv.getText().toString();
                char ch=str.charAt(str.length()-1);
                prevRes="0";
                if(ch=='(')
                    countBrac--;
                else if(ch==')')
                    countBrac++;
            }else{
                res=0;
                prevRes="0";
            }

            if(countBrac ==1){
                countBrac =0;
                brackets=false;
            }
        }
    }

    private void resetShakeParameters(){
        lastX=0;
        lastY=0;
        lastZ=0;
        directioncounter=0;
    }
}
