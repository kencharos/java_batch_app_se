package chunk;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.Serializable;
import javax.batch.api.BatchProperty;
import javax.batch.api.chunk.ItemReader;
import javax.batch.runtime.context.JobContext;
import javax.enterprise.context.Dependent;
import javax.inject.Inject;
import javax.inject.Named;

/**
 *
 */
@Dependent
@Named
public class EmployeeFileReader implements ItemReader {

    private MyCheckPoint checkpoint;
    
    private BufferedReader br;
    
    @Inject
    private JobContext ctx;
    
    @Inject
    @BatchProperty
    private String input;
    
    @Override
    public Serializable checkpointInfo() throws Exception {
        return checkpoint;
    }

    @Override
    public void open(Serializable ckpt) throws Exception {
        if (ckpt == null) {
            checkpoint = new MyCheckPoint();
        } else {
            checkpoint = (MyCheckPoint) ckpt;
        }
        br = new BufferedReader(new FileReader(input));
        
        // 復帰時の処理済行の読み飛ばし
        for (int i = 0; i < checkpoint.getCount(); i++) {
            br.readLine();
        }
    }

    @Override
    public Object readItem() throws Exception {
        checkpoint.addCount();;
        return br.readLine();
    }

    @Override
    public void close() throws Exception {
        br.close();
    }

    
    
    

    
}
