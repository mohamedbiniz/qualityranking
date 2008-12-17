<?php /* Smarty version 2.6.7, created on 2008-10-28 16:48:56
         compiled from modulo_testset_index.tpl */ ?>
<form name='exportar_testset' id='exportar_testset' action='?acao=exportar' method='post' >
<center>

Please select one dataset and the dataset's queries to export.

<?php if (count($_from = (array)$this->_tpl_vars['datasets'])):
    foreach ($_from as $this->_tpl_vars['dataset'] => $this->_tpl_vars['atual']):
?>
<p>
	<div><input type="radio" name="iddataset" value="<?php echo $this->_tpl_vars['datasets'][$this->_tpl_vars['dataset']]['id']; ?>
">  <b>DataSet:</b> <?php echo $this->_tpl_vars['dataset']; ?>
</div>

	<?php if (is_array ( $this->_tpl_vars['datasets'][$this->_tpl_vars['dataset']]['perguntas'] )): ?>
		<br/>
		<div><b>Queries</b></div>
		<table class="listagem" cellpadding="0" cellspacing="0">	
			<tr>
				<td> </td>
				<td>Name</td>
				<td>Status</td>
				<td>Evaluated pages</td>
				<td>Relevant pages</td>
			</tr>
				<?php if (count($_from = (array)$this->_tpl_vars['datasets'][$this->_tpl_vars['dataset']]['perguntas'])):
    foreach ($_from as $this->_tpl_vars['pergunta'] => $this->_tpl_vars['perguntaatual']):
?>
				<tr>
					<td><input type="checkbox" checked='checked' name="pergunta[<?php echo $this->_tpl_vars['datasets'][$this->_tpl_vars['dataset']]['id']; ?>
][<?php echo $this->_tpl_vars['perguntaatual']['id']; ?>
]" value="true"></td>
					<td><?php echo $this->_tpl_vars['perguntaatual']['query']; ?>
</td>
					<td class="center"><?php if ($this->_tpl_vars['perguntaatual']['active'] == 1): ?>Active<?php else: ?>Inactive<?php endif; ?></td>
					<td><?php echo $this->_tpl_vars['perguntaatual']['pa']; ?>
</td>
					<td><?php echo $this->_tpl_vars['perguntaatual']['par']; ?>
</td>
				</tr>
				<?php endforeach; endif; unset($_from); ?>			
		</table><br/>
		

	<?php endif; ?>
</p>
<hr/>
<?php endforeach; endif; unset($_from); ?>
<br>
		<input type="submit" name="exportar" value="Export">
</center>
</form>