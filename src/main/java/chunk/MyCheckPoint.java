package chunk;

import java.io.Serializable;

/**
 *
 */
public class MyCheckPoint implements  Serializable{
    
    private int count;
    
    public int getCount() {
        return count;
    }
    
    public void addCount() {
        count++;
    }

}
