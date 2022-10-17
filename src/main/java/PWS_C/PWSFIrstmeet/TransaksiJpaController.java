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

/**
 *
 * @author Reza Ramadhani
 */
public class TransaksiJpaController implements Serializable {

    public TransaksiJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Transaksi transaksi) throws IllegalOrphanException, PreexistingEntityException, Exception {
        List<String> illegalOrphanMessages = null;
        Barang kodeBarangOrphanCheck = transaksi.getKodeBarang();
        if (kodeBarangOrphanCheck != null) {
            Transaksi oldTransaksiOfKodeBarang = kodeBarangOrphanCheck.getTransaksi();
            if (oldTransaksiOfKodeBarang != null) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("The Barang " + kodeBarangOrphanCheck + " already has an item of type Transaksi whose kodeBarang column cannot be null. Please make another selection for the kodeBarang field.");
            }
        }
        Pembeli idPembeliOrphanCheck = transaksi.getIdPembeli();
        if (idPembeliOrphanCheck != null) {
            Transaksi oldTransaksiOfIdPembeli = idPembeliOrphanCheck.getTransaksi();
            if (oldTransaksiOfIdPembeli != null) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("The Pembeli " + idPembeliOrphanCheck + " already has an item of type Transaksi whose idPembeli column cannot be null. Please make another selection for the idPembeli field.");
            }
        }
        if (illegalOrphanMessages != null) {
            throw new IllegalOrphanException(illegalOrphanMessages);
        }
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Barang kodeBarang = transaksi.getKodeBarang();
            if (kodeBarang != null) {
                kodeBarang = em.getReference(kodeBarang.getClass(), kodeBarang.getKodeBarang());
                transaksi.setKodeBarang(kodeBarang);
            }
            Pembeli idPembeli = transaksi.getIdPembeli();
            if (idPembeli != null) {
                idPembeli = em.getReference(idPembeli.getClass(), idPembeli.getIdPembeli());
                transaksi.setIdPembeli(idPembeli);
            }
            em.persist(transaksi);
            if (kodeBarang != null) {
                kodeBarang.setTransaksi(transaksi);
                kodeBarang = em.merge(kodeBarang);
            }
            if (idPembeli != null) {
                idPembeli.setTransaksi(transaksi);
                idPembeli = em.merge(idPembeli);
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            if (findTransaksi(transaksi.getIdTransaksi()) != null) {
                throw new PreexistingEntityException("Transaksi " + transaksi + " already exists.", ex);
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(Transaksi transaksi) throws IllegalOrphanException, NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Transaksi persistentTransaksi = em.find(Transaksi.class, transaksi.getIdTransaksi());
            Barang kodeBarangOld = persistentTransaksi.getKodeBarang();
            Barang kodeBarangNew = transaksi.getKodeBarang();
            Pembeli idPembeliOld = persistentTransaksi.getIdPembeli();
            Pembeli idPembeliNew = transaksi.getIdPembeli();
            List<String> illegalOrphanMessages = null;
            if (kodeBarangNew != null && !kodeBarangNew.equals(kodeBarangOld)) {
                Transaksi oldTransaksiOfKodeBarang = kodeBarangNew.getTransaksi();
                if (oldTransaksiOfKodeBarang != null) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("The Barang " + kodeBarangNew + " already has an item of type Transaksi whose kodeBarang column cannot be null. Please make another selection for the kodeBarang field.");
                }
            }
            if (idPembeliNew != null && !idPembeliNew.equals(idPembeliOld)) {
                Transaksi oldTransaksiOfIdPembeli = idPembeliNew.getTransaksi();
                if (oldTransaksiOfIdPembeli != null) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("The Pembeli " + idPembeliNew + " already has an item of type Transaksi whose idPembeli column cannot be null. Please make another selection for the idPembeli field.");
                }
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            if (kodeBarangNew != null) {
                kodeBarangNew = em.getReference(kodeBarangNew.getClass(), kodeBarangNew.getKodeBarang());
                transaksi.setKodeBarang(kodeBarangNew);
            }
            if (idPembeliNew != null) {
                idPembeliNew = em.getReference(idPembeliNew.getClass(), idPembeliNew.getIdPembeli());
                transaksi.setIdPembeli(idPembeliNew);
            }
            transaksi = em.merge(transaksi);
            if (kodeBarangOld != null && !kodeBarangOld.equals(kodeBarangNew)) {
                kodeBarangOld.setTransaksi(null);
                kodeBarangOld = em.merge(kodeBarangOld);
            }
            if (kodeBarangNew != null && !kodeBarangNew.equals(kodeBarangOld)) {
                kodeBarangNew.setTransaksi(transaksi);
                kodeBarangNew = em.merge(kodeBarangNew);
            }
            if (idPembeliOld != null && !idPembeliOld.equals(idPembeliNew)) {
                idPembeliOld.setTransaksi(null);
                idPembeliOld = em.merge(idPembeliOld);
            }
            if (idPembeliNew != null && !idPembeliNew.equals(idPembeliOld)) {
                idPembeliNew.setTransaksi(transaksi);
                idPembeliNew = em.merge(idPembeliNew);
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                String id = transaksi.getIdTransaksi();
                if (findTransaksi(id) == null) {
                    throw new NonexistentEntityException("The transaksi with id " + id + " no longer exists.");
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
            Transaksi transaksi;
            try {
                transaksi = em.getReference(Transaksi.class, id);
                transaksi.getIdTransaksi();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The transaksi with id " + id + " no longer exists.", enfe);
            }
            Barang kodeBarang = transaksi.getKodeBarang();
            if (kodeBarang != null) {
                kodeBarang.setTransaksi(null);
                kodeBarang = em.merge(kodeBarang);
            }
            Pembeli idPembeli = transaksi.getIdPembeli();
            if (idPembeli != null) {
                idPembeli.setTransaksi(null);
                idPembeli = em.merge(idPembeli);
            }
            em.remove(transaksi);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<Transaksi> findTransaksiEntities() {
        return findTransaksiEntities(true, -1, -1);
    }

    public List<Transaksi> findTransaksiEntities(int maxResults, int firstResult) {
        return findTransaksiEntities(false, maxResults, firstResult);
    }

    private List<Transaksi> findTransaksiEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Transaksi.class));
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

    public Transaksi findTransaksi(String id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Transaksi.class, id);
        } finally {
            em.close();
        }
    }

    public int getTransaksiCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Transaksi> rt = cq.from(Transaksi.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
