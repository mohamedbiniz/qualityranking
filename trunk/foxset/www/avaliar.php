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
if(!$user->LOGED){
	die("usuario nao logado");
}

$datasetID = $user->getDatasetID();
$sql = sprintf('SELECT manual_evaluation_strategy FROM dataset WHERE id = %s', $datasetID);
$db->query($sql);
$manualEvaluationStrategy = $db->result(0, 0);

switch (true){

	case (isset($_GET["tituloframe"]) && $_GET["tituloframe"]):
		if ($manualEvaluationStrategy == 'Q') {
			if (isset($_GET["idurl"]) && $_GET["idurl"]) {
				$sql = sprintf("SELECT url FROM document WHERE id = %s", addslashes($_GET["idurl"]));
				$db->query($sql);
				$url = $db->fetch_assoc();
				if (!$url) die("Error");
				$smarty->assign("url",$url);
				//$smarty->assign("idpergunta",$_GET["idpergunta"]);
				$db->fechaConexao();
				$smarty->display("avaliar_topoframe.tpl");
				die();
			} else {
				$smarty->assign("url", array('url' => 'none selected.'));
				$smarty->display("avaliar_topoframe.tpl");
				die();
			}
		} else if ($manualEvaluationStrategy == 'D') {
			if (isset($_GET["idpergunta"]) && $_GET["idpergunta"]) {
				$sql = sprintf("SELECT query, description FROM query WHERE id = %s", addslashes($_GET["idpergunta"]));
				$db->query($sql);
				$query = $db->fetch_assoc();
				if (!$query) die("Error");
				$smarty->assign("query",$query);
				//$smarty->assign("idpergunta",$_GET["idpergunta"]);
				$db->fechaConexao();
				$smarty->display("avaliar_topoframe_documentos.tpl");
				die();
			} else {
				$smarty->assign("query", array('query' => 'No query selected.', 'description' => 'Select one from the list on the sidebar.'));
				$smarty->display("avaliar_topoframe_documentos.tpl");
				die();
			}
		}
		break;

	case (isset($_GET["pa"]) && $_GET["pa"]):
		if ($manualEvaluationStrategy == 'Q') {
			$smarty->assign('mensagem', 'Please, select a URL from the list on the sidebar.');
		} else if ($manualEvaluationStrategy == 'D') {
			$smarty->assign('mensagem', 'Please, select a query from the list on the sidebar.');
		}
		$smarty->display("avaliar_pa.tpl");
		die();

		break;

	case ($datasetID && isset($_GET["idpergunta"]) && $_GET["idpergunta"] && isset($_GET["lista"]) && $_GET["lista"]):
		//seleciona a url
		$sql = sprintf("SELECT document_id FROM answer
						WHERE query_id = %s ",
						$_GET["idpergunta"]);
		$db->query($sql);

		$aux = '';
		while($tmp = $db->fetch_assoc()){
			$aux .= $tmp["document_id"] . ', ';
		}
		$aux = substr($aux, 0, -2);
		
		if ($aux)
			$aux = "AND u.id not in ($aux)";

		$sql = sprintf("SELECT distinct u.id, u.url
						FROM document u
						LEFT JOIN answer a ON u.id = a.document_id
						WHERE u.dataset_id = %s and u.active = 1 %s",
						 $datasetID, $aux);
		$db->query($sql);
		$urls = array();
		while ($tmp = $db->fetch_assoc()) $urls[] = $tmp;

		$sql = sprintf("SELECT * from query where id = %s",addslashes($_GET["idpergunta"]));
		$db->query($sql);
		$tmp = $db->fetch_assoc();

		$smarty->assign("Nome",$tmp["query"]);
		$smarty->assign("Descricao",$tmp["description"]);
		$smarty->assign("idpergunta",$_GET["idpergunta"]);
		$smarty->assign("mensagem","Please, select a document to evaluate.");

		$smarty->assign("urls", $urls);
		$smarty->assign("titulomodulo","Evaluate");

		$smarty->display("avaliar_listaurls.tpl");

		die();

		break;

	case ($datasetID && isset($_GET["idurl"]) && $_GET["idurl"] && isset($_GET["lista"]) && $_GET["lista"]):
		//seleciona a url
		$sql = sprintf("SELECT query_id FROM answer
						WHERE document_id = %s ",
						$_GET["idurl"]);
		$db->query($sql);

		$aux = '';
		while($tmp = $db->fetch_assoc()){
			$aux .= $tmp["query_id"] . ', ';
		}
		$aux = substr($aux, 0, -2);
		
		if ($aux) $aux = "AND u.id not in ($aux)";

		$sql = sprintf("SELECT distinct u.id, u.query
						FROM query u
						LEFT JOIN answer a ON u.id = a.query_id
						WHERE u.dataset_id = %s and u.active = 1 %s",
						 $datasetID, $aux);
		$db->query($sql);
		$queries = array();
		while ($tmp = $db->fetch_assoc()) $queries[] = $tmp;

		$sql = sprintf("SELECT * from document where id = %s",addslashes($_GET["idurl"]));
		$db->query($sql);
		$tmp = $db->fetch_assoc();

		$smarty->assign("url",$tmp["url"]);
		$smarty->assign("idurl",$_GET["idurl"]);
		$smarty->assign("mensagem","Please, select a query to evaluate this document on.");

		$smarty->assign("queries", $queries);
		$smarty->assign("titulomodulo","Evaluate");

		$smarty->display("avaliar_listaqueries.tpl");

		die();

		break;
		
		
	case ($datasetID && ((isset($_GET["idpergunta"]) && $_GET["idpergunta"]) || (isset($_GET["idurl"]) && $_GET["idurl"]))):
		if ($manualEvaluationStrategy == 'Q') {
			$smarty->assign("idpergunta",$_GET["idpergunta"]);
			if (isset($_GET["idurl"])) $smarty->assign("idurl",$_GET["idurl"]);
			$db->fechaConexao();
			$smarty->display("avaliar_frameset.tpl");
			die();

			break;
		} else if ($manualEvaluationStrategy == 'D') {
			$smarty->assign("idurl",$_GET["idurl"]);
			if (isset($_GET["idpergunta"])) $smarty->assign("idpergunta",$_GET["idpergunta"]);
			$db->fechaConexao();
			$smarty->display("avaliar_frameset_documentos.tpl");
			die();

			break;
		}
		
	case ($datasetID):
		//seleciona a pergunta
		if ($manualEvaluationStrategy == 'Q') {
			$sql = sprintf("SELECT d.context AS DataSet, d.description AS Desc_DataSet,
						p.id AS IdPergunta, p.query AS Pergunta, p.description AS Desc_Pergunta
					FROM dataset d, query p
					WHERE d.id = %s
					  AND d.id = p.dataset_id
					  AND p.active = 1", $datasetID);
			$db->query($sql);
			$perguntas = array();
			while ($tmp = $db->fetch_assoc()) $perguntas[] = $tmp;

			$smarty->assign("perguntas",$perguntas);
			$smarty->assign("titulomodulo","Evaluate");
			$smarty->assign("mensagem","Please, select a query to evaluate documents on.");
			$smarty->assign("display_conteudo", "avaliar_listaperguntas");
		} else if ($manualEvaluationStrategy == 'D') {
			$sql = sprintf("SELECT ds.context AS dataset_context, ds.description AS dataset_description, doc.id AS document_id, doc.url AS document_url
							FROM document AS doc
							INNER JOIN dataset AS ds ON (ds.id = doc.dataset_id)
							INNER JOIN query AS q ON (q.dataset_id = ds.id)
							WHERE doc.dataset_id = %s AND q.active = 1
							GROUP BY ds.context, ds.description, doc.id, doc.url
							ORDER BY doc.url", $datasetID);
			$db->query($sql);
			$docs = array();
			while ($tmp = $db->fetch_assoc()) $docs[] = $tmp;

			$smarty->assign("docs", $docs);
			$smarty->assign("titulomodulo","Evaluate");
			$smarty->assign("mensagem","Please, select a document to evaluate.");
			$smarty->assign("display_conteudo", "avaliar_listadocumentos");
		}
		finaliza();
		
		break;
}


function finaliza(){
	global $db, $smarty;
	$db->fechaConexao();
	$smarty->display("esqueleto_plugin.tpl");
	die();
}
?>