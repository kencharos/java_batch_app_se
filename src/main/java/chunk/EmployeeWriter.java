package chunk;

import java.io.Serializable;
import java.util.List;
import javax.batch.api.chunk.ItemWriter;
import javax.enterprise.context.Dependent;
import javax.inject.Inject;
import javax.inject.Named;
import javax.persistence.EntityManager;

/**
 *
 */
@Dependent
@Named
public class EmployeeWriter implements ItemWriter {

    @Inject
    private  EntityManager em;
    
    private MyCheckPoint myCheckpoint;
    
    @Override
    public void writeItems(List<Object> items) throws Exception {
            System.out.println("items size:" + items.size());
            
            for (Object obj : items) {
                myCheckpoint.addCount();
                em.persist(obj);
            }
    }

    @Override
    public void open(Serializable checkpoint) throws Exception {
        // checkpoint が来るのはリトライのみ。
        if (checkpoint == null) {
            System.out.println("checkpoint null");
            myCheckpoint = new MyCheckPoint();
            
        } else {
            myCheckpoint = (MyCheckPoint)checkpoint;
        }
        
        System.out.println("witer cp:" + myCheckpoint.getCount());
   }

    @Override
    public void close() throws Exception {
        System.out.println("close call;" + em.hashCode());
        em.close();
    }

    @Override
    public Serializable checkpointInfo() throws Exception {
       // リトライ時の取得のために使われる。生成用ではない。
       // フィールドに保持しておいて、ライトのたびに更新が基本。
       // ItremReaderと共有されるものでもない。
        return this.myCheckpoint;
    }

    
    
}
