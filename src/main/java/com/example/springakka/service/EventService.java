package com.example.springakka.service;

import com.example.springakka.aop.InjectSession;
import com.example.springakka.aop.Transactional;
import com.example.springakka.entity.Event;
import com.example.springakka.repository.EventDao;
import org.hibernate.Session;

import java.util.List;

/**
 * Created by Ido on 2017/9/29.
 */
public class EventService extends TransactionManager {
    private EventDao eventDao = new EventDao();


    @Override
    public void setSession(Session session) {
        this.session = session;
        eventDao.setSession(session);
    }


    @Transactional
    public void save(Event e){
        eventDao.save(e);

       

    }

    public Event findByName(String name) {
        return eventDao.findByName(name);
    }

    public Session getSession() {
        return eventDao.getSession();
    }

    @InjectSession
    public void update(Event event) {
        eventDao.update(event);
    }

    @InjectSession
    public void save(List<Event> events) {
        eventDao.save(events);
    }

    @InjectSession
    public void delete(Event event) {
        eventDao.delete(event);
    }

    @InjectSession
    public void delete(List<Event> events) {
        eventDao.delete(events);
    }

    @InjectSession
    public List<Event> findAll() {
        return eventDao.findAll();
    }
}
