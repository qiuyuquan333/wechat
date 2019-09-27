package com.qyq.springbootapi.controller;

import com.alibaba.fastjson.JSON;
import com.qyq.springbootapi.mapper.TokenMapper;
import com.qyq.springbootapi.mapper.UserInfoMapper;
import com.qyq.springbootapi.result.wechat.AccessToken;
import com.qyq.springbootapi.result.wechat.TokenExample;
import com.qyq.springbootapi.result.wechat.UserInfoResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import javax.servlet.http.HttpServletRequest;

@RequestMapping("/wechat")
@RestController
public class WeChatController {

    @Autowired
    private RestTemplate restTemplate;

    String APPID = "wx7a5ab729c84a0c99";
    String APPSECRET = "323a86069b930185eafe607eb078a821";

    @Autowired
    private TokenMapper tokenMapper;
    @Autowired
    private UserInfoMapper userInfoMapper;

    @RequestMapping("/authori")
    public String authoriController(HttpServletRequest request){
        String code = request.getParameter("code");
        String state = request.getParameter("state");
        String object = restTemplate.getForObject("https://api.weixin.qq.com/sns/oauth2/access_token?appid=" + APPID + "&secret=" + APPSECRET + "&code=" + code + "&grant_type=authorization_code", String.class);
        AccessToken accessToken = JSON.parseObject(object, AccessToken.class);
        String access_token = accessToken.getAccess_token();
        String openid = accessToken.getOpenid();
        String scope = accessToken.getScope();

        TokenExample example = new TokenExample();
        example.setCode(code);
        example.setState(state);
        example.setAccess_token(access_token);
        example.setOpenid(openid);
        example.setScope(scope);
        tokenMapper.insertToken(example);

        String object1 = restTemplate.getForObject("https://api.weixin.qq.com/sns/userinfo?access_token=" + access_token + "&openid=" + openid + "&lang=zh_CN", String.class);
        UserInfoResult infoResult = JSON.parseObject(object1, UserInfoResult.class);
//        infoResult.getOpenid();
//        infoResult.getNickname();
//        infoResult.getSex();
//        infoResult.getProvince();
//        infoResult.getCity();
//        infoResult.getCountry();
//        infoResult.getHeadimgurl();
//        infoResult.getPrivilege();
//        infoResult.getUnionid();
        userInfoMapper.insertUserInfo(infoResult);

        return "欢迎你【"+infoResult.getNickname()+"】,扫码登录成功！";

    }


}
