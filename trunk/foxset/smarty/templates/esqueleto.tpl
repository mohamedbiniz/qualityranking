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
	{include file="inicio.tpl"}
	<!-- Fim do inicio -->
<div id="principal">
	
	<div id="menus">
		{if $menu != ""}
		{include file="menu.tpl"}
		{/if}
		
		{if $submenu != ""}
		{include file="submenu.tpl"}
		{/if}
		
	</div>
	
	<div id="espaco"></div>
	{if $titulomodulo != ""}
		<div id="titulomodulo">{$titulomodulo}</div>
	{/if}
	
	{if $mensagem != ""}
		<div id="mensagem">{$mensagem}</div>
	{/if}
	
	<!-- Inicio do conteudo -->
	<div id="conteudo" >
		{include file="$display_conteudo.tpl"}
 	</div>
	<div id="footer"></div>
	<!-- Fim do conteudo -->
	
</div>


<!-- Inicio do rodape -->
{include file="rodape.tpl"}
<!-- Fim do rodape -->