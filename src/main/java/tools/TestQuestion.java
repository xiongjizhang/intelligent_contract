package tools;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import org.apache.log4j.Level;
import org.apache.log4j.Logger;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URLEncoder;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

public class TestQuestion {

    private static Logger logger = Logger.getLogger(TestQuestion.class);

    public static final String DATE_FORMAT = "yyyy-MM-dd'T'HH:mm:ss'Z'";

    public static void main(String[] args) throws Exception {

        logger.setLevel(Level.INFO);

        InputStream in = TestQuestion.class.getClass().getResourceAsStream("/question_answer/test_question.csv");

        String access_key_id = "LTAIUzHm83TGI0Gz";
        String instance_id = "chatbot-cn-0pp14yt01000av";

        BufferedReader in2 = new BufferedReader(new InputStreamReader(in));
        String line = "";
        int success_count = 0;
        int failure_count = 0;
        while ((line = in2.readLine()) != null) {//一行一行读

            String utterance = line.split(",")[0];
            String answer = line.split(",")[1];

            DateFormat formatter = new SimpleDateFormat(DATE_FORMAT);
            formatter.setTimeZone(TimeZone.getTimeZone("UTC"));
            Date date = Calendar.getInstance().getTime();
            String timestamp = formatter.format(date);
            String signature_nonce = "" + System.currentTimeMillis();
            String params = "AccessKeyId=" + URLEncoder.encode(access_key_id, "UTF-8") +
                    "&Action=Chat" +
                    "&Format=json" +
                    "&InstanceId=" + URLEncoder.encode(instance_id, "UTF-8") +
                    "&SessionId=123asdfasd" +
                    "&SignatureMethod=HMAC-SHA1" +
                    "&SignatureNonce=" + URLEncoder.encode(signature_nonce, "UTF-8") +
                    "&SignatureVersion=1.0" +
                    "&Timestamp=" + URLEncoder.encode(timestamp, "UTF-8") +
                    "&Utterance=" + URLEncoder.encode(utterance, "UTF-8") +
                    "&Version=2017-10-11";

            params = "GET&%2F&" + URLEncoder.encode(params, "UTF-8");

            String key = "s1HdCHuGJGORPbZ09rNbc8nMOdIQxQ&";
            String signature = HMACSHA1.getSignatureBase64(params, key);

            String url = "https://chatbot.cn-shanghai.aliyuncs.com/?" +
                    "AccessKeyId=" + URLEncoder.encode(access_key_id, "UTF-8") +
                    "&Action=Chat" +
                    "&Format=json" +
                    "&InstanceId=" + URLEncoder.encode(instance_id, "UTF-8") +
                    "&SessionId=123asdfasd" +
                    "&SignatureMethod=HMAC-SHA1" +
                    "&SignatureNonce=" + URLEncoder.encode(signature_nonce, "UTF-8") +
                    "&SignatureVersion=1.0" +
                    "&Timestamp=" + URLEncoder.encode(timestamp, "UTF-8") +
                    "&Utterance=" + URLEncoder.encode(utterance, "UTF-8") +
                    "&Version=2017-10-11" +
                    "&Signature=" + URLEncoder.encode(signature, "UTF-8");

            String result = HttpClient3.doGet(url);
            logger.info(signature);

            JSONObject object = JSONObject.parseObject(result);

            JSONArray messages = object.getJSONArray("Messages");
            String session_id = object.getString("SessionId");
            String message_id = object.getString("MessageId");
            String result_answer = "";
            for (int i = 0; i < messages.size(); i++) {
                JSONObject message = messages.getJSONObject(i);
                switch (message.getString("Type")) {
                    case "Recommend":
                        /*logger.info("Result Type Recommend!!!");*/
                        JSONArray recommends = message.getJSONArray("Recommends");
                        for (int j = 0; j < recommends.size(); j++) {
                            JSONObject recommend = recommends.getJSONObject(j);
                            String knowledge_id = recommend.getString("KnowledgeId"); // 知识库中的关联问题的ID
                            String title = recommend.getString("Title"); // 知识库中的关联问题的标题
                            /*logger.info("KnowledgeId: " + knowledge_id);
                            logger.info("Title: " + title);*/
                            if (i == 0 && j == 0) {
                                result_answer = title;
                            }
                        }
                        break;
                    case "Text":
                        JSONObject text = message.getJSONObject("Text");
                        String content = text.getString("Content"); // 文本消息的内容
                        String answer_source = text.getString("AnswerSource"); // 区分答案类型。ChitChat:闲聊；KnowledgeBase:知识库条；BotFramework:任务型；NO_ANSWER:无答案
                        /*logger.info("Result Type Text!!!");
                        logger.info("Content : " + content);
                        logger.info("AnswerSource: " + answer_source);*/
                        if (i == 0) {
                            result_answer = content;
                        }
                        break;
                    case "Knowledge":
                        JSONObject knowledge = message.getJSONObject("Knowledge");
                        String id = knowledge.getString("Id"); // 知识库中的关联问题ID
                        String title = knowledge.getString("Title"); // 关联问题的标题
                        String summary = knowledge.getString("Summary"); // 关联问题的简介
                        String content1 = knowledge.getString("Content"); // 关联问题的内容
                        /*logger.info("Result Type Knowledge!!!");
                        logger.info("KnowledgeId: " + id);
                        logger.info("Title: " + title);
                        logger.info("Summary: " + summary);
                        logger.info("Content: " + content1);*/
                        if (i == 0) {
                            result_answer = title;
                        }
                        break;
                }

            }
            if (answer.equals(result_answer) ) {
                logger.info("****************success****************");
                logger.info(utterance + ", " + answer + ", " + result_answer);
                success_count++;
            } else {
                logger.info("****************failure****************");
                logger.info(utterance + ", " + answer + ", " + result_answer);
                failure_count++;
            }
            // Thread.sleep(20000);
        }

        logger.info("Success Count: " + success_count + "  Failure Count: " + failure_count);

    }

}
