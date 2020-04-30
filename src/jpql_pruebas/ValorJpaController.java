/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package jpql_pruebas;

import java.io.Serializable;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Query;
import javax.persistence.EntityNotFoundException;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import jpql_pruebas.exceptions.NonexistentEntityException;
import jpql_pruebas.exceptions.PreexistingEntityException;

public class ValorJpaController implements Serializable {

    public ValorJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Valor valor) throws PreexistingEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            em.persist(valor);
            em.getTransaction().commit();
        } catch (Exception ex) {
            if (findValor(valor.getNombre()) != null) {
                throw new PreexistingEntityException("Valor " + valor + " already exists.", ex);
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(Valor valor) throws NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            valor = em.merge(valor);
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                String id = valor.getNombre();
                if (findValor(id) == null) {
                    throw new NonexistentEntityException("The valor with id " + id + " no longer exists.");
                }
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void destroy(String id) throws NonexistentEntityException {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Valor valor;
            try {
                valor = em.getReference(Valor.class, id);
                valor.getNombre();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The valor with id " + id + " no longer exists.", enfe);
            }
            em.remove(valor);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<Valor> findValorEntities() {
        return findValorEntities(true, -1, -1);
    }

    public List<Valor> findValorEntities(int maxResults, int firstResult) {
        return findValorEntities(false, maxResults, firstResult);
    }

    private List<Valor> findValorEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Valor.class));
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

    public Valor findValor(String id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Valor.class, id);
        } finally {
            em.close();
        }
    }

    public int getValorCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Valor> rt = cq.from(Valor.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }

}
