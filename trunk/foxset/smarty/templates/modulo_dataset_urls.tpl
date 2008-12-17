<center>
<p><strong>Dataset:</strong> {$datasetContext}</p>
<b>Documents</b><br/>
{if $datasetMethod != 'M'}
<p>
<form action="?acao=urls&tipo=score&iddataset={$iddataset}" method="post">
Activate documents with score equal to or greater than:
<img src="/images/sliderHelp.png" />
<input type="text" class="inputText fd_slider_cn_halfSize fd_range_0d00_1d00 fd_inc_0d01 fd_tween" name="score" id="score" size="5" maxlength="5" value="{$score}" />
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
{foreach item=atual key=doc from=$documents}
<tr>
<td><a href="/geturl.php?idurl={$documents[$doc].id}" target="_blank">{$documents[$doc].url}</a></td>
<td>{$documents[$doc].score}</td>
</tr>
{/foreach}
</tbody>
</table>
</p>
{/if}

<p>
<form name='ativacao_utl_dataset' id='ativacao_utl_dataset' action='?acao=salvarstatusurl&iddataset={$iddataset}' method='post' onsubmit='return ativacao_utl_dataset();'>
<table>
<tr>
	<td align="center">Inactive documents:<br/>{$lstUrlsInativas}<br /><input type="button" value="Open selected" onclick="var id = document.getElementById('iurls').value; window.open('/geturl.php?idurl=' + id, 'docWindow' + id);" /></td>
	<td style="padding: 0 30px 0 30px">{$adicionarurl}<br>{$removerurl}</td>
	<td align="center">Active documents:<br/>{$lstUrlsAtivas}<br /><input type="button" value="Open selected" onclick="var id = document.getElementById('aurls').value; window.open('/geturl.php?idurl=' + id, 'docWindow' + id);" /></</td>
	</tr>
</table>
</form>
</p>
</center>