package db;

import javax.enterprise.context.Dependent;
import javax.enterprise.inject.Produces;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

/**
 *
 */
@Dependent
public class EMProducer {
    
    EntityManagerFactory emf = Persistence.createEntityManagerFactory("default");
    
    @Inject
    private EMHolder holder;
    
    @Produces
    public EntityManager getEm() {
        
        if(holder.get() == null) {
            EntityManager em = emf.createEntityManager();
            holder.add(em);
        }
        
        EntityManager em = holder.get();
        System.out.println(em.hashCode());
        
        return em;
    }
    
    
}
