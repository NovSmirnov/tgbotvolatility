package org.smirnovav.tgbotvolatility.service.mainservice.db.dao;

import jakarta.persistence.NoResultException;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Root;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.query.Query;
import org.smirnovav.tgbotvolatility.service.mainservice.db.dbutils.HibernateSessionFactoryUtil;
import org.smirnovav.tgbotvolatility.service.mainservice.db.entities.AtrValueEntity;
import org.smirnovav.tgbotvolatility.service.mainservice.db.entities.IntegratedFutVolLiqEntity;

import java.util.List;

public class Dao {
    private Session session;

    public void saveAll(List<IntegratedFutVolLiqEntity> integratedFutVolLiqEntityList) {
        for (IntegratedFutVolLiqEntity entity : integratedFutVolLiqEntityList) {
            for (AtrValueEntity atrValueEntity : entity.getValues()) {
                atrValueEntity.setIntegratedFutVolLiq(entity);
            }
        }
        session = HibernateSessionFactoryUtil.getCreationSessionFactory().openSession();
        Transaction ta1 = session.beginTransaction();
        for (IntegratedFutVolLiqEntity entity : integratedFutVolLiqEntityList) {
            session.persist(entity);
            for (AtrValueEntity atrValueEntity : entity.getValues()) {
                session.persist(atrValueEntity);
            }
        }
        ta1.commit();
        session.close();
    }

    public List<IntegratedFutVolLiqEntity> loadAll() {
        session = HibernateSessionFactoryUtil.getRequestSessionFactory().openSession();
        CriteriaBuilder builder = session.getCriteriaBuilder();
        CriteriaQuery<IntegratedFutVolLiqEntity> criteria = builder.createQuery(IntegratedFutVolLiqEntity.class);
        Root<IntegratedFutVolLiqEntity> root = criteria.from(IntegratedFutVolLiqEntity.class);
        criteria.select(root);
        Query<IntegratedFutVolLiqEntity> query = session.createQuery(criteria);
        try {
            List<IntegratedFutVolLiqEntity> integratedFutVolLiqEntities = query.getResultList();
            session.close();
            return integratedFutVolLiqEntities;
        } catch (NoResultException e) {
            session.close();
            e.printStackTrace();
            return null;
        }
    }

    public void cleanDb() {
        session = HibernateSessionFactoryUtil.getRequestSessionFactory().openSession();
        String stringQuery1 = "DELETE FROM AtrValueEntity";
        String stringQuery2 = "DELETE FROM IntegratedFutVolLiqEntity";
        String[] queries = {stringQuery1, stringQuery2};
        Transaction tx1 = session.beginTransaction();
        for (String strQuery : queries) {
            jakarta.persistence.Query query = session.createQuery(strQuery, null);
            query.executeUpdate();
        }
        tx1.commit();
        session.close();
    }

}
