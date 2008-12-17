<?php
$requerlogin = tue;
define('DIR_SITE_ROOT', '../../');
require_once "../inicio.php";

$smarty->assign("titulomodulo","Relatorios");
$smarty->assign("submenu_titulo", "Relatorios");

$submenu["nome"] = "Relatorio 1";
$submenu["pagina"] = "?rel1";
$submenu["icone"] = "/images/ico_usuarios.gif";
$submenu["ordem"] = "1";
$submenus[] = $submenu;

$submenu["nome"] = "Relatorio 2";
$submenu["pagina"] = "?rel1";
$submenu["icone"] = "/images/ico_usuarios.gif";
$submenu["ordem"] = "2";
$submenus[] = $submenu;

$submenu["nome"] = "Relatorio 3";
$submenu["pagina"] = "?rel1";
$submenu["icone"] = "/images/ico_usuarios.gif";
$submenu["ordem"] = "3";
$submenus[] = $submenu;
unset ($submenu);
$smarty->assign("submenu",$submenus);


$smarty->assign("display_conteudo", "modulo_index");

finaliza_request();

?>