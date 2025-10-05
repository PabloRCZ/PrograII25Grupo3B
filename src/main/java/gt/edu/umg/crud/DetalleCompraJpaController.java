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
import gt.edu.umg.crud.test.Compra;
import gt.edu.umg.crud.test.DetalleCompra;
import gt.edu.umg.crud.test.Inventario;
import gt.edu.umg.crud.test.InventarioAnimales;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

/**
 *
 * @author caste
 */
public class DetalleCompraJpaController implements Serializable {

    public DetalleCompraJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(DetalleCompra detalleCompra) {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Compra compraId = detalleCompra.getCompraId();
            if (compraId != null) {
                compraId = em.getReference(compraId.getClass(), compraId.getId());
                detalleCompra.setCompraId(compraId);
            }
            Inventario productoId = detalleCompra.getProductoId();
            if (productoId != null) {
                productoId = em.getReference(productoId.getClass(), productoId.getId());
                detalleCompra.setProductoId(productoId);
            }
            InventarioAnimales animalId = detalleCompra.getAnimalId();
            if (animalId != null) {
                animalId = em.getReference(animalId.getClass(), animalId.getId());
                detalleCompra.setAnimalId(animalId);
            }
            em.persist(detalleCompra);
            if (compraId != null) {
                compraId.getDetalleCompraList().add(detalleCompra);
                compraId = em.merge(compraId);
            }
            if (productoId != null) {
                productoId.getDetalleCompraList().add(detalleCompra);
                productoId = em.merge(productoId);
            }
            if (animalId != null) {
                animalId.getDetalleCompraList().add(detalleCompra);
                animalId = em.merge(animalId);
            }
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(DetalleCompra detalleCompra) throws NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            DetalleCompra persistentDetalleCompra = em.find(DetalleCompra.class, detalleCompra.getId());
            Compra compraIdOld = persistentDetalleCompra.getCompraId();
            Compra compraIdNew = detalleCompra.getCompraId();
            Inventario productoIdOld = persistentDetalleCompra.getProductoId();
            Inventario productoIdNew = detalleCompra.getProductoId();
            InventarioAnimales animalIdOld = persistentDetalleCompra.getAnimalId();
            InventarioAnimales animalIdNew = detalleCompra.getAnimalId();
            if (compraIdNew != null) {
                compraIdNew = em.getReference(compraIdNew.getClass(), compraIdNew.getId());
                detalleCompra.setCompraId(compraIdNew);
            }
            if (productoIdNew != null) {
                productoIdNew = em.getReference(productoIdNew.getClass(), productoIdNew.getId());
                detalleCompra.setProductoId(productoIdNew);
            }
            if (animalIdNew != null) {
                animalIdNew = em.getReference(animalIdNew.getClass(), animalIdNew.getId());
                detalleCompra.setAnimalId(animalIdNew);
            }
            detalleCompra = em.merge(detalleCompra);
            if (compraIdOld != null && !compraIdOld.equals(compraIdNew)) {
                compraIdOld.getDetalleCompraList().remove(detalleCompra);
                compraIdOld = em.merge(compraIdOld);
            }
            if (compraIdNew != null && !compraIdNew.equals(compraIdOld)) {
                compraIdNew.getDetalleCompraList().add(detalleCompra);
                compraIdNew = em.merge(compraIdNew);
            }
            if (productoIdOld != null && !productoIdOld.equals(productoIdNew)) {
                productoIdOld.getDetalleCompraList().remove(detalleCompra);
                productoIdOld = em.merge(productoIdOld);
            }
            if (productoIdNew != null && !productoIdNew.equals(productoIdOld)) {
                productoIdNew.getDetalleCompraList().add(detalleCompra);
                productoIdNew = em.merge(productoIdNew);
            }
            if (animalIdOld != null && !animalIdOld.equals(animalIdNew)) {
                animalIdOld.getDetalleCompraList().remove(detalleCompra);
                animalIdOld = em.merge(animalIdOld);
            }
            if (animalIdNew != null && !animalIdNew.equals(animalIdOld)) {
                animalIdNew.getDetalleCompraList().add(detalleCompra);
                animalIdNew = em.merge(animalIdNew);
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Integer id = detalleCompra.getId();
                if (findDetalleCompra(id) == null) {
                    throw new NonexistentEntityException("The detalleCompra with id " + id + " no longer exists.");
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
            DetalleCompra detalleCompra;
            try {
                detalleCompra = em.getReference(DetalleCompra.class, id);
                detalleCompra.getId();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The detalleCompra with id " + id + " no longer exists.", enfe);
            }
            Compra compraId = detalleCompra.getCompraId();
            if (compraId != null) {
                compraId.getDetalleCompraList().remove(detalleCompra);
                compraId = em.merge(compraId);
            }
            Inventario productoId = detalleCompra.getProductoId();
            if (productoId != null) {
                productoId.getDetalleCompraList().remove(detalleCompra);
                productoId = em.merge(productoId);
            }
            InventarioAnimales animalId = detalleCompra.getAnimalId();
            if (animalId != null) {
                animalId.getDetalleCompraList().remove(detalleCompra);
                animalId = em.merge(animalId);
            }
            em.remove(detalleCompra);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<DetalleCompra> findDetalleCompraEntities() {
        return findDetalleCompraEntities(true, -1, -1);
    }

    public List<DetalleCompra> findDetalleCompraEntities(int maxResults, int firstResult) {
        return findDetalleCompraEntities(false, maxResults, firstResult);
    }

    private List<DetalleCompra> findDetalleCompraEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(DetalleCompra.class));
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

    public DetalleCompra findDetalleCompra(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(DetalleCompra.class, id);
        } finally {
            em.close();
        }
    }

    public int getDetalleCompraCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<DetalleCompra> rt = cq.from(DetalleCompra.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
