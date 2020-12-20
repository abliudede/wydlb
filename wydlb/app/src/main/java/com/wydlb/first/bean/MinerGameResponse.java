package com.wydlb.first.bean;

import java.util.List;

/**
 * Created by lrz on 2018/2/6.
 */

public class MinerGameResponse {

    /**
     * status_code : 200
     * message : 信息拉取成功
     * data : {"userInfo":{"avatar":"https://wx.qlogo.cn/mmopen/vi_32/09ibAEMM4ct04NyXeWlcrjIUK6cSEm1BYqeh5QwYRse2eacMlUq3foZwPvSCjf6NnsFoLT4VZX4JbkNEpoFGvbg/0","nickName":"Qing","mineLevel":1,"uid":16660},"contentList":[],"miningList":[],"headerNum":{"mining":{"current":"1040","all":"60482"},"chilun":{"current":"50"},"probe":{"current":0,"all":4}},"accountMineList":[{"quantity":"12.151","display":1,"mineId":4824412},{"quantity":"12.135","display":1,"mineId":4840946},{"quantity":"12.120","display":1,"mineId":4874167},{"quantity":"12.084","display":1,"mineId":4890740},{"quantity":"17.992","display":1,"mineId":4907529},{"quantity":"17.939","display":1,"mineId":4924227},{"quantity":"17.778","display":1,"mineId":4940911},{"quantity":"17.628","display":1,"mineId":4957599},{"quantity":"17.324","display":1,"mineId":4990970},{"quantity":"17.205","display":1,"mineId":5007570},{"quantity":"17.066","display":1,"mineId":5024215},{"quantity":"16.783","display":1,"mineId":5057461},{"quantity":"16.617","display":1,"mineId":5090669},{"quantity":"12.903","display":1,"mineId":5107218},{"quantity":"12.834","display":1,"mineId":5123824},{"quantity":"13.003","display":1,"mineId":5140476},{"quantity":"12.653","display":1,"mineId":5157067},{"quantity":"12.674","display":1,"mineId":5173744},{"quantity":"12.615","display":1,"mineId":5190371},{"quantity":"12.523","display":1,"mineId":5207038},{"quantity":"12.394","display":1,"mineId":5223809},{"quantity":"12.317","display":1,"mineId":5240496},{"quantity":"12.254","display":1,"mineId":5257069},{"quantity":"12.166","display":1,"mineId":5273732},{"quantity":"12.048","display":1,"mineId":5290538},{"quantity":"13.560","display":1,"mineId":5307488},{"quantity":"13.456","display":1,"mineId":5324192},{"quantity":"13.349","display":1,"mineId":5340877},{"quantity":"13.136","display":1,"mineId":5357835},{"quantity":"12.940","display":1,"mineId":5374774},{"quantity":"12.737","display":1,"mineId":5391762},{"quantity":"12.603","display":1,"mineId":5408558},{"quantity":"12.427","display":1,"mineId":5425480},{"quantity":"12.290","display":1,"mineId":5442376},{"quantity":"12.099","display":1,"mineId":5459443},{"quantity":"11.899","display":1,"mineId":5476549},{"quantity":"11.752","display":1,"mineId":5493508},{"quantity":"20.389","display":1,"mineId":5510285},{"quantity":"20.189","display":1,"mineId":5527097},{"quantity":"19.982","display":1,"mineId":5543967},{"quantity":"19.765","display":1,"mineId":5560848},{"quantity":"19.535","display":1,"mineId":5577762},{"quantity":"19.337","display":1,"mineId":5594600},{"quantity":"13.679","display":1,"mineId":5611446},{"quantity":"13.527","display":1,"mineId":5628322},{"quantity":"13.335","display":1,"mineId":5645397},{"quantity":"13.133","display":1,"mineId":5662545},{"quantity":"13.026","display":1,"mineId":5679388},{"quantity":"12.932","display":1,"mineId":5712745},{"quantity":"15.330","display":1,"mineId":5746158},{"quantity":"15.146","display":1,"mineId":5779504},{"quantity":"15.022","display":1,"mineId":5812713},{"quantity":"14.894","display":1,"mineId":5845952},{"quantity":"14.745","display":1,"mineId":5879279},{"quantity":"14.658","display":1,"mineId":5912374},{"quantity":"14.584","display":1,"mineId":5945473},{"quantity":"14.482","display":1,"mineId":5978630},{"quantity":"14.397","display":1,"mineId":6011737},{"quantity":"14.338","display":1,"mineId":6044778},{"quantity":"14.273","display":1,"mineId":6077878},{"quantity":"14.198","display":1,"mineId":6110914},{"quantity":"11.582","display":1,"mineId":6659724},{"quantity":"11.454","display":1,"mineId":6676221},{"quantity":"11.328","display":1,"mineId":6692787},{"quantity":"11.162","display":1,"mineId":6709513},{"quantity":"11.017","display":1,"mineId":6726201},{"quantity":"10.889","display":1,"mineId":6742852},{"quantity":"10.825","display":1,"mineId":6759331},{"quantity":"10.703","display":1,"mineId":6775997},{"quantity":"10.609","display":1,"mineId":6792575},{"quantity":"10.482","display":1,"mineId":6809280},{"quantity":"10.382","display":1,"mineId":6825863},{"quantity":"10.276","display":1,"mineId":6842515},{"quantity":"8.722","display":1,"mineId":6859125},{"quantity":"8.618","display":1,"mineId":6875881},{"quantity":"8.514","display":1,"mineId":6892610},{"quantity":"8.400","display":1,"mineId":6909480},{"quantity":"8.289","display":1,"mineId":6926334},{"quantity":"8.228","display":1,"mineId":6942981},{"quantity":"8.157","display":1,"mineId":6959654},{"quantity":"8.090","display":1,"mineId":6976314},{"quantity":"8.005","display":1,"mineId":6993075},{"quantity":"7.923","display":1,"mineId":7009845},{"quantity":"7.840","display":1,"mineId":7026648},{"quantity":"7.765","display":1,"mineId":7043380},{"quantity":"11.596","display":1,"mineId":7059936},{"quantity":"11.572","display":1,"mineId":7076475},{"quantity":"11.499","display":1,"mineId":7093177},{"quantity":"11.424","display":1,"mineId":7109854},{"quantity":"11.377","display":1,"mineId":7126460},{"quantity":"11.340","display":1,"mineId":7143042},{"quantity":"11.310","display":1,"mineId":7159564},{"quantity":"11.265","display":1,"mineId":7176154},{"quantity":"11.234","display":1,"mineId":7192710},{"quantity":"11.197","display":1,"mineId":7209291},{"quantity":"11.139","display":1,"mineId":7225866},{"quantity":"11.083","display":1,"mineId":7242459},{"quantity":"8.571","display":1,"mineId":7259123},{"quantity":"8.536","display":1,"mineId":7275693},{"quantity":"8.512","display":1,"mineId":7292202},{"quantity":"8.470","display":1,"mineId":7308908},{"quantity":"8.394","display":1,"mineId":7325757},{"quantity":"8.356","display":1,"mineId":7342401},{"quantity":"8.339","display":1,"mineId":7358906},{"quantity":"8.337","display":1,"mineId":7375381}]}
     */

    private int status_code;
    private String message;
    private DataBean data;

    public int getStatus_code() {
        return status_code;
    }

    public void setStatus_code(int status_code) {
        this.status_code = status_code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public DataBean getData() {
        return data;
    }

    public void setData(DataBean data) {
        this.data = data;
    }

    public static class DataBean {
        /**
         * userInfo : {"avatar":"https://wx.qlogo.cn/mmopen/vi_32/09ibAEMM4ct04NyXeWlcrjIUK6cSEm1BYqeh5QwYRse2eacMlUq3foZwPvSCjf6NnsFoLT4VZX4JbkNEpoFGvbg/0","nickName":"Qing","mineLevel":1,"uid":16660}
         * contentList : []
         * miningList : []
         * headerNum : {"mining":{"current":"1040","all":"60482"},"chilun":{"current":"50"},"probe":{"current":0,"all":4}}
         * accountMineList : [{"quantity":"12.151","display":1,"mineId":4824412},{"quantity":"12.135","display":1,"mineId":4840946},{"quantity":"12.120","display":1,"mineId":4874167},{"quantity":"12.084","display":1,"mineId":4890740},{"quantity":"17.992","display":1,"mineId":4907529},{"quantity":"17.939","display":1,"mineId":4924227},{"quantity":"17.778","display":1,"mineId":4940911},{"quantity":"17.628","display":1,"mineId":4957599},{"quantity":"17.324","display":1,"mineId":4990970},{"quantity":"17.205","display":1,"mineId":5007570},{"quantity":"17.066","display":1,"mineId":5024215},{"quantity":"16.783","display":1,"mineId":5057461},{"quantity":"16.617","display":1,"mineId":5090669},{"quantity":"12.903","display":1,"mineId":5107218},{"quantity":"12.834","display":1,"mineId":5123824},{"quantity":"13.003","display":1,"mineId":5140476},{"quantity":"12.653","display":1,"mineId":5157067},{"quantity":"12.674","display":1,"mineId":5173744},{"quantity":"12.615","display":1,"mineId":5190371},{"quantity":"12.523","display":1,"mineId":5207038},{"quantity":"12.394","display":1,"mineId":5223809},{"quantity":"12.317","display":1,"mineId":5240496},{"quantity":"12.254","display":1,"mineId":5257069},{"quantity":"12.166","display":1,"mineId":5273732},{"quantity":"12.048","display":1,"mineId":5290538},{"quantity":"13.560","display":1,"mineId":5307488},{"quantity":"13.456","display":1,"mineId":5324192},{"quantity":"13.349","display":1,"mineId":5340877},{"quantity":"13.136","display":1,"mineId":5357835},{"quantity":"12.940","display":1,"mineId":5374774},{"quantity":"12.737","display":1,"mineId":5391762},{"quantity":"12.603","display":1,"mineId":5408558},{"quantity":"12.427","display":1,"mineId":5425480},{"quantity":"12.290","display":1,"mineId":5442376},{"quantity":"12.099","display":1,"mineId":5459443},{"quantity":"11.899","display":1,"mineId":5476549},{"quantity":"11.752","display":1,"mineId":5493508},{"quantity":"20.389","display":1,"mineId":5510285},{"quantity":"20.189","display":1,"mineId":5527097},{"quantity":"19.982","display":1,"mineId":5543967},{"quantity":"19.765","display":1,"mineId":5560848},{"quantity":"19.535","display":1,"mineId":5577762},{"quantity":"19.337","display":1,"mineId":5594600},{"quantity":"13.679","display":1,"mineId":5611446},{"quantity":"13.527","display":1,"mineId":5628322},{"quantity":"13.335","display":1,"mineId":5645397},{"quantity":"13.133","display":1,"mineId":5662545},{"quantity":"13.026","display":1,"mineId":5679388},{"quantity":"12.932","display":1,"mineId":5712745},{"quantity":"15.330","display":1,"mineId":5746158},{"quantity":"15.146","display":1,"mineId":5779504},{"quantity":"15.022","display":1,"mineId":5812713},{"quantity":"14.894","display":1,"mineId":5845952},{"quantity":"14.745","display":1,"mineId":5879279},{"quantity":"14.658","display":1,"mineId":5912374},{"quantity":"14.584","display":1,"mineId":5945473},{"quantity":"14.482","display":1,"mineId":5978630},{"quantity":"14.397","display":1,"mineId":6011737},{"quantity":"14.338","display":1,"mineId":6044778},{"quantity":"14.273","display":1,"mineId":6077878},{"quantity":"14.198","display":1,"mineId":6110914},{"quantity":"11.582","display":1,"mineId":6659724},{"quantity":"11.454","display":1,"mineId":6676221},{"quantity":"11.328","display":1,"mineId":6692787},{"quantity":"11.162","display":1,"mineId":6709513},{"quantity":"11.017","display":1,"mineId":6726201},{"quantity":"10.889","display":1,"mineId":6742852},{"quantity":"10.825","display":1,"mineId":6759331},{"quantity":"10.703","display":1,"mineId":6775997},{"quantity":"10.609","display":1,"mineId":6792575},{"quantity":"10.482","display":1,"mineId":6809280},{"quantity":"10.382","display":1,"mineId":6825863},{"quantity":"10.276","display":1,"mineId":6842515},{"quantity":"8.722","display":1,"mineId":6859125},{"quantity":"8.618","display":1,"mineId":6875881},{"quantity":"8.514","display":1,"mineId":6892610},{"quantity":"8.400","display":1,"mineId":6909480},{"quantity":"8.289","display":1,"mineId":6926334},{"quantity":"8.228","display":1,"mineId":6942981},{"quantity":"8.157","display":1,"mineId":6959654},{"quantity":"8.090","display":1,"mineId":6976314},{"quantity":"8.005","display":1,"mineId":6993075},{"quantity":"7.923","display":1,"mineId":7009845},{"quantity":"7.840","display":1,"mineId":7026648},{"quantity":"7.765","display":1,"mineId":7043380},{"quantity":"11.596","display":1,"mineId":7059936},{"quantity":"11.572","display":1,"mineId":7076475},{"quantity":"11.499","display":1,"mineId":7093177},{"quantity":"11.424","display":1,"mineId":7109854},{"quantity":"11.377","display":1,"mineId":7126460},{"quantity":"11.340","display":1,"mineId":7143042},{"quantity":"11.310","display":1,"mineId":7159564},{"quantity":"11.265","display":1,"mineId":7176154},{"quantity":"11.234","display":1,"mineId":7192710},{"quantity":"11.197","display":1,"mineId":7209291},{"quantity":"11.139","display":1,"mineId":7225866},{"quantity":"11.083","display":1,"mineId":7242459},{"quantity":"8.571","display":1,"mineId":7259123},{"quantity":"8.536","display":1,"mineId":7275693},{"quantity":"8.512","display":1,"mineId":7292202},{"quantity":"8.470","display":1,"mineId":7308908},{"quantity":"8.394","display":1,"mineId":7325757},{"quantity":"8.356","display":1,"mineId":7342401},{"quantity":"8.339","display":1,"mineId":7358906},{"quantity":"8.337","display":1,"mineId":7375381}]
         */

        private UserInfoBean userInfo;
        private HeaderNumBean headerNum;
        private List<?> contentList;
        private List<?> miningList;
        private List<AccountMineListBean> accountMineList;

        public UserInfoBean getUserInfo() {
            return userInfo;
        }

        public void setUserInfo(UserInfoBean userInfo) {
            this.userInfo = userInfo;
        }

        public HeaderNumBean getHeaderNum() {
            return headerNum;
        }

        public void setHeaderNum(HeaderNumBean headerNum) {
            this.headerNum = headerNum;
        }

        public List<?> getContentList() {
            return contentList;
        }

        public void setContentList(List<?> contentList) {
            this.contentList = contentList;
        }

        public List<?> getMiningList() {
            return miningList;
        }

        public void setMiningList(List<?> miningList) {
            this.miningList = miningList;
        }

        public List<AccountMineListBean> getAccountMineList() {
            return accountMineList;
        }

        public void setAccountMineList(List<AccountMineListBean> accountMineList) {
            this.accountMineList = accountMineList;
        }

        public static class UserInfoBean {
            /**
             * avatar : https://wx.qlogo.cn/mmopen/vi_32/09ibAEMM4ct04NyXeWlcrjIUK6cSEm1BYqeh5QwYRse2eacMlUq3foZwPvSCjf6NnsFoLT4VZX4JbkNEpoFGvbg/0
             * nickName : Qing
             * mineLevel : 1
             * uid : 16660
             */

            private String avatar;
            private String nickName;
            private int mineLevel;
            private int uid;

            public String getAvatar() {
                return avatar;
            }

            public void setAvatar(String avatar) {
                this.avatar = avatar;
            }

            public String getNickName() {
                return nickName;
            }

            public void setNickName(String nickName) {
                this.nickName = nickName;
            }

            public int getMineLevel() {
                return mineLevel;
            }

            public void setMineLevel(int mineLevel) {
                this.mineLevel = mineLevel;
            }

            public int getUid() {
                return uid;
            }

            public void setUid(int uid) {
                this.uid = uid;
            }
        }

        public static class HeaderNumBean {
            /**
             * mining : {"current":"1040","all":"60482"}
             * chilun : {"current":"50"}
             * probe : {"current":0,"all":4}
             */

            private MiningBean mining;
            private ChilunBean chilun;
            private ProbeBean probe;

            public MiningBean getMining() {
                return mining;
            }

            public void setMining(MiningBean mining) {
                this.mining = mining;
            }

            public ChilunBean getChilun() {
                return chilun;
            }

            public void setChilun(ChilunBean chilun) {
                this.chilun = chilun;
            }

            public ProbeBean getProbe() {
                return probe;
            }

            public void setProbe(ProbeBean probe) {
                this.probe = probe;
            }

            public static class MiningBean {
                /**
                 * current : 1040
                 * all : 60482
                 */

                private String current;
                private String all;

                public String getCurrent() {
                    return current;
                }

                public void setCurrent(String current) {
                    this.current = current;
                }

                public String getAll() {
                    return all;
                }

                public void setAll(String all) {
                    this.all = all;
                }
            }

            public static class ChilunBean {
                /**
                 * current : 50
                 */

                private String current;

                public String getCurrent() {
                    return current;
                }

                public void setCurrent(String current) {
                    this.current = current;
                }
            }

            public static class ProbeBean {
                /**
                 * current : 0
                 * all : 4
                 */

                private int current;
                private int all;

                public int getCurrent() {
                    return current;
                }

                public void setCurrent(int current) {
                    this.current = current;
                }

                public int getAll() {
                    return all;
                }

                public void setAll(int all) {
                    this.all = all;
                }
            }
        }

        public static class AccountMineListBean {
            /**
             * quantity : 12.151
             * display : 1
             * mineId : 4824412
             */

            private String quantity;
            private int display;
            private int mineId;

            public String getQuantity() {
                return quantity;
            }

            public void setQuantity(String quantity) {
                this.quantity = quantity;
            }

            public int getDisplay() {
                return display;
            }

            public void setDisplay(int display) {
                this.display = display;
            }

            public int getMineId() {
                return mineId;
            }

            public void setMineId(int mineId) {
                this.mineId = mineId;
            }
        }
    }
}
