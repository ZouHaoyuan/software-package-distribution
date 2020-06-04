package com.config;

import com.alipay.api.AlipayClient;
import com.alipay.api.DefaultAlipayClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
/**
 * @param
 * @return
 */
@Configuration
public class AlipayConfig {
    // 应用ID,您的APPID，收款账号既是您的APPID对应支付宝账号
    public static String app_id = "2016102200741364";

    // 商户私钥，您的PKCS8格式RSA2私钥
    public static String app_private_key = "MIIEvgIBADANBgkqhkiG9w0BAQEFAASCBKgwggSkAgEAAoIBAQCt/l6RLz/hKgEUSG60k89RNNZcTr5a1M75yFfZMdpdQ01UgMdJbGvGpdB+j4OsQZqZKYHMMmBvuiGCTxMoUw7BP0G8qbLN3FWO+qmfmbu6ybFCZd0ov3//LyGZuyMmuJv5Q+iFlrrsiNxjbPcp4tiCjlij/4oR4oeXqUfKnBTdm/mgG2UXNVqhZt+9z96rFDDG+fcXQ/RSJbVbg3kqwulRVjOtOIlEG3lAJ4YqlY9ePblIzqgGPxxm1qB010ci5xLWK29aNWuqlEFZ09GawtDwDnp04qxmdb/o2Aj57mplNMLYJnC7W5AJQV7HsFz7eA5WXDP6rA4D9c/XWamImYbfAgMBAAECggEAEyJA9ghTJwi+e6OHgRYsG0sLM7V5G3YBmNheSMVBtTXcgCl1QO7KX1sGtZREr6G7RZKZ8oGrtz2px7G4cpvHqJqzOPMUda0AP6vzEVkPmvfpJVscWgnNIpvlPwmkUPLGPEAqht5Pigl26vwlphaLJrhOqSJF5bean6P/ABnv9Xqt8eMfWjukDZyXyU6sL4ynt31pLxd5QVv8PRt8ZswbICkfx6F6Ku4x9hZYYCayJbVbAeDDyvCAbXGmlZ0qV3WhBujZAsg0uBTnPK67bgC52s6aNkg8zJcvkLsxdhy4Z/mKFRSKHFUOGF3/dcIpI6iZd0vK7PqU8cHpu7+peZToKQKBgQDoueTjM7tAfFIgrGTbBCjXlo1OT6Yl0TFsb+fNAsVUWtCCczFU+BA2iHs6tR/uH5bALHg1Xziq+6WVsi1g9xkpAY3zP53nH9EH4Ff3Pa35niSfHzAlHWEs/swfKLlAQwZ1CJV5y7el5tKvhllzw4bpRRgzxA8CXDYiNIauxVsEnQKBgQC/ZNfMwRfFECLenT97xrdfJssxO9Fe8Cl5XiM89yGRQmLiQR+LGI+mvBEWRCddz7zdN+j72Fp5BatyS3bJMwQjAA/UbBrbT0c+rOJBj8OuPR4SbpTmql97cBNv3NokLUPvKSnpTPK4dguoergr06znjHqWuVkq5tbSDp3L7/OaqwKBgQCdQV+EluQkwtzL7zGHPNxMMRLyo6dlTmHTIBrF0OV+Bo92VlgzThvGfZqnJNSalwXKfUIM7nbgMoLFEEzRhKafqsk8YPRVxI3+gH1wN2FjlpSwYOarbZrISLOtKltjLOckFdTLI/jIRZx/toyjSK/U2DduY0CxG5YIru2DEyBZkQKBgQCOz2sqj+Xxl/G80+6bqBIJgMPa77xxL5hyxChdBDXa0oNAOcGUo3YEvo2tZJtPJ4PQOTiyOqR/axibhu2SgWuTxicJpMSBYe1SOeP1cZVcPKRksniOiRAH5f/K1ys6FUB8guWP/Geb9hEK3M7xfNemSq7qKlD7bxVZ1agulNPbUwKBgAcnkhPZqSbRF1xzo/PEJu484kW0CZ2nadbXNIWEqjS/IfU8x3cosX0l76vmDr6cF3hVa8nfnWocla/iP1HTJ7Lkp3z6mw2muVn3psFIjWAFNycvmC9s027nvpQCyxUWWoIb4Rprg1pU56jhJWwqlWwtMTcVUHHkliodRrFVdWkd";

    // 支付宝公钥,查看地址：https://openhome.alipay.com/platform/keyManage.htm 对应APPID下的支付宝公钥。
    public static String alipay_public_key = "MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEArf5ekS8/4SoBFEhutJPPUTTWXE6+WtTO+chX2THaXUNNVIDHSWxrxqXQfo+DrEGamSmBzDJgb7ohgk8TKFMOwT9BvKmyzdxVjvqpn5m7usmxQmXdKL9//y8hmbsjJrib+UPohZa67IjcY2z3KeLYgo5Yo/+KEeKHl6lHypwU3Zv5oBtlFzVaoWbfvc/eqxQwxvn3F0P0UiW1W4N5KsLpUVYzrTiJRBt5QCeGKpWPXj25SM6oBj8cZtagdNdHIucS1itvWjVrqpRBWdPRmsLQ8A56dOKsZnW/6NgI+e5qZTTC2CZwu1uQCUFex7Bc+3gOVlwz+qwOA/XP11mpiJmG3wIDAQAB";

    // 服务器异步通知页面路径  需http://格式的完整路径，不能加?id=123这类自定义参数，必须外网可以正常访问
    public static String notify_url = "http://hsdu3j.natappfree.cc/index.html";

    // 页面跳转同步通知页面路径 需http://格式的完整路径，不能加?id=123这类自定义参数，必须外网可以正常访问
    public static String return_url = "http://hsdu3j.natappfree.cc/index.html";

    // 签名方式
    public static String sign_type = "RSA2";

    // 字符编码格式
    public static String charset = "utf-8";

    // 支付宝网关
    public static String gatewayUrl = "https://openapi.alipaydev.com/gateway.do";


    public final static String format = "json";

    @Bean
    public AlipayClient alipayClient() {
        AlipayClient alipayClient = new DefaultAlipayClient(gatewayUrl, app_id, app_private_key, format, charset, alipay_public_key, sign_type);
        return alipayClient;
    }
}