<?php
$requerlogin = true;
define('DIR_SITE_ROOT', '../../');
require_once "../inicio.php";
require DIR_INCLUDES . "xoopsformloader.php";

$smarty->assign("titulomodulo","Datasets");
$smarty->assign("submenu_titulo", "Datasets");
$submenus = array();
if ($user->COORD) {
	$submenu["nome"] = "New dataset";
	$submenu["pagina"] = "?acao=criar";
	$submenu["icone"] = "/images/ico_usuarios.gif";
	$submenu["ordem"] = "1";
	$submenus[] = $submenu;
}

unset ($submenu);
$smarty->assign("submenu",$submenus);

if (isset($_GET['acao'])) {
switch($_GET["acao"]){

	//---------------------------------------------
	//Assunto
	//

	case "salvar":
		$ass = $_POST["dataset"];
		$qds = $_POST['qds'];
		
		if ($ass['modoEscala'] == '3'){

			$i = 0;
			foreach ($ass['escalap'] as $escalaatual){
				if (trim($escalaatual) != "")	$i++;
			}

			if (($i < 2)){
				$smarty->assign("mensagem", "Error adding dataset. At least, 2 items in the scale must be filled.");

				break;
			}
		}

		//print_r($ass);
		
		$sql = sprintf("insert into dataset (context, description, min_pages, language_id, collaborator_id, creation_datetime, method, p_of_n, status, manual_evaluation_strategy)
						values('%s','%s','%s','%s', %s, now(), '%s', %s, '%s', '%s')",
		$ass['Nome'],
		addslashes($ass['Descricao']),
		$ass['MinPagina'],
		$ass['IdIdioma'],
		$user->ID,
		$ass['method'],
		$ass['method'] == 'P' ? $ass['pofn'] : '1',
		$ass['method'] != 'M' ? 'L' : 'M',
		$ass['manualEvaluationStrategy']);

		if ($db->query($sql)){

			$iddataset = $db->insert_id();
			if ($ass['method'] == 'C' || $ass['method'] == 'Q') {
				// Adding quality dimensions
				$sql = array();
				foreach ($qds as $qd_id => $qd) {
					if (!isset($qd['check']) || $qd['check'] != '1') continue;
					$sql[] = sprintf('INSERT INTO context_quality_dimension_weight (dataset_id, quality_dimension_id, quality_dimension_weight_id) VALUES (%s, %s, %s)', $iddataset, $qd_id, $qd['weight']);
				}
				foreach ($sql as $sqlatual) {
					if (!$db->query($sqlatual)){
						$smarty->assign("mensagem", "Error adding dataset's quality dimensions.");
						echo $sql . $db->error() ;
						$erro = true;
					}
				}
			}
			if ($ass['method'] == 'C') {
				// Adding seed pages
				$sql = array();
				$seedURLs = explode(' ', $ass['seedURLs']);
				foreach ($seedURLs as $seedURL) {
					if (stripos($seedURL, '://') === false) {
						$seedURL = 'http://' . $seedURL;
					}
					$partsURL = parse_url($seedURL);
					$sql[] = sprintf("INSERT INTO dataset_seed_documents (dataset_id, url, domain) VALUES (%s, '%s', '%s')", $iddataset, $seedURL, $partsURL['host']);
				}
				foreach ($sql as $sqlatual) {
					if (!$db->query($sqlatual)){
						$smarty->assign("mensagem", "Error adding dataset's seed URLs.");
						echo $sql . $db->error() ;
						$erro = true;
					}
				}
			}
			$sql = "";
			switch ($ass['modoEscala']){

				case 1:
					$sql[] = sprintf("insert into assessment_scale (name, relevant, `order`, dataset_id) values('No relevance', %s,0,$iddataset);",(isset($_POST["escala1_1"]) && $_POST["escala1_1"] == "S")? "1":"0" );
					$sql[] = sprintf("insert into assessment_scale (name, relevant, `order`, dataset_id) values('Low relevance', %s,1,$iddataset);",(isset($_POST["escala1_2"]) && $_POST["escala1_2"] == "S")? "1":"0" );
					$sql[] = sprintf("insert into assessment_scale (name, relevant, `order`, dataset_id) values('Medium relevance', %s,2,$iddataset);",(isset($_POST["escala1_3"]) && $_POST["escala1_3"] == "S")? "1":"0" );
					$sql[] = sprintf("insert into assessment_scale (name, relevant, `order`, dataset_id) values('High relevance', %s,3,$iddataset);",(isset($_POST["escala1_4"]) && $_POST["escala1_4"] == "S")? "1":"0" );

					break;
				case 2:
					$sql[] = sprintf("insert into assessment_scale (name, relevant, `order`, dataset_id) values('1',%s, 0,$iddataset);",(isset($_POST["escala2_1"]) && $_POST["escala2_1"] == "S")? "1":"0" );
					$sql[] = sprintf("insert into assessment_scale (name, relevant, `order`, dataset_id) values('2',%s, 1,$iddataset);",(isset($_POST["escala2_2"]) && $_POST["escala2_2"] == "S")? "1":"0" );
					$sql[] = sprintf("insert into assessment_scale (name, relevant, `order`, dataset_id) values('3',%s, 2,$iddataset);",(isset($_POST["escala2_3"]) && $_POST["escala2_3"] == "S")? "1":"0" );
					$sql[] = sprintf("insert into assessment_scale (name, relevant, `order`, dataset_id) values('4',%s, 3,$iddataset);",(isset($_POST["escala2_4"]) && $_POST["escala2_4"] == "S")? "1":"0" );
					$sql[] = sprintf("insert into assessment_scale (name, relevant, `order`, dataset_id) values('5',%s, 4,$iddataset);",(isset($_POST["escala2_5"]) && $_POST["escala2_5"] == "S")? "1":"0" );

					break;
				case 3:
					$i=0;

					foreach ($ass['escalap'] as $escalaatual) {
						if (trim($escalaatual) != ""){
							$sql[] = sprintf("insert into assessment_scale (name, relevant, `order`, dataset_id) values('$escalaatual', %s,$i,$iddataset);",(isset($_POST["escala3_$i"]) && $_POST["escala3_$i"] == "S")? "1":"0" );
							$i++;
						}
					}
			}
			if (isset($erro) && $erro == true)break;

			foreach ($sql as $sqlatual) {
				if (!$db->query($sqlatual)){
					$smarty->assign("mensagem", "Error adding dataset's scale.");
					echo $sql .$db->error() ;
					$erro = true;
				}
			}

			if (isset($erro) && $erro == true)break;
			$smarty->assign("mensagem", "Dataset added successfully.");
			$sql = sprintf("insert into dataset_collaborator (dataset_id, collaborator_id, role) values(%s,%s,'%s')",
			$iddataset, $user->ID, 'C');

			if (!$db->query($sql)){
				$smarty->assign("mensagem", "Error including dataset's roles.");
				echo $sql .$db->error() ;
				break;
			}


		}else{
			$smarty->assign("mensagem", "Error adding dataset.");
			echo $sql .$db->error() ;;
		}
		break;

	case "criar":
		$sql = sprintf("SELECT id, name FROM quality_dimension");
		$db->query($sql);
		$qds = array();
		while ($tmp = $db->fetch_assoc()) {
			$qds[] = $tmp;
		}
		
		$sql = sprintf("SELECT id, weight, description FROM quality_dimension_weight ORDER BY weight");
		$db->query($sql);
		$qdws = array();
		while ($tmp = $db->fetch_assoc()) {
			$qdws[] = $tmp;
		}
	
		$formulario = new XoopsThemeForm("New DataSet","inclusao_assunto","?acao=salvar");
		$formulario->addElement(new XoopsFormText("Name:","dataset[Nome]",50,50));
		$formulario->addElement(new XoopsFormTextArea("Description:","dataset[Descricao]","",5,80));
		$idioma = new XoopsFormSelect("Language:","dataset[IdIdioma]",null,1,false);//,"Idioma das páginas inseridas no DataSet");
		$idioma->addOptionArray(pegaIdiomas());
		$formulario->addElement($idioma);
		$formulario->addElement(new XoopsFormText("Min. pages in dataset:","dataset[MinPagina]",5,5, 10));
		$formulario->insertBreak();
		$formulario->insertBreak();
		$method = new XoopsFormSelect("Dataset feeding method:","dataset[method]",null,1,false);
		$method->addOptionArray(array('M' => 'Manual', 'C' => 'Crawler + Quality Evaluation', 'Q' => 'Search engines + Quality Evaluation', 'P' => 'Search engines + P of N'));
		$formulario->addElement($method);
		$formulario->addElement(new XoopsFormLabel('If you are using a quality evaluation method, select which quality dimensions you would like FoxSet to evaluate and assign them a weight (click <a href="#" onclick="document.getElementById(\'table_qwds\').style.display = \'block\'; return false;">here</a> to see how the weight scale works):'));
		$tabela = '<div id="table_qwds" style="display: none;"><table cellspacing="10" style="border: 1px solid #000000;"><tr><td><strong>Weight</strong></td><td><strong>Description</strong></td></tr>';
		foreach ($qdws as $qwd) {
			$tabela .= '<tr><td align="center">' . $qwd['weight'] . '</td><td>' . $qwd['description'] . '</td></tr>';
		}
		$tabela .= '</table></div>';
		$formulario->addElement(new XoopsFormLabel($tabela));
		foreach ($qds as $qd) {
			$nametray = 'tray_qd' . $qd['id'];
			$$nametray = new XoopsFormElementTray("");
			$namecheck = 'check_qd' . $qd['id'];
			$$namecheck = new XoopsFormCheckBox("", "qds[{$qd['id']}][check]");
			$$namecheck->addOption("1", $qd['name']);
			$$nametray->addElement($$namecheck);
			$nameselect = 'select_qd' . $qd['id'];
			$$nameselect = new XoopsFormSelect(" - weight:", "qds[{$qd['id']}][weight]");
			foreach ($qdws as $qdw) {
				$$nameselect->addOption($qdw['id'], $qdw['weight']);
			}
			$$nametray->addElement($$nameselect);
			$formulario->addElement($$nametray);
		}
		//$formulario->addElement(new XoopsFormLabel('Minimum score for a page to be automatically added:', '<img src="/images/sliderHelp.png" /><input type="text" class="inputText fd_slider_cn_halfSize fd_range_0d00_1d00 fd_inc_0d01 fd_tween" name="dataset[minscore]" id="dataset_minscore_" size="5" maxlength="5" value="0.5" />'));
		//$formulario->addElement(new XoopsFormText("Minimum score for a page to be automatically added:", "dataset[minscore]",5,5));
		$formulario->addElement(new XoopsFormText("Space-separated list of seed URLs (only if you are using the crawler):", "dataset[seedURLs]",80,300));
		$formulario->addElement(new XoopsFormText("P of N (only if you are using it):","dataset[pofn]",5,5, 2));
		$formulario->insertBreak();
		$formulario->insertBreak();
		$mes = new XoopsFormRadio("Manual evaluation strategy:","dataset[manualEvaluationStrategy]","D");
		$mes ->addOption("D","Evaluate a given document on all queries");
		$mes ->addOption("Q","Evaluate all documents on a given query");
		$formulario->addElement($mes);
		$formulario->insertBreak();
		$formulario->addElement(new XoopsFormLabel("Select one of the scale method below:"));

		$escala1 = new XoopsFormRadio("","dataset[modoEscala]","1");
		$escala1->addOption("1","Standard relevance:");
		$formulario->addElement($escala1);

		$formulario->addElement(new XoopsFormLabel('',
		"<blockquote>
		<input type=\"checkbox\" name=\"escala1_1\" value=\"S\"> No relevance<br>
		<input type=\"checkbox\" name=\"escala1_2\" value=\"S\"> Low relevance<br>
		<input type=\"checkbox\" name=\"escala1_3\" value=\"S\" checked> Medium relevance<br>
		<input type=\"checkbox\" name=\"escala1_4\" value=\"S\" checked> High relevance<br>
		</blockquote>"));

		$escala2 = new XoopsFormRadio("","dataset[modoEscala]");
		$escala2->addOption("2","Score:");
		$formulario->addElement($escala2);

		$formulario->addElement(new XoopsFormLabel('',
		"<blockquote>
		<input type=\"checkbox\" name=\"escala2_1\" value=\"S\"> 1<br>
		<input type=\"checkbox\" name=\"escala2_2\" value=\"S\"> 2<br>
		<input type=\"checkbox\" name=\"escala2_3\" value=\"S\"> 3<br>
		<input type=\"checkbox\" name=\"escala2_4\" value=\"S\" checked> 4<br>
		<input type=\"checkbox\" name=\"escala2_5\" value=\"S\" checked> 5<br>
		</blockquote>"));

		$escala3 = new XoopsFormRadio("","dataset[modoEscala]");
		$escala3->addOption("3","Custom:");
		$formulario->addElement($escala3);

		$tray1 = new XoopsFormElementTray("","<br/>","","center");
		$tray1->addElement(new XoopsFormLabel('',"<blockquote>"));

		for ($i = 0; $i < 10; $i++){
			$traytmp = new XoopsFormElementTray("");

			$namecheck = "check_$i";
			$$namecheck = new XoopsFormCheckBox("","escala3_$i");
			$$namecheck->addOption("S","$i - ");

			$traytmp->addElement($$namecheck);
			$traytmp->addElement(new XoopsFormText("","dataset[escalap][$i]",20,20));
			$nametray = "tray_$i";
			$$nametray = $traytmp;
			$tray1->addElement($$nametray);
		}

		$tray1->addElement(new XoopsFormLabel('',"</blockquote>"));
		$formulario->addElement($tray1);

		$tray2 = new XoopsFormElementTray("","table","","right");
		$tray2->addElement(new XoopsFormButton("","incluir","Save","submit",true));
		$formulario->addElement($tray2);
		$smarty->assign("formulario", $formulario->render());
		$smarty->assign("display_conteudo", "formulariopadrao");
		finaliza_request();
		break;


		//---------------------------------------------
		//Dataset
		//

	case "criarperguntasalvar":
		$tmp = $_POST["pergunta"];
		$sql = sprintf("insert into query (query, dataset_id, description, active, collaborator_id, creation_datetime) values('%s',%s,'%s',1,%s,now())",
		$tmp['Nome'],
		$tmp['IdDataSet'],
		addslashes($tmp['Descricao']),
		$user->ID
		);

		if ($db->query($sql)){
			$smarty->assign("mensagem", "Query added successfully.");
		}else{
			$smarty->assign("mensagem", "Error adding query.");
			echo $sql .$db->error() ;;
		}
		break;

	case "editarperguntasalvar":
		if (isset($_POST["inativarquery"]) && $_POST["inativarquery"]){

			$sql = sprintf("update query set active = 0 Where id = %s ",
			$_GET["idpergunta"]);

			if ($db->query($sql)){
				$smarty->assign("mensagem", "Query deactivated successfully.");
			}else{
				$smarty->assign("mensagem", "Error deactivating query.");
			}
			break;
		}

		$tmp = $_POST["pergunta"];
		$sql = sprintf("update query set
						query = '%s', 
						description = '%s'
					Where id = %s ",
		$tmp['Nome'], $tmp['Descricao'], $_GET["idpergunta"]);

		if ($db->query($sql)){
			$smarty->assign("mensagem", "Query saved successfully.");
		}else{
			$smarty->assign("mensagem", "Error saving query.");
			echo $sql .$db->error() ;
		}
		break;

	case "criarpergunta":
		$ds = getDataset($_GET["iddataset"]);
		$smarty->assign('tituloFormulario', '<strong>Dataset:</strong> ' . $ds['context']);
		$formulario = new XoopsThemeForm("New query","inclusao_pergunta","?acao=criarperguntasalvar");
		$formulario->addElement(new XoopsFormText("Query:","pergunta[Nome]",100,255));
		$formulario->addElement( new XoopsFormHidden("pergunta[IdDataSet]",$_GET["iddataset"]));
		$formulario->addElement(new XoopsFormTextArea("Description:","pergunta[Descricao]","",5,96));
		$tray2 = new XoopsFormElementTray("","table","","right");
		$tray2->addElement(new XoopsFormButton("","incluirpergunta","Save","submit",true));
		$formulario->addElement($tray2);
		$smarty->assign("formulario", $formulario->render());
		$smarty->assign("display_conteudo", "formulariopadrao");
		finaliza_request();
		break;

	case "editarpergunta":
		$sql = sprintf("select * from query where id = %s",$_GET["idpergunta"]);
		$db->query($sql);

		$tmp = $db->fetch_assoc();
		$ds = getQueryDataset($_GET["idpergunta"]);
		$smarty->assign('tituloFormulario', '<strong>Dataset:</strong> ' . $ds['context']);
		$formulario = new XoopsThemeForm("Edit query","edicao_dataset","?acao=editarperguntasalvar&idpergunta=".$_GET["idpergunta"]);
		$formulario->addElement(new XoopsFormText("Query:","pergunta[Nome]",100,255,$tmp['query']));
		$formulario->addElement(new XoopsFormTextArea("Description:","pergunta[Descricao]",$tmp['description'],5,96));
		$tray2 = new XoopsFormElementTray("","table","","right");
		$tray2->addElement(new XoopsFormButton("","inativarquery","Deactivate","submit",true));
		$tray2->addElement(new XoopsFormButton("","salvarPergunta","Save","submit",true));
		$formulario->addElement($tray2);
		$smarty->assign("formulario", $formulario->render());
		$smarty->assign("display_conteudo", "formulariopadrao");
		finaliza_request();
		break;

	case "salvarpermissao":
		switch(true) {

			case (isset($_POST["adicionar"]) && $_POST["adicionar"]):

				switch (true){
					case (isset($_POST["adicionar"]["Coordenador"]) && $_POST["adicionar"]["Coordenador"]): $permissao = "C";break;
					case (isset($_POST["adicionar"]["Avaliador"]) && $_POST["adicionar"]["Avaliador"]): $permissao = "E";break;
					case (isset($_POST["adicionar"]["Utilizador"]) && $_POST["adicionar"]["Utilizador"]): $permissao = "U";break;
					default:
						die("erro")	;
				}
				if (!isset($_POST["usuario"]) || !$_POST["usuario"]) $mensagem = "Please, select a collaborator first.";

				if (isset($mensagem) && $mensagem){
					$smarty->assign("mensagem",$mensagem);
				}else{
					$sql = sprintf("insert into dataset_collaborator (dataset_id, collaborator_id, role) values(%s,%s,'%s')",
					$_GET["iddataset"],
					$_POST["usuario"],
					$permissao
					);

					if ($db->query($sql)){
						$smarty->assign("mensagem", "Role added successfully.");
					}else{
						$smarty->assign("mensagem", "Error adding role.");
						echo $sql .$db->error() ;;
					}
				}
				break;

			case (isset($_POST["remover"]) && $_POST["remover"]):
				switch (true){
					case (isset($_POST["remover"]["Coordenador"]) && $_POST["remover"]["Coordenador"]):
						$permissao = "C";
						if (!isset($_POST["coordenador"]) || !$_POST["coordenador"]) $mensagem = "Please, select a collaborator first.";
						else $idusuario = $_POST["coordenador"];
						break;
					case (isset($_POST["remover"]["Avaliador"]) && $_POST["remover"]["Avaliador"]):
						$permissao = "E";
						if (!isset($_POST["avaliador"]) || !$_POST["avaliador"]) $mensagem = "Please, select a collaborator first.";
						else $idusuario = $_POST["avaliador"];
						break;
					case (isset($_POST["remover"]["Utilizador"]) && $_POST["remover"]["Utilizador"]):
						$permissao = "U";
						if (!isset($_POST["utilizador"]) || !$_POST["utilizador"]) $mensagem = "Please, select a collaborator first.";
						else $idusuario = $_POST["utilizador"];
						break;
					default:
						die("error")	;
				}

				if (isset($mensagem) && $mensagem){
					$smarty->assign("mensagem",$mensagem);
				}else{
					$sql = sprintf("delete from dataset_collaborator
							where collaborator_id = %s and dataset_id = %s and role = '%s' ;",
					$idusuario, $_GET["iddataset"], $permissao);

					if ($db->query($sql)){
						$smarty->assign("mensagem", "Role removed successfully.");
					}else{
						$smarty->assign("mensagem", "Error removing role.");
						echo $sql .$db->error() ;;
					}
				}
		}



	case "permissoes":
		$iddataset = $_GET["iddataset"];
		$ds = getDataset($iddataset);
		$smarty->assign("iddataset", $iddataset);
		$smarty->assign("datasetContext", $ds['context']);

		$usuarios = new XoopsFormSelect("","usuario",null,20);
		$usuarios->_class = "SelectList";
		$usuarios->addOptionArray(pegaUsuariosSemPermissao($iddataset));
		$smarty->assign("lstUsuarios", $usuarios->render());

		$coordenadores = new XoopsFormSelect("","coordenador",null,5);
		$coordenadores->_class = "SelectList";
		$coordenadores->addOptionArray(pegaCoordenadores($iddataset));
		$smarty->assign("lstCoordenadores", $coordenadores->render());

		$utilizadores = new XoopsFormSelect("","utilizador",null,5);
		$utilizadores->_class = "SelectList";
		$utilizadores->addOptionArray(pegaUtilizadores($iddataset));
		$smarty->assign("lstUtilizadores", $utilizadores->render());

		$avaliadores = new XoopsFormSelect("","avaliador",null,5);
		$avaliadores->_class = "SelectList";
		$avaliadores->addOptionArray(pegaAvaliadores($iddataset));
		$smarty->assign("lstAvaliadores", $avaliadores->render());


		$adicionarCoordenador = new XoopsFormButton("","adicionar[Coordenador]",">","submit") ;
		$smarty->assign("adicionarCoordenador", $adicionarCoordenador->render());

		$adicionarUtilizador = new XoopsFormButton("","adicionar[Utilizador]",">","submit") ;
		$smarty->assign("adicionarUtilizador", $adicionarUtilizador->render());

		$adicionarAvaliador = new XoopsFormButton("","adicionar[Avaliador]",">","submit") ;
		$smarty->assign("adicionarAvaliador", $adicionarAvaliador->render());

		$removerCoordenador = new XoopsFormButton("","remover[Coordenador]","<","submit") ;
		$smarty->assign("removerCoordenador", $removerCoordenador->render());

		$removerUtilizador = new XoopsFormButton("","remover[Utilizador]","<","submit") ;
		$smarty->assign("removerUtilizador", $removerUtilizador->render());

		$removerAvaliador = new XoopsFormButton("","remover[Avaliador]","<","submit") ;
		$smarty->assign("removerAvaliador", $removerAvaliador->render());


		$smarty->assign("display_conteudo", "modulo_dataset_permissoes");


		finaliza_request();
		break;

	case "salvarstatusurl":
		switch(true) {

			case (isset($_POST["adicionarurl"]) && $_POST["adicionarurl"]):

				if (!isset($_POST["iurls"]) || !$_POST["iurls"]) $mensagem = "Please, select a document first.";

				if (isset($mensagem) && $mensagem){
					$smarty->assign("mensagem",$mensagem);
				}else{
					$sql = sprintf("UPDATE document SET active = 1 WHERE id = %s",
					$_POST["iurls"]
					);

					if ($db->query($sql)){
						$smarty->assign("mensagem", "Document activated successfully.");
					}else{
						$smarty->assign("mensagem", "Error activating document.");
						echo $sql .$db->error() ;;
					}
				}
				break;

			case ($_POST["removerurl"]):

				if (!isset($_POST["aurls"]) || !$_POST["aurls"]) $mensagem = "Please, select a document first.";

				if (isset($mensagem) && $mensagem){
					$smarty->assign("mensagem",$mensagem);
				}else{
					$sql = sprintf("UPDATE document SET active = 0 WHERE id = %s",
					$_POST["aurls"]
					);

					if ($db->query($sql)){
						$smarty->assign("mensagem", "Document deactivated successfully.");
					}else{
						$smarty->assign("mensagem", "Error deactivating document.");
						echo $sql .$db->error() ;;
					}
				}
				break;
		}


	case "urls":
		$iddataset = $_GET["iddataset"];
		if (isset($_GET['tipo']) && $_GET['tipo'] == 'score') {
			$sql1 = sprintf("UPDATE document SET active = 1 WHERE dataset_id = %s AND (score >= %s OR score IS NULL)", $iddataset, $_POST["score"]);
			$sql2 = sprintf("UPDATE document SET active = 0 WHERE dataset_id = %s AND score < %s", $iddataset, $_POST["score"]);
			if ($db->query($sql1) && $db->query($sql2)) {
				$smarty->assign("mensagem", "Documents activated successfully.");
			} else {
				$smarty->assign("mensagem", "Error activating documents.");
				echo $sql . $db->error();
			}
		}
		$ds = getDataset($iddataset);
		$smarty->assign("iddataset", $iddataset);
		$smarty->assign("datasetContext", $ds['context']);
		$smarty->assign("dataset`Method", $ds['method']);
		$smarty->assign("score", isset($_POST['score']) ? $_POST['score'] : 0.5);

		$docs = getDocuments($iddataset);
		$smarty->assign("documents", $docs);
		
		$urlsinativas = new XoopsFormSelect("","iurls",null,20);
		$urlsinativas->_class = "SelectList";
		$urlsinativas->addOptionArray(pegaUrlsInativas($iddataset));
		$smarty->assign("lstUrlsInativas", $urlsinativas->render());

		$urlsativas = new XoopsFormSelect("","aurls",null,20);
		$urlsativas->_class = "SelectList";
		$urlsativas->addOptionArray(pegaUrlsAtivas($iddataset));
		$smarty->assign("lstUrlsAtivas", $urlsativas->render());


		$adicionarCoordenador = new XoopsFormButton("","adicionarurl",">","submit") ;
		$smarty->assign("adicionarurl", $adicionarCoordenador->render());

		$removerCoordenador = new XoopsFormButton("","removerurl","<","submit") ;
		$smarty->assign("removerurl", $removerCoordenador->render());

		$smarty->assign("display_conteudo", "modulo_dataset_urls");


		finaliza_request();
		break;

	case "finalizar":
		$iddataset = $_GET["iddataset"];

		$sql = sprintf("UPDATE dataset set
								status = 'F', 
								finalization_datetime = now()
						WHERE id = %s ", $iddataset);

		if ($db->query($sql)){
			$smarty->assign("mensagem", "Dataset finalized successfully.");
		}else{
			$smarty->assign("mensagem", "Error finalizing dataset.");
			echo $sql .$db->error() ;
		}

		break;

	default:

}
}

$aux = '';
if (isset($_GET["selected"]) && $datasetID = $user->getDatasetID()) {
	$aux = "AND d.id = " . $datasetID;
}

$sql = sprintf("SELECT p.*, d.id AS DID, d.context AS DNOME, d.status AS DSTATUS
			FROM dataset d
			LEFT JOIN query p ON d.id = p.dataset_id
			INNER JOIN dataset_collaborator ud ON d.id = ud.dataset_id
			WHERE ud.collaborator_id = %s AND ud.role = 'C' %s
			GROUP BY p.id, d.id
			ORDER BY d.context",
$user->ID, $aux );
$q1 = $db->query($sql);
$datasets = array();
if ($db->rows($q1)) {
	while($tmp = $db->fetch_assoc($q1)){
		$datasets[$tmp['DNOME']]["id"] = $tmp['DID'];
		$datasets[$tmp['DNOME']]["status"] = $tmp['DSTATUS'];
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
		}
	}
} else {
	$smarty->assign("mensagem","There are no datasets under your coordination.");
}

$smarty->assign("datasets", $datasets);
$smarty->assign("display_conteudo", "modulo_dataset_index");

finaliza_request();

$smarty->assign("display_conteudo", "modulo_index");
finaliza_request();

function pegaIdiomas(){
	global $db;
	$sql = "select * from language";
	$db->query($sql);
	$idiomas = array();
	while($tmp = $db->fetch_assoc()){
		$idiomas[$tmp["id"]] = $tmp["name"];
	}
	return $idiomas;
}


function getDataset($id) {
	global $db;
	$sql = "SELECT * FROM dataset WHERE id = $id";
	$db->query($sql);
	return $db->fetch_assoc();
}


function getQueryDataset($queryID) {
	global $db;
	$sql = "SELECT d.* FROM dataset AS d INNER JOIN query AS q ON q.dataset_id = d.id WHERE q.id = $queryID";
	$db->query($sql);
	return $db->fetch_assoc();
}


function getDocuments($id) {
	global $db;
	$sql = sprintf("SELECT * FROM document WHERE dataset_id = %s ORDER BY score", $id);
	$db->query($sql);
	$docs = array();
	while($tmp = $db->fetch_assoc()){
		$docs[] = $tmp;
	}
	return $docs;
}



function pegaCoordenadores($iddataset){
	return pegaPermissao($iddataset,"C");
}

function pegaAvaliadores($iddataset){
	return pegaPermissao($iddataset,"E");
}

function pegaUtilizadores($iddataset){
	return pegaPermissao($iddataset,"U");
}

function pegaPermissao($iddataset,$permissao){
	global $db;
	$sql = sprintf("SELECT * FROM collaborator u
			INNER JOIN dataset_collaborator ud ON u.id = ud.collaborator_id
			WHERE ud.dataset_id = %s AND ud.role = '%s'
			ORDER BY name",$iddataset,$permissao);
	$db->query($sql);
	$usuarios = array();
	while($tmp = $db->fetch_assoc()){
		$usuarios[$tmp["id"]] = $tmp["name"];
	}
	return $usuarios;
}

function pegaUsuariosSemPermissao($iddataset){
	global $db;
	$sql = sprintf("SELECT collaborator_id FROM dataset_collaborator
				WHERE dataset_id =%s", $iddataset);
	$db->query($sql);

	$aux = '0';
	while($tmp = $db->fetch_assoc()){
		$aux .= ', ' . $tmp["collaborator_id"];
	}

	$sql = sprintf("SELECT * FROM collaborator
			WHERE id NOT IN (%s)
			ORDER BY name",$aux);
	$db->query($sql);
	$usuarios = array();
	while($tmp = $db->fetch_assoc()){
		$usuarios[$tmp["id"]] = $tmp["name"];
	}
	return $usuarios;
}



function pegaUrlsInativas($iddataset){
	global $db;
	$sql = sprintf("SELECT id, url FROM document
				WHERE active = 0 and dataset_id = %s", $iddataset);
	$db->query($sql);
	$urls = array();
	while($tmp = $db->fetch_assoc()){
		$urls[$tmp["id"]] = $tmp["url"];
	}
	return $urls;
}
function pegaUrlsAtivas($iddataset){
	global $db;
	$sql = sprintf("SELECT id, url FROM document
				WHERE active = 1 and dataset_id = %s", $iddataset);
	$db->query($sql);
	
	$urls = array();
	while($tmp = $db->fetch_assoc()){
		$urls[$tmp["id"]] = $tmp["url"];
	}
	return $urls;
}

?>