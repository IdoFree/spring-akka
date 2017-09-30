package com.example.springakka.aop;

import com.example.springakka.entity.Event;
import com.example.springakka.service.EventService;
import org.junit.Test;

/**
 * Created by Ido on 2017/9/29.
 */
public class EventServiceTest {


    @Test
    public void testsaveTransaction(){
        EventService service = new EventService();
        Event e = new Event();
        e.setName("hello");
        service.save(e);
    }
    
    
}
