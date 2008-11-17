DROP TABLE IF EXISTS `sc`.`outputlink_CRAWLER`;
CREATE TABLE  `sc`.`outputlink_CRAWLER` (
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

DROP TABLE IF EXISTS `sc`.`metadata_CRAWLER`;
CREATE TABLE  `sc`.`metadata_CRAWLER` (
  `ID` bigint(20) NOT NULL,
  `IdPagina` bigint(20) default NULL,
  `NAME` varchar(255) default NULL,
  `VALUE` varchar(255) default NULL,
  `DATE_CREATE` datetime default NULL,
  `LAST_MODIFIED` datetime default NULL,
  PRIMARY KEY  (`ID`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

DROP TABLE IF EXISTS `sc`.`page_CRAWLER`;
CREATE TABLE  `sc`.`page_CRAWLER` (
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

select * from dataset;
select * from outputlink_CRAWLER where id=1 and visited=0;

truncate dataset;
truncate outputlink_CRAWLER;
truncate page_CRAWLER;
truncate metadata_CRAWLER;

select * from outputlink_CRAWLER;
select * from page;

INSERT INTO `sc`.`dataset`
  (`dataFinalizacao`,
  `nome`,
  `dataCriacao`,
  `idIdioma`,
  `maxRelevance`,
  `minAvaliacao`,
  `minPagina`,
  `minScore`,
  `statusCrawler`,
  `crawler`)
  VALUES
  ('2008-01-01',
    'economia',
    '2008-01-01',
    '1',
    100,
    20,
    50,
    20,
    'L',
    1);

INSERT INTO `sc`.`OUTPUTLINK_CRAWLER`
  (ID, IdPagina, ID_MD5, ORDEM_DOWNLOAD_PAI,
  SCORE, URL, DOMAIN, VISITED, DATE_CREATE,
  LAST_MODIFIED, NEXT_FETCH)
  VALUES
  (1, 1, 1, 1,
  1, 'http://www.economist.com', 'www.economist.com', 0, '2008-01-01',
  '2008-01-01', '2008-01-01');