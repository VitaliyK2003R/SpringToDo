package com.emobile.springtodo.config;

import jakarta.persistence.EntityManager;
import org.hibernate.SessionFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class HibernateConfig {

    @Bean
    public org.hibernate.cfg.Configuration configuration() {
        return new org.hibernate.cfg.Configuration().configure("hibernate/hibernate.cfg.xml");
    }

    @Bean
    public SessionFactory sessionFactory(org.hibernate.cfg.Configuration configuration) {
        return configuration.buildSessionFactory();
    }

    @Bean
    public EntityManager entityManager(SessionFactory sessionFactory) {
        return sessionFactory.createEntityManager();
    }
}
