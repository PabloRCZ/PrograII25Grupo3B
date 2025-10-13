/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package gt.edu.miumg.db;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.UniqueConstraint;

/**
 *
 * @author pabli
 */
@Entity
@Table(name = "InventarioAnimales", catalog = "granja_db", schema = "dbo", uniqueConstraints = {
    @UniqueConstraint(columnNames = {"identificador_unico"})})
@NamedQueries({
    @NamedQuery(name = "InventarioAnimales.findAll", query = "SELECT i FROM InventarioAnimales i"),
    @NamedQuery(name = "InventarioAnimales.findById", query = "SELECT i FROM InventarioAnimales i WHERE i.id = :id"),
    @NamedQuery(name = "InventarioAnimales.findByEspecie", query = "SELECT i FROM InventarioAnimales i WHERE i.especie = :especie"),
    @NamedQuery(name = "InventarioAnimales.findByRaza", query = "SELECT i FROM InventarioAnimales i WHERE i.raza = :raza"),
    @NamedQuery(name = "InventarioAnimales.findByPeso", query = "SELECT i FROM InventarioAnimales i WHERE i.peso = :peso"),
    @NamedQuery(name = "InventarioAnimales.findByEdad", query = "SELECT i FROM InventarioAnimales i WHERE i.edad = :edad"),
    @NamedQuery(name = "InventarioAnimales.findByEstado", query = "SELECT i FROM InventarioAnimales i WHERE i.estado = :estado"),
    @NamedQuery(name = "InventarioAnimales.findByFechaIngreso", query = "SELECT i FROM InventarioAnimales i WHERE i.fechaIngreso = :fechaIngreso"),
    @NamedQuery(name = "InventarioAnimales.findByIdentificadorUnico", query = "SELECT i FROM InventarioAnimales i WHERE i.identificadorUnico = :identificadorUnico"),
    @NamedQuery(name = "InventarioAnimales.findByObservaciones", query = "SELECT i FROM InventarioAnimales i WHERE i.observaciones = :observaciones"),
    @NamedQuery(name = "InventarioAnimales.findByCreatedAt", query = "SELECT i FROM InventarioAnimales i WHERE i.createdAt = :createdAt"),
    @NamedQuery(name = "InventarioAnimales.findByUpdatedAt", query = "SELECT i FROM InventarioAnimales i WHERE i.updatedAt = :updatedAt")})
public class InventarioAnimales implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id", nullable = false)
    private Integer id;
    @Basic(optional = false)
    @Column(name = "especie", nullable = false, length = 50)
    private String especie;
    @Column(name = "raza", length = 50)
    private String raza;
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Column(name = "peso", precision = 6, scale = 2)
    private BigDecimal peso;
    @Column(name = "edad")
    private Integer edad;
    @Column(name = "estado", length = 20)
    private String estado;
    @Basic(optional = false)
    @Column(name = "fecha_ingreso", nullable = false)
    @Temporal(TemporalType.DATE)
    private Date fechaIngreso;
    @Basic(optional = false)
    @Column(name = "identificador_unico", nullable = false, length = 50)
    private String identificadorUnico;
    @Column(name = "observaciones", length = 255)
    private String observaciones;
    @Column(name = "created_at")
    @Temporal(TemporalType.TIMESTAMP)
    private Date createdAt;
    @Column(name = "updated_at")
    @Temporal(TemporalType.TIMESTAMP)
    private Date updatedAt;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "animalId", fetch = FetchType.LAZY)
    private List<EventoAnimal> eventoAnimalList;
    @OneToMany(mappedBy = "animalId", fetch = FetchType.LAZY)
    private List<DetalleCompra> detalleCompraList;
    @OneToMany(mappedBy = "animalId", fetch = FetchType.LAZY)
    private List<DetalleVenta> detalleVentaList;

    public InventarioAnimales() {
    }

    public InventarioAnimales(Integer id) {
        this.id = id;
    }

    public InventarioAnimales(Integer id, String especie, Date fechaIngreso, String identificadorUnico) {
        this.id = id;
        this.especie = especie;
        this.fechaIngreso = fechaIngreso;
        this.identificadorUnico = identificadorUnico;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getEspecie() {
        return especie;
    }

    public void setEspecie(String especie) {
        this.especie = especie;
    }

    public String getRaza() {
        return raza;
    }

    public void setRaza(String raza) {
        this.raza = raza;
    }

    public BigDecimal getPeso() {
        return peso;
    }

    public void setPeso(BigDecimal peso) {
        this.peso = peso;
    }

    public Integer getEdad() {
        return edad;
    }

    public void setEdad(Integer edad) {
        this.edad = edad;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    public Date getFechaIngreso() {
        return fechaIngreso;
    }

    public void setFechaIngreso(Date fechaIngreso) {
        this.fechaIngreso = fechaIngreso;
    }

    public String getIdentificadorUnico() {
        return identificadorUnico;
    }

    public void setIdentificadorUnico(String identificadorUnico) {
        this.identificadorUnico = identificadorUnico;
    }

    public String getObservaciones() {
        return observaciones;
    }

    public void setObservaciones(String observaciones) {
        this.observaciones = observaciones;
    }

    public Date getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }

    public Date getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(Date updatedAt) {
        this.updatedAt = updatedAt;
    }

    public List<EventoAnimal> getEventoAnimalList() {
        return eventoAnimalList;
    }

    public void setEventoAnimalList(List<EventoAnimal> eventoAnimalList) {
        this.eventoAnimalList = eventoAnimalList;
    }

    public List<DetalleCompra> getDetalleCompraList() {
        return detalleCompraList;
    }

    public void setDetalleCompraList(List<DetalleCompra> detalleCompraList) {
        this.detalleCompraList = detalleCompraList;
    }

    public List<DetalleVenta> getDetalleVentaList() {
        return detalleVentaList;
    }

    public void setDetalleVentaList(List<DetalleVenta> detalleVentaList) {
        this.detalleVentaList = detalleVentaList;
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
        if (!(object instanceof InventarioAnimales)) {
            return false;
        }
        InventarioAnimales other = (InventarioAnimales) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "gt.edu.miumg.db.InventarioAnimales[ id=" + id + " ]";
    }
    
}
