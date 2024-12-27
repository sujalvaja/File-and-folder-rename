package com.example.filefolderrename.models;

import android.net.Uri;

public class Filedata {

    public String fname;
    public String size;
    public Uri furi;
    public Filedata(String fname, String size, Uri furi){
        this.fname = fname;
        this.size = size;
        this.furi = furi;
    }
}
