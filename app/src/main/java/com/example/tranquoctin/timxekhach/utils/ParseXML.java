package com.example.tranquoctin.timxekhach.utils;

import android.app.Activity;
import android.content.res.Resources;
import android.content.res.XmlResourceParser;

import com.example.tranquoctin.timxekhach.entities.NhaXeEntities;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;
import java.util.ArrayList;

/**
 * láy dữ liệu từ file xml
 */
public class ParseXML {

    public static ArrayList<NhaXeEntities> run(
            Activity activity, String tinhdi, String tinhden)
            throws XmlPullParserException, IOException {

        // Lưu danh sách tìm kiếm
        ArrayList<NhaXeEntities> arrNhaXe;
        ArrayList<String> sdt;
        NhaXeEntities nhaXe;
        arrNhaXe = new ArrayList<NhaXeEntities>();
        sdt = new ArrayList<String>();
        nhaXe = new NhaXeEntities();
        boolean flag = false;   // đánh dấu khi tìm thấy tỉnh đi

        // định dạng lại tỉnh đi và đến
        tinhdi = VNCharacterUtils.removeAccent(tinhdi);
        tinhden = VNCharacterUtils.removeAccent(tinhden);

        // Đổi saigon -> hochiminh
        if(tinhdi.equals("saigon")){
            tinhdi = "hochiminh";
        }
        if(tinhden.equals("saigon")){
            tinhden = "hochiminh";
        }

        // Lấy dữ liệu file xml
        Resources res = activity.getResources();
        int resId = res.getIdentifier(tinhdi, "xml",
                activity.getPackageName());
        XmlResourceParser parser = res.getXml(resId);

        parser.next(); //bắt đầu duyệt
        int eventType = parser.getEventType();
        while (eventType != XmlPullParser.END_DOCUMENT) {
            String name = null;
            switch (eventType) {
                case XmlPullParser.START_TAG:
                    name = parser.getName();
                    if (name.equals(tinhden)) { // tìm được tinhden
                        flag = true;    // bật flag
                    }
                    if (flag) {
                        if (name.equals("nhaxe")) {
                            nhaXe = new NhaXeEntities();
                            sdt = new ArrayList<String>();
                        }
                        if (name.equals("ten")) {
                            nhaXe.setTen(parser.nextText());
                        }
                        if (name.equals("thoigian")) {
                            nhaXe.setThoigian(parser.nextText());
                        }
                        if (name.equals("diadiemdi")) {
                            nhaXe.setDiadiemdi(parser.nextText());
                        }
                        if (name.equals("diadiemden")) {
                            nhaXe.setDiadiemden(parser.nextText());
                        }
                        if (name.equals("dienthoai1")) {
                            sdt.add(parser.nextText());
                        }
                        if (name.equals("dienthoai2")) {
                            String sdt2 = parser.nextText();
                            if (!sdt.equals("")) {
                                sdt.add(sdt2);
                            }
                        }
                        if (name.equals("giave")) {
                            nhaXe.setGiave(parser.nextText());
                        }
                    }
                    break;
                case XmlPullParser.END_TAG:
                    name = parser.getName();
                    if (flag) {
                        if (name.equals("nhaxe")) { // Kết thúc tag nhaxe
                            nhaXe.setDienthoai(sdt);
                            arrNhaXe.add(nhaXe);
                        }
                        if (name.equals(tinhden)) { // Kết thúc tag tinhden
                            return arrNhaXe;
                        }
                    }
                    break;
            }
            eventType = parser.next();
        }

        return arrNhaXe;
    }

}
