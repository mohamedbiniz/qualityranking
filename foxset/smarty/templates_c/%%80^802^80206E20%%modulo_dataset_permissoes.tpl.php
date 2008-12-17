<?php /* Smarty version 2.6.7, created on 2008-10-27 20:27:20
         compiled from modulo_dataset_permissoes.tpl */ ?>
<form name='inclusao_permissao_dataset' id='inclusao_permissao_dataset' action='?acao=salvarpermissao&iddataset=<?php echo $this->_tpl_vars['iddataset']; ?>
' method='post' onsubmit='return xoopsFormValidate_inclusao_permissao_dataset();'>

<center>
<p><strong>Dataset:</strong> <?php echo $this->_tpl_vars['datasetContext']; ?>
</p>
<b>Roles</b><br/>

<table>
<tr>
	<td rowspan="3">Collaborators:<br/><?php echo $this->_tpl_vars['lstUsuarios']; ?>
</td>
	<td style="padding: 0 30px 0 30px"><?php echo $this->_tpl_vars['adicionarCoordenador']; ?>
<br><?php echo $this->_tpl_vars['removerCoordenador']; ?>
</td>
	<td>Coordinators:<br/><?php echo $this->_tpl_vars['lstCoordenadores']; ?>
</td>
</tr>

<tr>
	<td align="center"><?php echo $this->_tpl_vars['adicionarUtilizador']; ?>
<br><?php echo $this->_tpl_vars['removerUtilizador']; ?>
</td>
	<td>Users:<br/><?php echo $this->_tpl_vars['lstUtilizadores']; ?>
</td>
</tr>

<tr>
	<td align="center"><?php echo $this->_tpl_vars['adicionarAvaliador']; ?>
<br><?php echo $this->_tpl_vars['removerAvaliador']; ?>
</td>
	<td>Evaluators:<br/><?php echo $this->_tpl_vars['lstAvaliadores']; ?>
</td>
</tr>
</table>

</center>
</form>