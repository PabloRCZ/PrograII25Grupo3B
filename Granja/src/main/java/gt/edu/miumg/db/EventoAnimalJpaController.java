/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package gt.edu.miumg.db;

import gt.edu.miumg.db.exceptions.NonexistentEntityException;
import java.io.Serializable;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Query;
import javax.persistence.EntityNotFoundException;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;

/**
 *
 * @author pabli
 */
public class EventoAnimalJpaController implements Serializable {

    public EventoAnimalJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(EventoAnimal eventoAnimal) {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            InventarioAnimales animalId = eventoAnimal.getAnimalId();
            if (animalId != null) {
                animalId = em.getReference(animalId.getClass(), animalId.getId());
                eventoAnimal.setAnimalId(animalId);
            }
            Usuario creadoPor = eventoAnimal.getCreadoPor();
            if (creadoPor != null) {
                creadoPor = em.getReference(creadoPor.getClass(), creadoPor.getId());
                eventoAnimal.setCreadoPor(creadoPor);
            }
            em.persist(eventoAnimal);
            if (animalId != null) {
                animalId.getEventoAnimalList().add(eventoAnimal);
                animalId = em.merge(animalId);
            }
            if (creadoPor != null) {
                creadoPor.getEventoAnimalList().add(eventoAnimal);
                creadoPor = em.merge(creadoPor);
            }
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(EventoAnimal eventoAnimal) throws NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            EventoAnimal persistentEventoAnimal = em.find(EventoAnimal.class, eventoAnimal.getId());
            InventarioAnimales animalIdOld = persistentEventoAnimal.getAnimalId();
            InventarioAnimales animalIdNew = eventoAnimal.getAnimalId();
            Usuario creadoPorOld = persistentEventoAnimal.getCreadoPor();
            Usuario creadoPorNew = eventoAnimal.getCreadoPor();
            if (animalIdNew != null) {
                animalIdNew = em.getReference(animalIdNew.getClass(), animalIdNew.getId());
                eventoAnimal.setAnimalId(animalIdNew);
            }
            if (creadoPorNew != null) {
                creadoPorNew = em.getReference(creadoPorNew.getClass(), creadoPorNew.getId());
                eventoAnimal.setCreadoPor(creadoPorNew);
            }
            eventoAnimal = em.merge(eventoAnimal);
            if (animalIdOld != null && !animalIdOld.equals(animalIdNew)) {
                animalIdOld.getEventoAnimalList().remove(eventoAnimal);
                animalIdOld = em.merge(animalIdOld);
            }
            if (animalIdNew != null && !animalIdNew.equals(animalIdOld)) {
                animalIdNew.getEventoAnimalList().add(eventoAnimal);
                animalIdNew = em.merge(animalIdNew);
            }
            if (creadoPorOld != null && !creadoPorOld.equals(creadoPorNew)) {
                creadoPorOld.getEventoAnimalList().remove(eventoAnimal);
                creadoPorOld = em.merge(creadoPorOld);
            }
            if (creadoPorNew != null && !creadoPorNew.equals(creadoPorOld)) {
                creadoPorNew.getEventoAnimalList().add(eventoAnimal);
                creadoPorNew = em.merge(creadoPorNew);
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Integer id = eventoAnimal.getId();
                if (findEventoAnimal(id) == null) {
                    throw new NonexistentEntityException("The eventoAnimal with id " + id + " no longer exists.");
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
            EventoAnimal eventoAnimal;
            try {
                eventoAnimal = em.getReference(EventoAnimal.class, id);
                eventoAnimal.getId();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The eventoAnimal with id " + id + " no longer exists.", enfe);
            }
            InventarioAnimales animalId = eventoAnimal.getAnimalId();
            if (animalId != null) {
                animalId.getEventoAnimalList().remove(eventoAnimal);
                animalId = em.merge(animalId);
            }
            Usuario creadoPor = eventoAnimal.getCreadoPor();
            if (creadoPor != null) {
                creadoPor.getEventoAnimalList().remove(eventoAnimal);
                creadoPor = em.merge(creadoPor);
            }
            em.remove(eventoAnimal);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<EventoAnimal> findEventoAnimalEntities() {
        return findEventoAnimalEntities(true, -1, -1);
    }

    public List<EventoAnimal> findEventoAnimalEntities(int maxResults, int firstResult) {
        return findEventoAnimalEntities(false, maxResults, firstResult);
    }

    private List<EventoAnimal> findEventoAnimalEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(EventoAnimal.class));
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

    public EventoAnimal findEventoAnimal(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(EventoAnimal.class, id);
        } finally {
            em.close();
        }
    }

    public int getEventoAnimalCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<EventoAnimal> rt = cq.from(EventoAnimal.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
