<form name='inclusao_permissao_dataset' id='inclusao_permissao_dataset' action='?acao=salvarpermissao&iddataset={$iddataset}' method='post' onsubmit='return xoopsFormValidate_inclusao_permissao_dataset();'>

<center>
<p><strong>Dataset:</strong> {$datasetContext}</p>
<b>Roles</b><br/>

<table>
<tr>
	<td rowspan="3">Collaborators:<br/>{$lstUsuarios}</td>
	<td style="padding: 0 30px 0 30px">{$adicionarCoordenador}<br>{$removerCoordenador}</td>
	<td>Coordinators:<br/>{$lstCoordenadores}</td>
</tr>

<tr>
	<td align="center">{$adicionarUtilizador}<br>{$removerUtilizador}</td>
	<td>Users:<br/>{$lstUtilizadores}</td>
</tr>

<tr>
	<td align="center">{$adicionarAvaliador}<br>{$removerAvaliador}</td>
	<td>Evaluators:<br/>{$lstAvaliadores}</td>
</tr>
</table>

</center>
</form>