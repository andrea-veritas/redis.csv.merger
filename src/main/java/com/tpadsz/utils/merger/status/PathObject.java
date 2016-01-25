package com.tpadsz.utils.merger.status;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by roger.wang on 2016/1/26.
 */
public class PathObject{
    public PathObject(String key, Long size_in_bytes) {
        this.key = key;
        this.size_in_bytes = size_in_bytes;
    }

    public String getKey() {
        return key;
    }

    public Long getSize_in_bytes() {
        return size_in_bytes;
    }

    public void addSize_in_bytes(Long size_in_bytes){
        this.size_in_bytes+=size_in_bytes;
    }

    public static List<PathObject> fromStringPaths(String[] path,Long size_in_bytes){
        List<PathObject> ret=new ArrayList<PathObject>(path.length);
        for(String key:path){
            ret.add(new PathObject(key,size_in_bytes));
        }
        return ret;
    }

    String key;
    Long size_in_bytes;
}