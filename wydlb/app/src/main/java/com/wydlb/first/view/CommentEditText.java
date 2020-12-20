package com.wydlb.first.view;

import android.content.Context;
import android.text.InputFilter;
import android.text.Spanned;
import android.util.AttributeSet;

/**
 * Created by qingcheng on 2018/3/28.
 * 评论框限制
 */

public class CommentEditText extends androidx.appcompat.widget.AppCompatEditText {
    public CommentEditText(Context context) {
        super(context);
        setFilter();
    }

    public CommentEditText(Context context, AttributeSet attrs) {
        super(context, attrs);
        setFilter();
    }

    public CommentEditText(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        setFilter();
    }

    private void setFilter(){
        this.setFilters(new InputFilter[]{
                new InputFilter() {
                    @Override
                    public CharSequence filter(CharSequence source, int start, int end, Spanned dest, int dstart, int dend) {
                        String content=getText().toString().replace("\n","\\n");
                        //首字符不能是空格或者换行符
                        if (dstart==0&&source.toString().contentEquals("\n")||dstart==0&&source.toString().contentEquals(" ")){
                            //
                            return "";
                        }else{
                            return null;
                        }

                    }
                },new InputFilter.LengthFilter(500)
        });
    }
    //   public String getPublishText(){
//        return this.getText().toString().replace("\n","<p>");
//   }
    public String getPublishText(){
        return this.getText().toString();
    }
}
