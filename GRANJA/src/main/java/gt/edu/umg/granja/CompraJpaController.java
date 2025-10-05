/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package gt.edu.umg.granja;

import gt.edu.umg.granja.ENTITY.Compra;
import java.io.Serializable;
import javax.persistence.Query;
import javax.persistence.EntityNotFoundException;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import gt.edu.umg.granja.ENTITY.Factura;
import gt.edu.umg.granja.ENTITY.Proveedor;
import gt.edu.umg.granja.ENTITY.Usuario;
import gt.edu.umg.granja.ENTITY.DetalleCompra;
import gt.edu.umg.granja.exceptions.IllegalOrphanException;
import gt.edu.umg.granja.exceptions.NonexistentEntityException;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

/**
 *
 * @author caste
 */
public class CompraJpaController implements Serializable {

    public CompraJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Compra compra) {
        if (compra.getDetalleCompraList() == null) {
            compra.setDetalleCompraList(new ArrayList<DetalleCompra>());
        }
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Factura facturaId = compra.getFacturaId();
            if (facturaId != null) {
                facturaId = em.getReference(facturaId.getClass(), facturaId.getId());
                compra.setFacturaId(facturaId);
            }
            Proveedor proveedorId = compra.getProveedorId();
            if (proveedorId != null) {
                proveedorId = em.getReference(proveedorId.getClass(), proveedorId.getId());
                compra.setProveedorId(proveedorId);
            }
            Usuario usuarioId = compra.getUsuarioId();
            if (usuarioId != null) {
                usuarioId = em.getReference(usuarioId.getClass(), usuarioId.getId());
                compra.setUsuarioId(usuarioId);
            }
            List<DetalleCompra> attachedDetalleCompraList = new ArrayList<DetalleCompra>();
            for (DetalleCompra detalleCompraListDetalleCompraToAttach : compra.getDetalleCompraList()) {
                detalleCompraListDetalleCompraToAttach = em.getReference(detalleCompraListDetalleCompraToAttach.getClass(), detalleCompraListDetalleCompraToAttach.getId());
                attachedDetalleCompraList.add(detalleCompraListDetalleCompraToAttach);
            }
            compra.setDetalleCompraList(attachedDetalleCompraList);
            em.persist(compra);
            if (facturaId != null) {
                Compra oldCompraOfFacturaId = facturaId.getCompra();
                if (oldCompraOfFacturaId != null) {
                    oldCompraOfFacturaId.setFacturaId(null);
                    oldCompraOfFacturaId = em.merge(oldCompraOfFacturaId);
                }
                facturaId.setCompra(compra);
                facturaId = em.merge(facturaId);
            }
            if (proveedorId != null) {
                proveedorId.getCompraList().add(compra);
                proveedorId = em.merge(proveedorId);
            }
            if (usuarioId != null) {
                usuarioId.getCompraList().add(compra);
                usuarioId = em.merge(usuarioId);
            }
            for (DetalleCompra detalleCompraListDetalleCompra : compra.getDetalleCompraList()) {
                Compra oldCompraIdOfDetalleCompraListDetalleCompra = detalleCompraListDetalleCompra.getCompraId();
                detalleCompraListDetalleCompra.setCompraId(compra);
                detalleCompraListDetalleCompra = em.merge(detalleCompraListDetalleCompra);
                if (oldCompraIdOfDetalleCompraListDetalleCompra != null) {
                    oldCompraIdOfDetalleCompraListDetalleCompra.getDetalleCompraList().remove(detalleCompraListDetalleCompra);
                    oldCompraIdOfDetalleCompraListDetalleCompra = em.merge(oldCompraIdOfDetalleCompraListDetalleCompra);
                }
            }
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(Compra compra) throws IllegalOrphanException, NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Compra persistentCompra = em.find(Compra.class, compra.getId());
            Factura facturaIdOld = persistentCompra.getFacturaId();
            Factura facturaIdNew = compra.getFacturaId();
            Proveedor proveedorIdOld = persistentCompra.getProveedorId();
            Proveedor proveedorIdNew = compra.getProveedorId();
            Usuario usuarioIdOld = persistentCompra.getUsuarioId();
            Usuario usuarioIdNew = compra.getUsuarioId();
            List<DetalleCompra> detalleCompraListOld = persistentCompra.getDetalleCompraList();
            List<DetalleCompra> detalleCompraListNew = compra.getDetalleCompraList();
            List<String> illegalOrphanMessages = null;
            for (DetalleCompra detalleCompraListOldDetalleCompra : detalleCompraListOld) {
                if (!detalleCompraListNew.contains(detalleCompraListOldDetalleCompra)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain DetalleCompra " + detalleCompraListOldDetalleCompra + " since its compraId field is not nullable.");
                }
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            if (facturaIdNew != null) {
                facturaIdNew = em.getReference(facturaIdNew.getClass(), facturaIdNew.getId());
                compra.setFacturaId(facturaIdNew);
            }
            if (proveedorIdNew != null) {
                proveedorIdNew = em.getReference(proveedorIdNew.getClass(), proveedorIdNew.getId());
                compra.setProveedorId(proveedorIdNew);
            }
            if (usuarioIdNew != null) {
                usuarioIdNew = em.getReference(usuarioIdNew.getClass(), usuarioIdNew.getId());
                compra.setUsuarioId(usuarioIdNew);
            }
            List<DetalleCompra> attachedDetalleCompraListNew = new ArrayList<DetalleCompra>();
            for (DetalleCompra detalleCompraListNewDetalleCompraToAttach : detalleCompraListNew) {
                detalleCompraListNewDetalleCompraToAttach = em.getReference(detalleCompraListNewDetalleCompraToAttach.getClass(), detalleCompraListNewDetalleCompraToAttach.getId());
                attachedDetalleCompraListNew.add(detalleCompraListNewDetalleCompraToAttach);
            }
            detalleCompraListNew = attachedDetalleCompraListNew;
            compra.setDetalleCompraList(detalleCompraListNew);
            compra = em.merge(compra);
            if (facturaIdOld != null && !facturaIdOld.equals(facturaIdNew)) {
                facturaIdOld.setCompra(null);
                facturaIdOld = em.merge(facturaIdOld);
            }
            if (facturaIdNew != null && !facturaIdNew.equals(facturaIdOld)) {
                Compra oldCompraOfFacturaId = facturaIdNew.getCompra();
                if (oldCompraOfFacturaId != null) {
                    oldCompraOfFacturaId.setFacturaId(null);
                    oldCompraOfFacturaId = em.merge(oldCompraOfFacturaId);
                }
                facturaIdNew.setCompra(compra);
                facturaIdNew = em.merge(facturaIdNew);
            }
            if (proveedorIdOld != null && !proveedorIdOld.equals(proveedorIdNew)) {
                proveedorIdOld.getCompraList().remove(compra);
                proveedorIdOld = em.merge(proveedorIdOld);
            }
            if (proveedorIdNew != null && !proveedorIdNew.equals(proveedorIdOld)) {
                proveedorIdNew.getCompraList().add(compra);
                proveedorIdNew = em.merge(proveedorIdNew);
            }
            if (usuarioIdOld != null && !usuarioIdOld.equals(usuarioIdNew)) {
                usuarioIdOld.getCompraList().remove(compra);
                usuarioIdOld = em.merge(usuarioIdOld);
            }
            if (usuarioIdNew != null && !usuarioIdNew.equals(usuarioIdOld)) {
                usuarioIdNew.getCompraList().add(compra);
                usuarioIdNew = em.merge(usuarioIdNew);
            }
            for (DetalleCompra detalleCompraListNewDetalleCompra : detalleCompraListNew) {
                if (!detalleCompraListOld.contains(detalleCompraListNewDetalleCompra)) {
                    Compra oldCompraIdOfDetalleCompraListNewDetalleCompra = detalleCompraListNewDetalleCompra.getCompraId();
                    detalleCompraListNewDetalleCompra.setCompraId(compra);
                    detalleCompraListNewDetalleCompra = em.merge(detalleCompraListNewDetalleCompra);
                    if (oldCompraIdOfDetalleCompraListNewDetalleCompra != null && !oldCompraIdOfDetalleCompraListNewDetalleCompra.equals(compra)) {
                        oldCompraIdOfDetalleCompraListNewDetalleCompra.getDetalleCompraList().remove(detalleCompraListNewDetalleCompra);
                        oldCompraIdOfDetalleCompraListNewDetalleCompra = em.merge(oldCompraIdOfDetalleCompraListNewDetalleCompra);
                    }
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Integer id = compra.getId();
                if (findCompra(id) == null) {
                    throw new NonexistentEntityException("The compra with id " + id + " no longer exists.");
                }
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void destroy(Integer id) throws IllegalOrphanException, NonexistentEntityException {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Compra compra;
            try {
                compra = em.getReference(Compra.class, id);
                compra.getId();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The compra with id " + id + " no longer exists.", enfe);
            }
            List<String> illegalOrphanMessages = null;
            List<DetalleCompra> detalleCompraListOrphanCheck = compra.getDetalleCompraList();
            for (DetalleCompra detalleCompraListOrphanCheckDetalleCompra : detalleCompraListOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This Compra (" + compra + ") cannot be destroyed since the DetalleCompra " + detalleCompraListOrphanCheckDetalleCompra + " in its detalleCompraList field has a non-nullable compraId field.");
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            Factura facturaId = compra.getFacturaId();
            if (facturaId != null) {
                facturaId.setCompra(null);
                facturaId = em.merge(facturaId);
            }
            Proveedor proveedorId = compra.getProveedorId();
            if (proveedorId != null) {
                proveedorId.getCompraList().remove(compra);
                proveedorId = em.merge(proveedorId);
            }
            Usuario usuarioId = compra.getUsuarioId();
            if (usuarioId != null) {
                usuarioId.getCompraList().remove(compra);
                usuarioId = em.merge(usuarioId);
            }
            em.remove(compra);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<Compra> findCompraEntities() {
        return findCompraEntities(true, -1, -1);
    }

    public List<Compra> findCompraEntities(int maxResults, int firstResult) {
        return findCompraEntities(false, maxResults, firstResult);
    }

    private List<Compra> findCompraEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Compra.class));
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

    public Compra findCompra(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Compra.class, id);
        } finally {
            em.close();
        }
    }

    public int getCompraCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Compra> rt = cq.from(Compra.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
