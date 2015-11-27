package com.example.tranquoctin.timxekhach.entities;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by sapui on 10/28/2015.
 */
public class NhaXeEntities implements Serializable {
    private String ten;
    private String thoigian;
    private String diadiemdi;
    private String diadiemden;
    private String giave;
    private ArrayList<String> dienthoai;

    public NhaXeEntities(String ten, String thoigian,
                         String diadiemdi, String diadiemden,
                         String giave, ArrayList<String> dienthoai) {
        this.ten = ten;
        this.thoigian = thoigian;
        this.diadiemdi = diadiemdi;
        this.diadiemden = diadiemden;
        this.giave = giave;
        this.dienthoai = dienthoai;
    }

    public NhaXeEntities() {
    }

    public String getTen() {
        return ten;
    }

    public void setTen(String ten) {
        this.ten = ten;
    }

    public String getThoigian() {
        return thoigian;
    }

    public void setThoigian(String thoigian) {
        this.thoigian = thoigian;
    }

    public String getDiadiemdi() {
        return diadiemdi;
    }

    public void setDiadiemdi(String diadiemdi) {
        this.diadiemdi = diadiemdi;
    }

    public String getDiadiemden() {
        return diadiemden;
    }

    public void setDiadiemden(String diadiemden) {
        this.diadiemden = diadiemden;
    }

    public String getGiave() {
        return giave+ " đ";
    }

    public void setGiave(String giave) {
        this.giave = giave;
    }

    public ArrayList<String> getDienthoai() {
        return dienthoai;
    }

    public void setDienthoai(ArrayList<String> dienthoai) {
        this.dienthoai = dienthoai;
    }
}
