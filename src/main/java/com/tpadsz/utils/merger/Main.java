package com.tpadsz.utils.merger;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Roger on 2016/1/21.
 */
public class Main implements Runnable{
    public static void main(String[] argv){
        if(argv.length<1){
            System.out.println("Need at least 1 file to merge.");
            return;

        }
        List<File> files=new ArrayList<File>(argv.length);
        for(String str:argv){
            File file=new File(str);
            if(!file.exists()
                    || !file.canRead()){
                System.out.println(String.format("%s : Cannot open for reading."));
                return;
            }else{
                files.add(file);
            }
        }

        try {
            new Thread(new Main(files.toArray(new File[files.size()]))).start();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
    File[] files;
    BufferedReader readers[];
    String latestLine=null;
    String[] lineBuffer=null;
    private Main(File[] argv) throws IOException {
        this.files=argv;
        this.readers=new BufferedReader[this.files.length];
        this.lineBuffer=new String[this.files.length];
        for(int i=0;i<this.files.length;i++){
            this.readers[i]=new BufferedReader(new FileReader(this.files[i]));
            lineBuffer[i]=this.readers[i].readLine();
        }
    }

    public void run() {






        try {
            close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }



    private void close() throws IOException {
        for(BufferedReader rd:this.readers){
            rd.close();
        }
    }
}
