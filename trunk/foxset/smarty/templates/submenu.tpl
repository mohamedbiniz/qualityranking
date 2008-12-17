<br/>
<div id="menu">
	<div class="titulobloco titulomenu">
		{$submenu_titulo}
	</div>
	<ul class="nav">
	{foreach item=menu_atual key=id_menu from=$submenu}
		<li class="navList"><a href="{$menu_atual.pagina}"><img src="{$menu_atual.icone}" title="{$menu_atual.nome}" align="left" hspace="2" vspace="1">{$menu_atual.nome}</a></li>
	{/foreach}
	 </ul>
</div>

