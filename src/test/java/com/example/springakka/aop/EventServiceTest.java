package com.example.springakka.aop;

import com.example.springakka.entity.Event;
import com.example.springakka.repository.EventDao;

import com.example.springakka.service.EventService;
import org.junit.Test;

/**
 * Created by Ido on 2017/9/29.
 */
public class EventServiceTest {


    @Test
    public void testNestTransaction(){
        EventService service = new EventService();
//        EventDao dao = new EventDao();
        Event e = new Event();
        e.setName("hello");
        Event e1 = new Event();
        e1.setName("test");
        service.createEvent(e);
//        dao.save(e1);
        service.createEvent(e1);
    }
}
