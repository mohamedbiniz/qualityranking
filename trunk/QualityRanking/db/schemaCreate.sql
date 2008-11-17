DROP TABLE IF EXISTS `foxset`.`answer`;
CREATE TABLE  `foxset`.`answer` (
  `query_id` bigint(20) NOT NULL default '0',
  `document_id` bigint(20) NOT NULL default '0',
  `assessment_scale_id` bigint(20) NOT NULL,
  `collaborator_id` bigint(20) NOT NULL,
  PRIMARY KEY  (`document_id`,`query_id`),
  KEY `FK752F2BDE77239575` (`document_id`),
  KEY `FK752F2BDE72936415` (`collaborator_id`),
  KEY `FK752F2BDE2724BABA` (`assessment_scale_id`),
  KEY `FK752F2BDECE53CFF` (`query_id`),
  CONSTRAINT `FK752F2BDECE53CFF` FOREIGN KEY (`query_id`) REFERENCES `query` (`id`),
  CONSTRAINT `FK752F2BDE2724BABA` FOREIGN KEY (`assessment_scale_id`) REFERENCES `assessment_scale` (`id`),
  CONSTRAINT `FK752F2BDE72936415` FOREIGN KEY (`collaborator_id`) REFERENCES `collaborator` (`id`),
  CONSTRAINT `FK752F2BDE77239575` FOREIGN KEY (`document_id`) REFERENCES `document` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

DROP TABLE IF EXISTS `foxset`.`assessment_scale`;
CREATE TABLE  `foxset`.`assessment_scale` (
  `id` bigint(20) NOT NULL auto_increment,
  `name` varchar(50) NOT NULL,
  `order` int(11) NOT NULL,
  `relevant` bit(1) NOT NULL,
  `dataSet_id` bigint(20) NOT NULL,
  PRIMARY KEY  (`id`),
  KEY `FK4FAC5D8D2373E99F` (`dataSet_id`),
  CONSTRAINT `FK4FAC5D8D2373E99F` FOREIGN KEY (`dataSet_id`) REFERENCES `dataset` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

DROP TABLE IF EXISTS `foxset`.`collaborator`;
CREATE TABLE  `foxset`.`collaborator` (
  `id` bigint(20) NOT NULL auto_increment,
  `active` bit(1) NOT NULL,
  `administrator` bit(1) NOT NULL,
  `coordinator` bit(1) NOT NULL,
  `email` varchar(100) NOT NULL,
  `name` varchar(100) NOT NULL,
  `password` varchar(50) NOT NULL,
  `username` varchar(50) NOT NULL,
  PRIMARY KEY  (`id`),
  UNIQUE KEY `username` (`username`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

DROP TABLE IF EXISTS `foxset`.`context_quality_dimension_weight`;
CREATE TABLE  `foxset`.`context_quality_dimension_weight` (
  `dataSet_id` bigint(20) NOT NULL default '0',
  `quality_dimension_id` bigint(20) NOT NULL default '0',
  `quality_dimension_weight_id` bigint(20) NOT NULL default '0',
  PRIMARY KEY  (`dataSet_id`,`quality_dimension_id`,`quality_dimension_weight_id`),
  KEY `FKBE41BC1BCC63843` (`quality_dimension_weight_id`),
  KEY `FKBE41BC12373E99F` (`dataSet_id`),
  KEY `FKBE41BC12BA5EF96` (`quality_dimension_id`),
  CONSTRAINT `FKBE41BC12BA5EF96` FOREIGN KEY (`quality_dimension_id`) REFERENCES `quality_dimension` (`id`),
  CONSTRAINT `FKBE41BC12373E99F` FOREIGN KEY (`dataSet_id`) REFERENCES `dataset` (`id`),
  CONSTRAINT `FKBE41BC1BCC63843` FOREIGN KEY (`quality_dimension_weight_id`) REFERENCES `quality_dimension_weight` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

DROP TABLE IF EXISTS `foxset`.`dataset`;
CREATE TABLE  `foxset`.`dataset` (
  `id` bigint(20) NOT NULL auto_increment,
  `active` bit(1) NOT NULL,
  `busy` bit(1) NOT NULL,
  `finalization_datetime` datetime default NULL,
  `context` varchar(50) NOT NULL,
  `crawler` bit(1) NOT NULL,
  `creation_datetime` datetime NOT NULL,
  `description` longtext NOT NULL,
  `min_pages` int(11) NOT NULL,
  `min_score` decimal(6,5) default NULL,
  `status` char(1) NOT NULL,
  `collaborator_id` bigint(20) NOT NULL,
  `language_id` bigint(20) NOT NULL,
  PRIMARY KEY  (`id`),
  UNIQUE KEY `context` (`context`),
  KEY `FKB93DD43872936415` (`collaborator_id`),
  KEY `FKB93DD4387F501055` (`language_id`),
  CONSTRAINT `FKB93DD4387F501055` FOREIGN KEY (`language_id`) REFERENCES `language` (`id`),
  CONSTRAINT `FKB93DD43872936415` FOREIGN KEY (`collaborator_id`) REFERENCES `collaborator` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

DROP TABLE IF EXISTS `foxset`.`dataset_collaborator`;
CREATE TABLE  `foxset`.`dataset_collaborator` (
  `role` char(1) NOT NULL,
  `dataSet_id` bigint(20) NOT NULL default '0',
  `collaborator_id` bigint(20) NOT NULL default '0',
  PRIMARY KEY  (`collaborator_id`,`dataSet_id`),
  KEY `FK16D374CD72936415` (`collaborator_id`),
  KEY `FK16D374CD2373E99F` (`dataSet_id`),
  CONSTRAINT `FK16D374CD2373E99F` FOREIGN KEY (`dataSet_id`) REFERENCES `dataset` (`id`),
  CONSTRAINT `FK16D374CD72936415` FOREIGN KEY (`collaborator_id`) REFERENCES `collaborator` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

DROP TABLE IF EXISTS `foxset`.`dataset_seed_documents`;
CREATE TABLE  `foxset`.`dataset_seed_documents` (
  `id` bigint(20) NOT NULL auto_increment,
  `domain` varchar(255) default NULL,
  `url` varchar(255) default NULL,
  `dataSet_id` bigint(20) default NULL,
  PRIMARY KEY  (`id`),
  KEY `FK5F2B11512373E99F` (`dataSet_id`),
  CONSTRAINT `FK5F2B11512373E99F` FOREIGN KEY (`dataSet_id`) REFERENCES `dataset` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

DROP TABLE IF EXISTS `foxset`.`document`;
CREATE TABLE  `foxset`.`document` (
  `id` bigint(20) NOT NULL auto_increment,
  `active` bit(1) NOT NULL,
  `score` decimal(6,5) default NULL,
  `url` varchar(255) NOT NULL,
  `dataSet_id` bigint(20) NOT NULL,
  PRIMARY KEY  (`id`),
  KEY `FK3737353B2373E99F` (`dataSet_id`),
  CONSTRAINT `FK3737353B2373E99F` FOREIGN KEY (`dataSet_id`) REFERENCES `dataset` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

DROP TABLE IF EXISTS `foxset`.`document_data`;
CREATE TABLE  `foxset`.`document_data` (
  `id` bigint(20) NOT NULL auto_increment,
  `content_type` varchar(255) NOT NULL,
  `data` longblob NOT NULL,
  `original_data` longblob NOT NULL,
  `size` int(11) NOT NULL,
  `url` varchar(255) NOT NULL,
  `document_id` bigint(20) NOT NULL,
  PRIMARY KEY  (`id`),
  KEY `FK8E0AF44E77239575` (`document_id`),
  CONSTRAINT `FK8E0AF44E77239575` FOREIGN KEY (`document_id`) REFERENCES `document` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

DROP TABLE IF EXISTS `foxset`.`document_quality_dimension`;
CREATE TABLE  `foxset`.`document_quality_dimension` (
  `score` decimal(6,5) NOT NULL,
  `quality_dimension_id` bigint(20) NOT NULL default '0',
  `document_id` bigint(20) NOT NULL default '0',
  PRIMARY KEY  (`document_id`,`quality_dimension_id`),
  KEY `FK96FCFBE277239575` (`document_id`),
  KEY `FK96FCFBE22BA5EF96` (`quality_dimension_id`),
  CONSTRAINT `FK96FCFBE22BA5EF96` FOREIGN KEY (`quality_dimension_id`) REFERENCES `quality_dimension` (`id`),
  CONSTRAINT `FK96FCFBE277239575` FOREIGN KEY (`document_id`) REFERENCES `document` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

DROP TABLE IF EXISTS `foxset`.`language`;
CREATE TABLE  `foxset`.`language` (
  `id` bigint(20) NOT NULL auto_increment,
  `name` varchar(50) NOT NULL,
  PRIMARY KEY  (`id`),
  UNIQUE KEY `name` (`name`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

DROP TABLE IF EXISTS `foxset`.`quality_dimension`;
CREATE TABLE  `foxset`.`quality_dimension` (
  `id` bigint(20) NOT NULL auto_increment,
  `code` varchar(3) NOT NULL,
  `name` varchar(50) NOT NULL,
  PRIMARY KEY  (`id`),
  UNIQUE KEY `code` (`code`),
  UNIQUE KEY `name` (`name`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

DROP TABLE IF EXISTS `foxset`.`quality_dimension_weight`;
CREATE TABLE  `foxset`.`quality_dimension_weight` (
  `id` bigint(20) NOT NULL auto_increment,
  `description` varchar(100) NOT NULL,
  `weight` int(11) NOT NULL,
  PRIMARY KEY  (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

DROP TABLE IF EXISTS `foxset`.`query`;
CREATE TABLE  `foxset`.`query` (
  `id` bigint(20) NOT NULL auto_increment,
  `active` bit(1) NOT NULL,
  `creation_datetime` datetime NOT NULL,
  `description` longtext NOT NULL,
  `query` varchar(255) NOT NULL,
  `collaborator_id` bigint(20) NOT NULL,
  `dataSet_id` bigint(20) NOT NULL,
  PRIMARY KEY  (`id`),
  KEY `FK4AC28A872936415` (`collaborator_id`),
  KEY `FK4AC28A82373E99F` (`dataSet_id`),
  CONSTRAINT `FK4AC28A82373E99F` FOREIGN KEY (`dataSet_id`) REFERENCES `dataset` (`id`),
  CONSTRAINT `FK4AC28A872936415` FOREIGN KEY (`collaborator_id`) REFERENCES `collaborator` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

DROP TABLE IF EXISTS `foxset`.`subset`;
CREATE TABLE  `foxset`.`subset` (
  `id` bigint(20) NOT NULL auto_increment,
  `creation_datetime` datetime NOT NULL,
  `min_score` decimal(6,5) NOT NULL,
  `dataSet_id` bigint(20) NOT NULL,
  `min_assesment_scale_id` bigint(20) NOT NULL,
  PRIMARY KEY  (`id`),
  KEY `FK9440CA222373E99F` (`dataSet_id`),
  KEY `FK9440CA221EBFA81C` (`min_assesment_scale_id`),
  CONSTRAINT `FK9440CA221EBFA81C` FOREIGN KEY (`min_assesment_scale_id`) REFERENCES `assessment_scale` (`id`),
  CONSTRAINT `FK9440CA222373E99F` FOREIGN KEY (`dataSet_id`) REFERENCES `dataset` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

DROP TABLE IF EXISTS `foxset`.`subset_quality_dimension`;
CREATE TABLE  `foxset`.`subset_quality_dimension` (
  `min_score` decimal(6,5) NOT NULL,
  `subSet_id` bigint(20) NOT NULL default '0',
  `quality_dimension_id` bigint(20) NOT NULL,
  PRIMARY KEY  (`subSet_id`),
  KEY `FK3907C5091DA4D6B5` (`subSet_id`),
  KEY `FK3907C5092BA5EF96` (`quality_dimension_id`),
  CONSTRAINT `FK3907C5092BA5EF96` FOREIGN KEY (`quality_dimension_id`) REFERENCES `quality_dimension` (`id`),
  CONSTRAINT `FK3907C5091DA4D6B5` FOREIGN KEY (`subSet_id`) REFERENCES `subset` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

DROP TABLE IF EXISTS `foxset`.`subset_query`;
CREATE TABLE  `foxset`.`subset_query` (
  `query_id` bigint(20) NOT NULL default '0',
  `subSet_id` bigint(20) NOT NULL default '0',
  PRIMARY KEY  (`query_id`,`subSet_id`),
  KEY `FKBC5DB44B1DA4D6B5` (`subSet_id`),
  KEY `FKBC5DB44BCE53CFF` (`query_id`),
  CONSTRAINT `FKBC5DB44BCE53CFF` FOREIGN KEY (`query_id`) REFERENCES `query` (`id`),
  CONSTRAINT `FKBC5DB44B1DA4D6B5` FOREIGN KEY (`subSet_id`) REFERENCES `subset` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;