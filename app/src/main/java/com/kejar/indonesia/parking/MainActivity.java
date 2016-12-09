package com.kejar.indonesia.parking;

import android.content.Intent;
import android.os.Handler;
import android.os.SystemClock;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import static java.lang.String.valueOf;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    int hours=0, mins=0;
    Button btnMasuk, btnKeluar, btnReset;
    TextView txtTimer;
    Handler customHandler = new Handler();
    long startTime=0L,timeInMillisecond=0L, timeSwap=0L, updateTime=0L;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button btnCeks = (Button) findViewById(R.id.cek_billing);
        btnCeks.setOnClickListener(this);

        btnMasuk = (Button) findViewById(R.id.button_masuk);
        btnKeluar = (Button) findViewById(R.id.button_keluar);
        btnReset = (Button) findViewById(R.id.button_reset);
        txtTimer = (TextView) findViewById(R.id.timer);


        btnMasuk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startTime = SystemClock.uptimeMillis();

                customHandler.postDelayed(updateTimeThread, 0);
            }
        });

        btnKeluar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                timeSwap += timeInMillisecond;
                customHandler.removeCallbacks(updateTimeThread);
            }
        });

        btnReset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                txtTimer.setText("" + 00 + ":" + 00 + ":"
                        + String.format("%02d", 00) + ":"
                        + String.format("%03d", 00));
                startTime = SystemClock.uptimeMillis();
                timeSwap = 0;
            }
        });
    }


    public void onClick(View view) {
        if (view.getId() == R.id.cek_billing) {
            int price = calculate(hours,mins);
            createSummary(price);
        }
    }



    private void createSummary(int price) {
        TextView bill=(TextView) findViewById(R.id.billing_summary);
        bill.setText("$ " + price);
    }


    private int calculate(int hours, int mins){
        int total=hours*3000+mins*200;
        return total;
    }

    Runnable updateTimeThread = new Runnable() {
        @Override
        public void run() {
            timeInMillisecond = SystemClock.uptimeMillis()-startTime;
            updateTime = timeSwap+timeInMillisecond;
            int secs=(int)(updateTime/1000);
            mins=secs/60;
            hours=mins/60;
            secs%=60;
            int milliseconds =(int) (updateTime%1000);
            txtTimer.setText((""+hours+":"+mins+":"+String.format("%2d", secs)+":"
                    +String.format("%3d",milliseconds)));
            customHandler.postDelayed(this,0);
        }
    };

}
