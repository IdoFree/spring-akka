package com.example.springakka.service;

import com.example.springakka.aop.InjectSession;
import com.example.springakka.aop.Transactional;
import com.example.springakka.entity.Event;
import com.example.springakka.entity.Tag;
import com.example.springakka.repository.EventDao;
import com.example.springakka.repository.TagDao;
import org.hibernate.Session;

import java.util.List;

/**
 * Created by Ido on 2017/9/29.
 */
public class EventService extends TransactionManager {
    private EventDao eventDao = new EventDao();
    private TagDao tagDao = new TagDao();

    @Override
    public void setSession(Session session) {
        this.session = session;
        eventDao.setSession(session);
        tagDao.setSession(session);
    }


    @Transactional
    public void save(Event e){
        eventDao.save(e);

    }

    @Transactional
    public void createEvent(Event e){
        eventDao.save(e);
        if(e.getName().equals("test")){
            throw new RuntimeException("test not save");
        }

        Tag tag = new Tag();
        tag.setName(e.getName());
        tag.setRelateId(e.getId());
        tagDao.save(tag);

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
