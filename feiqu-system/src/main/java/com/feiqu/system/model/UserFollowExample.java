package com.feiqu.system.model;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class UserFollowExample {
    protected String orderByClause;

    protected boolean distinct;

    protected List<Criteria> oredCriteria;

    public UserFollowExample() {
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

    protected abstract static class GeneratedCriteria {
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

        public Criteria andFollowerUserIdIsNull() {
            addCriterion("follower_user_id is null");
            return (Criteria) this;
        }

        public Criteria andFollowerUserIdIsNotNull() {
            addCriterion("follower_user_id is not null");
            return (Criteria) this;
        }

        public Criteria andFollowerUserIdEqualTo(Integer value) {
            addCriterion("follower_user_id =", value, "followerUserId");
            return (Criteria) this;
        }

        public Criteria andFollowerUserIdNotEqualTo(Integer value) {
            addCriterion("follower_user_id <>", value, "followerUserId");
            return (Criteria) this;
        }

        public Criteria andFollowerUserIdGreaterThan(Integer value) {
            addCriterion("follower_user_id >", value, "followerUserId");
            return (Criteria) this;
        }

        public Criteria andFollowerUserIdGreaterThanOrEqualTo(Integer value) {
            addCriterion("follower_user_id >=", value, "followerUserId");
            return (Criteria) this;
        }

        public Criteria andFollowerUserIdLessThan(Integer value) {
            addCriterion("follower_user_id <", value, "followerUserId");
            return (Criteria) this;
        }

        public Criteria andFollowerUserIdLessThanOrEqualTo(Integer value) {
            addCriterion("follower_user_id <=", value, "followerUserId");
            return (Criteria) this;
        }

        public Criteria andFollowerUserIdIn(List<Integer> values) {
            addCriterion("follower_user_id in", values, "followerUserId");
            return (Criteria) this;
        }

        public Criteria andFollowerUserIdNotIn(List<Integer> values) {
            addCriterion("follower_user_id not in", values, "followerUserId");
            return (Criteria) this;
        }

        public Criteria andFollowerUserIdBetween(Integer value1, Integer value2) {
            addCriterion("follower_user_id between", value1, value2, "followerUserId");
            return (Criteria) this;
        }

        public Criteria andFollowerUserIdNotBetween(Integer value1, Integer value2) {
            addCriterion("follower_user_id not between", value1, value2, "followerUserId");
            return (Criteria) this;
        }

        public Criteria andFollowedUserIdIsNull() {
            addCriterion("followed_user_id is null");
            return (Criteria) this;
        }

        public Criteria andFollowedUserIdIsNotNull() {
            addCriterion("followed_user_id is not null");
            return (Criteria) this;
        }

        public Criteria andFollowedUserIdEqualTo(Integer value) {
            addCriterion("followed_user_id =", value, "followedUserId");
            return (Criteria) this;
        }

        public Criteria andFollowedUserIdNotEqualTo(Integer value) {
            addCriterion("followed_user_id <>", value, "followedUserId");
            return (Criteria) this;
        }

        public Criteria andFollowedUserIdGreaterThan(Integer value) {
            addCriterion("followed_user_id >", value, "followedUserId");
            return (Criteria) this;
        }

        public Criteria andFollowedUserIdGreaterThanOrEqualTo(Integer value) {
            addCriterion("followed_user_id >=", value, "followedUserId");
            return (Criteria) this;
        }

        public Criteria andFollowedUserIdLessThan(Integer value) {
            addCriterion("followed_user_id <", value, "followedUserId");
            return (Criteria) this;
        }

        public Criteria andFollowedUserIdLessThanOrEqualTo(Integer value) {
            addCriterion("followed_user_id <=", value, "followedUserId");
            return (Criteria) this;
        }

        public Criteria andFollowedUserIdIn(List<Integer> values) {
            addCriterion("followed_user_id in", values, "followedUserId");
            return (Criteria) this;
        }

        public Criteria andFollowedUserIdNotIn(List<Integer> values) {
            addCriterion("followed_user_id not in", values, "followedUserId");
            return (Criteria) this;
        }

        public Criteria andFollowedUserIdBetween(Integer value1, Integer value2) {
            addCriterion("followed_user_id between", value1, value2, "followedUserId");
            return (Criteria) this;
        }

        public Criteria andFollowedUserIdNotBetween(Integer value1, Integer value2) {
            addCriterion("followed_user_id not between", value1, value2, "followedUserId");
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
    }

    public static class Criteria extends GeneratedCriteria {

        protected Criteria() {
            super();
        }
    }

    public static class Criterion {
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