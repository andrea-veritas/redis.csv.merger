package com.tpadsz.utils.merger.test;

import com.tpadsz.utils.merger.IdStringToPathsConverter;
import com.tpadsz.utils.merger.status.PathObject;
import com.tpadsz.utils.merger.status.PathStatus;
import org.junit.Test;

import java.util.List;

/**
 * Created by roger.wang on 2016/1/25.
 */
public class TestPathStatus {
    @Test
    public void test1(){
        String _id="x_y_z";
        PathStatus status=new PathStatus();
        assert status.enroll(new IdStringToPathsConverter(_id).getPathStrings(), 150L)==null;
        assert status.getStatus().size()==3;
        assert status.getStatus().get(0).getKey().equals("x");
        assert status.getStatus().get(1).getKey().equals("x_y");
        assert status.getStatus().get(2).getKey().equals("x_y_z");

        assert status.getStatus().get(0).getSize_in_bytes().equals(150L);
        assert status.getStatus().get(1).getSize_in_bytes().equals(150L);
        assert status.getStatus().get(2).getSize_in_bytes().equals(150L);

        assert status.enroll(new IdStringToPathsConverter("x_y_z_a").getPathStrings(),12L)==null;

        assert status.getStatus().size()==4;
        assert status.getStatus().get(0).getKey().equals("x");
        assert status.getStatus().get(1).getKey().equals("x_y");
        assert status.getStatus().get(2).getKey().equals("x_y_z");
        assert status.getStatus().get(3).getKey().equals("x_y_z_a");
        assert status.getStatus().get(0).getSize_in_bytes().equals(162L);
        assert status.getStatus().get(1).getSize_in_bytes().equals(162L);
        assert status.getStatus().get(2).getSize_in_bytes().equals(162L);
        assert status.getStatus().get(3).getSize_in_bytes().equals(12L);
    }

    @Test
    public void test2(){
        String _id="x_y_z";
        PathStatus status=new PathStatus();
        assert status.enroll(new IdStringToPathsConverter(_id).getPathStrings(), 150L)==null;
        assert status.getStatus().size()==3;
        assert status.getStatus().get(0).getKey().equals("x");
        assert status.getStatus().get(1).getKey().equals("x_y");
        assert status.getStatus().get(2).getKey().equals("x_y_z");

        assert status.getStatus().get(0).getSize_in_bytes().equals(150L);
        assert status.getStatus().get(1).getSize_in_bytes().equals(150L);
        assert status.getStatus().get(2).getSize_in_bytes().equals(150L);

        assert status.enroll(new IdStringToPathsConverter("x_y_z_a_b").getPathStrings(),12L)==null;

        assert status.getStatus().size()==5;
        assert status.getStatus().get(0).getKey().equals("x");
        assert status.getStatus().get(1).getKey().equals("x_y");
        assert status.getStatus().get(2).getKey().equals("x_y_z");
        assert status.getStatus().get(3).getKey().equals("x_y_z_a");
        assert status.getStatus().get(4).getKey().equals("x_y_z_a_b");

        assert status.getStatus().get(0).getSize_in_bytes().equals(162L);
        assert status.getStatus().get(1).getSize_in_bytes().equals(162L);
        assert status.getStatus().get(2).getSize_in_bytes().equals(162L);
        assert status.getStatus().get(3).getSize_in_bytes().equals(12L);
        assert status.getStatus().get(4).getSize_in_bytes().equals(12L);
    }


    @Test
    public void test3() {
        String _id = "x_y_z";
        PathStatus status = new PathStatus();
        assert status.enroll(new IdStringToPathsConverter(_id).getPathStrings(), 150L) == null;
        assert status.getStatus().size() == 3;
        assert status.getStatus().get(0).getKey().equals("x");
        assert status.getStatus().get(1).getKey().equals("x_y");
        assert status.getStatus().get(2).getKey().equals("x_y_z");

        assert status.getStatus().get(0).getSize_in_bytes().equals(150L);
        assert status.getStatus().get(1).getSize_in_bytes().equals(150L);
        assert status.getStatus().get(2).getSize_in_bytes().equals(150L);

        List<PathObject> ret = status.enroll(new IdStringToPathsConverter("x_a_b").getPathStrings(), 12L);

        assert ret.size()==2;
        assert ret.get(0).getKey().equals("x_y");
        assert ret.get(1).getKey().equals("x_y_z");
        assert ret.get(0).getSize_in_bytes()==150L;
        assert ret.get(1).getSize_in_bytes()==150L;

        assert status.getStatus().size()==3;

        assert status.getStatus().get(0).getKey().equals("x");
        assert status.getStatus().get(1).getKey().equals("x_a");
        assert status.getStatus().get(2).getKey().equals("x_a_b");

        assert status.getStatus().get(0).getSize_in_bytes().equals(162L);
        assert status.getStatus().get(1).getSize_in_bytes().equals(12L);
        assert status.getStatus().get(2).getSize_in_bytes().equals(12L);
    }

    @Test
    public void test4() {
        String _id = "x_y_z";
        PathStatus status = new PathStatus();
        assert status.enroll(new IdStringToPathsConverter(_id).getPathStrings(), 150L) == null;
        assert status.getStatus().size() == 3;
        assert status.getStatus().get(0).getKey().equals("x");
        assert status.getStatus().get(1).getKey().equals("x_y");
        assert status.getStatus().get(2).getKey().equals("x_y_z");

        assert status.getStatus().get(0).getSize_in_bytes().equals(150L);
        assert status.getStatus().get(1).getSize_in_bytes().equals(150L);
        assert status.getStatus().get(2).getSize_in_bytes().equals(150L);

        List<PathObject> ret = status.enroll(new IdStringToPathsConverter("a_b_c").getPathStrings(), 12L);

        assert ret.size() == 3;
        assert ret.get(0).getKey().equals("x");
        assert ret.get(1).getKey().equals("x_y");
        assert ret.get(2).getKey().equals("x_y_z");

        assert ret.get(0).getSize_in_bytes() == 150L;
        assert ret.get(1).getSize_in_bytes() == 150L;
        assert ret.get(2).getSize_in_bytes() == 150L;

        assert status.getStatus().size()==3;

        assert status.getStatus().get(0).getKey().equals("a");
        assert status.getStatus().get(1).getKey().equals("a_b");
        assert status.getStatus().get(2).getKey().equals("a_b_c");

        assert status.getStatus().get(0).getSize_in_bytes()==12L;
        assert status.getStatus().get(1).getSize_in_bytes()==12L;
        assert status.getStatus().get(2).getSize_in_bytes()==12L;


    }


}
