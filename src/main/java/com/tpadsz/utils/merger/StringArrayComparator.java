package com.tpadsz.utils.merger;

import java.util.List;

/**
 * Created by Roger on 2016/1/21.
 */
public class StringArrayComparator {


    String[] array;
    public StringArrayComparator(String[] array) {
        this.array = array;
    }

    public StringArrayComparator(List<String> array){
        this.array=array.toArray(new String[array.size()]);
    }
    public void setArray(String[] array) {
        this.array = array;
    }
    public void setArray(List<String> array){
        this.array=array.toArray(new String[array.size()]);
    }
    public int max(){
        return findMaxOrMinElementIndex(this.array,true);
    }

    public int min(){
        return findMaxOrMinElementIndex(this.array,false);
    }


    private int findMaxOrMinElementIndex(String[] array,boolean max){
        if(array==null||array.length<1){
            return -1;
        }else{
            int index=0;
            String flag=array[0];
            for(int i=1;i<array.length;i++){
                if(flag==null&&i<array.length-1){
                    flag=array[i+1];
                }
                if(compare(flag.compareTo(array[i]),max)){
                    index=i;
                    flag=array[i];
                }
            }
            if(flag==null){
                return -1;
            }
            return index;
        }
    }

    private boolean compare(int i,boolean max){
        if(max){
            return i<0;
        }else{
            return i>0;
        }
    }


}
