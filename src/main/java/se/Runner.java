package se;


import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Properties;
import java.util.concurrent.CompletableFuture;
import javax.batch.operations.JobOperator;
import javax.batch.runtime.BatchRuntime;
import javax.batch.runtime.JobExecution;

/**
 *
 */
public class Runner {

    public static void main(String[] args) {
        
        JobOperator jobOperator = BatchRuntime.getJobOperator();
        
        // 同時実行
        CompletableFuture.allOf(
            CompletableFuture.runAsync(() -> executeJob(jobOperator,"chunk-sample")),
            CompletableFuture.runAsync(() -> executeJob(jobOperator,"chunk-sample"))
            //CompletableFuture.runAsync(() -> executeJob(jobOperator,"cleanup-job"))
          //  CompletableFuture.runAsync(() -> executeJob(jobOperator,"cleanup-job")),
          //  CompletableFuture.runAsync(() -> executeJob(jobOperator,"cleanup-job"))
        ).join();
        executeJob(jobOperator,"cleanup-job");
        System.exit(0);
    }
    
    private static void executeJob(JobOperator jobOperator, String jobId) {
        long execID=0;
        // ジョブの開始時はXML読み込みなど同時実行されるとおかしくなる処理があるので、同期化しておく。
        synchronized(jobOperator){
            Properties props = new Properties();
            props.setProperty("date", LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd")));
            execID = jobOperator.start(jobId, props);
            System.out.println("job stated id:" + execID);
        }
        JobExecution jobExec = null;
        // ジョブが終了するまでポーリング
        while (true) {
            jobExec = jobOperator.getJobExecution(execID);
            if (jobExec.getEndTime() != null) {
                break;
            }

            try {
                Thread.sleep(1000L);
            } catch (InterruptedException ex) {
            }
        }
        System.out.println("JOB END:Status is " + jobExec.getExitStatus() );
        
    }
}
