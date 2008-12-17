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
header('Content-Type: text/html; charset=utf-8');

$cmd = ($_POST["cmd"])? $_POST["cmd"]: $_GET["cmd"];

switch ($cmd){
	case "login":
		$user->logoff();
		$user->forceLogon($_POST["username"], $_POST["password"]);
		if ($user->ERROR){
			echo json_encode(array('success' => false, 'errorCode' => 1, 'message' => 'Invalid username or password.'));
		}else {
			$sql = sprintf("select id, username, name, email, administrator, coordinator from collaborator where id = %s", $user->ID);

			$db->query($sql);
			
			$collaborator = $db->fetch_assoc();
			//Pega os DataSets que o usuário tem permissão
			$sql = sprintf("SELECT d.id, d.context, d.status, d.method, dc.role
							FROM dataset d, dataset_collaborator dc
							WHERE d.id = dc.dataset_id
							  AND dc.collaborator_id = %s
							  ORDER BY d.context", $user->ID);
			$db->query($sql);

			$ds = array();
			while($tmp = $db->fetch_assoc()){
				$ds[] = $tmp;
			}
			echo json_encode(array('success' => true, 'message' => 'You have successfully logged in.', 'collaborator' => $collaborator, 'datasets' => $ds));
		}

		break;

	case "coordinate":		echo json_encode(array('success' => true, 'url' => "cadastros/datasets.php?selected=1"));
		break;

	case "add":
		include_once('urlgrabber.php');
		$iddataset = $user->getDatasetID();
		$url = isset($_POST["url"]) ? $_POST["url"] : '';

		if ( ($url == "") || !$iddataset ) {
			die(json_encode(array('success' => false, 'message' => 'Error: no URL or dataset selected.')));
		}

		if (strpos($url, '://') === false) {
			$url = 'http://' . $url;
		}
		if (!FOXGrabber::parse_url($url, $segmentos)) {
			die(json_encode(array('success' => false, 'message' => 'Error: unable to parse URL.')));
		}
		$sql = sprintf("SELECT * FROM document WHERE dataset_id = %s AND url = '%s'", $iddataset, addslashes($url));
		if ($db->query($sql)) {
			if ($db->rows()) {
				die(json_encode(array('success' => false, 'message' => 'Error: document already exists.')));
			}
		} else {
			die(json_encode(array('success' => false, 'message' => 'Error: unable to execute query.')));
		}
		$sql = sprintf("INSERT INTO document (dataset_id, url) Values (%s,'%s');", $iddataset, addslashes($url));
		if ($db->query($sql)) {
			die(json_encode(array('success' => true, 'message' => 'Page added successfully.')));
		} else {
			die(json_encode(array('success' => false, 'message' => 'Error: unable to execute statement.')));
		}
		break;

	case "evaluate":		$datasetID = $user->getDatasetID();		if (!$datasetID) {			die(json_encode(array('success' => false, 'message' => 'Error: no dataset selected.')));		}
		//Pega os DataSets que o usuário tem permissão
		$sql = sprintf("SELECT id, name
						FROM assessment_scale
						WHERE dataset_id = %s
						ORDER BY `order`", $datasetID);
		$db->query($sql);
		$scale = array();
		while($tmp = $db->fetch_assoc()){
			$scale[] = $tmp;
		}
		echo json_encode(array('success' => true, 'url' => 'avaliar.php', 'scale' => $scale));
		break;
		
	case "save_evaluate":
		if (!isset($_POST['idurl'])) {
			die(json_encode(array('success' => false, 'message' => 'Error: evaluation tab not active.')));
		}
		$idURL = $_POST["idurl"];
		
		if (!isset($_POST['idpergunta'])) {			die(json_encode(array('success' => false, 'message' => 'Error: evaluation tab not active.')));		}
		$idPergunta = $_POST["idpergunta"];

		
		$sql = sprintf("INSERT INTO answer (document_id, query_id, collaborator_id, assessment_scale_id)
						VALUES (%s, %s, %s, %s)",
						$idURL, $idPergunta, $user->ID, $_POST["IdEscala"]);
						
		if ($db->query($sql)) {
			$sql = sprintf('SELECT manual_evaluation_strategy FROM dataset WHERE id = %s', $_SESSION['datasetID']);
			$db->query($sql);
			$manualEvaluationStrategy = $db->result(0, 0);
			if ($manualEvaluationStrategy == 'Q') {
				die(json_encode(array('success' => true, 'url' => "avaliar.php?idpergunta=" . $idPergunta)));
			} else if ($manualEvaluationStrategy == 'D') {
				die(json_encode(array('success' => true, 'url' => "avaliar.php?idurl=" . $idURL)));
			}
		} else {
			die(json_encode(array('success' => false, 'message' => 'Error: unable to execute query.')));
		}
		
		break;

	case "setDataset":
		$did = $_SESSION['datasetID'] = $_POST["id"];
		echo json_encode(array('success' => true, 'url' => "infods.php?id=$did"));				break;	case "logout":		$user->logoff();		echo json_encode(array('success' => true, 'message' => "Bye! Thanks for using FoxSet!"));				break;

		
	case 'datasets':
		$sql = sprintf("SELECT d.id, d.context, d.status, d.method, dc.role
						FROM dataset d, dataset_collaborator dc
						WHERE d.id = dc.dataset_id
						  AND dc.collaborator_id = %s
						  ORDER BY d.context", $user->ID);
		$db->query($sql);
		$ds = array();
		while($tmp = $db->fetch_assoc()){
			$ds[] = $tmp;
		}
		echo json_encode(array('success' => true, 'datasets' => $ds));
		break;
		
	default:
		echo json_encode(array('success' => false, errorCode => -1, 'message' => 'Unknown command.'));
}
?>