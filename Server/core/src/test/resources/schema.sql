CREATE SCHEMA IF NOT EXISTS `coredb`;
USE `coredb`;

CREATE TABLE IF NOT EXISTS `member`
(
    `id`             INT(11) NOT NULL AUTO_INCREMENT,
    `created_at`     datetime DEFAULT NULL,
    `updated_at`     datetime DEFAULT NULL,
    PRIMARY KEY (`id`)
);