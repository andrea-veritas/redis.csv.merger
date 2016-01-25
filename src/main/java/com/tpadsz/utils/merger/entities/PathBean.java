package com.tpadsz.utils.merger.entities;

import com.tpadsz.utils.merger.IdStringToPathsConverter;

import java.io.Serializable;
import java.nio.file.Path;

/**
 * Created by roger.wang on 2016/1/25.
 */
public class PathBean implements Serializable{
    private static final long serialVersionUID = -3951296044688346206L;


    String key;
    Long size_in_bytes;
    PathBean subPath;


    public PathBean(String key, Long size_in_bytes, PathBean subPath) {
        this.key = key;
        this.size_in_bytes = size_in_bytes;
        this.subPath = subPath;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public Long getSize_in_bytes() {
        return size_in_bytes;
    }

    public void setSize_in_bytes(Long size_in_bytes) {
        this.size_in_bytes = size_in_bytes;
    }

    public PathBean getSubPath() {
        return subPath;
    }

    public void setSubPath(PathBean subPath) {
        this.subPath = subPath;
    }

    public int getPathLength(){
        int count=1;
        PathBean ptr=this;
        while(ptr.subPath!=null){
            count++;
            ptr=ptr.subPath;
        }
        return count;
    }

    public String[] toPathStings(){
        return this.getSubPathStringAtIndex(0);
    }

    public PathBean getSubPathBeanAtIndex(int index){
        if(index<0||index>=this.getPathLength()){
            throw new ArrayIndexOutOfBoundsException(String.format("The length of this pah is %s, but required index %s.",this.getPathLength(),index));
        }


        int i=0;
        PathBean ptr=null;
        while(i<=index){
            ptr=(ptr==null)?this:ptr.getSubPath();
            i++;
        }
        return ptr;
    }

    /**
     * Like String.subString(int start). This method will return a sub-path of this path
     * from the start index.(index itself included)
     * @param index start index
     * @return Sub-path string
     */
    public String[] getSubPathStringAtIndex(int index){
        if(index<0||index>=this.getPathLength()){
            throw new ArrayIndexOutOfBoundsException(String.format("The length of this pah is %s, but required index %s.",this.getPathLength(),index));
        }
        String[] ret=new String[this.getPathLength()-index];

        int i=0;
        PathBean ptr=this;
        while(i<index){
            ptr=ptr.getSubPath();
            i++;
        }

        int j=0;
        while(ptr!=null){
            ret[j]=ptr.getKey();
            j++;
            ptr=ptr.getSubPath();
        }
        return ret;
    }



    public static PathBean newPathBeansChainFromStrings(String[] paths){
        if(paths==null||paths.length==0){
            return null;
        }else{
            PathBean root=new PathBean(paths[0],0L,null);
            PathBean ptr=root;
            for(int i=1;i<paths.length;i++){
                ptr.setSubPath(new PathBean(paths[i],0L,null));
                ptr=ptr.getSubPath();
            }
            return root;
        }
    }


    public static PathBean newPathBeansChainFromIdKey(String _id){
        String[] paths=new IdStringToPathsConverter(_id).getPathStrings();
        return newPathBeansChainFromStrings(paths);
    }

    public int findMaxIndexOfSamePaths(String[] referer){
        return findMaxIndexOfSamePaths(this.toPathStings(), referer);
    }

    private int findMaxIndexOfSamePaths(String[] latestPaths,String[] currentPaths){
        int mostRight=Math.min(latestPaths.length,currentPaths.length);
        int index=-1;
        while((index+1)<mostRight){
            if(!latestPaths[index+1].equals(currentPaths[index+1])){
                return index;
            }else{
                ++index;
            }
        }
        return index;
    }

    /**
     * Substitute this path with a new branch.
     * @param branch
     * @return Returns an array of PathBean. The array is of 2 elements with
     * the first one referring the new root PathBean and the second referring
     * the paths which have been substituted.
     */
    public PathBean[] substitutePaths(PathBean branch, int index){
        PathBean[] ret=new PathBean[2];

        if(index<0||index>=this.getPathLength()){
            throw new ArrayIndexOutOfBoundsException();
        }
        if(index==0){
            ret[0]=branch;
            ret[1]=this;
            return ret;
        }

        int i=0;
        PathBean ptr=this;
        while(i<index){
            ptr=ptr.getSubPath();
            i++;
        }
        PathBean tail=getSubPathBeanAtIndex(index);
        ret[1]=new PathBean(tail.getKey(),tail.getSize_in_bytes(),null);

        ptr.setSubPath(branch);
        ret[0]=this;


        return ret;
    }

    public void addSize_in_bytes(Long size_in_bytes){
        PathBean ptr=this;
        while(ptr!=null){
            ptr.setSize_in_bytes(ptr.getSize_in_bytes()+size_in_bytes);
            ptr=ptr.getSubPath();
        }
    }

}
