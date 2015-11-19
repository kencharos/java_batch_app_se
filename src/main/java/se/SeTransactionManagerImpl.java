package se;

import db.EMProducer;
import com.ibm.jbatch.container.exception.TransactionManagementException;
import com.ibm.jbatch.container.services.impl.BatchTransactionServiceImpl;
import com.ibm.jbatch.spi.services.TransactionManagerAdapter;
import javax.batch.runtime.context.StepContext;
import javax.enterprise.inject.spi.Bean;
import javax.enterprise.inject.spi.BeanManager;
import javax.enterprise.inject.spi.CDI;
import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;

/**
 *
 */
public class SeTransactionManagerImpl extends BatchTransactionServiceImpl {

    @Override
    public TransactionManagerAdapter getTransactionManager(StepContext stepContext) throws TransactionManagementException {
        
        return new TransactionManagerAdapter() {
            EntityTransaction tx;
            @Override
            public void begin() {
                
                BeanManager beanManager = CDI.current().getBeanManager();Bean<?> empBean = beanManager.getBeans(EMProducer.class).iterator().next();
        
                EMProducer emp = (EMProducer)beanManager.getReference(empBean, EMProducer.class, beanManager.createCreationalContext(empBean));
                EntityManager em = emp.getEm();
                tx = em.getTransaction();
                System.out.println("tx begin");
                tx.begin();
            }

            @Override
            public void commit() {
                System.out.println("tx commit");
                tx.commit();
            }

            @Override
            public int getStatus() {
                return 0;
            }

            @Override
            public void rollback() {
                tx.rollback();
            }

            @Override
            public void setRollbackOnly() {
                tx.setRollbackOnly();
           }

            @Override
            public void setTransactionTimeout(int arg0) {
                
            }
        };
    }

    
}
