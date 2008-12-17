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
	<!-- Inicio do inicio -->
	<div id="topoplugin">
		<center>	
			<span class="titulo1">FoxSet</span><br/>
			<span class="titulo2">A tool for creating datasets</span><br/><br/>
		</center>
	</div>
	<!-- Fim do inicio -->
<div id="principal">
	
	{if $titulomodulo != ""}
		<div id="titulomodulo">{$titulomodulo}</div>
	{/if}
	
	{if $mensagem != ""}
		<div id="mensagem">{$mensagem}</div>
	{/if}
	<!-- Inicio do conteudo -->
	<div id="conteudoPlugin" >
		{include file="$display_conteudo.tpl"}
 	</div>
	<div id="footer"></div>
	<!-- Fim do conteudo -->
</div>

<!-- Inicio do rodape -->
{include file="rodape.tpl"}
<!-- Fim do rodape -->