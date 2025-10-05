/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package gt.edu.umg.crud;

import gt.edu.umg.crud.exceptions.IllegalOrphanException;
import gt.edu.umg.crud.exceptions.NonexistentEntityException;
import java.io.Serializable;
import javax.persistence.Query;
import javax.persistence.EntityNotFoundException;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import gt.edu.umg.crud.test.EventoAnimal;
import java.util.ArrayList;
import java.util.List;
import gt.edu.umg.crud.test.Venta;
import gt.edu.umg.crud.test.Compra;
import gt.edu.umg.crud.test.BitacoraAcceso;
import gt.edu.umg.crud.test.Usuario;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

/**
 *
 * @author caste
 */
public class UsuarioJpaController implements Serializable {

    public UsuarioJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Usuario usuario) {
        if (usuario.getEventoAnimalList() == null) {
            usuario.setEventoAnimalList(new ArrayList<EventoAnimal>());
        }
        if (usuario.getVentaList() == null) {
            usuario.setVentaList(new ArrayList<Venta>());
        }
        if (usuario.getCompraList() == null) {
            usuario.setCompraList(new ArrayList<Compra>());
        }
        if (usuario.getBitacoraAccesoList() == null) {
            usuario.setBitacoraAccesoList(new ArrayList<BitacoraAcceso>());
        }
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            List<EventoAnimal> attachedEventoAnimalList = new ArrayList<EventoAnimal>();
            for (EventoAnimal eventoAnimalListEventoAnimalToAttach : usuario.getEventoAnimalList()) {
                eventoAnimalListEventoAnimalToAttach = em.getReference(eventoAnimalListEventoAnimalToAttach.getClass(), eventoAnimalListEventoAnimalToAttach.getId());
                attachedEventoAnimalList.add(eventoAnimalListEventoAnimalToAttach);
            }
            usuario.setEventoAnimalList(attachedEventoAnimalList);
            List<Venta> attachedVentaList = new ArrayList<Venta>();
            for (Venta ventaListVentaToAttach : usuario.getVentaList()) {
                ventaListVentaToAttach = em.getReference(ventaListVentaToAttach.getClass(), ventaListVentaToAttach.getId());
                attachedVentaList.add(ventaListVentaToAttach);
            }
            usuario.setVentaList(attachedVentaList);
            List<Compra> attachedCompraList = new ArrayList<Compra>();
            for (Compra compraListCompraToAttach : usuario.getCompraList()) {
                compraListCompraToAttach = em.getReference(compraListCompraToAttach.getClass(), compraListCompraToAttach.getId());
                attachedCompraList.add(compraListCompraToAttach);
            }
            usuario.setCompraList(attachedCompraList);
            List<BitacoraAcceso> attachedBitacoraAccesoList = new ArrayList<BitacoraAcceso>();
            for (BitacoraAcceso bitacoraAccesoListBitacoraAccesoToAttach : usuario.getBitacoraAccesoList()) {
                bitacoraAccesoListBitacoraAccesoToAttach = em.getReference(bitacoraAccesoListBitacoraAccesoToAttach.getClass(), bitacoraAccesoListBitacoraAccesoToAttach.getId());
                attachedBitacoraAccesoList.add(bitacoraAccesoListBitacoraAccesoToAttach);
            }
            usuario.setBitacoraAccesoList(attachedBitacoraAccesoList);
            em.persist(usuario);
            for (EventoAnimal eventoAnimalListEventoAnimal : usuario.getEventoAnimalList()) {
                Usuario oldCreadoPorOfEventoAnimalListEventoAnimal = eventoAnimalListEventoAnimal.getCreadoPor();
                eventoAnimalListEventoAnimal.setCreadoPor(usuario);
                eventoAnimalListEventoAnimal = em.merge(eventoAnimalListEventoAnimal);
                if (oldCreadoPorOfEventoAnimalListEventoAnimal != null) {
                    oldCreadoPorOfEventoAnimalListEventoAnimal.getEventoAnimalList().remove(eventoAnimalListEventoAnimal);
                    oldCreadoPorOfEventoAnimalListEventoAnimal = em.merge(oldCreadoPorOfEventoAnimalListEventoAnimal);
                }
            }
            for (Venta ventaListVenta : usuario.getVentaList()) {
                Usuario oldUsuarioIdOfVentaListVenta = ventaListVenta.getUsuarioId();
                ventaListVenta.setUsuarioId(usuario);
                ventaListVenta = em.merge(ventaListVenta);
                if (oldUsuarioIdOfVentaListVenta != null) {
                    oldUsuarioIdOfVentaListVenta.getVentaList().remove(ventaListVenta);
                    oldUsuarioIdOfVentaListVenta = em.merge(oldUsuarioIdOfVentaListVenta);
                }
            }
            for (Compra compraListCompra : usuario.getCompraList()) {
                Usuario oldUsuarioIdOfCompraListCompra = compraListCompra.getUsuarioId();
                compraListCompra.setUsuarioId(usuario);
                compraListCompra = em.merge(compraListCompra);
                if (oldUsuarioIdOfCompraListCompra != null) {
                    oldUsuarioIdOfCompraListCompra.getCompraList().remove(compraListCompra);
                    oldUsuarioIdOfCompraListCompra = em.merge(oldUsuarioIdOfCompraListCompra);
                }
            }
            for (BitacoraAcceso bitacoraAccesoListBitacoraAcceso : usuario.getBitacoraAccesoList()) {
                Usuario oldUsuarioIdOfBitacoraAccesoListBitacoraAcceso = bitacoraAccesoListBitacoraAcceso.getUsuarioId();
                bitacoraAccesoListBitacoraAcceso.setUsuarioId(usuario);
                bitacoraAccesoListBitacoraAcceso = em.merge(bitacoraAccesoListBitacoraAcceso);
                if (oldUsuarioIdOfBitacoraAccesoListBitacoraAcceso != null) {
                    oldUsuarioIdOfBitacoraAccesoListBitacoraAcceso.getBitacoraAccesoList().remove(bitacoraAccesoListBitacoraAcceso);
                    oldUsuarioIdOfBitacoraAccesoListBitacoraAcceso = em.merge(oldUsuarioIdOfBitacoraAccesoListBitacoraAcceso);
                }
            }
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(Usuario usuario) throws IllegalOrphanException, NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Usuario persistentUsuario = em.find(Usuario.class, usuario.getId());
            List<EventoAnimal> eventoAnimalListOld = persistentUsuario.getEventoAnimalList();
            List<EventoAnimal> eventoAnimalListNew = usuario.getEventoAnimalList();
            List<Venta> ventaListOld = persistentUsuario.getVentaList();
            List<Venta> ventaListNew = usuario.getVentaList();
            List<Compra> compraListOld = persistentUsuario.getCompraList();
            List<Compra> compraListNew = usuario.getCompraList();
            List<BitacoraAcceso> bitacoraAccesoListOld = persistentUsuario.getBitacoraAccesoList();
            List<BitacoraAcceso> bitacoraAccesoListNew = usuario.getBitacoraAccesoList();
            List<String> illegalOrphanMessages = null;
            for (Venta ventaListOldVenta : ventaListOld) {
                if (!ventaListNew.contains(ventaListOldVenta)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain Venta " + ventaListOldVenta + " since its usuarioId field is not nullable.");
                }
            }
            for (Compra compraListOldCompra : compraListOld) {
                if (!compraListNew.contains(compraListOldCompra)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain Compra " + compraListOldCompra + " since its usuarioId field is not nullable.");
                }
            }
            for (BitacoraAcceso bitacoraAccesoListOldBitacoraAcceso : bitacoraAccesoListOld) {
                if (!bitacoraAccesoListNew.contains(bitacoraAccesoListOldBitacoraAcceso)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain BitacoraAcceso " + bitacoraAccesoListOldBitacoraAcceso + " since its usuarioId field is not nullable.");
                }
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            List<EventoAnimal> attachedEventoAnimalListNew = new ArrayList<EventoAnimal>();
            for (EventoAnimal eventoAnimalListNewEventoAnimalToAttach : eventoAnimalListNew) {
                eventoAnimalListNewEventoAnimalToAttach = em.getReference(eventoAnimalListNewEventoAnimalToAttach.getClass(), eventoAnimalListNewEventoAnimalToAttach.getId());
                attachedEventoAnimalListNew.add(eventoAnimalListNewEventoAnimalToAttach);
            }
            eventoAnimalListNew = attachedEventoAnimalListNew;
            usuario.setEventoAnimalList(eventoAnimalListNew);
            List<Venta> attachedVentaListNew = new ArrayList<Venta>();
            for (Venta ventaListNewVentaToAttach : ventaListNew) {
                ventaListNewVentaToAttach = em.getReference(ventaListNewVentaToAttach.getClass(), ventaListNewVentaToAttach.getId());
                attachedVentaListNew.add(ventaListNewVentaToAttach);
            }
            ventaListNew = attachedVentaListNew;
            usuario.setVentaList(ventaListNew);
            List<Compra> attachedCompraListNew = new ArrayList<Compra>();
            for (Compra compraListNewCompraToAttach : compraListNew) {
                compraListNewCompraToAttach = em.getReference(compraListNewCompraToAttach.getClass(), compraListNewCompraToAttach.getId());
                attachedCompraListNew.add(compraListNewCompraToAttach);
            }
            compraListNew = attachedCompraListNew;
            usuario.setCompraList(compraListNew);
            List<BitacoraAcceso> attachedBitacoraAccesoListNew = new ArrayList<BitacoraAcceso>();
            for (BitacoraAcceso bitacoraAccesoListNewBitacoraAccesoToAttach : bitacoraAccesoListNew) {
                bitacoraAccesoListNewBitacoraAccesoToAttach = em.getReference(bitacoraAccesoListNewBitacoraAccesoToAttach.getClass(), bitacoraAccesoListNewBitacoraAccesoToAttach.getId());
                attachedBitacoraAccesoListNew.add(bitacoraAccesoListNewBitacoraAccesoToAttach);
            }
            bitacoraAccesoListNew = attachedBitacoraAccesoListNew;
            usuario.setBitacoraAccesoList(bitacoraAccesoListNew);
            usuario = em.merge(usuario);
            for (EventoAnimal eventoAnimalListOldEventoAnimal : eventoAnimalListOld) {
                if (!eventoAnimalListNew.contains(eventoAnimalListOldEventoAnimal)) {
                    eventoAnimalListOldEventoAnimal.setCreadoPor(null);
                    eventoAnimalListOldEventoAnimal = em.merge(eventoAnimalListOldEventoAnimal);
                }
            }
            for (EventoAnimal eventoAnimalListNewEventoAnimal : eventoAnimalListNew) {
                if (!eventoAnimalListOld.contains(eventoAnimalListNewEventoAnimal)) {
                    Usuario oldCreadoPorOfEventoAnimalListNewEventoAnimal = eventoAnimalListNewEventoAnimal.getCreadoPor();
                    eventoAnimalListNewEventoAnimal.setCreadoPor(usuario);
                    eventoAnimalListNewEventoAnimal = em.merge(eventoAnimalListNewEventoAnimal);
                    if (oldCreadoPorOfEventoAnimalListNewEventoAnimal != null && !oldCreadoPorOfEventoAnimalListNewEventoAnimal.equals(usuario)) {
                        oldCreadoPorOfEventoAnimalListNewEventoAnimal.getEventoAnimalList().remove(eventoAnimalListNewEventoAnimal);
                        oldCreadoPorOfEventoAnimalListNewEventoAnimal = em.merge(oldCreadoPorOfEventoAnimalListNewEventoAnimal);
                    }
                }
            }
            for (Venta ventaListNewVenta : ventaListNew) {
                if (!ventaListOld.contains(ventaListNewVenta)) {
                    Usuario oldUsuarioIdOfVentaListNewVenta = ventaListNewVenta.getUsuarioId();
                    ventaListNewVenta.setUsuarioId(usuario);
                    ventaListNewVenta = em.merge(ventaListNewVenta);
                    if (oldUsuarioIdOfVentaListNewVenta != null && !oldUsuarioIdOfVentaListNewVenta.equals(usuario)) {
                        oldUsuarioIdOfVentaListNewVenta.getVentaList().remove(ventaListNewVenta);
                        oldUsuarioIdOfVentaListNewVenta = em.merge(oldUsuarioIdOfVentaListNewVenta);
                    }
                }
            }
            for (Compra compraListNewCompra : compraListNew) {
                if (!compraListOld.contains(compraListNewCompra)) {
                    Usuario oldUsuarioIdOfCompraListNewCompra = compraListNewCompra.getUsuarioId();
                    compraListNewCompra.setUsuarioId(usuario);
                    compraListNewCompra = em.merge(compraListNewCompra);
                    if (oldUsuarioIdOfCompraListNewCompra != null && !oldUsuarioIdOfCompraListNewCompra.equals(usuario)) {
                        oldUsuarioIdOfCompraListNewCompra.getCompraList().remove(compraListNewCompra);
                        oldUsuarioIdOfCompraListNewCompra = em.merge(oldUsuarioIdOfCompraListNewCompra);
                    }
                }
            }
            for (BitacoraAcceso bitacoraAccesoListNewBitacoraAcceso : bitacoraAccesoListNew) {
                if (!bitacoraAccesoListOld.contains(bitacoraAccesoListNewBitacoraAcceso)) {
                    Usuario oldUsuarioIdOfBitacoraAccesoListNewBitacoraAcceso = bitacoraAccesoListNewBitacoraAcceso.getUsuarioId();
                    bitacoraAccesoListNewBitacoraAcceso.setUsuarioId(usuario);
                    bitacoraAccesoListNewBitacoraAcceso = em.merge(bitacoraAccesoListNewBitacoraAcceso);
                    if (oldUsuarioIdOfBitacoraAccesoListNewBitacoraAcceso != null && !oldUsuarioIdOfBitacoraAccesoListNewBitacoraAcceso.equals(usuario)) {
                        oldUsuarioIdOfBitacoraAccesoListNewBitacoraAcceso.getBitacoraAccesoList().remove(bitacoraAccesoListNewBitacoraAcceso);
                        oldUsuarioIdOfBitacoraAccesoListNewBitacoraAcceso = em.merge(oldUsuarioIdOfBitacoraAccesoListNewBitacoraAcceso);
                    }
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Integer id = usuario.getId();
                if (findUsuario(id) == null) {
                    throw new NonexistentEntityException("The usuario with id " + id + " no longer exists.");
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
            Usuario usuario;
            try {
                usuario = em.getReference(Usuario.class, id);
                usuario.getId();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The usuario with id " + id + " no longer exists.", enfe);
            }
            List<String> illegalOrphanMessages = null;
            List<Venta> ventaListOrphanCheck = usuario.getVentaList();
            for (Venta ventaListOrphanCheckVenta : ventaListOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This Usuario (" + usuario + ") cannot be destroyed since the Venta " + ventaListOrphanCheckVenta + " in its ventaList field has a non-nullable usuarioId field.");
            }
            List<Compra> compraListOrphanCheck = usuario.getCompraList();
            for (Compra compraListOrphanCheckCompra : compraListOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This Usuario (" + usuario + ") cannot be destroyed since the Compra " + compraListOrphanCheckCompra + " in its compraList field has a non-nullable usuarioId field.");
            }
            List<BitacoraAcceso> bitacoraAccesoListOrphanCheck = usuario.getBitacoraAccesoList();
            for (BitacoraAcceso bitacoraAccesoListOrphanCheckBitacoraAcceso : bitacoraAccesoListOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This Usuario (" + usuario + ") cannot be destroyed since the BitacoraAcceso " + bitacoraAccesoListOrphanCheckBitacoraAcceso + " in its bitacoraAccesoList field has a non-nullable usuarioId field.");
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            List<EventoAnimal> eventoAnimalList = usuario.getEventoAnimalList();
            for (EventoAnimal eventoAnimalListEventoAnimal : eventoAnimalList) {
                eventoAnimalListEventoAnimal.setCreadoPor(null);
                eventoAnimalListEventoAnimal = em.merge(eventoAnimalListEventoAnimal);
            }
            em.remove(usuario);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<Usuario> findUsuarioEntities() {
        return findUsuarioEntities(true, -1, -1);
    }

    public List<Usuario> findUsuarioEntities(int maxResults, int firstResult) {
        return findUsuarioEntities(false, maxResults, firstResult);
    }

    private List<Usuario> findUsuarioEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Usuario.class));
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

    public Usuario findUsuario(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Usuario.class, id);
        } finally {
            em.close();
        }
    }

    public int getUsuarioCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Usuario> rt = cq.from(Usuario.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
