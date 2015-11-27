package com.example.tranquoctin.timxekhach.adapter;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;

import com.example.tranquoctin.timxekhach.R;
import com.example.tranquoctin.timxekhach.activity.ResponseActivity;
import com.example.tranquoctin.timxekhach.entities.NhaXeEntities;
import com.example.tranquoctin.timxekhach.utils.QustomDialogBuilder;

import java.util.ArrayList;

public class NhaXeAdapter extends ArrayAdapter<NhaXeEntities> {

    Activity context = null;
    ArrayList<NhaXeEntities> arrNhaXe = null;
    int layoutId;


    public NhaXeAdapter(Activity context,
                        int layoutId,
                        ArrayList<NhaXeEntities> arr) {
        super(context, layoutId, arr);
        this.context = context;
        this.layoutId = layoutId;
        this.arrNhaXe = arr;
    }

    @Override
    public View getView(int position, View convertView,
                        ViewGroup parent) {
        LayoutInflater inflater =
                context.getLayoutInflater();
        convertView = inflater.inflate(layoutId, null);

        if (arrNhaXe.size() > 0 && position >= 0) {
            final TextView txtTenNhaXe = (TextView)
                    convertView.findViewById(R.id.txtTenNhaXe);
            final TextView txtThoiGian = (TextView)
                    convertView.findViewById(R.id.txtThoiGian);
            final TextView txtDiaDiem = (TextView)
                    convertView.findViewById(R.id.txtDiaDiem);
            final TextView txtGiaVe = (TextView)
                    convertView.findViewById(R.id.txtGiaVe);
            final Button btnSDT = (Button)
                    convertView.findViewById(R.id.btnSDT);
            final NhaXeEntities nhaxe = arrNhaXe.get(position);

            txtTenNhaXe.setText(nhaxe.getTen());
            txtThoiGian.setText(nhaxe.getThoigian());
            txtDiaDiem.setText(nhaxe.getDiadiemdi() + " - " +
                    nhaxe.getDiadiemden());
            txtGiaVe.setText(nhaxe.getGiave());

            // khi click btn điện thoại
            btnSDT.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    selectPhone(nhaxe);
                }
            });

            convertView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    showPopUpNhaXe(context,nhaxe);
                }
            });
        }
        return convertView;
    }

    // lựa chọn số
    public void selectPhone(NhaXeEntities nhaxe){
        if (nhaxe.getDienthoai().get(1).equals("")) {
            String sdt = nhaxe.getDienthoai().get(0);
            callPhone(sdt);
        } else {
            // chọn số
            showPopUpSelectPhone(context, nhaxe.getDienthoai());
        }
    }
    public void showPopUpSelectPhone(final Context context1,
                                            final ArrayList<String> dienthoai) {
        // valiable
        AlertDialog.Builder diBuilder = new AlertDialog.Builder(context1);
        final ArrayAdapter<String> adapter = new ArrayAdapter<String>(
                context1, android.R.layout.simple_list_item_1);

        for (String sdt : dienthoai) {
            if (!sdt.equals("")) {
                adapter.add(sdt);
            }
        }

        // Process
        diBuilder.setTitle("Gọi điện");
        diBuilder.setIcon(R.drawable.ic_action_phone_outgoing);
        diBuilder.setAdapter(adapter, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int item) {
                String sdt = dienthoai.get(item);
                Log.i(String.valueOf(item), sdt);
                callPhone(sdt);
            }
        });
        diBuilder.setNegativeButton("Cancel", null);
        // Output
        AlertDialog alert = diBuilder.create();
        alert.show();
    }

    public void callPhone(String sdt) {
        sdt = sdt.replace(".", "");
        Intent callIntent = new Intent(Intent.ACTION_CALL);
        callIntent.setData(Uri.parse("tel:" + sdt));

        if (ContextCompat.checkSelfPermission(context, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    public void requestPermissions(@NonNull String[] permissions, int requestCode)
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for Activity#requestPermissions for more details.
            return;
        }
        context.startActivity(callIntent);
    }

    public void showPopUpNhaXe(final Context context1, final NhaXeEntities nhaxe) {
        AlertDialog.Builder diBuilder;
        diBuilder = new AlertDialog.Builder(context1);
        final ArrayAdapter<String> adapter = new ArrayAdapter<String>(
                context1, android.R.layout.simple_list_item_1);

        adapter.add("Điện thoại");
        adapter.add("Phản hồi thông tin sai");

        // Process
        diBuilder.setTitle(nhaxe.getTen());
//        diBuilder.setIcon(R.drawable.ic_action_bus);

        diBuilder.setAdapter(adapter, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int item) {
                if (item == 0 ){
                    // gọi điện
                    selectPhone(nhaxe);
                }else {
                    //  phản hồi thông tin
                    moveResponseActivity(nhaxe);
                }
            }
        });
        diBuilder.setNegativeButton("Cancel", null);

        // Output
        AlertDialog alert = diBuilder.create();
        alert.show();
    }

    private void moveResponseActivity(NhaXeEntities nhaxe) {
        Intent i = new Intent(context,
                ResponseActivity.class);
        i.putExtra("nhaxe", nhaxe);
        context.startActivity(i);
    }


}
