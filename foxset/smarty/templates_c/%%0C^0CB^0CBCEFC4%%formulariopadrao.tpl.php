<?php /* Smarty version 2.6.7, created on 2008-10-27 21:01:51
         compiled from formulariopadrao.tpl */ ?>
<?php if ($this->_tpl_vars['tituloFormulario']): ?><p align="center"><?php echo $this->_tpl_vars['tituloFormulario']; ?>
</p><?php endif; ?>
<?php if ($this->_tpl_vars['formulario']): ?>
	<center>
	<?php echo $this->_tpl_vars['formulario']; ?>

	</center>
<?php endif; ?>
<?php if ($this->_tpl_vars['formularios']): ?>
	<center>
<?php unset($this->_sections['formnum']);
$this->_sections['formnum']['name'] = 'formnum';
$this->_sections['formnum']['loop'] = is_array($_loop=$this->_tpl_vars['formularios']) ? count($_loop) : max(0, (int)$_loop); unset($_loop);
$this->_sections['formnum']['show'] = true;
$this->_sections['formnum']['max'] = $this->_sections['formnum']['loop'];
$this->_sections['formnum']['step'] = 1;
$this->_sections['formnum']['start'] = $this->_sections['formnum']['step'] > 0 ? 0 : $this->_sections['formnum']['loop']-1;
if ($this->_sections['formnum']['show']) {
    $this->_sections['formnum']['total'] = $this->_sections['formnum']['loop'];
    if ($this->_sections['formnum']['total'] == 0)
        $this->_sections['formnum']['show'] = false;
} else
    $this->_sections['formnum']['total'] = 0;
if ($this->_sections['formnum']['show']):

            for ($this->_sections['formnum']['index'] = $this->_sections['formnum']['start'], $this->_sections['formnum']['iteration'] = 1;
                 $this->_sections['formnum']['iteration'] <= $this->_sections['formnum']['total'];
                 $this->_sections['formnum']['index'] += $this->_sections['formnum']['step'], $this->_sections['formnum']['iteration']++):
$this->_sections['formnum']['rownum'] = $this->_sections['formnum']['iteration'];
$this->_sections['formnum']['index_prev'] = $this->_sections['formnum']['index'] - $this->_sections['formnum']['step'];
$this->_sections['formnum']['index_next'] = $this->_sections['formnum']['index'] + $this->_sections['formnum']['step'];
$this->_sections['formnum']['first']      = ($this->_sections['formnum']['iteration'] == 1);
$this->_sections['formnum']['last']       = ($this->_sections['formnum']['iteration'] == $this->_sections['formnum']['total']);
?>
	<?php echo $this->_tpl_vars['formularios'][$this->_sections['formnum']['index']]; ?>

<?php endfor; endif; ?>
	</center>
<?php endif; ?>
<br/><br/>