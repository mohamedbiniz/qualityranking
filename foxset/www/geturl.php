<?php
define('DIR_SITE_ROOT', '../');
require_once DIR_SITE_ROOT . '/config/config.php';
require_once "lib_db_mysql.php";

/* @var $db db_mysql*/
$db = new db_mysql;

//incluindo bibliotecas:^M
include_once 'lib_datetime.php';
include_once 'funcoes.php';

/* @var $user logon*/
$db->abreConexao(NUM_CONEXAO);

if (isset($_GET["url"])) $url=$_GET["url"];
require_once("../include/urlgrabber.php");

if (isset($_GET["idurl"]))$idurl = $_GET["idurl"];
if (isset($_GET["furl"]))$furl = $_GET["furl"];

if (isset($idurl) && $idurl && (!isset($furl) || (!$furl))){

	$sql = sprintf("SELECT url FROM document WHERE id = %s",addslashes($idurl));
	$db->query($sql);
	$url = $db->result(0,0);
	if (!$url) die("Erro");
	header('Location: '. $url);
	die();
	$grabber = new FoxGrabber($url,$idurl);//,$_GET["furl"]);

}else{
	$grabber = new FoxGrabber(FoxGrabber::decode_url($furl),$idurl);

}
$grabber->GrabAndShow();

?>