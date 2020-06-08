#软件分发
##官网发布
         

软件分发之网站发布

网站分发难点：支付接口调用

1、支付宝接口调用(以网站支付为例)

支付宝为普通商户提供如下支付产品：

![img](./img/image002.jpg)

电脑网站支付：

PC网站轻松收款，资金马上到账：  用户在商家PC网站消费，自动跳转支付宝PC网站收银台完成付款。  交易资金直接打入商家支付宝账户，实时到账。

![img](./img/image004.jpg)

接入手机网站支付需要具备如下条件：

申请前必须拥有经过实名认证的支付宝账户；

企业或个体工商户可申请；

需提供真实有效的营业执照，且支付宝账户名称需与营业执照主体一致；

网站能正常访问且页面显示完整，网站需要明确经营内容且有完整的商品信息；

如为个体工商户，则团购不开放，且古玩、珠宝等奢侈品、投资类行业无法申请本产品。并且需支付一定手续费

![img](./img/image006.jpg)

申请步骤：

支付宝商家中心中申请： [https://www.alipay.com/](https://www.alipay.com/)

![img](./img/image008.png)

一个工作日后登录到蚂蚁金服开发者中心中：

![img](./img/image010.png)

可以查看到一个已经签约上线的应用。  其中非常重要的是这个APPID，需要记录下来之后的程序中要用到这个参数。

点击  查看

![img](./img/image012.png)

到此为止，电商网站可以访问支付宝的最基本的准备已经完成。

配置密钥

为了保证交易双方（商户和支付宝）的身份和数据安全，开发者在调用接口前，需要配置双方密钥，对交易数据进行双方校验。密钥包含应用私钥（APP\_PRIVATE\_KEY）和应用公钥（APP\_PUBLIC\_KEY）。生成密钥后，开发者需要在开放平台开发者中心进行密钥配置，配置完成后可以获取支付宝公钥（ALIPAY\_PUBLIC\_KEY）。

1）密钥作是与支付宝接口对接的必要参数。

首先下载密钥生成工具：AlipayDevelopmentAssistant-1.0.2.exe，执行安装，注意不要安装在有空格的目录中。  [https://ideservice.alipay.com/ide/getPluginUrl.htm?clientType=assistant&platform=win&channelType=WEB](https://ideservice.alipay.com/ide/getPluginUrl.htm?clientType=assistant&platform=win&channelType=WEB)

安装成功，打开“支付宝开放平台开发助手”，使用工具生成密钥（应用私钥和应用公钥），如下图，点击“生成密钥”：

![img](./img/image014.jpg)

参考：[https://opendocs.alipay.com/open/291/105972](https://opendocs.alipay.com/open/291/105972)

接口调用过程

1. 商户系统请求支付宝接口  [alipay.trade.page.pay](https://opendocs.alipay.com/apis/api_1/alipay.trade.page.pay)，支付宝对商户请求参数进行校验，而后重新定向至用户登录页面。

2. 用户确认支付后，支付宝通过 get 请求 returnUrl（商户入参传入），返回同步返回参数。

3. 交易成功后，支付宝通过 post 请求 notifyUrl（商户入参传入），返回异步通知参数。

4. 若由于网络等问题异步通知没有到达，商户可自行调用交易查询接口[alipay.trade.query](https://opendocs.alipay.com/apis/api_1/alipay.trade.page.pay)  进行查询，根据查询接口获取交易以及支付信息（商户也可以直接调用查询接口，不需要依赖异步通知）。

![img](./img/image016.jpg)

网站支付接入详细参见：https://docs.open.alipay.com/203/105285/

下单接口定义

接口定义：外部商户请求支付宝创建订单并支付

公共参数

请求地址：[https://openapi.alipaydev.com/gateway.do](https://openapi.alipaydev.com/gateway.do)

公共请求参数：

详细参数参见：https://docs.open.alipay.com/203/107090/， https://docs.open.alipay.com/api_1/alipay.trade.wap.pay 标记蓝色的由支付宝sdk（开发工具包）设置、标记红色的已在支付渠道参数中配置，标记绿色的需程序来设置。

![img](./img/image018.jpg)

![img](./img/image020.jpg)

业务参数如下：

![img](./img/image022.jpg)

![img](./img/image024.jpg)

以上都是前置知识与准备工作

下面以实际项目为例，用户支付完成后可进行下载，实现效果如下。

![img](./img/image026.jpg)![img](./img/image028.jpg)

![img](./img/image029.jpg)

![img](./img/image031.jpg)

支付完成后实现app下载。

在交易服务中引入支付宝的sdk作为测试战场，使用支付宝提供sdk（开发工具包）调用支付宝的接口。 sdk是一个方便调用支付宝接口的开发工具包，提供方便接口调用的api方法。

在交易服务pom.xml中添加如下坐标：

<!‐‐支付宝SDK ‐‐>

<dependency>         

<groupId>com.alipay.sdk</groupId>         

<artifactId>alipay‐sdk‐java</artifactId>     

</dependency>

<!‐‐支付宝SDK依赖的日志 ‐‐>

<dependency>         

<groupId>commons‐logging</groupId>         

 <artifactId>commons‐logging</artifactId>

 <version>1.2</version>

 </dependency>

、跳转支付宝
======

![img](./img/image033.png)

1 分析
----

功能要求：

1、制作支付宝需要的各种参数

2、保存支付信息，作用：追踪交易状态、去重、对账

3、帮助用户跳转到支付宝的页面

 分析支付宝需要什么参数？

 查看蚂蚁金服的文档中心中的电脑网站支付说明

![img](./img/image035.png)

这些参数可以一次性注入到阿里提供alipayClient中，以后就不用再赋值了。

业务参数

![img](./img/image037.png)

![img](./img/image039.png)

2 支付信息的保存
---------

表结构 payment_info

![img](./img/image041.png)

id

主键自动生成

out\_trade\_no

订单中已生成的对外交易编号。订单中获取

alipay\_trade\_no

订单编号 初始为空，支付宝回调时生成

total_amount

订单金额。订单中获取

subject

交易内容。利用商品名称拼接。

payment_status

支付状态，默认值未支付。

create_time

创建时间，当前时间

callback_time

回调时间，初始为空，支付宝异步回调时记录

callback_content

回调信息，初始为空，支付宝异步回调时记录

3  类AlipayConfig整合到项目
---------------------

import com.alipay.api.AlipayClient;

import com.alipay.api.DefaultAlipayClient;

import org.springframework.beans.factory.annotation.Value;

import org.springframework.context.annotation.Bean;

import org.springframework.context.annotation.Configuration;

import org.springframework.context.annotation.PropertySource;

/**

\* @param

\* @return

*/

@Configuration

@PropertySource("classpath:alipay.properties")

public class AlipayConfig {

 @Value("${alipay_url}")

 private String alipay_url;

 @Value("${app\_private\_key}")

 private String app\_private\_key;

 @Value("${app_id}")

 private String app_id;

 public final static String format="json";

 public final static String charset="utf-8";

 public final static String sign_type="RSA2";

 public static String return\_payment\_url;

 public static String notify\_payment\_url;

 public static String return\_order\_url;

 public static String alipay\_public\_key;

 @Value("${alipay\_public\_key}")

 public void setAlipay\_public\_key(String alipay\_public\_key) {

 AlipayConfig.alipay\_public\_key = alipay\_public\_key;

 }

 @Value("${return\_payment\_url}")

 public void setReturn_url(String return\_payment\_url) {

 AlipayConfig.return\_payment\_url = return\_payment\_url;

 }

 @Value("${notify\_payment\_url}")

 public void setNotify_url(String notify\_payment\_url) {

 AlipayConfig.notify\_payment\_url = notify\_payment\_url;

 }

 @Value("${return\_order\_url}")

 public void setReturn\_order\_url(String return\_order\_url) {

 AlipayConfig.return\_order\_url = return\_order\_url;

 }

 @Bean

 public AlipayClient alipayClient(){

 AlipayClient alipayClient=new DefaultAlipayClient(alipay\_url,app\_id,app\_private\_key,format,charset, alipay\_public\_key,sign_type );

 return alipayClient;

 }

4 PaymentServiceImpl
--------------------

@Override  
public void savePaymentInfo(PaymentInfo paymentInfo) { //必须保证每个订单只有唯一的支付信息，所以如果之前已经有了该笔订单的支付信息，那么只更新时间 PaymentInfo paymentInfoQuery=new PaymentInfo();  
 paymentInfoQuery.setOrderId(paymentInfo.getOrderId());  
  
 PaymentInfo paymentInfoExists = paymentInfoMapper.selectOne(paymentInfoQuery); if(paymentInfoExists!=null){  
 paymentInfoExists.setCreateTime(new Date()); paymentInfoMapper.updateByPrimaryKey(paymentInfoExists); return;  
 }  
  
 paymentInfo.setCreateTime(new Date()); paymentInfoMapper.insertSelective(paymentInfo);  
}

PaymentInfoMapper

public interface PaymentInfoMapper extends Mapper<PaymentInfo>{  
}

OrderInfo

//生成摘要

public String getOrderSubject(){  
 String body=""; if(orderDetailList!=null&&orderDetailList.size()>0){  
 body= orderDetailList.get(0).getSkuName();  
 }  
 body+="等"+getTotalSkuNum()+"件商品"; return body;  
  
}  
  
public Integer getTotalSkuNum(){  
 Integer totalNum=0; for (OrderDetail orderDetail : orderDetailList) {  
 totalNum+= orderDetail.getSkuNum();  
 } return totalNum;  
}

初始化AlipayClient

@Configuration  
@PropertySource("classpath:alipay.properties")  
public class AlipayConfig { @Value("${alipay_url}") private String alipay_url; @Value("${app\_private\_key}") private String app\_private\_key; @Value("${app_id}") private String app_id; public final static String format="json"; public final static String charset="utf-8"; public final static String sign_type="RSA2"; public static String return\_payment\_url; public static String notify\_payment\_url; public static String return\_order\_url; public static String alipay\_public\_key; @Value("${alipay\_public\_key}") public void setAlipay\_public\_key(String alipay\_public\_key) {  
 AlipayConfig.alipay\_public\_key = alipay\_public\_key;  
 } @Value("${return\_payment\_url}") public void setReturn\_url(String return\_payment_url) {  
 AlipayConfig.return\_payment\_url = return\_payment\_url;  
 } @Value("${notify\_payment\_url}") public void setNotify\_url(String notify\_payment_url) {  
 AlipayConfig.notify\_payment\_url = notify\_payment\_url;  
 } @Value("${return\_order\_url}") public void setReturn\_order\_url(String return\_order\_url) {  
 AlipayConfig.return\_order\_url = return\_order\_url;  
 } @Bean public AlipayClient alipayClient(){  
 AlipayClient alipayClient=new DefaultAlipayClient(alipay_url,app_id,app\_private\_key,format,charset, alipay\_public\_key,sign_type ); return alipayClient;  
 }  
}

5 PaymentController
-------------------

分析  ：

1、通过orderId取得订单信息

2、组合对应的支付信息保存到数据库。

3、组合需要传给支付宝的参数。

4、根据返回的表单html，传给浏览器。

支付宝开发手册：[https://docs.open.alipay.com/270/105900/](https://docs.open.alipay.com/270/105900/)

@RequestMapping(value = "/alipay/submit",method = RequestMethod.POST)  
@ResponseBody  
public ResponseEntity<String> paymentAlipay(HttpServletRequest request, HttpServletResponse httpServletResponse, Model model) throws IOException {  
  
  
 String orderId = request.getParameter("orderId");  
 if(orderId==null||orderId.length()==0){  
 return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();  
 }  
 //获取订单信息 OrderInfo orderInfo= orderService.getOrderInfo( orderId );  
 if(orderInfo==null ){  
 //没有对应的订单 return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();  
 }  
 //保存支付信息 PaymentInfo paymentInfo =new PaymentInfo();  
 paymentInfo.setOrderId(orderId);  
 paymentInfo.setOutTradeNo(orderInfo.getOutTradeNo());  
 paymentInfo.setSubject(orderInfo.getOrderSubject());  
 paymentInfo.setPaymentStatus(PaymentStatus.UNPAID);  
 paymentInfo.setTotalAmount(orderInfo.getTotalAmount());  
 paymentService.savePaymentInfo(paymentInfo);  
  
  
 //利用支付宝客户端生成表单页面 AlipayTradePagePayRequest alipayRequest=new AlipayTradePagePayRequest();  
  
 alipayRequest.setReturnUrl(AlipayConfig.return\_payment\_url);  
 alipayRequest.setNotifyUrl(AlipayConfig.notify\_payment\_url);  
  
 Map<String,String> paramMap=new HashMap<>();  
 paramMap.put("out\_trade\_no",paymentInfo.getOutTradeNo());  
 paramMap.put("product_code","FAST\_INSTANT\_TRADE_PAY");  
 paramMap.put("total_amount",paymentInfo.getTotalAmount().toString());  
 paramMap.put("subject",paymentInfo.getSubject());  
 String paramJson = JSON.toJSONString(paramMap);  
 alipayRequest.setBizContent(paramJson);  
 String form="";  
 try {  
 form = alipayClient.pageExecute(alipayRequest).getBody();  
 } catch (AlipayApiException e) {  
 e.printStackTrace();  
 }  
  
 httpServletResponse.setContentType("text/html;charset=utf-8" );  
  
 //把表单html打印到客户端浏览器 return ResponseEntity.ok().body(form) ;  
}

5 配置alipay.properties
---------------------

增加alipay.properties

alipay_url=https://openapi.alipay.com/gateway.do  
  
app_id=2018020102122556  
  
app\_private\_key=MIIEvQIBADANBgkqhkiG9w0BAQEFAASCBKcwggSjAgEAAoIBAQCdQeknhM2rhiGAH6V0ljxn3rAWIdzduTEQuteTfwjnZtvMhQPuuN1b/88D5yMuaZhZNFeUdWb+SmtP9DAzAWWgnT13T0YhJcxP6txm7JBRrjadCRt+LOFxPiPQk5t9fH7yXjw9i4uMDsNJeTncrVZ/AZYrk0ESC9anJR8XeuBc3HE8T4fqlKKl35jlumIWrPbPNQhKGXaGcOnpiaXO9qYYUSP/tnrjNYXHOso0yBs4YTl+LLX2TJ12p3n/oX6HnL4zQgtN5k4QasHP7CIig1ngcVQGfWsMm4djI9KXNXvGLQPfMQEmyb71mM5OCdl1MtAc6OaIAymhSv2hOLNIuyodAgMBAAECggEAe05/P5mGm4QlKI2n8u8KlneqovASe1kG/BNFjkYB+VBR8OAr4TfbepPvAyRuFap+5xN/yMz14VcBJkRWtufVhEdHNxJV7w/wUIncIGhGEYYFFMVbZWhTrbQH6TiUp6TC9dCmc6vD1CKPRkFj+YGBXT0lPy3LzBa0TYNyCbszyhthrgkpuFYbB0R93IPvvBh5NJFXQytwNb2oVopC9AQWviqnZUZcT0eJ087dQ1WLPa6blBD8DP1PUq0Ldr6pgKfObFxIj8+87DlJznRfdEsbqZlS7jagdw5tLr71WJpctIGPqKpgvajfePP/lj3eY82BKQB+aTw0zmAiB05Yes4LgQKBgQDq3EiQR8J1MEN2rpiLt1WvDYYvKVUgOY7Od//fRPgaMBstbe4TzGBpR8E+z267bHAWLaWtHkfX6muFHn1x68ozEUWk/nZq0smWnuPdcy4E7Itbk36W2FF/rOZB7j5ddlC9byrxDSNgcf9/FA/CU+i5KVQpLYfsk2dvwomvu0aFVQKBgQCraXpxzMmsBx4127LsZDO5bxfxb6nqzyK4NPe0VaGiRg8oaCWczcLz1J5iRqC9QeEwsSt4XU1sYBMTcsFpA0apZpm3prH2HJRx/isNENesaHcihF0mMd0WxU3xyRvWSDeZV5A1Zy1ZEJ+p17DGwb2j+yo2uBrDNXBgBWEzXwiRqQKBgBdXFvsHtqKQzlOQHGbeLGy+KlSrheMy9Sc9s7cLkqB/oWPNZfifugEceW71jGqh5y29EZb3yGoDyPWsxwi4Rxr2H3a7Nyd8lT4bwkdyt+MTYvIR4WW6T7chhqyMsbP2GyYIUzsrdBWUnrCRXNOSJTGpksyY0sZHC+OGcMp/EQ4VAoGBAIISSVL/pm1+/UK7U1ukcced8JpKNLM0uVD1CJ50eHHOHgR4e0owrWYfioxisejLjBlJ6AWvL2g0w2T3qKKKVN2JOM4ulU5/w3l4+KwygqaWowizTogEQJPd5ta52ADTzjTzSD/t6nByd+YHAWLhc4lyt0bMj6pf68VBb8/upm75AoGAGAYz79IVHp9eppykufjNcWu6okkG8tZnzuyaWKW/CuKKBWMaTk0vcyQlfJfxIBccoQrBuYyXBdcpPuZ/ys2C25pNrkACuhIKNgnMc0floJoYEfJzetw/3cIimWu4NJzVQOaojaGA58oo2+fub43Xn25Jq4rvSVe3oLdb5xWkw5Q=  
  
alipay\_public\_key=MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAhkZi6W0wn/prX+NIIF9ATb5Z8ReKK4hFYtBrweDfGHD1mNW7YIZY4G5hE7S2Sry8eFXlFgSlBWlJ4fVnDaK9MkVThpwE2H65ooVlK/wLuyPqovIVpMt/utva5Ayuzv7eQOWK45FdLDNDlK8QLoBko6SS+YbnWnf7a+mrf4NAS4UFClpfe8Byqe8XIraO2Cg4Ko5Y5schX39rOAH8GlLdgqQRYVQ2dCnkIQ+L+I4Cy9Mvw3rIkTwt3MBU+AqREXY4r5Bn6cmmX/9MAJbFqrofGiUAqG+qbjTcZAzgNPfuiD0zXgt/YYjMQMzck75BOmwnYOam2ajODUSQn8Xybsa7wQIDAQAB  
  
\# 同步回调地址  重定向地址本地浏览器  
return\_payment\_url=http://payment.gmall.com:8090/alipay/callback/return  
  
\# 异步通知地址  公网接口(webService)  
notify\_payment\_url=异步通知地址  
return\_order\_url=同步通知地址

支付后回调—同步回调
==========

![img](./img/image043.png)

PaymentController

@RequestMapping(value="/alipay/callback/return",method = RequestMethod.GET)  
public String callbackReturn(HttpServletRequest request, Model model) throws UnsupportedEncodingException {  
 System.out.println(" callback return to "+AlipayConfig.return\_order\_url); return "redirect:"+AlipayConfig.return\_order\_url;  
}

这里requestMapping对应的路径必须与之前传给支付宝的alipayRequest.setReturnUrl保持一致。