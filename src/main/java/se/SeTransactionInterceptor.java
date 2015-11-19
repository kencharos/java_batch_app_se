package se;

import db.EMHolder;
import javax.inject.Inject;
import javax.interceptor.AroundInvoke;
import javax.interceptor.Interceptor;
import javax.interceptor.InvocationContext;
import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;
import javax.transaction.Transactional;

/**
 *
 */
@Transactional
@Interceptor
public class SeTransactionInterceptor {

    @Inject
    private EntityManager em;
    
    @Inject
    private EMHolder holder;

    @AroundInvoke
    public Object doTransaction(InvocationContext ic) throws Exception {
        EntityTransaction tx = em.getTransaction();
        Object result = null;
        try {
            if (!tx.isActive()) {
                tx.begin();
            }
            
            System.out.println("tx interceptor start");
            result = ic.proceed();
            if (tx.isActive()) {
                tx.commit();
            } 
        } catch (Exception e) {
            try {
                if (tx.isActive()) {
                    tx.rollback();
                }
            } catch (Exception e2) {
            }
            throw e;
        } finally{
            holder.remove();
            if(em.isOpen()) {
                em.close();
            }
        }
        return result;
    }
}
