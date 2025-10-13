/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package gt.edu.miumg.db;

import java.io.Serializable;
import java.util.Date;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

/**
 *
 * @author pabli
 */
@Entity
@Table(name = "BitacoraAcceso", catalog = "granja_db", schema = "dbo")
@NamedQueries({
    @NamedQuery(name = "BitacoraAcceso.findAll", query = "SELECT b FROM BitacoraAcceso b"),
    @NamedQuery(name = "BitacoraAcceso.findById", query = "SELECT b FROM BitacoraAcceso b WHERE b.id = :id"),
    @NamedQuery(name = "BitacoraAcceso.findByFecha", query = "SELECT b FROM BitacoraAcceso b WHERE b.fecha = :fecha"),
    @NamedQuery(name = "BitacoraAcceso.findByIpAcceso", query = "SELECT b FROM BitacoraAcceso b WHERE b.ipAcceso = :ipAcceso"),
    @NamedQuery(name = "BitacoraAcceso.findByExito", query = "SELECT b FROM BitacoraAcceso b WHERE b.exito = :exito")})
public class BitacoraAcceso implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id", nullable = false)
    private Integer id;
    @Column(name = "fecha")
    @Temporal(TemporalType.TIMESTAMP)
    private Date fecha;
    @Column(name = "ip_acceso", length = 45)
    private String ipAcceso;
    @Column(name = "exito")
    private Boolean exito;
    @JoinColumn(name = "usuario_id", referencedColumnName = "id", nullable = false)
    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    private Usuario usuarioId;

    public BitacoraAcceso() {
    }

    public BitacoraAcceso(Integer id) {
        this.id = id;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Date getFecha() {
        return fecha;
    }

    public void setFecha(Date fecha) {
        this.fecha = fecha;
    }

    public String getIpAcceso() {
        return ipAcceso;
    }

    public void setIpAcceso(String ipAcceso) {
        this.ipAcceso = ipAcceso;
    }

    public Boolean getExito() {
        return exito;
    }

    public void setExito(Boolean exito) {
        this.exito = exito;
    }

    public Usuario getUsuarioId() {
        return usuarioId;
    }

    public void setUsuarioId(Usuario usuarioId) {
        this.usuarioId = usuarioId;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (id != null ? id.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof BitacoraAcceso)) {
            return false;
        }
        BitacoraAcceso other = (BitacoraAcceso) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "gt.edu.miumg.db.BitacoraAcceso[ id=" + id + " ]";
    }
    
}
