package com.tpadsz.utils.merger.test;

import com.alibaba.fastjson.JSON;
import com.tpadsz.utils.merger.KeyToTreeAnalyzer;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.*;

/**
 * Created by Roger on 2016/1/22.
 */
public class TestKeyAnalyzer {
    ByteArrayOutputStream out=new ByteArrayOutputStream();


    @Before
    public void setUp() throws UnsupportedEncodingException {
        System.setIn(new ByteArrayInputStream("\"abcd\",125".getBytes("UTF-8")));
        System.setOut(new PrintStream(out));
    }

    @Test
    public void testConvert() throws UnsupportedEncodingException {
        new KeyToTreeAnalyzer(new String[]{}).run();
        String json=new String(out.toByteArray(),"UTF-8");
        assert JSON.parse(json)!=null;
    }

    @After
    public void tearDown() throws IOException {
        if(out!=null){
            out.close();
        }
    }

}
