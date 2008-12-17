{if $tituloFormulario}<p align="center">{$tituloFormulario}</p>{/if}
{if $formulario}
	<center>
	{$formulario}
	</center>
{/if}
{if $formularios}
	<center>
{section name=formnum loop=$formularios}
	{$formularios[formnum]}
{/section}
	</center>
{/if}
<br/><br/>
