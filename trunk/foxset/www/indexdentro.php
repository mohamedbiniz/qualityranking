<?php
error_reporting(E_ALL ^ E_NOTICE);
$requerlogin = true;
define('DIR_SITE_ROOT', '..');
require_once "inicio.php";
$smarty->assign("display_conteudo", "modulo_index_dentro");
finaliza_request();

?>