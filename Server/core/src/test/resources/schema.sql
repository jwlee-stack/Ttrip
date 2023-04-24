CREATE SCHEMA IF NOT EXISTS `coredb`;
USE `coredb`;

CREATE TABLE  IF NOT EXISTS `member`(
                                        `id` int(11) NOT NULL AUTO_INCREMENT,
                                        `created_at` datetime DEFAULT NULL,
                                        `updated_at` datetime DEFAULT NULL,
                                        `birthday` date DEFAULT NULL,
                                        `fcm_token` varchar(255) DEFAULT NULL,
                                        `gender` varchar(255) DEFAULT NULL,
                                        `image_path` varchar(255) DEFAULT NULL,
                                        `intro` varchar(20) DEFAULT NULL,
                                        `nickname` varchar(6) DEFAULT NULL,
                                        `password` varchar(255) DEFAULT NULL,
                                        `phone_number` varchar(255) DEFAULT NULL,
                                        `share_location` bit(1) DEFAULT NULL,
                                        `uuid` varchar(255) NOT NULL,
                                        PRIMARY KEY (`id`),
                                        UNIQUE KEY `UK_errlnismyy5xmphdt6e4l0oi1` (`uuid`),
                                        UNIQUE KEY `UK_hh9kg6jti4n1eoiertn2k6qsc` (`nickname`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

