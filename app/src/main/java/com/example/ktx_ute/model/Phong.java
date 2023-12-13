package com.example.ktx_ute.model;

import java.io.Serializable;

public class Phong implements Serializable {
    private String phongID;
    private String soPhong;
    private String soLuongHienTai;
    private String soLuongToiDa;
    private String loaiPhong;

    public Phong(String phongID, String soPhong, String soLuongHienTai, String soLuongToiDa, String loaiPhong) {
        this.phongID = phongID;
        this.soPhong = soPhong;
        this.soLuongHienTai = soLuongHienTai;
        this.soLuongToiDa = soLuongToiDa;
        this.loaiPhong = loaiPhong;
    }

    public Phong() {
    }

    public String getPhongID() {
        return phongID;
    }

    public void setPhongID(String phongID) {
        this.phongID = phongID;
    }

    public String getSoPhong() {
        return soPhong;
    }

    public void setSoPhong(String soPhong) {
        this.soPhong = soPhong;
    }

    public String getSoLuongHienTai() {
        return soLuongHienTai;
    }

    public void setSoLuongHienTai(String soLuongHienTai) {
        this.soLuongHienTai = soLuongHienTai;
    }

    public String getSoLuongToiDa() {
        return soLuongToiDa;
    }

    public void setSoLuongToiDa(String soLuongToiDa) {
        this.soLuongToiDa = soLuongToiDa;
    }

    public String getLoaiPhong() {
        return loaiPhong;
    }

    @Override
    public String toString() {
        return "Phong{" +
                "phongID='" + phongID + '\'' +
                '}';
    }

    public void setLoaiPhong(String loaiPhong) {


        this.loaiPhong = loaiPhong;
    }
}
