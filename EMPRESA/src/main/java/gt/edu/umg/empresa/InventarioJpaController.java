/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package gt.edu.umg.empresa;

import java.io.Serializable;
import javax.persistence.Query;
import javax.persistence.EntityNotFoundException;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import gt.edu.umg.empresa.ENTITY.DetalleVenta;
import java.util.ArrayList;
import java.util.List;
import gt.edu.umg.empresa.ENTITY.DetalleCompra;
import gt.edu.umg.empresa.ENTITY.Inventario;
import gt.edu.umg.empresa.exceptions.NonexistentEntityException;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

/**
 *
 * @author caste
 */
public class InventarioJpaController implements Serializable {

    public InventarioJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Inventario inventario) {
        if (inventario.getDetalleVentaList() == null) {
            inventario.setDetalleVentaList(new ArrayList<DetalleVenta>());
        }
        if (inventario.getDetalleCompraList() == null) {
            inventario.setDetalleCompraList(new ArrayList<DetalleCompra>());
        }
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            List<DetalleVenta> attachedDetalleVentaList = new ArrayList<DetalleVenta>();
            for (DetalleVenta detalleVentaListDetalleVentaToAttach : inventario.getDetalleVentaList()) {
                detalleVentaListDetalleVentaToAttach = em.getReference(detalleVentaListDetalleVentaToAttach.getClass(), detalleVentaListDetalleVentaToAttach.getId());
                attachedDetalleVentaList.add(detalleVentaListDetalleVentaToAttach);
            }
            inventario.setDetalleVentaList(attachedDetalleVentaList);
            List<DetalleCompra> attachedDetalleCompraList = new ArrayList<DetalleCompra>();
            for (DetalleCompra detalleCompraListDetalleCompraToAttach : inventario.getDetalleCompraList()) {
                detalleCompraListDetalleCompraToAttach = em.getReference(detalleCompraListDetalleCompraToAttach.getClass(), detalleCompraListDetalleCompraToAttach.getId());
                attachedDetalleCompraList.add(detalleCompraListDetalleCompraToAttach);
            }
            inventario.setDetalleCompraList(attachedDetalleCompraList);
            em.persist(inventario);
            for (DetalleVenta detalleVentaListDetalleVenta : inventario.getDetalleVentaList()) {
                Inventario oldProductoIdOfDetalleVentaListDetalleVenta = detalleVentaListDetalleVenta.getProductoId();
                detalleVentaListDetalleVenta.setProductoId(inventario);
                detalleVentaListDetalleVenta = em.merge(detalleVentaListDetalleVenta);
                if (oldProductoIdOfDetalleVentaListDetalleVenta != null) {
                    oldProductoIdOfDetalleVentaListDetalleVenta.getDetalleVentaList().remove(detalleVentaListDetalleVenta);
                    oldProductoIdOfDetalleVentaListDetalleVenta = em.merge(oldProductoIdOfDetalleVentaListDetalleVenta);
                }
            }
            for (DetalleCompra detalleCompraListDetalleCompra : inventario.getDetalleCompraList()) {
                Inventario oldProductoIdOfDetalleCompraListDetalleCompra = detalleCompraListDetalleCompra.getProductoId();
                detalleCompraListDetalleCompra.setProductoId(inventario);
                detalleCompraListDetalleCompra = em.merge(detalleCompraListDetalleCompra);
                if (oldProductoIdOfDetalleCompraListDetalleCompra != null) {
                    oldProductoIdOfDetalleCompraListDetalleCompra.getDetalleCompraList().remove(detalleCompraListDetalleCompra);
                    oldProductoIdOfDetalleCompraListDetalleCompra = em.merge(oldProductoIdOfDetalleCompraListDetalleCompra);
                }
            }
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(Inventario inventario) throws NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Inventario persistentInventario = em.find(Inventario.class, inventario.getId());
            List<DetalleVenta> detalleVentaListOld = persistentInventario.getDetalleVentaList();
            List<DetalleVenta> detalleVentaListNew = inventario.getDetalleVentaList();
            List<DetalleCompra> detalleCompraListOld = persistentInventario.getDetalleCompraList();
            List<DetalleCompra> detalleCompraListNew = inventario.getDetalleCompraList();
            List<DetalleVenta> attachedDetalleVentaListNew = new ArrayList<DetalleVenta>();
            for (DetalleVenta detalleVentaListNewDetalleVentaToAttach : detalleVentaListNew) {
                detalleVentaListNewDetalleVentaToAttach = em.getReference(detalleVentaListNewDetalleVentaToAttach.getClass(), detalleVentaListNewDetalleVentaToAttach.getId());
                attachedDetalleVentaListNew.add(detalleVentaListNewDetalleVentaToAttach);
            }
            detalleVentaListNew = attachedDetalleVentaListNew;
            inventario.setDetalleVentaList(detalleVentaListNew);
            List<DetalleCompra> attachedDetalleCompraListNew = new ArrayList<DetalleCompra>();
            for (DetalleCompra detalleCompraListNewDetalleCompraToAttach : detalleCompraListNew) {
                detalleCompraListNewDetalleCompraToAttach = em.getReference(detalleCompraListNewDetalleCompraToAttach.getClass(), detalleCompraListNewDetalleCompraToAttach.getId());
                attachedDetalleCompraListNew.add(detalleCompraListNewDetalleCompraToAttach);
            }
            detalleCompraListNew = attachedDetalleCompraListNew;
            inventario.setDetalleCompraList(detalleCompraListNew);
            inventario = em.merge(inventario);
            for (DetalleVenta detalleVentaListOldDetalleVenta : detalleVentaListOld) {
                if (!detalleVentaListNew.contains(detalleVentaListOldDetalleVenta)) {
                    detalleVentaListOldDetalleVenta.setProductoId(null);
                    detalleVentaListOldDetalleVenta = em.merge(detalleVentaListOldDetalleVenta);
                }
            }
            for (DetalleVenta detalleVentaListNewDetalleVenta : detalleVentaListNew) {
                if (!detalleVentaListOld.contains(detalleVentaListNewDetalleVenta)) {
                    Inventario oldProductoIdOfDetalleVentaListNewDetalleVenta = detalleVentaListNewDetalleVenta.getProductoId();
                    detalleVentaListNewDetalleVenta.setProductoId(inventario);
                    detalleVentaListNewDetalleVenta = em.merge(detalleVentaListNewDetalleVenta);
                    if (oldProductoIdOfDetalleVentaListNewDetalleVenta != null && !oldProductoIdOfDetalleVentaListNewDetalleVenta.equals(inventario)) {
                        oldProductoIdOfDetalleVentaListNewDetalleVenta.getDetalleVentaList().remove(detalleVentaListNewDetalleVenta);
                        oldProductoIdOfDetalleVentaListNewDetalleVenta = em.merge(oldProductoIdOfDetalleVentaListNewDetalleVenta);
                    }
                }
            }
            for (DetalleCompra detalleCompraListOldDetalleCompra : detalleCompraListOld) {
                if (!detalleCompraListNew.contains(detalleCompraListOldDetalleCompra)) {
                    detalleCompraListOldDetalleCompra.setProductoId(null);
                    detalleCompraListOldDetalleCompra = em.merge(detalleCompraListOldDetalleCompra);
                }
            }
            for (DetalleCompra detalleCompraListNewDetalleCompra : detalleCompraListNew) {
                if (!detalleCompraListOld.contains(detalleCompraListNewDetalleCompra)) {
                    Inventario oldProductoIdOfDetalleCompraListNewDetalleCompra = detalleCompraListNewDetalleCompra.getProductoId();
                    detalleCompraListNewDetalleCompra.setProductoId(inventario);
                    detalleCompraListNewDetalleCompra = em.merge(detalleCompraListNewDetalleCompra);
                    if (oldProductoIdOfDetalleCompraListNewDetalleCompra != null && !oldProductoIdOfDetalleCompraListNewDetalleCompra.equals(inventario)) {
                        oldProductoIdOfDetalleCompraListNewDetalleCompra.getDetalleCompraList().remove(detalleCompraListNewDetalleCompra);
                        oldProductoIdOfDetalleCompraListNewDetalleCompra = em.merge(oldProductoIdOfDetalleCompraListNewDetalleCompra);
                    }
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Integer id = inventario.getId();
                if (findInventario(id) == null) {
                    throw new NonexistentEntityException("The inventario with id " + id + " no longer exists.");
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
            Inventario inventario;
            try {
                inventario = em.getReference(Inventario.class, id);
                inventario.getId();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The inventario with id " + id + " no longer exists.", enfe);
            }
            List<DetalleVenta> detalleVentaList = inventario.getDetalleVentaList();
            for (DetalleVenta detalleVentaListDetalleVenta : detalleVentaList) {
                detalleVentaListDetalleVenta.setProductoId(null);
                detalleVentaListDetalleVenta = em.merge(detalleVentaListDetalleVenta);
            }
            List<DetalleCompra> detalleCompraList = inventario.getDetalleCompraList();
            for (DetalleCompra detalleCompraListDetalleCompra : detalleCompraList) {
                detalleCompraListDetalleCompra.setProductoId(null);
                detalleCompraListDetalleCompra = em.merge(detalleCompraListDetalleCompra);
            }
            em.remove(inventario);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<Inventario> findInventarioEntities() {
        return findInventarioEntities(true, -1, -1);
    }

    public List<Inventario> findInventarioEntities(int maxResults, int firstResult) {
        return findInventarioEntities(false, maxResults, firstResult);
    }

    private List<Inventario> findInventarioEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Inventario.class));
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

    public Inventario findInventario(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Inventario.class, id);
        } finally {
            em.close();
        }
    }

    public int getInventarioCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Inventario> rt = cq.from(Inventario.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
