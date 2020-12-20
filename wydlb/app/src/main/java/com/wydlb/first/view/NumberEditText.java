package com.wydlb.first.view;

import android.content.Context;
import androidx.appcompat.widget.AppCompatEditText;
import android.text.InputFilter;
import android.text.Spanned;
import android.util.AttributeSet;

/**
 * Created by lrz on 2018/3/28.
 * 正整数数量输入框
 */

public class NumberEditText extends AppCompatEditText {
    public NumberEditText(Context context) {
        super(context);
        setFilter();
    }

    public NumberEditText(Context context, AttributeSet attrs) {
        super(context, attrs);
        setFilter();
    }

    public NumberEditText(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        setFilter();
    }

    private void setFilter(){
        this.setFilters(new InputFilter[]{
                new InputFilter() {
                    @Override
                    public CharSequence filter(CharSequence source, int start, int end, Spanned dest, int dstart, int dend) {
                        String content=getText().toString();
                        if (content.length()>7){//超过7位不能继续输入
                            return"";
                        }
                        if (source.length()>1){//复制过来的
                            if (source.toString().indexOf("0")==0){//首字符不能是0
                                return "";
                            }else{
                                if (source.toString().contains(".")){
                                    int index=source.toString().indexOf(".");
                                    if (index==0){//第一个是.直接不显示
                                        return "";
                                    }else{
                                        return source.toString().substring(0,index);
                                    }
                                }else{
                                    return null;
                                }
                            }

                        }else{//手动输入的
                            if (dend==0&&source.toString().equals("0")){
                                return"";//首字符不能是0
                            }else{
                                return null;
                            }
                        }

                    }
                }
        });

    }

}
