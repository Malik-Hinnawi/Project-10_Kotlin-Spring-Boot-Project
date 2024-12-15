DROP TABLE IF EXISTS `authors`;
CREATE TABLE `authors` (
  `id` bigint AUTO_INCREMENT PRIMARY KEY NOT NULL,
  `age` smallint,
  `description` VARCHAR(512),
  `image` VARCHAR(512),
   `name` VARCHAR(512)
);

DROP TABLE IF EXISTS `books`;
CREATE TABLE `books` (
    `isbn` VARCHAR(19) PRIMARY KEY NOT NULL,
    `description` VARCHAR(2048),
    `title` VARCHAR(512),
    `author_id` bigint NOT NULL REFERENCES `authors` (`id`),
    `image` VARCHAR(512)
);