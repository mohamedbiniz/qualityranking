<center>
<table cellpadding="0" cellspacing="0" class="listagem">	
	<tr class="titulo">
		<td>Username</td>
		<td>Name</td>
		<td>E-mail</td>
		<td>Administrator</td>
		<td>Coordinator</td>
		<td> </td>
	</tr>
	{foreach item=atual key=idusuario from=$usuarios}
	<tr>
		<td>{$atual.username}</td>
		<td>{$atual.name}</td>
		<td>{$atual.email}</td>
		<td class="center">{if $atual.administrator}Yes{else}No{/if}</td>
		<td class="center">{if $atual.coordinator}Yes{else}No{/if}</td>
		<td><a href="?acao=editar&idusuario={$atual.id}">Edit</a></td>
	</tr>
	{/foreach}
</table>
</center>