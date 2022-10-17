/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package PWS_C.PWSFIrstmeet;

import java.io.Serializable;
import javax.persistence.Basic;
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
@Table(name = "transaksi")
@NamedQueries({
    @NamedQuery(name = "Transaksi.findAll", query = "SELECT t FROM Transaksi t"),
    @NamedQuery(name = "Transaksi.findByIdTransaksi", query = "SELECT t FROM Transaksi t WHERE t.idTransaksi = :idTransaksi"),
    @NamedQuery(name = "Transaksi.findByQty", query = "SELECT t FROM Transaksi t WHERE t.qty = :qty"),
    @NamedQuery(name = "Transaksi.findByAlamatPembeli", query = "SELECT t FROM Transaksi t WHERE t.alamatPembeli = :alamatPembeli"),
    @NamedQuery(name = "Transaksi.findByNoHpPembeli", query = "SELECT t FROM Transaksi t WHERE t.noHpPembeli = :noHpPembeli")})
public class Transaksi implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @Column(name = "Id_Transaksi")
    private String idTransaksi;
    @Basic(optional = false)
    @Column(name = "Qty")
    private int qty;
    @Basic(optional = false)
    @Column(name = "Alamat_Pembeli")
    private String alamatPembeli;
    @Basic(optional = false)
    @Column(name = "NoHp_Pembeli")
    private String noHpPembeli;
    @JoinColumn(name = "Kode_Barang", referencedColumnName = "Kode_Barang")
    @OneToOne(optional = false)
    private Barang kodeBarang;
    @JoinColumn(name = "Id_Pembeli", referencedColumnName = "Id_Pembeli")
    @OneToOne(optional = false)
    private Pembeli idPembeli;

    public Transaksi() {
    }

    public Transaksi(String idTransaksi) {
        this.idTransaksi = idTransaksi;
    }

    public Transaksi(String idTransaksi, int qty, String alamatPembeli, String noHpPembeli) {
        this.idTransaksi = idTransaksi;
        this.qty = qty;
        this.alamatPembeli = alamatPembeli;
        this.noHpPembeli = noHpPembeli;
    }

    public String getIdTransaksi() {
        return idTransaksi;
    }

    public void setIdTransaksi(String idTransaksi) {
        this.idTransaksi = idTransaksi;
    }

    public int getQty() {
        return qty;
    }

    public void setQty(int qty) {
        this.qty = qty;
    }

    public String getAlamatPembeli() {
        return alamatPembeli;
    }

    public void setAlamatPembeli(String alamatPembeli) {
        this.alamatPembeli = alamatPembeli;
    }

    public String getNoHpPembeli() {
        return noHpPembeli;
    }

    public void setNoHpPembeli(String noHpPembeli) {
        this.noHpPembeli = noHpPembeli;
    }

    public Barang getKodeBarang() {
        return kodeBarang;
    }

    public void setKodeBarang(Barang kodeBarang) {
        this.kodeBarang = kodeBarang;
    }

    public Pembeli getIdPembeli() {
        return idPembeli;
    }

    public void setIdPembeli(Pembeli idPembeli) {
        this.idPembeli = idPembeli;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (idTransaksi != null ? idTransaksi.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Transaksi)) {
            return false;
        }
        Transaksi other = (Transaksi) object;
        if ((this.idTransaksi == null && other.idTransaksi != null) || (this.idTransaksi != null && !this.idTransaksi.equals(other.idTransaksi))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "PWS_C.PWSFIrstmeet.Transaksi[ idTransaksi=" + idTransaksi + " ]";
    }
    
}
