<?php

class logon{
	var $ID = '';
	var $USERNAME = '';
	var $NOME = '';
	var $LOGIN = 0;
	var $EMAIL = 0;
	var $ADMIN = false;
	var $COORD = false;
	var $LOGED = false;
	var $ERROR = '';

	function logon($login='', $passdw=''){
		global $db;

		// Validando usuário:
		if((($login!='') and ($passdw!='')) and (!$this->LOGED)){
			// Validando usuário:
			$sql = "SELECT *
					FROM collaborator
					WHERE username = '" . addslashes($login) . "' AND password = '". md5($passdw) ."' AND active = 1;";

			$sql_handle = $db->query($sql) or die("<br>". $db->error() ."<br>Error reading username and password.<br>");
			if($loginAtual = $db->fetch($sql_handle)){
				$this->ID = $loginAtual['id'];
				$this->USERNAME = $loginAtual['username'];
				$this->NOME = $loginAtual['name'];
				$this->EMAIL = $loginAtual['email'];
				$this->ADMIN = ($loginAtual['administrator'] == "1")? true:false;
				$this->COORD = ($loginAtual['coordinator'] == "1")? true:false;
				$this->LOGED = true;
				$this->ERROR = '';
				
				/*if (!$this->COORD) {
					$sql = "SELECT dataset_id FROM dataset_collaborator 
							WHERE role = 'C' AND collaborator_id = " . $this->ID;
					$db->query($sql);
		
					if ($db->rows()) {
						$this->COORD = true;
					}	

				}*/

			}
			$db->free($sql_handle);

			if($this->LOGED){
				// Lendo permissões:

			}else{
				$this->ID = '';
				$this->USERNAME = '';
				$this->NOME = '';
				$this->PERMISSAO = 0;
				$this->LOGED = false;
				$this->ADMIN = false;
				$this->COORD = false;
				$this->ERROR = '<b><span style="color:red">ERROR</span>, invalid username or password!</b>';
			}
		}
	}

	function forceLogon($login='', $senha=''){
		if((($login!='') and ($senha!='')) and (!$this->LOGED)){
			$this->logon($login, $senha);
		}else{
			$this->ID = '';
			$this->USERNAME = '';
			$this->NOME = '';
			$this->PERMISSAO = 0;
			$this->LOGED = false;
			$this->ERROR = '<b><span style="color:red">ERROR!</span> You have to provide<br>a username and password.</b>';
		}
	}

	function logoff(){
		$this->ID = '';
		$this->USERNAME = '';
		$this->NOME = '';
		$this->PERMISSAO = 0;
		$this->LOGED = false;
		$this->ERROR = '';
		$this->ADMIN = false;
	}

	function getId() {
		return $this->ID;
	}

	function getNome() {
		return $this->NOME;
	}

	function getLoged() {
		return $this->LOGED;
	}

	function getError() {
		return $this->ERROR;
	}

	function getEmail() {
		return $this->EMAIL;
	}
	function getLogin() {
		return $this->LOGIN;
	}

	function hasRole($role) {
		global $db;
		$sql = sprintf("SELECT dataset_id
						FROM dataset_collaborator
						WHERE collaborator_id = %s AND role = '%s'", $this->ID, $role);
		$db->query($sql);
		return $db->rows();
	}
	
	function getRole($datasetId = '') {
		global $db;
		if ($datasetId == '') {
			if (!isset($_SESSION['datasetID'])) return null;
			$datasetId = $_SESSION['datasetID'];
		}
		$sql = sprintf("SELECT role FROM collaborator AS c
						INNER JOIN dataset_collaborator AS dc ON dc.collaborator_id = c.id
						WHERE c.id = %s AND dc.dataset_id = %s", $this->ID, $datasetId);
		$db->query($sql);
		if (!$db->rows()) return null;
		return $db->result(0, 0);
	}
	
	function getDatasetID() {
		return isset($_SESSION['datasetID']) ? $_SESSION['datasetID'] : 0;
	}
}

?>
