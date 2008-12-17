<div id="menu">
	<div class="titulobloco titulomenu">
		Menu	
	</div>
	<ul class="nav">
	{foreach item=menu_atual key=id_menu from=$menu}
		<li class="navList"><a href="{if $menu_atual.pagina }{$menu_atual.pagina}{else}/desenvolvimento.php{/if}"><img src="{$menu_atual.icone}" title="{$menu_atual.nome}" align="left" hspace="2" vspace="1">{$menu_atual.nome}</a></li>
	{/foreach}
		{if $logado == true}
			<li class="navList"><a href="/logout.php"><img src="/images/ico_usuarios.gif" title="Sair" align="left" hspace="2" vspace="1">Logout</a></li>
		{/if}
	 </ul>
</div>

