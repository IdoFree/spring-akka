package com.example.springakka.repository;

import com.example.springakka.aop.Transactional;
import com.example.springakka.entity.Event;
import org.hibernate.Query;

/**
 * Created by Ido on 2017/9/27.
 */
public class EventDao extends BasicDao<Event> {

    public EventDao() {
        super(Event.class);
    }

    public Event findByName(String name) {
        Query qy = session.createQuery("select e  from Event e where name = :name");
        qy.setParameter("name", name);
        Event evt = (Event) qy.uniqueResult();

        return evt;

    }


}
