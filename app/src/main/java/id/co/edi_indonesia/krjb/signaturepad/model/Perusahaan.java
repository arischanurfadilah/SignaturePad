package id.co.edi_indonesia.krjb.signaturepad.model;

import java.io.Serializable;

/**
 * Created by Arischa Nur Fadilah on 07/08/2017.
 */

public class Perusahaan implements Serializable {

    public String id;
    public String nama;
    public String keluhan;

    public Perusahaan(String id, String nama, String keluhan) {

        this.id = id;
        this.nama = nama;
        this.keluhan = keluhan;
    }
}
