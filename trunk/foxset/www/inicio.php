<?php
require_once DIR_SITE_ROOT . '/config/config.php';
require_once "lib_db_mysql.php";

/* @var $db db_mysql*/
$db = new db_mysql;

//incluindo bibliotecas:
include_once 'lib_datetime.php';
include_once 'funcoes.php';

//tempo de inicio
$starttime = microtime_float();
/* @var $user logon*/
$db->abreConexao(NUM_CONEXAO);

//efetua o login caso solicitado
if (isset($_POST["efetualogin"]) && $_POST["efetualogin"]){
	$user->forceLogon($_POST["nome"],$_POST["senha"]);
	if ($user->ERROR) $smarty->assign("mensagem", $user->ERROR);
}
$smarty->assign("logado", $user->LOGED);
$smarty->assign("username", $user->USERNAME);

// Verificando se o usuário está logado:
if($user->LOGED){
	$smarty->assign("usuario_nome", $user->NOME);
	$hasRoleC = $user->hasRole('C');
	$hasRoleU = $user->hasRole('U');
	$menus = array();
	if ($user->ADMIN) {
		$menu["nome"] = "Collaborators";
		$menu["pagina"] = "/cadastros/usuarios.php";
		$menu["icone"] = "/images/ico_usuarios.gif";
		$menu["ordem"] = "1";
		$menus[] = $menu;
	}
	if ($user->COORD || $hasRoleC) {
		$menu["nome"] = "Datasets";
		$menu["pagina"] = "/cadastros/datasets.php";
		$menu["icone"] = "/images/ico_usuarios.gif";
		$menu["ordem"] = "5";
		$menus[] = $menu;
	}
	if ($user->COORD || $hasRoleC || $hasRoleU) {
		$menu["nome"] = "Subsets";
		$menu["pagina"] = "/exportar_testset.php";
		$menu["icone"] = "/images/ico_usuarios.gif";
		$menu["ordem"] = "10";
		$menus[] = $menu;
	}

	unset ($menu);
	$smarty->assign("menu",$menus);
}else{

	$menu["nome"] = "About";
	$menu["pagina"] = "/sobre.php";
	$menu["ordem"] = "25";
	$menus[] = $menu;

	$menu["nome"] = "Team";
	$menu["pagina"] = "/autores.php";
	$menu["ordem"] = "25";
	$menus[] = $menu;

	unset ($menu);
	$smarty->assign("menu",$menus);

	if (isset($requerlogin) && $requerlogin){
		if (! $user->ERROR)$smarty->assign("mensagem", "You must be logged in to access this module.");
		$smarty->assign("display_conteudo", "semacesso");
		finaliza_request();
	}


}

function permissao_negada(){
	global $smarty;
	$smarty->assign("mensagem", "Access denied.");
	$smarty->assign("display_conteudo", "semacesso");
	finaliza_request();
}


function finaliza_request(){
	// Fexando conex?o com o banco de dados:
	global $db, $smarty, $starttime, $db_time, $db_stats;

	$db->fechaConexao();
	$totaltime = microtime_float() - $starttime;
	$smarty->assign("total_time",  $totaltime );
	$smarty->assign("db_time", $db_time);
	$smarty->assign("real_time", $totaltime - $db_time );
	$smarty->assign("db_stats", $db_stats);
	$smarty->display("esqueleto.tpl");
	die();
}
?>