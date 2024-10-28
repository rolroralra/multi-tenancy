-- test.note_entity definition

CREATE TABLE IF NOT EXISTS `NOTE` (
    `id` bigint NOT NULL AUTO_INCREMENT,
    `insert_at` datetime(6) DEFAULT NULL,
    `update_at` datetime(6) DEFAULT NULL,
    `title` varchar(255) DEFAULT NULL,
    `content` varchar(255) DEFAULT NULL,
    PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;