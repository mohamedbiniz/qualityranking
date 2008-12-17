<?php /* Smarty version 2.6.7, created on 2008-10-28 18:16:27
         compiled from avaliar_listaperguntas.tpl */ ?>
<b><?php echo $this->_tpl_vars['perguntas'][0]['DataSet']; ?>
</b><br><?php echo $this->_tpl_vars['perguntas'][0]['Desc_DataSet']; ?>
<br><br>

<ul>
<?php if (count($_from = (array)$this->_tpl_vars['perguntas'])):
    foreach ($_from as $this->_tpl_vars['idpergunta'] => $this->_tpl_vars['pergunta_atual']):
?>
	<li>
		<a href="?idpergunta=<?php echo $this->_tpl_vars['pergunta_atual']['IdPergunta']; ?>
">
			<b><?php echo $this->_tpl_vars['pergunta_atual']['Pergunta']; ?>
</b>
		</a>
		<br>
		<?php echo $this->_tpl_vars['pergunta_atual']['Desc_Pergunta']; ?>

	</li>
<?php endforeach; endif; unset($_from); ?>
</ul>	