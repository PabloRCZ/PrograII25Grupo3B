/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package gt.edu.miumg.db;

import java.io.Serializable;
import java.math.BigDecimal;
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

/**
 *
 * @author pabli
 */
@Entity
@Table(name = "DetalleCompra", catalog = "granja_db", schema = "dbo")
@NamedQueries({
    @NamedQuery(name = "DetalleCompra.findAll", query = "SELECT d FROM DetalleCompra d"),
    @NamedQuery(name = "DetalleCompra.findById", query = "SELECT d FROM DetalleCompra d WHERE d.id = :id"),
    @NamedQuery(name = "DetalleCompra.findByCantidad", query = "SELECT d FROM DetalleCompra d WHERE d.cantidad = :cantidad"),
    @NamedQuery(name = "DetalleCompra.findByPrecioUnitario", query = "SELECT d FROM DetalleCompra d WHERE d.precioUnitario = :precioUnitario"),
    @NamedQuery(name = "DetalleCompra.findBySubtotal", query = "SELECT d FROM DetalleCompra d WHERE d.subtotal = :subtotal")})
public class DetalleCompra implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id", nullable = false)
    private Integer id;
    @Basic(optional = false)
    @Column(name = "cantidad", nullable = false)
    private int cantidad;
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Basic(optional = false)
    @Column(name = "precio_unitario", nullable = false, precision = 10, scale = 2)
    private BigDecimal precioUnitario;
    @Basic(optional = false)
    @Column(name = "subtotal", nullable = false, precision = 10, scale = 2)
    private BigDecimal subtotal;
    @JoinColumn(name = "compra_id", referencedColumnName = "id", nullable = false)
    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    private Compra compraId;
    @JoinColumn(name = "producto_id", referencedColumnName = "id")
    @ManyToOne(fetch = FetchType.LAZY)
    private Inventario productoId;
    @JoinColumn(name = "animal_id", referencedColumnName = "id")
    @ManyToOne(fetch = FetchType.LAZY)
    private InventarioAnimales animalId;

    public DetalleCompra() {
    }

    public DetalleCompra(Integer id) {
        this.id = id;
    }

    public DetalleCompra(Integer id, int cantidad, BigDecimal precioUnitario, BigDecimal subtotal) {
        this.id = id;
        this.cantidad = cantidad;
        this.precioUnitario = precioUnitario;
        this.subtotal = subtotal;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public int getCantidad() {
        return cantidad;
    }

    public void setCantidad(int cantidad) {
        this.cantidad = cantidad;
    }

    public BigDecimal getPrecioUnitario() {
        return precioUnitario;
    }

    public void setPrecioUnitario(BigDecimal precioUnitario) {
        this.precioUnitario = precioUnitario;
    }

    public BigDecimal getSubtotal() {
        return subtotal;
    }

    public void setSubtotal(BigDecimal subtotal) {
        this.subtotal = subtotal;
    }

    public Compra getCompraId() {
        return compraId;
    }

    public void setCompraId(Compra compraId) {
        this.compraId = compraId;
    }

    public Inventario getProductoId() {
        return productoId;
    }

    public void setProductoId(Inventario productoId) {
        this.productoId = productoId;
    }

    public InventarioAnimales getAnimalId() {
        return animalId;
    }

    public void setAnimalId(InventarioAnimales animalId) {
        this.animalId = animalId;
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
        if (!(object instanceof DetalleCompra)) {
            return false;
        }
        DetalleCompra other = (DetalleCompra) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "gt.edu.miumg.db.DetalleCompra[ id=" + id + " ]";
    }
    
}
