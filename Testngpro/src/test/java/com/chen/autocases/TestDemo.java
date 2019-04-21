package com.chen.autocases;

import org.testng.Assert;
import org.testng.annotations.Test;
import org.apache.log4j.Logger;
public class TestDemo {
    private static Logger log = Logger.getLogger(TestDemo.class);
    @Test
    public void testcase1(){
        log.info("testcase1");
        Assert.assertTrue(false);

    }
    @Test
    public void testcase2(){
        Assert.assertTrue(true);
        log.debug("testcase2");
    }
}