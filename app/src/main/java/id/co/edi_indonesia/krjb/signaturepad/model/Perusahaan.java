package id.co.edi_indonesia.krjb.signaturepad.model;

import java.io.Serializable;

/**
 * Created by Arischa Nur Fadilah on 07/08/2017.
 */

public class Perusahaan implements Serializable {
    public String nama;
    public String keluhan;

    public Perusahaan(String nama, String keluhan) {
        this.nama = nama;
        this.keluhan = keluhan;
    }
}
