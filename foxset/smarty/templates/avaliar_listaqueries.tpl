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
	
<b>URL:</b><br>{$url}<br><br>

<div id="avlistaurl" class="lala"">
<ul>
{foreach item=query key=idpergunta from=$queries}
	<li>
		<a href="/avaliar.php?idurl={$idurl}&idpergunta={$query.id}" target="_top">
			<b>{$query.query}</b>
		</a>
	</li>
{/foreach}
</ul>	
</div>


<!-- Inicio do rodape -->
{include file="rodape.tpl"}
<!-- Fim do rodape -->