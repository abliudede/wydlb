package com.wydlb.first.service;

import android.os.Looper;
import android.text.TextUtils;

import com.wydlb.first.bean.OutsideChapterDetail;
import com.wydlb.first.utils.RxLogTool;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.nio.charset.Charset;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by lrz on 2018/4/10.
 */

public class ParserService {
    public static String delHTMLTag(String paramString) {
        paramString = Pattern.compile("<script[^>]*?>[\\s\\S]*?<\\/script>", Pattern.CASE_INSENSITIVE).matcher(paramString).replaceAll("");
        paramString = Pattern.compile("<style[^>]*?>[\\s\\S]*?<\\/style>", Pattern.CASE_INSENSITIVE).matcher(paramString).replaceAll("");
        return Pattern.compile("<[^>]+>", Pattern.CASE_INSENSITIVE).matcher(paramString).replaceAll("").trim();
    }


    public static OutsideChapterDetail getDataBeanFromHtml(String sourceHtml, String regContent,String[] illegalWords) {

        //先替换br等为换行，以免后面去掉子标签时没有换行格式。
        String sourceData=sourceHtml.replace("<br>", "\n").replaceAll("<br\\s*/>", "\n");
        OutsideChapterDetail outsideChapterDetail=new OutsideChapterDetail();

        //换用新的css选择器
        Document doc = Jsoup.parse(sourceData);
                    /*获取章节内容*/
                    Elements elements = doc.select(regContent);
                    StringBuilder content = new StringBuilder();
                    for(Element element:elements){
                        //去掉所有子标签
                        element.children().remove();
                        //此时文本内应无任何html标签,用wholetext保留换行格式
                        String source = element.wholeText();
                        source = replaceLine(source);
                        //只有包含非空字符时，才加入字符串拼接，不然看起来就是一个空页面
                        if(!TextUtils.isEmpty(source.trim())){
                            content.append(source);
                            content.append("\n");
                        }
                    }
                    /*过滤多余字符*/
                    String contentTemp = content.toString();
                    if (null!=illegalWords){//非法字符过滤
                        for(String str:illegalWords){
                            if (contentTemp.contains(str)){
                                contentTemp=contentTemp.replace(str,"");//替换
                            }
                        }
                    }
        outsideChapterDetail.setContent(contentTemp);

//        Pattern pattern=Pattern.compile(regContent,Pattern.DOTALL);
//        Matcher matcher=pattern.matcher(sourceData);
//        while (matcher.find()){
//            String source=matcher.group(0);
//            source=delHTMLTag(replaceLine(source));
//            if (null!=illegalWords){//非法字符过滤
//                for(String str:illegalWords){
//                    RxLogTool.e("ParserService illegalWord:"+str);
//                    if (source.contains(str)){
//                        source=source.replace(str,"");//替换
//
//                        RxLogTool.e("ParserService illegalWord replace:"+str);
//                    }
//                }
//            }
//            try{
//                source=Pattern.compile("&#\\d+;",Pattern.CASE_INSENSITIVE).matcher(source).replaceAll("");
//            }catch (Exception e){
//                e.printStackTrace();
//            }
//            outsideChapterDetail.setContent(source);
//            break;
//        }


        return  outsideChapterDetail;

    }



    public static OutsideChapterDetail getDataBeanFromInside(String sourceHtml) {
        //先替换br等为换行，以免后面去掉子标签时没有换行格式。
        String sourceData=sourceHtml.replace("<br>", "\n").replace("<p>", "\n").replaceAll("<br\\s*/>", "\n");
        OutsideChapterDetail outsideChapterDetail=new OutsideChapterDetail();

        outsideChapterDetail.setContent(sourceData);

        return  outsideChapterDetail;
    }

    public static String replaceLine(String paramString)
    {
        return paramString.replace("&gt;", " ").replace("&nbsp;", " ").replaceAll("\n{2,}", "\n").replace("\r", "\n").replaceAll("[\n]{2,}", "\n");
    }

}
