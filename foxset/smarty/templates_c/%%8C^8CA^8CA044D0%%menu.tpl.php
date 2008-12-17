<?php /* Smarty version 2.6.7, created on 2008-07-15 17:32:13
         compiled from menu.tpl */ ?>
<div id="menu">
	<div class="titulobloco titulomenu">
		Menu	
	</div>
	<ul class="nav">
	<?php if (count($_from = (array)$this->_tpl_vars['menu'])):
    foreach ($_from as $this->_tpl_vars['id_menu'] => $this->_tpl_vars['menu_atual']):
?>
		<li class="navList"><a href="<?php if ($this->_tpl_vars['menu_atual']['pagina']):  echo $this->_tpl_vars['menu_atual']['pagina'];  else: ?>/desenvolvimento.php<?php endif; ?>"><img src="<?php echo $this->_tpl_vars['menu_atual']['icone']; ?>
" title="<?php echo $this->_tpl_vars['menu_atual']['nome']; ?>
" align="left" hspace="2" vspace="1"><?php echo $this->_tpl_vars['menu_atual']['nome']; ?>
</a></li>
	<?php endforeach; endif; unset($_from); ?>
		<?php if ($this->_tpl_vars['logado'] == true): ?>
			<li class="navList"><a href="/logout.php"><img src="/images/ico_usuarios.gif" title="Sair" align="left" hspace="2" vspace="1">Logout</a></li>
		<?php endif; ?>
	 </ul>
</div>
