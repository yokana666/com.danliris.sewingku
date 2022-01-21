package com.danliris.sewingku.object;

public class ProcessItem {

    int id;
    String line;
    String ro;
    int barang_ok;
    int barang_bs;
    int total_komponen;
    int id_proses;

    public ProcessItem(int id, String line, String ro, int barang_ok, int barang_bs, int total_komponen, int id_proses) {
        this.id = id;
        this.line = line;
        this.ro = ro;
        this.barang_ok = barang_ok;
        this.barang_bs = barang_bs;
        this.total_komponen = total_komponen;
        this.id_proses = id_proses;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getLine() {
        return line;
    }

    public void setLine(String line) {
        this.line = line;
    }

    public String getRo() {
        return ro;
    }

    public void setRo(String ro) {
        this.ro = ro;
    }

    public int getBarang_ok() {
        return barang_ok;
    }

    public void setBarang_ok(int barang_ok) {
        this.barang_ok = barang_ok;
    }

    public int getBarang_bs() {
        return barang_bs;
    }

    public void setBarang_bs(int barang_bs) {
        this.barang_bs = barang_bs;
    }

    public int getTotal_komponen() {
        return total_komponen;
    }

    public void setTotal_komponen(int total_komponen) {
        this.total_komponen = total_komponen;
    }

    public int getId_proses() {
        return id_proses;
    }

    public void setId_proses(int id_proses) {
        this.id_proses = id_proses;
    }
}
