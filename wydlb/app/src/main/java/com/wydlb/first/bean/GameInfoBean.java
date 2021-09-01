package com.wydlb.first.bean;

import java.util.List;

/**
 * Copyright (C), 2018
 * FileName: GameInfoBean
 * Author: lrz
 * Date: 2021/8/29 10:39
 * Description: ${DESCRIPTION}
 */
public class GameInfoBean extends BaseBean{

    private String module;
    private DataBean data;

    public static class DataBean {
        private String id;
        private Integer company_size;
        private Integer employee_count;
        private Integer leisure_count;
        private Integer funds;
        private Integer games_number;
        private Integer maximum_rounds;
        private Integer current_rounds;
        private String investment_month;
        private String games_status;
        private Integer superposition_ratio;
        private Integer buy_rounds;
        private List<InvestmentsBean> investments;
        private List<SkillsBean> skills;


        public static class InvestmentsBean {
            private String id;
            private String project_name;
            private String project_code;
            private Integer project_max;
            private Integer risk_level;
            private Integer profit_amount;
            private Integer people_count;
            private Integer investment_total;
            private String investment_month;
            private Integer prediction_amount;
            private String investment_status;

            public String getId() {
                return id;
            }

            public void setId(String id) {
                this.id = id;
            }

            public String getProject_name() {
                return project_name;
            }

            public void setProject_name(String project_name) {
                this.project_name = project_name;
            }

            public String getProject_code() {
                return project_code;
            }

            public void setProject_code(String project_code) {
                this.project_code = project_code;
            }

            public Integer getProject_max() {
                return project_max;
            }

            public void setProject_max(Integer project_max) {
                this.project_max = project_max;
            }

            public Integer getRisk_level() {
                return risk_level;
            }

            public void setRisk_level(Integer risk_level) {
                this.risk_level = risk_level;
            }

            public Integer getProfit_amount() {
                return profit_amount;
            }

            public void setProfit_amount(Integer profit_amount) {
                this.profit_amount = profit_amount;
            }

            public Integer getPeople_count() {
                return people_count;
            }

            public void setPeople_count(Integer people_count) {
                this.people_count = people_count;
            }

            public Integer getInvestment_total() {
                return investment_total;
            }

            public void setInvestment_total(Integer investment_total) {
                this.investment_total = investment_total;
            }

            public String getInvestment_month() {
                return investment_month;
            }

            public void setInvestment_month(String investment_month) {
                this.investment_month = investment_month;
            }

            public Integer getPrediction_amount() {
                return prediction_amount;
            }

            public void setPrediction_amount(Integer prediction_amount) {
                this.prediction_amount = prediction_amount;
            }

            public String getInvestment_status() {
                return investment_status;
            }

            public void setInvestment_status(String investment_status) {
                this.investment_status = investment_status;
            }
        }

        public static class SkillsBean {
            private String id;
            private String skill_name;
            private String skill_code;
            private String skill_type;
            private Integer skill_level;
            private String skill_introduce;
            private String skill_icon;
            private Integer bonus_bonus;
            private Integer loss_bonus;
            private List<SkillNoteBean> skill_note;

            public static class SkillNoteBean {
                private String name;
                private Integer skill_level;
                private String skill_condition;
                private String time;

                public String getName() {
                    return name;
                }

                public void setName(String name) {
                    this.name = name;
                }

                public Integer getSkill_level() {
                    return skill_level;
                }

                public void setSkill_level(Integer skill_level) {
                    this.skill_level = skill_level;
                }

                public String getSkill_condition() {
                    return skill_condition;
                }

                public void setSkill_condition(String skill_condition) {
                    this.skill_condition = skill_condition;
                }

                public String getTime() {
                    return time;
                }

                public void setTime(String time) {
                    this.time = time;
                }
            }

            public String getId() {
                return id;
            }

            public void setId(String id) {
                this.id = id;
            }

            public String getSkill_name() {
                return skill_name;
            }

            public void setSkill_name(String skill_name) {
                this.skill_name = skill_name;
            }

            public String getSkill_code() {
                return skill_code;
            }

            public void setSkill_code(String skill_code) {
                this.skill_code = skill_code;
            }

            public String getSkill_type() {
                return skill_type;
            }

            public void setSkill_type(String skill_type) {
                this.skill_type = skill_type;
            }

            public Integer getSkill_level() {
                return skill_level;
            }

            public void setSkill_level(Integer skill_level) {
                this.skill_level = skill_level;
            }

            public String getSkill_introduce() {
                return skill_introduce;
            }

            public void setSkill_introduce(String skill_introduce) {
                this.skill_introduce = skill_introduce;
            }

            public String getSkill_icon() {
                return skill_icon;
            }

            public void setSkill_icon(String skill_icon) {
                this.skill_icon = skill_icon;
            }

            public Integer getBonus_bonus() {
                return bonus_bonus;
            }

            public void setBonus_bonus(Integer bonus_bonus) {
                this.bonus_bonus = bonus_bonus;
            }

            public Integer getLoss_bonus() {
                return loss_bonus;
            }

            public void setLoss_bonus(Integer loss_bonus) {
                this.loss_bonus = loss_bonus;
            }

            public List<SkillNoteBean> getSkill_note() {
                return skill_note;
            }

            public void setSkill_note(List<SkillNoteBean> skill_note) {
                this.skill_note = skill_note;
            }
        }

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public Integer getCompany_size() {
            return company_size;
        }

        public void setCompany_size(Integer company_size) {
            this.company_size = company_size;
        }

        public Integer getEmployee_count() {
            return employee_count;
        }

        public void setEmployee_count(Integer employee_count) {
            this.employee_count = employee_count;
        }

        public Integer getLeisure_count() {
            return leisure_count;
        }

        public void setLeisure_count(Integer leisure_count) {
            this.leisure_count = leisure_count;
        }

        public Integer getFunds() {
            return funds;
        }

        public void setFunds(Integer funds) {
            this.funds = funds;
        }

        public Integer getGames_number() {
            return games_number;
        }

        public void setGames_number(Integer games_number) {
            this.games_number = games_number;
        }

        public Integer getMaximum_rounds() {
            return maximum_rounds;
        }

        public void setMaximum_rounds(Integer maximum_rounds) {
            this.maximum_rounds = maximum_rounds;
        }

        public Integer getCurrent_rounds() {
            return current_rounds;
        }

        public void setCurrent_rounds(Integer current_rounds) {
            this.current_rounds = current_rounds;
        }

        public String getInvestment_month() {
            return investment_month;
        }

        public void setInvestment_month(String investment_month) {
            this.investment_month = investment_month;
        }

        public String getGames_status() {
            return games_status;
        }

        public void setGames_status(String games_status) {
            this.games_status = games_status;
        }

        public Integer getSuperposition_ratio() {
            return superposition_ratio;
        }

        public void setSuperposition_ratio(Integer superposition_ratio) {
            this.superposition_ratio = superposition_ratio;
        }

        public Integer getBuy_rounds() {
            return buy_rounds;
        }

        public void setBuy_rounds(Integer buy_rounds) {
            this.buy_rounds = buy_rounds;
        }

        public List<InvestmentsBean> getInvestments() {
            return investments;
        }

        public void setInvestments(List<InvestmentsBean> investments) {
            this.investments = investments;
        }

        public List<SkillsBean> getSkills() {
            return skills;
        }

        public void setSkills(List<SkillsBean> skills) {
            this.skills = skills;
        }
    }

    public String getModule() {
        return module;
    }

    public void setModule(String module) {
        this.module = module;
    }

    public DataBean getData() {
        return data;
    }

    public void setData(DataBean data) {
        this.data = data;
    }
}
