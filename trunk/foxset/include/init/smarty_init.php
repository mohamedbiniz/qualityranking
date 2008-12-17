<?php
require(DIR_SMARTY_LIBS.'Smarty.class.php');

class Template extends Smarty {
	function Template() {
		$this->Smarty();
		$this->template_dir = DIR_SMARTY . '/templates/';
		$this->compile_dir = DIR_SMARTY . '/templates_c/';
		$this->config_dir = DIR_SMARTY . '/configs/';
		$this->cache_dir = DIR_SMARTY . '/cache/';

		$this->caching = false;
		$this->compile_check = true;
	}
}

$GLOBALS['smarty'] = new Template;
/* @var $smarty Template */
?>
