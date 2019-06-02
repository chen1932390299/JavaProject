package http.chen.autocases;
import java.util.*;
import java.net.URI;
import org.apache.http.Consts;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.*;
import org.apache.http.entity.StringEntity;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import java.io.IOException;
import net.sf.json.JSONObject;
import org.apache.log4j.Logger;

import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.ssl.SSLContexts;
import org.apache.http.ssl.TrustStrategy;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import javax.net.ssl.SSLContext;
import org.apache.http.conn.ssl.NoopHostnameVerifier;
import java.io.*;
import java.io.File;
import org.apache.http.entity.mime.MultipartEntityBuilder;

public class Httputils {
    static HttpPost post;
    static HttpResponse response;
    static HttpGet get;
    static HttpEntity entity;
	private static Logger logger = Logger.getLogger(Httputils.class);


    //ssl安全跳过
    public static CloseableHttpClient getignoreSSLClient() {
        CloseableHttpClient client =null;
        try {
            SSLContext sslContext=SSLContexts.custom().loadTrustMaterial(null, new TrustStrategy() {

                @Override
                public boolean isTrusted(X509Certificate[] x509Certificates, String s) throws CertificateException {
                    return true;
                }
            }).build();
            client=HttpClients.custom().setSSLContext(sslContext).setSSLHostnameVerifier(new NoopHostnameVerifier()).build();

        }catch(Exception e) {
            e.printStackTrace();
        }
        return client;
    }

    //
    public static String getrequest(String url,Map<Object, Object> parameters,Map<Object,Object> headers){
        String basicUrl=url;
        String result=null;
        String urlencoded=null;
//		CloseableHttpClient httpclient=HttpClients.createDefault();
        CloseableHttpClient httpclient=getignoreSSLClient();
        List<NameValuePair> formData=new ArrayList<NameValuePair>();

        try {
            //参数分离
            if(!url.contains("=")) {
                if(parameters !=null && parameters.size()>0) {
                    for(Map.Entry<Object,Object> entry:parameters.entrySet()) {
                        String k =entry.getKey().toString();
                        String v=entry.getValue().toString();
                        formData.add(new BasicNameValuePair(k, v));
                    }
                }
                urlencoded =EntityUtils.toString(new UrlEncodedFormEntity(formData, Consts.UTF_8));
                if(basicUrl.contains("?")) {

                    get=new HttpGet(basicUrl+urlencoded);
                    if(headers !=null && headers.size()>0) {
                        for(Map.Entry<Object, Object> entry:headers.entrySet()) {
                            get.setHeader(entry.getKey().toString(),entry.getValue().toString());
                        }
                    }else {
                        get.setHeader(null);
                    }
                    response=httpclient.execute(get);
                    entity=response.getEntity();
                    result=EntityUtils.toString(entity,"UTF-8");
                    return result;
                }else {
                    //无？
                    get=new HttpGet(basicUrl+"?"+urlencoded);
                    if(headers !=null && headers.size()>0) {
                        for(Map.Entry<Object, Object> entry:headers.entrySet()) {
                            get.setHeader(entry.getKey().toString(),entry.getValue().toString());
                        }
                    }else {
                        get.setHeader(null);
                    }
                    response=httpclient.execute(get);
                    entity=response.getEntity();
                    result=EntityUtils.toString(entity,"UTF-8");
                    return result;
                }
            }else {

                //纯url
                get=new HttpGet(basicUrl);
                response=httpclient.execute(get);
                entity=response.getEntity();
                result=EntityUtils.toString(entity,"UTF-8");
                return result;
            }
        }catch (IOException e) {
            e.printStackTrace();
        }finally {
            try {
                if(httpclient!=null) {
                    httpclient.close();
                }
            }catch(IOException e) {
                e.printStackTrace();
            }
        }
        return result;
    }

    //postformData
    public static String postformData(String url,Map<Object, Object> body,Map<Object, Object> headers){
        String basicUrl=url;
        String result=null;
        // formdata格式可以直接传输采纳数到请求地址后
        CloseableHttpClient httpclient=getignoreSSLClient();
        List<NameValuePair> formData=new ArrayList<NameValuePair>();
        try {
            post =new HttpPost();
            post.setURI(new URI(basicUrl));
            //上面可以直接用post = new HttpPost(url)代替
            for (Iterator iter = body.keySet().iterator(); iter.hasNext();) {
                String k = (String) iter.next();
                String v = String.valueOf(body.get(k));
                formData.add(new BasicNameValuePair(k, v));
                logger.info("构造参数完毕");
            }
            if(headers !=null && headers.size()>0) {
                for(Map.Entry<Object, Object> entry:headers.entrySet()) {
                    post.setHeader(entry.getKey().toString(),entry.getValue().toString());
                }
            }else {
                post.setHeader(null);
                //header设置完毕
            }
            post.setEntity(new UrlEncodedFormEntity(formData,Consts.UTF_8));

            response = httpclient.execute(post);
            entity  =response.getEntity();
            result=EntityUtils.toString(entity, "UTF-8");
            int errorCode= response.getStatusLine().getStatusCode();
            if(String.valueOf(errorCode)!=null) {
                return result;
            }else {
                result=null;
                return result;
            }

        }catch(Exception e ) {
            e.printStackTrace();
        }finally {
            try {
                if(httpclient!=null) {
                    httpclient.close();
                }
            }catch(Exception e) {
                e.printStackTrace();
            }
        }
        return result;
    }

    // postJson
    public static String postJsonrequest(String url , Map<Object, Object> body , Map<Object, Object> headers){

        String basicUrl=url;
        String result=null;
        post =new HttpPost(basicUrl);
        // 设置请求头格式
        post.setHeader("content-type", "application/json");
        CloseableHttpClient httpclient=getignoreSSLClient();
        try {
            if(headers !=null && headers.size()>0) {
                for(Map.Entry<Object, Object> entry:headers.entrySet()) {
                    post.setHeader(entry.getKey().toString(),entry.getValue().toString());
                }
            }else {
                post.setHeader(null);
                //header设置完毕
            }
            //body转string,处理entity传入httpEntity
            StringEntity newEntity=new StringEntity(body.toString(),"utf-8");

            post.setEntity(newEntity);;
            response=httpclient.execute(post);
            int errorCode =response.getStatusLine().getStatusCode();
            if(String.valueOf(errorCode) !=null) {
                entity =response.getEntity();
                result=EntityUtils.toString(entity, "UTF-8");
                return result;
            }
        }catch (Exception e) {
            e.printStackTrace();
        }finally {
            try {
                if(httpclient!=null) {
                    httpclient.close();
                }

            }catch (Exception e) {
                e.printStackTrace();
            }
        }
        return result;
    }

    // download file ,两个都要带路径
    public static void downloadfile(String url,String localfileName,String remotefileName) {
        FileOutputStream output = null;
        InputStream in = null;
        CloseableHttpClient httpclient=getignoreSSLClient();
        try {
            get=new HttpGet(url);
            get.addHeader("fileName",remotefileName );
            response=httpclient.execute(get);
            entity =response.getEntity();
            in = entity.getContent();
            long length = entity.getContentLength();
            if (length <= 0) {
                return;
            }
            File localfile = new File(localfileName);

            if(! localfile.exists()) {
                localfile.createNewFile();
            }
            output=new FileOutputStream(localfile);
            byte[] buffer = new byte[4096];
            int readLength = 0;
            while ((readLength=in.read(buffer)) > 0) {
                byte[] bytes = new byte[readLength];
                System.arraycopy(buffer, 0, bytes, 0, readLength);
                output.write(bytes);
            }
            output.flush();
        }catch(Exception e) {
            e.printStackTrace();
        }finally {
            try {
                if(in !=null) {
                    in.close();
                }
                if(output !=null) {
                    output.close();
                }
            }catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    // uploadfile
    public static void uploadfile(String localfilepath,String url) {


        CloseableHttpClient httpclient=getignoreSSLClient();
        try {
            post = new HttpPost(url);
            FileBody bin=new FileBody(new File(localfilepath));
            HttpEntity reentity= MultipartEntityBuilder.create().addPart("bin",bin).build();
            post.setEntity(reentity);
            response=httpclient.execute(post);
            entity=response.getEntity();
            if(entity!=null) {
                // length entity.getContentLength();
                String content=EntityUtils.toString(entity,"utf-8");
                EntityUtils.consume(entity);
            }

        }catch(Exception e) {
            e.printStackTrace();
        }finally {
            try {
                if(httpclient!=null ) {
                    httpclient.close();
                }
            }catch(Exception e) {
                e.printStackTrace();
            }
        }

    }
}
