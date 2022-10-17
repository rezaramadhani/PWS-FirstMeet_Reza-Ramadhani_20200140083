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
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToOne;
import javax.persistence.Table;

/**
 *
 * @author Reza Ramadhani
 */
@Entity
@Table(name = "pembeli")
@NamedQueries({
    @NamedQuery(name = "Pembeli.findAll", query = "SELECT p FROM Pembeli p"),
    @NamedQuery(name = "Pembeli.findByIdPembeli", query = "SELECT p FROM Pembeli p WHERE p.idPembeli = :idPembeli"),
    @NamedQuery(name = "Pembeli.findByNamaPembeli", query = "SELECT p FROM Pembeli p WHERE p.namaPembeli = :namaPembeli"),
    @NamedQuery(name = "Pembeli.findByNoHpPembeli", query = "SELECT p FROM Pembeli p WHERE p.noHpPembeli = :noHpPembeli"),
    @NamedQuery(name = "Pembeli.findByAlamatPembeli", query = "SELECT p FROM Pembeli p WHERE p.alamatPembeli = :alamatPembeli")})
public class Pembeli implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @Column(name = "Id_Pembeli")
    private String idPembeli;
    @Basic(optional = false)
    @Column(name = "Nama_Pembeli")
    private String namaPembeli;
    @Basic(optional = false)
    @Column(name = "NoHp_Pembeli")
    private String noHpPembeli;
    @Basic(optional = false)
    @Column(name = "Alamat_Pembeli")
    private String alamatPembeli;
    @OneToOne(cascade = CascadeType.ALL, mappedBy = "idPembeli")
    private Transaksi transaksi;

    public Pembeli() {
    }

    public Pembeli(String idPembeli) {
        this.idPembeli = idPembeli;
    }

    public Pembeli(String idPembeli, String namaPembeli, String noHpPembeli, String alamatPembeli) {
        this.idPembeli = idPembeli;
        this.namaPembeli = namaPembeli;
        this.noHpPembeli = noHpPembeli;
        this.alamatPembeli = alamatPembeli;
    }

    public String getIdPembeli() {
        return idPembeli;
    }

    public void setIdPembeli(String idPembeli) {
        this.idPembeli = idPembeli;
    }

    public String getNamaPembeli() {
        return namaPembeli;
    }

    public void setNamaPembeli(String namaPembeli) {
        this.namaPembeli = namaPembeli;
    }

    public String getNoHpPembeli() {
        return noHpPembeli;
    }

    public void setNoHpPembeli(String noHpPembeli) {
        this.noHpPembeli = noHpPembeli;
    }

    public String getAlamatPembeli() {
        return alamatPembeli;
    }

    public void setAlamatPembeli(String alamatPembeli) {
        this.alamatPembeli = alamatPembeli;
    }

    public Transaksi getTransaksi() {
        return transaksi;
    }

    public void setTransaksi(Transaksi transaksi) {
        this.transaksi = transaksi;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (idPembeli != null ? idPembeli.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Pembeli)) {
            return false;
        }
        Pembeli other = (Pembeli) object;
        if ((this.idPembeli == null && other.idPembeli != null) || (this.idPembeli != null && !this.idPembeli.equals(other.idPembeli))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "PWS_C.PWSFIrstmeet.Pembeli[ idPembeli=" + idPembeli + " ]";
    }
    
}
