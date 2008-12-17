
<center>
{include file="formulariopadrao.tpl"}
</center>
<center>
<b>Permissões Atuais</b>
<br/>
<table cellpadding="0" cellspacing="0" class="listagem">	
	<tr class="titulo">
		<td>Login</td>
		<td>Nome</td>
		<td>Permissão</td>
		<td> </td>
	</tr>
	{foreach item=atual key=idpermissao from=$permissoes}
	<tr>
		<td>{$atual.Login}</td>
		<td>{$atual.Nome}</td>
		<td>{$atual.Permissao}</td>
		<td><a href="?acao=excluirpermissao&idusuario={$atual.IdUsuario}&permissao={$atual.Permissao}&iddataset={$iddataset}&excluirpermissao=1">Excluir</a></td>
	</tr>
	{/foreach}
</table>
</center>
