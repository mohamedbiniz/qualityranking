SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0;
SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0;
SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='TRADITIONAL';

CREATE SCHEMA IF NOT EXISTS `ireval` DEFAULT CHARACTER SET latin1 COLLATE latin1_swedish_ci ;
USE `ireval`;

-- -----------------------------------------------------
-- Table `ireval`.`experiment`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `ireval`.`experiment` ;

CREATE  TABLE IF NOT EXISTS `ireval`.`experiment` (
  `id` INT NOT NULL AUTO_INCREMENT ,
  `subject` VARCHAR(100) NOT NULL ,
  `evaluations_per_document` INT NOT NULL ,
  `documents_per_evaluator` INT NOT NULL ,
  `researcher_email` VARCHAR(255) NOT NULL ,
  `status` CHAR(1) NOT NULL DEFAULT 'O' ,
  PRIMARY KEY (`id`) )
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `ireval`.`query`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `ireval`.`query` ;

CREATE  TABLE IF NOT EXISTS `ireval`.`query` (
  `id` INT NOT NULL AUTO_INCREMENT ,
  `experiment_id` INT NOT NULL ,
  `description` VARCHAR(200) NOT NULL ,
  PRIMARY KEY (`id`) ,
  INDEX fk_experiment_query (`experiment_id` ASC) ,
  CONSTRAINT `fk_experiment_query`
    FOREIGN KEY (`experiment_id` )
    REFERENCES `ireval`.`experiment` (`id` )
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `ireval`.`document`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `ireval`.`document` ;

CREATE  TABLE IF NOT EXISTS `ireval`.`document` (
  `id` INT NOT NULL AUTO_INCREMENT ,
  `experiment_id` INT NOT NULL ,
  `url` VARCHAR(255) NOT NULL ,
  PRIMARY KEY (`id`) ,
  INDEX fk_experiment_document (`experiment_id` ASC) ,
  CONSTRAINT `fk_experiment_document`
    FOREIGN KEY (`experiment_id` )
    REFERENCES `ireval`.`experiment` (`id` )
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `ireval`.`evaluator`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `ireval`.`evaluator` ;

CREATE  TABLE IF NOT EXISTS `ireval`.`evaluator` (
  `id` INT NOT NULL AUTO_INCREMENT ,
  `experiment_id` INT NOT NULL ,
  `email` VARCHAR(255) NOT NULL ,
  PRIMARY KEY (`id`) ,
  INDEX fk_experiment_evaluator (`experiment_id` ASC) ,
  CONSTRAINT `fk_experiment_evaluator`
    FOREIGN KEY (`experiment_id` )
    REFERENCES `ireval`.`experiment` (`id` )
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `ireval`.`experiment_evaluation`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `ireval`.`experiment_evaluation` ;

CREATE  TABLE IF NOT EXISTS `ireval`.`experiment_evaluation` (
  `experiment_id` INT NOT NULL ,
  `evaluator_id` INT NOT NULL ,
  `knowledge_rating_id` INT NULL ,
  `start_datetime` DATETIME NULL ,
  `end_datetime` DATETIME NULL ,
  PRIMARY KEY (`experiment_id`, `evaluator_id`) ,
  INDEX fk_experiment_experiment_evaluation (`experiment_id` ASC) ,
  INDEX fk_evaluator_experiment_evaluation (`evaluator_id` ASC) ,
  CONSTRAINT `fk_experiment_experiment_evaluation`
    FOREIGN KEY (`experiment_id` )
    REFERENCES `ireval`.`experiment` (`id` )
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `fk_evaluator_experiment_evaluation`
    FOREIGN KEY (`evaluator_id` )
    REFERENCES `ireval`.`evaluator` (`id` )
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `ireval`.`linguistic_variable`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `ireval`.`linguistic_variable` ;

CREATE  TABLE IF NOT EXISTS `ireval`.`linguistic_variable` (
  `id` INT NOT NULL AUTO_INCREMENT ,
  `experiment_id` INT NOT NULL ,
  `name` VARCHAR(50) NOT NULL ,
  `description` VARCHAR(200) NOT NULL ,
  PRIMARY KEY (`id`) ,
  INDEX fk_experiment_linguistic_variable (`experiment_id` ASC) ,
  CONSTRAINT `fk_experiment_linguistic_variable`
    FOREIGN KEY (`experiment_id` )
    REFERENCES `ireval`.`experiment` (`id` )
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `ireval`.`linguistic_term`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `ireval`.`linguistic_term` ;

CREATE  TABLE IF NOT EXISTS `ireval`.`linguistic_term` (
  `id` INT NOT NULL AUTO_INCREMENT ,
  `experiment_id` INT NOT NULL ,
  `name` VARCHAR(50) NOT NULL ,
  PRIMARY KEY (`id`) ,
  INDEX fk_experiment_linguistic_term (`experiment_id` ASC) ,
  CONSTRAINT `fk_experiment_linguistic_term`
    FOREIGN KEY (`experiment_id` )
    REFERENCES `ireval`.`experiment` (`id` )
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `ireval`.`knowledge_rating`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `ireval`.`knowledge_rating` ;

CREATE  TABLE IF NOT EXISTS `ireval`.`knowledge_rating` (
  `id` INT NOT NULL AUTO_INCREMENT ,
  `experiment_id` INT NOT NULL ,
  `name` VARCHAR(50) NOT NULL ,
  PRIMARY KEY (`id`) ,
  INDEX fk_experiment_knowledge_rating (`experiment_id` ASC) ,
  CONSTRAINT `fk_experiment_knowledge_rating`
    FOREIGN KEY (`experiment_id` )
    REFERENCES `ireval`.`experiment` (`id` )
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `ireval`.`document_evaluation`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `ireval`.`document_evaluation` ;

CREATE  TABLE IF NOT EXISTS `ireval`.`document_evaluation` (
  `document_id` INT NOT NULL ,
  `evaluator_id` INT NOT NULL ,
  `linguistic_variable_id` INT NOT NULL ,
  `linguistic_term_id` INT NULL ,
  PRIMARY KEY (`document_id`, `evaluator_id`, `linguistic_variable_id`) ,
  INDEX fk_document_document_evaluation (`document_id` ASC) ,
  INDEX fk_evaluator_document_evaluation (`evaluator_id` ASC) ,
  INDEX fk_linguistic_variable_document_evaluation (`linguistic_variable_id` ASC) ,
  INDEX fk_linguistic_term_document_evaluation (`linguistic_term_id` ASC) ,
  CONSTRAINT `fk_document_document_evaluation`
    FOREIGN KEY (`document_id` )
    REFERENCES `ireval`.`document` (`id` )
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `fk_evaluator_document_evaluation`
    FOREIGN KEY (`evaluator_id` )
    REFERENCES `ireval`.`evaluator` (`id` )
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `fk_linguistic_variable_document_evaluation`
    FOREIGN KEY (`linguistic_variable_id` )
    REFERENCES `ireval`.`linguistic_variable` (`id` )
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `fk_linguistic_term_document_evaluation`
    FOREIGN KEY (`linguistic_term_id` )
    REFERENCES `ireval`.`linguistic_term` (`id` )
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `ireval`.`query_evaluation`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `ireval`.`query_evaluation` ;

CREATE  TABLE IF NOT EXISTS `ireval`.`query_evaluation` (
  `document_id` INT NOT NULL ,
  `query_id` INT NOT NULL ,
  `evaluator_id` INT NOT NULL ,
  `relevant` BOOLEAN NULL ,
  PRIMARY KEY (`document_id`, `query_id`, `evaluator_id`) ,
  INDEX fk_document_query_evaluation (`document_id` ASC) ,
  INDEX fk_query_query_evaluation (`query_id` ASC) ,
  INDEX fk_evaluator_query_evaluation (`evaluator_id` ASC) ,
  CONSTRAINT `fk_document_query_evaluation`
    FOREIGN KEY (`document_id` )
    REFERENCES `ireval`.`document` (`id` )
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `fk_query_query_evaluation`
    FOREIGN KEY (`query_id` )
    REFERENCES `ireval`.`query` (`id` )
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `fk_evaluator_query_evaluation`
    FOREIGN KEY (`evaluator_id` )
    REFERENCES `ireval`.`evaluator` (`id` )
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;



SET SQL_MODE=@OLD_SQL_MODE;
SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS;
SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS;
