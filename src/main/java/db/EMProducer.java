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
        // ホルダーにEMが無い場合やあるけどトランザクションが終了している場合(※1)は新規取得
        // ※1 ジョブの複数起動でスレッドが再利用された場合
        if(holder.getEM()== null || !holder.getEM().isOpen()) {
            EntityManager em = emf.createEntityManager();
            holder.setEM(em);
        }
        
        EntityManager em = holder.getEM();
        System.out.println(em.hashCode());
        
        return em;
    }
}
