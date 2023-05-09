package com.example.myapplication;



import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import com.google.zxing.Result;

import java.sql.Connection;

import me.dm7.barcodescanner.zxing.ZXingScannerView;

public class MainActivity extends AppCompatActivity implements ZXingScannerView.ResultHandler{
    ZXingScannerView zXingScannerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Button ok_button = (Button) findViewById(R.id.ok_button);
        ok_button.setOnClickListener(ok_btn);
        zXingScannerView = findViewById(R.id.ZXingScannerView_QRCode);
        //取得相機權限
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M &&
                ActivityCompat.checkSelfPermission(this
                        , Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CAMERA},
                    100);
        } else {
            //若先前已取得權限，則直接開啟
            openQRCamera();
        }
    }

    private void openQRCamera() {
        zXingScannerView.setResultHandler(this);
        zXingScannerView.startCamera();
    }

    /**
     * 取得權限回傳
     */
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == 100 && grantResults[0] == 0) {
            openQRCamera();
        } else {
            Toast.makeText(this, "權限勒？", Toast.LENGTH_SHORT).show();
        }
    }


    @Override
    public void handleResult(Result rawResult) {
        TextView tvResult = findViewById(R.id.text_view);

        tvResult.setText(rawResult.getText());
        //ZXing相機預設掃描到物件後就會停止，以此這邊再次呼叫開啟，使相機可以為連續掃描之狀態
        openQRCamera();
    }

    private Button.OnClickListener ok_btn = new Button.OnClickListener() {
        @Override
        public void onClick(View v) {

            final TextView text_view = (TextView) findViewById(R.id.text_view);

            new Thread(new Runnable() {
                @Override
                public void run() {

                    String stringdata = text_view.getText().toString();
                    Connect con = new Connect();
                    con.insertData(stringdata);
                    // 讀取更新後的資料
                    final String updata = con.getData();
                    Log.v("OK", updata);

                    text_view.post(new Runnable() {
                        public void run() {
                            text_view.setText("已放入資料庫");
                            Toast.makeText(MainActivity.this,"已放入資料庫",Toast.LENGTH_SHORT).show();
                        }
                    });

                }
            }).start();
        }
    };
}

