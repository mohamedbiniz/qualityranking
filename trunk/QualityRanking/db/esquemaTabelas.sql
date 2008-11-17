DROP TABLE IF EXISTS `foxset`.`outputlink_CRAWLER`;
CREATE TABLE  `foxset`.`outputlink_CRAWLER` (
  `ID` bigint(20) NOT NULL,
  `DATE_CREATE` datetime default NULL,
  `DOMAIN` varchar(255) default NULL,
  `IdDataSet` bigint(20) default 0,
  `IdPagina` bigint(20)  default 0,
  `ID_MD5` bigint(20)  default 0,
  `LAST_MODIFIED` datetime default NULL,
  `NEXT_FETCH` datetime default NULL,
  `ORDEM_DOWNLOAD_PAI` bigint(20) default 0,
  `SCORE` double default 0,
  `URL` longtext,
  `VISITED` bit(1) ,
  `SEED` bit(1),
  PRIMARY KEY  (`ID`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

DROP TABLE IF EXISTS `foxset`.`metadata_CRAWLER`;
CREATE TABLE  `foxset`.`metadata_CRAWLER` (
  `ID` bigint(20) NOT NULL,
  `IdPagina` bigint(20) default NULL,
  `NAME` varchar(255) default NULL,
  `VALUE` varchar(255) default NULL,
  `DATE_CREATE` datetime default NULL,
  `LAST_MODIFIED` datetime default NULL,
  PRIMARY KEY  (`ID`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

DROP TABLE IF EXISTS `foxset`.`page_CRAWLER`;
CREATE TABLE  `foxset`.`page_CRAWLER` (
  `ID` bigint(20) NOT NULL,
  `IdDataSet` bigint(20) default NULL,
  `SCORE` double default NULL,
  `VERSION` bigint(20) default NULL,
  `URL` varchar(255) default NULL,
  `CONTENT` LONGTEXT default NULL,
  `PATH_PAGE` varchar(255) default NULL,
  `DATE_CREATE` datetime default NULL,
  `LAST_MODIFIED` datetime default NULL,
  PRIMARY KEY  (`ID`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;