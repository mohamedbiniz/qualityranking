<?php /* Smarty version 2.6.7, created on 2008-10-07 17:03:59
         compiled from avaliar_listaurls.tpl */ ?>
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

<div>
	<center>	
		<span class="titulo1">FoxSet</span><br/>
	</center>
</div>
	
<b><?php echo $this->_tpl_vars['Nome']; ?>
</b><br><?php echo $this->_tpl_vars['Descricao']; ?>
<br><br>

<div id="avlistaurl" class="lala"">
<ul>
<?php if (count($_from = (array)$this->_tpl_vars['urls'])):
    foreach ($_from as $this->_tpl_vars['idurl'] => $this->_tpl_vars['url_atual']):
?>
	<li>
		<a href="/avaliar.php?idurl=<?php echo $this->_tpl_vars['url_atual']['id']; ?>
&idpergunta=<?php echo $this->_tpl_vars['idpergunta']; ?>
" target="_top">
			<b><?php echo $this->_tpl_vars['url_atual']['url']; ?>
</b>
		</a>
	</li>
<?php endforeach; endif; unset($_from); ?>
</ul>	
</div>


<!-- Inicio do rodape -->
<?php $_smarty_tpl_vars = $this->_tpl_vars;
$this->_smarty_include(array('smarty_include_tpl_file' => "rodape.tpl", 'smarty_include_vars' => array()));
$this->_tpl_vars = $_smarty_tpl_vars;
unset($_smarty_tpl_vars);
 ?>
<!-- Fim do rodape -->