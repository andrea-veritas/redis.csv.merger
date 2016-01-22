package com.tpadsz.utils.merger;

import com.alibaba.fastjson.JSON;
import com.tpadsz.utils.merger.contants.BeanOfferEventType;
import com.tpadsz.utils.merger.entities.BeanOfferEvent;
import com.tpadsz.utils.merger.entities.MongodbJsonBean;

import java.io.*;
import java.util.HashMap;
import java.util.Map;
import java.util.Queue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;

/**
 * Created by Roger on 2016/1/21.
 */
public class KeyToTreeAnalyzer implements Runnable{

    private String[] argv;
    public KeyToTreeAnalyzer(String argv[])
    {
        this.argv=argv;
    }


    public void run() {
        BufferedReader reader=new BufferedReader(new InputStreamReader(System.in));
        String line=null;
        File fForest=new File("forest");


        if (fForest.exists()){
            System.err.println(String.format("%s exits. Please make room for the forest folder.",fForest.getAbsolutePath()));
            try {
                reader.close();
            }catch (IOException e){

            }finally {
                System.exit(0);
            }
        }
        fForest.mkdir();
        System.out.println(String.format("Created forest folder: %s",fForest.getAbsolutePath()));

        try {
            String root=null;

            Map<String,Thread> threadPool=new HashMap<String, Thread>();
            Thread latestThread=null;
            // Begin to process the forest.
            while((line=reader.readLine())!=null){

                BlockingQueue<BeanOfferEvent<BeanOfferEventType>> queue=new LinkedBlockingQueue<BeanOfferEvent<BeanOfferEventType>>();
                MongodbJsonBean bean = MongodbJsonBean.fromCSV(line);
                if(bean.get_id().equals(root)==false){
                    // This "if" means we are going to a new tree.


                }else{

                }

                /*
                System.out.println(
                        JSON.toJSON(MongodbJsonBean.fromCSV(line)
                        )
                );
                */



            }
        } catch (IOException e) {
            e.printStackTrace();
        }


        try {
            reader.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public static void main(String[] argv){
        new Thread(new KeyToTreeAnalyzer(argv)).start();
    }

}

class JsonBeanReceiver implements  Runnable{
    private static final int MAX_RETRY_COUNT=3;

    File fTree=null;
    BufferedWriter out=null;

    BlockingQueue<BeanOfferEvent<MongodbJsonBean>> queue;
    public JsonBeanReceiver(BlockingQueue<BeanOfferEvent<MongodbJsonBean>> queue) {
        this.queue = queue;
    }
    public void run() {
        int retryCounter=0;
        Map<String,Long> branches=new HashMap<String, Long>();

        while(true) {
            try {
                BeanOfferEvent<MongodbJsonBean> event = queue.poll(1000L, TimeUnit.MILLISECONDS);
                if (BeanOfferEventType.EVENT_BEAN_OFFER_ADD.equals(event.getEventType())) {
                    String _id=event.getElement().get_id();
                    if(out==null){
                        try {
                            createFile(_id.substring(0,_id.indexOf("-")));
                        }catch (Exception ex){
                            System.err.println(String.format("Fatal! Cannot create file %s\nException: %s",fTree.getAbsolutePath(),ex.getMessage()));
                            break;
                        }
                    }else{
                        try {
                            out.write(JSON.toJSONString(event.getElement()));
                        } catch (IOException ex) {
                            System.err.println(String.format("Fatal! Cannot write to file %s\nwith _id %s\nException: %s",fTree.getAbsolutePath(),_id,ex.getMessage()));
                            break;
                        }

                        if(_id.contains("_")){
                            
                        }
                    }


                } else if (BeanOfferEventType.EVENT_BEAN_OFFER_END.equals(event.getEventType())) {


                    break;
                }

            } catch (InterruptedException e) {
                if(++retryCounter>MAX_RETRY_COUNT){
                    break;
                    //Thread.currentThread().interrupt();
                }
            }
        }

    }

    private void closeFile() throws Exception{
        if(out!=null){
            out.flush();
            out.close();
        }
    }

    private void createFile(String treeName) throws Exception{
        fTree=new File(treeName);
        if(fTree.exists()){
            fTree.delete();
        }
        fTree.createNewFile();
        out=new BufferedWriter(new FileWriter(fTree));

    }
}
