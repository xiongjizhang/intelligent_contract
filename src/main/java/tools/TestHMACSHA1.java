package tools;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import org.apache.log4j.Level;
import org.apache.log4j.Logger;

import java.net.URLEncoder;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

public class TestHMACSHA1 {
    private static Logger logger = Logger.getLogger(TestHMACSHA1.class);

    public static final String DATE_FORMAT = "yyyy-MM-dd'T'HH:mm:ss'Z'";

    public static void main(String[] args) throws Exception {

        logger.setLevel(Level.INFO);

        String test = URLEncoder.encode("账户注册", "UTF-8");

        String access_key_id = "LTAIUzHm83TGI0Gz";
        String instance_id = "chatbot-cn-0pp14yt01000av";
        String utterance = "自助备案系统如何注册";

        DateFormat formatter = new SimpleDateFormat(DATE_FORMAT);
        formatter.setTimeZone(TimeZone.getTimeZone("UTC"));
        Date date = Calendar.getInstance().getTime();
        String timestamp = formatter.format(date);
        String signature_nonce = "" + System.currentTimeMillis();
        String params = "AccessKeyId=" + URLEncoder.encode(access_key_id,"UTF-8") +
                "&Action=Chat" +
                "&Format=json" +
                "&InstanceId=" + URLEncoder.encode(instance_id,"UTF-8") +
                "&SignatureMethod=HMAC-SHA1" +
                "&SignatureNonce=" + URLEncoder.encode(signature_nonce,"UTF-8") +
                "&SignatureVersion=1.0" +
                "&Timestamp=" + URLEncoder.encode(timestamp,"UTF-8") +
                "&Utterance=" + URLEncoder.encode(utterance,"UTF-8") +
                "&Version=2017-10-11";

        params = "GET&%2F&" + URLEncoder.encode(params ,"UTF-8");

        /* String comment = "GET&%2F&" +
                "AccessKeyId%3DLTAIUzHm83TGI0Gz%26" +
                "Action%3DChat%26" +
                "Format%3Dxml%26" +
                "InstanceId%3Dchatbot-cn-0pp14yt01000av%26" +
                "SignatureMethod%3DHMAC-SHA1%26" +
                "SignatureNonce%3D1558378466%26" +
                "SignatureVersion%3D1.0%26" +
                "Timestamp%3D2019-05-22T02%253A30%253A07Z%26" +
                "Utterance%3D%25E8%25B4%25A6%25E6%2588%25B7%25E6%25B3%25A8%25E5%2586%258C%26" +
                "Version%3D2017-10-11";
              */
        String key = "s1HdCHuGJGORPbZ09rNbc8nMOdIQxQ&";
        String signature = HMACSHA1.getSignatureBase64(params, key);

        String url = "https://chatbot.cn-shanghai.aliyuncs.com/?" +
                "AccessKeyId=" + URLEncoder.encode(access_key_id,"UTF-8") +
                "&Action=Chat" +
                "&Format=json" +
                "&InstanceId=" + URLEncoder.encode(instance_id,"UTF-8") +
                "&SignatureMethod=HMAC-SHA1" +
                "&SignatureNonce=" + URLEncoder.encode(signature_nonce,"UTF-8") +
                "&SignatureVersion=1.0" +
                "&Timestamp=" + URLEncoder.encode(timestamp,"UTF-8") +
                "&Utterance=" + URLEncoder.encode(utterance,"UTF-8") +
                "&Version=2017-10-11" +
                "&Signature=" + signature;

        String result = HttpClient3.doGet(url);

        JSONObject object = JSONObject.parseObject(result);

        JSONArray messages = object.getJSONArray("Messages");
        String session_id = object.getString("SessionId");
        String message_id = object.getString("MessageId");
        for(int i = 0; i < messages.size(); i++) {
            JSONObject message = messages.getJSONObject(i);
            switch (message.getString("Type")) {
                case "Recommend" :
                    logger.info("Result Type Recommend!!!");
                    JSONArray recommends = message.getJSONArray("Recommends");
                    for (int j = 0; j < recommends.size(); j++) {
                        JSONObject recommend = recommends.getJSONObject(j);
                        String knowledge_id = recommend.getString("KnowledgeId"); // 知识库中的关联问题的ID
                        String title = recommend.getString("Title"); // 知识库中的关联问题的标题
                        logger.info("KnowledgeId: " + knowledge_id);
                        logger.info("Title: " + title);
                    }
                    break;
                case "Text":
                    JSONObject text = message.getJSONObject("Text");
                    String content = text.getString("Content"); // 文本消息的内容
                    String answer_source = text.getString("AnswerSource"); // 区分答案类型。ChitChat:闲聊；KnowledgeBase:知识库条；BotFramework:任务型；NO_ANSWER:无答案
                    logger.info("Result Type Text!!!");
                    logger.info("Content : " +content);
                    logger.info("AnswerSource: " + answer_source);
                    break;
                case "Knowledge" :
                    JSONObject knowledge = message.getJSONObject("Knowledge");
                    String id = knowledge.getString("Id"); // 知识库中的关联问题ID
                    String title = knowledge.getString("Title"); // 关联问题的标题
                    String summary = knowledge.getString("Summary"); // 关联问题的简介
                    String content1 = knowledge.getString("Content"); // 关联问题的内容
                    logger.info("Result Type Knowledge!!!");
                    logger.info("KnowledgeId: " + id);
                    logger.info("Title: " + title);
                    logger.info("Summary: " + summary);
                    logger.info("Content: " + content1);
                    break;
            }

        }

    }
    // cdoqLgBxZpEr3IcUFNRmG6sQq3E=
    // cdoqLgBxZpEr3IcUFNRmG6sQq3E=

}
