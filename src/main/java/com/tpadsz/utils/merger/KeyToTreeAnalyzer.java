package com.tpadsz.utils.merger;

import com.tpadsz.utils.merger.contants.BeanOfferEventType;
import com.tpadsz.utils.merger.entities.BeanOfferEvent;
import com.tpadsz.utils.merger.entities.MongodbJsonBean;

import java.io.*;
import java.lang.ref.SoftReference;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.BlockingQueue;
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
    public static final int EVENT_QUEUE_SIZE=100000;


    private void enqueueWithWaiting(BlockingQueue<BeanOfferEvent<MongodbJsonBean>> queue,BeanOfferEvent event){
        boolean isEnqueued=false;
        do{
            synchronized (queue){
                isEnqueued=queue.offer(event);
            }
        }while (!isEnqueued);
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
                System.err.println(e.getMessage());
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
            BlockingQueue<BeanOfferEvent<MongodbJsonBean>> latestQueue=null;

            // Begin to process the forest.
            while((line=reader.readLine())!=null){

                BlockingQueue<BeanOfferEvent<BeanOfferEventType>> queue=new LinkedBlockingQueue<BeanOfferEvent<BeanOfferEventType>>(EVENT_QUEUE_SIZE);
                MongodbJsonBean bean = MongodbJsonBean.fromCSV(line);

                if(IdStringToPathsConverter.getRoot(bean.get_id()).equals(root)==false){
                    // This "if" means we are going to a new tree.
                    root= IdStringToPathsConverter.getRoot(bean.get_id());
                    if(latestQueue!=null){
                        enqueueWithWaiting(latestQueue, new BeanOfferEvent<MongodbJsonBean>(null, BeanOfferEventType.EVENT_BEAN_OFFER_END));
                    }
                    latestQueue=new LinkedBlockingQueue<BeanOfferEvent<MongodbJsonBean>>(EVENT_QUEUE_SIZE);
                    enqueueWithWaiting(latestQueue,new BeanOfferEvent<MongodbJsonBean>(bean, BeanOfferEventType.EVENT_BEAN_OFFER_ADD));
                    latestThread=new Thread(new JsonBeanReceiver(latestQueue));
                    latestThread.start();

                }else{
                    enqueueWithWaiting(latestQueue, new BeanOfferEvent<MongodbJsonBean>(bean, BeanOfferEventType.EVENT_BEAN_OFFER_ADD));
                }

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
    //Map<String,Long> pathSizeMap=new HashMap<String, Long>();


    SoftReference<Map<String,Long>> refMap=new SoftReference<Map<String, Long>>(new HashMap<String, Long>());


    BlockingQueue<BeanOfferEvent<MongodbJsonBean>> queue;
    public JsonBeanReceiver(BlockingQueue<BeanOfferEvent<MongodbJsonBean>> queue) {
        this.queue = queue;
    }
    public void run() {
        int retryCounter=0;


        while(true) {
            try {
                BeanOfferEvent<MongodbJsonBean> event = null;
                synchronized (queue) {
                    event=queue.poll(1000L, TimeUnit.MILLISECONDS);
                }
                System.out.println(String.format("Received BeanOfferEvent: %s",event.getElement()==null?"null":event.getElement().toString()));

                if (BeanOfferEventType.EVENT_BEAN_OFFER_ADD.equals(event.getEventType())) {
                    String _id=event.getElement().get_id();
                    if(out==null){
                        try {
                            if(_id.contains("_")){
                                createFile(_id.substring(0,_id.indexOf("_")));
                            }else{
                                createFile(_id);
                            }

                        }catch (Exception ex){
                            System.err.println(String.format("Fatal! Cannot create file %s\nException: %s",fTree.getAbsolutePath(),ex.getMessage()));
                            try {
                                finish();
                            } catch (Exception e) {
                                System.err.println(e.getMessage());
                            }
                            return;
                        }
                    }
                    try {
                        String json=event.getElement().toString();
                        out.write(json);
                        System.out.println(String.format("Write leaf to buffer: %s",json));

                        out.newLine();
                    } catch (IOException ex) {
                        System.err.println(String.format("Fatal! Cannot write to file %s\nwith _id %s\nException: %s",fTree.getAbsolutePath(),_id,ex.getMessage()));
                        try {
                            finish();
                        } catch (Exception e) {
                            System.err.println(e.getMessage());
                        }
                        return;
                    }

                    if(_id.contains("_")){
                        String[] paths=new IdStringToPathsConverter(_id).getPathStrings();
                        // ignore the last path which must be the path to the leaf node.
                        for(int i=0;i<paths.length-1;i++){
                            String path=paths[i];
                            if(refMap.get().containsKey(path)){
                                refMap.get().put(path, refMap.get().get(path) + event.getElement().getSize_in_bytes());
                            }else{
                                refMap.get().put(path, event.getElement().getSize_in_bytes());
                            }
                        }
                    }

                } else if (BeanOfferEventType.EVENT_BEAN_OFFER_END.equals(event.getEventType())) {
                    System.out.println(String.format("Received end event for path"));
                    for(String path:refMap.get().keySet()){
                        Long size_in_bytes=refMap.get().get(path);
                        String csvLine=String.format("\"%s\",%s",path,Long.toString(size_in_bytes));
                        String json=MongodbJsonBean.fromCSV(csvLine).setIs_leaf(false).toString();
                        try {
                            out.write(json);
                            System.out.println(String.format("Writing analyzed path to buffer: %s",json));
                            out.newLine();
                        } catch (IOException e) {
                            System.err.println(e.getMessage());
                            try {
                                finish();
                            } catch (Exception e1) {
                                System.err.println(e1.getMessage());
                            }
                            return;
                        }
                    }

                    try {
                        finish();
                    } catch (Exception e) {
                        System.err.println(e.getMessage());
                    }finally {
                        return;
                    }
                }

            } catch (InterruptedException e) {
                if(++retryCounter>MAX_RETRY_COUNT){
                    //break;
                    try {
                        finish();
                    } catch (Exception e1) {
                        System.err.println(e1.getMessage());
                    }
                    return;
                }
            }
        }

    }

    private void finish() throws Exception{
        refMap.clear();
        if(out!=null){
            out.flush();
            out.close();
        }
    }

    private void createFile(String treeName) throws Exception{
        fTree=new File("forest/"+treeName);
        if(fTree.exists()){
            fTree.delete();
        }
        fTree.createNewFile();
        out=new BufferedWriter(new FileWriter(fTree));

        System.out.println(String.format("Created tree file: %s", fTree.getAbsolutePath()));
    }


}
