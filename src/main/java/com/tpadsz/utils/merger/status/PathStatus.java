package com.tpadsz.utils.merger.status;

import com.tpadsz.utils.merger.IdStringToPathsConverter;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by roger.wang on 2016/1/25.
 */
public class PathStatus {

    List<PathObject> status=new ArrayList<PathObject>();


    public List<PathObject> enroll(String[] paths,Long size_in_bytes){
        boolean identicalFlag=true;
        int i;
        for(i=0;i<paths.length;i++){
            if(i>=this.status.size()){
                for(int j=i;j<paths.length;j++){
                    this.status.add(new PathObject(paths[j],size_in_bytes));
                }
                return null;
            }
            if(paths[i].equals(this.status.get(i).getKey())){
                this.status.get(i).addSize_in_bytes(size_in_bytes);
            }else{
                identicalFlag=false;
                break;
            }
        }

        if(identicalFlag){
            return null;
        }else{
            List<PathObject> ret = Arrays.asList(Arrays.copyOfRange(this.status.toArray(new PathObject[0]),i,this.status.size()));
            this.status.removeAll(ret);
            this.status.addAll(PathObject.fromStringPaths(Arrays.copyOfRange(paths, i, paths.length),size_in_bytes));
            return ret;
        }


    }

    public List<PathObject> getStatus() {
        return status;
    }

}
