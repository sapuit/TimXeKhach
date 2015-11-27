package com.example.tranquoctin.timxekhach.activity;

import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.tranquoctin.timxekhach.R;
import com.example.tranquoctin.timxekhach.utils.QustomDialogBuilder;

import static com.example.tranquoctin.timxekhach.mailapi.SendMail.sendMail;
import static com.example.tranquoctin.timxekhach.utils.ConnectInternet.isInternetAvailable;

public class CreateNhaXeActivity extends AppCompatActivity {

    EditText etTenNhaXe, etThoiGian, etTuyen,
            etGiaVe, etSDT,etKhac;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_nha_xe);
        setTitle("Thêm thông tin hãng xe ");
        initialView();
    }

    private void initialView() {
        etTenNhaXe = (EditText) findViewById(R.id.etTen);
        etThoiGian = (EditText) findViewById(R.id.etThoiGian);
        etTuyen = (EditText) findViewById(R.id.etTuyen);
        etGiaVe = (EditText) findViewById(R.id.etGiaVe);
        etSDT = (EditText) findViewById(R.id.etSDT);
        etKhac = (EditText) findViewById(R.id.etKhac);
    }

    public void sendNewCreate(){
        // check Internet connected
        if (TextUtils.isEmpty(etTenNhaXe.getText().toString())) {
            etTenNhaXe.setError("Chưa nhập tên hãng xe");
            etTenNhaXe.setFocusable(true);
            return ;
        }

        if (TextUtils.isEmpty(etTenNhaXe.getText().toString())) {
            etTenNhaXe.setError("Chưa nhập số điện thoại");
            etTenNhaXe.setFocusable(true);
            return ;
        }

        if (isInternetAvailable(getApplicationContext())) { // true

            StringBuffer content = new StringBuffer();
            content.append("- Thông tin hãng xe : " + "\r\n");
            content.append(etTenNhaXe.getText().toString() + "\r\n");
            content.append(etThoiGian.getText().toString()+ "\r\n");
            content.append(etTuyen.getText().toString()+ "\r\n");
            content.append(etGiaVe.getText().toString()+ "\r\n");
            content.append(etSDT.getText().toString()+ "\r\n");
            content.append(etKhac.getText().toString()+ "\r\n");

            sendMail("Create", content.toString());
        } else {
            // save
            saveCreate();
        }

        QustomDialogBuilder b = new QustomDialogBuilder(this);
        b.setTitleColor("#4CAF50");
        b.setDividerColor("#4CAF50");
        b.setTitle("Thêm hãng xe");
        b.setMessage("Cám ơn bạn đã đóng góp ! " +
                "\nDữ liệu sẽ sớm được cập nhật .");
        b.setNegativeButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
                finish();
            }
        });
        b.create().show();

    }

    private void saveCreate() {

        try {
            String prefname = "data_create";
            //tạo đối tượng getSharedPreferences
            SharedPreferences pre = getSharedPreferences(prefname, MODE_PRIVATE);
            //tạo đối tượng Editor để lưu thay đổi
            SharedPreferences.Editor editor = pre.edit();

            //xóa mọi lưu trữ trước đó
            editor.clear();

            //lưu vào editor
            editor.putString("tenNhaXe",
                    etTenNhaXe.getText().toString());
            editor.putString("tuyen",
                    etTuyen.getText().toString());
            editor.putString("thoigian",
                    etThoiGian.getText().toString());
            editor.putString("sodienthoai",
                    etSDT.getText().toString());
            editor.putString("giave",
                    etGiaVe.getText().toString());
            editor.putString("ghichu",
                    etKhac.getText().toString());

            //chấp nhận lưu xuống file
            editor.commit();
        } catch (Exception e) {
            Log.e("saveResponse error", e.getMessage());
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_create, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_sent) {
            sendNewCreate();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
