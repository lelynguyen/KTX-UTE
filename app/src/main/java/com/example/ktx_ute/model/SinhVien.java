package com.example.ktx_ute.model;

import java.io.Serializable;

public class SinhVien implements Serializable {
    private String sinhVienID;
    private String hoTenSV;
    private String maSV;
    private String tenDangNhap;
    private String matKhau;
    private String lopID;

    public SinhVien(String sinhVienID, String hoTenSV, String maSV, String tenDangNhap, String matKhau, String lopID) {
        this.sinhVienID = sinhVienID;
        this.hoTenSV = hoTenSV;
        this.maSV = maSV;
        this.tenDangNhap = tenDangNhap;
        this.matKhau = matKhau;
        this.lopID = lopID;
    }

    public SinhVien() {
    }

    public String getSinhVienID() {
        return sinhVienID;
    }

    public void setSinhVienID(String sinhVienID) {
        this.sinhVienID = sinhVienID;
    }

    public String getHoTenSV() {
        return hoTenSV;
    }

    public void setHoTenSV(String hoTenSV) {
        this.hoTenSV = hoTenSV;
    }

    public String getMaSV() {
        return maSV;
    }

    public void setMaSV(String maSV) {
        this.maSV = maSV;
    }

    public String getTenDangNhap() {
        return tenDangNhap;
    }

    public void setTenDangNhap(String tenDangNhap) {
        this.tenDangNhap = tenDangNhap;
    }

    public String getMatKhau() {
        return matKhau;
    }

    public void setMatKhau(String matKhau) {
        this.matKhau = matKhau;
    }

    public String getLopID() {
        return lopID;
    }

    public void setLopID(String lopID) {
        this.lopID = lopID;
    }
}
