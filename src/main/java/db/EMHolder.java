package db;

import javax.enterprise.context.ApplicationScoped;
import javax.persistence.EntityManager;

/**
 * スレッドローカルでEntityMangerを保持するBean
 */
@ApplicationScoped
public class EMHolder {

    private ThreadLocal<EntityManager> holder = new ThreadLocal<>();
    
    public void setEM(EntityManager em) {
        holder.set(em);
    }
    
    public EntityManager getEM() {
        return holder.get();
    }
    
    public void removeEM() {
        holder.remove();
    }
}
