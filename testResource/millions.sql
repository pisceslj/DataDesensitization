DROP TABLE IF EXISTS `vote_record_memory_10w`;
CREATE TABLE `vote_record_memory_10w` (
	`id` int(10) unsigned NOT NULL AUTO_INCREMENT,
	`user_id` varchar(20) NOT NULL DEFAULT '',
	`name` varchar(128) NOT NULL DEFAULT '',
	`idCard` varchar(18) NOT NULL DEFAULT '',
	`phone` varchar(50) DEFAULT NULL,
	`tel` varchar(16) DEFAULT NULL,
	`email` varchar(128) DEFAULT NULL,
	`addr` text NOT NULL,
    	`vote_num` int(10) unsigned NOT NULL DEFAULT '0',
    	`group_id` int(10) unsigned NOT NULL DEFAULT '0',
    	`status` tinyint(2) unsigned NOT NULL DEFAULT '1',
    	`create_time` varchar(128) NOT NULL,
    	PRIMARY KEY (`id`),
    	KEY `index_user_id` (`user_id`) USING HASH
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8;

DROP TABLE IF EXISTS `vote_record_10w`;
CREATE TABLE `vote_record_10w` (
	`id` int(10) unsigned NOT NULL AUTO_INCREMENT,
	`user_id` varchar(20) NOT NULL DEFAULT '' COMMENT '用户Id',
	`name` varchar(128) NOT NULL DEFAULT '' COMMENT '姓名',
	`idCard` varchar(18) NOT NULL DEFAULT '' COMMENT '身份证号',
	`phone` varchar(50) DEFAULT NULL COMMENT '手机号',
	`tel` varchar(16) DEFAULT NULL COMMENT '固定电话',
	`email` varchar(128) DEFAULT NULL COMMENT '邮箱',
	`addr` text NOT NULL COMMENT '住址',
	`vote_num` int(10) unsigned NOT NULL DEFAULT '0' COMMENT '投票数',
	`group_id` int(10) unsigned NOT NULL DEFAULT '0' COMMENT '用户组id 0-未激活用户 1-普通用户 2-vip用户 3-管理员用户',
	`status` tinyint(2) unsigned NOT NULL DEFAULT '1' COMMENT '状态 1-正常 2-已删除',
	`create_time` varchar(128) NOT NULL COMMENT '创建时间',
	PRIMARY KEY (`id`),
	KEY `index_user_id` (`user_id`) USING HASH COMMENT '用户ID哈希索引'
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='投票记录表';

DELIMITER // 
DROP FUNCTION IF EXISTS `rand_string_10w` //
SET NAMES utf8 //
CREATE FUNCTION `rand_string_10w` (n INT) RETURNS VARCHAR(255) CHARSET 'utf8'
BEGIN 
    DECLARE char_str varchar(100) DEFAULT 'abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789';
    DECLARE return_str varchar(255) DEFAULT '';
    DECLARE i INT DEFAULT 0;
    WHILE i < n DO
        SET return_str = concat(return_str, substring(char_str, FLOOR(1 + RAND()*62), 1));
        SET i = i+1;
    END WHILE;
    RETURN return_str;
END //

DROP PROCEDURE IF EXISTS `add_vote_record_memory_10w` //
CREATE PROCEDURE `add_vote_record_memory_10w`(IN n INT)
BEGIN
    DECLARE i INT DEFAULT 1;
    DECLARE name VARCHAR(128);
    DECLARE idCard VARCHAR(18);
    DECLARE phone VARCHAR(50);
    DECLARE tel VARCHAR(16);
    DECLARE email VARCHAR(128);
    DECLARE addr TEXT;
    DECLARE vote_num INT DEFAULT 0;
    DECLARE group_id INT DEFAULT 0;
    DECLARE status TINYINT DEFAULT 1;
    WHILE i < n DO
        SET name = '李思思';
        SET idCard = '370784198905019989';
        SET phone = '13402889001';
        SET tel = '010-39000865';
        SET email = 'liuxiaodong@ict.ac.cn';
        SET addr = '北京市海淀区科学院南路6号中科院计算所';
        SET vote_num = FLOOR(1 + RAND() * 10000);
        SET group_id = FLOOR(0 + RAND()*3);
        SET status = FLOOR(1 + RAND()*2);
        INSERT INTO `vote_record_memory_10w` VALUES (NULL, rand_string_10w(20), name, idCard, phone, tel, email, addr, vote_num, group_id, status, NOW());
        SET i = i + 1;
    END WHILE;
END //
DELIMITER ;  

CALL add_vote_record_memory_10w(100000);

SELECT count(*) FROM `vote_record_memory_10w`;

INSERT INTO vote_record_10w SELECT * FROM `vote_record_memory_10w`;

