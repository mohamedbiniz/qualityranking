{include file="cabecalho.tpl"}
<!-- Fim do cabecalho -->
{if $display_form_style != ""}
<!-- Inicio do Css do Formulario -->
<style type="text/css">
	<!--
		{$display_form_style}
	//-->
</style>
<!-- Fim do Css do Formulario -->
{/if}

<div>
	<center>	
		<span class="titulo1">FoxSet</span><br/>
	</center>
</div>
	
<b>{$Nome}</b><br>{$Descricao}<br><br>

<div id="avlistaurl" class="lala"">
<ul>
{foreach item=url_atual key=idurl from=$urls}
	<li>
		<a href="/avaliar.php?idurl={$url_atual.id}&idpergunta={$idpergunta}" target="_top">
			<b>{$url_atual.url}</b>
		</a>
	</li>
{/foreach}
</ul>	
</div>


<!-- Inicio do rodape -->
{include file="rodape.tpl"}
<!-- Fim do rodape -->