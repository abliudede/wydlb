package com.wydlb.first.view;

import android.content.Context;
import androidx.appcompat.widget.AppCompatEditText;
import android.text.InputFilter;
import android.text.Spanned;
import android.util.AttributeSet;

import com.wydlb.first.utils.RxLogTool;

/**
 * Created by lrz on 2018/3/28.
 * 金额数量输入框
 */

public class DecimalNumberEditText extends AppCompatEditText {
    public DecimalNumberEditText(Context context) {
        super(context);
        setFilter();
    }

    public DecimalNumberEditText(Context context, AttributeSet attrs) {
        super(context, attrs);
        setFilter();
    }

    public DecimalNumberEditText(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        setFilter();
    }

    private void setFilter(){
        this.setFilters(new InputFilter[]{
                new InputFilter() {
                    @Override
                    public CharSequence filter(CharSequence source, int start, int end, Spanned dest, int dstart, int dend) {
                        String content=getText().toString();
                        RxLogTool.e("ValueEditText","source:"+source+"--content:"+content+"-dest:"+dest.toString());
                        if (source.toString().length()>1){//直接复制过来的
                            if (source.toString().indexOf(".")>0&&source.toString().length()-source.toString().indexOf(".")>2){
                                return source.toString().substring(0,source.toString().indexOf(".")+3);
                            }else if(source.toString().indexOf(".")==0){
                                return "";
                            }
                            else{
                                return null;
                            }
                        }

                        if (dstart==0&&source.toString().contentEquals(".")){
                            //首字符不能是小数点
                            return "";
                        }else{//34.5
                            int index=content.indexOf(".");
                            RxLogTool.e("ValueEditText","index:"+index);
                            if (index>0&&content.toString().length()-3==index){//小数点后两位
                                return "";
                            }else if(content.length()>10){
                                return "";
                            }else{
                                return null;
                            }
                        }

                    }
                }
        });

    }

}
