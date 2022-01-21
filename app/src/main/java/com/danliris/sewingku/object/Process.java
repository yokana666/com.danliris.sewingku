package com.danliris.sewingku.object;
import java.sql.Time;
import java.util.Date;

public class Process {

    int id;
    String npk;
    long jam_masuk;
    long jam_keluar;
    Date tanggal;

    public Process(int id, String npk, long jam_masuk, long jam_keluar, Date tanggal) {
        this.id = id;
        this.npk = npk;
        this.jam_masuk = jam_masuk;
        this.jam_keluar = jam_keluar;
        this.tanggal = tanggal;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNpk() {
        return npk;
    }

    public void setNpk(String npk) {
        this.npk = npk;
    }

    public long getJam_masuk() {
        return jam_masuk;
    }

    public void setJam_masuk(long jam_masuk) {
        this.jam_masuk = jam_masuk;
    }

    public long getJam_keluar() {
        return jam_keluar;
    }

    public void setJam_keluar(long jam_keluar) {
        this.jam_keluar = jam_keluar;
    }

    public Date getTanggal() {
        return tanggal;
    }

    public void setTanggal(Date tanggal) {
        this.tanggal = tanggal;
    }
}
