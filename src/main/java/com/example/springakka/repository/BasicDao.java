package com.example.springakka.repository;

import com.example.springakka.aop.Transactional;
import com.example.springakka.service.TransactionManager;
import org.hibernate.Query;
import org.hibernate.Session;

import java.io.Serializable;
import java.util.List;

/**
 * Created by Ido on 2017/9/27.
 */
public abstract class BasicDao<T extends Serializable> extends TransactionManager{
    private  Class<T> type;


    public BasicDao(Class<T> type) {
        this.type = type;
    }

    public Session getSession() {
		return session;
	}

	public void setSession(Session session) {
		this.session = session;
	}

    public void save(T t) {
        session.save(t);
    }

    public void update(T t) {
        session.update(t);
    }



    public void save(List<T> ts){
        ts.forEach(t->{
            session.save(t);
        });

    }

    public void delete(T t) {
        session.delete(t);

    }

    public void delete(List<T> ts) {
        ts.forEach(t->{
            session.delete(t);
        });

    }

    public List<T> findAll(){
        String sql = "from "+ type.getSimpleName() ;
        System.out.println(sql);
        Query query = session.createQuery(sql);
        return query.list();

    }
}
