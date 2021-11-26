create table article
(
	id int auto_increment
		primary key,
	article_title varchar(255) charset utf8 default '' null,
	article_content text null,
	create_time datetime null,
	user_id int null,
	del_flag int(255) default '0' null,
	like_count int default '0' null,
	comment_count int default '0' null,
	browse_count int default '0' null,
	label int null,
	anonymous_switch int default '0' not null,
	is_recommend int default '0' null,
	content_type int default '1' not null
)
charset=utf8mb4
;



create table c_message
(
	id int auto_increment
		primary key,
	content varchar(255) null,
	create_time datetime null,
	post_user_id int null,
	received_user_id int null,
	del_flag int default '0' null,
	type int null,
	is_read int default '0' null
)
;

create table general_comment
(
	id int not null auto_increment
		primary key,
	topic_id int null,
	topic_type int null,
	content varchar(255) null,
	post_user_id int null,
	create_time datetime null,
	like_count int null,
	del_flag int default '0' null,
	has_reply INT DEFAULT '0' null
)
comment '评论表设计'
;

create table general_like
(
	id int not null auto_increment
		primary key,
	topic_id int null,
	topic_type int null,
	like_value int default '0' null,
	post_user_id int null,
	create_time datetime null,
	del_flag int default '0' null
)
comment '点赞表'
;

create table cwd_boring.general_reply
(
	id int not null auto_increment
		primary key,
	comment_id int null,
	content varchar(255) null,
	post_user_id int null,
	to_user_id int null,
	type int null,
	create_time datetime null,
	del_flag int default '0' null,
	reply_id int null
)
comment '回复评论的'
;

create table question
(
	id int not null auto_increment
		primary key,
	que_content varchar(255) null,
	createtime datetime null,
	del_flag int(255) default '0' null,
	user_id int null
)
;

create table super_beauty
(
	id int not null auto_increment
		primary key,
	img_url varchar(255) null,
	upload_user_id int null,
	create_time datetime null,
	del_flag int(255) default '0' null,
	like_count int default '0' null,
	title varchar(255) null,
	category varchar(255) default '' null comment '类别',
	pic_list varchar(500) default '' null,
	pic_desc_list varchar(500) default '' null,
	see_count int default '0' null
)
;


create table thought
(
	id int auto_increment
		primary key,
	thought_content varchar(400) null,
	create_time datetime null,
	user_id int null,
	like_count int default '0' null,
	comment_count int default '0' null,
	del_flag int default '0' null,
	area varchar(100) null,
	last_reply_time varchar(50) default '''' null,
	pic_list varchar(500) default '' null,
	last_reply_user_name varchar(50) default '' null
)
comment '想法'
;




create table fq_user
(
	id int not null auto_increment
		primary key,
	username varchar(255) null,
	password varchar(255) null,
	create_time datetime null,
	nickname varchar(255) null,
	icon varchar(255) null,
	create_ip varchar(255) null,
	city varchar(50) null,
	sex int null,
	is_single int null,
	is_mail_bind int null,
	sign varchar(255) null,
	openid varchar(255) null,
	provider varchar(255) null,
	qudou_num int null,
	birth varchar(255) default '' null,
	education varchar(255) default '' null,
	school varchar(255) default '' null,
	role int default '0' null,
	level int default '1' null,
	status int default '0' null
)
;





create table user_time_line
(
	id int not null auto_increment
		primary key,
	content varchar(255) null,
	create_time datetime null,
	del_flag int default '0' null,
	user_id int null
)
;


create table job_talk
(
	id int auto_increment
		primary key,
	content text null,
	user_id int null,
	title varchar(255) null,
	create_time datetime null,
	del_flag int default '0' null,
	comment_count int default '0' null,
	label varchar(255) null,
	type int null,
	last_pub_nickname varchar(255) null,
	last_pub_time datetime null,
	see_count int null
)
;


create table upload_img_record
(
	id int auto_increment
		primary key,
	pic_url varchar(255) null,
	pic_md5 varchar(255) null,
	create_time datetime null,
	ip varchar(100) null,
	user_id int null,
	pic_size bigint null
)
;


create table user_activate
(
	id int auto_increment
		primary key,
	user_id int null,
	token varchar(255) null,
	create_time datetime null
)
;

create table cwd_boring.user_follow
(
	id int not null auto_increment
		primary key,
	follower_user_id int null,
	followed_user_id int null,
	create_time datetime null,
	del_flag int default '0' null
)
comment '用户关注表'
;


create table fq_notice
(
	id int auto_increment
		primary key,
	content text null,
	create_time datetime null,
	title varchar(255) null,
	fq_order int null,
	is_show int default '0' null,
	user_id int null,
	nickname varchar(255) null,
	icon varchar(255) null,
	type varchar(50) null,
	comment_num int null
)
	comment '通知表'
;


create table nginx_log
(
	id int auto_increment
		primary key,
	ip varchar(255) null,
	local_time varchar(255) null,
	method varchar(255) null,
	url varchar(255) null,
	http varchar(255) null,
	status varchar(255) null,
	bytes varchar(255) null,
	referer varchar(255) null,
	xforward varchar(255) null,
	request_time double null,
	user_agent varchar(255) null,
	create_time datetime null,
	spider_type int default '0' null comment '//0代表没有爬虫 1 百度爬虫 2 google爬虫 3 bing爬虫 4 搜狗'
)
;



create table fq_sign
(
	id int auto_increment
		primary key,
	days int not null comment '实时的连续的签到天数',
	user_id int null,
	sign_time datetime null,
	sign_days varchar(255) null,
	max_days int default 0 not null comment '签到最长的天数，用于补签'
)
comment '签到' charset=utf8;





create table fq_third_party
(
	id int auto_increment
		primary key,
	openid varchar(255) null,
	provider varchar(255) null,
	user_id int null,
	create_time datetime null
)
	comment '第三方'
;

create table fq_label
(
	id int auto_increment
		primary key,
	name varchar(255) null,
	del_flag int default '0' null,
	type int null
)
;

create table fq_area
(
	id int auto_increment
		primary key,
	name varchar(255) null,
	card_num int default '0' null
)
;
create table cwd_boring.fq_visit_record
(
	id int not null auto_increment
		primary key,
	visit_user_id int null,
	visited_user_id int null,
	visit_time datetime null,
	del_flag int default '0' null
)
;

create table fq_music
(
	id int auto_increment
		primary key,
	music_name varchar(255) null,
	music_url varchar(255) null,
	del_flag int null,
	create_time datetime null,
	like_count int default '0' null,
	play_count int null,
	user_id int null,
	lyric varchar(1000) null,
	singer varchar(50) default '' null
)
	engine=InnoDB
;



create table fq_friend_link
(
	id int auto_increment
		primary key,
	link_name varchar(255) null,
	link_url varchar(255) null,
	create_time datetime null
)
;

create table fq_background_img
(
	id int auto_increment
		primary key,
	img_url varchar(255) null,
	del_flag int default '0' null,
	user_id int null,
	create_time datetime null,
	update_time datetime null,
	history_urls varchar(500) default '' null
)
;


create table cwd_boring.fq_collect
(
	id int not null auto_increment
		primary key,
	topic_type int null,
	topic_id int null,
	del_flag int default '0' null,
	create_time datetime null,
	user_id int null
)
	comment '收藏表'
;

create table fq_website_dir
(
	id int not null auto_increment
		primary key,
	url varchar(255) default '' not null,
	type varchar(40) default '' null,
	del_flag int default '0' null,
	name varchar(255) default '' not null,
	click_count int default '0' null,
	user_id int default '0' null,
	create_time datetime null,
	icon varchar(100) default '' null
)
;








create table fq_short_video
(
	id bigint not null auto_increment
		primary key,
	url varchar(255) default '' not null,
	user_id int default '0' not null,
	create_time datetime not null,
	del_flag int default '0' not null,
	like_count int default '0' not null,
	title varchar(40) default '' not null
)
;

create table fq_news
(
	ID bigint auto_increment comment '主键'
		primary key,
	TITLE varchar(100) default '' not null comment '标题',
	CONTENT text null,
	SOURCE varchar(50) default '' not null,
	COMMENT_COUNT int default '0' not null,
	IMG_SRC varchar(100) default '' not null,
	P_TIME varchar(50) default '' not null,
	GMT_CREATE datetime not null
)
comment '新闻'
;
create table fq_topic
(
  ID bigint auto_increment comment '主键'
    primary key,
  TITLE varchar(100) default '' not null comment '标题',
  CONTENT text null,
  SOURCE varchar(50) default '' not null,
  AUTHOR varchar(50) default '' not null COMMENT '作者',
  AUTHOR_ICON varchar(100) default '' not null COMMENT '作者头像',
  COMMENT_COUNT int default '0' not null,
  GMT_CREATE datetime not null,
  TYPE VARCHAR(20) DEFAULT '' NOT NULL
)
  comment '话题'
;
CREATE TABLE fq_topic_reply
(
    ID BIGINT PRIMARY KEY NOT NULL AUTO_INCREMENT,
    CONTENT VARCHAR(500) DEFAULT '' NOT NULL,
    TOPIC_ID BIGINT DEFAULT 0 NOT NULL,
    GMT_CREATE DATETIME NOT NULL
);

create table wang_hong_wan
(
	ID bigint not null auto_increment
		primary key,
	AUTHOR varchar(50) default '' not null,
	AREA varchar(50) default '' not null,
	CONTENT varchar(500) default '' not null,
	PIC_LIST varchar(1000) default '' not null
)
	comment '网红玩'
;
CREATE TABLE FQ_SEARCH_RECORD
(
    ID BIGINT PRIMARY KEY NOT NULL AUTO_INCREMENT,
    NAME VARCHAR(50) DEFAULT '' NOT NULL COMMENT '搜索名称',
    GMT_CREATE DATETIME NOT NULL,
    DEL_FLAG int DEFAULT 0 NOT NULL,
    TYPE int DEFAULT 0 NOT NULL COMMENT '搜索类别',
    USER_ID int DEFAULT 0 NOT NULL COMMENT '用户id'
);
ALTER TABLE FQ_SEARCH_RECORD COMMENT = '搜索记录';
CREATE TABLE FQ_BLACK_LIST
(
    ID BIGINT PRIMARY KEY NOT NULL AUTO_INCREMENT,
    IP VARCHAR(20) DEFAULT '' NOT NULL COMMENT '被拉黑的ip',
    GMT_CREATE DATETIME NOT NULL,
    OPERATOR VARCHAR(45) DEFAULT '' NOT NULL
);
ALTER TABLE FQ_BLACK_LIST COMMENT = '黑名单';

CREATE TABLE fq_user_active_num
(
    ID BIGINT PRIMARY KEY NOT NULL AUTO_INCREMENT,
    ACTIVE_NUM int DEFAULT 0 NOT NULL COMMENT '活跃度',
    GMT_CREATE DATETIME NOT NULL,
    USER_ID int DEFAULT 0 NOT NULL COMMENT '用户id',
    MARK VARCHAR(20) DEFAULT '' NOT NULL COMMENT '标识'
);
ALTER TABLE fq_user_active_num COMMENT = '用户活跃度';

CREATE TABLE FQ_USER_ACTIVITY_RECORD
(
    ID BIGINT PRIMARY KEY NOT NULL AUTO_INCREMENT,
    USER_ID INT DEFAULT 0 NOT NULL,
    CREATE_TIME DATETIME DEFAULT CURRENT_TIMESTAMP NOT NULL,
    DEL_FLAG int DEFAULT 0 NOT NULL,
    ACTIVITY_CONTENT VARCHAR(500) DEFAULT '' NOT NULL
);
ALTER TABLE FQ_USER_ACTIVITY_RECORD COMMENT = '用户活动记录';

create table api_doc_project
(
	ID bigint not null auto_increment
		primary key,
	USER_ID int default '0' not null comment '创建人的id',
	PROJECT_NAME varchar(50) default '' not null comment '项目名称',
	CREATE_TIME datetime default CURRENT_TIMESTAMP not null,
	STATUS tinyint default '1' not null,
	REMARK varchar(200) default '' not null,
	TYPE tinyint default '1' not null,
	PASSWORD varchar(45) default '' not null,
	COVER varchar(200) default '' not null
)
	comment 'api文档项目'
;

CREATE TABLE api_doc_interface (
	ID bigint not null auto_increment
		primary key,
	`url` varchar(200) NOT NULL COMMENT 'api链接',
	`method` varchar(50) NOT NULL COMMENT ' 请求方式',
	`param` text COMMENT '参数列表',
	`paramRemark` text COMMENT '请求参数备注',
	`requestExam` text COMMENT '请求示例',
	`responseParam` text COMMENT '返回参数说明',
	`errorList` text COMMENT '接口错误码列表',
	`trueExam` text COMMENT '正确返回示例',
	`falseExam` text COMMENT '错误返回示例',
	`status` tinyint(4) NOT NULL DEFAULT '1' COMMENT '是否可用;0不可用；1可用;-1 删除',
	`moduleId` BIGINT NOT NULL DEFAULT '0' COMMENT '所属模块ID',
	`interfaceName` varchar(100) NOT NULL COMMENT '接口名',
	`remark` text,
	`errors` text COMMENT '错误码、错误码信息',
	`updateBy` varchar(100) DEFAULT NULL,
	`updateTime` timestamp NOT NULL DEFAULT '2018-12-31 00:00:00',
	`createTime` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
	`version` varchar(20) NOT NULL DEFAULT '1.0' COMMENT '版本号',
	`sequence` int(11) NOT NULL DEFAULT '0' COMMENT '排序，越大越靠前',
	`header` text,
	`fullUrl` varchar(255) NOT NULL DEFAULT '',
	`monitorEmails` varchar(200) DEFAULT NULL,
	`isTemplate` bit(1) NOT NULL DEFAULT b'0' COMMENT '是否是模板',
	`projectId` BIGINT NOT NULL DEFAULT '0',
	`contentType` varchar(45) NOT NULL DEFAULT 'application/json' COMMENT '接口返回contentType'
) COMMENT 'api文档接口';

create table api_doc_module
(
	ID bigint not null auto_increment
		primary key,
	MODULE_NAME varchar(50) default '' not null,
	CREATE_TIME datetime default CURRENT_TIMESTAMP not null,
	STATUS tinyint default '1' not null,
	URL varchar(100) default '' not null,
	REMARK varchar(200) default '' not null,
	USER_ID int default '0' not null,
	PROJECT_ID bigint default '0' not null
)
	comment '模块'
;
create table api_doc_project_user
(
  ID bigint not null auto_increment
    primary key,
  PROJECT_ID bigint default '0' not null,
  USER_ID int default '0' not null,
  CREATE_TIME datetime default CURRENT_TIMESTAMP not null,
  STATUS int default '0' not null,
  sponsor int default '0' not null
)
  comment '项目与用户的关联表'
;
create table fq_user_pay_way
(
  ID bigint auto_increment
    primary key,
  PAY_WAY int default '1' not null comment '1 支付宝 2 微信',
  PAY_IMG_URL varchar(100) default '' not null comment '支付的二维码照片',
  GMT_CREATE datetime not null comment '创建时间',
  USER_ID int default '0' not null comment '用户id',
  DEL_FLAG int default '0' not null comment '是否删除'
)
  comment '支付方式'
;
CREATE TABLE cwd_boring.fq_lolita
(
    ID BIGINT PRIMARY KEY NOT NULL AUTO_INCREMENT,
    PIC_URL VARCHAR(500) DEFAULT '' NOT NULL,
    USER_ID INT DEFAULT 0 NOT NULL,
    GMT_CREATE DATETIME NOT NULL COMMENT '创建时间',
    DEL_FLAG INT DEFAULT 0 NOT NULL,
    REMARK VARCHAR(100) DEFAULT '' NOT NULL COMMENT '备注',
    LINK VARCHAR(100) DEFAULT '' NOT NULL COMMENT '自动生成的链接 用于分享',
    LIKE_COUNT INT DEFAULT 0 NOT NULL ,
    COMMENT_COUNT INT DEFAULT 0 NOT NULL
);
ALTER TABLE cwd_boring.fq_lolita COMMENT = 'LOLITA FASHION洋装';

create table fq_advertisement
(
	ID int auto_increment
		primary key,
	PIC_URL varchar(100) default '' not null comment '广告图片',
	REMARK varchar(100) default '' not null comment '备注',
	GMT_CREATE datetime not null comment '创建时间',
	AD_HREF varchar(100) default '' not null comment '广告跳转链接',
	POSITION int default '1' not null comment '0 首页banner 1 列表页 2 详情页'
)
comment '广告配置'
;
create table fq_doutu_cloud
(
	ID bigint auto_increment
		primary key,
	USER_ID int default 0 not null,
	IMG_URL varchar(500) default '' not null,
	GMT_CREATE datetime not null,
	DEL_FLAG int default 0 not null,
	TAG varchar(100) default '' not null comment '标签 逗号隔开',
	VIDEO_URL varchar(100) default '' not null,
	TITLE varchar(100) default '' not null comment '标题'
)
comment '斗图云';
create table fq_user_auth
(
	ID int auto_increment,
	USER_ID int default 0 not null comment '授权的用户',
	AUTHED_USER_ID int default 0 not null comment '被授权的用户',
	AUTH_TIME DATETIME not null comment '被授权时间',
	DEL_FLAG INT default 0 not null comment '删除标志',
	AUTH_TYPE int default 0 not null comment '授权类型',
	constraint fq_user_auth_pk
		primary key (ID)
)
comment '用户授权';
create table fq_good_pic
(
	ID BIGINT auto_increment,
	TITLE VARCHAR(100) default '' not null,
	PIC_URL_LIST VARCHAR(1000) default '' not null comment '图片列表',
	GMT_CREATE DATETIME not null comment '创建时间',
	constraint fq_good_pic__pk
		primary key (ID)
)
comment '好资源的图片';

create table sys_job (
                       job_id 		      int(11) 	    not null auto_increment    comment '任务ID',
                       job_name            varchar(64)   default ''                 comment '任务名称',
                       job_group           varchar(64)   default ''                 comment '任务组名',
                       method_name         varchar(500)  default ''                 comment '任务方法',
                       method_params       varchar(50)   default null               comment '方法参数',
                       cron_expression     varchar(255)  default ''                 comment 'cron执行表达式',
                       misfire_policy      varchar(20)   default '3'                comment '计划执行错误策略（1立即执行 2执行一次 3放弃执行）',
                       status              char(1)       default '0'                comment '状态（0正常 1暂停）',
                       create_by           varchar(64)   default ''                 comment '创建者',
                       create_time         datetime                                 comment '创建时间',
                       update_by           varchar(64)   default ''                 comment '更新者',
                       update_time         datetime                                 comment '更新时间',
                       remark              varchar(500)  default ''                 comment '备注信息',
                       concurrent              char(1)  default '1'                 comment '是否并发执行（0允许 1禁止）',
                       primary key (job_id)
) engine=innodb auto_increment=100 default charset=utf8 comment = '定时任务调度表';




-- ----------------------------
-- 17、定时任务调度日志表
-- ----------------------------
create table sys_job_log (
                           job_log_id          int(11) 	    not null auto_increment    comment '任务日志ID',
                           job_name            varchar(64)   not null                   comment '任务名称',
                           job_group           varchar(64)   not null                   comment '任务组名',
                           method_name         varchar(500)                             comment '任务方法',
                           method_params       varchar(50)   default null               comment '方法参数',
                           job_message         varchar(500)                             comment '日志信息',
                           status              char(1)       default '0'                comment '执行状态（0正常 1失败）',
                           exception_info      varchar(2000) default ''                 comment '异常信息',
                           create_time         datetime                                 comment '创建时间',
                           primary key (job_log_id)
) engine=innodb default charset=utf8 comment = '定时任务调度日志表';

create table fq_change_log_collect
(
	id int auto_increment,
	title varchar(50) default '' not null comment '标题',
	gmt_create datetime not null,
	content text null,
	watch_count int default 0 not null comment '观看数量',
	constraint fq_change_log_collect_pk
		primary key (id)
)
	comment '更新日志收集';








