SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0;
SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0;
SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='TRADITIONAL,ALLOW_INVALID_DATES';

DROP SCHEMA IF EXISTS `db_os_users` ;
CREATE SCHEMA IF NOT EXISTS `db_os_users` DEFAULT CHARACTER SET utf8 COLLATE utf8_general_ci ;
USE `db_os_users` ;

-- -----------------------------------------------------
-- Table `db_os_users`.`usuarios`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `db_os_users`.`usuarios` ;

CREATE TABLE IF NOT EXISTS `db_os_users`.`usuarios` (
  `id_user` INT NOT NULL AUTO_INCREMENT,
  `username` VARCHAR(45) NOT NULL,
  `password` VARCHAR(45) NOT NULL,
  `nombre_completo` VARCHAR(128) NOT NULL,
  `foto` VARCHAR(1000) NULL,
  `estado` INT NOT NULL DEFAULT 0,
  PRIMARY KEY (`id_user`))
ENGINE = InnoDB;

CREATE UNIQUE INDEX `username_UNIQUE` ON `db_os_users`.`usuarios` (`username` ASC);


-- -----------------------------------------------------
-- Table `db_os_users`.`mensajes`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `db_os_users`.`mensajes` ;

CREATE TABLE IF NOT EXISTS `db_os_users`.`mensajes` (
  `id_user1` INT NOT NULL,
  `id_user2` INT NOT NULL,
  `mensaje` TEXT NOT NULL,
  `hora` TIMESTAMP NOT NULL,
  PRIMARY KEY (`id_user1`, `id_user2`, `hora`),
  CONSTRAINT `message_emisor`
    FOREIGN KEY (`id_user1`)
    REFERENCES `db_os_users`.`usuarios` (`id_user`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `message_receptor`
    FOREIGN KEY (`id_user2`)
    REFERENCES `db_os_users`.`usuarios` (`id_user`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;

CREATE INDEX `message_receptor_idx` ON `db_os_users`.`mensajes` (`id_user2` ASC);


-- -----------------------------------------------------
-- Table `db_os_users`.`amigos`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `db_os_users`.`amigos` ;

CREATE TABLE IF NOT EXISTS `db_os_users`.`amigos` (
  `id_user` INT NOT NULL,
  `id_user2` INT NOT NULL,
  PRIMARY KEY (`id_user`, `id_user2`),
  CONSTRAINT `amigo_user1`
    FOREIGN KEY (`id_user`)
    REFERENCES `db_os_users`.`usuarios` (`id_user`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `amigo_user2`
    FOREIGN KEY (`id_user2`)
    REFERENCES `db_os_users`.`usuarios` (`id_user`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;

CREATE INDEX `amigo_user2_idx` ON `db_os_users`.`amigos` (`id_user2` ASC);


-- -----------------------------------------------------
-- Table `db_os_users`.`comentarios`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `db_os_users`.`comentarios` ;

CREATE TABLE IF NOT EXISTS `db_os_users`.`comentarios` (
  `id` INT NOT NULL AUTO_INCREMENT,
  `id_user` INT NOT NULL,
  `comentario` TEXT NULL,
  `imagen` VARCHAR(1000) NULL,
  PRIMARY KEY (`id`),
  CONSTRAINT `comment_user`
    FOREIGN KEY (`id_user`)
    REFERENCES `db_os_users`.`usuarios` (`id_user`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;

CREATE INDEX `comment_user_idx` ON `db_os_users`.`comentarios` (`id_user` ASC);


-- -----------------------------------------------------
-- Table `db_os_users`.`likes_users`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `db_os_users`.`likes_users` ;

CREATE TABLE IF NOT EXISTS `db_os_users`.`likes_users` (
  `id` INT NOT NULL AUTO_INCREMENT,
  `id_user` INT NOT NULL,
  `id_user2` INT NOT NULL,
  PRIMARY KEY (`id`),
  CONSTRAINT `like_user1`
    FOREIGN KEY (`id_user`)
    REFERENCES `db_os_users`.`usuarios` (`id_user`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `like_user2`
    FOREIGN KEY (`id_user2`)
    REFERENCES `db_os_users`.`usuarios` (`id_user`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;

CREATE INDEX `like_user1_idx` ON `db_os_users`.`likes_users` (`id_user` ASC);

CREATE INDEX `like_user2_idx` ON `db_os_users`.`likes_users` (`id_user2` ASC);


-- -----------------------------------------------------
-- Table `db_os_users`.`likes_comments`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `db_os_users`.`likes_comments` ;

CREATE TABLE IF NOT EXISTS `db_os_users`.`likes_comments` (
  `id` INT NOT NULL AUTO_INCREMENT,
  `user` INT NOT NULL,
  `comment` INT NOT NULL,
  PRIMARY KEY (`id`),
  CONSTRAINT `like_comment_user`
    FOREIGN KEY (`user`)
    REFERENCES `db_os_users`.`usuarios` (`id_user`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `like_comment_comment`
    FOREIGN KEY (`comment`)
    REFERENCES `db_os_users`.`comentarios` (`id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;

CREATE INDEX `like_comment_user_idx` ON `db_os_users`.`likes_comments` (`user` ASC);

CREATE INDEX `like_comment_comment_idx` ON `db_os_users`.`likes_comments` (`comment` ASC);


-- -----------------------------------------------------
-- Table `db_os_users`.`solicitud`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `db_os_users`.`solicitud` ;

CREATE TABLE IF NOT EXISTS `db_os_users`.`solicitud` (
  `id` INT NOT NULL AUTO_INCREMENT,
  `id_user1` INT NOT NULL,
  `id_user2` INT NOT NULL,
  `activo` INT NOT NULL,
  `fecha` DATETIME NULL DEFAULT now(),
  PRIMARY KEY (`id`),
  CONSTRAINT `solicitud_destinatario`
    FOREIGN KEY (`id_user1`)
    REFERENCES `db_os_users`.`usuarios` (`id_user`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `solicitud_emisor`
    FOREIGN KEY (`id_user2`)
    REFERENCES `db_os_users`.`usuarios` (`id_user`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;

CREATE INDEX `solicitud_destinatario_idx` ON `db_os_users`.`solicitud` (`id_user1` ASC);

CREATE INDEX `solicitud_emisor_idx` ON `db_os_users`.`solicitud` (`id_user2` ASC);


SET SQL_MODE=@OLD_SQL_MODE;
SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS;
SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS;

-- -----------------------------------------------------
-- Data for table `db_os_users`.`usuarios`
-- -----------------------------------------------------
START TRANSACTION;
USE `db_os_users`;
INSERT INTO `db_os_users`.`usuarios` (`id_user`, `username`, `password`, `nombre_completo`, `foto`, `estado`) VALUES (1, 'user1', '1234', 'Usuario 1', '', 0);
INSERT INTO `db_os_users`.`usuarios` (`id_user`, `username`, `password`, `nombre_completo`, `foto`, `estado`) VALUES (2, 'user2', '1234', 'Usuario 2', '', 0);

COMMIT;


-- -----------------------------------------------------
-- Data for table `db_os_users`.`solicitud`
-- -----------------------------------------------------
START TRANSACTION;
USE `db_os_users`;
INSERT INTO `db_os_users`.`solicitud` (`id`, `id_user1`, `id_user2`, `activo`, `fecha`) VALUES (1, 1, 2, 1, 'now()');

COMMIT;

