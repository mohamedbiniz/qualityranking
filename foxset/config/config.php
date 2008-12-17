<?php
// Vari?veis globais:
define('DIR_SMARTY', DIR_SITE_ROOT.'/smarty/');
define('DIR_HTDOCS', DIR_SITE_ROOT.'/www/');
define('DIR_INCLUDES', DIR_SITE_ROOT.'/include/');
define('DIR_SMARTY_LIBS', DIR_INCLUDES . '/smarty/');
define('DIR_INITS', DIR_INCLUDES . '/init/');
ini_set('include_path', DIR_INCLUDES .';'. ini_get('include_path'));
global $servers;


//ve se esta na hostnet
define("HOSTNET",(@file_exists( DIR_SITE_ROOT . '/hostnet'))?true:false);



//hostnet
$server["type"] = "mysql";
$server["addr"] = "127.0.0.1";
$server["user"] = "root";
$server["pass"] = "123";
$servers[] = $server;

//hostnet externo
$server["type"] = "mysql";
$server["addr"] = "127.0.0.1";
$server["user"] = "root";
$server["pass"] = "123";
$servers[] = $server;

//localhost
$server["type"] = "mysql";
$server["addr"] = "127.0.0.1";
$server["user"] = "root";
$server["pass"] = "123";
$servers[] = $server;

global $servers;
unset($server);

//database_info
define("DB_MAIN", "newfoxset");

define("NUM_CONEXAO",(HOSTNET)?0:2);

//include modules and smarty
require_once DIR_INITS.'mysql_init.php';
require_once DIR_INITS.'smarty_init.php';

//inclui classes do modulo adm
require_once DIR_INCLUDES . 'logon.php';

//liga o output buffering
ini_set('output_buffering', '4096');

// URLs essenciais a futuros links.
$URL_FULL = 'http://'. $_SERVER['HTTP_HOST'] . $_SERVER['PHP_SELF'] . '?' . http_build_query(array_merge($_GET, $_POST));
$URL_SELF = 'http://'. $_SERVER['HTTP_HOST'] . $_SERVER['PHP_SELF'] . '?';
$URL = 'http://'. $_SERVER['HTTP_HOST'] .'/';

define ('URL',$URL);
define ('URL_SELF',$URL_SELF);
define ('URL_FULL',$URL_FULL);


// Iniciando sessуo de usuсrio:
session_start();

// Definindo paginaчуo:
$GLOBALS['paginacao'] = array(
	'itens_por_pagina'=>20,
	'paginas_a_apresentar'=>20,
	'pagina_atual'=>1,
	'inicio'=>0,
	'fim_relativo'=>0,
	'fim_absoluto'=>0
);

//logon
if(! isset($_SESSION['user'])){
	$_SESSION['user'] = new logon();
}

Global $user;
global $db_time;
global $db_stats;
global $db;
$user = $_SESSION['user'];

?>