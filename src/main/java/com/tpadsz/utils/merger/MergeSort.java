package com.tpadsz.utils.merger;


import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * Created by Roger on 2016/1/21.
 */
public class MergeSort implements Runnable{
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
            new Thread(new MergeSort(files.toArray(new File[files.size()]))).start();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
    File[] files;
//    BufferedReader readers[];
    List<BufferedReader> readers;
    String latestLine=null;
//    String[] lineBuffer=null;
    List<String> lineBuffer;
    private MergeSort(File[] argv) throws IOException {
        this.files=argv;
//        this.readers=new BufferedReader[this.files.length];
//        this.lineBuffer=new String[this.files.length];
        this.readers = new ArrayList<BufferedReader>(argv.length);
        this.lineBuffer = new ArrayList<String>(argv.length);
        for(int i=0;i<this.files.length;i++){
//            this.readers[i]=new BufferedReader(new FileReader(this.files[i]));
            this.readers.add(i,new BufferedReader(new FileReader(this.files[i])));
//            lineBuffer[i]=this.readers[i].readLine();
            this.lineBuffer.add(i,this.readers.get(i).readLine());
        }
    }

    private List<BufferedReader> removeEmptyFileByIndex(List<String> lines,List<BufferedReader> readers,int index) throws IllegalArgumentException{
        if(lines==null||readers==null){
            throw new IllegalArgumentException("lines or readers are null.");

        }
        if(lines.size()!=readers.size()){
            throw new IllegalArgumentException("length of lines incompatible to readers.");
        }

        lines.remove(index);
        BufferedReader reader=readers.remove(index);
        return Arrays.asList(new BufferedReader[]{reader});
    }


    private List<BufferedReader> removeEmptyFile(List<String> lines,List<BufferedReader> readers)throws IllegalArgumentException{
        if(lines==null||readers==null){
            throw new IllegalArgumentException("lines or readers are null.");

        }
        if(lines.size()!=readers.size()){
            throw new IllegalArgumentException("length of lines incompatible to readers.");
        }

        List<BufferedReader> ret=new ArrayList<BufferedReader>();
        for(int i=0;i<lines.size();i++){
            if(lines.get(i)==null){
                lines.remove(i);
                ret.add(readers.get(i));
                readers.remove(i);
            }
        }
        return ret;
    }

    private void closeReaders(List<BufferedReader> readers){
        for(BufferedReader reader:readers){
            if(reader!=null){
                try {
                    reader.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public void run() {
        closeReaders(removeEmptyFile(this.lineBuffer, this.readers));
        StringArrayComparator cmp=new StringArrayComparator(this.lineBuffer);
        while(isNullArray(this.readers)==false){
            cmp.setArray(this.lineBuffer);
            int index=cmp.min();
//            System.out.println(this.lineBuffer[index]);
            System.out.println(this.lineBuffer.get(index));
            try {
//                String line=this.readers[index].readLine();
                String line=this.readers.get(index).readLine();
                if(line!=null) {
//                    this.lineBuffer[index] = line;
                    this.lineBuffer.set(index,line);
                }else{
//                    closeReaders(removeEmptyFile(this.lineBuffer,this.readers));
                    closeReaders(removeEmptyFileByIndex(this.lineBuffer,this.readers,index));
                }
            } catch (IOException e) {
                System.err.println(e.getMessage());
            }
        }
        try {
            close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    private boolean isNullArray(Object[] array){
        if(array==null){
            return true;
        }
        for(Object obj:array){
            if(obj!=null){
                return false;
            }
        }
        return true;
    }
    private boolean isNullArray(List array){
        if(array==null){
            return true;
        }
        for(Object obj:array){
            if(obj!=null){
                return false;
            }
        }
        return true;
    }


    private void close() throws IOException {
        for(BufferedReader rd:this.readers){
            rd.close();
        }
    }
}

class NullRemoverForArrays<E>{
    E[] array;
    List<E> newList;
    E[] newArray;
    public NullRemoverForArrays(E[] array) {
        this.array = array;

        if(array!=null){
            newList=new ArrayList<E>(array.length);
            for(int i=0;i<array.length;i++){
                newList.add(array[i]);
            }

            newArray=(E[])newList.toArray();
        }else{
            newList=null;
        }

    }

    public E[] getNoNullArray(){
        if(newList==null){
            return null;
        }
        return newArray;
    }



}