<?php /* Smarty version 2.6.7, created on 2008-10-04 02:37:49
         compiled from avaliar_frameset.tpl */ ?>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd"  >
<html xmlns="http://www.w3.org/1999/xhtml" xml:lang="pt_BR" lang="pt_BR" >
<head>
	<meta http-equiv="content-type" content="text/html; charset=UTF-8" />
	<meta http-equiv="content-language" content="pt_BR" />
	<meta name="robots" content="noindex,nofollow" />

	<title>FoxSet</title>
</head>
<FRAMESET cols="20%, 80%" border=1>
	<FRAME src="/avaliar.php?idpergunta=<?php echo $this->_tpl_vars['idpergunta']; ?>
&lista=1">
	<FRAMESET rows="40px, *" border=1>
	<FRAME src="/avaliar.php?tituloframe=1&idurl=<?php echo $this->_tpl_vars['idurl']; ?>
">
		<?php if ($this->_tpl_vars['idurl']): ?>
			<FRAME src="/geturl.php?idurl=<?php echo $this->_tpl_vars['idurl']; ?>
">
		<?php else: ?>
			<FRAME src="/avaliar.php?pa=1">
		<?php endif; ?>
	</FRAMESET>
	<NOFRAMES>
	This page requires a frame compactible browser.
	</NOFRAMES>
</FRAMESET>
</HTML>