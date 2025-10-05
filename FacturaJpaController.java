/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package gt.edu.umg.crud;

import gt.edu.umg.crud.exceptions.NonexistentEntityException;
import java.io.Serializable;
import javax.persistence.Query;
import javax.persistence.EntityNotFoundException;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import gt.edu.umg.crud.test.Venta;
import gt.edu.umg.crud.test.Compra;
import gt.edu.umg.crud.test.Factura;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

/**
 *
 * @author caste
 */
public class FacturaJpaController implements Serializable {

    public FacturaJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Factura factura) {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Venta venta = factura.getVenta();
            if (venta != null) {
                venta = em.getReference(venta.getClass(), venta.getId());
                factura.setVenta(venta);
            }
            Compra compra = factura.getCompra();
            if (compra != null) {
                compra = em.getReference(compra.getClass(), compra.getId());
                factura.setCompra(compra);
            }
            em.persist(factura);
            if (venta != null) {
                Factura oldFacturaIdOfVenta = venta.getFacturaId();
                if (oldFacturaIdOfVenta != null) {
                    oldFacturaIdOfVenta.setVenta(null);
                    oldFacturaIdOfVenta = em.merge(oldFacturaIdOfVenta);
                }
                venta.setFacturaId(factura);
                venta = em.merge(venta);
            }
            if (compra != null) {
                Factura oldFacturaIdOfCompra = compra.getFacturaId();
                if (oldFacturaIdOfCompra != null) {
                    oldFacturaIdOfCompra.setCompra(null);
                    oldFacturaIdOfCompra = em.merge(oldFacturaIdOfCompra);
                }
                compra.setFacturaId(factura);
                compra = em.merge(compra);
            }
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(Factura factura) throws NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Factura persistentFactura = em.find(Factura.class, factura.getId());
            Venta ventaOld = persistentFactura.getVenta();
            Venta ventaNew = factura.getVenta();
            Compra compraOld = persistentFactura.getCompra();
            Compra compraNew = factura.getCompra();
            if (ventaNew != null) {
                ventaNew = em.getReference(ventaNew.getClass(), ventaNew.getId());
                factura.setVenta(ventaNew);
            }
            if (compraNew != null) {
                compraNew = em.getReference(compraNew.getClass(), compraNew.getId());
                factura.setCompra(compraNew);
            }
            factura = em.merge(factura);
            if (ventaOld != null && !ventaOld.equals(ventaNew)) {
                ventaOld.setFacturaId(null);
                ventaOld = em.merge(ventaOld);
            }
            if (ventaNew != null && !ventaNew.equals(ventaOld)) {
                Factura oldFacturaIdOfVenta = ventaNew.getFacturaId();
                if (oldFacturaIdOfVenta != null) {
                    oldFacturaIdOfVenta.setVenta(null);
                    oldFacturaIdOfVenta = em.merge(oldFacturaIdOfVenta);
                }
                ventaNew.setFacturaId(factura);
                ventaNew = em.merge(ventaNew);
            }
            if (compraOld != null && !compraOld.equals(compraNew)) {
                compraOld.setFacturaId(null);
                compraOld = em.merge(compraOld);
            }
            if (compraNew != null && !compraNew.equals(compraOld)) {
                Factura oldFacturaIdOfCompra = compraNew.getFacturaId();
                if (oldFacturaIdOfCompra != null) {
                    oldFacturaIdOfCompra.setCompra(null);
                    oldFacturaIdOfCompra = em.merge(oldFacturaIdOfCompra);
                }
                compraNew.setFacturaId(factura);
                compraNew = em.merge(compraNew);
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Integer id = factura.getId();
                if (findFactura(id) == null) {
                    throw new NonexistentEntityException("The factura with id " + id + " no longer exists.");
                }
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void destroy(Integer id) throws NonexistentEntityException {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Factura factura;
            try {
                factura = em.getReference(Factura.class, id);
                factura.getId();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The factura with id " + id + " no longer exists.", enfe);
            }
            Venta venta = factura.getVenta();
            if (venta != null) {
                venta.setFacturaId(null);
                venta = em.merge(venta);
            }
            Compra compra = factura.getCompra();
            if (compra != null) {
                compra.setFacturaId(null);
                compra = em.merge(compra);
            }
            em.remove(factura);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<Factura> findFacturaEntities() {
        return findFacturaEntities(true, -1, -1);
    }

    public List<Factura> findFacturaEntities(int maxResults, int firstResult) {
        return findFacturaEntities(false, maxResults, firstResult);
    }

    private List<Factura> findFacturaEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Factura.class));
            Query q = em.createQuery(cq);
            if (!all) {
                q.setMaxResults(maxResults);
                q.setFirstResult(firstResult);
            }
            return q.getResultList();
        } finally {
            em.close();
        }
    }

    public Factura findFactura(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Factura.class, id);
        } finally {
            em.close();
        }
    }

    public int getFacturaCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Factura> rt = cq.from(Factura.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
