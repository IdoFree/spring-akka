package com.example.springakka.service;

import org.hibernate.Session;

/**
 * Created by Ido on 2017/9/29.
 */
public abstract class TransactionManager {
    protected Session session;



    abstract public void setSession(Session session) ;
}
