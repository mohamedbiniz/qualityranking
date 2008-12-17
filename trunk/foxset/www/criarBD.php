<?php
define('DIR_SITE_ROOT', '../');
require_once DIR_SITE_ROOT . '/config/config.php';
require_once "lib_db_mysql.php";

/* @var $db db_mysql*/
$db = new db_mysql;

//incluindo bibliotecas:
include_once 'lib_datetime.php';
include_once 'funcoes.php';
/* @var $user logon*/
$db->abreConexao(NUM_CONEXAO);
if(!$user->LOGED){
	die("usuario nao logado");
}
$sql = "UPDATE idioma SET Descricao = 'Português (Brasil)' WHERE IdIdioma = 1";
$db->query($sql);
$sql = "UPDATE idioma SET Descricao = 'Inglês (EUA)' WHERE IdIdioma = 2";
$db->query($sql);
?>