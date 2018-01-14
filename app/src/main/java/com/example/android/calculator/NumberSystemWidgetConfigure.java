package com.example.android.calculator;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RemoteViews;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

/**
 * Created by Lenovo on 12-Jan-18.
 */

public class NumberSystemWidgetConfigure extends AppCompatActivity implements Spinner.OnItemSelectedListener, View.OnClickListener {

    int mAppWidgetId;
    Spinner mBaseLangSpinner,mFinalLangSpinner;
    EditText mBaseSystemET;
    TextView mFinalSystemET;
    String mBaseSystem, mFinalSystem;
    Button mConvert,mCancel;
    long mBaseNumber;
    String mFinalNumber;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.widget_configure_layout);
        mBaseLangSpinner= (Spinner) findViewById(R.id.baseLangSpinner);
        mFinalLangSpinner= (Spinner) findViewById(R.id.finalLangSpinner);
        mBaseSystemET= (EditText) findViewById(R.id.etBaseSystem);
        mFinalSystemET= (TextView) findViewById(R.id.etFinalSystem);
        mConvert= (Button) findViewById(R.id.btConvert);
        mCancel= (Button) findViewById(R.id.btCancel);

        ArrayAdapter<CharSequence> arrayAdapter=ArrayAdapter.createFromResource(this,R.array.NumberSystems,
                android.R.layout.simple_spinner_item);

        arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        mBaseLangSpinner.setAdapter(arrayAdapter);
        mBaseLangSpinner.setOnItemSelectedListener(this);
        mBaseLangSpinner.setPrompt("None");
        mFinalLangSpinner.setAdapter(arrayAdapter);
        mFinalLangSpinner.setOnItemSelectedListener(this);
        mFinalLangSpinner.setPrompt("None");

    }

    @Override
    protected void onStart() {
        super.onStart();
        Intent intent=getIntent();
        Bundle args=intent.getExtras();
        if(args!=null){
            mAppWidgetId=args.getInt(AppWidgetManager.EXTRA_APPWIDGET_ID,AppWidgetManager.INVALID_APPWIDGET_ID);
            mConvert.setOnClickListener(this);
            mCancel.setOnClickListener(this);
        }
    }

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
        switch (adapterView.getId()){
            case R.id.baseLangSpinner:
                mBaseSystem= (String) adapterView.getItemAtPosition(i);
                break;
            case R.id.finalLangSpinner:
                mFinalSystem= (String) adapterView.getItemAtPosition(i);
                break;
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {
        mBaseSystem=mFinalSystem="None";
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btConvert:
                if (!mBaseSystem.equals("None") && !mFinalSystem.equals("None")) {
                    try{
                        mBaseNumber = Long.parseLong(mBaseSystemET.getText().toString());
                        switch (mBaseSystem) {
                            case "Binary":
                                switch (mFinalSystem) {
                                    case "Decimal":
                                        mFinalNumber = binaryToDecimal(mBaseNumber);
                                        break;
                                    case "Octal":
                                        mFinalNumber = binaryToOctal(mBaseNumber);
                                        break;
                                    case "Hexadecimal":
                                        mFinalNumber = binaryToHexa(mBaseNumber);
                                        break;
                                    case "Binary":
                                        mFinalNumber = String.valueOf(mBaseNumber);
                                }
                                break;
                            case "Decimal":
                                switch (mFinalSystem) {
                                    case "Binary":
                                        mFinalNumber = decimalToBinary(mBaseNumber);
                                        break;
                                    case "Octal":
                                        mFinalNumber = decimalToOctal(mBaseNumber);
                                        break;
                                    case "Hexadecimal":
                                        mFinalNumber = decimalToHexadecimal(mBaseNumber);
                                        break;
                                    case "Decimal":
                                        mFinalNumber = String.valueOf(mBaseNumber);
                                        break;
                                }
                                break;
                            case "Octal":
                                switch (mFinalSystem) {
                                    case "Binary":
                                        mFinalNumber = octalToBinary(mBaseNumber);
                                        break;
                                    case "Decimal":
                                        mFinalNumber = octalToDecimal(mBaseNumber);
                                        break;
                                    case "Hexadecimal":
                                        mFinalNumber = octalToHexa(mBaseNumber);
                                        break;
                                    case "Octal":
                                        mFinalNumber = String.valueOf(mBaseNumber);
                                        break;
                                }
                                break;
                            case "Hexadecimal":
                                switch (mFinalSystem) {
                                    case "Binary":
                                        mFinalNumber = hexToBinary(mBaseNumber);
                                        break;
                                    case "Decimal":
                                        mFinalNumber = hexToDecimal(mBaseNumber);
                                        break;
                                    case "Octal":
                                        mFinalNumber = hexToOctal(mBaseNumber);
                                        break;
                                    case "Hexadecimal":
                                        mFinalNumber = String.valueOf(mBaseNumber);
                                        break;
                                }
                                break;
                        }
                        mFinalSystemET.setText(String.valueOf(mFinalNumber));
                    }catch (Exception e){
                        Toast.makeText(this,"Invalid",Toast.LENGTH_SHORT).show();
                    }
                }
                break;
            case R.id.btCancel:
                AppWidgetManager appWidgetManager=AppWidgetManager.getInstance(this);
                RemoteViews remoteViews=new RemoteViews(this.getPackageName(),R.layout.layout_widget);
                appWidgetManager.updateAppWidget(mAppWidgetId,remoteViews);

                Intent updateIntent=new Intent(this,NumberSystemWidgetConfigure.class);
                updateIntent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID,mAppWidgetId);
                PendingIntent updatePendingIntent=PendingIntent.getActivity(this,0,updateIntent,PendingIntent.FLAG_UPDATE_CURRENT);
                remoteViews.setOnClickPendingIntent(R.id.btOpenConvert,updatePendingIntent);

                appWidgetManager.updateAppWidget(mAppWidgetId,remoteViews);

                Intent returnintent=new Intent();
                returnintent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID,mAppWidgetId);
                setResult(RESULT_OK,returnintent);
                finish();
        }
    }

    private String hexToOctal(long number) {
        String str=octalToDecimal(number);
        long num=Long.parseLong(str);
        return Long.toOctalString(num);
    }

    private String hexToDecimal(long number) {
        String str=String.valueOf(number);
        long dec=Long.parseLong(str,16);
        return String.valueOf(dec);
    }

    private String hexToBinary(long number) {
        String str=octalToDecimal(number);
        long num=Long.parseLong(str);
        return Long.toBinaryString(num);
    }

    private String octalToHexa(long number) {
        String str=octalToDecimal(number);
        long num=Long.parseLong(str);
        return Long.toHexString(num);
    }

    private String octalToDecimal(long number) {
        String str=String.valueOf(number);
        long dec=Long.parseLong(str,8);
        return String.valueOf(dec);
    }

    private String octalToBinary(long number) {
        String str=octalToDecimal(number);
        long num=Long.parseLong(str);
        return Long.toBinaryString(num);
    }

    private String decimalToHexadecimal(long number) {
        return Long.toHexString(number);
    }

    private String decimalToOctal(long number) {
        return Long.toOctalString(number);
    }

    private String decimalToBinary(long number) {
        return Long.toBinaryString(number);
    }

    private String binaryToHexa(long number) {
        number=Long.parseLong(binaryToDecimal(number));
        String hexa=Long.toHexString(number);
        return hexa;
    }

    private String binaryToOctal(long n) {
        n= Long.parseLong(binaryToDecimal(n));
        String octal=Long.toOctalString(n);
        return octal;
    }

    private String binaryToDecimal(long n) {

        String str=String.valueOf(n);
        long decimal= Long.parseLong(str, 2);
        return String.valueOf(decimal);
    }
}
