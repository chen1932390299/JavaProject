package http.chen.autocases;

import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.Assert;
import org.testng.annotations.Test;
import org.apache.log4j.Logger;
import http.chen.autocases.Httputils;
import java.util.HashMap;
import java.util.Map;
import http.chen.autocases.TransferUtils;

public class TestHttpRequest {
    private static Logger log = Logger.getLogger(TestHttpRequest.class);
    @BeforeClass
    public void beforeClass() {

        log.info(System.getProperty("user.dir")+"http request------");

    }
    @AfterClass
    public void afterClass(){
        log.info("http consume----");

    }

    @Test
    public void testHttp1(){
        String url="http://www.baidu.com";
        Map<Object, Object> parameters=new HashMap<>();
        Map<Object, Object> headers=new HashMap<>();
        String res="";
        res=Httputils.getrequest(url,parameters,headers);
        log.info("test http1 result is: "+res);
        Assert.assertTrue(true);

    }
    @Test
    public void testHttpPost(){
        String url="https://re.csdn.net/csdnbi";
        String result="";
        Map<Object, Object> body=new HashMap<>();
        body.put("headers","{\"component\":\"enterprise\",\"datatype\":\"re\",\"version\":\"v1\"}");
        body.put("body","{\\\"re\\\":\\\"ref=https%3A%2F%2Fpassport.csdn.net%2Faccount%2Fverify&mtp=2&mod=ad_popu_282&con=ad_content_3743%2Cad_order_719&uid=chen498858336&ck=-\\\"}");
        Map<Object, Object> headers=new HashMap<>();
        headers.put("Content-Type","application/json");
        result=Httputils.postJsonrequest(url,body,headers).trim();
        log.info(result.getClass().getName());
        String exp="true";
        Assert.assertEquals(result,exp);
        log.info("test httPost csdn result is :"+result);
    }
}
