package com.feiqu.system.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class ThoughtExample implements Serializable {
    protected String orderByClause;

    protected boolean distinct;

    protected List<Criteria> oredCriteria;

    private static final long serialVersionUID = 1L;

    public ThoughtExample() {
        oredCriteria = new ArrayList<Criteria>();
    }

    public void setOrderByClause(String orderByClause) {
        this.orderByClause = orderByClause;
    }

    public String getOrderByClause() {
        return orderByClause;
    }

    public void setDistinct(boolean distinct) {
        this.distinct = distinct;
    }

    public boolean isDistinct() {
        return distinct;
    }

    public List<Criteria> getOredCriteria() {
        return oredCriteria;
    }

    public void or(Criteria criteria) {
        oredCriteria.add(criteria);
    }

    public Criteria or() {
        Criteria criteria = createCriteriaInternal();
        oredCriteria.add(criteria);
        return criteria;
    }

    public Criteria createCriteria() {
        Criteria criteria = createCriteriaInternal();
        if (oredCriteria.size() == 0) {
            oredCriteria.add(criteria);
        }
        return criteria;
    }

    protected Criteria createCriteriaInternal() {
        Criteria criteria = new Criteria();
        return criteria;
    }

    public void clear() {
        oredCriteria.clear();
        orderByClause = null;
        distinct = false;
    }

    protected abstract static class GeneratedCriteria implements Serializable {
        protected List<Criterion> criteria;

        protected GeneratedCriteria() {
            super();
            criteria = new ArrayList<Criterion>();
        }

        public boolean isValid() {
            return criteria.size() > 0;
        }

        public List<Criterion> getAllCriteria() {
            return criteria;
        }

        public List<Criterion> getCriteria() {
            return criteria;
        }

        protected void addCriterion(String condition) {
            if (condition == null) {
                throw new RuntimeException("Value for condition cannot be null");
            }
            criteria.add(new Criterion(condition));
        }

        protected void addCriterion(String condition, Object value, String property) {
            if (value == null) {
                throw new RuntimeException("Value for " + property + " cannot be null");
            }
            criteria.add(new Criterion(condition, value));
        }

        protected void addCriterion(String condition, Object value1, Object value2, String property) {
            if (value1 == null || value2 == null) {
                throw new RuntimeException("Between values for " + property + " cannot be null");
            }
            criteria.add(new Criterion(condition, value1, value2));
        }

        public Criteria andIdIsNull() {
            addCriterion("id is null");
            return (Criteria) this;
        }

        public Criteria andIdIsNotNull() {
            addCriterion("id is not null");
            return (Criteria) this;
        }

        public Criteria andIdEqualTo(Integer value) {
            addCriterion("id =", value, "id");
            return (Criteria) this;
        }

        public Criteria andIdNotEqualTo(Integer value) {
            addCriterion("id <>", value, "id");
            return (Criteria) this;
        }

        public Criteria andIdGreaterThan(Integer value) {
            addCriterion("id >", value, "id");
            return (Criteria) this;
        }

        public Criteria andIdGreaterThanOrEqualTo(Integer value) {
            addCriterion("id >=", value, "id");
            return (Criteria) this;
        }

        public Criteria andIdLessThan(Integer value) {
            addCriterion("id <", value, "id");
            return (Criteria) this;
        }

        public Criteria andIdLessThanOrEqualTo(Integer value) {
            addCriterion("id <=", value, "id");
            return (Criteria) this;
        }

        public Criteria andIdIn(List<Integer> values) {
            addCriterion("id in", values, "id");
            return (Criteria) this;
        }

        public Criteria andIdNotIn(List<Integer> values) {
            addCriterion("id not in", values, "id");
            return (Criteria) this;
        }

        public Criteria andIdBetween(Integer value1, Integer value2) {
            addCriterion("id between", value1, value2, "id");
            return (Criteria) this;
        }

        public Criteria andIdNotBetween(Integer value1, Integer value2) {
            addCriterion("id not between", value1, value2, "id");
            return (Criteria) this;
        }

        public Criteria andThoughtContentIsNull() {
            addCriterion("thought_content is null");
            return (Criteria) this;
        }

        public Criteria andThoughtContentIsNotNull() {
            addCriterion("thought_content is not null");
            return (Criteria) this;
        }

        public Criteria andThoughtContentEqualTo(String value) {
            addCriterion("thought_content =", value, "thoughtContent");
            return (Criteria) this;
        }

        public Criteria andThoughtContentNotEqualTo(String value) {
            addCriterion("thought_content <>", value, "thoughtContent");
            return (Criteria) this;
        }

        public Criteria andThoughtContentGreaterThan(String value) {
            addCriterion("thought_content >", value, "thoughtContent");
            return (Criteria) this;
        }

        public Criteria andThoughtContentGreaterThanOrEqualTo(String value) {
            addCriterion("thought_content >=", value, "thoughtContent");
            return (Criteria) this;
        }

        public Criteria andThoughtContentLessThan(String value) {
            addCriterion("thought_content <", value, "thoughtContent");
            return (Criteria) this;
        }

        public Criteria andThoughtContentLessThanOrEqualTo(String value) {
            addCriterion("thought_content <=", value, "thoughtContent");
            return (Criteria) this;
        }

        public Criteria andThoughtContentLike(String value) {
            addCriterion("thought_content like", value, "thoughtContent");
            return (Criteria) this;
        }

        public Criteria andThoughtContentNotLike(String value) {
            addCriterion("thought_content not like", value, "thoughtContent");
            return (Criteria) this;
        }

        public Criteria andThoughtContentIn(List<String> values) {
            addCriterion("thought_content in", values, "thoughtContent");
            return (Criteria) this;
        }

        public Criteria andThoughtContentNotIn(List<String> values) {
            addCriterion("thought_content not in", values, "thoughtContent");
            return (Criteria) this;
        }

        public Criteria andThoughtContentBetween(String value1, String value2) {
            addCriterion("thought_content between", value1, value2, "thoughtContent");
            return (Criteria) this;
        }

        public Criteria andThoughtContentNotBetween(String value1, String value2) {
            addCriterion("thought_content not between", value1, value2, "thoughtContent");
            return (Criteria) this;
        }

        public Criteria andCreateTimeIsNull() {
            addCriterion("create_time is null");
            return (Criteria) this;
        }

        public Criteria andCreateTimeIsNotNull() {
            addCriterion("create_time is not null");
            return (Criteria) this;
        }

        public Criteria andCreateTimeEqualTo(Date value) {
            addCriterion("create_time =", value, "createTime");
            return (Criteria) this;
        }

        public Criteria andCreateTimeNotEqualTo(Date value) {
            addCriterion("create_time <>", value, "createTime");
            return (Criteria) this;
        }

        public Criteria andCreateTimeGreaterThan(Date value) {
            addCriterion("create_time >", value, "createTime");
            return (Criteria) this;
        }

        public Criteria andCreateTimeGreaterThanOrEqualTo(Date value) {
            addCriterion("create_time >=", value, "createTime");
            return (Criteria) this;
        }

        public Criteria andCreateTimeLessThan(Date value) {
            addCriterion("create_time <", value, "createTime");
            return (Criteria) this;
        }

        public Criteria andCreateTimeLessThanOrEqualTo(Date value) {
            addCriterion("create_time <=", value, "createTime");
            return (Criteria) this;
        }

        public Criteria andCreateTimeIn(List<Date> values) {
            addCriterion("create_time in", values, "createTime");
            return (Criteria) this;
        }

        public Criteria andCreateTimeNotIn(List<Date> values) {
            addCriterion("create_time not in", values, "createTime");
            return (Criteria) this;
        }

        public Criteria andCreateTimeBetween(Date value1, Date value2) {
            addCriterion("create_time between", value1, value2, "createTime");
            return (Criteria) this;
        }

        public Criteria andCreateTimeNotBetween(Date value1, Date value2) {
            addCriterion("create_time not between", value1, value2, "createTime");
            return (Criteria) this;
        }

        public Criteria andUserIdIsNull() {
            addCriterion("user_id is null");
            return (Criteria) this;
        }

        public Criteria andUserIdIsNotNull() {
            addCriterion("user_id is not null");
            return (Criteria) this;
        }

        public Criteria andUserIdEqualTo(Integer value) {
            addCriterion("user_id =", value, "userId");
            return (Criteria) this;
        }

        public Criteria andUserIdNotEqualTo(Integer value) {
            addCriterion("user_id <>", value, "userId");
            return (Criteria) this;
        }

        public Criteria andUserIdGreaterThan(Integer value) {
            addCriterion("user_id >", value, "userId");
            return (Criteria) this;
        }

        public Criteria andUserIdGreaterThanOrEqualTo(Integer value) {
            addCriterion("user_id >=", value, "userId");
            return (Criteria) this;
        }

        public Criteria andUserIdLessThan(Integer value) {
            addCriterion("user_id <", value, "userId");
            return (Criteria) this;
        }

        public Criteria andUserIdLessThanOrEqualTo(Integer value) {
            addCriterion("user_id <=", value, "userId");
            return (Criteria) this;
        }

        public Criteria andUserIdIn(List<Integer> values) {
            addCriterion("user_id in", values, "userId");
            return (Criteria) this;
        }

        public Criteria andUserIdNotIn(List<Integer> values) {
            addCriterion("user_id not in", values, "userId");
            return (Criteria) this;
        }

        public Criteria andUserIdBetween(Integer value1, Integer value2) {
            addCriterion("user_id between", value1, value2, "userId");
            return (Criteria) this;
        }

        public Criteria andUserIdNotBetween(Integer value1, Integer value2) {
            addCriterion("user_id not between", value1, value2, "userId");
            return (Criteria) this;
        }

        public Criteria andLikeCountIsNull() {
            addCriterion("like_count is null");
            return (Criteria) this;
        }

        public Criteria andLikeCountIsNotNull() {
            addCriterion("like_count is not null");
            return (Criteria) this;
        }

        public Criteria andLikeCountEqualTo(Integer value) {
            addCriterion("like_count =", value, "likeCount");
            return (Criteria) this;
        }

        public Criteria andLikeCountNotEqualTo(Integer value) {
            addCriterion("like_count <>", value, "likeCount");
            return (Criteria) this;
        }

        public Criteria andLikeCountGreaterThan(Integer value) {
            addCriterion("like_count >", value, "likeCount");
            return (Criteria) this;
        }

        public Criteria andLikeCountGreaterThanOrEqualTo(Integer value) {
            addCriterion("like_count >=", value, "likeCount");
            return (Criteria) this;
        }

        public Criteria andLikeCountLessThan(Integer value) {
            addCriterion("like_count <", value, "likeCount");
            return (Criteria) this;
        }

        public Criteria andLikeCountLessThanOrEqualTo(Integer value) {
            addCriterion("like_count <=", value, "likeCount");
            return (Criteria) this;
        }

        public Criteria andLikeCountIn(List<Integer> values) {
            addCriterion("like_count in", values, "likeCount");
            return (Criteria) this;
        }

        public Criteria andLikeCountNotIn(List<Integer> values) {
            addCriterion("like_count not in", values, "likeCount");
            return (Criteria) this;
        }

        public Criteria andLikeCountBetween(Integer value1, Integer value2) {
            addCriterion("like_count between", value1, value2, "likeCount");
            return (Criteria) this;
        }

        public Criteria andLikeCountNotBetween(Integer value1, Integer value2) {
            addCriterion("like_count not between", value1, value2, "likeCount");
            return (Criteria) this;
        }

        public Criteria andCommentCountIsNull() {
            addCriterion("comment_count is null");
            return (Criteria) this;
        }

        public Criteria andCommentCountIsNotNull() {
            addCriterion("comment_count is not null");
            return (Criteria) this;
        }

        public Criteria andCommentCountEqualTo(Integer value) {
            addCriterion("comment_count =", value, "commentCount");
            return (Criteria) this;
        }

        public Criteria andCommentCountNotEqualTo(Integer value) {
            addCriterion("comment_count <>", value, "commentCount");
            return (Criteria) this;
        }

        public Criteria andCommentCountGreaterThan(Integer value) {
            addCriterion("comment_count >", value, "commentCount");
            return (Criteria) this;
        }

        public Criteria andCommentCountGreaterThanOrEqualTo(Integer value) {
            addCriterion("comment_count >=", value, "commentCount");
            return (Criteria) this;
        }

        public Criteria andCommentCountLessThan(Integer value) {
            addCriterion("comment_count <", value, "commentCount");
            return (Criteria) this;
        }

        public Criteria andCommentCountLessThanOrEqualTo(Integer value) {
            addCriterion("comment_count <=", value, "commentCount");
            return (Criteria) this;
        }

        public Criteria andCommentCountIn(List<Integer> values) {
            addCriterion("comment_count in", values, "commentCount");
            return (Criteria) this;
        }

        public Criteria andCommentCountNotIn(List<Integer> values) {
            addCriterion("comment_count not in", values, "commentCount");
            return (Criteria) this;
        }

        public Criteria andCommentCountBetween(Integer value1, Integer value2) {
            addCriterion("comment_count between", value1, value2, "commentCount");
            return (Criteria) this;
        }

        public Criteria andCommentCountNotBetween(Integer value1, Integer value2) {
            addCriterion("comment_count not between", value1, value2, "commentCount");
            return (Criteria) this;
        }

        public Criteria andDelFlagIsNull() {
            addCriterion("del_flag is null");
            return (Criteria) this;
        }

        public Criteria andDelFlagIsNotNull() {
            addCriterion("del_flag is not null");
            return (Criteria) this;
        }

        public Criteria andDelFlagEqualTo(Integer value) {
            addCriterion("del_flag =", value, "delFlag");
            return (Criteria) this;
        }

        public Criteria andDelFlagNotEqualTo(Integer value) {
            addCriterion("del_flag <>", value, "delFlag");
            return (Criteria) this;
        }

        public Criteria andDelFlagGreaterThan(Integer value) {
            addCriterion("del_flag >", value, "delFlag");
            return (Criteria) this;
        }

        public Criteria andDelFlagGreaterThanOrEqualTo(Integer value) {
            addCriterion("del_flag >=", value, "delFlag");
            return (Criteria) this;
        }

        public Criteria andDelFlagLessThan(Integer value) {
            addCriterion("del_flag <", value, "delFlag");
            return (Criteria) this;
        }

        public Criteria andDelFlagLessThanOrEqualTo(Integer value) {
            addCriterion("del_flag <=", value, "delFlag");
            return (Criteria) this;
        }

        public Criteria andDelFlagIn(List<Integer> values) {
            addCriterion("del_flag in", values, "delFlag");
            return (Criteria) this;
        }

        public Criteria andDelFlagNotIn(List<Integer> values) {
            addCriterion("del_flag not in", values, "delFlag");
            return (Criteria) this;
        }

        public Criteria andDelFlagBetween(Integer value1, Integer value2) {
            addCriterion("del_flag between", value1, value2, "delFlag");
            return (Criteria) this;
        }

        public Criteria andDelFlagNotBetween(Integer value1, Integer value2) {
            addCriterion("del_flag not between", value1, value2, "delFlag");
            return (Criteria) this;
        }

        public Criteria andAreaIsNull() {
            addCriterion("area is null");
            return (Criteria) this;
        }

        public Criteria andAreaIsNotNull() {
            addCriterion("area is not null");
            return (Criteria) this;
        }

        public Criteria andAreaEqualTo(String value) {
            addCriterion("area =", value, "area");
            return (Criteria) this;
        }

        public Criteria andAreaNotEqualTo(String value) {
            addCriterion("area <>", value, "area");
            return (Criteria) this;
        }

        public Criteria andAreaGreaterThan(String value) {
            addCriterion("area >", value, "area");
            return (Criteria) this;
        }

        public Criteria andAreaGreaterThanOrEqualTo(String value) {
            addCriterion("area >=", value, "area");
            return (Criteria) this;
        }

        public Criteria andAreaLessThan(String value) {
            addCriterion("area <", value, "area");
            return (Criteria) this;
        }

        public Criteria andAreaLessThanOrEqualTo(String value) {
            addCriterion("area <=", value, "area");
            return (Criteria) this;
        }

        public Criteria andAreaLike(String value) {
            addCriterion("area like", value, "area");
            return (Criteria) this;
        }

        public Criteria andAreaNotLike(String value) {
            addCriterion("area not like", value, "area");
            return (Criteria) this;
        }

        public Criteria andAreaIn(List<String> values) {
            addCriterion("area in", values, "area");
            return (Criteria) this;
        }

        public Criteria andAreaNotIn(List<String> values) {
            addCriterion("area not in", values, "area");
            return (Criteria) this;
        }

        public Criteria andAreaBetween(String value1, String value2) {
            addCriterion("area between", value1, value2, "area");
            return (Criteria) this;
        }

        public Criteria andAreaNotBetween(String value1, String value2) {
            addCriterion("area not between", value1, value2, "area");
            return (Criteria) this;
        }

        public Criteria andPicListIsNull() {
            addCriterion("pic_list is null");
            return (Criteria) this;
        }

        public Criteria andPicListIsNotNull() {
            addCriterion("pic_list is not null");
            return (Criteria) this;
        }

        public Criteria andPicListEqualTo(String value) {
            addCriterion("pic_list =", value, "picList");
            return (Criteria) this;
        }

        public Criteria andPicListNotEqualTo(String value) {
            addCriterion("pic_list <>", value, "picList");
            return (Criteria) this;
        }

        public Criteria andPicListGreaterThan(String value) {
            addCriterion("pic_list >", value, "picList");
            return (Criteria) this;
        }

        public Criteria andPicListGreaterThanOrEqualTo(String value) {
            addCriterion("pic_list >=", value, "picList");
            return (Criteria) this;
        }

        public Criteria andPicListLessThan(String value) {
            addCriterion("pic_list <", value, "picList");
            return (Criteria) this;
        }

        public Criteria andPicListLessThanOrEqualTo(String value) {
            addCriterion("pic_list <=", value, "picList");
            return (Criteria) this;
        }

        public Criteria andPicListLike(String value) {
            addCriterion("pic_list like", value, "picList");
            return (Criteria) this;
        }

        public Criteria andPicListNotLike(String value) {
            addCriterion("pic_list not like", value, "picList");
            return (Criteria) this;
        }

        public Criteria andPicListIn(List<String> values) {
            addCriterion("pic_list in", values, "picList");
            return (Criteria) this;
        }

        public Criteria andPicListNotIn(List<String> values) {
            addCriterion("pic_list not in", values, "picList");
            return (Criteria) this;
        }

        public Criteria andPicListBetween(String value1, String value2) {
            addCriterion("pic_list between", value1, value2, "picList");
            return (Criteria) this;
        }

        public Criteria andPicListNotBetween(String value1, String value2) {
            addCriterion("pic_list not between", value1, value2, "picList");
            return (Criteria) this;
        }

        public Criteria andLastReplyTimeIsNull() {
            addCriterion("last_reply_time is null");
            return (Criteria) this;
        }

        public Criteria andLastReplyTimeIsNotNull() {
            addCriterion("last_reply_time is not null");
            return (Criteria) this;
        }

        public Criteria andLastReplyTimeEqualTo(String value) {
            addCriterion("last_reply_time =", value, "lastReplyTime");
            return (Criteria) this;
        }

        public Criteria andLastReplyTimeNotEqualTo(String value) {
            addCriterion("last_reply_time <>", value, "lastReplyTime");
            return (Criteria) this;
        }

        public Criteria andLastReplyTimeGreaterThan(String value) {
            addCriterion("last_reply_time >", value, "lastReplyTime");
            return (Criteria) this;
        }

        public Criteria andLastReplyTimeGreaterThanOrEqualTo(String value) {
            addCriterion("last_reply_time >=", value, "lastReplyTime");
            return (Criteria) this;
        }

        public Criteria andLastReplyTimeLessThan(String value) {
            addCriterion("last_reply_time <", value, "lastReplyTime");
            return (Criteria) this;
        }

        public Criteria andLastReplyTimeLessThanOrEqualTo(String value) {
            addCriterion("last_reply_time <=", value, "lastReplyTime");
            return (Criteria) this;
        }

        public Criteria andLastReplyTimeLike(String value) {
            addCriterion("last_reply_time like", value, "lastReplyTime");
            return (Criteria) this;
        }

        public Criteria andLastReplyTimeNotLike(String value) {
            addCriterion("last_reply_time not like", value, "lastReplyTime");
            return (Criteria) this;
        }

        public Criteria andLastReplyTimeIn(List<String> values) {
            addCriterion("last_reply_time in", values, "lastReplyTime");
            return (Criteria) this;
        }

        public Criteria andLastReplyTimeNotIn(List<String> values) {
            addCriterion("last_reply_time not in", values, "lastReplyTime");
            return (Criteria) this;
        }

        public Criteria andLastReplyTimeBetween(String value1, String value2) {
            addCriterion("last_reply_time between", value1, value2, "lastReplyTime");
            return (Criteria) this;
        }

        public Criteria andLastReplyTimeNotBetween(String value1, String value2) {
            addCriterion("last_reply_time not between", value1, value2, "lastReplyTime");
            return (Criteria) this;
        }

        public Criteria andLastReplyUserNameIsNull() {
            addCriterion("last_reply_user_name is null");
            return (Criteria) this;
        }

        public Criteria andLastReplyUserNameIsNotNull() {
            addCriterion("last_reply_user_name is not null");
            return (Criteria) this;
        }

        public Criteria andLastReplyUserNameEqualTo(String value) {
            addCriterion("last_reply_user_name =", value, "lastReplyUserName");
            return (Criteria) this;
        }

        public Criteria andLastReplyUserNameNotEqualTo(String value) {
            addCriterion("last_reply_user_name <>", value, "lastReplyUserName");
            return (Criteria) this;
        }

        public Criteria andLastReplyUserNameGreaterThan(String value) {
            addCriterion("last_reply_user_name >", value, "lastReplyUserName");
            return (Criteria) this;
        }

        public Criteria andLastReplyUserNameGreaterThanOrEqualTo(String value) {
            addCriterion("last_reply_user_name >=", value, "lastReplyUserName");
            return (Criteria) this;
        }

        public Criteria andLastReplyUserNameLessThan(String value) {
            addCriterion("last_reply_user_name <", value, "lastReplyUserName");
            return (Criteria) this;
        }

        public Criteria andLastReplyUserNameLessThanOrEqualTo(String value) {
            addCriterion("last_reply_user_name <=", value, "lastReplyUserName");
            return (Criteria) this;
        }

        public Criteria andLastReplyUserNameLike(String value) {
            addCriterion("last_reply_user_name like", value, "lastReplyUserName");
            return (Criteria) this;
        }

        public Criteria andLastReplyUserNameNotLike(String value) {
            addCriterion("last_reply_user_name not like", value, "lastReplyUserName");
            return (Criteria) this;
        }

        public Criteria andLastReplyUserNameIn(List<String> values) {
            addCriterion("last_reply_user_name in", values, "lastReplyUserName");
            return (Criteria) this;
        }

        public Criteria andLastReplyUserNameNotIn(List<String> values) {
            addCriterion("last_reply_user_name not in", values, "lastReplyUserName");
            return (Criteria) this;
        }

        public Criteria andLastReplyUserNameBetween(String value1, String value2) {
            addCriterion("last_reply_user_name between", value1, value2, "lastReplyUserName");
            return (Criteria) this;
        }

        public Criteria andLastReplyUserNameNotBetween(String value1, String value2) {
            addCriterion("last_reply_user_name not between", value1, value2, "lastReplyUserName");
            return (Criteria) this;
        }
    }

    public static class Criteria extends GeneratedCriteria implements Serializable {

        protected Criteria() {
            super();
        }
    }

    public static class Criterion implements Serializable {
        private String condition;

        private Object value;

        private Object secondValue;

        private boolean noValue;

        private boolean singleValue;

        private boolean betweenValue;

        private boolean listValue;

        private String typeHandler;

        public String getCondition() {
            return condition;
        }

        public Object getValue() {
            return value;
        }

        public Object getSecondValue() {
            return secondValue;
        }

        public boolean isNoValue() {
            return noValue;
        }

        public boolean isSingleValue() {
            return singleValue;
        }

        public boolean isBetweenValue() {
            return betweenValue;
        }

        public boolean isListValue() {
            return listValue;
        }

        public String getTypeHandler() {
            return typeHandler;
        }

        protected Criterion(String condition) {
            super();
            this.condition = condition;
            this.typeHandler = null;
            this.noValue = true;
        }

        protected Criterion(String condition, Object value, String typeHandler) {
            super();
            this.condition = condition;
            this.value = value;
            this.typeHandler = typeHandler;
            if (value instanceof List<?>) {
                this.listValue = true;
            } else {
                this.singleValue = true;
            }
        }

        protected Criterion(String condition, Object value) {
            this(condition, value, null);
        }

        protected Criterion(String condition, Object value, Object secondValue, String typeHandler) {
            super();
            this.condition = condition;
            this.value = value;
            this.secondValue = secondValue;
            this.typeHandler = typeHandler;
            this.betweenValue = true;
        }

        protected Criterion(String condition, Object value, Object secondValue) {
            this(condition, value, secondValue, null);
        }
    }
}