<?php
define('DIR_SITE_ROOT', '../');

session_start();
session_destroy();

require_once "inicio.php";
$smarty->assign("titulomodulo","Logout");

$smarty->assign("mensagem","Your session was successfully closed.");

$smarty->assign("display_conteudo", "logout");
finaliza_request();
?>