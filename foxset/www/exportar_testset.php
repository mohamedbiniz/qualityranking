<?php
$requerlogin = true;
define('DIR_SITE_ROOT', '..');
require_once "inicio.php";

$smarty->assign("titulomodulo","Subsets");

if (isset($_GET["acao"])) {
switch (true){

	case ($_GET["acao"] == 'exportar') :

		if (!isset($_POST["iddataset"]) || !$_POST["iddataset"]){
			$smarty->assign("mensagem", "Please, select a dataset to export.");
			break;
		}

		$iddataset = $_POST["iddataset"];
		
		$sql = sprintf("
			SELECT context from dataset WHERE id = %s
		",
		$iddataset
		);
		
		$db->query($sql);
		$nomeDataSet = $db->result(0,0);
		

		$perguntas = isset($_POST["pergunta"]) && isset($_POST["pergunta"][$iddataset]) ? $_POST["pergunta"][$iddataset] : null;

		if ( !@is_Array($perguntas)){
			$smarty->assign("mensagem", "Please, select one or more queries to export.");
			break;
		}

		$sql = sprintf("
			SELECT DISTINCT av.document_id, p.id AS query_id, es.id AS assessment_scale_id, p.query, uds.url
			FROM answer av
			INNER JOIN document uds ON uds.id = av.document_id
			INNER JOIN assessment_scale es ON av.assessment_scale_id = es.id
			INNER JOIN query p ON p.id = av.query_id
			WHERE uds.dataset_id = %s
			AND es.relevant = 1
			ORDER BY p.id
		",
		$iddataset
		);

		$db->query($sql);
		
		header("Content-type: text/xml");
		echo "<?xml version=\"1.0\" encoding=\"utf-8\" ?>\n";
		echo "<dataset id=\"".$iddataset."\" name=\"".$nomeDataSet."\" >\n";
		echo "\t<questions>\n";

		$idpergunta = -1;
		while ($p = $db->fetch() ){

			if ($idpergunta != $p["query_id"]){
				if ($idpergunta != -1){
					echo "\t\t</question>\n";
				}
				echo "\t\t<question id=\"".$p["query_id"]."\" name=\"".$p["query"]."\">\n";
			}
			$idpergunta = $p["query_id"];
			echo "\t\t\t<url location=\"".$p["url"]."\" id=\"".$p["document_id"]."\"/>\n";
			
			
		}
		if ($idpergunta != -1) {
			echo "\t\t</question>\n";
		}
		echo "\t</questions>\n";

		//--------------------------
		
		$sql = sprintf("
			SELECT DISTINCT uds.id, uds.url
			FROM document uds
			INNER JOIN answer av ON uds.id = av.document_id
			WHERE uds.dataset_id = %s
		",
		$iddataset
		);

		$db->query($sql);

		echo "\t<urls>\n";


		while ($p = $db->fetch() ){

			echo "\t\t<url location=\"".$p["url"]."\" id=\"".$p["id"]."\"/>\n";

		}
		
		echo "\t</urls>\n";
		echo "</dataset>";
		
		
		
		
		

		die();

		break;

}
}

$aux = '';
/*$datasetID = $user->getDatasetID();
if ($datasetID) {
	$aux = "AND d.id = " . $datasetID;
}*/

$sql = sprintf("SELECT p.*, d.id AS DID, d.context AS DNOME, d.status AS DSTATUS
			FROM dataset d LEFT JOIN query p ON d.id = p.dataset_id
			INNER JOIN dataset_collaborator ud ON d.id = ud.dataset_id
			WHERE ud.collaborator_id = %s AND ud.role IN ('C', 'U') %s
			GROUP BY p.id, d.id
			ORDER BY d.context",
$user->ID, $aux );
$q1 = $db->query($sql);

$datasets = array();
if ($db->rows($q1)) {
	while($tmp = $db->fetch_assoc($q1)){
		if ( $tmp["id"]) {
			$tmp2 = $tmp;

			//pega as paginas avaliadas
			$sql = sprintf("select query_id ,count(query_id) as contagem from answer
							where query_id = %s group by query_id ",
			$tmp["id"] );
			$q2 = $db->query($sql);
			$tmp3 = mysql_fetch_array($q2);
			$tmp2["pa"] = ($tmp3["contagem"])? $tmp3["contagem"]:0;

			//pega as paginas avaliadas relevantes
			$sql = sprintf("select query_id ,count(query_id) as contagem
							from answer a
							Inner join assessment_scale e on a.assessment_scale_id = e.id 
							where query_id = %s and e.relevant = 1 group by query_id ",
			$tmp["id"] );
			$q2 = $db->query($sql);
			$tmp3 = mysql_fetch_array($q2);
			$tmp2["par"] = ($tmp3["contagem"])? $tmp3["contagem"]:0;

			$datasets[$tmp['DNOME']]["perguntas"][] = $tmp2;
			$datasets[$tmp['DNOME']]["id"] = $tmp2['DID'];
			$datasets[$tmp['DNOME']]["status"] = $tmp2['DSTATUS'];
		}else{
			$datasets[$tmp['DNOME']]["id"] = $tmp['DID'];
		}
	}
} else {
	$smarty->assign("mensagem","There are no datasets under your coordination.");
}

$smarty->assign("datasets", $datasets);
$smarty->assign("display_conteudo", "modulo_testset_index");

finaliza_request();

?>