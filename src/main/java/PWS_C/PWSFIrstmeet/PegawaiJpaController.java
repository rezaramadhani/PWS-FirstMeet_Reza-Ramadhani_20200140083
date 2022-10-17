/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package PWS_C.PWSFIrstmeet;

import PWS_C.PWSFIrstmeet.exceptions.IllegalOrphanException;
import PWS_C.PWSFIrstmeet.exceptions.NonexistentEntityException;
import PWS_C.PWSFIrstmeet.exceptions.PreexistingEntityException;
import java.io.Serializable;
import javax.persistence.Query;
import javax.persistence.EntityNotFoundException;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

/**
 *
 * @author Reza Ramadhani
 */
public class PegawaiJpaController implements Serializable {

    public PegawaiJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = Persistence.createEntityManagerFactory("PWS_C_PWSFIrstmeet_jar_0.0.1-SNAPSHOTPU");

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Pegawai pegawai) throws PreexistingEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            PengelolaanData pengelolaanData = pegawai.getPengelolaanData();
            if (pengelolaanData != null) {
                pengelolaanData = em.getReference(pengelolaanData.getClass(), pengelolaanData.getIdPengelolaan());
                pegawai.setPengelolaanData(pengelolaanData);
            }
            em.persist(pegawai);
            if (pengelolaanData != null) {
                Pegawai oldIdPegawaiOfPengelolaanData = pengelolaanData.getIdPegawai();
                if (oldIdPegawaiOfPengelolaanData != null) {
                    oldIdPegawaiOfPengelolaanData.setPengelolaanData(null);
                    oldIdPegawaiOfPengelolaanData = em.merge(oldIdPegawaiOfPengelolaanData);
                }
                pengelolaanData.setIdPegawai(pegawai);
                pengelolaanData = em.merge(pengelolaanData);
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            if (findPegawai(pegawai.getIdPegawai()) != null) {
                throw new PreexistingEntityException("Pegawai " + pegawai + " already exists.", ex);
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(Pegawai pegawai) throws IllegalOrphanException, NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Pegawai persistentPegawai = em.find(Pegawai.class, pegawai.getIdPegawai());
            PengelolaanData pengelolaanDataOld = persistentPegawai.getPengelolaanData();
            PengelolaanData pengelolaanDataNew = pegawai.getPengelolaanData();
            List<String> illegalOrphanMessages = null;
            if (pengelolaanDataOld != null && !pengelolaanDataOld.equals(pengelolaanDataNew)) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("You must retain PengelolaanData " + pengelolaanDataOld + " since its idPegawai field is not nullable.");
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            if (pengelolaanDataNew != null) {
                pengelolaanDataNew = em.getReference(pengelolaanDataNew.getClass(), pengelolaanDataNew.getIdPengelolaan());
                pegawai.setPengelolaanData(pengelolaanDataNew);
            }
            pegawai = em.merge(pegawai);
            if (pengelolaanDataNew != null && !pengelolaanDataNew.equals(pengelolaanDataOld)) {
                Pegawai oldIdPegawaiOfPengelolaanData = pengelolaanDataNew.getIdPegawai();
                if (oldIdPegawaiOfPengelolaanData != null) {
                    oldIdPegawaiOfPengelolaanData.setPengelolaanData(null);
                    oldIdPegawaiOfPengelolaanData = em.merge(oldIdPegawaiOfPengelolaanData);
                }
                pengelolaanDataNew.setIdPegawai(pegawai);
                pengelolaanDataNew = em.merge(pengelolaanDataNew);
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                String id = pegawai.getIdPegawai();
                if (findPegawai(id) == null) {
                    throw new NonexistentEntityException("The pegawai with id " + id + " no longer exists.");
                }
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void destroy(String id) throws IllegalOrphanException, NonexistentEntityException {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Pegawai pegawai;
            try {
                pegawai = em.getReference(Pegawai.class, id);
                pegawai.getIdPegawai();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The pegawai with id " + id + " no longer exists.", enfe);
            }
            List<String> illegalOrphanMessages = null;
            PengelolaanData pengelolaanDataOrphanCheck = pegawai.getPengelolaanData();
            if (pengelolaanDataOrphanCheck != null) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This Pegawai (" + pegawai + ") cannot be destroyed since the PengelolaanData " + pengelolaanDataOrphanCheck + " in its pengelolaanData field has a non-nullable idPegawai field.");
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            em.remove(pegawai);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<Pegawai> findPegawaiEntities() {
        return findPegawaiEntities(true, -1, -1);
    }

    public List<Pegawai> findPegawaiEntities(int maxResults, int firstResult) {
        return findPegawaiEntities(false, maxResults, firstResult);
    }

    private List<Pegawai> findPegawaiEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Pegawai.class));
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

    public Pegawai findPegawai(String id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Pegawai.class, id);
        } finally {
            em.close();
        }
    }

    public int getPegawaiCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Pegawai> rt = cq.from(Pegawai.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
