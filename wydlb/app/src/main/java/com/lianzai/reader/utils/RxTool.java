package com.lianzai.reader.utils;

import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.database.Cursor;
import android.os.Handler;
import android.os.HandlerThread;
import android.provider.ContactsContract;
import android.text.Editable;
import android.text.InputFilter;
import android.text.InputType;
import android.text.Spanned;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.lianzai.reader.R;
import com.lianzai.reader.base.BuglyApplicationLike;
import com.lianzai.reader.base.Constant;
import com.lianzai.reader.bean.AccountDetailBean;
import com.lianzai.reader.bean.AreaCodeBean;
import com.lianzai.reader.interfaces.OnDelayListener;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.math.RoundingMode;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.SortedMap;

/**
 * Created by vondear on 2016/1/24.
 * RxTools的常用工具类
 * <p>
 * For the brave souls who get this far: You are the chosen ones,
 * the valiant knights of programming who toil away, without rest,
 * fixing our most awful code. To you, true saviors, kings of men,
 * I say this: never gonna give you up, never gonna let you down,
 * never gonna run around and desert you. Never gonna make you cry,
 * never gonna say goodbye. Never gonna tell a lie and hurt you.
 * <p>
 * 致终于来到这里的勇敢的人：
 * 你是被上帝选中的人，是英勇的、不敌辛苦的、不眠不休的来修改我们这最棘手的代码的编程骑士。
 * 你，我们的救世主，人中之龙，我要对你说：永远不要放弃，永远不要对自己失望，永远不要逃走，辜负了自己，
 * 永远不要哭啼，永远不要说再见，永远不要说谎来伤害自己。
 */
public class RxTool {

    private static Context context;
    private static long lastClickTime;

    /**
     * 初始化工具类
     *
     * @param context 上下文
     */
    public static void init(Context context) {
        RxTool.context = context.getApplicationContext();
//        RxCrashTool.getInstance(context).init();
    }

    /**
     * 在某种获取不到 Context 的情况下，即可以使用才方法获取 Context
     * <p>
     * 获取ApplicationContext
     *
     * @return ApplicationContext
     */
    public static Context getContext() {
        if (context != null) return context;
        throw new NullPointerException("请先调用init()方法");
    }
    //==============================================================================================延时任务封装 end

    //----------------------------------------------------------------------------------------------延时任务封装 start
    public static void delayToDo(long delayTime, final OnDelayListener onDelayListener) {
        new Handler().postDelayed(new Runnable() {
            public void run() {
                //execute the task
                onDelayListener.doSomething();
            }
        }, delayTime);
    }

    /**
     * 手动计算出listView的高度，但是不再具有滚动效果
     *
     * @param listView
     */
    public static void fixListViewHeight(ListView listView) {
        // 如果没有设置数据适配器，则ListView没有子项，返回。
        ListAdapter listAdapter = listView.getAdapter();
        int totalHeight = 0;
        if (listAdapter == null) {
            return;
        }
        for (int index = 0, len = listAdapter.getCount(); index < len; index++) {
            View listViewItem = listAdapter.getView(index, null, listView);
            // 计算子项View 的宽高
            listViewItem.measure(0, 0);
            // 计算所有子项的高度
            totalHeight += listViewItem.getMeasuredHeight();
        }
        ViewGroup.LayoutParams params = listView.getLayoutParams();
        // listView.getDividerHeight()获取子项间分隔符的高度
        // params.height设置ListView完全显示需要的高度
        params.height = totalHeight + (listView.getDividerHeight() * (listAdapter.getCount() - 1));
        listView.setLayoutParams(params);
    }

    //---------------------------------------------MD5加密-------------------------------------------

    /**
     * 生成MD5加密32位字符串
     *
     * @param MStr :需要加密的字符串
     * @return
     */
    public static String Md5(String MStr) {
        try {
            final MessageDigest mDigest = MessageDigest.getInstance("MD5");
            mDigest.update(MStr.getBytes());
            return bytesToHexString(mDigest.digest());
        } catch (NoSuchAlgorithmException e) {
            return String.valueOf(MStr.hashCode());
        }
    }

    // MD5内部算法---------------不能修改!
    private static String bytesToHexString(byte[] bytes) {
        // http://stackoverflow.com/questions/332079
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < bytes.length; i++) {
            String hex = Integer.toHexString(0xFF & bytes[i]);
            if (hex.length() == 1) {
                sb.append('0');
            }
            sb.append(hex);
        }
        return sb.toString();
    }
    //============================================MD5加密============================================

    /**
     * 根据资源名称获取资源 id
     * <p>
     * 不提倡使用这个方法获取资源,比其直接获取ID效率慢
     * <p>
     * 例如
     * getResources().getIdentifier("ic_launcher", "drawable", getPackageName());
     *
     * @param context
     * @param name
     * @param defType
     * @return
     */
    public static final int getResIdByName(Context context, String name, String defType) {
        return context.getResources().getIdentifier("ic_launcher", "drawable", context.getPackageName());
    }

    public static boolean isFastClick(int millisecond) {
        long curClickTime = System.currentTimeMillis();
        long interval = (curClickTime - lastClickTime);

        if (0 < interval && interval < millisecond) {
            // 超过点击间隔后再将lastClickTime重置为当前点击时间
            return true;
        }
        lastClickTime = curClickTime;
        return false;
    }

    /**
     * Edittext 首位小数点自动加零，最多两位小数
     *
     * @param editText
     */
    public static void setEdTwoDecimal(EditText editText) {
        setEdDecimal(editText, 3);
    }

    public static void setEdDecimal(EditText editText, int count) {
        if (count < 1) {
            count = 1;
        }

        editText.setInputType(InputType.TYPE_NUMBER_FLAG_DECIMAL | InputType.TYPE_CLASS_NUMBER);

        //设置字符过滤
        final int finalCount = count;
        editText.setFilters(new InputFilter[]{new InputFilter() {
            @Override
            public CharSequence filter(CharSequence source, int start, int end, Spanned dest, int dstart, int dend) {
                if (source.equals(".") && dest.toString().length() == 0) {
                    return "0.";
                }
                if (dest.toString().contains(".")) {
                    int index = dest.toString().indexOf(".");
                    int mlength = dest.toString().substring(index).length();
                    if (mlength == finalCount) {
                        return "";
                    }
                }
                return null;
            }
        }});
    }

    public static void setEditNumberPrefix(final EditText edSerialNumber, final int number) {
        edSerialNumber.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus) {
                    String s = edSerialNumber.getText().toString();
                    String temp = "";
                    for (int i = s.length(); i < number; i++) {
                        s = "0" + s;
                    }

                    for (int i = 0; i < number; i++) {
                        temp += "0";
                    }
                    if (s.equals(temp)) {
                        s = temp.substring(1) + "1";
                    }
                    edSerialNumber.setText(s);
                }
            }
        });
    }

    public static Handler getBackgroundHandler() {
        HandlerThread thread = new HandlerThread("background");
        thread.start();
        Handler mBackgroundHandler = new Handler(thread.getLooper());
        return mBackgroundHandler;
    }

    public static void stringFormat(View view, int stringId, String value, boolean hint, Context context) {
        if (view instanceof EditText) {
            if (hint) {
                ((EditText) view).setHint(String.format(context.getResources().getString(stringId), value));
            } else {
                ((EditText) view).setText(String.format(context.getResources().getString(stringId), value));
            }

        } else if (view instanceof TextView) {
            ((TextView) view).setText(String.format(context.getResources().getString(stringId), value));
        }
    }

    public static void stringFormat(TextView view, int stringId, String value,Context mcontext){
        view.setText(String.format(mcontext.getResources().getString(stringId), value));
    }


    /**
     * 余额不足，文字变红色
     *
     * @param et
     * @param max
     * @param context
     */
    public static void editTextHint(final EditText et, final Double max, final Context context) {
        TextWatcher textWatcher = new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (!TextUtils.isEmpty(s.toString()) && max < Double.parseDouble(s.toString())) {
                    et.setTextColor(context.getResources().getColor(R.color.red_color));
                } else {
                    et.setTextColor(context.getResources().getColor(R.color.normal_text_color));
                }
            }
        };
        if (null != et)
            et.addTextChangedListener(textWatcher);

    }

    public static String getResourceString(int string, Context context) {
        return context.getResources().getString(string);
    }

    public static AccountDetailBean getAccountBean() {
        return RxSharedPreferencesUtil.getInstance().getObject(Constant.ACCOUNT_CACHE, AccountDetailBean.class);
    }


    public static String getContactPhone(Context context, Cursor cursor) {
        // TODO Auto-generated method stub
        int phoneColumn = cursor
                .getColumnIndex(ContactsContract.Contacts.HAS_PHONE_NUMBER);
        int phoneNum = cursor.getInt(phoneColumn);

        RxLogTool.e("getContactPhone phoneNum:"+phoneNum);
        String result = "";
        if (phoneNum > 0) {
            // 获得联系人的ID号
            int idColumn = cursor.getColumnIndex(ContactsContract.Contacts._ID);
            RxLogTool.e("getContactPhone idColumn:"+idColumn);
            String contactId = cursor.getString(idColumn);
            // 获得联系人电话的cursor
            Cursor phone = context.getContentResolver().query(
                    ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
                    null,
                    ContactsContract.CommonDataKinds.Phone.RAW_CONTACT_ID + "="
                            + contactId, null, null);

            RxLogTool.e("getContactPhone phone count:"+phone.getCount());
            if (phone.moveToFirst()) {
                for (; !phone.isAfterLast(); phone.moveToNext()) {
                    int index = phone
                            .getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER);
                    int typeindex = phone
                            .getColumnIndex(ContactsContract.CommonDataKinds.Phone.TYPE);
                    int phone_type = phone.getInt(typeindex);
                    String phoneNumber = phone.getString(index);
                    result = phoneNumber;
                    switch (phone_type) {//此处请看下方注释
                        case 2:
                            result = phoneNumber;
                            break;

                        default:
                            break;
                    }
                }
                if (!phone.isClosed()) {
                    phone.close();
                }
            }
        }
        return result.replace(" ", "");
    }

    public static boolean isApkDebugable() {
        try {
            ApplicationInfo info= BuglyApplicationLike.getContext().getApplicationInfo();
            return (info.flags&ApplicationInfo.FLAG_DEBUGGABLE)!=0;
        } catch (Exception e) {

        }
        return false;
    }


    public static String getWordNumFormat(String wordNum){
        if (RxDataTool.isEmpty(wordNum))
            return"";
        double data=Double.parseDouble(wordNum);
        DecimalFormat decimalFormat=new DecimalFormat("0.00");
        decimalFormat.setRoundingMode(RoundingMode.FLOOR);
        if (data>10000){//大于1W字
            return decimalFormat.format( data/10000)+"万";
        }
//        else if (data<10000){
//            return decimalFormat.format( data/10000)+"千";
//        }
        else{
            return String.valueOf(wordNum);
        }

    }

    public static String getMineralNumFormat(String wordNum){
        if (RxDataTool.isEmpty(wordNum))
            return"";
        double data=Double.parseDouble(wordNum);
        DecimalFormat decimalFormat=new DecimalFormat("0.00");
        decimalFormat.setRoundingMode(RoundingMode.FLOOR);
        if (data>1000){//大于1W字
            return decimalFormat.format( data/1000)+"k";
        }
//        else if (data<10000){
//            return decimalFormat.format( data/10000)+"千";
//        }
        else{
            return String.valueOf(wordNum);
        }

    }

    /**
     * 过滤特殊字符
     * @param content
     * @return
     */
    public static String filterChar(String content){
        return content.replace("&#12288;","").replace("\\n","\n").replace("&quot;","\"").replace("&emsp;","").replace("&amp;","&").replace("&lt;","<").replace("&gt;",">").replace("&nbsp;"," ");
    }

    /**
     * 将服务器返回的\r \t \n转换，不然布局不显示
     * @param content
     * @return
     */
    public static String filterContent(String content){
        if(null == content) return null;
        if (content.length()>3&&content.substring(0,3).equals("<p>")){//首字符是<p>去掉
            content=content.substring(3);
        }
        return  content
                .replace("<br/>","\n")
                .replace("<br>","\n")
                .replace("<p>","\n")
                .replace("\\n","\n")
                .replace("\\t","\t")
                .replace("\\r","\r");
    }
    public static String filterChapterContent(String content){
        if (content.length()>3&&content.substring(0,3).equals("<p>")){//首字符是<p>去掉
            content=content.substring(3);
        }

        return  content
                .replace("<br/>","\n")
                .replace("<br>","\n")
                .replace("<p>","\n")
                .replace("\\n","\n")
                .replace("\\t","\t")
                .replace("\\r","\r");
    }

    public static String filterHtmlChar(String content){
        return RxTool.filterChar(content).replace("<br/>","\n");
    }

    public static String deleteHtmlChar(String content){
        return RxTool.filterChar(content).replace("<br/>"," ")
                .replace("</br>"," ")
                .replace("<p>"," ")
                .replace("<br>"," ");
    }


    /**
     * 加载国际和地区编码
     * @param mContext
     * @return
     * @throws IOException
     * @throws JSONException
     */
    public static ArrayList<AreaCodeBean> loadAreaCodes(Context mContext) throws IOException, JSONException {
        ArrayList<AreaCodeBean> areaCodeBeans = new ArrayList<AreaCodeBean>();
        BufferedReader reader = null;
        try {
            InputStream in = mContext.getAssets().open("data/areaCode.json");
            reader = new BufferedReader(new InputStreamReader(in));
            StringBuilder jsonString = new StringBuilder();
            String line = null;
            while ((line = reader.readLine()) != null) {
                jsonString.append(line);
            }
            JSONArray array = (JSONArray) new JSONTokener(jsonString.toString())
                    .nextValue();
            for (int i = 0; i < array.length(); i++) {
                JSONObject object=array.getJSONObject(i);
                AreaCodeBean areaCodeBean=new AreaCodeBean();

                areaCodeBean.setChinaName(object.getString("ChinaName"));
                areaCodeBean.setInternaCode(object.getString("InternaCode"));
                areaCodeBean.setQTQ(object.getString("QTQ"));
                areaCodeBean.setREGENT(object.getString("REGENT"));
                areaCodeBean.setISNI(object.getString("ISNI"));
                RxLogTool.e("AreaCodeBean:"+areaCodeBean.toString());
                areaCodeBeans.add(areaCodeBean);
            }
        } catch (FileNotFoundException e) {
            RxLogTool.e("FileNotFoundException"+e.getMessage());

        } finally {
            if (reader != null) {
                reader.close();
            }
        }
        return areaCodeBeans;
    }

    public static String sortMap( SortedMap<Object,Object> parameters, String key){
        StringBuffer sb = new StringBuffer();
        StringBuffer sbkey = new StringBuffer();
        Set es = parameters.entrySet();  //所有参与传参的参数按照accsii排序（升序）
        Iterator it = es.iterator();
        while(it.hasNext()) {
            Map.Entry entry = (Map.Entry)it.next();
            String k = (String)entry.getKey();
            Object v = entry.getValue();
            //空值不传递，不参与签名组串
            if(null != v && !"".equals(v)) {
                sb.append(k + "=" + v + "&");
                sbkey.append(k + "=" + v + "&");
            }
        }
        sbkey=sbkey.append("key="+key);
        return sbkey.toString();
    }

    public static void saveLoginInfo(final String account, final String token) {
        Preferences.saveUserAccount(account);
        Preferences.saveUserToken(token);
    }


    public static void saveBeforeLoginOption(String optionStr,String id){
        try{
            JSONObject jsonObject=new JSONObject();
            jsonObject.put("optionStr",optionStr);
            jsonObject.put("id",id);
            RxSharedPreferencesUtil.getInstance().putString(Constant.LOGIN_BEFORE_OPTION_KEY,jsonObject.toString());
        }catch (Exception e){
//            e.printStackTrace();
        }
    }

    public static JSONObject getBeforeLoginOption(){
        JSONObject jsonObject=null;
        try{
            String json=RxSharedPreferencesUtil.getInstance().getString(Constant.LOGIN_BEFORE_OPTION_KEY);

            jsonObject=new JSONObject(json);


        }catch (Exception e){
//            e.printStackTrace();
        }
        return jsonObject;
    }

}
