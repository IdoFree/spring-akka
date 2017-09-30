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

    @Pointcut("within(com.example.springakka.repository..*)")
    public void daoLayer() {}

    @Pointcut("within(com.example.springakka.service..*)")
    public void serviceLayer() {}

    @Pointcut("execution(* *(..))")
    public void atExecution(){}

//    @Pointcut("@annotation(com.example.springakka.aop.Transactional) && daoLayer()")
//    public void daoTransationMethod() {}
    
    @Before("daoLayer()")
    public void injectSession(JoinPoint joinPoint) throws Throwable {
    		logger.info("inject session before calling DAO ");
	    	TransactionManager manager = (TransactionManager) joinPoint.getTarget();
            logger.info("injectSession open session  ");
	    	Session session  =  sessionFactory.openSession();
	    	if(session == null){
                logger.error("can not get session");
                return ;
            }
            logger.info(session.toString());
	    	manager.setSession(session);

    }

    @Pointcut("@annotation(com.example.springakka.aop.Transactional) && serviceLayer() ")
    public void serviceTransationMethod() {}
    
    
//    @Around("daoTransationMethod()")
//    public Object transactionManage(ProceedingJoinPoint pjp) throws Throwable {
//        Transaction transaction = null;
//        TransactionManager manager = (TransactionManager) pjp.getTarget();
//        Session session  =  sessionFactory.openSession();
//        manager.setSession(session);
//        Object retVal = null;
//        try {
//            logger.info("transaction begin");
//            transaction = session.getTransaction();
//            transaction.begin();
//            retVal = pjp.proceed();
//            transaction.commit();
//            logger.info("transaction commit");
//        } catch (Exception ex) {
//            if (transaction != null) {
//                transaction.rollback();
//            }
//            logger.info("method : daoTransationMethod , message : DAO operation failed"+ ex.getMessage() );
//        } finally {
//            if (session != null) {
//                session.close();
//            }
//        }
//
//        return retVal;
//    }


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
