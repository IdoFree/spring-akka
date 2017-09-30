package com.example.springakka.service;

import com.example.springakka.aop.Transactional;
import com.example.springakka.entity.Event;
import com.example.springakka.entity.Tag;
import com.example.springakka.repository.EventDao;
import com.example.springakka.repository.TagDao;
import org.hibernate.Session;

/**
 * Created by Ido on 2017/9/29.
 */
public class EventService extends TransactionManager {
    private EventDao eventDao = new EventDao();
    private TagDao tagDao = new TagDao();


    public Session getSession() {
        return session;
    }

    public void setSession(Session session) {
        this.session = session;
    }

    @Transactional
    public void createEvent(Event e){
        eventDao.save(e);

        Tag tag = new Tag();
        tag.setName("test");
        if(e.getName().equals("test")){
            throw new RuntimeException("");
        }

        tagDao.save(tag);

    }


}
