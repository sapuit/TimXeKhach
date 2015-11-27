package com.example.tranquoctin.timxekhach.activity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import com.example.tranquoctin.timxekhach.R;
import com.example.tranquoctin.timxekhach.entities.NhaXeEntities;
import com.example.tranquoctin.timxekhach.mailapi.GMailSender;
import com.example.tranquoctin.timxekhach.utils.QustomDialogBuilder;

import java.net.InetAddress;

import static com.example.tranquoctin.timxekhach.mailapi.SendMail.sendMail;
import static com.example.tranquoctin.timxekhach.utils.ConnectInternet.isInternetAvailable;

public class ResponseActivity extends AppCompatActivity {

    CheckBox cbTenNhaXe, cbThoiGian, cbTuyen, cbGiaVe, cbSDT;
    EditText etGhiChu;
    Button btnResponse;
    NhaXeEntities nhaXe;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_response);

        try {
            getItent();
            initiateView();

        } catch (Exception e) {
            Log.e("ResponseActivity error ", e.getMessage());
        }

    }

    // get data from previous activity
    public void getItent() {
        Intent i = getIntent();
        nhaXe = (NhaXeEntities) i.
                getSerializableExtra("nhaxe");
        setTitle("hãng xe " +nhaXe.getTen());
    }

    // initiate view
    public void initiateView() {
        cbTenNhaXe = (CheckBox) findViewById(R.id.cbTenNhaXe);
        cbThoiGian = (CheckBox) findViewById(R.id.cbThoiGian);
        cbTuyen = (CheckBox) findViewById(R.id.cbTuyen);
        cbGiaVe = (CheckBox) findViewById(R.id.cbGiaVe);
        cbSDT = (CheckBox) findViewById(R.id.cbSDT);
        etGhiChu = (EditText) findViewById(R.id.etGhiChu);
        btnResponse= (Button) findViewById(R.id.btnResponse);
    }

    // click event
    public void clickResponse(View v) {
        // check Internet connected
        if (isInternetAvailable(getApplicationContext())) { // true
            // send response to email
            StringBuffer content = new StringBuffer();
            content.append("- Thông tin hãng xe : " + "\r\n");
            content.append(nhaXe.getTen() + " : " +
                    cbTenNhaXe.isChecked()+ "\r\n");
            content.append(nhaXe.getDiadiemdi() + " - " +
                    nhaXe.getDiadiemden() + " : " +
                    cbTuyen.isChecked()+ "\r\n");
            content.append(nhaXe.getThoigian() + " : " +
                    cbThoiGian.isChecked()+ "\r\n");
            content.append(nhaXe.getDienthoai() + " : " +
                    cbSDT.isChecked()+ "\r\n");
            content.append(nhaXe.getGiave() + " : " +
                    cbGiaVe.isChecked()+ "\r\n");
            content.append("Ghi chú : " + etGhiChu.getText().toString());
            sendMail("Response", content.toString());
        } else {
            // save
            saveResponse();
        }


        // thông báo gửi thành công
        QustomDialogBuilder b = new QustomDialogBuilder(this);
        b.setTitleColor("#4CAF50");
        b.setDividerColor("#4CAF50");
        b.setTitle("Gửi phản hồi");
        b.setMessage("Cám ơn bạn đã phản hồi thông tin ! " +
                "\nDữ liệu sẽ sớm được cập nhật .");
        b.setNegativeButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
                finish();
            }
        });
        b.show();

    }

    public void saveResponse() {
        try {
            Log.i("saveResponse", "OK");
            String prefname = "data_response";
            //tạo đối tượng getSharedPreferences
            SharedPreferences pre = getSharedPreferences(prefname, MODE_PRIVATE);
            //tạo đối tượng Editor để lưu thay đổi
            SharedPreferences.Editor editor = pre.edit();

            //xóa mọi lưu trữ trước đó
            editor.clear();

            //lưu vào editor
            editor.putString("tenNhaXe",
                    nhaXe.getTen() + " : " +
                            cbTenNhaXe.isChecked());
            editor.putString("tuyen",
                    nhaXe.getDiadiemdi() + " - " +
                            nhaXe.getDiadiemden() + " : " +
                            cbTuyen.isChecked());
            editor.putString("thoigian",
                    nhaXe.getThoigian() + " : " +
                            cbThoiGian.isChecked());
            editor.putString("sodienthoai",
                    nhaXe.getDienthoai() + " : " +
                            cbSDT.isChecked());
            editor.putString("giave",
                    nhaXe.getGiave() + " : " +
                            cbGiaVe.isChecked());
            editor.putString("ghichu", "Ghi chú : " + etGhiChu);

            //chấp nhận lưu xuống file
            editor.commit();
        } catch (Exception e) {
            Log.e("saveResponse error", e.getMessage());
        }
    }
}
