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
$IdDataSet = $user->getDatasetID();
if (!$IdDataSet) {
	$IdDataSet = $_GET["id"];
}

//Pega os dados do DataSet
$sql = sprintf("SELECT d.*, i.name AS language
				FROM dataset d, language i
				WHERE d.id = %s AND d.language_id = i.id",
				$IdDataSet);
$db->query($sql);
$tmp = $db->fetch_assoc();

$InfoDS = $tmp;

//Pega os coordenadores do DataSet
$sql = sprintf("SELECT u.name
				FROM collaborator u, dataset_collaborator d
				WHERE d.dataset_id = %s AND d.collaborator_id = u.id
				  AND d.role = 'C'
				ORDER BY name",
				$IdDataSet);
$db->query($sql);

$aux = '';
while($tmp = $db->fetch_assoc()){
	$aux .= $tmp["name"] . '<br>';
}

$InfoDS["user"] = $aux;

$smarty->assign("ds",$InfoDS);
$smarty->assign("titulomodulo","DataSet");
$smarty->assign("display_conteudo", "info_dataset");
finaliza();


function finaliza(){
	global $db, $smarty;
	$db->fechaConexao();
	$smarty->display("esqueleto_plugin.tpl");
	die();
}
?>