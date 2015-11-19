package batchlet;

import javax.batch.api.AbstractBatchlet;
import javax.batch.api.BatchProperty;
import javax.batch.runtime.BatchRuntime;
import javax.batch.runtime.context.JobContext;
import javax.batch.runtime.context.StepContext;
import javax.enterprise.context.Dependent;
import javax.inject.Inject;
import javax.inject.Named;
import javax.persistence.EntityManager;
import javax.transaction.Transactional;

/**
 * データ削除
 */
@Dependent
@Named
@Transactional
public class DataDeleteProcess extends AbstractBatchlet{

    @Inject
    private JobContext ctx;

    @Inject
    private EntityManager em;
    
    @Inject
    @BatchProperty
    private String text;
    
    
    @Inject
    @BatchProperty
    private String ptext;
    
    @Inject
    private StepContext stepCtx;
    
    @Override
    public String process() throws Exception {
        // ジョブコンテキストからプロパティを取得
        java.util.Properties p = ctx.getProperties();
        System.out.println(BatchRuntime.getJobOperator().getParameters(ctx.getExecutionId()));
        System.out.println(Thread.currentThread().getName());
        System.out.println("executedate-" + 
                BatchRuntime.getJobOperator().getParameters(ctx.getExecutionId()).getProperty("exec_date") 
                +" ptext-" + text
                +" -text_from_ctx-" + p.getProperty("ptext") 
                + " -text" + stepCtx.getProperties().getProperty("text")
                +" ptext_from_inject:" + ptext); //NG 全体プロパティはインジェクションできない。
        
        int deleted = em.createQuery("delete from Employee e").executeUpdate();
        
        System.out.println(deleted + " rows deleted.");
       
        
        // 終了ステータス
        return "COMPLETE";
    }
}
    
   
