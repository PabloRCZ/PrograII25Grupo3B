/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package gt.edu.umg.empresa.ENTITY;

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
 * @author caste
 */
@Entity
@Table(name = "BitacoraAcceso")
@NamedQueries({
    @NamedQuery(name = "BitacoraAcceso.findAll", query = "SELECT b FROM BitacoraAcceso b"),
    @NamedQuery(name = "BitacoraAcceso.findById", query = "SELECT b FROM BitacoraAcceso b WHERE b.id = :id"),
    @NamedQuery(name = "BitacoraAcceso.findByFechaAcceso", query = "SELECT b FROM BitacoraAcceso b WHERE b.fechaAcceso = :fechaAcceso"),
    @NamedQuery(name = "BitacoraAcceso.findByAccion", query = "SELECT b FROM BitacoraAcceso b WHERE b.accion = :accion"),
    @NamedQuery(name = "BitacoraAcceso.findByIp", query = "SELECT b FROM BitacoraAcceso b WHERE b.ip = :ip")})
public class BitacoraAcceso implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Integer id;
    @Column(name = "fecha_acceso")
    @Temporal(TemporalType.TIMESTAMP)
    private Date fechaAcceso;
    @Basic(optional = false)
    @Column(name = "accion")
    private String accion;
    @Column(name = "ip")
    private String ip;
    @JoinColumn(name = "usuario_id", referencedColumnName = "id")
    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    private Usuario usuarioId;

    public BitacoraAcceso() {
    }

    public BitacoraAcceso(Integer id) {
        this.id = id;
    }

    public BitacoraAcceso(Integer id, String accion) {
        this.id = id;
        this.accion = accion;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Date getFechaAcceso() {
        return fechaAcceso;
    }

    public void setFechaAcceso(Date fechaAcceso) {
        this.fechaAcceso = fechaAcceso;
    }

    public String getAccion() {
        return accion;
    }

    public void setAccion(String accion) {
        this.accion = accion;
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
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
        return "gt.edu.umg.empresa.ENTITY.BitacoraAcceso[ id=" + id + " ]";
    }
    
}
