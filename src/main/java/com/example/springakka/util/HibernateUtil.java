package com.example.springakka.util; /**
 * Created by Ido on 2017/9/22.
 */

import org.hibernate.SessionFactory;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.cfg.Configuration;


/**
 * @author imssbora
 */
public class HibernateUtil {

    private static SessionFactory sessionFactory;

    synchronized public static SessionFactory getSessionFactory() {
        if (sessionFactory == null) {
            try {


                Configuration configuration = new Configuration();
                configuration.configure("hibernate.cfg.xml");
                StandardServiceRegistryBuilder ssrb = new StandardServiceRegistryBuilder().applySettings(configuration.getProperties());
                sessionFactory = configuration.buildSessionFactory(ssrb.build());
                System.out.println("config session factory successfully.");
            } catch (Exception e) {

                e.printStackTrace();
            }
        }
        return sessionFactory;
    }

    public static void shutdown() {
        if(sessionFactory!=null){
            sessionFactory.close();
        }
    }
}
