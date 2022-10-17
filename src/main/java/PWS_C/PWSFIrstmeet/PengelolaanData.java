/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package PWS_C.PWSFIrstmeet;

import java.io.Serializable;
import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToOne;
import javax.persistence.Table;

/**
 *
 * @author Reza Ramadhani
 */
@Entity
@Table(name = "pengelolaan_data")
@NamedQueries({
    @NamedQuery(name = "PengelolaanData.findAll", query = "SELECT p FROM PengelolaanData p"),
    @NamedQuery(name = "PengelolaanData.findByIdPengelolaan", query = "SELECT p FROM PengelolaanData p WHERE p.idPengelolaan = :idPengelolaan"),
    @NamedQuery(name = "PengelolaanData.findByKodeBarang", query = "SELECT p FROM PengelolaanData p WHERE p.kodeBarang = :kodeBarang"),
    @NamedQuery(name = "PengelolaanData.findByQty", query = "SELECT p FROM PengelolaanData p WHERE p.qty = :qty")})
public class PengelolaanData implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @Column(name = "Id_Pengelolaan")
    private String idPengelolaan;
    @Basic(optional = false)
    @Column(name = "Kode_Barang")
    private String kodeBarang;
    @Basic(optional = false)
    @Column(name = "Qty")
    private int qty;
    @OneToOne(cascade = CascadeType.ALL, mappedBy = "pengelolaanData")
    private Barang barang;
    @JoinColumn(name = "Id_Pegawai", referencedColumnName = "Id_Pegawai")
    @OneToOne(optional = false)
    private Pegawai idPegawai;

    public PengelolaanData() {
    }

    public PengelolaanData(String idPengelolaan) {
        this.idPengelolaan = idPengelolaan;
    }

    public PengelolaanData(String idPengelolaan, String kodeBarang, int qty) {
        this.idPengelolaan = idPengelolaan;
        this.kodeBarang = kodeBarang;
        this.qty = qty;
    }

    public String getIdPengelolaan() {
        return idPengelolaan;
    }

    public void setIdPengelolaan(String idPengelolaan) {
        this.idPengelolaan = idPengelolaan;
    }

    public String getKodeBarang() {
        return kodeBarang;
    }

    public void setKodeBarang(String kodeBarang) {
        this.kodeBarang = kodeBarang;
    }

    public int getQty() {
        return qty;
    }

    public void setQty(int qty) {
        this.qty = qty;
    }

    public Barang getBarang() {
        return barang;
    }

    public void setBarang(Barang barang) {
        this.barang = barang;
    }

    public Pegawai getIdPegawai() {
        return idPegawai;
    }

    public void setIdPegawai(Pegawai idPegawai) {
        this.idPegawai = idPegawai;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (idPengelolaan != null ? idPengelolaan.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof PengelolaanData)) {
            return false;
        }
        PengelolaanData other = (PengelolaanData) object;
        if ((this.idPengelolaan == null && other.idPengelolaan != null) || (this.idPengelolaan != null && !this.idPengelolaan.equals(other.idPengelolaan))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "PWS_C.PWSFIrstmeet.PengelolaanData[ idPengelolaan=" + idPengelolaan + " ]";
    }
    
}
