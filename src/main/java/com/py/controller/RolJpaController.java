/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.py.controller;

import com.py.controller.exceptions.NonexistentEntityException;
import com.py.controller.exceptions.RollbackFailureException;
import java.io.Serializable;
import javax.persistence.Query;
import javax.persistence.EntityNotFoundException;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import com.py.modelo.Usuario;
import java.util.ArrayList;
import java.util.Collection;
import com.py.modelo.Permiso;
import com.py.modelo.Rol;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.transaction.UserTransaction;

/**
 *
 * @author ACER-PC
 */
public class RolJpaController implements Serializable {

    public RolJpaController(UserTransaction utx, EntityManagerFactory emf) {
        this.utx = utx;
        this.emf = emf;
    }
    private UserTransaction utx = null;
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Rol rol) throws RollbackFailureException, Exception {
        if (rol.getUsuarioCollection() == null) {
            rol.setUsuarioCollection(new ArrayList<Usuario>());
        }
        if (rol.getPermisoCollection() == null) {
            rol.setPermisoCollection(new ArrayList<Permiso>());
        }
        EntityManager em = null;
        try {
            utx.begin();
            em = getEntityManager();
            Collection<Usuario> attachedUsuarioCollection = new ArrayList<Usuario>();
            for (Usuario usuarioCollectionUsuarioToAttach : rol.getUsuarioCollection()) {
                usuarioCollectionUsuarioToAttach = em.getReference(usuarioCollectionUsuarioToAttach.getClass(), usuarioCollectionUsuarioToAttach.getId());
                attachedUsuarioCollection.add(usuarioCollectionUsuarioToAttach);
            }
            rol.setUsuarioCollection(attachedUsuarioCollection);
            Collection<Permiso> attachedPermisoCollection = new ArrayList<Permiso>();
            for (Permiso permisoCollectionPermisoToAttach : rol.getPermisoCollection()) {
                permisoCollectionPermisoToAttach = em.getReference(permisoCollectionPermisoToAttach.getClass(), permisoCollectionPermisoToAttach.getId());
                attachedPermisoCollection.add(permisoCollectionPermisoToAttach);
            }
            rol.setPermisoCollection(attachedPermisoCollection);
            em.persist(rol);
            for (Usuario usuarioCollectionUsuario : rol.getUsuarioCollection()) {
                usuarioCollectionUsuario.getRolCollection().add(rol);
                usuarioCollectionUsuario = em.merge(usuarioCollectionUsuario);
            }
            for (Permiso permisoCollectionPermiso : rol.getPermisoCollection()) {
                Rol oldRolIdOfPermisoCollectionPermiso = permisoCollectionPermiso.getRolId();
                permisoCollectionPermiso.setRolId(rol);
                permisoCollectionPermiso = em.merge(permisoCollectionPermiso);
                if (oldRolIdOfPermisoCollectionPermiso != null) {
                    oldRolIdOfPermisoCollectionPermiso.getPermisoCollection().remove(permisoCollectionPermiso);
                    oldRolIdOfPermisoCollectionPermiso = em.merge(oldRolIdOfPermisoCollectionPermiso);
                }
            }
            utx.commit();
        } catch (Exception ex) {
            try {
                utx.rollback();
            } catch (Exception re) {
                throw new RollbackFailureException("An error occurred attempting to roll back the transaction.", re);
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(Rol rol) throws NonexistentEntityException, RollbackFailureException, Exception {
        EntityManager em = null;
        try {
            utx.begin();
            em = getEntityManager();
            Rol persistentRol = em.find(Rol.class, rol.getId());
            Collection<Usuario> usuarioCollectionOld = persistentRol.getUsuarioCollection();
            Collection<Usuario> usuarioCollectionNew = rol.getUsuarioCollection();
            Collection<Permiso> permisoCollectionOld = persistentRol.getPermisoCollection();
            Collection<Permiso> permisoCollectionNew = rol.getPermisoCollection();
            Collection<Usuario> attachedUsuarioCollectionNew = new ArrayList<Usuario>();
            for (Usuario usuarioCollectionNewUsuarioToAttach : usuarioCollectionNew) {
                usuarioCollectionNewUsuarioToAttach = em.getReference(usuarioCollectionNewUsuarioToAttach.getClass(), usuarioCollectionNewUsuarioToAttach.getId());
                attachedUsuarioCollectionNew.add(usuarioCollectionNewUsuarioToAttach);
            }
            usuarioCollectionNew = attachedUsuarioCollectionNew;
            rol.setUsuarioCollection(usuarioCollectionNew);
            Collection<Permiso> attachedPermisoCollectionNew = new ArrayList<Permiso>();
            for (Permiso permisoCollectionNewPermisoToAttach : permisoCollectionNew) {
                permisoCollectionNewPermisoToAttach = em.getReference(permisoCollectionNewPermisoToAttach.getClass(), permisoCollectionNewPermisoToAttach.getId());
                attachedPermisoCollectionNew.add(permisoCollectionNewPermisoToAttach);
            }
            permisoCollectionNew = attachedPermisoCollectionNew;
            rol.setPermisoCollection(permisoCollectionNew);
            rol = em.merge(rol);
            for (Usuario usuarioCollectionOldUsuario : usuarioCollectionOld) {
                if (!usuarioCollectionNew.contains(usuarioCollectionOldUsuario)) {
                    usuarioCollectionOldUsuario.getRolCollection().remove(rol);
                    usuarioCollectionOldUsuario = em.merge(usuarioCollectionOldUsuario);
                }
            }
            for (Usuario usuarioCollectionNewUsuario : usuarioCollectionNew) {
                if (!usuarioCollectionOld.contains(usuarioCollectionNewUsuario)) {
                    usuarioCollectionNewUsuario.getRolCollection().add(rol);
                    usuarioCollectionNewUsuario = em.merge(usuarioCollectionNewUsuario);
                }
            }
            for (Permiso permisoCollectionOldPermiso : permisoCollectionOld) {
                if (!permisoCollectionNew.contains(permisoCollectionOldPermiso)) {
                    permisoCollectionOldPermiso.setRolId(null);
                    permisoCollectionOldPermiso = em.merge(permisoCollectionOldPermiso);
                }
            }
            for (Permiso permisoCollectionNewPermiso : permisoCollectionNew) {
                if (!permisoCollectionOld.contains(permisoCollectionNewPermiso)) {
                    Rol oldRolIdOfPermisoCollectionNewPermiso = permisoCollectionNewPermiso.getRolId();
                    permisoCollectionNewPermiso.setRolId(rol);
                    permisoCollectionNewPermiso = em.merge(permisoCollectionNewPermiso);
                    if (oldRolIdOfPermisoCollectionNewPermiso != null && !oldRolIdOfPermisoCollectionNewPermiso.equals(rol)) {
                        oldRolIdOfPermisoCollectionNewPermiso.getPermisoCollection().remove(permisoCollectionNewPermiso);
                        oldRolIdOfPermisoCollectionNewPermiso = em.merge(oldRolIdOfPermisoCollectionNewPermiso);
                    }
                }
            }
            utx.commit();
        } catch (Exception ex) {
            try {
                utx.rollback();
            } catch (Exception re) {
                throw new RollbackFailureException("An error occurred attempting to roll back the transaction.", re);
            }
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Integer id = rol.getId();
                if (findRol(id) == null) {
                    throw new NonexistentEntityException("The rol with id " + id + " no longer exists.");
                }
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void destroy(Integer id) throws NonexistentEntityException, RollbackFailureException, Exception {
        EntityManager em = null;
        try {
            utx.begin();
            em = getEntityManager();
            Rol rol;
            try {
                rol = em.getReference(Rol.class, id);
                rol.getId();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The rol with id " + id + " no longer exists.", enfe);
            }
            Collection<Usuario> usuarioCollection = rol.getUsuarioCollection();
            for (Usuario usuarioCollectionUsuario : usuarioCollection) {
                usuarioCollectionUsuario.getRolCollection().remove(rol);
                usuarioCollectionUsuario = em.merge(usuarioCollectionUsuario);
            }
            Collection<Permiso> permisoCollection = rol.getPermisoCollection();
            for (Permiso permisoCollectionPermiso : permisoCollection) {
                permisoCollectionPermiso.setRolId(null);
                permisoCollectionPermiso = em.merge(permisoCollectionPermiso);
            }
            em.remove(rol);
            utx.commit();
        } catch (Exception ex) {
            try {
                utx.rollback();
            } catch (Exception re) {
                throw new RollbackFailureException("An error occurred attempting to roll back the transaction.", re);
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<Rol> findRolEntities() {
        return findRolEntities(true, -1, -1);
    }

    public List<Rol> findRolEntities(int maxResults, int firstResult) {
        return findRolEntities(false, maxResults, firstResult);
    }

    private List<Rol> findRolEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Rol.class));
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

    public Rol findRol(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Rol.class, id);
        } finally {
            em.close();
        }
    }

    public int getRolCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Rol> rt = cq.from(Rol.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
