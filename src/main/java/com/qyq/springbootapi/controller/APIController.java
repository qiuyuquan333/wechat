package com.qyq.springbootapi.controller;

import com.alibaba.fastjson.JSON;
import com.qyq.springbootapi.result.BaiduResult;
import com.qyq.springbootapi.result.ResponseResult;
import com.qyq.springbootapi.util.RsaUtil;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.net.URLDecoder;
import java.net.URLEncoder;

@RestController
public class APIController {

    @Autowired
    private RestTemplate restTemplate;

    @RequestMapping("/baidu/{query}/{region}/{output}")
    public BaiduResult BaiduApiController(@PathVariable("query") String query,@PathVariable("region") String region,@PathVariable("output") String output){
        ResponseEntity<String> entity = restTemplate.getForEntity("http://api.map.baidu.com/place/v2/search?query="+query+"&region="+region+"&output="+output+"&ak=zlGSZV51xCwMUprdbFefGL41wrvU60Vb", String.class);
        String body = entity.getBody();
        BaiduResult baiduResult = JSON.parseObject(body, BaiduResult.class);
//        Location location = baiduResult.getResults().get(0);
        return baiduResult;
    }


    @ApiOperation(value = "服务端第三方接口",notes = "接收加密过后的字符串进行解密并返回签证信息" )
    @ApiImplicitParams({
            @ApiImplicitParam(name = "result",value = "加密字符串",required = true,dataType = "String")
    })
    @GetMapping("/test")
    public ResponseResult TestApiController(@RequestParam("result") String result){
        String privateKey = "MIICdgIBADANBgkqhkiG9w0BAQEFAASCAmAwggJcAgEAAoGBAMcJilCfGtsoXe/ioAqT2sh36bz2j+KmUUcaKaF+dQNQ/LKTZNyftazl44Oyl6gEGfSYkFgGiCMutv6EMHlPRV6AYL8VNo8BmcDGDfWWyL3fDIpxtD99IXLV7k0f7Hm4doEuaEEb9pY3+wpAYe2Fr41MLRv4kI9nG7/OH24/cTHdAgMBAAECgYATKFPr1qSbG5OPF10YJ4sTaJdS3NiUlDWWhmojZSyWGSF5fr5ijezKq4Xuky4NufdVl6mtD1PjLwfmD4sq4fpnJqfAuUMUGe3NC9Y6hm3VqWdXqY9UrRaQQUUsPlQlxaMYCOwjPdgvk3BRkIet1vY/63EWjrFfXXqhb+GpgKz68QJBAO4/FZI7E9lK8BMRodrT44rB1W0hV/nan0Lel2ZSoZf0QhChCtQLP6WJ3rfC1IQHmL9ZWVqD1fzndXEHyAJcUmcCQQDV3ns0lVFll76dGZ0VEB8G+vVD7L8TIpJSuCaRKf4KZXIWoOUIoefgNEEW8AGF9EwkPlUNFrSXahEeyvjFgNcbAkAvqKLyC6tezmTUC7TrB3P/L+M333Gv45H0WH7E00bMFOqaVPzLiiyDOt84t1q3xqVBsefz+Wd3Hf4aTEowXrOFAkBrPi/r00BIwEnd/z0MldPpLSiV0f6SetCC2ELGohb4YZxPCokuxfk/LRTCisFIyZLwzazabltKXe6N0VvjtdKnAkEAxxToDiKLandMMcAZjpeQS57ENSq17DVA7v3ZCl5W9wIIi7zGwcfUFZ/Uwwk55+Hrx/DmBqxQjhGanPzp9XWMVw==";

        try {
            String decode = URLDecoder.decode(result, "UTF-8");
            System.out.println("接收到的加密数据："+decode);
            //解密
            String decrypt = RsaUtil.decrypt(decode, privateKey);
            System.out.println("解密后的内容："+decrypt);
            //加签返回信息
            String sign = RsaUtil.sign(decode, privateKey);
            return new ResponseResult(0,"成功",sign);

        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseResult(0,"失败",404);
        }


    }

    @GetMapping("/get")
    public ResponseResult TestApiController1(String content){
        String publicKey = "MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQDHCYpQnxrbKF3v4qAKk9rId+m89o/iplFHGimhfnUDUPyyk2Tcn7Ws5eODspeoBBn0mJBYBogjLrb+hDB5T0VegGC/FTaPAZnAxg31lsi93wyKcbQ/fSFy1e5NH+x5uHaBLmhBG/aWN/sKQGHtha+NTC0b+JCPZxu/zh9uP3Ex3QIDAQAB";

        try {
            //公钥加密
            String encrypt = RsaUtil.encrypt(content, publicKey);
            System.out.println("公钥加密："+encrypt);
            URI uri = UriComponentsBuilder.fromHttpUrl("http://localhost:8080/test").queryParam("result", URLEncoder.encode(encrypt,"UTF-8")).build().toUri();
            ResponseResult object = restTemplate.getForObject(uri, ResponseResult.class);
            String data = JSON.toJSONString(object.getData());
            System.out.println("返回的签证信息："+data);
            boolean verify = RsaUtil.verify(encrypt, publicKey, data);
            System.out.println("验签结果："+verify);
            return new ResponseResult(0,"成功",verify);

        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseResult(0,"失败",404);
        }

    }



}
