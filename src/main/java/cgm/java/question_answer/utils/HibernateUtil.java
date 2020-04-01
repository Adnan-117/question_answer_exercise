package cgm.java.question_answer.utils;


import java.util.Properties;

import cgm.java.question_answer.model.Answers;
import cgm.java.question_answer.model.Question;
import org.hibernate.SessionFactory;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.cfg.Configuration;
import org.hibernate.cfg.Environment;
import org.hibernate.service.ServiceRegistry;


public class HibernateUtil {

  private static SessionFactory sessionFactory;

  public static SessionFactory getSessionFactory() {
    if (sessionFactory == null) {
      try {
        // jdbc and hibernate configuration
        Configuration configuration = new Configuration();

        Properties settings = getProperties();

        configuration.setProperties(settings);
        configuration.addAnnotatedClass(Question.class);
        configuration.addAnnotatedClass(Answers.class);

        ServiceRegistry serviceRegistry = new StandardServiceRegistryBuilder()
            .applySettings(configuration.getProperties()).build();

        sessionFactory = configuration.buildSessionFactory(serviceRegistry);

      } catch (Exception e) {
        System.out.println("Cannot create database connection");
        e.printStackTrace();
      }
    }
    return sessionFactory;
  }

  private static Properties getProperties() {
    Properties settings = new Properties();
    settings.put(Environment.DRIVER, "com.mysql.cj.jdbc.Driver");
    settings.put(Environment.URL,
                 "jdbc:mysql://localhost:3306/db?createDatabaseIfNotExist=true&useUnicode=yes&characterEncoding=UTF-8");
    settings.put(Environment.USER, "user");
    settings.put(Environment.PASS, "password");
    settings.put(Environment.DIALECT, "org.hibernate.dialect.MySQL5Dialect");
//    settings.put(Environment.SHOW_SQL, "true");
    settings.put(Environment.CURRENT_SESSION_CONTEXT_CLASS, "thread");
    settings.put(Environment.HBM2DDL_AUTO, "update");
    return settings;
  }

}
