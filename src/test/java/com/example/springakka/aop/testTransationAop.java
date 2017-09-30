package com.example.springakka.aop;

import com.example.springakka.entity.Event;
import com.example.springakka.repository.BasicDao;
import com.example.springakka.repository.EventDao;
import com.example.springakka.util.HibernateUtil;

import java.util.List;

import org.junit.Assert;
import org.junit.Test;

/**
 * Created by Ido on 2017/9/28.
 */
public class testTransationAop {
//    @Test
    public void testBasicDao(){
        EventDao basicDao = new EventDao();
        Event e = basicDao.findByName("jay");
        e.setTicketCount(e.getTicketCount() - 100);
        basicDao.save(e);
        System.out.println(e.getName());
        Assert.assertNotNull(e);
        HibernateUtil.shutdown();
    }


    @Test
    public void testFindAll(){
        EventDao basicDao = new EventDao();
        List result =  basicDao.findAll();
        
        Assert.assertNotNull(result);
    }
    
    @Test
    public void testSave(){
        EventDao basicDao = new EventDao();
        Event e = new Event();
        e.setName("hello");
        basicDao.save(e );
    }
}
