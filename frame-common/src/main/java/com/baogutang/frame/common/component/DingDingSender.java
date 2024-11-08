package com.baogutang.frame.common.component;

import com.baogutang.frame.common.utils.SpringUtil;
import com.dingtalk.api.DefaultDingTalkClient;
import com.dingtalk.api.DingTalkClient;
import com.dingtalk.api.request.OapiRobotSendRequest;
import com.dingtalk.api.response.OapiRobotSendResponse;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.codec.binary.Base64;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.concurrent.CompletableFuture;

/**
 * @author N1KO
 */
@Slf4j
@Component
public class DingDingSender {

    @Value("${robot.token:7797d926b98cd7ea50427fff39e7f6de9c621a36e493a9b873182904eb3d9cb2}")
    private String robotToken;
    @Value("${robot.secret:SEC4dd51e536439c206508afa929231911f6f48d47b6d582e90a944b207172ff22d}")
    private String robotSecret;
    @Value("${spring.application.name:baogutang}")
    private String appName;
    @Value("${robot.profile:prod}")
    private List<String> profiles;
    @Value("${robot.business.token:12f335a9c451368dcca16e9614a2987c8588140b18c081fb012a4378f2031226}")
    private String businessRobotToken;
    @Value("${robot.business.secret:SEC2bbaa3ab65ac46b929da54db7e3f0969da9d324f32d372c7d680b3e2e673a2ec}")
    private String businessRobotSecret;

    public void dingSend(String content) {
        if (profiles.contains(SpringUtil.getActiveProfile())) {
            CompletableFuture.runAsync(() -> {
                try {
                    Long timestamp = System.currentTimeMillis();
                    String secret = robotSecret;
                    String stringToSign = timestamp + "\n" + secret;
                    Mac mac = Mac.getInstance("HmacSHA256");
                    mac.init(new SecretKeySpec(secret.getBytes(StandardCharsets.UTF_8), "HmacSHA256"));
                    byte[] signData = mac.doFinal(stringToSign.getBytes(StandardCharsets.UTF_8));
                    String sign = URLEncoder.encode(new String(Base64.encodeBase64(signData)), StandardCharsets.UTF_8);
                    DingTalkClient client = new DefaultDingTalkClient("https://oapi.dingtalk.com/robot/send?sign=" + sign + "&timestamp=" + timestamp);
                    OapiRobotSendRequest req = new OapiRobotSendRequest();
                    OapiRobotSendRequest.Text text = new OapiRobotSendRequest.Text();
                    text.setContent(appName + "\n" + SpringUtil.getActiveProfile() + " env \n" + content);
                    req.setMsgtype("text");
                    req.setText(text);
                    OapiRobotSendResponse rsp = client.execute(req, robotToken);
                    log.info("OapiRobotSendResponse:{}", rsp);
                } catch (Exception e) {
                    log.error("dingSendError:", e);
                }
            });
        }
    }

    public void dingBusinessSend(String title, String content) {
        if (profiles.contains(SpringUtil.getActiveProfile())) {
            CompletableFuture.runAsync(() -> {
                try {
                    Long timestamp = System.currentTimeMillis();
                    String stringToSign = timestamp + "\n" + businessRobotSecret;
                    Mac mac = Mac.getInstance("HmacSHA256");
                    mac.init(new SecretKeySpec(businessRobotSecret.getBytes(StandardCharsets.UTF_8), "HmacSHA256"));
                    byte[] signData = mac.doFinal(stringToSign.getBytes(StandardCharsets.UTF_8));
                    String sign = URLEncoder.encode(new String(Base64.encodeBase64(signData)), StandardCharsets.UTF_8);
                    DingTalkClient client = new DefaultDingTalkClient("https://oapi.dingtalk.com/robot/send?sign=" + sign + "&timestamp=" + timestamp);
                    OapiRobotSendRequest req = new OapiRobotSendRequest();
                    OapiRobotSendRequest.Markdown markdown = new OapiRobotSendRequest.Markdown();
                    markdown.setTitle(title);
                    markdown.setText("## NOTICE\n" +
                            " #### 标题：" + title + "\n" +
                            "  具体内容: " + content);
                    req.setMsgtype("markdown");
                    req.setMarkdown(markdown);
                    OapiRobotSendResponse rsp = client.execute(req, businessRobotToken);
                    log.info("BusinessOapiRobotSendResponse:{}", rsp);
                } catch (Exception e) {
                    log.error("dingSendError:", e);
                }
            });
        }

    }
}
