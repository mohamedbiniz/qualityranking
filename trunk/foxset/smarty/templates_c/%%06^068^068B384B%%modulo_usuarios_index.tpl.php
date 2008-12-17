<?php /* Smarty version 2.6.7, created on 2008-10-27 21:08:21
         compiled from modulo_usuarios_index.tpl */ ?>
<center>
<table cellpadding="0" cellspacing="0" class="listagem">	
	<tr class="titulo">
		<td>Username</td>
		<td>Name</td>
		<td>E-mail</td>
		<td>Administrator</td>
		<td>Coordinator</td>
		<td> </td>
	</tr>
	<?php if (count($_from = (array)$this->_tpl_vars['usuarios'])):
    foreach ($_from as $this->_tpl_vars['idusuario'] => $this->_tpl_vars['atual']):
?>
	<tr>
		<td><?php echo $this->_tpl_vars['atual']['username']; ?>
</td>
		<td><?php echo $this->_tpl_vars['atual']['name']; ?>
</td>
		<td><?php echo $this->_tpl_vars['atual']['email']; ?>
</td>
		<td class="center"><?php if ($this->_tpl_vars['atual']['administrator']): ?>Yes<?php else: ?>No<?php endif; ?></td>
		<td class="center"><?php if ($this->_tpl_vars['atual']['coordinator']): ?>Yes<?php else: ?>No<?php endif; ?></td>
		<td><a href="?acao=editar&idusuario=<?php echo $this->_tpl_vars['atual']['id']; ?>
">Edit</a></td>
	</tr>
	<?php endforeach; endif; unset($_from); ?>
</table>
</center>