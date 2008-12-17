<?php /* Smarty version 2.6.7, created on 2008-07-15 17:32:13
         compiled from esqueleto.tpl */ ?>
<?php $_smarty_tpl_vars = $this->_tpl_vars;
$this->_smarty_include(array('smarty_include_tpl_file' => "cabecalho.tpl", 'smarty_include_vars' => array()));
$this->_tpl_vars = $_smarty_tpl_vars;
unset($_smarty_tpl_vars);
 ?>
<!-- Fim do cabecalho -->
<?php if ($this->_tpl_vars['display_form_style'] != ""): ?>
<!-- Inicio do Css do Formulario -->
<style type="text/css">
	<!--
		<?php echo $this->_tpl_vars['display_form_style']; ?>

	//-->
</style>
<!-- Fim do Css do Formulario -->
<?php endif; ?>
	<!-- Inicio do inicio -->
	<?php $_smarty_tpl_vars = $this->_tpl_vars;
$this->_smarty_include(array('smarty_include_tpl_file' => "inicio.tpl", 'smarty_include_vars' => array()));
$this->_tpl_vars = $_smarty_tpl_vars;
unset($_smarty_tpl_vars);
 ?>
	<!-- Fim do inicio -->
<div id="principal">
	
	<div id="menus">
		<?php if ($this->_tpl_vars['menu'] != ""): ?>
		<?php $_smarty_tpl_vars = $this->_tpl_vars;
$this->_smarty_include(array('smarty_include_tpl_file' => "menu.tpl", 'smarty_include_vars' => array()));
$this->_tpl_vars = $_smarty_tpl_vars;
unset($_smarty_tpl_vars);
 ?>
		<?php endif; ?>
		
		<?php if ($this->_tpl_vars['submenu'] != ""): ?>
		<?php $_smarty_tpl_vars = $this->_tpl_vars;
$this->_smarty_include(array('smarty_include_tpl_file' => "submenu.tpl", 'smarty_include_vars' => array()));
$this->_tpl_vars = $_smarty_tpl_vars;
unset($_smarty_tpl_vars);
 ?>
		<?php endif; ?>
		
	</div>
	
	<div id="espaco"></div>
	<?php if ($this->_tpl_vars['titulomodulo'] != ""): ?>
		<div id="titulomodulo"><?php echo $this->_tpl_vars['titulomodulo']; ?>
</div>
	<?php endif; ?>
	
	<?php if ($this->_tpl_vars['mensagem'] != ""): ?>
		<div id="mensagem"><?php echo $this->_tpl_vars['mensagem']; ?>
</div>
	<?php endif; ?>
	
	<!-- Inicio do conteudo -->
	<div id="conteudo" >
		<?php $_smarty_tpl_vars = $this->_tpl_vars;
$this->_smarty_include(array('smarty_include_tpl_file' => ($this->_tpl_vars['display_conteudo']).".tpl", 'smarty_include_vars' => array()));
$this->_tpl_vars = $_smarty_tpl_vars;
unset($_smarty_tpl_vars);
 ?>
 	</div>
	<div id="footer"></div>
	<!-- Fim do conteudo -->
	
</div>


<!-- Inicio do rodape -->
<?php $_smarty_tpl_vars = $this->_tpl_vars;
$this->_smarty_include(array('smarty_include_tpl_file' => "rodape.tpl", 'smarty_include_vars' => array()));
$this->_tpl_vars = $_smarty_tpl_vars;
unset($_smarty_tpl_vars);
 ?>
<!-- Fim do rodape -->