<?php /* Smarty version 2.6.7, created on 2008-10-28 19:06:45
         compiled from avaliar_topoframe_documentos.tpl */ ?>
<?php $_smarty_tpl_vars = $this->_tpl_vars;
$this->_smarty_include(array('smarty_include_tpl_file' => "cabecalho.tpl", 'smarty_include_vars' => array()));
$this->_tpl_vars = $_smarty_tpl_vars;
unset($_smarty_tpl_vars);
 ?>
<!-- Fim do cabecalho -->

	<!-- Inicio do inicio -->
	<div><strong><?php echo $this->_tpl_vars['query']['query']; ?>
</strong></div>
	<div><?php echo $this->_tpl_vars['query']['description']; ?>
</div>

<!-- Inicio do rodape -->
<?php $_smarty_tpl_vars = $this->_tpl_vars;
$this->_smarty_include(array('smarty_include_tpl_file' => "rodape.tpl", 'smarty_include_vars' => array()));
$this->_tpl_vars = $_smarty_tpl_vars;
unset($_smarty_tpl_vars);
 ?>
<!-- Fim do rodape -->