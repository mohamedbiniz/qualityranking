-- phpMyAdmin SQL Dump
-- version 2.6.2
-- http://www.phpmyadmin.net
-- 
-- Banco de Dados: `foxset`
-- 
CREATE DATABASE `foxset`;
USE foxset;

-- --------------------------------------------------------

-- 
-- Estrutura da tabela `Avaliacao`
-- 

CREATE TABLE `Avaliacao` (
  `IdPergunta` int(11) NOT NULL default '0',
  `IdUrlDataSet` int(11) NOT NULL default '0',
  `IdUsuario` int(11) NOT NULL default '0',
  `IdEscala` int(11) NOT NULL default '0',
  PRIMARY KEY  (`IdPergunta`,`IdUrlDataSet`,`IdUsuario`)
) TYPE=MyISAM;

-- --------------------------------------------------------

-- 
-- Estrutura da tabela `DataSet`
-- 

CREATE TABLE `DataSet` (
  `IdDataSet` int(11) NOT NULL auto_increment,
  `Nome` varchar(50) NOT NULL default '',
  `Descricao` text NOT NULL,
  `Status` enum('A','I') NOT NULL default 'A',
  `MinPagina` tinyint(4) NOT NULL default '0',
  `MinAvaliacao` smallint(6) NOT NULL default '0',
  `IdIdioma` tinyint(4) NOT NULL default '0',
  `DataCriacao` datetime NOT NULL default '0000-00-00 00:00:00',
  `DataFinalizacao` datetime default NULL,
  PRIMARY KEY  (`IdDataSet`)
) TYPE=MyISAM;

-- --------------------------------------------------------

-- 
-- Estrutura da tabela `Escala`
-- 

CREATE TABLE `Escala` (
  `IdEscala` int(11) NOT NULL auto_increment,
  `Item` varchar(50) NOT NULL default '',
  `Relevante` enum('S','N') NOT NULL default 'S',
  `Ordem` tinyint(4) NOT NULL default '0',
  `IdDataSet` int(11) NOT NULL default '0',
  PRIMARY KEY  (`IdEscala`)
) TYPE=MyISAM;

-- --------------------------------------------------------

-- 
-- Estrutura da tabela `Idioma`
-- 

CREATE TABLE `Idioma` (
  `IdIdioma` tinyint(4) NOT NULL auto_increment,
  `Descricao` varchar(100) NOT NULL default '',
  PRIMARY KEY  (`IdIdioma`)
) TYPE=MyISAM;

-- --------------------------------------------------------

-- 
-- Estrutura da tabela `Pergunta`
-- 

CREATE TABLE `Pergunta` (
  `IdPergunta` int(11) NOT NULL auto_increment,
  `Nome` varchar(255) NOT NULL default '',
  `IdDataSet` int(11) NOT NULL default '0',
  `Descricao` text NOT NULL,
  `Status` set('A','I') NOT NULL default 'A',
  `IdUsuario` smallint(6) NOT NULL default '0',
  `DataCriacao` datetime NOT NULL default '0000-00-00 00:00:00',
  PRIMARY KEY  (`IdPergunta`)
) TYPE=MyISAM;

-- --------------------------------------------------------

-- 
-- Estrutura da tabela `UrlData`
-- 

CREATE TABLE `UrlData` (
  `IdUrlDataSetData` int(11) NOT NULL auto_increment,
  `IdUrlDataSet` int(11) NOT NULL default '0',
  `CaminhoUrl` varchar(255) NOT NULL default '',
  `Content-Type` varchar(255) NOT NULL default '',
  `Size` int(11) NOT NULL default '0',
  `Data` blob NOT NULL,
  `OriginalData` blob NOT NULL,
  PRIMARY KEY  (`IdUrlDataSetData`),
  KEY `IdUrl` (`IdUrlDataSet`),
  KEY `CaminhoUrl` (`CaminhoUrl`)
) TYPE=MyISAM;

-- --------------------------------------------------------

-- 
-- Estrutura da tabela `UrlDataInfo`
-- 

CREATE TABLE `UrlDataInfo` (
  `IdUrlUrl` int(11) NOT NULL default '0',
  `Sname` varchar(255) NOT NULL default '',
  `Sval` varchar(255) NOT NULL default '',
  PRIMARY KEY  (`IdUrlUrl`,`Sname`)
) TYPE=MyISAM;

-- --------------------------------------------------------

-- 
-- Estrutura da tabela `UrlDataSet`
-- 

CREATE TABLE `UrlDataSet` (
  `IdUrlDataSet` int(11) NOT NULL auto_increment,
  `idDataSet` int(11) NOT NULL default '0',
  `Url` varchar(255) NOT NULL default '',
  PRIMARY KEY  (`IdUrlDataSet`),
  KEY `idDataSet` (`idDataSet`)
) TYPE=MyISAM;

-- --------------------------------------------------------

-- 
-- Estrutura da tabela `Usuario`
-- 

CREATE TABLE `Usuario` (
  `IdUsuario` int(11) NOT NULL auto_increment,
  `Login` varchar(20) NOT NULL default '',
  `Senha` varchar(64) NOT NULL default '',
  `Nome` varchar(100) NOT NULL default '',
  `Email` varchar(100) NOT NULL default '',
  `Status` enum('A','I') NOT NULL default 'A',
  `Administrador` enum('0','1') NOT NULL default '0',
  `Coordenador` enum('0','1') NOT NULL default '0',
  PRIMARY KEY  (`IdUsuario`)
) TYPE=MyISAM;

-- --------------------------------------------------------

-- 
-- Estrutura da tabela `Usuario_DataSet`
-- 

CREATE TABLE `Usuario_DataSet` (
  `IdUsuario` int(11) NOT NULL default '0',
  `IdDataSet` int(11) NOT NULL default '0',
  `Permissao` enum('Avaliador','Utilizador','Coordenador') NOT NULL default 'Avaliador',
  PRIMARY KEY  (`IdUsuario`,`IdDataSet`,`Permissao`)
) TYPE=MyISAM;
        