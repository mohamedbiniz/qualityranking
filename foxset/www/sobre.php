<?php
error_reporting(E_ALL ^ E_NOTICE);
define('DIR_SITE_ROOT', '..');
require_once "inicio.php";
$smarty->assign("titulomodulo","About FoxSet");
$smarty->assign("display_conteudo", "modulo_sobre");
finaliza_request();

?>