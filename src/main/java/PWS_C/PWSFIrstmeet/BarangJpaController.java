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
public class BarangJpaController implements Serializable {

    public BarangJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = Persistence.createEntityManagerFactory("PWS_C_PWSFIrstmeet_jar_0.0.1-SNAPSHOTPU");

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Barang barang) throws IllegalOrphanException, PreexistingEntityException, Exception {
        List<String> illegalOrphanMessages = null;
        PengelolaanData pengelolaanDataOrphanCheck = barang.getPengelolaanData();
        if (pengelolaanDataOrphanCheck != null) {
            Barang oldBarangOfPengelolaanData = pengelolaanDataOrphanCheck.getBarang();
            if (oldBarangOfPengelolaanData != null) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("The PengelolaanData " + pengelolaanDataOrphanCheck + " already has an item of type Barang whose pengelolaanData column cannot be null. Please make another selection for the pengelolaanData field.");
            }
        }
        if (illegalOrphanMessages != null) {
            throw new IllegalOrphanException(illegalOrphanMessages);
        }
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            PengelolaanData pengelolaanData = barang.getPengelolaanData();
            if (pengelolaanData != null) {
                pengelolaanData = em.getReference(pengelolaanData.getClass(), pengelolaanData.getIdPengelolaan());
                barang.setPengelolaanData(pengelolaanData);
            }
            Transaksi transaksi = barang.getTransaksi();
            if (transaksi != null) {
                transaksi = em.getReference(transaksi.getClass(), transaksi.getIdTransaksi());
                barang.setTransaksi(transaksi);
            }
            em.persist(barang);
            if (pengelolaanData != null) {
                pengelolaanData.setBarang(barang);
                pengelolaanData = em.merge(pengelolaanData);
            }
            if (transaksi != null) {
                Barang oldKodeBarangOfTransaksi = transaksi.getKodeBarang();
                if (oldKodeBarangOfTransaksi != null) {
                    oldKodeBarangOfTransaksi.setTransaksi(null);
                    oldKodeBarangOfTransaksi = em.merge(oldKodeBarangOfTransaksi);
                }
                transaksi.setKodeBarang(barang);
                transaksi = em.merge(transaksi);
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            if (findBarang(barang.getKodeBarang()) != null) {
                throw new PreexistingEntityException("Barang " + barang + " already exists.", ex);
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(Barang barang) throws IllegalOrphanException, NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Barang persistentBarang = em.find(Barang.class, barang.getKodeBarang());
            PengelolaanData pengelolaanDataOld = persistentBarang.getPengelolaanData();
            PengelolaanData pengelolaanDataNew = barang.getPengelolaanData();
            Transaksi transaksiOld = persistentBarang.getTransaksi();
            Transaksi transaksiNew = barang.getTransaksi();
            List<String> illegalOrphanMessages = null;
            if (pengelolaanDataNew != null && !pengelolaanDataNew.equals(pengelolaanDataOld)) {
                Barang oldBarangOfPengelolaanData = pengelolaanDataNew.getBarang();
                if (oldBarangOfPengelolaanData != null) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("The PengelolaanData " + pengelolaanDataNew + " already has an item of type Barang whose pengelolaanData column cannot be null. Please make another selection for the pengelolaanData field.");
                }
            }
            if (transaksiOld != null && !transaksiOld.equals(transaksiNew)) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("You must retain Transaksi " + transaksiOld + " since its kodeBarang field is not nullable.");
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            if (pengelolaanDataNew != null) {
                pengelolaanDataNew = em.getReference(pengelolaanDataNew.getClass(), pengelolaanDataNew.getIdPengelolaan());
                barang.setPengelolaanData(pengelolaanDataNew);
            }
            if (transaksiNew != null) {
                transaksiNew = em.getReference(transaksiNew.getClass(), transaksiNew.getIdTransaksi());
                barang.setTransaksi(transaksiNew);
            }
            barang = em.merge(barang);
            if (pengelolaanDataOld != null && !pengelolaanDataOld.equals(pengelolaanDataNew)) {
                pengelolaanDataOld.setBarang(null);
                pengelolaanDataOld = em.merge(pengelolaanDataOld);
            }
            if (pengelolaanDataNew != null && !pengelolaanDataNew.equals(pengelolaanDataOld)) {
                pengelolaanDataNew.setBarang(barang);
                pengelolaanDataNew = em.merge(pengelolaanDataNew);
            }
            if (transaksiNew != null && !transaksiNew.equals(transaksiOld)) {
                Barang oldKodeBarangOfTransaksi = transaksiNew.getKodeBarang();
                if (oldKodeBarangOfTransaksi != null) {
                    oldKodeBarangOfTransaksi.setTransaksi(null);
                    oldKodeBarangOfTransaksi = em.merge(oldKodeBarangOfTransaksi);
                }
                transaksiNew.setKodeBarang(barang);
                transaksiNew = em.merge(transaksiNew);
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                String id = barang.getKodeBarang();
                if (findBarang(id) == null) {
                    throw new NonexistentEntityException("The barang with id " + id + " no longer exists.");
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
            Barang barang;
            try {
                barang = em.getReference(Barang.class, id);
                barang.getKodeBarang();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The barang with id " + id + " no longer exists.", enfe);
            }
            List<String> illegalOrphanMessages = null;
            Transaksi transaksiOrphanCheck = barang.getTransaksi();
            if (transaksiOrphanCheck != null) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This Barang (" + barang + ") cannot be destroyed since the Transaksi " + transaksiOrphanCheck + " in its transaksi field has a non-nullable kodeBarang field.");
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            PengelolaanData pengelolaanData = barang.getPengelolaanData();
            if (pengelolaanData != null) {
                pengelolaanData.setBarang(null);
                pengelolaanData = em.merge(pengelolaanData);
            }
            em.remove(barang);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<Barang> findBarangEntities() {
        return findBarangEntities(true, -1, -1);
    }

    public List<Barang> findBarangEntities(int maxResults, int firstResult) {
        return findBarangEntities(false, maxResults, firstResult);
    }

    private List<Barang> findBarangEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Barang.class));
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

    public Barang findBarang(String id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Barang.class, id);
        } finally {
            em.close();
        }
    }

    public int getBarangCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Barang> rt = cq.from(Barang.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
