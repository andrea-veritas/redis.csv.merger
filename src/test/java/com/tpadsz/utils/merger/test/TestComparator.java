package com.tpadsz.utils.merger.test;


import com.tpadsz.utils.merger.StringArrayComparator;
import org.junit.Test;

/**
 * Created by Roger on 2016/1/21.
 */

public class TestComparator {
    @Test
    public void testFindMax(){
        assert (new StringArrayComparator(new String[]{"1","2","3"}).max()==2);
    }

    public void testFindMin(){
        assert (new StringArrayComparator(new String[]{"e","b","a","f"}).min()==2);
    }
}
