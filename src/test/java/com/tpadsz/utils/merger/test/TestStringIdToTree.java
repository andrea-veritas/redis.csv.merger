package com.tpadsz.utils.merger.test;

import com.tpadsz.utils.merger.IdStringToPathsConverter;
import org.junit.Test;

/**
 * Created by roger.wang on 2016/1/22.
 */
public class TestStringIdToTree {
    @Test
    public void testConvertSingleNode(){
        String _id="uic";
        String path[]=new IdStringToPathsConverter(_id).getPathStrings();
        assert path!=null;
        assert path.length==1;
        assert path[0].equals("uic");

    }

    @Test
    public void testConvertDualNodes(){
        String _id="uic_cic";
        String path[]=new IdStringToPathsConverter(_id).getPathStrings();
        assert path!=null;

        for(String str:path){
            System.out.println(str);
        }

        assert path.length==2;
        assert path[0].equals("uic");
        assert path[1].equals("uic_cic");


    }
    @Test
    public void testConvertTripleNodes(){
        String _id="uic_cic_ctc";
        String path[]=new IdStringToPathsConverter(_id).getPathStrings();
        assert path!=null;

        for(String str:path){
            System.out.println(str);
        }

        assert path.length==3;
        assert path[0].equals("uic");
        assert path[1].equals("uic_cic");
        assert path[2].equals("uic_cic_ctc");

    }

    @Test
    public void testConvertMultipleNodes(){
        String _id="uic_cic_ctc_a_b_c_d";
        String path[]=new IdStringToPathsConverter(_id).getPathStrings();
        assert path!=null;

        for(String str:path){
            System.out.println(str);
        }

        assert path.length==7;
        assert path[0].equals("uic");
        assert path[1].equals("uic_cic");
        assert path[2].equals("uic_cic_ctc");
        assert path[3].equals("uic_cic_ctc_a");
        assert path[4].equals("uic_cic_ctc_a_b");
        assert path[5].equals("uic_cic_ctc_a_b_c");
        assert path[6].equals("uic_cic_ctc_a_b_c_d");


    }

}
