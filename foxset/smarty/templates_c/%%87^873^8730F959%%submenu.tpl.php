<?php /* Smarty version 2.6.7, created on 2008-07-15 18:15:52
         compiled from submenu.tpl */ ?>
<br/>
<div id="menu">
	<div class="titulobloco titulomenu">
		<?php echo $this->_tpl_vars['submenu_titulo']; ?>

	</div>
	<ul class="nav">
	<?php if (count($_from = (array)$this->_tpl_vars['submenu'])):
    foreach ($_from as $this->_tpl_vars['id_menu'] => $this->_tpl_vars['menu_atual']):
?>
		<li class="navList"><a href="<?php echo $this->_tpl_vars['menu_atual']['pagina']; ?>
"><img src="<?php echo $this->_tpl_vars['menu_atual']['icone']; ?>
" title="<?php echo $this->_tpl_vars['menu_atual']['nome']; ?>
" align="left" hspace="2" vspace="1"><?php echo $this->_tpl_vars['menu_atual']['nome']; ?>
</a></li>
	<?php endforeach; endif; unset($_from); ?>
	 </ul>
</div>
