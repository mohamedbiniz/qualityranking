<b>{$perguntas[0].DataSet}</b><br>{$perguntas[0].Desc_DataSet}<br><br>

<ul>
{foreach item=pergunta_atual key=idpergunta from=$perguntas}
	<li>
		<a href="?idpergunta={$pergunta_atual.IdPergunta}">
			<b>{$pergunta_atual.Pergunta}</b>
		</a>
		<br>
		{$pergunta_atual.Desc_Pergunta}
	</li>
{/foreach}
</ul>	
