<form name='exportar_testset' id='exportar_testset' action='?acao=exportar' method='post' >
<center>

Please select one dataset and the dataset's queries to export.

{foreach item=atual key=dataset from=$datasets}
<p>
	<div><input type="radio" name="iddataset" value="{$datasets[$dataset].id}">  <b>DataSet:</b> {$dataset}</div>

	{if is_array($datasets[$dataset].perguntas)}
		<br/>
		<div><b>Queries</b></div>
		<table class="listagem" cellpadding="0" cellspacing="0">	
			<tr>
				<td> </td>
				<td>Name</td>
				<td>Status</td>
				<td>Evaluated pages</td>
				<td>Relevant pages</td>
			</tr>
				{foreach item=perguntaatual key=pergunta from=$datasets[$dataset].perguntas}
				<tr>
					<td><input type="checkbox" checked='checked' name="pergunta[{$datasets[$dataset].id}][{$perguntaatual.id}]" value="true"></td>
					<td>{$perguntaatual.query}</td>
					<td class="center">{if $perguntaatual.active == 1}Active{else}Inactive{/if}</td>
					<td>{$perguntaatual.pa}</td>
					<td>{$perguntaatual.par}</td>
				</tr>
				{/foreach}			
		</table><br/>
		

	{/if}
</p>
<hr/>
{/foreach}
<br>
		<input type="submit" name="exportar" value="Export">
</center>
</form>