DROP TABLE IF EXISTS `foxset`.`answer`;
CREATE TABLE  `foxset`.`answer` (
  `query_id` bigint(20) NOT NULL default '0',
  `document_id` bigint(20) NOT NULL default '0',
  `assessment_scale_id` bigint(20) NOT NULL,
  `collaborator_id` bigint(20) NOT NULL,
  PRIMARY KEY  (`document_id`,`query_id`),
  KEY `FK752F2BDEEF83D58A` (`document_id`),
  KEY `FK752F2BDE41FB91AA` (`collaborator_id`),
  KEY `FK752F2BDE53752905` (`assessment_scale_id`),
  KEY `FK752F2BDEA451FD0A` (`query_id`),
  CONSTRAINT `FK752F2BDEA451FD0A` FOREIGN KEY (`query_id`) REFERENCES `query` (`id`),
  CONSTRAINT `FK752F2BDE41FB91AA` FOREIGN KEY (`collaborator_id`) REFERENCES `collaborator` (`id`),
  CONSTRAINT `FK752F2BDE53752905` FOREIGN KEY (`assessment_scale_id`) REFERENCES `assessment_scale` (`id`),
  CONSTRAINT `FK752F2BDEEF83D58A` FOREIGN KEY (`document_id`) REFERENCES `document` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1 COLLATE=latin1_general_ci;

DROP TABLE IF EXISTS `foxset`.`assessment_scale`;
CREATE TABLE  `foxset`.`assessment_scale` (
  `id` bigint(20) NOT NULL auto_increment,
  `name` varchar(50) collate latin1_general_ci NOT NULL,
  `order` int(11) NOT NULL,
  `relevant` bit(1) NOT NULL,
  `dataSet_id` bigint(20) NOT NULL,
  PRIMARY KEY  (`id`),
  KEY `FK4FAC5D8D92B0D2EA` (`dataSet_id`),
  CONSTRAINT `FK4FAC5D8D92B0D2EA` FOREIGN KEY (`dataSet_id`) REFERENCES `dataset` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1 COLLATE=latin1_general_ci;

DROP TABLE IF EXISTS `foxset`.`collaborator`;
CREATE TABLE  `foxset`.`collaborator` (
  `id` bigint(20) NOT NULL auto_increment,
  `active` tinyint(1) NOT NULL,
  `administrator` tinyint(1) NOT NULL,
  `coordinator` tinyint(1) NOT NULL,
  `email` varchar(100) collate latin1_general_ci NOT NULL,
  `name` varchar(100) collate latin1_general_ci NOT NULL,
  `password` varchar(50) collate latin1_general_ci NOT NULL,
  `username` varchar(50) collate latin1_general_ci NOT NULL,
  PRIMARY KEY  (`id`),
  UNIQUE KEY `username` (`username`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1 COLLATE=latin1_general_ci;

DROP TABLE IF EXISTS `foxset`.`context_quality_dimension_weight`;
CREATE TABLE  `foxset`.`context_quality_dimension_weight` (
  `dataSet_id` bigint(20) NOT NULL default '0',
  `quality_dimension_id` bigint(20) NOT NULL default '0',
  `quality_dimension_weight_id` bigint(20) NOT NULL default '0',
  PRIMARY KEY  (`dataSet_id`,`quality_dimension_id`,`quality_dimension_weight_id`),
  KEY `FKBE41BC146B62F98` (`quality_dimension_weight_id`),
  KEY `FKBE41BC192B0D2EA` (`dataSet_id`),
  KEY `FKBE41BC189634AAB` (`quality_dimension_id`),
  CONSTRAINT `FKBE41BC189634AAB` FOREIGN KEY (`quality_dimension_id`) REFERENCES `quality_dimension` (`id`),
  CONSTRAINT `FKBE41BC146B62F98` FOREIGN KEY (`quality_dimension_weight_id`) REFERENCES `quality_dimension_weight` (`id`),
  CONSTRAINT `FKBE41BC192B0D2EA` FOREIGN KEY (`dataSet_id`) REFERENCES `dataset` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1 COLLATE=latin1_general_ci;

DROP TABLE IF EXISTS `foxset`.`dataset`;
CREATE TABLE  `foxset`.`dataset` (
  `id` bigint(20) NOT NULL auto_increment,
  `finalization_datetime` datetime default NULL,
  `context` varchar(50) collate latin1_general_ci NOT NULL,
  `creation_datetime` datetime NOT NULL,
  `description` longtext collate latin1_general_ci NOT NULL,
  `method` char(1) collate latin1_general_ci NOT NULL,
  `min_pages` int(11) NOT NULL,
  `p_of_n` int(11) default NULL,
  `status` char(1) collate latin1_general_ci NOT NULL,
  `collaborator_id` bigint(20) NOT NULL,
  `dataSetFather_id` bigint(20) default NULL,
  `language_id` bigint(20) NOT NULL,
  PRIMARY KEY  (`id`),
  UNIQUE KEY `context` (`context`),
  KEY `FKB93DD43841FB91AA` (`collaborator_id`),
  KEY `FKB93DD438F5827A0E` (`dataSetFather_id`),
  KEY `FKB93DD438F7B0506A` (`language_id`),
  CONSTRAINT `FKB93DD43841FB91AA` FOREIGN KEY (`collaborator_id`) REFERENCES `collaborator` (`id`),
  CONSTRAINT `FKB93DD438F5827A0E` FOREIGN KEY (`dataSetFather_id`) REFERENCES `dataset` (`id`),
  CONSTRAINT `FKB93DD438F7B0506A` FOREIGN KEY (`language_id`) REFERENCES `language` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1 COLLATE=latin1_general_ci;

DROP TABLE IF EXISTS `foxset`.`dataset_collaborator`;
CREATE TABLE  `foxset`.`dataset_collaborator` (
  `role` char(1) collate latin1_general_ci NOT NULL,
  `dataSet_id` bigint(20) NOT NULL default '0',
  `collaborator_id` bigint(20) NOT NULL default '0',
  PRIMARY KEY  (`collaborator_id`,`dataSet_id`),
  KEY `FK16D374CD41FB91AA` (`collaborator_id`),
  KEY `FK16D374CD92B0D2EA` (`dataSet_id`),
  CONSTRAINT `FK16D374CD92B0D2EA` FOREIGN KEY (`dataSet_id`) REFERENCES `dataset` (`id`),
  CONSTRAINT `FK16D374CD41FB91AA` FOREIGN KEY (`collaborator_id`) REFERENCES `collaborator` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1 COLLATE=latin1_general_ci;

DROP TABLE IF EXISTS `foxset`.`dataset_seed_documents`;
CREATE TABLE  `foxset`.`dataset_seed_documents` (
  `id` bigint(20) NOT NULL auto_increment,
  `domain` varchar(255) collate latin1_general_ci default NULL,
  `url` varchar(255) collate latin1_general_ci default NULL,
  `dataSet_id` bigint(20) default NULL,
  PRIMARY KEY  (`id`),
  KEY `FK5F2B115192B0D2EA` (`dataSet_id`),
  CONSTRAINT `FK5F2B115192B0D2EA` FOREIGN KEY (`dataSet_id`) REFERENCES `dataset` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1 COLLATE=latin1_general_ci;

DROP TABLE IF EXISTS `foxset`.`document`;
CREATE TABLE  `foxset`.`document` (
  `id` bigint(20) NOT NULL auto_increment,
  `active` tinyint(1) NOT NULL,
  `score` decimal(6,5) default NULL,
  `url` varchar(255) collate latin1_general_ci NOT NULL,
  `dataSet_id` bigint(20) NOT NULL,
  PRIMARY KEY  (`id`),
  KEY `FK3737353B92B0D2EA` (`dataSet_id`),
  CONSTRAINT `FK3737353B92B0D2EA` FOREIGN KEY (`dataSet_id`) REFERENCES `dataset` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1 COLLATE=latin1_general_ci;

DROP TABLE IF EXISTS `foxset`.`document_data`;
CREATE TABLE  `foxset`.`document_data` (
  `id` bigint(20) NOT NULL auto_increment,
  `content_type` varchar(255) collate latin1_general_ci NOT NULL,
  `data` longblob NOT NULL,
  `original_data` longblob NOT NULL,
  `size` int(11) NOT NULL,
  `url` varchar(255) collate latin1_general_ci NOT NULL,
  `document_id` bigint(20) NOT NULL,
  PRIMARY KEY  (`id`),
  KEY `FK8E0AF44EEF83D58A` (`document_id`),
  CONSTRAINT `FK8E0AF44EEF83D58A` FOREIGN KEY (`document_id`) REFERENCES `document` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1 COLLATE=latin1_general_ci;

DROP TABLE IF EXISTS `foxset`.`document_document`;
CREATE TABLE  `foxset`.`document_document` (
  `child_document_id` bigint(20) NOT NULL default '0',
  `father_document_id` bigint(20) NOT NULL default '0',
  PRIMARY KEY  (`father_document_id`,`child_document_id`),
  KEY `FKB49DE3BFEF215EE7` (`child_document_id`),
  KEY `FKB49DE3BFB7028F27` (`father_document_id`),
  CONSTRAINT `FKB49DE3BFB7028F27` FOREIGN KEY (`father_document_id`) REFERENCES `document` (`id`),
  CONSTRAINT `FKB49DE3BFEF215EE7` FOREIGN KEY (`child_document_id`) REFERENCES `document` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1 COLLATE=latin1_general_ci;

DROP TABLE IF EXISTS `foxset`.`document_quality_dimension`;
CREATE TABLE  `foxset`.`document_quality_dimension` (
  `score` decimal(6,5) NOT NULL,
  `quality_dimension_id` bigint(20) NOT NULL default '0',
  `document_id` bigint(20) NOT NULL default '0',
  PRIMARY KEY  (`document_id`,`quality_dimension_id`),
  KEY `FK96FCFBE2EF83D58A` (`document_id`),
  KEY `FK96FCFBE289634AAB` (`quality_dimension_id`),
  CONSTRAINT `FK96FCFBE289634AAB` FOREIGN KEY (`quality_dimension_id`) REFERENCES `quality_dimension` (`id`),
  CONSTRAINT `FK96FCFBE2EF83D58A` FOREIGN KEY (`document_id`) REFERENCES `document` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1 COLLATE=latin1_general_ci;

DROP TABLE IF EXISTS `foxset`.`language`;
CREATE TABLE  `foxset`.`language` (
  `id` bigint(20) NOT NULL auto_increment,
  `name` varchar(50) collate latin1_general_ci NOT NULL,
  PRIMARY KEY  (`id`),
  UNIQUE KEY `name` (`name`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1 COLLATE=latin1_general_ci;

DROP TABLE IF EXISTS `foxset`.`metadata`;
CREATE TABLE  `foxset`.`metadata` (
  `id` bigint(20) NOT NULL auto_increment,
  `type` varchar(255) collate latin1_general_ci NOT NULL,
  `value` longblob NOT NULL,
  `document_id` bigint(20) NOT NULL,
  PRIMARY KEY  (`id`),
  KEY `FKE907DF4FEF83D58A` (`document_id`),
  CONSTRAINT `FKE907DF4FEF83D58A` FOREIGN KEY (`document_id`) REFERENCES `document` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1 COLLATE=latin1_general_ci;

DROP TABLE IF EXISTS `foxset`.`metadata_crawler`;
CREATE TABLE  `foxset`.`metadata_crawler` (
  `ID` bigint(20) NOT NULL,
  `IdPagina` bigint(20) default NULL,
  `NAME` varchar(255) default NULL,
  `VALUE` varchar(255) default NULL,
  `DATE_CREATE` datetime default NULL,
  `LAST_MODIFIED` datetime default NULL,
  PRIMARY KEY  (`ID`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

DROP TABLE IF EXISTS `foxset`.`outputlink_crawler`;
CREATE TABLE  `foxset`.`outputlink_crawler` (
  `ID` bigint(20) NOT NULL,
  `DATE_CREATE` datetime default NULL,
  `DOMAIN` varchar(255) default NULL,
  `IdDataSet` bigint(20) default '0',
  `IdPagina` bigint(20) default '0',
  `ID_MD5` bigint(20) default '0',
  `LAST_MODIFIED` datetime default NULL,
  `NEXT_FETCH` datetime default NULL,
  `ORDEM_DOWNLOAD_PAI` bigint(20) default '0',
  `SCORE` double default '0',
  `URL` longtext,
  `VISITED` bit(1) default NULL,
  `SEED` bit(1) default NULL,
  PRIMARY KEY  (`ID`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

DROP TABLE IF EXISTS `foxset`.`page_crawler`;
CREATE TABLE  `foxset`.`page_crawler` (
  `ID` bigint(20) NOT NULL,
  `IdDataSet` bigint(20) default NULL,
  `page_id` bigint(20) default NULL,
  `ordem_download` bigint(20) default NULL,
  `SCORE` double default NULL,
  `VERSION` bigint(20) default NULL,
  `URL` varchar(255) default NULL,
  `CONTENT` longtext,
  `PATH_PAGE` varchar(255) default NULL,
  `DATE_CREATE` datetime default NULL,
  `LAST_MODIFIED` datetime default NULL,
  PRIMARY KEY  (`ID`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

DROP TABLE IF EXISTS `foxset`.`quality_dimension`;
CREATE TABLE  `foxset`.`quality_dimension` (
  `id` bigint(20) NOT NULL auto_increment,
  `code` varchar(3) collate latin1_general_ci NOT NULL,
  `name` varchar(50) collate latin1_general_ci NOT NULL,
  PRIMARY KEY  (`id`),
  UNIQUE KEY `code` (`code`),
  UNIQUE KEY `name` (`name`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1 COLLATE=latin1_general_ci;

DROP TABLE IF EXISTS `foxset`.`quality_dimension_weight`;
CREATE TABLE  `foxset`.`quality_dimension_weight` (
  `id` bigint(20) NOT NULL auto_increment,
  `description` varchar(100) collate latin1_general_ci NOT NULL,
  `weight` int(11) NOT NULL,
  PRIMARY KEY  (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1 COLLATE=latin1_general_ci;

DROP TABLE IF EXISTS `foxset`.`query`;
CREATE TABLE  `foxset`.`query` (
  `id` bigint(20) NOT NULL auto_increment,
  `active` tinyint(1) NOT NULL,
  `creation_datetime` datetime NOT NULL,
  `description` longtext collate latin1_general_ci NOT NULL,
  `query` varchar(255) collate latin1_general_ci NOT NULL,
  `collaborator_id` bigint(20) NOT NULL,
  `dataSet_id` bigint(20) NOT NULL,
  PRIMARY KEY  (`id`),
  KEY `FK4AC28A841FB91AA` (`collaborator_id`),
  KEY `FK4AC28A892B0D2EA` (`dataSet_id`),
  CONSTRAINT `FK4AC28A841FB91AA` FOREIGN KEY (`collaborator_id`) REFERENCES `collaborator` (`id`),
  CONSTRAINT `FK4AC28A892B0D2EA` FOREIGN KEY (`dataSet_id`) REFERENCES `dataset` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1 COLLATE=latin1_general_ci;

DROP TABLE IF EXISTS `foxset`.`subset`;
CREATE TABLE  `foxset`.`subset` (
  `id` bigint(20) NOT NULL auto_increment,
  `creation_datetime` datetime NOT NULL,
  `min_score` decimal(6,5) NOT NULL,
  `dataSet_id` bigint(20) NOT NULL,
  `min_assesment_scale_id` bigint(20) NOT NULL,
  PRIMARY KEY  (`id`),
  KEY `FK9440CA2292B0D2EA` (`dataSet_id`),
  KEY `FK9440CA224B101667` (`min_assesment_scale_id`),
  CONSTRAINT `FK9440CA224B101667` FOREIGN KEY (`min_assesment_scale_id`) REFERENCES `assessment_scale` (`id`),
  CONSTRAINT `FK9440CA2292B0D2EA` FOREIGN KEY (`dataSet_id`) REFERENCES `dataset` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1 COLLATE=latin1_general_ci;

DROP TABLE IF EXISTS `foxset`.`subset_quality_dimension`;
CREATE TABLE  `foxset`.`subset_quality_dimension` (
  `min_score` decimal(6,5) NOT NULL,
  `subSet_id` bigint(20) NOT NULL default '0',
  `quality_dimension_id` bigint(20) NOT NULL,
  PRIMARY KEY  (`subSet_id`),
  KEY `FK3907C50973D0180A` (`subSet_id`),
  KEY `FK3907C50989634AAB` (`quality_dimension_id`),
  CONSTRAINT `FK3907C50989634AAB` FOREIGN KEY (`quality_dimension_id`) REFERENCES `quality_dimension` (`id`),
  CONSTRAINT `FK3907C50973D0180A` FOREIGN KEY (`subSet_id`) REFERENCES `subset` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1 COLLATE=latin1_general_ci;

DROP TABLE IF EXISTS `foxset`.`subset_query`;
CREATE TABLE  `foxset`.`subset_query` (
  `query_id` bigint(20) NOT NULL default '0',
  `subSet_id` bigint(20) NOT NULL default '0',
  PRIMARY KEY  (`query_id`,`subSet_id`),
  KEY `FKBC5DB44B73D0180A` (`subSet_id`),
  KEY `FKBC5DB44BA451FD0A` (`query_id`),
  CONSTRAINT `FKBC5DB44BA451FD0A` FOREIGN KEY (`query_id`) REFERENCES `query` (`id`),
  CONSTRAINT `FKBC5DB44B73D0180A` FOREIGN KEY (`subSet_id`) REFERENCES `subset` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1 COLLATE=latin1_general_ci;