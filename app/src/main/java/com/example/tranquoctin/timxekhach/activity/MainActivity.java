package com.example.tranquoctin.timxekhach.activity;

import android.annotation.TargetApi;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.example.tranquoctin.timxekhach.R;
import com.example.tranquoctin.timxekhach.utils.DanhSachTinh;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import static com.example.tranquoctin.timxekhach.mailapi.SendMail.sendMail;
import static com.example.tranquoctin.timxekhach.utils.ConnectInternet.isInternetAvailable;

public class MainActivity extends AppCompatActivity
        implements TextWatcher {

    private AutoCompleteTextView actvTinhDi;
    private AutoCompleteTextView actvTinhDen;
    private String tinhdi, tinhden;
    private Button btnTimKiem;
    private EditText edtNgay;
    private ImageButton btnNgay;
    private ImageButton btnLogo;
    Calendar cal;
    Date date;
    private Toolbar toolbar;
    private List<String> danhsachTinh;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        // update response
        if (isInternetAvailable(getApplicationContext())) {
            sendResponse("data_response");
            sendResponse("data_create");
        }

        initialView();
        chonngaydi();
        chuy();
    }


    private void sendResponse(String prefname) {
        try {

            //tạo đối tượng getSharedPreferences
            SharedPreferences pre = getSharedPreferences(prefname, MODE_PRIVATE);
            StringBuffer content = new StringBuffer();

            if (pre.contains("tenNhaXe")) {
                content.append("- Thông tin hãng xe : \r\n");
                content.append(pre.getString("tenNhaXe", "") + "\r\n");
                content.append(pre.getString("tuyen", "") + "\r\n");
                content.append(pre.getString("thoigian", "") + "\r\n");
                content.append(pre.getString("sodienthoai", "") + "\r\n");
                content.append(pre.getString("giave", "") + "\r\n");
                content.append(pre.getString("ghichu", "") + "\r\n");

                //tạo đối tượng Editor để lưu thay đổi
                SharedPreferences.Editor editor = pre.edit();

                //xóa mọi lưu trữ trước đó
                editor.clear();
                //chấp nhận lưu xuống file
                editor.commit();

                sendMail(prefname, content.toString());
            }
        } catch (Exception e) {
            Log.e("saveResponse error", e.getMessage());
        }
    }

    // khai báo các view
    public void initialView() {
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        actvTinhDi = (AutoCompleteTextView) findViewById(
                R.id.actvTinhDi);
        actvTinhDen = (AutoCompleteTextView) findViewById(
                R.id.actvTinhDen);
        btnTimKiem = (Button) findViewById(R.id.btnTimKiem);
        edtNgay = (EditText) findViewById(R.id.edtNgay);
        btnNgay = (ImageButton) findViewById(R.id.btnNgay);
        // thiết lập autocomplete
        danhsachTinh = DanhSachTinh.getListTinh();
        actvTinhDi.addTextChangedListener(this);
        actvTinhDi.setAdapter(new ArrayAdapter<String>(this,
                android.R.layout.simple_list_item_1,
                danhsachTinh));
        actvTinhDen.addTextChangedListener(this);
        actvTinhDen.setAdapter(new ArrayAdapter<String>(this,
                android.R.layout.simple_list_item_1,
                danhsachTinh));


    }

    // click sự kiện tìm kiếm
    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    public void onClick(View v) {
        try {
            if (kiemtraTinh() == true) {
                Intent i = new Intent(getApplication(),
                        DanhSachActivity.class);
                Bundle bundle = new Bundle();
                bundle.putString("tinhdi", tinhdi);
                bundle.putString("tinhden", tinhden);
                i.putExtra("tinh", bundle);
                startActivity(i, bundle);
            }
        } catch (Exception e) {
            Log.e("Button tìm kiếm", "onClick : " + e.getMessage());
        }
    }

    //image button chon ngay
    public void chonngaydi() {
        btnNgay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                layThongSo();
                ChonNgay();
                btnNgay.setVisibility(View.GONE);
            }
        });
        edtNgay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                layThongSo();
                ChonNgay();
            }
        });
    }

    public void ChonNgay() {
        DatePickerDialog.OnDateSetListener callback = new DatePickerDialog.OnDateSetListener() {

            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear,
                                  int dayOfMonth) {
                // TODO Auto-generated method stub
                // cập nhật lại ngày tháng mỗi lần thay đổi
                edtNgay.setText((dayOfMonth) + "/" + (monthOfYear + 1));
                cal.set(year, monthOfYear, dayOfMonth);
                date = cal.getTime();
            }
        };
        // xử lý ngày giờ trong datePickerDialog
        String s = edtNgay.getText() + "";
        String strArrtmp[] = s.split("/");
        int ngay = Integer.parseInt(strArrtmp[0]);
        int thang = Integer.parseInt(strArrtmp[1]) - 1;
        int nam = Integer.parseInt(strArrtmp[2]);

        DatePickerDialog dpic = new DatePickerDialog(MainActivity.this, callback, nam, thang, ngay);
        dpic.setTitle("chọn ngày đi");
        dpic.show();
    }

    // lấy các thông số lần đầu chạy ứng dụng
    public void layThongSo() {
        // lấy ngày hiện tại của hệ thống
        cal = Calendar.getInstance();
        SimpleDateFormat dft = null;
        // định dạng ngày tháng năm
        dft = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
        String strDate = dft.format(cal.getTime());
        edtNgay.setText(strDate);
        // gán cal.getTime() cho ngày hoàn thành
        date = cal.getTime();
    }

    // sử lý autocomple
    @Override
    public void beforeTextChanged(CharSequence s, int start,
                                  int count, int after) {
        Log.i("autocomple", "beforeTextChanged");
    }

    @Override
    public void onTextChanged(CharSequence s, int start,
                              int before, int count) {
        Log.i("autocomple", "onTextChanged");
    }

    @Override
    public void afterTextChanged(Editable s) {
        Log.i("autocomple", "afterTextChanged");
    }

    public boolean kiemtraTinh() {
        tinhdi = actvTinhDi.getText().toString();
        tinhden = actvTinhDen.getText().toString();
        if (!danhsachTinh.contains(tinhdi)) {
            actvTinhDi.setError("Tỉnh đi nhập chưa đúng");
            actvTinhDi.setFocusable(true);
            return false;
        }
        if (!danhsachTinh.contains(tinhden)) {
            actvTinhDen.setError("Tỉnh đến nhập chưa đúng");
            actvTinhDen.setFocusable(true);
            return false;
        }
        if (tinhden.equals(tinhdi)) {
            actvTinhDen.setError("Tỉnh đi và đến trùng nhau ");
            actvTinhDen.setFocusable(true);
            return false;
        }
        return true;
    }

    //su kien kich vao logo
    public void chuy() {
        btnLogo = (ImageButton) findViewById(R.id.btnLogo);
        btnLogo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showPopUp();
            }
        });
    }

    public void showPopUp() {
        AlertDialog.Builder b = new AlertDialog.Builder(MainActivity.this);
        b.setTitle("Ghi chú.");
        b.setIcon(R.drawable.ic_action_user);
        b.setMessage("Do biết đến thông tin cuộc thi khá trễ nên còn một số chức năng chưa hoàn chỉnh và cơ sở dữ liệu còn hạn chế, mới được hơn 50 tuyến, tập trung ở các thành phố lớn. " +
                "\n Mong các bạn thông cảm và ủng hộ chúng tôi. Hẹn các bạn một phiên bản hoàn thiện hơn!");
        b.setNegativeButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });
        b.create().show();
    }
}