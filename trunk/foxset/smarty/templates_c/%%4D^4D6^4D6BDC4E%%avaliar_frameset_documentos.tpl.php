<?php /* Smarty version 2.6.7, created on 2008-10-28 19:04:56
         compiled from avaliar_frameset_documentos.tpl */ ?>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd"  >
<html xmlns="http://www.w3.org/1999/xhtml" xml:lang="pt_BR" lang="pt_BR" >
<head>
	<meta http-equiv="content-type" content="text/html; charset=UTF-8" />
	<meta http-equiv="content-language" content="pt_BR" />
	<meta name="robots" content="noindex,nofollow" />

	<title>FoxSet</title>
</head>
<FRAMESET cols="20%, 80%" border=1>
	<FRAME src="/avaliar.php?idurl=<?php echo $this->_tpl_vars['idurl']; ?>
&lista=1">
	<FRAMESET rows="40px, *" border=1>
		<FRAME src="/avaliar.php?tituloframe=1&idpergunta=<?php echo $this->_tpl_vars['idpergunta']; ?>
">
		<FRAME src="/geturl.php?idurl=<?php echo $this->_tpl_vars['idurl']; ?>
">
	</FRAMESET>
	<NOFRAMES>
	This page requires a frame compactible browser.
	</NOFRAMES>
</FRAMESET>
</HTML>