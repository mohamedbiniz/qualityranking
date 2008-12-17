<div id="topo">
	<div id="logo">
		<a href="/index.php" ><img alt="FoxSet" src="/images/foxset-peq.jpg"/></a>
	</div>	
	{if $logado == false}
		<div id="login">
			<div class="titulobloco">
				Login
			</div>
			<form action="/indexdentro.php" method="POST" >	
			<ul class="nav">
				<li class="navList">Username:<input type="text" class="inputText" name="nome" size="20"></li>
				<li class="navList">Password:<input type="password" class="inputPassword" name="senha" size="20"></li>
				<li class="navList"><input type="submit" class="inputButtom" name="efetualogin" value="Login"></li>
			 </ul>
			 </form>
		</div>
	{else}
		<div id="login">
			<div class="titulobloco">Collaborator:</div>
			<p align="center">{$username}</p>
		</div>
	{/if}
	<center>	
	<br/>
	<span class="titulo1">FoxSet</span><br/>
	<span class="titulo2">A tool for creating datasets</span><br/><br/>
	</center>
</div>