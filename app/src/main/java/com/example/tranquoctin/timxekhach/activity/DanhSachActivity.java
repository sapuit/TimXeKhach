package com.example.tranquoctin.timxekhach.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.tranquoctin.timxekhach.R;
import com.example.tranquoctin.timxekhach.adapter.NhaXeAdapter;
import com.example.tranquoctin.timxekhach.entities.NhaXeEntities;
import com.melnykov.fab.FloatingActionButton;

import java.util.ArrayList;
import static com.example.tranquoctin.timxekhach.utils.ParseXML.run;

/*Hiển thị kết quả  danh sách nhà xe */
public class DanhSachActivity extends AppCompatActivity {

    String tinhdi, tinhden;
    ArrayList<NhaXeEntities> arrNhaXe = new ArrayList<NhaXeEntities>();
    NhaXeAdapter adapter = null;
    ListView lvNhaXe;
    Toolbar toolbar;
    TextView txtCapNhatDuLieu;
    ImageView imageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_danh_sach);

        initialView();

        getBundle();    // lấy dữ liệu từ activity trước
        getSupportActionBar().setTitle(tinhdi + " - " + tinhden);

        // Hiển thị dữ liệu ra listview
        try {
            arrNhaXe = run(DanhSachActivity.this, tinhdi, tinhden);
            if (arrNhaXe.size() == 0) { // nếu không có dữ liệu trong mảng
                txtCapNhatDuLieu.setVisibility(View.VISIBLE);
            }
        } catch (Exception e) {
            try {
                arrNhaXe = run(DanhSachActivity.this,
                        tinhden, tinhdi);
                if (arrNhaXe.size() == 0) { // nếu không có dữ liệu trong mảng
                    txtCapNhatDuLieu.setVisibility(
                            View.VISIBLE);
                }
            } catch (Exception e1) {
                e1.printStackTrace();
                txtCapNhatDuLieu.setVisibility(
                        View.VISIBLE);
            }
        }
        adapter = new NhaXeAdapter(
                this,
                R.layout.custom_listview, arrNhaXe);
        lvNhaXe.setAdapter(adapter);

    }

    public void initialView(){
        // thiết lập action bar
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        // load các view
        lvNhaXe = (ListView) findViewById(R.id.lvNhaXe);
        arrNhaXe = new ArrayList<NhaXeEntities>();
        txtCapNhatDuLieu = (TextView) findViewById(
                R.id.txtCapNhatDuLieu);

        // in Activity Context
        ImageView imageView = new ImageView(this); // Create an icon
        imageView.setImageResource(R.drawable.ic_logo);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.attachToListView(lvNhaXe);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplication(),CreateNhaXeActivity.class));
            }
        });
    }

    // lấy tỉnh đi và đến từ main actyvity
    private void getBundle() {
        Intent i = getIntent();
        Bundle bundle = i.getBundleExtra("tinh");
        tinhdi = bundle.getString("tinhdi");
        tinhden = bundle.getString("tinhden");
    }


}
