
DROP SCHEMA IF EXISTS itovip;
CREATE SCHEMA itovip DEFAULT CHARACTER SET utf8 COLLATE utf8_general_ci;
USE itovip;
SET AUTOCOMMIT=0;

--
-- Table structure for table `user`
--

DROP TABLE IF EXISTS `user`;

CREATE TABLE `user` (
  `user_id` bigint(20) NOT NULL PRIMARY KEY AUTO_INCREMENT,
  `username` varchar(255),
  `nickname` varchar(255),
  `gender` varchar(10),
  `birthday` datetime,
  `email_address` varchar(255),
  `phone_number` varchar(20),
  `image` varchar(255),
  `password_` varchar(255),
  `created_by` bigint(20),
  `created_date` datetime,
  `updated_by` bigint(20),
  `updated_date` datetime,
  `last_login_date` datetime
) engine=innodb default charset=utf8 auto_increment=1;

--
-- Table structure for table `address`
--

DROP TABLE IF EXISTS `address`;

CREATE TABLE `address` (
  `address_id` bigint(20) NOT NULL PRIMARY KEY AUTO_INCREMENT,
  `user_id` bigint(20),
  `address` varchar(255),
  `detail_address` varchar(255),
  `is_default` tinyint(1),
  `created_by` bigint(20),
  `created_date` datetime,
  `updated_by` bigint(20),
  `updated_date` datetime,
  foreign key(user_id) references user(user_id)
) engine=innodb default charset=utf8 auto_increment=1;

--
-- Table structure for table `role_`
--

DROP TABLE IF EXISTS `role_`;

CREATE TABLE `role_` (
  `role_id` bigint(20) NOT NULL PRIMARY KEY AUTO_INCREMENT,
  `name` bigint(20)
) engine=innodb default charset=utf8 auto_increment=1;

--
-- Table structure for table `user_role`
--

DROP TABLE IF EXISTS `user_role`;

CREATE TABLE `user_role` (
  `user_id` bigint(20),
  `role_id` bigint(20),
  foreign key(user_id) references user(user_id),
  foreign key(role_id) references role_(role_id)
) engine=innodb default charset=utf8 auto_increment=1;

--
-- Table structure for table `store`
--

DROP TABLE IF EXISTS `store`;

CREATE TABLE `store` (
  `store_id` bigint(20) NOT NULL PRIMARY KEY AUTO_INCREMENT,
  `name` varchar(255),
  `address` varchar(255),
  `contact` varchar(20),
  `is_active` tinyint(1),
  `created_by` bigint(20),
  `created_date` datetime,
  `updated_by` bigint(20),
  `updated_date` datetime
) engine=innodb default charset=utf8 auto_increment=1;

--
-- Table structure for table `addreuser_storess`
--

DROP TABLE IF EXISTS `user_store`;

CREATE TABLE `user_store` (
  `user_id` bigint(20) ,
  `store_id` bigint(20),
  foreign key(user_id) references user(user_id),
  foreign key(store_id) references store(store_id)
) engine=innodb default charset=utf8 auto_increment=1;

--
-- Table structure for table `vip_level`
--

DROP TABLE IF EXISTS `vip_level`;

CREATE TABLE `vip_level` (
  `level_id` bigint(20) NOT NULL PRIMARY KEY AUTO_INCREMENT,
  `name` varchar(255),
  `description` varchar(255),
  `integral` bigint(11)
) engine=innodb default charset=utf8 auto_increment=1;

--
-- Table structure for table `vip_information`
--

DROP TABLE IF EXISTS `vip_information`;

CREATE TABLE `vip_information` (
  `id` bigint(20) NOT NULL PRIMARY KEY AUTO_INCREMENT,
  `user_id` bigint(20),
  `vip_id` bigint(20),
  `total_point` bigint(11),
  `current_point` bigint(11),
  `current_level` bigint(20),
  `balance` decimal(10, 2),
  foreign key(user_id) references user(user_id)
) engine=innodb default charset=utf8 auto_increment=1;

--
-- Table structure for table `vip_point_detail`
--

DROP TABLE IF EXISTS `vip_point_detail`;

CREATE TABLE `vip_point_detail` (
  `id` bigint(20) NOT NULL PRIMARY KEY AUTO_INCREMENT,
  `user_id` bigint(20),
  `point` bigint(11),
  `type` varchar(32),
  `comments` varchar(255),
  `created_date` datetime,
  foreign key(user_id) references user(user_id)
) engine=innodb default charset=utf8 auto_increment=1;

--
-- Table structure for table `order`
--

DROP TABLE IF EXISTS `order`;

CREATE TABLE `order` (
  `id` bigint(20) NOT NULL PRIMARY KEY AUTO_INCREMENT,
  `user_id` bigint(20),
  `store_id` bigint(20),
  `amount` decimal(10, 2),
  `pay_type` varchar(32),
  `created_by` bigint(20),
  `created_date` datetime,
  foreign key(user_id) references user(user_id),
  foreign key(store_id) references store(store_id)
) engine=innodb default charset=utf8 auto_increment=1;

--
-- Table structure for table `order_detail`
--

DROP TABLE IF EXISTS `order_detail`;

CREATE TABLE `order_detail` (
  `id` bigint(20) NOT NULL PRIMARY KEY AUTO_INCREMENT,
  `order_id` bigint(20),
  `bar_code` varchar(255),
  `product_name` varchar(255),
  `product_amount` decimal(10, 2),
  `product_count` bigint(10),
  `total_amount` decimal(10, 2),
  foreign key(order_id) references `order`(id)
) engine=innodb default charset=utf8 auto_increment=1;

--
-- Table structure for table `notification`
--

DROP TABLE IF EXISTS `notification`;

CREATE TABLE `notification` (
  `notification_id` bigint(20) NOT NULL PRIMARY KEY AUTO_INCREMENT,
  `subject` varchar(255),
  `content` text,
  `user_id` bigint(20),
  `created_by` bigint(20),
  `created_date` datetime,
  `updated_by` bigint(20),
  `updated_date` datetime,
  `read_date` datetime,
  foreign key(user_id) references user(user_id)
) engine=innodb default charset=utf8 auto_increment=1;

--
-- Table structure for table `message`
--

DROP TABLE IF EXISTS `message`;

CREATE TABLE `message` (
  `message_id` bigint(20) NOT NULL PRIMARY KEY AUTO_INCREMENT,
  `subject` varchar(255),
  `content` text,
  `created_by` bigint(20),
  `created_date` datetime,
  `updated_by` bigint(20),
  `updated_date` datetime,
  `read_date` datetime
) engine=innodb default charset=utf8 auto_increment=1;

--
-- Table structure for table `reply`
--

DROP TABLE IF EXISTS `reply`;

CREATE TABLE `reply` (
  `reply_id` bigint(20) NOT NULL PRIMARY KEY AUTO_INCREMENT,
  `message_id` bigint(20),
  `content` text,
  `created_by` bigint(20),
  `created_date` datetime,
  `updated_by` bigint(20),
  `updated_date` datetime,
  `read_date` datetime,
  foreign key(message_id) references message(message_id)
) engine=innodb default charset=utf8 auto_increment=1;

--
-- Table structure for table `recommend`
--

DROP TABLE IF EXISTS `recommend`;

CREATE TABLE `recommend` (
  `recommend_id` bigint(20) NOT NULL PRIMARY KEY AUTO_INCREMENT,
  `store_id` bigint(20),
  `name` varchar(255),
  `price` varchar(255),
  `price_type` varchar(20),
  `image` varchar(255),
  `amount` int(10),
  `is_active` tinyint(1),
  `start_time` datetime,
  `end_time` datetime,
  `created_by` bigint(20),
  `created_date` datetime,
  `updated_by` bigint(20),
  `updated_date` datetime,
  foreign key(store_id) references store(store_id)
) engine=innodb default charset=utf8 auto_increment=1;

--
-- Table structure for table `activity`
--

DROP TABLE IF EXISTS `activity`;

CREATE TABLE `activity` (
  `activity_id` bigint(20) NOT NULL PRIMARY KEY AUTO_INCREMENT,
  `name` varchar(255),
  `description` varchar(255),
  `is_active` tinyint(1),
  `image` varchar(255),
  `start_time` datetime,
  `end_time` datetime,
  `created_by` bigint(20),
  `created_date` datetime,
  `updated_by` bigint(20),
  `updated_date` datetime
) engine=innodb default charset=utf8 auto_increment=1;


--
-- Table structure for table `coupon`
--

DROP TABLE IF EXISTS `coupon`;

CREATE TABLE `coupon` (
  `coupon_id` bigint(20) NOT NULL PRIMARY KEY AUTO_INCREMENT,
  `name` varchar(255),
  `description` varchar(255),
  `total_amount` int(10),
  `reduce_amount` int(10),
  `type` varchar(255),
  `is_limit` tinyint(1),
  `limit_amount` int (10),
  `level_limit` int(10),
  `limit_count` int(10),
  `status` tinyint(2),
  `start_time` datetime,
  `end_time` datetime,
  `created_by` bigint(20),
  `created_date` datetime,
  `updated_by` bigint(20),
  `updated_date` datetime
) engine=innodb default charset=utf8 auto_increment=1;


--
-- Table structure for table `coupon_detail`
--

DROP TABLE IF EXISTS `coupon_detail`;

CREATE TABLE `coupon_detail` (
  `coupon_id` bigint(20) NOT NULL PRIMARY KEY AUTO_INCREMENT,
  `coupon_number` varchar(32),
  `is_available` tinyint(1)
) engine=innodb default charset=utf8 auto_increment=1;


--
-- Table structure for table `user_coupon`
--

DROP TABLE IF EXISTS `user_coupon`;

CREATE TABLE `user_coupon` (
  `id` bigint(20) NOT NULL PRIMARY KEY AUTO_INCREMENT,
  `user_id` bigint(20),
  `coupon_number` bigint(20),
  `is_used` tinyint(1),
  `image` varchar(255),
  `created_by` bigint(20),
  `created_date` datetime,
  `updated_by` bigint(20),
  `updated_date` datetime
) engine=innodb default charset=utf8 auto_increment=1;


--
-- Table structure for table `user_coupon_history`
--

DROP TABLE IF EXISTS `user_coupon_history`;

CREATE TABLE `user_coupon_history` (
  `id` bigint(20) NOT NULL PRIMARY KEY AUTO_INCREMENT,
  `vip_id` bigint(20),
  `coupon_id` bigint(20),
  `action_id` tinyint(2),
  `is_active` tinyint(1),
  `store_id` bigint(20),
  `created_by` bigint(20),
  `created_date` datetime,
  `updated_by` bigint(20),
  `updated_date` datetime,
  foreign key(coupon_id) references coupon(coupon_id)
) engine=innodb default charset=utf8 auto_increment=1;


--
-- Table structure for table `shopping_card`
--

DROP TABLE IF EXISTS `shopping_card`;

CREATE TABLE `shopping_card` (
  `card_id` bigint(20) NOT NULL PRIMARY KEY AUTO_INCREMENT,
  `name` varchar(255),
  `description` varchar(255),
  `total_amount` int(10),
  `denomination` int(10),
  `status` tinyint(2),
  `start_time` datetime,
  `end_time` datetime,
  `created_by` bigint(20),
  `created_date` datetime,
  `updated_by` bigint(20),
  `updated_date` datetime
) engine=innodb default charset=utf8 auto_increment=1;


--
-- Table structure for table `shopping_card_detail`
--

DROP TABLE IF EXISTS `shopping_card_detail`;

CREATE TABLE `shopping_card_detail` (
  `id` bigint(20) NOT NULL PRIMARY KEY AUTO_INCREMENT,
  `card_number` varchar(32),
  `card_password` varchar(32),
  `is_available` tinyint(1)
) engine=innodb default charset=utf8 auto_increment=1;


--
-- Table structure for table `user_card`
--

DROP TABLE IF EXISTS `user_card`;

CREATE TABLE `user_card` (
  `id` bigint(20) NOT NULL PRIMARY KEY AUTO_INCREMENT,
  `user_id` bigint(20),
  `card_number` varchar(32),
  `description` varchar(255),
  `is_used` tinyint(1),
  `created_by` bigint(20),
  `created_date` datetime,
  `updated_by` bigint(20),
  `updated_date` datetime,
  foreign key(user_id) references user(user_id)
) engine=innodb default charset=utf8 auto_increment=1;


--
-- Table structure for table `user_card_history`
--

DROP TABLE IF EXISTS `user_card_history`;

CREATE TABLE `user_card_history` (
  `id` bigint(20) NOT NULL PRIMARY KEY AUTO_INCREMENT,
  `user_id` bigint(20),
  `card_number` varchar(32),
  `store_id` bigint(20),
  `action_id` tinyint(2),
  `created_by` bigint(20),
  `created_date` datetime,
  `updated_by` bigint(20),
  `updated_date` datetime,
  foreign key(user_id) references user(user_id)
) engine=innodb default charset=utf8 auto_increment=1;


--
-- Table structure for table `api_log`
--

DROP TABLE IF EXISTS `api_log`;

CREATE TABLE `api_log` (
  `api_log_id` bigint(20) NOT NULL PRIMARY KEY AUTO_INCREMENT,
  `user_id` bigint(20),
  `ip_address` varchar(255),
  `device_id` varchar(255),
  `parameters` text,
  `runtime` text,
  `error_msg` text,
  `user_agent` text,
  `headers` text,
  `hostname` varchar(255),
  `forwarded_for` text,
  `api_class` text,
  `api_method` varchar(255),
  `session_id` varchar(255),
  `request_uri` text,
  `query_string` text,
  `http_method` varchar(255)
) engine=innodb default charset=utf8 auto_increment=1;


--
-- Table structure for table `jwt_token`
--

DROP TABLE IF EXISTS `jwt_token`;

CREATE TABLE `jwt_token` (
  `jwt_token_id` bigint(20) NOT NULL PRIMARY KEY AUTO_INCREMENT,
  `user_id` bigint(20),
  `token_value` varchar(255),
  `created_date` datetime,
  `expire_date` datetime
) engine=innodb default charset=utf8 auto_increment=1;

--
-- Table structure for table `token`
--

DROP TABLE IF EXISTS `token`;

CREATE TABLE `token` (
  `token_id` bigint(20) NOT NULL PRIMARY KEY AUTO_INCREMENT,
  `value_field` varchar(255),
  `user_id` bigint(20),
  `device_id` varchar(255),
  `login_date` datetime,
  `expire_date` datetime
) engine=innodb default charset=utf8 auto_increment=1;

--
-- Table structure for table `sms`
--

DROP TABLE IF EXISTS `sms`;

CREATE TABLE `sms` (
  `sms_id` bigint(20) NOT NULL PRIMARY KEY AUTO_INCREMENT,
  `phone` varchar(32),
  `validate_code` bigint(20),
  `login_date` datetime,
  `expire_date` datetime,
  `is_used` tinyint(1),
  `used_date` datetime
) engine=innodb default charset=utf8 auto_increment=1;



