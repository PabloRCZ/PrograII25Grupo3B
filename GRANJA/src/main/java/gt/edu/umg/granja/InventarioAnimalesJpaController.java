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
import gt.edu.umg.granja.ENTITY.DetalleVenta;
import java.util.ArrayList;
import java.util.List;
import gt.edu.umg.granja.ENTITY.EventoAnimal;
import gt.edu.umg.granja.ENTITY.DetalleCompra;
import gt.edu.umg.granja.ENTITY.InventarioAnimales;
import gt.edu.umg.granja.exceptions.IllegalOrphanException;
import gt.edu.umg.granja.exceptions.NonexistentEntityException;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

/**
 *
 * @author caste
 */
public class InventarioAnimalesJpaController implements Serializable {

    public InventarioAnimalesJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(InventarioAnimales inventarioAnimales) {
        if (inventarioAnimales.getDetalleVentaList() == null) {
            inventarioAnimales.setDetalleVentaList(new ArrayList<DetalleVenta>());
        }
        if (inventarioAnimales.getEventoAnimalList() == null) {
            inventarioAnimales.setEventoAnimalList(new ArrayList<EventoAnimal>());
        }
        if (inventarioAnimales.getDetalleCompraList() == null) {
            inventarioAnimales.setDetalleCompraList(new ArrayList<DetalleCompra>());
        }
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            List<DetalleVenta> attachedDetalleVentaList = new ArrayList<DetalleVenta>();
            for (DetalleVenta detalleVentaListDetalleVentaToAttach : inventarioAnimales.getDetalleVentaList()) {
                detalleVentaListDetalleVentaToAttach = em.getReference(detalleVentaListDetalleVentaToAttach.getClass(), detalleVentaListDetalleVentaToAttach.getId());
                attachedDetalleVentaList.add(detalleVentaListDetalleVentaToAttach);
            }
            inventarioAnimales.setDetalleVentaList(attachedDetalleVentaList);
            List<EventoAnimal> attachedEventoAnimalList = new ArrayList<EventoAnimal>();
            for (EventoAnimal eventoAnimalListEventoAnimalToAttach : inventarioAnimales.getEventoAnimalList()) {
                eventoAnimalListEventoAnimalToAttach = em.getReference(eventoAnimalListEventoAnimalToAttach.getClass(), eventoAnimalListEventoAnimalToAttach.getId());
                attachedEventoAnimalList.add(eventoAnimalListEventoAnimalToAttach);
            }
            inventarioAnimales.setEventoAnimalList(attachedEventoAnimalList);
            List<DetalleCompra> attachedDetalleCompraList = new ArrayList<DetalleCompra>();
            for (DetalleCompra detalleCompraListDetalleCompraToAttach : inventarioAnimales.getDetalleCompraList()) {
                detalleCompraListDetalleCompraToAttach = em.getReference(detalleCompraListDetalleCompraToAttach.getClass(), detalleCompraListDetalleCompraToAttach.getId());
                attachedDetalleCompraList.add(detalleCompraListDetalleCompraToAttach);
            }
            inventarioAnimales.setDetalleCompraList(attachedDetalleCompraList);
            em.persist(inventarioAnimales);
            for (DetalleVenta detalleVentaListDetalleVenta : inventarioAnimales.getDetalleVentaList()) {
                InventarioAnimales oldAnimalIdOfDetalleVentaListDetalleVenta = detalleVentaListDetalleVenta.getAnimalId();
                detalleVentaListDetalleVenta.setAnimalId(inventarioAnimales);
                detalleVentaListDetalleVenta = em.merge(detalleVentaListDetalleVenta);
                if (oldAnimalIdOfDetalleVentaListDetalleVenta != null) {
                    oldAnimalIdOfDetalleVentaListDetalleVenta.getDetalleVentaList().remove(detalleVentaListDetalleVenta);
                    oldAnimalIdOfDetalleVentaListDetalleVenta = em.merge(oldAnimalIdOfDetalleVentaListDetalleVenta);
                }
            }
            for (EventoAnimal eventoAnimalListEventoAnimal : inventarioAnimales.getEventoAnimalList()) {
                InventarioAnimales oldAnimalIdOfEventoAnimalListEventoAnimal = eventoAnimalListEventoAnimal.getAnimalId();
                eventoAnimalListEventoAnimal.setAnimalId(inventarioAnimales);
                eventoAnimalListEventoAnimal = em.merge(eventoAnimalListEventoAnimal);
                if (oldAnimalIdOfEventoAnimalListEventoAnimal != null) {
                    oldAnimalIdOfEventoAnimalListEventoAnimal.getEventoAnimalList().remove(eventoAnimalListEventoAnimal);
                    oldAnimalIdOfEventoAnimalListEventoAnimal = em.merge(oldAnimalIdOfEventoAnimalListEventoAnimal);
                }
            }
            for (DetalleCompra detalleCompraListDetalleCompra : inventarioAnimales.getDetalleCompraList()) {
                InventarioAnimales oldAnimalIdOfDetalleCompraListDetalleCompra = detalleCompraListDetalleCompra.getAnimalId();
                detalleCompraListDetalleCompra.setAnimalId(inventarioAnimales);
                detalleCompraListDetalleCompra = em.merge(detalleCompraListDetalleCompra);
                if (oldAnimalIdOfDetalleCompraListDetalleCompra != null) {
                    oldAnimalIdOfDetalleCompraListDetalleCompra.getDetalleCompraList().remove(detalleCompraListDetalleCompra);
                    oldAnimalIdOfDetalleCompraListDetalleCompra = em.merge(oldAnimalIdOfDetalleCompraListDetalleCompra);
                }
            }
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(InventarioAnimales inventarioAnimales) throws IllegalOrphanException, NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            InventarioAnimales persistentInventarioAnimales = em.find(InventarioAnimales.class, inventarioAnimales.getId());
            List<DetalleVenta> detalleVentaListOld = persistentInventarioAnimales.getDetalleVentaList();
            List<DetalleVenta> detalleVentaListNew = inventarioAnimales.getDetalleVentaList();
            List<EventoAnimal> eventoAnimalListOld = persistentInventarioAnimales.getEventoAnimalList();
            List<EventoAnimal> eventoAnimalListNew = inventarioAnimales.getEventoAnimalList();
            List<DetalleCompra> detalleCompraListOld = persistentInventarioAnimales.getDetalleCompraList();
            List<DetalleCompra> detalleCompraListNew = inventarioAnimales.getDetalleCompraList();
            List<String> illegalOrphanMessages = null;
            for (EventoAnimal eventoAnimalListOldEventoAnimal : eventoAnimalListOld) {
                if (!eventoAnimalListNew.contains(eventoAnimalListOldEventoAnimal)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain EventoAnimal " + eventoAnimalListOldEventoAnimal + " since its animalId field is not nullable.");
                }
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            List<DetalleVenta> attachedDetalleVentaListNew = new ArrayList<DetalleVenta>();
            for (DetalleVenta detalleVentaListNewDetalleVentaToAttach : detalleVentaListNew) {
                detalleVentaListNewDetalleVentaToAttach = em.getReference(detalleVentaListNewDetalleVentaToAttach.getClass(), detalleVentaListNewDetalleVentaToAttach.getId());
                attachedDetalleVentaListNew.add(detalleVentaListNewDetalleVentaToAttach);
            }
            detalleVentaListNew = attachedDetalleVentaListNew;
            inventarioAnimales.setDetalleVentaList(detalleVentaListNew);
            List<EventoAnimal> attachedEventoAnimalListNew = new ArrayList<EventoAnimal>();
            for (EventoAnimal eventoAnimalListNewEventoAnimalToAttach : eventoAnimalListNew) {
                eventoAnimalListNewEventoAnimalToAttach = em.getReference(eventoAnimalListNewEventoAnimalToAttach.getClass(), eventoAnimalListNewEventoAnimalToAttach.getId());
                attachedEventoAnimalListNew.add(eventoAnimalListNewEventoAnimalToAttach);
            }
            eventoAnimalListNew = attachedEventoAnimalListNew;
            inventarioAnimales.setEventoAnimalList(eventoAnimalListNew);
            List<DetalleCompra> attachedDetalleCompraListNew = new ArrayList<DetalleCompra>();
            for (DetalleCompra detalleCompraListNewDetalleCompraToAttach : detalleCompraListNew) {
                detalleCompraListNewDetalleCompraToAttach = em.getReference(detalleCompraListNewDetalleCompraToAttach.getClass(), detalleCompraListNewDetalleCompraToAttach.getId());
                attachedDetalleCompraListNew.add(detalleCompraListNewDetalleCompraToAttach);
            }
            detalleCompraListNew = attachedDetalleCompraListNew;
            inventarioAnimales.setDetalleCompraList(detalleCompraListNew);
            inventarioAnimales = em.merge(inventarioAnimales);
            for (DetalleVenta detalleVentaListOldDetalleVenta : detalleVentaListOld) {
                if (!detalleVentaListNew.contains(detalleVentaListOldDetalleVenta)) {
                    detalleVentaListOldDetalleVenta.setAnimalId(null);
                    detalleVentaListOldDetalleVenta = em.merge(detalleVentaListOldDetalleVenta);
                }
            }
            for (DetalleVenta detalleVentaListNewDetalleVenta : detalleVentaListNew) {
                if (!detalleVentaListOld.contains(detalleVentaListNewDetalleVenta)) {
                    InventarioAnimales oldAnimalIdOfDetalleVentaListNewDetalleVenta = detalleVentaListNewDetalleVenta.getAnimalId();
                    detalleVentaListNewDetalleVenta.setAnimalId(inventarioAnimales);
                    detalleVentaListNewDetalleVenta = em.merge(detalleVentaListNewDetalleVenta);
                    if (oldAnimalIdOfDetalleVentaListNewDetalleVenta != null && !oldAnimalIdOfDetalleVentaListNewDetalleVenta.equals(inventarioAnimales)) {
                        oldAnimalIdOfDetalleVentaListNewDetalleVenta.getDetalleVentaList().remove(detalleVentaListNewDetalleVenta);
                        oldAnimalIdOfDetalleVentaListNewDetalleVenta = em.merge(oldAnimalIdOfDetalleVentaListNewDetalleVenta);
                    }
                }
            }
            for (EventoAnimal eventoAnimalListNewEventoAnimal : eventoAnimalListNew) {
                if (!eventoAnimalListOld.contains(eventoAnimalListNewEventoAnimal)) {
                    InventarioAnimales oldAnimalIdOfEventoAnimalListNewEventoAnimal = eventoAnimalListNewEventoAnimal.getAnimalId();
                    eventoAnimalListNewEventoAnimal.setAnimalId(inventarioAnimales);
                    eventoAnimalListNewEventoAnimal = em.merge(eventoAnimalListNewEventoAnimal);
                    if (oldAnimalIdOfEventoAnimalListNewEventoAnimal != null && !oldAnimalIdOfEventoAnimalListNewEventoAnimal.equals(inventarioAnimales)) {
                        oldAnimalIdOfEventoAnimalListNewEventoAnimal.getEventoAnimalList().remove(eventoAnimalListNewEventoAnimal);
                        oldAnimalIdOfEventoAnimalListNewEventoAnimal = em.merge(oldAnimalIdOfEventoAnimalListNewEventoAnimal);
                    }
                }
            }
            for (DetalleCompra detalleCompraListOldDetalleCompra : detalleCompraListOld) {
                if (!detalleCompraListNew.contains(detalleCompraListOldDetalleCompra)) {
                    detalleCompraListOldDetalleCompra.setAnimalId(null);
                    detalleCompraListOldDetalleCompra = em.merge(detalleCompraListOldDetalleCompra);
                }
            }
            for (DetalleCompra detalleCompraListNewDetalleCompra : detalleCompraListNew) {
                if (!detalleCompraListOld.contains(detalleCompraListNewDetalleCompra)) {
                    InventarioAnimales oldAnimalIdOfDetalleCompraListNewDetalleCompra = detalleCompraListNewDetalleCompra.getAnimalId();
                    detalleCompraListNewDetalleCompra.setAnimalId(inventarioAnimales);
                    detalleCompraListNewDetalleCompra = em.merge(detalleCompraListNewDetalleCompra);
                    if (oldAnimalIdOfDetalleCompraListNewDetalleCompra != null && !oldAnimalIdOfDetalleCompraListNewDetalleCompra.equals(inventarioAnimales)) {
                        oldAnimalIdOfDetalleCompraListNewDetalleCompra.getDetalleCompraList().remove(detalleCompraListNewDetalleCompra);
                        oldAnimalIdOfDetalleCompraListNewDetalleCompra = em.merge(oldAnimalIdOfDetalleCompraListNewDetalleCompra);
                    }
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Integer id = inventarioAnimales.getId();
                if (findInventarioAnimales(id) == null) {
                    throw new NonexistentEntityException("The inventarioAnimales with id " + id + " no longer exists.");
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
            InventarioAnimales inventarioAnimales;
            try {
                inventarioAnimales = em.getReference(InventarioAnimales.class, id);
                inventarioAnimales.getId();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The inventarioAnimales with id " + id + " no longer exists.", enfe);
            }
            List<String> illegalOrphanMessages = null;
            List<EventoAnimal> eventoAnimalListOrphanCheck = inventarioAnimales.getEventoAnimalList();
            for (EventoAnimal eventoAnimalListOrphanCheckEventoAnimal : eventoAnimalListOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This InventarioAnimales (" + inventarioAnimales + ") cannot be destroyed since the EventoAnimal " + eventoAnimalListOrphanCheckEventoAnimal + " in its eventoAnimalList field has a non-nullable animalId field.");
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            List<DetalleVenta> detalleVentaList = inventarioAnimales.getDetalleVentaList();
            for (DetalleVenta detalleVentaListDetalleVenta : detalleVentaList) {
                detalleVentaListDetalleVenta.setAnimalId(null);
                detalleVentaListDetalleVenta = em.merge(detalleVentaListDetalleVenta);
            }
            List<DetalleCompra> detalleCompraList = inventarioAnimales.getDetalleCompraList();
            for (DetalleCompra detalleCompraListDetalleCompra : detalleCompraList) {
                detalleCompraListDetalleCompra.setAnimalId(null);
                detalleCompraListDetalleCompra = em.merge(detalleCompraListDetalleCompra);
            }
            em.remove(inventarioAnimales);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<InventarioAnimales> findInventarioAnimalesEntities() {
        return findInventarioAnimalesEntities(true, -1, -1);
    }

    public List<InventarioAnimales> findInventarioAnimalesEntities(int maxResults, int firstResult) {
        return findInventarioAnimalesEntities(false, maxResults, firstResult);
    }

    private List<InventarioAnimales> findInventarioAnimalesEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(InventarioAnimales.class));
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

    public InventarioAnimales findInventarioAnimales(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(InventarioAnimales.class, id);
        } finally {
            em.close();
        }
    }

    public int getInventarioAnimalesCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<InventarioAnimales> rt = cq.from(InventarioAnimales.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
