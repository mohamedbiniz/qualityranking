<?php /* Smarty version 2.6.7, created on 2008-10-28 16:56:01
         compiled from modulo_dataset_index.tpl */ ?>
<center>
<?php if (count($_from = (array)$this->_tpl_vars['datasets'])):
    foreach ($_from as $this->_tpl_vars['dataset'] => $this->_tpl_vars['atual']):
?>
<p>
	<div><b>Dataset:</b> <?php echo $this->_tpl_vars['dataset']; ?>
</div>
	<div>
		<?php if ($this->_tpl_vars['datasets'][$this->_tpl_vars['dataset']]['status'] != 'F'): ?><a href="?acao=urls&iddataset=<?php echo $this->_tpl_vars['datasets'][$this->_tpl_vars['dataset']]['id']; ?>
">Documents</a> &nbsp; - &nbsp;<?php endif; ?>
		<a href="?acao=permissoes&iddataset=<?php echo $this->_tpl_vars['datasets'][$this->_tpl_vars['dataset']]['id']; ?>
">Roles</a> &nbsp; - &nbsp; 
		<?php if ($this->_tpl_vars['datasets'][$this->_tpl_vars['dataset']]['status'] != 'F'): ?><a href="?acao=criarpergunta&iddataset=<?php echo $this->_tpl_vars['datasets'][$this->_tpl_vars['dataset']]['id']; ?>
">New query</a> &nbsp; - &nbsp;
		<a href="?acao=finalizar&iddataset=<?php echo $this->_tpl_vars['datasets'][$this->_tpl_vars['dataset']]['id']; ?>
">Finalize</a><?php endif; ?>
		<?php if ($this->_tpl_vars['datasets'][$this->_tpl_vars['dataset']]['status'] == 'F'): ?>Finalized<?php endif; ?>
	</div>
	<?php if (is_array ( $this->_tpl_vars['datasets'][$this->_tpl_vars['dataset']]['perguntas'] )): ?>
		<br/>
		<div><b>Queries</b></div>
		<table class="listagem" cellpadding="0" cellspacing="0">	
			<tr>
				<td>Query</td>
				<td>Active</td>
				<td>Evaluated pages</td>
				<td>Relevant pages</td>
				<td> </td>
			</tr>
				<?php if (count($_from = (array)$this->_tpl_vars['datasets'][$this->_tpl_vars['dataset']]['perguntas'])):
    foreach ($_from as $this->_tpl_vars['pergunta'] => $this->_tpl_vars['perguntaatual']):
?>
				<tr>
					<td><?php echo $this->_tpl_vars['perguntaatual']['query']; ?>
</td>
					<td class="center"><?php if ($this->_tpl_vars['perguntaatual']['active'] == 1): ?>Yes<?php else: ?>No<?php endif; ?></td>
					<td><?php echo $this->_tpl_vars['perguntaatual']['pa']; ?>
</td>
					<td><?php echo $this->_tpl_vars['perguntaatual']['par']; ?>
</td>
					<td><?php if ($this->_tpl_vars['datasets'][$this->_tpl_vars['dataset']]['status'] != 'F'): ?>
						<?php if ($this->_tpl_vars['perguntaatual']['active'] == 1): ?>
							<a href="?acao=editarpergunta&idpergunta=<?php echo $this->_tpl_vars['perguntaatual']['id']; ?>
">Edit</a>
						<?php endif; ?>
					<?php endif; ?></td>
				</tr>
				<?php endforeach; endif; unset($_from); ?>			
		</table><br/>
	<?php endif; ?>
</p>
<hr/>
<?php endforeach; endif; unset($_from); ?>
</center>