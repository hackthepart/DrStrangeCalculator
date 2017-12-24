package com.example.android.calculator;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
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
    Button plus, minus, multiply, divide;
    TextView input_et;
    TextView result_tv;
    double a = 0, res = 0;
    boolean set = false;
    char operation = '0';

    SensorManager mSensorManager;
    SensorEventListener mSensorListener;
    float mAcceleration = 0.0f, mAccelerationCurrent = 0.0f, mAccelerationLast = 0.0f;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        plus = (Button) findViewById(R.id.add);
        minus = (Button) findViewById(R.id.subtract);
        multiply = (Button) findViewById(R.id.multiply);
        divide = (Button) findViewById(R.id.divide);
        input_et = (TextView) findViewById(R.id.input_et);
        result_tv = (TextView) findViewById(R.id.result_tv);

        mSensorManager = (SensorManager) this.getSystemService(Context.SENSOR_SERVICE);
        mAcceleration = 0.0f;
        mAccelerationCurrent = SensorManager.GRAVITY_EARTH;
        mAccelerationLast = SensorManager.GRAVITY_EARTH;

        bindShakeToClearListener();
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

    @Override
    protected void onResume() {
        super.onResume();
        mSensorManager.registerListener(mSensorListener, mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER), SensorManager.SENSOR_DELAY_NORMAL);
    }

    @Override
    protected void onPause() {
        super.onPause();
        mSensorManager.unregisterListener(mSensorListener);
    }

    public void display(View view) {
        switch (operation) {
            case '+':
                res += a;
                break;
            case '-':
                res -= a;
                break;
            case '*':
                res *= a;
                break;
            case '/':
                res /= a;
                break;
            default:
                break;
        }
        a = res;
        result_tv.setText("" + res);
    }

    public void clear(View view) {
        res = 0;
        a = 0;
        set = false;
        operation = '0';
        result_tv.setText("" + res);
        input_et.setText("" + a);
    }

    public void add(View view) {
        if (!set) {
            res = a;
            result_tv.setText("" + res);
            set = true;
        }
        //  Toast.makeText(this,"Plus button clicked",Toast.LENGTH_SHORT).show();
        //  a=Float.parseFloat(input_et.getText().toString());
        a = 0;
        operation = '+';
        input_et.setText("" + a);
    }

    public void subtract(View view) {
        if (!set) {
            res = a;
            result_tv.setText("" + res);
            set = true;
        }
        //  Toast.makeText(this,"Plus button clicked",Toast.LENGTH_SHORT).show();
        // a=Float.parseFloat(input_et.getText().toString());
        a = 0;
        operation = '-';
        input_et.setText("" + a);
    }

    public void multiply(View view) {
        if (!set) {
            res = a;
            result_tv.setText("" + res);
            set = true;
        }
        //  Toast.makeText(this,"Plus button clicked",Toast.LENGTH_SHORT).show()
        a = 0;
        operation = '*';
        input_et.setText("" + a);
    }

    public void divide(View view) {
        if (!set) {
            res = a;
            result_tv.setText("" + res);
            set = true;
        }
        //  Toast.makeText(this,"Plus button clicked",Toast.LENGTH_SHORT).show();
        //  a=Float.parseFloat(input_et.getText().toString());
        a = 0;
        operation = '/';
        input_et.setText("" + a);
    }

    public void value1(View view) {
        a *= 10;
        a += 1;
        input_et.setText("" + a);
    }

    public void value2(View view) {
        a *= 10;
        a += 2;
        input_et.setText("" + a);
    }

    public void value3(View view) {
        a *= 10;
        a += 3;
        input_et.setText("" + a);
    }

    public void value4(View view) {
        a *= 10;
        a += 4;
        input_et.setText("" + a);
    }

    public void value5(View view) {
        a *= 10;
        a += 5;
        input_et.setText("" + a);
    }

    public void value6(View view) {
        a *= 10;
        a += 6;
        input_et.setText("" + a);
    }

    public void value7(View view) {
        a *= 10;
        a += 7;
        input_et.setText("" + a);
    }

    public void value8(View view) {
        a *= 10;
        a += 8;
        input_et.setText("" + a);
    }

    public void value9(View view) {
        a *= 10;
        a += 9;
        input_et.setText("" + a);
    }

    public void value0(View view) {
        a *= 10;
        input_et.setText("" + a);
    }

    // Method to implement shake to clear feature
    public void bindShakeToClearListener(){
        mSensorListener = new SensorEventListener() {
            @Override
            public void onSensorChanged(SensorEvent sensorEvent) {
                float x,y,z;
                x = sensorEvent.values[0];
                y = sensorEvent.values[1];
            //    z = sensorEvent.values[2];

                mAccelerationLast = mAccelerationCurrent;
                mAccelerationCurrent = (float)Math.sqrt(((x * x + y * y )));        // Not taking z-axis to avoid clearing when the device is picked up or lowered
                double delta = mAccelerationCurrent - mAccelerationLast;
                mAcceleration = (float)(mAcceleration * 0.9f + delta);

                if (mAcceleration > 10.5) {        // Value '12' so that triggered only after a reasonable acceleration
                    clear(findViewById(android.R.id.content));
                }
            }

            @Override
            public void onAccuracyChanged(Sensor sensor, int i) {
                // Do nothing
            }
        };
    }

}
