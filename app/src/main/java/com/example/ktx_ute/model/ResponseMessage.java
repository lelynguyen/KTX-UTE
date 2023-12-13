package com.example.ktx_ute.model;

public class ResponseMessage{
    private String tinNhanID;
    private String sinhVienID;
    private String phongID;
    private String noiDung;
    private String thoiGian;

    public String getTinNhanID() {
        return tinNhanID;
    }

    public void setTinNhanID(String tinNhanID) {
        this.tinNhanID = tinNhanID;
    }

    public String getSinhVienID() {
        return sinhVienID;
    }

    public void setSinhVienID(String sinhVienID) {
        this.sinhVienID = sinhVienID;
    }

    public String getPhongID() {
        return phongID;
    }

    public void setPhongID(String phongID) {
        this.phongID = phongID;
    }

    public String getNoiDung() {
        return noiDung;
    }

    public void setNoiDung(String noiDung) {
        this.noiDung = noiDung;
    }

    public String getThoiGian() {
        return thoiGian;
    }

    public void setThoiGian(String thoiGian) {
        this.thoiGian = thoiGian;
    }
}
