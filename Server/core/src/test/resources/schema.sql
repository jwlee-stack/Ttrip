CREATE SCHEMA IF NOT EXISTS `coredb`;
USE `coredb`;

CREATE TABLE IF NOT EXISTS `member`
(
    `id`               int(11)  NOT NULL AUTO_INCREMENT,
    `created_at`       datetime     DEFAULT NULL,
    `updated_at`       datetime     DEFAULT NULL,
    `birthday`         date         DEFAULT NULL,
    `fcm_token`        varchar(255) DEFAULT NULL,
    `gender`           varchar(255) DEFAULT NULL,
    `image_path`       varchar(255) DEFAULT NULL,
    `nickname`         varchar(6)   DEFAULT NULL,
    `password`         varchar(255) DEFAULT NULL,
    `phone_number`     varchar(255) DEFAULT NULL,
    `share_location`   tinyint(1)   DEFAULT 0,
    `uuid`             char(36) NOT NULL,
    `authority`        varchar(255) DEFAULT NULL,
    `intro`            varchar(20)  DEFAULT NULL,
    `marker_img_path`  varchar(255) DEFAULT NULL,
    `profile_img_path` varchar(255) DEFAULT NULL,
    `age`              tinyint(4)   DEFAULT 0,
    PRIMARY KEY (`id`),
    UNIQUE KEY `UK_errlnismyy5xmphdt6e4l0oi1` (`uuid`),
    UNIQUE KEY `UK_hh9kg6jti4n1eoiertn2k6qsc` (`nickname`)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_unicode_ci;

CREATE TABLE IF NOT EXISTS `survey`
(
    `id`                              int(11) NOT NULL AUTO_INCREMENT,
    `created_at`                      datetime DEFAULT NULL,
    `updated_at`                      datetime DEFAULT NULL,
    `prefer_cheap_hotel_than_comfort` float    DEFAULT NULL,
    `prefer_cheap_traffic`            float    DEFAULT NULL,
    `prefer_direct_flight`            float    DEFAULT NULL,
    `prefer_good_food`                float    DEFAULT NULL,
    `prefer_nature_than_city`         float    DEFAULT NULL,
    `prefer_personal_budget`          float    DEFAULT NULL,
    `prefer_plan`                     float    DEFAULT NULL,
    `prefer_shopping_than_tour`       float    DEFAULT NULL,
    `prefer_tight_schedule`           float    DEFAULT NULL,
    PRIMARY KEY (`id`)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_unicode_ci;