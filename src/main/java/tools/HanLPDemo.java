package tools;

import com.hankcs.hanlp.HanLP;
import com.hankcs.hanlp.seg.Segment;
import com.hankcs.hanlp.seg.common.Term;
import com.huaban.analysis.jieba.JiebaSegmenter;
import com.huaban.analysis.jieba.SegToken;
import org.ansj.domain.Result;
import org.ansj.splitWord.analysis.IndexAnalysis;
import org.ansj.splitWord.analysis.NlpAnalysis;
import org.ansj.splitWord.analysis.ToAnalysis;

import java.util.List;

public class HanLPDemo {

    public static void main(String[] args) {
        String[] testCase = new String[]{
                "签约仪式前，秦光荣、李纪恒、仇和等一同会见了参加签约的企业家。",
                "王国强、高峰、汪洋、张朝阳光着头、韩寒、小四",
                "张浩和胡健康复员回家了",
                "王总和小丽结婚了",
                "编剧邵钧林和稽道青说",
                "这里有关天培的有关事迹",
                "龚学平等领导,邓颖超生前",
                "我是麦紫云我今天很开心",
                "联系人是黄妈妈",
                "我叫司马健",
                "我叫张雄机",
        };
        Segment segment = HanLP.newSegment().enableNameRecognize(true);
        JiebaSegmenter segmenter = new JiebaSegmenter();
        // ToAnalysis toAnalysis = new ToAnalysis();
        // NlpAnalysis nlpAnalysis = new NlpAnalysis();
        for (String sentence : testCase)
        {
            List<Term> termList = segment.seg(sentence);
            System.out.println(termList);
            List<SegToken> segTokenList = segmenter.process(sentence, JiebaSegmenter.SegMode.SEARCH);
            System.out.println(segmenter.process(sentence, JiebaSegmenter.SegMode.SEARCH).toString());
            Result result = NlpAnalysis.parse(sentence);
            System.out.println(result.toString());
            Result result1 = ToAnalysis.parse(sentence);
            System.out.println(result1.toString());
            Result result2 = IndexAnalysis.parse(sentence);
            System.out.println(result2.toString());
        }
    }

}
