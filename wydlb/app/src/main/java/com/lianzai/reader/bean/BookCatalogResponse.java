package com.lianzai.reader.bean;

import com.chad.library.adapter.base.entity.AbstractExpandableItem;
import com.chad.library.adapter.base.entity.MultiItemEntity;
import com.lianzai.reader.ui.adapter.BookCatalogExpandableItemAdapter;

import java.io.Serializable;
import java.util.List;

public class BookCatalogResponse {
    /**
     * code : 0
     * msg : 信息拉取成功
     * data : {"volumes":[{"title":"作品相关","chapters":[{"id":57081,"title":"内容简介1","novelId":2849},{"id":57083,"title":"内容简介2","novelId":2849},{"id":57113,"title":"005 青春初识","novelId":2849},{"id":57114,"title":"006 青春初识2","novelId":2849},{"id":57118,"title":"作品相关哦","novelId":2849},{"id":57126,"title":"测试下缓存","novelId":2849},{"id":57145,"title":"测试PC字数","novelId":2849}]},{"title":"第一卷","chapters":[{"id":56991,"title":"002 梦中白莲","novelId":2849},{"id":57051,"title":"003 老宅光阴","novelId":2849},{"id":57084,"title":"004 人间萍客","novelId":2849},{"id":57115,"title":"007 老宅光阴111","novelId":2849},{"id":57117,"title":"007 老宅光阴111","novelId":2849},{"id":57119,"title":"H5发的正文章节","novelId":2849},{"id":57127,"title":"定时发布的章节","novelId":2849},{"id":57185,"title":"25号发布的第一个章节","novelId":2849}]},{"title":"第二卷","chapters":[{"id":57188,"title":"25号发布的第三个章节","novelId":2849},{"id":57189,"title":"25号发布的第三个章节","novelId":2849},{"id":57190,"title":"25号发布的第二章定时发布","novelId":2849},{"id":57191,"title":"25号发布的第二章定时发布","novelId":2849},{"id":57194,"title":"111","novelId":2849},{"id":57195,"title":"222","novelId":2849},{"id":57212,"title":"h5发布的章节","novelId":2849},{"id":57225,"title":"定时发布","novelId":2849},{"id":57226,"title":"定时发布","novelId":2849}]},{"title":"真爱是一场修行","chapters":[{"id":57227,"title":"随便取的标题","novelId":2849},{"id":57228,"title":"再随便发一个","novelId":2849},{"id":57229,"title":"再发一次试试","novelId":2849},{"id":57230,"title":"来来来","novelId":2849},{"id":57231,"title":"消息免打扰也能收到","novelId":2849},{"id":57235,"title":"11111","novelId":2849},{"id":57237,"title":"11223366","novelId":2849},{"id":57240,"title":"mgdgdjd","novelId":2849}]}],"novelTitle":"你若安好,便是晴天","novelId":"2849"}
     */

    private int code;
    private String msg;
    private DataBean data;

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public DataBean getData() {
        return data;
    }

    public void setData(DataBean data) {
        this.data = data;
    }

    public static class DataBean {
        /**
         * volumes : [{"title":"作品相关","chapters":[{"id":57081,"title":"内容简介1","novelId":2849},{"id":57083,"title":"内容简介2","novelId":2849},{"id":57113,"title":"005 青春初识","novelId":2849},{"id":57114,"title":"006 青春初识2","novelId":2849},{"id":57118,"title":"作品相关哦","novelId":2849},{"id":57126,"title":"测试下缓存","novelId":2849},{"id":57145,"title":"测试PC字数","novelId":2849}]},{"title":"第一卷","chapters":[{"id":56991,"title":"002 梦中白莲","novelId":2849},{"id":57051,"title":"003 老宅光阴","novelId":2849},{"id":57084,"title":"004 人间萍客","novelId":2849},{"id":57115,"title":"007 老宅光阴111","novelId":2849},{"id":57117,"title":"007 老宅光阴111","novelId":2849},{"id":57119,"title":"H5发的正文章节","novelId":2849},{"id":57127,"title":"定时发布的章节","novelId":2849},{"id":57185,"title":"25号发布的第一个章节","novelId":2849}]},{"title":"第二卷","chapters":[{"id":57188,"title":"25号发布的第三个章节","novelId":2849},{"id":57189,"title":"25号发布的第三个章节","novelId":2849},{"id":57190,"title":"25号发布的第二章定时发布","novelId":2849},{"id":57191,"title":"25号发布的第二章定时发布","novelId":2849},{"id":57194,"title":"111","novelId":2849},{"id":57195,"title":"222","novelId":2849},{"id":57212,"title":"h5发布的章节","novelId":2849},{"id":57225,"title":"定时发布","novelId":2849},{"id":57226,"title":"定时发布","novelId":2849}]},{"title":"真爱是一场修行","chapters":[{"id":57227,"title":"随便取的标题","novelId":2849},{"id":57228,"title":"再随便发一个","novelId":2849},{"id":57229,"title":"再发一次试试","novelId":2849},{"id":57230,"title":"来来来","novelId":2849},{"id":57231,"title":"消息免打扰也能收到","novelId":2849},{"id":57235,"title":"11111","novelId":2849},{"id":57237,"title":"11223366","novelId":2849},{"id":57240,"title":"mgdgdjd","novelId":2849}]}]
         * novelTitle : 你若安好,便是晴天
         * novelId : 2849
         */

        private String novelTitle;
        private String novelId;
        private List<VolumesBean> volumes;

        public String getNovelTitle() {
            return novelTitle;
        }

        public void setNovelTitle(String novelTitle) {
            this.novelTitle = novelTitle;
        }

        public String getNovelId() {
            return novelId;
        }

        public void setNovelId(String novelId) {
            this.novelId = novelId;
        }

        public List<VolumesBean> getVolumes() {
            return volumes;
        }

        public void setVolumes(List<VolumesBean> volumes) {
            this.volumes = volumes;
        }

        public static class VolumesBean  extends AbstractExpandableItem<DataBean.VolumesBean.ChaptersBean> implements Serializable,MultiItemEntity {
            /**
             * title : 作品相关
             * chapters : [{"id":57081,"title":"内容简介1","novelId":2849},{"id":57083,"title":"内容简介2","novelId":2849},{"id":57113,"title":"005 青春初识","novelId":2849},{"id":57114,"title":"006 青春初识2","novelId":2849},{"id":57118,"title":"作品相关哦","novelId":2849},{"id":57126,"title":"测试下缓存","novelId":2849},{"id":57145,"title":"测试PC字数","novelId":2849}]
             */

            @Override
            public int getLevel() {
                return 1;
            }

            @Override
            public int getItemType() {
                return BookCatalogExpandableItemAdapter.VOLUME;
            }

            private String title;
            private List<ChaptersBean> chapters;

            public String getTitle() {
                return title;
            }

            public void setTitle(String title) {
                this.title = title;
            }

            public List<ChaptersBean> getChapters() {
                return chapters;
            }

            public void setChapters(List<ChaptersBean> chapters) {
                this.chapters = chapters;
            }

            public static class ChaptersBean implements Serializable,MultiItemEntity{
                @Override
                public int getItemType() {
                    return BookCatalogExpandableItemAdapter.CHAPTER;
                }
                /**
                 * id : 57081
                 * title : 内容简介1
                 * novelId : 2849
                 */

                private int id;
                private String title;
                private int novelId;

                private String url;

                public String getUrl() {
                    return url;
                }

                public void setUrl(String url) {
                    this.url = url;
                }

                private boolean isVip;

                public boolean isVip() {
                    return isVip;
                }

                public void setVip(boolean vip) {
                    isVip = vip;
                }

                public int getId() {
                    return id;
                }

                private int chapterPos;

                public int getChapterPos() {
                    return chapterPos;
                }

                public void setChapterPos(int chapterPos) {
                    this.chapterPos = chapterPos;
                }


                public void setId(int id) {
                    this.id = id;
                }

                public String getTitle() {
                    return title;
                }

                public void setTitle(String title) {
                    this.title = title;
                }

                public int getNovelId() {
                    return novelId;
                }

                public void setNovelId(int novelId) {
                    this.novelId = novelId;
                }
            }
        }
    }
}
