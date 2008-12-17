<?php /* Smarty version 2.6.7, created on 2008-10-28 19:03:32
         compiled from avaliar_listadocumentos.tpl */ ?>
<b><?php echo $this->_tpl_vars['docs'][0]['dataset_context']; ?>
</b><br><?php echo $this->_tpl_vars['docs'][0]['dataset_description']; ?>
<br><br>

<ul>
<?php if (count($_from = (array)$this->_tpl_vars['docs'])):
    foreach ($_from as $this->_tpl_vars['docID'] => $this->_tpl_vars['doc']):
?>
	<li><a href="?idurl=<?php echo $this->_tpl_vars['doc']['document_id']; ?>
"><?php echo $this->_tpl_vars['doc']['document_url']; ?>
</a></li>
<?php endforeach; endif; unset($_from); ?>
</ul>	