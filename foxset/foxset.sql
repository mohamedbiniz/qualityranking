SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0;
SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0;
SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='TRADITIONAL';

CREATE SCHEMA IF NOT EXISTS `newfoxset` DEFAULT CHARACTER SET latin1 COLLATE latin1_general_ci ;
USE `newfoxset`;

-- -----------------------------------------------------
-- Table `newfoxset`.`language`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `newfoxset`.`language` ;

CREATE  TABLE IF NOT EXISTS `newfoxset`.`language` (
  `id` INT NOT NULL AUTO_INCREMENT ,
  `name` VARCHAR(50) NOT NULL ,
  PRIMARY KEY (`id`) )
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Data for table `newfoxset`.`language`
-- -----------------------------------------------------
SET AUTOCOMMIT=0;
INSERT INTO `language` (`id`, `name`) VALUES (1, 'Português (Brasil)');
INSERT INTO `language` (`id`, `name`) VALUES (2, 'English (US)');

COMMIT;

-- -----------------------------------------------------
-- Table `newfoxset`.`collaborator`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `newfoxset`.`collaborator` ;

CREATE  TABLE IF NOT EXISTS `newfoxset`.`collaborator` (
  `id` INT NOT NULL AUTO_INCREMENT ,
  `username` VARCHAR(50) NOT NULL ,
  `password` VARCHAR(50) NOT NULL ,
  `name` VARCHAR(100) NOT NULL ,
  `email` VARCHAR(100) NOT NULL ,
  `active` BOOLEAN NOT NULL DEFAULT 1 ,
  `administrator` BOOLEAN NOT NULL DEFAULT 0 ,
  `coordinator` BOOLEAN NOT NULL DEFAULT 0 ,
  PRIMARY KEY (`id`) )
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Data for table `newfoxset`.`collaborator`
-- -----------------------------------------------------
SET AUTOCOMMIT=0;
INSERT INTO `collaborator` (`id`, `username`, `password`, `name`, `email`, `active`, `administrator`, `coordinator`) VALUES (1, 'foxset', '9efba686a780dd8cf1751c682d17a0c4', 'FoxSet', 'foxset@cos.ufrj.br', 1, 1, 1);

COMMIT;

-- -----------------------------------------------------
-- Table `newfoxset`.`dataset`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `newfoxset`.`dataset` ;

CREATE  TABLE IF NOT EXISTS `newfoxset`.`dataset` (
  `id` INT NOT NULL AUTO_INCREMENT ,
  `context` VARCHAR(50) NOT NULL ,
  `description` TEXT NOT NULL ,
  `creation_datetime` DATETIME NOT NULL ,
  `collaborator_id` INT NOT NULL ,
  `language_id` INT NOT NULL ,
  `min_pages` INT NOT NULL DEFAULT 0 ,
  `status` CHAR(1) NOT NULL DEFAULT 'C' ,
  `finalization_datetime` DATETIME NULL ,
  `manual_evaluation_strategy` CHAR(1) NOT NULL DEFAULT 'D' ,
  `method` CHAR(1) NOT NULL DEFAULT 'C' ,
  `p_of_n` INT NULL ,
  PRIMARY KEY (`id`) ,
  INDEX fk_dataset_language (`language_id` ASC) ,
  INDEX fk_dataset_collaborartor (`collaborator_id` ASC) ,
  CONSTRAINT `fk_dataset_language`
    FOREIGN KEY (`language_id` )
    REFERENCES `newfoxset`.`language` (`id` )
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `fk_dataset_collaborartor`
    FOREIGN KEY (`collaborator_id` )
    REFERENCES `newfoxset`.`collaborator` (`id` )
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `newfoxset`.`query`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `newfoxset`.`query` ;

CREATE  TABLE IF NOT EXISTS `newfoxset`.`query` (
  `id` INT NOT NULL AUTO_INCREMENT ,
  `dataset_id` INT NOT NULL ,
  `collaborator_id` INT NOT NULL ,
  `query` VARCHAR(255) NOT NULL ,
  `description` TEXT NOT NULL ,
  `active` BOOLEAN NOT NULL DEFAULT 1 ,
  `creation_datetime` DATETIME NOT NULL ,
  PRIMARY KEY (`id`) ,
  INDEX fk_query_dataset (`dataset_id` ASC) ,
  INDEX fk_query_collaborator (`collaborator_id` ASC) ,
  CONSTRAINT `fk_query_dataset`
    FOREIGN KEY (`dataset_id` )
    REFERENCES `newfoxset`.`dataset` (`id` )
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `fk_query_collaborator`
    FOREIGN KEY (`collaborator_id` )
    REFERENCES `newfoxset`.`collaborator` (`id` )
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `newfoxset`.`dataset_collaborator`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `newfoxset`.`dataset_collaborator` ;

CREATE  TABLE IF NOT EXISTS `newfoxset`.`dataset_collaborator` (
  `dataset_id` INT NOT NULL ,
  `collaborator_id` INT NOT NULL ,
  `role` CHAR(1) NOT NULL DEFAULT 'E' ,
  PRIMARY KEY (`dataset_id`, `collaborator_id`) ,
  INDEX fk_dataset_collaborator_dataset (`dataset_id` ASC) ,
  INDEX fk_dataset_collaborator_collaborator (`collaborator_id` ASC) ,
  CONSTRAINT `fk_dataset_collaborator_dataset`
    FOREIGN KEY (`dataset_id` )
    REFERENCES `newfoxset`.`dataset` (`id` )
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `fk_dataset_collaborator_collaborator`
    FOREIGN KEY (`collaborator_id` )
    REFERENCES `newfoxset`.`collaborator` (`id` )
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `newfoxset`.`quality_dimension`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `newfoxset`.`quality_dimension` ;

CREATE  TABLE IF NOT EXISTS `newfoxset`.`quality_dimension` (
  `id` INT NOT NULL AUTO_INCREMENT ,
  `name` VARCHAR(50) NOT NULL ,
  `code` CHAR(3) NOT NULL ,
  PRIMARY KEY (`id`) )
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Data for table `newfoxset`.`quality_dimension`
-- -----------------------------------------------------
SET AUTOCOMMIT=0;
INSERT INTO `quality_dimension` (`id`, `name`, `code`) VALUES (1, 'Completeness', 'COM');
INSERT INTO `quality_dimension` (`id`, `name`, `code`) VALUES (2, 'Reputation', 'REP');
INSERT INTO `quality_dimension` (`id`, `name`, `code`) VALUES (3, 'Timeliness', 'TIM');
INSERT INTO `quality_dimension` (`id`, `name`, `code`) VALUES (4, 'Security', 'SEC');

COMMIT;

-- -----------------------------------------------------
-- Table `newfoxset`.`quality_dimension_weight`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `newfoxset`.`quality_dimension_weight` ;

CREATE  TABLE IF NOT EXISTS `newfoxset`.`quality_dimension_weight` (
  `id` INT NOT NULL AUTO_INCREMENT ,
  `weight` INT NOT NULL ,
  `description` VARCHAR(100) NOT NULL ,
  PRIMARY KEY (`id`) )
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Data for table `newfoxset`.`quality_dimension_weight`
-- -----------------------------------------------------
SET AUTOCOMMIT=0;
INSERT INTO `quality_dimension_weight` (`id`, `weight`, `description`) VALUES (1, 0, 'Indicates that the evaluated quality dimension has no importance.');
INSERT INTO `quality_dimension_weight` (`id`, `weight`, `description`) VALUES (2, 1, 'Indicates that the evaluated quality dimension has a small importance.');
INSERT INTO `quality_dimension_weight` (`id`, `weight`, `description`) VALUES (3, 2, 'Indicates that the evaluated quality dimension is important in some circumstances, but not always.');
INSERT INTO `quality_dimension_weight` (`id`, `weight`, `description`) VALUES (4, 3, 'Indicates that the evaluated quality dimension is very important.');
INSERT INTO `quality_dimension_weight` (`id`, `weight`, `description`) VALUES (5, 4, 'Indicates that the evaluated quality dimension is essential.');

COMMIT;

-- -----------------------------------------------------
-- Table `newfoxset`.`context_quality_dimension_weight`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `newfoxset`.`context_quality_dimension_weight` ;

CREATE  TABLE IF NOT EXISTS `newfoxset`.`context_quality_dimension_weight` (
  `dataset_id` INT NOT NULL ,
  `quality_dimension_id` INT NOT NULL ,
  `quality_dimension_weight_id` INT NOT NULL ,
  PRIMARY KEY (`dataset_id`, `quality_dimension_id`, `quality_dimension_weight_id`) ,
  INDEX fk_context_quality_dimension_weight_dataset (`dataset_id` ASC) ,
  INDEX fk_context_quality_dimension_weight_quality_dimension (`quality_dimension_id` ASC) ,
  INDEX fk_context_quality_dimension_weight_quality_dimension_weight (`quality_dimension_weight_id` ASC) ,
  CONSTRAINT `fk_context_quality_dimension_weight_dataset`
    FOREIGN KEY (`dataset_id` )
    REFERENCES `newfoxset`.`dataset` (`id` )
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `fk_context_quality_dimension_weight_quality_dimension`
    FOREIGN KEY (`quality_dimension_id` )
    REFERENCES `newfoxset`.`quality_dimension` (`id` )
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `fk_context_quality_dimension_weight_quality_dimension_weight`
    FOREIGN KEY (`quality_dimension_weight_id` )
    REFERENCES `newfoxset`.`quality_dimension_weight` (`id` )
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `newfoxset`.`assessment_scale`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `newfoxset`.`assessment_scale` ;

CREATE  TABLE IF NOT EXISTS `newfoxset`.`assessment_scale` (
  `id` INT NOT NULL AUTO_INCREMENT ,
  `dataset_id` INT NOT NULL ,
  `name` VARCHAR(50) NOT NULL ,
  `order` INT NOT NULL ,
  `relevant` BOOLEAN NOT NULL DEFAULT 0 ,
  PRIMARY KEY (`id`) ,
  INDEX fk_assesment_scale_dataset (`dataset_id` ASC) ,
  CONSTRAINT `fk_assesment_scale_dataset`
    FOREIGN KEY (`dataset_id` )
    REFERENCES `newfoxset`.`dataset` (`id` )
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `newfoxset`.`document`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `newfoxset`.`document` ;

CREATE  TABLE IF NOT EXISTS `newfoxset`.`document` (
  `id` INT NOT NULL AUTO_INCREMENT ,
  `dataset_id` INT NOT NULL ,
  `url` VARCHAR(255) NOT NULL ,
  `active` BOOLEAN NOT NULL DEFAULT 1 ,
  `score` DECIMAL(6,5) NULL ,
  PRIMARY KEY (`id`) ,
  INDEX fk_document_dataset (`dataset_id` ASC) ,
  CONSTRAINT `fk_document_dataset`
    FOREIGN KEY (`dataset_id` )
    REFERENCES `newfoxset`.`dataset` (`id` )
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `newfoxset`.`answer`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `newfoxset`.`answer` ;

CREATE  TABLE IF NOT EXISTS `newfoxset`.`answer` (
  `document_id` INT NOT NULL ,
  `query_id` INT NOT NULL ,
  `collaborator_id` INT NOT NULL ,
  `assessment_scale_id` INT NOT NULL ,
  PRIMARY KEY (`document_id`, `query_id`) ,
  INDEX fk_manual_evaluation_collaborator (`collaborator_id` ASC) ,
  INDEX fk_manual_evalution_assesment_scale (`assessment_scale_id` ASC) ,
  INDEX fk_manual_evaluation_document (`document_id` ASC) ,
  INDEX fk_manual_evaluation_query (`query_id` ASC) ,
  CONSTRAINT `fk_manual_evaluation_collaborator`
    FOREIGN KEY (`collaborator_id` )
    REFERENCES `newfoxset`.`collaborator` (`id` )
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `fk_manual_evalution_assesment_scale`
    FOREIGN KEY (`assessment_scale_id` )
    REFERENCES `newfoxset`.`assessment_scale` (`id` )
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `fk_manual_evaluation_document`
    FOREIGN KEY (`document_id` )
    REFERENCES `newfoxset`.`document` (`id` )
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `fk_manual_evaluation_query`
    FOREIGN KEY (`query_id` )
    REFERENCES `newfoxset`.`query` (`id` )
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `newfoxset`.`document_quality_dimension`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `newfoxset`.`document_quality_dimension` ;

CREATE  TABLE IF NOT EXISTS `newfoxset`.`document_quality_dimension` (
  `document_id` INT NOT NULL ,
  `quality_dimension_id` INT NOT NULL ,
  `score` DECIMAL(6,5) NOT NULL ,
  PRIMARY KEY (`document_id`, `quality_dimension_id`) ,
  INDEX fk_document_quality_dimension_document (`document_id` ASC) ,
  INDEX fk_document_quality_dimension_quality_dimension (`quality_dimension_id` ASC) ,
  CONSTRAINT `fk_document_quality_dimension_document`
    FOREIGN KEY (`document_id` )
    REFERENCES `newfoxset`.`document` (`id` )
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `fk_document_quality_dimension_quality_dimension`
    FOREIGN KEY (`quality_dimension_id` )
    REFERENCES `newfoxset`.`quality_dimension` (`id` )
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `newfoxset`.`subset`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `newfoxset`.`subset` ;

CREATE  TABLE IF NOT EXISTS `newfoxset`.`subset` (
  `id` INT NOT NULL AUTO_INCREMENT ,
  `dataset_id` INT NOT NULL ,
  `creation_datetime` DATETIME NOT NULL ,
  `min_assesment_scale_id` INT NOT NULL ,
  `min_score` DECIMAL(6,5) NOT NULL DEFAULT 0 ,
  PRIMARY KEY (`id`) ,
  INDEX fk_subset_dataset (`dataset_id` ASC) ,
  INDEX fk_subset_assessment_scale (`min_assesment_scale_id` ASC) ,
  CONSTRAINT `fk_subset_dataset`
    FOREIGN KEY (`dataset_id` )
    REFERENCES `newfoxset`.`dataset` (`id` )
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `fk_subset_assessment_scale`
    FOREIGN KEY (`min_assesment_scale_id` )
    REFERENCES `newfoxset`.`assessment_scale` (`id` )
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `newfoxset`.`subset_query`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `newfoxset`.`subset_query` ;

CREATE  TABLE IF NOT EXISTS `newfoxset`.`subset_query` (
  `subset_id` INT NOT NULL ,
  `query_id` INT NOT NULL ,
  PRIMARY KEY (`subset_id`, `query_id`) ,
  INDEX fk_subset_query_subset (`subset_id` ASC) ,
  INDEX fk_subset_query_query (`query_id` ASC) ,
  CONSTRAINT `fk_subset_query_subset`
    FOREIGN KEY (`subset_id` )
    REFERENCES `newfoxset`.`subset` (`id` )
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `fk_subset_query_query`
    FOREIGN KEY (`query_id` )
    REFERENCES `newfoxset`.`query` (`id` )
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `newfoxset`.`subset_quality_dimension`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `newfoxset`.`subset_quality_dimension` ;

CREATE  TABLE IF NOT EXISTS `newfoxset`.`subset_quality_dimension` (
  `subset_id` INT NOT NULL ,
  `quality_dimension_id` INT NOT NULL ,
  `min_score` DECIMAL(6,5) NOT NULL DEFAULT 0 ,
  PRIMARY KEY (`subset_id`) ,
  INDEX fk_subset_quality_dimension_subset (`subset_id` ASC) ,
  INDEX fk_subset_quality_dimension_quality_dimension (`quality_dimension_id` ASC) ,
  CONSTRAINT `fk_subset_quality_dimension_subset`
    FOREIGN KEY (`subset_id` )
    REFERENCES `newfoxset`.`subset` (`id` )
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `fk_subset_quality_dimension_quality_dimension`
    FOREIGN KEY (`quality_dimension_id` )
    REFERENCES `newfoxset`.`quality_dimension` (`id` )
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `newfoxset`.`document_data`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `newfoxset`.`document_data` ;

CREATE  TABLE IF NOT EXISTS `newfoxset`.`document_data` (
  `id` INT NOT NULL AUTO_INCREMENT ,
  `document_id` INT NOT NULL ,
  `url` VARCHAR(255) NOT NULL ,
  `content_type` VARCHAR(255) NOT NULL ,
  `size` INT NOT NULL DEFAULT 0 ,
  `data` BLOB NOT NULL ,
  `original_data` BLOB NOT NULL ,
  PRIMARY KEY (`id`) ,
  INDEX fk_document_data_document (`document_id` ASC) ,
  CONSTRAINT `fk_document_data_document`
    FOREIGN KEY (`document_id` )
    REFERENCES `newfoxset`.`document` (`id` )
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `newfoxset`.`dataset_seed_documents`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `newfoxset`.`dataset_seed_documents` ;

CREATE  TABLE IF NOT EXISTS `newfoxset`.`dataset_seed_documents` (
  `id` INT NOT NULL AUTO_INCREMENT ,
  `dataset_id` INT NULL ,
  `url` VARCHAR(255) NULL ,
  `domain` VARCHAR(255) NULL ,
  PRIMARY KEY (`id`) ,
  INDEX fk_dataset_seed_documents_dataset (`dataset_id` ASC) ,
  CONSTRAINT `fk_dataset_seed_documents_dataset`
    FOREIGN KEY (`dataset_id` )
    REFERENCES `newfoxset`.`dataset` (`id` )
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;



SET SQL_MODE=@OLD_SQL_MODE;
SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS;
SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS;
