CREATE SCHEMA IF NOT EXISTS `coredb`;
USE `coredb`;

CREATE TABLE IF NOT EXISTS `member`
(
    `id`             int      NOT NULL AUTO_INCREMENT,
    `created_at`     datetime     DEFAULT NULL,
    `updated_at`     datetime     DEFAULT NULL,
    `birthday`       date         DEFAULT NULL,
    `fcm_token`      varchar(255) DEFAULT NULL,
    `gender`         varchar(255) DEFAULT NULL,
    `image_path`     varchar(255) DEFAULT NULL,
    `intro`          varchar(20)  DEFAULT NULL,
    `nickname`       varchar(6)   DEFAULT NULL,
    `password`       varchar(255) DEFAULT NULL,
    `phone_number`   varchar(255) DEFAULT NULL,
    `authority`      varchar(255) DEFAULT NULL,
    `share_location` bit       DEFAULT NULL,
    `uuid`           varchar(255) NOT NULL,
    PRIMARY KEY (`id`),
    UNIQUE  (`uuid`),
    UNIQUE  (`nickname`)
);

