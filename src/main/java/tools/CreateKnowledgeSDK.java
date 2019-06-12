package tools;

import com.aliyuncs.DefaultAcsClient;
import com.aliyuncs.IAcsClient;
import com.aliyuncs.chatbot.model.v20171011.*;
import com.aliyuncs.profile.DefaultProfile;

import java.util.ArrayList;

public class CreateKnowledgeSDK {

    public static void main(String[] args) throws Exception {
        DefaultProfile profile = DefaultProfile.getProfile("cn-shanghai", "LTAIUzHm83TGI0Gz", "s1HdCHuGJGORPbZ09rNbc8nMOdIQxQ");
        IAcsClient client = new DefaultAcsClient(profile);

        /*QueryCategoriesRequest queryCategoriesRequest = new QueryCategoriesRequest();
        // queryCategoriesRequest.setParentCategoryId(1L);
        queryCategoriesRequest.setShowChildrens(true);
        QueryCategoriesResponse response = client.getAcsResponse(queryCategoriesRequest);*/

        /*CreateKnowledgeRequest request = new CreateKnowledgeRequest();
        CreateKnowledgeRequest.Knowledge knowledge = new CreateKnowledgeRequest.Knowledge();
        knowledge.setCategoryId(1000034331L);
        ArrayList coreWord = new ArrayList();
        coreWord.add("test");
        knowledge.setCoreWords(coreWord);
        knowledge.setKnowledgeTitle("test");
        knowledge.setSolutions(coreWord);
        knowledge.setKnowledgeType(0);
        request.setKnowledge(knowledge);
        CreateKnowledgeResponse createKnowledgeResponse = client.getAcsResponse(request);*/

        CreateCategoryRequest createCategoryRequest = new CreateCategoryRequest();
        createCategoryRequest.setName("test");
        createCategoryRequest.setParentCategoryId(1000034331L);
        CreateCategoryResponse createCategoryResponse = client.getAcsResponse(createCategoryRequest);

        QueryCategoriesRequest queryCategoriesRequest = new QueryCategoriesRequest();
        queryCategoriesRequest.setParentCategoryId(1000034331L);
        queryCategoriesRequest.setShowChildrens(true);
        QueryCategoriesResponse queryCategoriesResponse = client.getAcsResponse(queryCategoriesRequest);

        QueryKnowledgesRequest queryKnowledgesRequest = new QueryKnowledgesRequest();
        queryKnowledgesRequest.setCoreWordName("备案号");
        QueryKnowledgesResponse queryKnowledgesResponse = client.getAcsResponse(queryKnowledgesRequest);

        System.out.println(0);

    }

}
