package com.example.springakka.aop;

import com.example.springakka.service.TransactionManager;
import com.example.springakka.util.HibernateUtil;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by Ido on 2017/9/27.
 */
@Aspect
public class DaoAspect {
    private static final Logger logger = LoggerFactory.getLogger(DaoAspect.class);
    private final  static SessionFactory sessionFactory = HibernateUtil.getSessionFactory();

//    @Pointcut("within(com.example.springakka.repository..*) && target(com.example.springakka.service.TransactionManager)")
//    public void daoLayer() {}

    @Pointcut("within(com.example.springakka.service..*) && target(com.example.springakka.service.TransactionManager)")
    public void serviceLayer() {}

//    @Pointcut("execution(public * *(..))")
//    public void atPublicExecution(){}

//    @Pointcut("@annotation(com.example.springakka.aop.InjectSession) && target(com.example.springakka.service.TransactionManager)")
//    public void session(){}

//    @Pointcut("@annotation(com.example.springakka.aop.Transactional) && serviceLayer()")
//    public void daoTransationMethod() {}
    

    @Pointcut("@annotation(com.example.springakka.aop.Transactional) && serviceLayer() ")
    public void serviceTransationMethod() {}
    
    


    @Around("serviceTransationMethod()")
    public Object serviceTransactionManage(ProceedingJoinPoint pjp) throws Throwable {
        Transaction transaction = null;
        TransactionManager manager = (TransactionManager) pjp.getTarget();
        logger.info("serviceTransactionManage open session  ");
        Session session  =  sessionFactory.openSession();
        manager.setSession(session);
        Object retVal = null;
        try {
        		if(session == null) {
        			logger.error("can not get session");
        			return null;
        		}
            logger.info("transaction begin");
            transaction = session.getTransaction();
            transaction.begin();
            retVal = pjp.proceed();
            transaction.commit();
            logger.info("transaction commit");
        } catch (Exception ex) {
            if (transaction != null) {
                transaction.rollback();
            }
            logger.error("method : serviceTransactionManage , message : DAO operation failed"+ ex.getMessage(),ex );
        } finally {
            if (session != null) {
                session.close();
            }
        }

        return retVal;
    }
}
