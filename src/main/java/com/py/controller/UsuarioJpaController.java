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
import com.py.modelo.Grupo;
import java.util.ArrayList;
import java.util.Collection;
import com.py.modelo.Rol;
import com.py.modelo.Usuario;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.transaction.UserTransaction;

/**
 *
 * @author ACER-PC
 */
public class UsuarioJpaController implements Serializable {

    public UsuarioJpaController(UserTransaction utx, EntityManagerFactory emf) {
        this.utx = utx;
        this.emf = emf;
    }
    private UserTransaction utx = null;
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Usuario usuario) throws RollbackFailureException, Exception {
        if (usuario.getGrupoCollection() == null) {
            usuario.setGrupoCollection(new ArrayList<Grupo>());
        }
        if (usuario.getRolCollection() == null) {
            usuario.setRolCollection(new ArrayList<Rol>());
        }
        EntityManager em = null;
        try {
            utx.begin();
            em = getEntityManager();
            Collection<Grupo> attachedGrupoCollection = new ArrayList<Grupo>();
            for (Grupo grupoCollectionGrupoToAttach : usuario.getGrupoCollection()) {
                grupoCollectionGrupoToAttach = em.getReference(grupoCollectionGrupoToAttach.getClass(), grupoCollectionGrupoToAttach.getId());
                attachedGrupoCollection.add(grupoCollectionGrupoToAttach);
            }
            usuario.setGrupoCollection(attachedGrupoCollection);
            Collection<Rol> attachedRolCollection = new ArrayList<Rol>();
            for (Rol rolCollectionRolToAttach : usuario.getRolCollection()) {
                rolCollectionRolToAttach = em.getReference(rolCollectionRolToAttach.getClass(), rolCollectionRolToAttach.getId());
                attachedRolCollection.add(rolCollectionRolToAttach);
            }
            usuario.setRolCollection(attachedRolCollection);
            em.persist(usuario);
            for (Grupo grupoCollectionGrupo : usuario.getGrupoCollection()) {
                grupoCollectionGrupo.getUsuarioCollection().add(usuario);
                grupoCollectionGrupo = em.merge(grupoCollectionGrupo);
            }
            for (Rol rolCollectionRol : usuario.getRolCollection()) {
                rolCollectionRol.getUsuarioCollection().add(usuario);
                rolCollectionRol = em.merge(rolCollectionRol);
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

    public void edit(Usuario usuario) throws NonexistentEntityException, RollbackFailureException, Exception {
        EntityManager em = null;
        try {
            utx.begin();
            em = getEntityManager();
            Usuario persistentUsuario = em.find(Usuario.class, usuario.getId());
            Collection<Grupo> grupoCollectionOld = persistentUsuario.getGrupoCollection();
            Collection<Grupo> grupoCollectionNew = usuario.getGrupoCollection();
            Collection<Rol> rolCollectionOld = persistentUsuario.getRolCollection();
            Collection<Rol> rolCollectionNew = usuario.getRolCollection();
            Collection<Grupo> attachedGrupoCollectionNew = new ArrayList<Grupo>();
            for (Grupo grupoCollectionNewGrupoToAttach : grupoCollectionNew) {
                grupoCollectionNewGrupoToAttach = em.getReference(grupoCollectionNewGrupoToAttach.getClass(), grupoCollectionNewGrupoToAttach.getId());
                attachedGrupoCollectionNew.add(grupoCollectionNewGrupoToAttach);
            }
            grupoCollectionNew = attachedGrupoCollectionNew;
            usuario.setGrupoCollection(grupoCollectionNew);
            Collection<Rol> attachedRolCollectionNew = new ArrayList<Rol>();
            for (Rol rolCollectionNewRolToAttach : rolCollectionNew) {
                rolCollectionNewRolToAttach = em.getReference(rolCollectionNewRolToAttach.getClass(), rolCollectionNewRolToAttach.getId());
                attachedRolCollectionNew.add(rolCollectionNewRolToAttach);
            }
            rolCollectionNew = attachedRolCollectionNew;
            usuario.setRolCollection(rolCollectionNew);
            usuario = em.merge(usuario);
            for (Grupo grupoCollectionOldGrupo : grupoCollectionOld) {
                if (!grupoCollectionNew.contains(grupoCollectionOldGrupo)) {
                    grupoCollectionOldGrupo.getUsuarioCollection().remove(usuario);
                    grupoCollectionOldGrupo = em.merge(grupoCollectionOldGrupo);
                }
            }
            for (Grupo grupoCollectionNewGrupo : grupoCollectionNew) {
                if (!grupoCollectionOld.contains(grupoCollectionNewGrupo)) {
                    grupoCollectionNewGrupo.getUsuarioCollection().add(usuario);
                    grupoCollectionNewGrupo = em.merge(grupoCollectionNewGrupo);
                }
            }
            for (Rol rolCollectionOldRol : rolCollectionOld) {
                if (!rolCollectionNew.contains(rolCollectionOldRol)) {
                    rolCollectionOldRol.getUsuarioCollection().remove(usuario);
                    rolCollectionOldRol = em.merge(rolCollectionOldRol);
                }
            }
            for (Rol rolCollectionNewRol : rolCollectionNew) {
                if (!rolCollectionOld.contains(rolCollectionNewRol)) {
                    rolCollectionNewRol.getUsuarioCollection().add(usuario);
                    rolCollectionNewRol = em.merge(rolCollectionNewRol);
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

    public void destroy(Integer id) throws NonexistentEntityException, RollbackFailureException, Exception {
        EntityManager em = null;
        try {
            utx.begin();
            em = getEntityManager();
            Usuario usuario;
            try {
                usuario = em.getReference(Usuario.class, id);
                usuario.getId();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The usuario with id " + id + " no longer exists.", enfe);
            }
            Collection<Grupo> grupoCollection = usuario.getGrupoCollection();
            for (Grupo grupoCollectionGrupo : grupoCollection) {
                grupoCollectionGrupo.getUsuarioCollection().remove(usuario);
                grupoCollectionGrupo = em.merge(grupoCollectionGrupo);
            }
            Collection<Rol> rolCollection = usuario.getRolCollection();
            for (Rol rolCollectionRol : rolCollection) {
                rolCollectionRol.getUsuarioCollection().remove(usuario);
                rolCollectionRol = em.merge(rolCollectionRol);
            }
            em.remove(usuario);
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
