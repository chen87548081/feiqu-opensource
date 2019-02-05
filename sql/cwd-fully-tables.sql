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


create table fq_theme
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
	days int null,
	user_id int null,
	sign_time datetime null,
	sign_days varchar(255) null comment '一个月签到哪些天 逗号隔开'
)
	comment '签到'
;



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


CREATE TABLE FQ_BLACK_LIST
(
    ID BIGINT PRIMARY KEY NOT NULL AUTO_INCREMENT,
    IP VARCHAR(20) DEFAULT '' NOT NULL COMMENT '被拉黑的ip',
    GMT_CREATE DATETIME NOT NULL,
    OPERATOR VARCHAR(45) DEFAULT '' NOT NULL
);
ALTER TABLE FQ_BLACK_LIST COMMENT = '黑名单';

CREATE TABLE FQ_USER_ACTIVE_NUM
(
    ID BIGINT PRIMARY KEY NOT NULL AUTO_INCREMENT,
    ACTIVE_NUM int DEFAULT 0 NOT NULL COMMENT '活跃度',
    GMT_CREATE DATETIME NOT NULL,
    USER_ID int DEFAULT 0 NOT NULL COMMENT '用户id',
    MARK VARCHAR(20) DEFAULT '' NOT NULL COMMENT '标识'
);
ALTER TABLE FQ_USER_ACTIVE_NUM COMMENT = '用户活跃度';



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
CREATE TABLE FQ_USER_PAY_WAY
(
    ID BIGINT PRIMARY KEY NOT NULL AUTO_INCREMENT,
    PAY_WAY int DEFAULT 1 NOT NULL COMMENT '1 支付宝 2 微信 ',
    PAY_IMG_URL VARCHAR(100) DEFAULT '' NOT NULL COMMENT '支付的二维码照片',
    GMT_CREATE DATETIME NOT NULL COMMENT '创建时间',
    USER_ID int DEFAULT 0 NOT NULL COMMENT '用户id',
    DEL_FLAG int DEFAULT 0 NOT NULL COMMENT '是否删除'
);
ALTER TABLE FQ_USER_PAY_WAY COMMENT = '支付方式';


