package db;

import javax.enterprise.context.ApplicationScoped;
import javax.persistence.EntityManager;

/**
 *
 */
@ApplicationScoped
public class EMHolder {

    private ThreadLocal<EntityManager> holder = new ThreadLocal<>();
    
    public void add(EntityManager em) {
        holder.set(em);
    }
    
    public EntityManager get() {
        return holder.get();
    }
    
    public void remove() {
        holder.remove();
    }
    
}
