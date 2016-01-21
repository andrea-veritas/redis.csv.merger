package com.tpadsz.utils.merger;

/**
 * Created by Roger on 2016/1/21.
 */
public class StringArrayComparator {
    String[] array;
    public StringArrayComparator(String[] array) {
        this.array = array;
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
            for(int i=0;i<array.length;i++){
                if(compare(flag.compareTo(array[i]),max)){
                    index=i;
                    flag=array[i];
                }
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
