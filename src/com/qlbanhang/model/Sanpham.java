package com.qlbanhang.model;

public class Sanpham {
    private String masp;
    private String tensp;
    private int giaban;

    public Sanpham() {

    }

    public Sanpham(String masp, String tensp, int giaban) {
        this.masp = masp;
        this.tensp = tensp;
        this.giaban = giaban;
    }

    public String getMasp() {
        return masp;
    }

    public void setMasp(String masp) {
        this.masp = masp;
    }

    public String getTensp() {
        return tensp;
    }

    public void setTensp(String tensp) {
        this.tensp = tensp;
    }

    public int getGiaban() {
        return giaban;
    }

    public void setGiaban(int giaban) {
        this.giaban = giaban;
    }
}
