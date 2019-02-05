package com.feiqu.system.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class NginxLogExample implements Serializable {
    protected String orderByClause;

    protected boolean distinct;

    protected List<Criteria> oredCriteria;

    private static final long serialVersionUID = 1L;

    public NginxLogExample() {
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

        public Criteria andIpIsNull() {
            addCriterion("ip is null");
            return (Criteria) this;
        }

        public Criteria andIpIsNotNull() {
            addCriterion("ip is not null");
            return (Criteria) this;
        }

        public Criteria andIpEqualTo(String value) {
            addCriterion("ip =", value, "ip");
            return (Criteria) this;
        }

        public Criteria andIpNotEqualTo(String value) {
            addCriterion("ip <>", value, "ip");
            return (Criteria) this;
        }

        public Criteria andIpGreaterThan(String value) {
            addCriterion("ip >", value, "ip");
            return (Criteria) this;
        }

        public Criteria andIpGreaterThanOrEqualTo(String value) {
            addCriterion("ip >=", value, "ip");
            return (Criteria) this;
        }

        public Criteria andIpLessThan(String value) {
            addCriterion("ip <", value, "ip");
            return (Criteria) this;
        }

        public Criteria andIpLessThanOrEqualTo(String value) {
            addCriterion("ip <=", value, "ip");
            return (Criteria) this;
        }

        public Criteria andIpLike(String value) {
            addCriterion("ip like", value, "ip");
            return (Criteria) this;
        }

        public Criteria andIpNotLike(String value) {
            addCriterion("ip not like", value, "ip");
            return (Criteria) this;
        }

        public Criteria andIpIn(List<String> values) {
            addCriterion("ip in", values, "ip");
            return (Criteria) this;
        }

        public Criteria andIpNotIn(List<String> values) {
            addCriterion("ip not in", values, "ip");
            return (Criteria) this;
        }

        public Criteria andIpBetween(String value1, String value2) {
            addCriterion("ip between", value1, value2, "ip");
            return (Criteria) this;
        }

        public Criteria andIpNotBetween(String value1, String value2) {
            addCriterion("ip not between", value1, value2, "ip");
            return (Criteria) this;
        }

        public Criteria andLocalTimeIsNull() {
            addCriterion("local_time is null");
            return (Criteria) this;
        }

        public Criteria andLocalTimeIsNotNull() {
            addCriterion("local_time is not null");
            return (Criteria) this;
        }

        public Criteria andLocalTimeEqualTo(String value) {
            addCriterion("local_time =", value, "localTime");
            return (Criteria) this;
        }

        public Criteria andLocalTimeNotEqualTo(String value) {
            addCriterion("local_time <>", value, "localTime");
            return (Criteria) this;
        }

        public Criteria andLocalTimeGreaterThan(String value) {
            addCriterion("local_time >", value, "localTime");
            return (Criteria) this;
        }

        public Criteria andLocalTimeGreaterThanOrEqualTo(String value) {
            addCriterion("local_time >=", value, "localTime");
            return (Criteria) this;
        }

        public Criteria andLocalTimeLessThan(String value) {
            addCriterion("local_time <", value, "localTime");
            return (Criteria) this;
        }

        public Criteria andLocalTimeLessThanOrEqualTo(String value) {
            addCriterion("local_time <=", value, "localTime");
            return (Criteria) this;
        }

        public Criteria andLocalTimeLike(String value) {
            addCriterion("local_time like", value, "localTime");
            return (Criteria) this;
        }

        public Criteria andLocalTimeNotLike(String value) {
            addCriterion("local_time not like", value, "localTime");
            return (Criteria) this;
        }

        public Criteria andLocalTimeIn(List<String> values) {
            addCriterion("local_time in", values, "localTime");
            return (Criteria) this;
        }

        public Criteria andLocalTimeNotIn(List<String> values) {
            addCriterion("local_time not in", values, "localTime");
            return (Criteria) this;
        }

        public Criteria andLocalTimeBetween(String value1, String value2) {
            addCriterion("local_time between", value1, value2, "localTime");
            return (Criteria) this;
        }

        public Criteria andLocalTimeNotBetween(String value1, String value2) {
            addCriterion("local_time not between", value1, value2, "localTime");
            return (Criteria) this;
        }

        public Criteria andMethodIsNull() {
            addCriterion("method is null");
            return (Criteria) this;
        }

        public Criteria andMethodIsNotNull() {
            addCriterion("method is not null");
            return (Criteria) this;
        }

        public Criteria andMethodEqualTo(String value) {
            addCriterion("method =", value, "method");
            return (Criteria) this;
        }

        public Criteria andMethodNotEqualTo(String value) {
            addCriterion("method <>", value, "method");
            return (Criteria) this;
        }

        public Criteria andMethodGreaterThan(String value) {
            addCriterion("method >", value, "method");
            return (Criteria) this;
        }

        public Criteria andMethodGreaterThanOrEqualTo(String value) {
            addCriterion("method >=", value, "method");
            return (Criteria) this;
        }

        public Criteria andMethodLessThan(String value) {
            addCriterion("method <", value, "method");
            return (Criteria) this;
        }

        public Criteria andMethodLessThanOrEqualTo(String value) {
            addCriterion("method <=", value, "method");
            return (Criteria) this;
        }

        public Criteria andMethodLike(String value) {
            addCriterion("method like", value, "method");
            return (Criteria) this;
        }

        public Criteria andMethodNotLike(String value) {
            addCriterion("method not like", value, "method");
            return (Criteria) this;
        }

        public Criteria andMethodIn(List<String> values) {
            addCriterion("method in", values, "method");
            return (Criteria) this;
        }

        public Criteria andMethodNotIn(List<String> values) {
            addCriterion("method not in", values, "method");
            return (Criteria) this;
        }

        public Criteria andMethodBetween(String value1, String value2) {
            addCriterion("method between", value1, value2, "method");
            return (Criteria) this;
        }

        public Criteria andMethodNotBetween(String value1, String value2) {
            addCriterion("method not between", value1, value2, "method");
            return (Criteria) this;
        }

        public Criteria andUrlIsNull() {
            addCriterion("url is null");
            return (Criteria) this;
        }

        public Criteria andUrlIsNotNull() {
            addCriterion("url is not null");
            return (Criteria) this;
        }

        public Criteria andUrlEqualTo(String value) {
            addCriterion("url =", value, "url");
            return (Criteria) this;
        }

        public Criteria andUrlNotEqualTo(String value) {
            addCriterion("url <>", value, "url");
            return (Criteria) this;
        }

        public Criteria andUrlGreaterThan(String value) {
            addCriterion("url >", value, "url");
            return (Criteria) this;
        }

        public Criteria andUrlGreaterThanOrEqualTo(String value) {
            addCriterion("url >=", value, "url");
            return (Criteria) this;
        }

        public Criteria andUrlLessThan(String value) {
            addCriterion("url <", value, "url");
            return (Criteria) this;
        }

        public Criteria andUrlLessThanOrEqualTo(String value) {
            addCriterion("url <=", value, "url");
            return (Criteria) this;
        }

        public Criteria andUrlLike(String value) {
            addCriterion("url like", value, "url");
            return (Criteria) this;
        }

        public Criteria andUrlNotLike(String value) {
            addCriterion("url not like", value, "url");
            return (Criteria) this;
        }

        public Criteria andUrlIn(List<String> values) {
            addCriterion("url in", values, "url");
            return (Criteria) this;
        }

        public Criteria andUrlNotIn(List<String> values) {
            addCriterion("url not in", values, "url");
            return (Criteria) this;
        }

        public Criteria andUrlBetween(String value1, String value2) {
            addCriterion("url between", value1, value2, "url");
            return (Criteria) this;
        }

        public Criteria andUrlNotBetween(String value1, String value2) {
            addCriterion("url not between", value1, value2, "url");
            return (Criteria) this;
        }

        public Criteria andHttpIsNull() {
            addCriterion("http is null");
            return (Criteria) this;
        }

        public Criteria andHttpIsNotNull() {
            addCriterion("http is not null");
            return (Criteria) this;
        }

        public Criteria andHttpEqualTo(String value) {
            addCriterion("http =", value, "http");
            return (Criteria) this;
        }

        public Criteria andHttpNotEqualTo(String value) {
            addCriterion("http <>", value, "http");
            return (Criteria) this;
        }

        public Criteria andHttpGreaterThan(String value) {
            addCriterion("http >", value, "http");
            return (Criteria) this;
        }

        public Criteria andHttpGreaterThanOrEqualTo(String value) {
            addCriterion("http >=", value, "http");
            return (Criteria) this;
        }

        public Criteria andHttpLessThan(String value) {
            addCriterion("http <", value, "http");
            return (Criteria) this;
        }

        public Criteria andHttpLessThanOrEqualTo(String value) {
            addCriterion("http <=", value, "http");
            return (Criteria) this;
        }

        public Criteria andHttpLike(String value) {
            addCriterion("http like", value, "http");
            return (Criteria) this;
        }

        public Criteria andHttpNotLike(String value) {
            addCriterion("http not like", value, "http");
            return (Criteria) this;
        }

        public Criteria andHttpIn(List<String> values) {
            addCriterion("http in", values, "http");
            return (Criteria) this;
        }

        public Criteria andHttpNotIn(List<String> values) {
            addCriterion("http not in", values, "http");
            return (Criteria) this;
        }

        public Criteria andHttpBetween(String value1, String value2) {
            addCriterion("http between", value1, value2, "http");
            return (Criteria) this;
        }

        public Criteria andHttpNotBetween(String value1, String value2) {
            addCriterion("http not between", value1, value2, "http");
            return (Criteria) this;
        }

        public Criteria andStatusIsNull() {
            addCriterion("status is null");
            return (Criteria) this;
        }

        public Criteria andStatusIsNotNull() {
            addCriterion("status is not null");
            return (Criteria) this;
        }

        public Criteria andStatusEqualTo(String value) {
            addCriterion("status =", value, "status");
            return (Criteria) this;
        }

        public Criteria andStatusNotEqualTo(String value) {
            addCriterion("status <>", value, "status");
            return (Criteria) this;
        }

        public Criteria andStatusGreaterThan(String value) {
            addCriterion("status >", value, "status");
            return (Criteria) this;
        }

        public Criteria andStatusGreaterThanOrEqualTo(String value) {
            addCriterion("status >=", value, "status");
            return (Criteria) this;
        }

        public Criteria andStatusLessThan(String value) {
            addCriterion("status <", value, "status");
            return (Criteria) this;
        }

        public Criteria andStatusLessThanOrEqualTo(String value) {
            addCriterion("status <=", value, "status");
            return (Criteria) this;
        }

        public Criteria andStatusLike(String value) {
            addCriterion("status like", value, "status");
            return (Criteria) this;
        }

        public Criteria andStatusNotLike(String value) {
            addCriterion("status not like", value, "status");
            return (Criteria) this;
        }

        public Criteria andStatusIn(List<String> values) {
            addCriterion("status in", values, "status");
            return (Criteria) this;
        }

        public Criteria andStatusNotIn(List<String> values) {
            addCriterion("status not in", values, "status");
            return (Criteria) this;
        }

        public Criteria andStatusBetween(String value1, String value2) {
            addCriterion("status between", value1, value2, "status");
            return (Criteria) this;
        }

        public Criteria andStatusNotBetween(String value1, String value2) {
            addCriterion("status not between", value1, value2, "status");
            return (Criteria) this;
        }

        public Criteria andBytesIsNull() {
            addCriterion("bytes is null");
            return (Criteria) this;
        }

        public Criteria andBytesIsNotNull() {
            addCriterion("bytes is not null");
            return (Criteria) this;
        }

        public Criteria andBytesEqualTo(String value) {
            addCriterion("bytes =", value, "bytes");
            return (Criteria) this;
        }

        public Criteria andBytesNotEqualTo(String value) {
            addCriterion("bytes <>", value, "bytes");
            return (Criteria) this;
        }

        public Criteria andBytesGreaterThan(String value) {
            addCriterion("bytes >", value, "bytes");
            return (Criteria) this;
        }

        public Criteria andBytesGreaterThanOrEqualTo(String value) {
            addCriterion("bytes >=", value, "bytes");
            return (Criteria) this;
        }

        public Criteria andBytesLessThan(String value) {
            addCriterion("bytes <", value, "bytes");
            return (Criteria) this;
        }

        public Criteria andBytesLessThanOrEqualTo(String value) {
            addCriterion("bytes <=", value, "bytes");
            return (Criteria) this;
        }

        public Criteria andBytesLike(String value) {
            addCriterion("bytes like", value, "bytes");
            return (Criteria) this;
        }

        public Criteria andBytesNotLike(String value) {
            addCriterion("bytes not like", value, "bytes");
            return (Criteria) this;
        }

        public Criteria andBytesIn(List<String> values) {
            addCriterion("bytes in", values, "bytes");
            return (Criteria) this;
        }

        public Criteria andBytesNotIn(List<String> values) {
            addCriterion("bytes not in", values, "bytes");
            return (Criteria) this;
        }

        public Criteria andBytesBetween(String value1, String value2) {
            addCriterion("bytes between", value1, value2, "bytes");
            return (Criteria) this;
        }

        public Criteria andBytesNotBetween(String value1, String value2) {
            addCriterion("bytes not between", value1, value2, "bytes");
            return (Criteria) this;
        }

        public Criteria andRefererIsNull() {
            addCriterion("referer is null");
            return (Criteria) this;
        }

        public Criteria andRefererIsNotNull() {
            addCriterion("referer is not null");
            return (Criteria) this;
        }

        public Criteria andRefererEqualTo(String value) {
            addCriterion("referer =", value, "referer");
            return (Criteria) this;
        }

        public Criteria andRefererNotEqualTo(String value) {
            addCriterion("referer <>", value, "referer");
            return (Criteria) this;
        }

        public Criteria andRefererGreaterThan(String value) {
            addCriterion("referer >", value, "referer");
            return (Criteria) this;
        }

        public Criteria andRefererGreaterThanOrEqualTo(String value) {
            addCriterion("referer >=", value, "referer");
            return (Criteria) this;
        }

        public Criteria andRefererLessThan(String value) {
            addCriterion("referer <", value, "referer");
            return (Criteria) this;
        }

        public Criteria andRefererLessThanOrEqualTo(String value) {
            addCriterion("referer <=", value, "referer");
            return (Criteria) this;
        }

        public Criteria andRefererLike(String value) {
            addCriterion("referer like", value, "referer");
            return (Criteria) this;
        }

        public Criteria andRefererNotLike(String value) {
            addCriterion("referer not like", value, "referer");
            return (Criteria) this;
        }

        public Criteria andRefererIn(List<String> values) {
            addCriterion("referer in", values, "referer");
            return (Criteria) this;
        }

        public Criteria andRefererNotIn(List<String> values) {
            addCriterion("referer not in", values, "referer");
            return (Criteria) this;
        }

        public Criteria andRefererBetween(String value1, String value2) {
            addCriterion("referer between", value1, value2, "referer");
            return (Criteria) this;
        }

        public Criteria andRefererNotBetween(String value1, String value2) {
            addCriterion("referer not between", value1, value2, "referer");
            return (Criteria) this;
        }

        public Criteria andXforwardIsNull() {
            addCriterion("xforward is null");
            return (Criteria) this;
        }

        public Criteria andXforwardIsNotNull() {
            addCriterion("xforward is not null");
            return (Criteria) this;
        }

        public Criteria andXforwardEqualTo(String value) {
            addCriterion("xforward =", value, "xforward");
            return (Criteria) this;
        }

        public Criteria andXforwardNotEqualTo(String value) {
            addCriterion("xforward <>", value, "xforward");
            return (Criteria) this;
        }

        public Criteria andXforwardGreaterThan(String value) {
            addCriterion("xforward >", value, "xforward");
            return (Criteria) this;
        }

        public Criteria andXforwardGreaterThanOrEqualTo(String value) {
            addCriterion("xforward >=", value, "xforward");
            return (Criteria) this;
        }

        public Criteria andXforwardLessThan(String value) {
            addCriterion("xforward <", value, "xforward");
            return (Criteria) this;
        }

        public Criteria andXforwardLessThanOrEqualTo(String value) {
            addCriterion("xforward <=", value, "xforward");
            return (Criteria) this;
        }

        public Criteria andXforwardLike(String value) {
            addCriterion("xforward like", value, "xforward");
            return (Criteria) this;
        }

        public Criteria andXforwardNotLike(String value) {
            addCriterion("xforward not like", value, "xforward");
            return (Criteria) this;
        }

        public Criteria andXforwardIn(List<String> values) {
            addCriterion("xforward in", values, "xforward");
            return (Criteria) this;
        }

        public Criteria andXforwardNotIn(List<String> values) {
            addCriterion("xforward not in", values, "xforward");
            return (Criteria) this;
        }

        public Criteria andXforwardBetween(String value1, String value2) {
            addCriterion("xforward between", value1, value2, "xforward");
            return (Criteria) this;
        }

        public Criteria andXforwardNotBetween(String value1, String value2) {
            addCriterion("xforward not between", value1, value2, "xforward");
            return (Criteria) this;
        }

        public Criteria andRequestTimeIsNull() {
            addCriterion("request_time is null");
            return (Criteria) this;
        }

        public Criteria andRequestTimeIsNotNull() {
            addCriterion("request_time is not null");
            return (Criteria) this;
        }

        public Criteria andRequestTimeEqualTo(Double value) {
            addCriterion("request_time =", value, "requestTime");
            return (Criteria) this;
        }

        public Criteria andRequestTimeNotEqualTo(Double value) {
            addCriterion("request_time <>", value, "requestTime");
            return (Criteria) this;
        }

        public Criteria andRequestTimeGreaterThan(Double value) {
            addCriterion("request_time >", value, "requestTime");
            return (Criteria) this;
        }

        public Criteria andRequestTimeGreaterThanOrEqualTo(Double value) {
            addCriterion("request_time >=", value, "requestTime");
            return (Criteria) this;
        }

        public Criteria andRequestTimeLessThan(Double value) {
            addCriterion("request_time <", value, "requestTime");
            return (Criteria) this;
        }

        public Criteria andRequestTimeLessThanOrEqualTo(Double value) {
            addCriterion("request_time <=", value, "requestTime");
            return (Criteria) this;
        }

        public Criteria andRequestTimeIn(List<Double> values) {
            addCriterion("request_time in", values, "requestTime");
            return (Criteria) this;
        }

        public Criteria andRequestTimeNotIn(List<Double> values) {
            addCriterion("request_time not in", values, "requestTime");
            return (Criteria) this;
        }

        public Criteria andRequestTimeBetween(Double value1, Double value2) {
            addCriterion("request_time between", value1, value2, "requestTime");
            return (Criteria) this;
        }

        public Criteria andRequestTimeNotBetween(Double value1, Double value2) {
            addCriterion("request_time not between", value1, value2, "requestTime");
            return (Criteria) this;
        }

        public Criteria andUserAgentIsNull() {
            addCriterion("user_agent is null");
            return (Criteria) this;
        }

        public Criteria andUserAgentIsNotNull() {
            addCriterion("user_agent is not null");
            return (Criteria) this;
        }

        public Criteria andUserAgentEqualTo(String value) {
            addCriterion("user_agent =", value, "userAgent");
            return (Criteria) this;
        }

        public Criteria andUserAgentNotEqualTo(String value) {
            addCriterion("user_agent <>", value, "userAgent");
            return (Criteria) this;
        }

        public Criteria andUserAgentGreaterThan(String value) {
            addCriterion("user_agent >", value, "userAgent");
            return (Criteria) this;
        }

        public Criteria andUserAgentGreaterThanOrEqualTo(String value) {
            addCriterion("user_agent >=", value, "userAgent");
            return (Criteria) this;
        }

        public Criteria andUserAgentLessThan(String value) {
            addCriterion("user_agent <", value, "userAgent");
            return (Criteria) this;
        }

        public Criteria andUserAgentLessThanOrEqualTo(String value) {
            addCriterion("user_agent <=", value, "userAgent");
            return (Criteria) this;
        }

        public Criteria andUserAgentLike(String value) {
            addCriterion("user_agent like", value, "userAgent");
            return (Criteria) this;
        }

        public Criteria andUserAgentNotLike(String value) {
            addCriterion("user_agent not like", value, "userAgent");
            return (Criteria) this;
        }

        public Criteria andUserAgentIn(List<String> values) {
            addCriterion("user_agent in", values, "userAgent");
            return (Criteria) this;
        }

        public Criteria andUserAgentNotIn(List<String> values) {
            addCriterion("user_agent not in", values, "userAgent");
            return (Criteria) this;
        }

        public Criteria andUserAgentBetween(String value1, String value2) {
            addCriterion("user_agent between", value1, value2, "userAgent");
            return (Criteria) this;
        }

        public Criteria andUserAgentNotBetween(String value1, String value2) {
            addCriterion("user_agent not between", value1, value2, "userAgent");
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

        public Criteria andSpiderTypeIsNull() {
            addCriterion("spider_type is null");
            return (Criteria) this;
        }

        public Criteria andSpiderTypeIsNotNull() {
            addCriterion("spider_type is not null");
            return (Criteria) this;
        }

        public Criteria andSpiderTypeEqualTo(Integer value) {
            addCriterion("spider_type =", value, "spiderType");
            return (Criteria) this;
        }

        public Criteria andSpiderTypeNotEqualTo(Integer value) {
            addCriterion("spider_type <>", value, "spiderType");
            return (Criteria) this;
        }

        public Criteria andSpiderTypeGreaterThan(Integer value) {
            addCriterion("spider_type >", value, "spiderType");
            return (Criteria) this;
        }

        public Criteria andSpiderTypeGreaterThanOrEqualTo(Integer value) {
            addCriterion("spider_type >=", value, "spiderType");
            return (Criteria) this;
        }

        public Criteria andSpiderTypeLessThan(Integer value) {
            addCriterion("spider_type <", value, "spiderType");
            return (Criteria) this;
        }

        public Criteria andSpiderTypeLessThanOrEqualTo(Integer value) {
            addCriterion("spider_type <=", value, "spiderType");
            return (Criteria) this;
        }

        public Criteria andSpiderTypeIn(List<Integer> values) {
            addCriterion("spider_type in", values, "spiderType");
            return (Criteria) this;
        }

        public Criteria andSpiderTypeNotIn(List<Integer> values) {
            addCriterion("spider_type not in", values, "spiderType");
            return (Criteria) this;
        }

        public Criteria andSpiderTypeBetween(Integer value1, Integer value2) {
            addCriterion("spider_type between", value1, value2, "spiderType");
            return (Criteria) this;
        }

        public Criteria andSpiderTypeNotBetween(Integer value1, Integer value2) {
            addCriterion("spider_type not between", value1, value2, "spiderType");
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