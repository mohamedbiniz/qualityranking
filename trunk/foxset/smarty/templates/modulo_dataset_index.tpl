<center>
{foreach item=atual key=dataset from=$datasets}
<p>
	<div><b>Dataset:</b> {$dataset}</div>
	<div>
		{if $datasets[$dataset].status != 'F'}<a href="?acao=urls&iddataset={$datasets[$dataset].id}">Documents</a> &nbsp; - &nbsp;{/if}
		<a href="?acao=permissoes&iddataset={$datasets[$dataset].id}">Roles</a> &nbsp; - &nbsp; 
		{if $datasets[$dataset].status != 'F'}<a href="?acao=criarpergunta&iddataset={$datasets[$dataset].id}">New query</a> &nbsp; - &nbsp;
		<a href="?acao=finalizar&iddataset={$datasets[$dataset].id}">Finalize</a>{/if}
		{if $datasets[$dataset].status == 'F'}Finalized{/if}
	</div>
	{if is_array($datasets[$dataset].perguntas)}
		<br/>
		<div><b>Queries</b></div>
		<table class="listagem" cellpadding="0" cellspacing="0">	
			<tr>
				<td>Query</td>
				<td>Active</td>
				<td>Evaluated pages</td>
				<td>Relevant pages</td>
				<td> </td>
			</tr>
				{foreach item=perguntaatual key=pergunta from=$datasets[$dataset].perguntas}
				<tr>
					<td>{$perguntaatual.query}</td>
					<td class="center">{if $perguntaatual.active == 1}Yes{else}No{/if}</td>
					<td>{$perguntaatual.pa}</td>
					<td>{$perguntaatual.par}</td>
					<td>{if $datasets[$dataset].status != 'F'}
						{if $perguntaatual.active == 1}
							<a href="?acao=editarpergunta&idpergunta={$perguntaatual.id}">Edit</a>
						{/if}
					{/if}</td>
				</tr>
				{/foreach}			
		</table><br/>
	{/if}
</p>
<hr/>
{/foreach}
</center>