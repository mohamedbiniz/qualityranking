<?php
error_reporting(E_ALL ^ E_NOTICE);
define('DIR_SITE_ROOT', '..');
require_once "inicio.php";
$smarty->assign("titulomodulo","FoxSet Team");
$smarty->assign("display_conteudo", "modulo_autores");
finaliza_request();
?>