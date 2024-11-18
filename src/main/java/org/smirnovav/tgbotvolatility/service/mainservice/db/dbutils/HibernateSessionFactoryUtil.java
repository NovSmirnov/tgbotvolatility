package org.smirnovav.tgbotvolatility.service.mainservice.db.dbutils;

import jakarta.persistence.NoResultException;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Root;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.cfg.Configuration;
import org.hibernate.query.Query;
import org.smirnovav.tgbotvolatility.service.configuration.AppConfig;
import org.smirnovav.tgbotvolatility.service.configuration.DbConfig;
import org.smirnovav.tgbotvolatility.service.mainservice.db.entities.AtrValueEntity;
import org.smirnovav.tgbotvolatility.service.mainservice.db.entities.IntegratedFutVolLiqEntity;
import org.springframework.context.ApplicationContext;

import java.util.List;
import java.util.Properties;

public class HibernateSessionFactoryUtil {

    private static SessionFactory sessionFactory;
    private static Properties properties;

    private HibernateSessionFactoryUtil() {
    }
    static {
        ApplicationContext context = AppConfig.getContext();
        DbConfig dbConfig = context.getBean(DbConfig.class);
        properties = new Properties();
        properties.put("hibernate.connection.driver_class", dbConfig.getDriver());
        properties.put("hibernate.connection.url", dbConfig.getUrl());
        properties.put("hibernate.dialect", dbConfig.getDialect());
        properties.put("show_sql", dbConfig.getShowSQL());
    }

    public static SessionFactory getCreationSessionFactory() {
        if (sessionFactory == null || sessionFactory.isClosed()) {

            Configuration configuration = new Configuration()
                    .addAnnotatedClass(IntegratedFutVolLiqEntity.class)
                    .addAnnotatedClass(AtrValueEntity.class)
                    .setProperties(properties)
                    .setProperty("hibernate.hbm2ddl.auto", "create");
            ;
            try {
                StandardServiceRegistryBuilder builder = new StandardServiceRegistryBuilder().applySettings(configuration.getProperties());
                sessionFactory = configuration.buildSessionFactory(builder.build());
            } catch (Exception e) {
                System.out.println("Ошибка подключения к локальной базе данных!");
                e.printStackTrace();
            }
        }
        return sessionFactory;
    }

    public static SessionFactory getRequestSessionFactory() {
        if (sessionFactory == null || sessionFactory.isClosed()) {
            Configuration configuration = new Configuration()
                    .addAnnotatedClass(IntegratedFutVolLiqEntity.class)
                    .addAnnotatedClass(AtrValueEntity.class)
                    .setProperties(properties);
            try {
                StandardServiceRegistryBuilder builder = new StandardServiceRegistryBuilder().applySettings(configuration.getProperties());
                sessionFactory = configuration.buildSessionFactory(builder.build());
            } catch (Exception e) {
                System.out.println("Ошибка подключения к локальной базе данных!");
                e.printStackTrace();
            }
        }
        return sessionFactory;
    }

    public static boolean isBaseExist() {

        if (sessionFactory == null || sessionFactory.isClosed()) {

            Configuration configuration = new Configuration()
                    .addAnnotatedClass(IntegratedFutVolLiqEntity.class)
                    .addAnnotatedClass(AtrValueEntity.class)
                    .setProperties(properties)
            ;
            try {
                StandardServiceRegistryBuilder builder = new StandardServiceRegistryBuilder()
                        .applySettings(configuration.getProperties());
                sessionFactory = configuration.buildSessionFactory(builder.build());
                Session session = sessionFactory.openSession();
                CriteriaBuilder cBuilder = session.getCriteriaBuilder();
                CriteriaQuery<IntegratedFutVolLiqEntity> criteria = cBuilder.createQuery(IntegratedFutVolLiqEntity.class);
                Root<IntegratedFutVolLiqEntity> root = criteria.from(IntegratedFutVolLiqEntity.class);
                criteria.select(root);
                Query<IntegratedFutVolLiqEntity> query = session.createQuery(criteria);
                try {
                    List<IntegratedFutVolLiqEntity> integratedFutVolLiqEntities = query.getResultList();
                } catch (NoResultException e) {
                    e.printStackTrace();
                    return false;
                } finally {
                    session.close();
                    sessionFactory.close();
                }
                return true;
            } catch (HibernateException e) {
                return false;
            } finally {
                sessionFactory.close();
            }
        }
        return true;
    }

}
