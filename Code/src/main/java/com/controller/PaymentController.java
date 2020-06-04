package com.controller;

import com.alibaba.fastjson.JSON;
import com.alipay.api.AlipayApiException;
import com.alipay.api.AlipayClient;
import com.alipay.api.DefaultAlipayClient;
import com.alipay.api.internal.util.file.FileUtils;
import com.alipay.api.internal.util.file.IOUtils;
import com.alipay.api.request.AlipayTradePagePayRequest;
import com.alipay.api.request.AlipayTradeWapPayRequest;
import com.config.AlipayConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.*;
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.util.UUID;

@Controller
public class PaymentController {

    //应用id
    String APP_ID = "2016102200741364";
    //应用私钥
    String APP_PRIVATE_KEY = "MIIEvgIBADANBgkqhkiG9w0BAQEFAASCBKgwggSkAgEAAoIBAQCt/l6RLz/hKgEUSG60k89RNNZcTr5a1M75yFfZMdpdQ01UgMdJbGvGpdB+j4OsQZqZKYHMMmBvuiGCTxMoUw7BP0G8qbLN3FWO+qmfmbu6ybFCZd0ov3//LyGZuyMmuJv5Q+iFlrrsiNxjbPcp4tiCjlij/4oR4oeXqUfKnBTdm/mgG2UXNVqhZt+9z96rFDDG+fcXQ/RSJbVbg3kqwulRVjOtOIlEG3lAJ4YqlY9ePblIzqgGPxxm1qB010ci5xLWK29aNWuqlEFZ09GawtDwDnp04qxmdb/o2Aj57mplNMLYJnC7W5AJQV7HsFz7eA5WXDP6rA4D9c/XWamImYbfAgMBAAECggEAEyJA9ghTJwi+e6OHgRYsG0sLM7V5G3YBmNheSMVBtTXcgCl1QO7KX1sGtZREr6G7RZKZ8oGrtz2px7G4cpvHqJqzOPMUda0AP6vzEVkPmvfpJVscWgnNIpvlPwmkUPLGPEAqht5Pigl26vwlphaLJrhOqSJF5bean6P/ABnv9Xqt8eMfWjukDZyXyU6sL4ynt31pLxd5QVv8PRt8ZswbICkfx6F6Ku4x9hZYYCayJbVbAeDDyvCAbXGmlZ0qV3WhBujZAsg0uBTnPK67bgC52s6aNkg8zJcvkLsxdhy4Z/mKFRSKHFUOGF3/dcIpI6iZd0vK7PqU8cHpu7+peZToKQKBgQDoueTjM7tAfFIgrGTbBCjXlo1OT6Yl0TFsb+fNAsVUWtCCczFU+BA2iHs6tR/uH5bALHg1Xziq+6WVsi1g9xkpAY3zP53nH9EH4Ff3Pa35niSfHzAlHWEs/swfKLlAQwZ1CJV5y7el5tKvhllzw4bpRRgzxA8CXDYiNIauxVsEnQKBgQC/ZNfMwRfFECLenT97xrdfJssxO9Fe8Cl5XiM89yGRQmLiQR+LGI+mvBEWRCddz7zdN+j72Fp5BatyS3bJMwQjAA/UbBrbT0c+rOJBj8OuPR4SbpTmql97cBNv3NokLUPvKSnpTPK4dguoergr06znjHqWuVkq5tbSDp3L7/OaqwKBgQCdQV+EluQkwtzL7zGHPNxMMRLyo6dlTmHTIBrF0OV+Bo92VlgzThvGfZqnJNSalwXKfUIM7nbgMoLFEEzRhKafqsk8YPRVxI3+gH1wN2FjlpSwYOarbZrISLOtKltjLOckFdTLI/jIRZx/toyjSK/U2DduY0CxG5YIru2DEyBZkQKBgQCOz2sqj+Xxl/G80+6bqBIJgMPa77xxL5hyxChdBDXa0oNAOcGUo3YEvo2tZJtPJ4PQOTiyOqR/axibhu2SgWuTxicJpMSBYe1SOeP1cZVcPKRksniOiRAH5f/K1ys6FUB8guWP/Geb9hEK3M7xfNemSq7qKlD7bxVZ1agulNPbUwKBgAcnkhPZqSbRF1xzo/PEJu484kW0CZ2nadbXNIWEqjS/IfU8x3cosX0l76vmDr6cF3hVa8nfnWocla/iP1HTJ7Lkp3z6mw2muVn3psFIjWAFNycvmC9s027nvpQCyxUWWoIb4Rprg1pU56jhJWwqlWwtMTcVUHHkliodRrFVdWkd";
    //支付宝公钥
    String ALIPAY_PUBLIC_KEY = "MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAml8nzZNHL3eGVypCRUn82Nuqh8h61dMJHNTutjiuNGtMt8fuRqIANFZ6y7GaKmDEHesVV4P4d4ETHm4PL+OCGZL1KxpNJjPQp3m5oD2Yv2dUt99DKewyODhCgQIZ5jYODo/guYEUXX1ZNo3GCgWkHu6qt8X6TX//rpErrdRerJvfcVoHOWN9BuaOYYP792i5sZNkUCxO5ntTHc+P+yjU4Cu0baMOuLXKRu9pQwBV30pt5C8pTP8hHn1TrByGxtMT4sKwoxKdQMCYyH8edARSjeUOXvBQQocRw7lEVJO9TSDs5paOMgA9uzZfA12sZRz46rgLe+MWEcGPrIQ/obWfuwIDAQAB";
    String CHARSET = "utf-8";
    //支付宝接口的网关地址，正式"https://openapi.alipay.com/gateway.do"
    String serverUrl = "https://openapi.alipaydev.com/gateway.do";
    //签名算法类型
    String sign_type = "RSA2";

    @RequestMapping("alipay/submit")
    public void alipaytest(HttpServletRequest httpRequest,
                           HttpServletResponse httpResponse) throws ServletException, IOException {
        //构造sdk的客户端对象


        String uuid = UUID.randomUUID().toString();
       /* AlipayClient alipayClient = new DefaultAlipayClient(serverUrl, APP_ID, APP_PRIVATE_KEY, "json", CHARSET, ALIPAY_PUBLIC_KEY, sign_type); //获得初始化的AlipayClient
        AlipayTradePagePayRequest alipayRequest = new AlipayTradePagePayRequest();//创建API对应的request
        alipayRequest.setReturnUrl("http://6fbyta.natappfree.cc");
//        alipayRequest.setNotifyUrl("http://hsdu3j.natappfree.cc/return_url");//在公共参数中设置回跳和通知地址
        alipayRequest.setBizContent("{" +
                " \"out_trade_no\":\" "+uuid+"\"," +
                " \"total_amount\":\"8.8\"," +
                " \"subject\":\"棒棒贝贝App下载\"," +
                " \"product_code\":\"FAST_INSTANT_TRADE_PAY\"" +
                " }");//填充业务参数
        String form="";
        try {
            //请求支付宝下单接口,发起http请求
            form = alipayClient.pageExecute(alipayRequest).getBody(); //调用SDK生成表单
        } catch (AlipayApiException e) {
            e.printStackTrace();
        }
        System.out.println(form);
        httpResponse.setContentType("text/html;charset=" + CHARSET);
        httpResponse.getWriter().write(form);//直接将完整的表单html输出到页面
        httpResponse.getWriter().flush();
        httpResponse.getWriter().close();*/


        AlipayClient alipayClient =  new  DefaultAlipayClient( serverUrl , APP_ID, APP_PRIVATE_KEY, "json", CHARSET, ALIPAY_PUBLIC_KEY, sign_type);  //获得初始化的AlipayClient
        AlipayTradePagePayRequest alipayRequest =  new  AlipayTradePagePayRequest(); //创建API对应的request
        alipayRequest.setReturnUrl( "http://127.0.0.1:8080/finish.html" );
        alipayRequest.setNotifyUrl( "http://6fbyta.natappfree.cc" ); //在公共参数中设置回跳和通知地址
        alipayRequest.setBizContent( "{"  +
                "    \"out_trade_no\":\""+uuid+"\","  +
                "    \"product_code\":\"FAST_INSTANT_TRADE_PAY\","  +
                "    \"total_amount\":88.88,"  +
                "    \"subject\":\"Iphone6 16G\","  +
                "    \"body\":\"Iphone6 16G\","  +
                "    \"sys_service_provider_id\":\"2088511833207846\""  +
                "    }" +
                "  }" ); //填充业务参数
        String form= "" ;
        try  {
            form = alipayClient.pageExecute(alipayRequest).getBody();  //调用SDK生成表单
        }  catch  (AlipayApiException e) {
            e.printStackTrace();
        }
        httpResponse.setContentType( "text/html;charset="  + CHARSET);
        httpResponse.getWriter().write(form); //直接将完整的表单html输出到页面
        httpResponse.getWriter().flush();
        httpResponse.getWriter().close();
    }

    @RequestMapping("return_url")
    public String Return_url(HttpServletRequest request, ModelMap modelMap, HttpServletResponse response, HttpSession httpSession) throws InterruptedException, UnsupportedEncodingException {
        response.setContentType("text/html;charset=utf-8");
        try {
            request.setCharacterEncoding("UTF-8");
        } catch (UnsupportedEncodingException e1) {
            // TODO Auto-generated catch block
            e1.printStackTrace();
        }
        String filePath = httpSession.getServletContext().getRealPath("/") + "file/git.exe";
        System.out.println(filePath);
        String fileName="qq.exe";
        java.io.BufferedInputStream bis = null;
        java.io.BufferedOutputStream bos = null;

        String downLoadPath = filePath;  //注意不同系统的分隔符
        //  String downLoadPath =filePath.replaceAll("/", "\\\\\\\\");   //replace replaceAll区别 *****
        System.out.println(downLoadPath);

        try {
            long fileLength = new File(downLoadPath).length();
            response.setContentType("application/x-msdownload;");
            response.setHeader("Content-disposition", "attachment; filename=" + new String(fileName.getBytes("utf-8"), "ISO8859-1"));
            response.setHeader("Content-Length", String.valueOf(fileLength));
            bis = new BufferedInputStream(new FileInputStream(downLoadPath));
            bos = new BufferedOutputStream(response.getOutputStream());
            byte[] buff = new byte[2048];
            int bytesRead;
            while (-1 != (bytesRead = bis.read(buff, 0, buff.length))) {
                bos.write(buff, 0, bytesRead);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (bis != null)
                try {
                    bis.close();
                } catch (IOException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            if (bos != null)
                try {
                    bos.close();
                } catch (IOException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
        }

        return "redirect:/index.html";
    }

}
