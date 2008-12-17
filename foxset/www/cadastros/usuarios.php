<?php
$requerlogin = true;
define('DIR_SITE_ROOT', '../../');
require_once "../inicio.php";
require DIR_INCLUDES . "xoopsformloader.php";

$smarty->assign("titulomodulo","Collaborators");
$smarty->assign("submenu_titulo", "Collaborators");

$submenu["nome"] = "New collaborator";
$submenu["pagina"] = "?acao=criar";
$submenu["icone"] = "/images/ico_usuarios.gif";
$submenu["ordem"] = "1";
$submenus[] = $submenu;

unset ($submenu);
$smarty->assign("submenu",$submenus);

if (isset($_GET['acao'])) {
switch($_GET["acao"]){

	case "criarsalvar":
		$usr = $_POST["usuario"];

		//O login deve ser único no banco
		$sql = "select id from collaborator where username = '" . $usr['Login'] . "'";
		$db->query($sql);

		if (!$db->rows()) {
			$sql = sprintf("insert into collaborator (username, password, name, email, active, administrator, coordinator) values ('%s','%s','%s','%s', 1,'%s','%s')",
				$usr['Login'], md5($usr['Senha']), $usr['Nome'], $usr['Email'],
				(isset($usr['Administrador']) && $usr['Administrador'])?"1":"0", (isset($usr['Coordenador']) && $usr['Coordenador'])?"1":"0" );

			if ($db->query($sql)){
				$smarty->assign("mensagem", "Collaborator added successfully.");
			}else{
				$smarty->assign("mensagem", "Error adding collaborator.");
			}
		} else {
			$smarty->assign("mensagem", 'Error: username (' . $usr['Login'] . ') already taken.');
		}
		break;

	case "editarsalvar":
		if (isset($_POST["excluirusuario"]) && $_POST["excluirusuario"]){

			$usr = $_POST["usuario"];
			$sql = sprintf("update collaborator set active = 0 Where id = %s ",
			$_GET["idusuario"]);

			if ($db->query($sql)){
				$smarty->assign("mensagem", "Collaborator deactivated successfully.");
			}else{
				$smarty->assign("mensagem", "Error deactivating collaborator.");
			}
			break;
		}
		$usr = $_POST["usuario"];
		$sql = sprintf("update collaborator set
				username = '%s', 
				password = '%s', 
				name = '%s', 
				email = '%s', 
				administrator = '%s',
				coordinator = '%s'
			Where id = %s ",
		$usr['Login'],
		($usr['Senha'] == $usr['SenhaTmp'])?$usr['Senha']:md5($usr['Senha']),
		$usr['Nome'],
		$usr['Email'],
		(isset($usr['Administrador']) && $usr['Administrador'])?"1":"0",
		(isset($usr['Coordenador']) && $usr['Coordenador'])?"1":"0",
		$_GET["idusuario"]);

		if ($db->query($sql)){
			$smarty->assign("mensagem", "Collaborator saved successfully.");
		}else{
			$smarty->assign("mensagem", "Error saving collaborator.");
		}
		break;

	case "criar":
		$formulario = new XoopsThemeForm("New collaborator","inclusao_usuario","?acao=criarsalvar");
		$formulario->addElement(new XoopsFormText("Name:","usuario[Nome]",50,100));
		$formulario->addElement(new XoopsFormText("E-mail:","usuario[Email]",50,100));
		$formulario->addElement(new XoopsFormText("Username:","usuario[Login]",20,20));
		$formulario->addElement(new XoopsFormPassword("Password:","usuario[Senha]",20,32));
		if ($user->ADMIN) {
			$administrador = new XoopsFormCheckBox("Administrator:","usuario[Administrador]", (isset($algumacoisa) && $algumacoisa) ? "true":"");
			$administrador->addOption("true"," ");
			$formulario->addElement($administrador);
		}
		if ($user->ADMIN or $user->COORD) {
			$coordenador = new XoopsFormCheckBox("Coordinator:","usuario[Coordenador]", (isset($algumacoisa) && $algumacoisa) ? "true":"");
			$coordenador->addOption("true"," ");
			$formulario->addElement($coordenador);
		}
		$tray2 = new XoopsFormElementTray("","table","","right");
		$tray2->addElement(new XoopsFormButton("","incluirusuario","Save","submit",true));
		$formulario->addElement($tray2);
		$smarty->assign("formulario", $formulario->render());
		$smarty->assign("display_conteudo", "formulariopadrao");
		finaliza_request();
		break;

	case "editar":
		$sql = sprintf("select * from collaborator where id = %s",$_GET["idusuario"]);
		$db->query($sql);

		$tmp = $db->fetch_assoc();

		$formulario = new XoopsThemeForm("Edit collaborator","Edicao_usuario","?acao=editarsalvar&idusuario=".$_GET["idusuario"]);
		$formulario->addElement(new XoopsFormText("Name:","usuario[Nome]",50,100,$tmp['name']));
		$formulario->addElement(new XoopsFormText("E-mail:","usuario[Email]",50,100,$tmp['email']));
		$formulario->addElement(new XoopsFormText("Username:","usuario[Login]",20,20,$tmp['username']));
		$formulario->addElement(new XoopsFormPassword("Password:","usuario[Senha]",20,32,$tmp['password']));
		$formulario->addElement(new XoopsFormHidden("usuario[SenhaTmp]",$tmp['password']));
		$administrador = new XoopsFormCheckBox("Administrator:","usuario[Administrador]", ($tmp['administrator']) ? "true":"");
		$administrador->addOption("true"," ");
		$formulario->addElement($administrador);
		$coordenador = new XoopsFormCheckBox("Coordinator:","usuario[Coordenador]", ($tmp['coordinator']) ? "true":"");
		$coordenador->addOption("true"," ");
		$formulario->addElement($coordenador);
		$tray2 = new XoopsFormElementTray("","table","","right");
		$tray2->addElement(new XoopsFormButton("","excluirusuario","Disable","submit",true));
		$tray2->addElement(new XoopsFormButton("","salvarusuario","Save","submit",true));
		$formulario->addElement($tray2);
		$smarty->assign("formulario", $formulario->render());
		$smarty->assign("display_conteudo", "formulariopadrao");
		finaliza_request();

		break;
	default:

}
}

$sql = "select * from collaborator where active = 1 order by name";
$db->query($sql);
$usuarios = array();
while($tmp = $db->fetch_assoc()){
	$usuarios[] = $tmp;
}
$smarty->assign("usuarios", $usuarios);
$smarty->assign("display_conteudo", "modulo_usuarios_index");

finaliza_request();

?>