package com.wydlb.first.utils;

import com.wydlb.first.base.Constant;

import java.io.File;
import java.lang.ref.WeakReference;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by newbiechen on 17-5-20.
 * 处理书籍的工具类，配合PageFactory使用
 * 已弃用，
 */

public class BookManager {
    private long position;
    private Map<String, Cache> cacheMap = new HashMap<>();
    private static volatile BookManager sInstance;

    public static BookManager getInstance(){
        if (sInstance == null){
            synchronized (BookManager.class){
                if (sInstance == null){
                    sInstance = new BookManager();
                }
            }
        }
        return sInstance;
    }

    public void setPosition(long position){
        this.position = position;
    }

    public long getPosition(){
        return position;
    }




    public void clear(){
        cacheMap.clear();
        position = 0;
    }

    /**
     * 创建或获取存储文件
     * @param folderName
     * @param fileName
     * @return
     */
    public static File getBookFile(String folderName, String fileName){
        return FileUtils.getFile(Constant.BOOK_CACHE_PATH + folderName
                + File.separator + fileName + FileUtils.SUFFIX_NB);
    }

    /**
     * 移除某本书的所有缓存文件
     * @param folderName
     */
    public static void deleteBookAllChapter(String folderName){
        FileUtils.deleteFile(Constant.BOOK_CACHE_PATH + folderName);
        RxLogTool.e("deleteBookAllChapter....");
    }

    public static long getBookSize(String folderName){
        return FileUtils.getDirSize(FileUtils
                .getFolder(Constant.BOOK_CACHE_PATH + folderName));
    }

    /**
     * 根据文件名判断是否被缓存过 (因为可能数据库显示被缓存过，但是文件中却没有的情况，所以需要根据文件判断是否被缓存
     * 过)
     * @param folderName : bookId
     * @param fileName: chapterName
     * @return
     */
    public static boolean isChapterCached(String folderName, String fileName){
//        RxLogTool.e("isChapterCached:"+folderName+"/"+fileName);
        File file = new File(Constant.BOOK_CACHE_PATH + folderName
                + File.separator + fileName + FileUtils.SUFFIX_NB);
//        RxLogTool.e("file :"+file.getAbsolutePath());
        return file.exists();
    }

    /**
     * 获取某本书已缓存的章节数量
     * @param folderName
     * @return
     */
    public static int getCacheChapterCount(String folderName){
        try{
            File file = new File(Constant.BOOK_CACHE_PATH + folderName);
            if (null!=file&&file.exists()){
                File[] listFiles=file.listFiles();
                return  listFiles.length;
            }
        }catch (Exception e){

        }
        return  0;
    }



    public class Cache {
        private long size;
        private WeakReference<char[]> data;

        public WeakReference<char[]> getData() {
            return data;
        }

        public void setData(WeakReference<char[]> data) {
            this.data = data;
        }

        public long getSize() {
            return size;
        }

        public void setSize(long size) {
            this.size = size;
        }
    }
}
