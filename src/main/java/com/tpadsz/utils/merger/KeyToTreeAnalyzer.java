package com.tpadsz.utils.merger;

import com.alibaba.fastjson.JSON;
import com.tpadsz.utils.merger.entities.MongodbJsonBean;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

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
        try {
            while((line=reader.readLine())!=null){

                System.out.println(
                        JSON.toJSON(MongodbJsonBean.fromCSV(line)
                        )
                );

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
