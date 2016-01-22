package com.tpadsz.utils.merger;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Arrays;

/**
 * Created by roger.wang on 2016/1/22.
 */
public class IdStringToTreeConverter {
    private String _id;
    private String treePath[];
    public IdStringToTreeConverter(String _id){
        this._id=_id;
    }

    private String[] exec(){
        if(this._id.contains("_")){
            String[] nodes=this._id.split("_");
            byte[][] backingBytesForStringPaths=new byte[nodes.length][];
            this.treePath=new String[nodes.length];
            int stringByteArrayLengthCounter=0;
            for(int i=0;i<nodes.length;i++){
                stringByteArrayLengthCounter+=nodes[i].length();
                ByteArrayOutputStream baos=new ByteArrayOutputStream(stringByteArrayLengthCounter+i*("_".getBytes().length));
                for(int j=0;j<i+1;j++){
                    try {
                        baos.write(nodes[j].getBytes("UTF-8"));
                        if(j>=0&&j<i){
                            baos.write("_".getBytes());
                        }
                    } catch (IOException e) {
                        baos.write(0);
                    }
                }
                try {
                    baos.flush();
                    backingBytesForStringPaths[i]=baos.toByteArray();
                    baos.close();
                } catch (IOException e) {
                    backingBytesForStringPaths[i]=new byte[0];
                }



            }
            for(int j=0;j<nodes.length;j++){
                this.treePath[j]=new String(backingBytesForStringPaths[j]);
            }
        }else{
            this.treePath=new String[]{this._id};
        }
        return this.treePath;
    }

    public String[] getTreePath(){
        return this.treePath==null?exec():this.treePath;
    }
}
