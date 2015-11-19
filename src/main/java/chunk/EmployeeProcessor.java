package chunk;

import entity.Employee;
import javax.batch.api.chunk.ItemProcessor;
import javax.enterprise.context.Dependent;
import javax.inject.Named;

/**
 *
 */
@Dependent
@Named
public class EmployeeProcessor implements ItemProcessor {

    @Override
    public Object processItem(Object item) throws Exception {
        
        String line = (String)item;
        
        Employee e = new Employee();
        e.setName(line);
        
        return e;
    }

    
    
}
