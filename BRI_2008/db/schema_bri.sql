DROP TABLE IF EXISTS `bri`.`artigos`;
CREATE TABLE  `bri`.`artigos` (
  `id` bigint(20) NOT NULL auto_increment,
  `abstracT` varchar(7000),
  `corpo` varchar(7000),
  `numero` int(11) NOT NULL,
  `title` varchar(7000),
  FULLTEXT (`title`),
  FULLTEXT (`abstracT`),
  FULLTEXT (`corpo`),
  PRIMARY KEY  (`id`),
  UNIQUE KEY `numero` (`numero`)
) ENGINE=MyISAM DEFAULT CHARSET=latin1;


DROP TABLE IF EXISTS `bri`.`itens`;
CREATE TABLE  `bri`.`itens` (
  `id` bigint(20) NOT NULL auto_increment,
  `artigo_id` bigint(20) NOT NULL,
  `query_id` bigint(20) NOT NULL,
  `score_id` bigint(20) NOT NULL,
  PRIMARY KEY  (`id`),
  UNIQUE(`artigo_id`,`query_id`),
  KEY `FK43AF7BF2AF1116F` (`score_id`),
  KEY `FK43AF7BFB7F814A5` (`artigo_id`),
  KEY `FK43AF7BF2435DAF` (`query_id`),
  CONSTRAINT `FK43AF7BF2435DAF` FOREIGN KEY (`query_id`) REFERENCES `queries` (`id`),
  CONSTRAINT `FK43AF7BF2AF1116F` FOREIGN KEY (`score_id`) REFERENCES `scores` (`id`),
  CONSTRAINT `FK43AF7BFB7F814A5` FOREIGN KEY (`artigo_id`) REFERENCES `artigos` (`id`)
) ENGINE=MyISAM DEFAULT CHARSET=latin1;

DROP TABLE IF EXISTS `bri`.`queries`;
CREATE TABLE  `bri`.`queries` (
  `id` bigint(20) NOT NULL auto_increment,
  `numero` int(11) default NULL,
  `text` varchar(255) NOT NULL,
  PRIMARY KEY  (`id`),
  UNIQUE KEY `numero` (`numero`)
) ENGINE=MyISAM DEFAULT CHARSET=latin1;

DROP TABLE IF EXISTS `bri`.`scores`;
CREATE TABLE  `bri`.`scores` (
  `id` bigint(20) NOT NULL auto_increment,
  `notas` varchar(4) default NULL,
  PRIMARY KEY  (`id`),
  UNIQUE KEY `notas` (`notas`)
) ENGINE=MyISAM DEFAULT CHARSET=latin1;

DROP TABLE IF EXISTS `bri`.`words_artigos`;
CREATE TABLE  `bri`.`words_artigos` (
  `id` bigint(20) NOT NULL auto_increment,
  `qtdOcorrencias` bigint(20) default NULL,
  PRIMARY KEY  (`id`)
) ENGINE=MyISAM DEFAULT CHARSET=latin1;

DROP TABLE IF EXISTS `bri`.`words_artigosabstract`;
CREATE TABLE  `bri`.`words_artigosabstract` (
  `word` varchar(255) NOT NULL,
  `id` bigint(20) NOT NULL,
  `artigo_id` bigint(20) NOT NULL,
  PRIMARY KEY  (`id`),
  UNIQUE(`word`,`artigo_id`),
  KEY `FK81E701B11DB03696` (`id`),
  KEY `FK81E701B1B7F814A5` (`artigo_id`),
  CONSTRAINT `FK81E701B1B7F814A5` FOREIGN KEY (`artigo_id`) REFERENCES `artigos` (`id`),
  CONSTRAINT `FK81E701B11DB03696` FOREIGN KEY (`id`) REFERENCES `words_artigos` (`id`)
) ENGINE=MyISAM DEFAULT CHARSET=latin1;

DROP TABLE IF EXISTS `bri`.`words_artigostitle`;
CREATE TABLE  `bri`.`words_artigostitle` (
  `word` varchar(255) NOT NULL,
  `id` bigint(20) NOT NULL,
  `artigo_id` bigint(20) NOT NULL,
  PRIMARY KEY  (`id`),
  UNIQUE(`word`,`artigo_id`),
  KEY `FK94ED07091DB03696` (`id`),
  KEY `FK94ED0709B7F814A5` (`artigo_id`),
  CONSTRAINT `FK94ED0709B7F814A5` FOREIGN KEY (`artigo_id`) REFERENCES `artigos` (`id`),
  CONSTRAINT `FK94ED07091DB03696` FOREIGN KEY (`id`) REFERENCES `words_artigos` (`id`)
) ENGINE=MyISAM DEFAULT CHARSET=latin1;

DROP TABLE IF EXISTS `bri`.`words_artigosall`;
CREATE TABLE  `bri`.`words_artigosall` (
  `word` varchar(255) NOT NULL,
  `id` bigint(20) NOT NULL,
  `artigo_id` bigint(20) NOT NULL,
  PRIMARY KEY  (`id`),
  UNIQUE(`word`,`artigo_id`),
  KEY `FK9BFDD692FF483D7B` (`id`),
  KEY `FK9BFDD692B7F814A5` (`artigo_id`),
  CONSTRAINT `FK9BFDD692B7F814A5` FOREIGN KEY (`artigo_id`) REFERENCES `artigos` (`id`),
  CONSTRAINT `FK9BFDD692FF483D7B` FOREIGN KEY (`id`) REFERENCES `words_artigos` (`id`)
) ENGINE=MyISAM DEFAULT CHARSET=latin1;