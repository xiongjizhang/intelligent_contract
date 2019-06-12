package tools;

import com.aliyuncs.DefaultAcsClient;
import com.aliyuncs.IAcsClient;
import com.aliyuncs.exceptions.ClientException;
import com.aliyuncs.exceptions.ServerException;
import com.aliyuncs.profile.DefaultProfile;
import com.google.gson.Gson;

import java.util.*;

import com.aliyuncs.chatbot.model.v20171011.*;

public class TestChatSDK {

    public static void main(String[] args) throws Exception {
        DefaultProfile profile = DefaultProfile.getProfile("cn-shanghai", "LTAIUzHm83TGI0Gz", "s1HdCHuGJGORPbZ09rNbc8nMOdIQxQ");
        IAcsClient client = new DefaultAcsClient(profile);

        ChatRequest request = new ChatRequest();
        request.setUtterance("挂机短信");
        request.setInstanceId("chatbot-cn-0pp14yt01000av");
        ChatResponse response = client.getAcsResponse(request);

        ChatRequest request2 = new ChatRequest();
        request2.setUtterance("取消");
        request2.setInstanceId("chatbot-cn-0pp14yt01000av");
        request2.setSessionId(response.getSessionId());  // bf9fdbac67f8451cb477defb152bfbb4
        ChatResponse response2 = client.getAcsResponse(request2);


        List<ChatResponse.Message> list_messages = response.getMessages();
        String content = "";
        for (ChatResponse.Message message : list_messages
        ) {
            String type = message.getType();
            System.out.println(type);
            if (type.equals("Recommend")) {
                List<ChatResponse.Message.Recommend> list_recommends = message.getRecommends();
                content += "您可以这样问:\n";
                for (ChatResponse.Message.Recommend recommend : list_recommends) {
                    content += recommend.getTitle() + '\n';

                }
            } else if (type.equals("Text")) {
                String text = message.getText().getContent();
                content += text;
            } else if (type.equals("Knowledge")) {
                String text = message.getKnowledge().getSummary();
                content += text;
            }


        }
        System.out.println(content);

        System.out.println(new Gson().toJson(response));


    }
}