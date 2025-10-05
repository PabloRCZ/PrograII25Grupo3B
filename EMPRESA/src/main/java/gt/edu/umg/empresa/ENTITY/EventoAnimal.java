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
@Table(name = "EventoAnimal")
@NamedQueries({
    @NamedQuery(name = "EventoAnimal.findAll", query = "SELECT e FROM EventoAnimal e"),
    @NamedQuery(name = "EventoAnimal.findById", query = "SELECT e FROM EventoAnimal e WHERE e.id = :id"),
    @NamedQuery(name = "EventoAnimal.findByTipo", query = "SELECT e FROM EventoAnimal e WHERE e.tipo = :tipo"),
    @NamedQuery(name = "EventoAnimal.findByDescripcion", query = "SELECT e FROM EventoAnimal e WHERE e.descripcion = :descripcion"),
    @NamedQuery(name = "EventoAnimal.findByFecha", query = "SELECT e FROM EventoAnimal e WHERE e.fecha = :fecha"),
    @NamedQuery(name = "EventoAnimal.findByCreatedAt", query = "SELECT e FROM EventoAnimal e WHERE e.createdAt = :createdAt")})
public class EventoAnimal implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Integer id;
    @Basic(optional = false)
    @Column(name = "tipo")
    private String tipo;
    @Column(name = "descripcion")
    private String descripcion;
    @Basic(optional = false)
    @Column(name = "fecha")
    @Temporal(TemporalType.DATE)
    private Date fecha;
    @Column(name = "created_at")
    @Temporal(TemporalType.TIMESTAMP)
    private Date createdAt;
    @JoinColumn(name = "animal_id", referencedColumnName = "id")
    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    private InventarioAnimales animalId;
    @JoinColumn(name = "creado_por", referencedColumnName = "id")
    @ManyToOne(fetch = FetchType.LAZY)
    private Usuario creadoPor;

    public EventoAnimal() {
    }

    public EventoAnimal(Integer id) {
        this.id = id;
    }

    public EventoAnimal(Integer id, String tipo, Date fecha) {
        this.id = id;
        this.tipo = tipo;
        this.fecha = fecha;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public Date getFecha() {
        return fecha;
    }

    public void setFecha(Date fecha) {
        this.fecha = fecha;
    }

    public Date getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }

    public InventarioAnimales getAnimalId() {
        return animalId;
    }

    public void setAnimalId(InventarioAnimales animalId) {
        this.animalId = animalId;
    }

    public Usuario getCreadoPor() {
        return creadoPor;
    }

    public void setCreadoPor(Usuario creadoPor) {
        this.creadoPor = creadoPor;
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
        if (!(object instanceof EventoAnimal)) {
            return false;
        }
        EventoAnimal other = (EventoAnimal) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "gt.edu.umg.empresa.ENTITY.EventoAnimal[ id=" + id + " ]";
    }
    
}
