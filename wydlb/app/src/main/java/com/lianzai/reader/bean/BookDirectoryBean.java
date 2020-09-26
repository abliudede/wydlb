package com.lianzai.reader.bean;

import com.chad.library.adapter.base.entity.AbstractExpandableItem;
import com.chad.library.adapter.base.entity.MultiItemEntity;
import com.lianzai.reader.ui.adapter.BookDirectoryExpandableItemAdapter;

import java.io.Serializable;
import java.util.List;

/**
 * Created by lrz on 2018/1/3.
 */

public class BookDirectoryBean implements Serializable {


    /**
     * status_code : 200
     * title : 一曲逍遥一剑明
     * volumeChapters : [{"title":"第一卷 世界起点","volume_num":1,"count":10,"record":[{"id":8247425804,"title":"第一章 傅越明"},{"id":8880985722,"title":"第二章 虚妄之地"},{"id":8996935729,"title":"第三章 此剑逍遥"},{"id":8442595728,"title":"第四章 九重仙界"},{"id":8836775727,"title":"第五章 仙族祭典"},{"id":8105865725,"title":"第六章 上古传说"},{"id":8654935726,"title":"第七章 世界起点"},{"id":8271596962,"title":"外传一 地下通道"},{"id":8575496598,"title":"传记其一 玉帝"},{"id":8206810635,"title":"一卷副传 天道"}]},{"title":"第二卷 墨雨萧音","volume_num":2,"count":18,"record":[{"id":8157125717,"title":"第一章 人间烟火"},{"id":8479585716,"title":"第二章 客家有音"},{"id":8782175713,"title":"第三章 误入封界"},{"id":8103835714,"title":"第四章 旋叶门流"},{"id":8652995710,"title":"第五章 初来乍到"},{"id":8167535781,"title":"第六章 一场试炼"},{"id":8541515672,"title":"第七章 行剑其五"},{"id":8982045671,"title":"第八章 门流往昔"},{"id":8515315677,"title":"第九章 古有神兵"},{"id":8092845676,"title":"第十章 蛮荒一别"},{"id":8459095674,"title":"第十一章 重岩叠嶂"},{"id":8173365652,"title":"第十二章 苦寻至宝"},{"id":8495405651,"title":"第十三章 山中狂妖"},{"id":8044845658,"title":"第十四章 有群蛮子"},{"id":8137065657,"title":"第十五章 以力降服"},{"id":8403885346,"title":"第十六章 归返门流"},{"id":8661025461,"title":"第十七章 门流有变"},{"id":8024475448,"title":"第十八章 墨雨萧音"}]},{"title":"第三卷 雨落成诗","volume_num":3,"count":21,"record":[{"id":8282195095,"title":"第一章 劫后余生"},{"id":8920015083,"title":"第二章 渺渺仙道"},{"id":8531525049,"title":"第三章 域外孤城"},{"id":8817816124,"title":"第四章 魔族踪迹"},{"id":8230676819,"title":"第五章 媛紫分堂"},{"id":8446876792,"title":"第六章 再度启程"},{"id":8391416676,"title":"第七章 打探被察"},{"id":8663396460,"title":"第八章 杀入媛紫"},{"id":8878443864,"title":"第九章 一线生机"},{"id":8845943698,"title":"第十章 庞大势力"},{"id":8524083469,"title":"第十一章 渝都秘密"},{"id":8404004121,"title":"第十二章 总舵追杀"},{"id":8406364596,"title":"第十三章 骇人的剑"},{"id":8441704601,"title":"第十四章 舵主出手"},{"id":8450324358,"title":"第十五章 秦家相助"},{"id":8064644013,"title":"第十六章 绝境求生"},{"id":8292484085,"title":"第十七章 天降一刀"},{"id":8861640943,"title":"第十八章 斩杀梁悔"},{"id":8038160772,"title":"第十九章 益丘拜谢"},{"id":8884460482,"title":"第二十章 昙花园中"},{"id":3566892191,"title":"第二十一章 雨落成诗"}]},{"title":"第四卷 一方天地","volume_num":4,"count":7,"record":[{"id":3113392520,"title":"第一章 人界帝都"},{"id":3790292497,"title":"第二章 故人重逢"},{"id":3812299163,"title":"第三章 人界至尊"},{"id":3559799533,"title":"第四章 妖族异动"},{"id":3661199481,"title":"第五章 上古狂兽"},{"id":3949391981,"title":"第六章 妖王蚩尤"},{"id":3083791867,"title":"第七章 益丘之战"}]}]
     * relatedChapters : [{"id":8114885737,"title":"一些心里话"},{"id":3085499435,"title":"《一曲逍遥一剑明》序传"}]
     */

    private int status_code;
    private String title;
    private List<VolumeChaptersBean> volumeChapters;
    private List<RelatedChaptersBean> relatedChapters;

    public int getStatus_code() {
        return status_code;
    }

    public void setStatus_code(int status_code) {
        this.status_code = status_code;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public List<VolumeChaptersBean> getVolumeChapters() {
        return volumeChapters;
    }

    public void setVolumeChapters(List<VolumeChaptersBean> volumeChapters) {
        this.volumeChapters = volumeChapters;
    }

    public List<RelatedChaptersBean> getRelatedChapters() {
        return relatedChapters;
    }

    public void setRelatedChapters(List<RelatedChaptersBean> relatedChapters) {
        this.relatedChapters = relatedChapters;
    }

    public static class VolumeChaptersBean extends AbstractExpandableItem<VolumeChaptersBean.RecordBean> implements Serializable,MultiItemEntity{
        @Override
        public int getLevel() {
            return 1;
        }

        @Override
        public int getItemType() {
            return BookDirectoryExpandableItemAdapter.VOLUME;
        }

        /**
         * title : 第一卷 世界起点
         * volume_num : 1
         * count : 10
         * record : [{"id":8247425804,"title":"第一章 傅越明"},{"id":8880985722,"title":"第二章 虚妄之地"},{"id":8996935729,"title":"第三章 此剑逍遥"},{"id":8442595728,"title":"第四章 九重仙界"},{"id":8836775727,"title":"第五章 仙族祭典"},{"id":8105865725,"title":"第六章 上古传说"},{"id":8654935726,"title":"第七章 世界起点"},{"id":8271596962,"title":"外传一 地下通道"},{"id":8575496598,"title":"传记其一 玉帝"},{"id":8206810635,"title":"一卷副传 天道"}]
         */



        private String title;
        private int volume_num;
        private int count;
        private List<RecordBean> record;

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public int getVolume_num() {
            return volume_num;
        }

        public void setVolume_num(int volume_num) {
            this.volume_num = volume_num;
        }

        public int getCount() {
            return count;
        }

        public void setCount(int count) {
            this.count = count;
        }

        public List<RecordBean> getRecord() {
            return record;
        }

        public void setRecord(List<RecordBean> record) {
            this.record = record;
        }

        public static class RecordBean implements Serializable,MultiItemEntity{
            @Override
            public int getItemType() {
                return BookDirectoryExpandableItemAdapter.CHAPTER;
            }
            /**
             * id : 8247425804
             * title : 第一章 傅越明
             */


            private String id;
            private String title;

            private int chapterPos;

            public int getChapterPos() {
                return chapterPos;
            }

            public void setChapterPos(int chapterPos) {
                this.chapterPos = chapterPos;
            }

            public String getId() {
                return id;
            }

            public void setId(String id) {
                this.id = id;
            }

            public String getTitle() {
                return title;
            }

            public void setTitle(String title) {
                this.title = title;
            }
        }
    }

    public static class RelatedChaptersBean implements Serializable{
        /**
         * id : 8114885737
         * title : 一些心里话
         */

        private String id;
        private String title;

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }
    }
}
