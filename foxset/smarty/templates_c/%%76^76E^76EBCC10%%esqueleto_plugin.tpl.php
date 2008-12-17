<?php /* Smarty version 2.6.7, created on 2008-10-26 16:00:54
         compiled from esqueleto_plugin.tpl */ ?>
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
	<div id="topoplugin">
		<center>	
			<span class="titulo1">FoxSet</span><br/>
			<span class="titulo2">A tool for creating datasets</span><br/><br/>
		</center>
	</div>
	<!-- Fim do inicio -->
<div id="principal">
	
	<?php if ($this->_tpl_vars['titulomodulo'] != ""): ?>
		<div id="titulomodulo"><?php echo $this->_tpl_vars['titulomodulo']; ?>
</div>
	<?php endif; ?>
	
	<?php if ($this->_tpl_vars['mensagem'] != ""): ?>
		<div id="mensagem"><?php echo $this->_tpl_vars['mensagem']; ?>
</div>
	<?php endif; ?>
	<!-- Inicio do conteudo -->
	<div id="conteudoPlugin" >
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