/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package gt.edu.umg.granja;

import java.io.Serializable;
import javax.persistence.Query;
import javax.persistence.EntityNotFoundException;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import gt.edu.umg.granja.ENTITY.Cliente;
import gt.edu.umg.granja.ENTITY.Factura;
import gt.edu.umg.granja.ENTITY.Usuario;
import gt.edu.umg.granja.ENTITY.DetalleVenta;
import gt.edu.umg.granja.ENTITY.Venta;
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
public class VentaJpaController implements Serializable {

    public VentaJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Venta venta) {
        if (venta.getDetalleVentaList() == null) {
            venta.setDetalleVentaList(new ArrayList<DetalleVenta>());
        }
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Cliente clienteId = venta.getClienteId();
            if (clienteId != null) {
                clienteId = em.getReference(clienteId.getClass(), clienteId.getId());
                venta.setClienteId(clienteId);
            }
            Factura facturaId = venta.getFacturaId();
            if (facturaId != null) {
                facturaId = em.getReference(facturaId.getClass(), facturaId.getId());
                venta.setFacturaId(facturaId);
            }
            Usuario usuarioId = venta.getUsuarioId();
            if (usuarioId != null) {
                usuarioId = em.getReference(usuarioId.getClass(), usuarioId.getId());
                venta.setUsuarioId(usuarioId);
            }
            List<DetalleVenta> attachedDetalleVentaList = new ArrayList<DetalleVenta>();
            for (DetalleVenta detalleVentaListDetalleVentaToAttach : venta.getDetalleVentaList()) {
                detalleVentaListDetalleVentaToAttach = em.getReference(detalleVentaListDetalleVentaToAttach.getClass(), detalleVentaListDetalleVentaToAttach.getId());
                attachedDetalleVentaList.add(detalleVentaListDetalleVentaToAttach);
            }
            venta.setDetalleVentaList(attachedDetalleVentaList);
            em.persist(venta);
            if (clienteId != null) {
                clienteId.getVentaList().add(venta);
                clienteId = em.merge(clienteId);
            }
            if (facturaId != null) {
                Venta oldVentaOfFacturaId = facturaId.getVenta();
                if (oldVentaOfFacturaId != null) {
                    oldVentaOfFacturaId.setFacturaId(null);
                    oldVentaOfFacturaId = em.merge(oldVentaOfFacturaId);
                }
                facturaId.setVenta(venta);
                facturaId = em.merge(facturaId);
            }
            if (usuarioId != null) {
                usuarioId.getVentaList().add(venta);
                usuarioId = em.merge(usuarioId);
            }
            for (DetalleVenta detalleVentaListDetalleVenta : venta.getDetalleVentaList()) {
                Venta oldVentaIdOfDetalleVentaListDetalleVenta = detalleVentaListDetalleVenta.getVentaId();
                detalleVentaListDetalleVenta.setVentaId(venta);
                detalleVentaListDetalleVenta = em.merge(detalleVentaListDetalleVenta);
                if (oldVentaIdOfDetalleVentaListDetalleVenta != null) {
                    oldVentaIdOfDetalleVentaListDetalleVenta.getDetalleVentaList().remove(detalleVentaListDetalleVenta);
                    oldVentaIdOfDetalleVentaListDetalleVenta = em.merge(oldVentaIdOfDetalleVentaListDetalleVenta);
                }
            }
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(Venta venta) throws IllegalOrphanException, NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Venta persistentVenta = em.find(Venta.class, venta.getId());
            Cliente clienteIdOld = persistentVenta.getClienteId();
            Cliente clienteIdNew = venta.getClienteId();
            Factura facturaIdOld = persistentVenta.getFacturaId();
            Factura facturaIdNew = venta.getFacturaId();
            Usuario usuarioIdOld = persistentVenta.getUsuarioId();
            Usuario usuarioIdNew = venta.getUsuarioId();
            List<DetalleVenta> detalleVentaListOld = persistentVenta.getDetalleVentaList();
            List<DetalleVenta> detalleVentaListNew = venta.getDetalleVentaList();
            List<String> illegalOrphanMessages = null;
            for (DetalleVenta detalleVentaListOldDetalleVenta : detalleVentaListOld) {
                if (!detalleVentaListNew.contains(detalleVentaListOldDetalleVenta)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain DetalleVenta " + detalleVentaListOldDetalleVenta + " since its ventaId field is not nullable.");
                }
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            if (clienteIdNew != null) {
                clienteIdNew = em.getReference(clienteIdNew.getClass(), clienteIdNew.getId());
                venta.setClienteId(clienteIdNew);
            }
            if (facturaIdNew != null) {
                facturaIdNew = em.getReference(facturaIdNew.getClass(), facturaIdNew.getId());
                venta.setFacturaId(facturaIdNew);
            }
            if (usuarioIdNew != null) {
                usuarioIdNew = em.getReference(usuarioIdNew.getClass(), usuarioIdNew.getId());
                venta.setUsuarioId(usuarioIdNew);
            }
            List<DetalleVenta> attachedDetalleVentaListNew = new ArrayList<DetalleVenta>();
            for (DetalleVenta detalleVentaListNewDetalleVentaToAttach : detalleVentaListNew) {
                detalleVentaListNewDetalleVentaToAttach = em.getReference(detalleVentaListNewDetalleVentaToAttach.getClass(), detalleVentaListNewDetalleVentaToAttach.getId());
                attachedDetalleVentaListNew.add(detalleVentaListNewDetalleVentaToAttach);
            }
            detalleVentaListNew = attachedDetalleVentaListNew;
            venta.setDetalleVentaList(detalleVentaListNew);
            venta = em.merge(venta);
            if (clienteIdOld != null && !clienteIdOld.equals(clienteIdNew)) {
                clienteIdOld.getVentaList().remove(venta);
                clienteIdOld = em.merge(clienteIdOld);
            }
            if (clienteIdNew != null && !clienteIdNew.equals(clienteIdOld)) {
                clienteIdNew.getVentaList().add(venta);
                clienteIdNew = em.merge(clienteIdNew);
            }
            if (facturaIdOld != null && !facturaIdOld.equals(facturaIdNew)) {
                facturaIdOld.setVenta(null);
                facturaIdOld = em.merge(facturaIdOld);
            }
            if (facturaIdNew != null && !facturaIdNew.equals(facturaIdOld)) {
                Venta oldVentaOfFacturaId = facturaIdNew.getVenta();
                if (oldVentaOfFacturaId != null) {
                    oldVentaOfFacturaId.setFacturaId(null);
                    oldVentaOfFacturaId = em.merge(oldVentaOfFacturaId);
                }
                facturaIdNew.setVenta(venta);
                facturaIdNew = em.merge(facturaIdNew);
            }
            if (usuarioIdOld != null && !usuarioIdOld.equals(usuarioIdNew)) {
                usuarioIdOld.getVentaList().remove(venta);
                usuarioIdOld = em.merge(usuarioIdOld);
            }
            if (usuarioIdNew != null && !usuarioIdNew.equals(usuarioIdOld)) {
                usuarioIdNew.getVentaList().add(venta);
                usuarioIdNew = em.merge(usuarioIdNew);
            }
            for (DetalleVenta detalleVentaListNewDetalleVenta : detalleVentaListNew) {
                if (!detalleVentaListOld.contains(detalleVentaListNewDetalleVenta)) {
                    Venta oldVentaIdOfDetalleVentaListNewDetalleVenta = detalleVentaListNewDetalleVenta.getVentaId();
                    detalleVentaListNewDetalleVenta.setVentaId(venta);
                    detalleVentaListNewDetalleVenta = em.merge(detalleVentaListNewDetalleVenta);
                    if (oldVentaIdOfDetalleVentaListNewDetalleVenta != null && !oldVentaIdOfDetalleVentaListNewDetalleVenta.equals(venta)) {
                        oldVentaIdOfDetalleVentaListNewDetalleVenta.getDetalleVentaList().remove(detalleVentaListNewDetalleVenta);
                        oldVentaIdOfDetalleVentaListNewDetalleVenta = em.merge(oldVentaIdOfDetalleVentaListNewDetalleVenta);
                    }
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Integer id = venta.getId();
                if (findVenta(id) == null) {
                    throw new NonexistentEntityException("The venta with id " + id + " no longer exists.");
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
            Venta venta;
            try {
                venta = em.getReference(Venta.class, id);
                venta.getId();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The venta with id " + id + " no longer exists.", enfe);
            }
            List<String> illegalOrphanMessages = null;
            List<DetalleVenta> detalleVentaListOrphanCheck = venta.getDetalleVentaList();
            for (DetalleVenta detalleVentaListOrphanCheckDetalleVenta : detalleVentaListOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This Venta (" + venta + ") cannot be destroyed since the DetalleVenta " + detalleVentaListOrphanCheckDetalleVenta + " in its detalleVentaList field has a non-nullable ventaId field.");
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            Cliente clienteId = venta.getClienteId();
            if (clienteId != null) {
                clienteId.getVentaList().remove(venta);
                clienteId = em.merge(clienteId);
            }
            Factura facturaId = venta.getFacturaId();
            if (facturaId != null) {
                facturaId.setVenta(null);
                facturaId = em.merge(facturaId);
            }
            Usuario usuarioId = venta.getUsuarioId();
            if (usuarioId != null) {
                usuarioId.getVentaList().remove(venta);
                usuarioId = em.merge(usuarioId);
            }
            em.remove(venta);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<Venta> findVentaEntities() {
        return findVentaEntities(true, -1, -1);
    }

    public List<Venta> findVentaEntities(int maxResults, int firstResult) {
        return findVentaEntities(false, maxResults, firstResult);
    }

    private List<Venta> findVentaEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Venta.class));
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

    public Venta findVenta(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Venta.class, id);
        } finally {
            em.close();
        }
    }

    public int getVentaCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Venta> rt = cq.from(Venta.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
