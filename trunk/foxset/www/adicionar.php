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

$sql = sprintf("SELECT d.Nome AS DataSet, d.Descricao AS Desc_DataSet, 
					p.Nome AS Pergunta, p.Descricao AS Desc_Pergunta
				FROM DataSet d, Pergunta p
				WHERE d.IdDataSet = %s
				  AND d.IdDataSet = p.IdDataSet
				  AND p.Status = 'A'", $user->getDatasetID());
$db->query($sql);

$tmp = $db->fetch_assoc();
echo "<b>" . $tmp["DataSet"] . "</b><br>" . $tmp["Desc_DataSet"] . "<br><br>";

do {
	echo $tmp["Pergunta"] . "<br>" . $tmp["Desc_Pergunta"] . "<br><br>";

} while ($tmp = $db->fetch_assoc());

?>