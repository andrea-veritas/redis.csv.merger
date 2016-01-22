package com.tpadsz.utils.merger.entities;

import java.io.Serializable;

/**
 * Created by Roger on 2016/1/21.
 */
public class MongodbJsonBean implements Serializable{
    private static final long serialVersionUID = -3956596044688346206L;



    private String _id;
    private Integer depth;
    private String parent;
    private Long size_in_bytes;
    private Boolean is_leaf;

    public static MongodbJsonBean fromCSV(String line){

        if(line==null ||line.split(",").length!=2){
            return null;
        }


        String _id=line.split(",")[0].replace("\"","");
        Long size_in_bytes=Long.parseLong(line.split(",")[1]);
        Integer depth=0;
        String parent="";
        Boolean is_leaf=Boolean.TRUE;

        if(_id.contains("_")){
            depth=_id.split("_").length - 1;
            parent=line.substring(1,line.lastIndexOf("_"));
        }else if(_id.contains("-")){
            depth=_id.split("-").length - 1;
            parent=line.substring(1,line.lastIndexOf("-"));

            // Replace all "-" splitter to "_"
            _id=_id.replace("-","_");
        }else{
            depth=0;
            parent="";
        }

        return new MongodbJsonBean().set_id(_id).setDepth(depth).setParent(parent).setSize_in_bytes(size_in_bytes).setIs_leaf(is_leaf);


    }

    public MongodbJsonBean() {
    }

    public MongodbJsonBean(String _id, Integer depth, String parent, Long size_in_bytes, Boolean is_leaf) {
        this._id = _id;
        this.depth = depth;
        this.parent = parent;
        this.size_in_bytes = size_in_bytes;
        this.is_leaf = is_leaf;
    }

    public static long getSerialVersionUID() {
        return serialVersionUID;
    }

    public String get_id() {
        return _id;
    }

    public MongodbJsonBean set_id(String _id) {
        this._id = _id;
        return this;
    }

    public Integer getDepth() {
        return depth;
    }

    public MongodbJsonBean setDepth(Integer depth) {
        this.depth = depth;
        return this;
    }

    public String getParent() {
        return parent;
    }

    public MongodbJsonBean setParent(String parent) {
        this.parent = parent;
        return this;
    }

    public Long getSize_in_bytes() {
        return size_in_bytes;
    }

    public MongodbJsonBean setSize_in_bytes(Long size_in_bytes) {
        this.size_in_bytes = size_in_bytes;
        return this;
    }

    public Boolean getIs_leaf() {
        return is_leaf;
    }

    public MongodbJsonBean setIs_leaf(Boolean is_leaf) {
        this.is_leaf = is_leaf;
        return this;
    }
}
