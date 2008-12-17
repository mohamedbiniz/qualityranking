<?php /* Smarty version 2.6.7, created on 2008-10-28 12:53:40
         compiled from modulo_dataset_urls.tpl */ ?>
<center>
<p><strong>Dataset:</strong> <?php echo $this->_tpl_vars['datasetContext']; ?>
</p>
<b>Documents</b><br/>
<?php if ($this->_tpl_vars['datasetCrawler'] == 1): ?>
<p>
<form action="?acao=urls&tipo=score&iddataset=<?php echo $this->_tpl_vars['iddataset']; ?>
" method="post">
Activate documents with score equal to or greater than:
<img src="/images/sliderHelp.png" />
<input type="text" class="inputText fd_slider_cn_halfSize fd_range_0d00_1d00 fd_inc_0d01 fd_tween" name="score" id="score" size="5" maxlength="5" value="<?php echo $this->_tpl_vars['score']; ?>
" />
<input type="submit" value="Activate"/>
</form>
</p>

<p>
<table border="1">
<tr>
<thead>
<td>URL</td>
<td>Score</td>
</tr>
</thead>
<tbody>
<?php if (count($_from = (array)$this->_tpl_vars['documents'])):
    foreach ($_from as $this->_tpl_vars['doc'] => $this->_tpl_vars['atual']):
?>
<tr>
<td><a href="/geturl.php?idurl=<?php echo $this->_tpl_vars['documents'][$this->_tpl_vars['doc']]['id']; ?>
" target="_blank"><?php echo $this->_tpl_vars['documents'][$this->_tpl_vars['doc']]['url']; ?>
</a></td>
<td><?php echo $this->_tpl_vars['documents'][$this->_tpl_vars['doc']]['score']; ?>
</td>
</tr>
<?php endforeach; endif; unset($_from); ?>
</tbody>
</table>
</p>
<?php endif; ?>

<p>
<form name='ativacao_utl_dataset' id='ativacao_utl_dataset' action='?acao=salvarstatusurl&iddataset=<?php echo $this->_tpl_vars['iddataset']; ?>
' method='post' onsubmit='return ativacao_utl_dataset();'>
<table>
<tr>
	<td align="center">Inactive documents:<br/><?php echo $this->_tpl_vars['lstUrlsInativas']; ?>
<br /><input type="button" value="Open selected" onclick="var id = document.getElementById('iurls').value; window.open('/geturl.php?idurl=' + id, 'docWindow' + id);" /></td>
	<td style="padding: 0 30px 0 30px"><?php echo $this->_tpl_vars['adicionarurl']; ?>
<br><?php echo $this->_tpl_vars['removerurl']; ?>
</td>
	<td align="center">Active documents:<br/><?php echo $this->_tpl_vars['lstUrlsAtivas']; ?>
<br /><input type="button" value="Open selected" onclick="var id = document.getElementById('aurls').value; window.open('/geturl.php?idurl=' + id, 'docWindow' + id);" /></</td>
	</tr>
</table>
</form>
</p>
</center>