<?php


/**
 * Simple function to replicate PHP 5 behaviour
 */
function microtime_float()
{
   list($usec, $sec) = explode(" ", microtime());
   return ((float)$usec + (float)$sec);
} 






/*
Fun??o: corrigePaginacao($numero_total_de_itens)
Esta fun??o inicializa, ou acerta, a vari?vel de pagina??o ($pagina??o). ? necess?rio
informar o n?mero total de ?tens a apresentar.
Rode esta fun??o sempre que for montar um lista paginada com a fun??o calculaPaginacao()
*/

function corrigePaginacao($numero_total_de_itens){
	global $paginacao;

	// Determinando o n?mero total de p?guinas:
	$numero_total_de_paginas = (integer)($numero_total_de_itens / $paginacao["itens_por_pagina"]);
	if(($numero_total_de_itens % $paginacao["itens_por_pagina"]) != 0) $numero_total_de_paginas++;

	// Corrigindo posi??o da p?gina atual:
	if($paginacao['pagina_atual'] < 1) $paginacao['pagina_atual'] = 1;
	if($paginacao['pagina_atual'] > $numero_total_de_paginas) $paginacao['pagina_atual'] = $numero_total_de_paginas;

	// Determinando posi??o final:
	$paginacao['inicio'] = (($paginacao["itens_por_pagina"] * $paginacao['pagina_atual']) - $paginacao["itens_por_pagina"]);
	if(($sessao_modulo['pagina_atual'] * $paginacao["itens_por_pagina"]) > $numero_total_de_itens){
		$paginacao['fim_absoluto'] = $numero_total_de_itens;
		$paginacao['fim_relativo'] = $paginacao["itens_por_pagina"] - (($paginacao['pagina_atual'] * $paginacao["itens_por_pagina"]) - $numero_total_de_itens);
	}else{
		$paginacao['fim_absoluto'] = $paginacao['inicio'] + $paginacao["itens_por_pagina"];
		$paginacao['fim_relativo'] = $paginacao["itens_por_pagina"];
	}
}


/*
Fun??o: calculaPaginacao($numero_total_de_itens)
Esta fun??o retorna uma array com a pagina??o, suas respectivas p?ginas e t?tulos. Ela
precisa que os valores da variavel $pagina??o tenham sido inicializados anteriormente
pela fun??o corrigePaginacao(). O par?metro "$numero_total_de_itens" deve ser passado
indicando o n?mero total de ?tens a serem apresentados.
*/
function calculaPaginacao($numero_total_de_itens){
	global $paginacao;

	// Calculando a pagina??o:
	//   Determinando o n?mero total de p?ginas:
	$numero_total_de_paginas = intval($numero_total_de_itens / $paginacao['itens_por_pagina']);
	if($numero_total_de_itens > ($paginacao['itens_por_pagina'] * $numero_total_de_paginas)) $numero_total_de_paginas++;
	//   Determinando quantas p?ginas apresentar:
	$paginas_a_apresentar_a_direita = intval($paginacao['paginas_a_apresentar'] / 2);
	$paginas_a_apresentar_a_esquerda = intval($paginacao['paginas_a_apresentar'] / 2);
	if(($paginacao['paginas_a_apresentar'] % 2)!=0){
		$paginas_a_apresentar_a_esquerda++;
	}else{
		$paginas_a_apresentar_a_direita--;
	}
	//   Determinando o intervalo de p?ginas a apresentar:
	$numero_de_pagina_minimo = $paginacao['pagina_atual'] - $paginas_a_apresentar_a_esquerda;
	if($numero_de_pagina_minimo < 1) $numero_de_pagina_minimo = 1;
	$numero_de_pagina_maximo = $paginacao['pagina_atual'] + $paginas_a_apresentar_a_direita;
	if($numero_de_pagina_maximo > $numero_total_de_paginas) $numero_de_pagina_maximo = $numero_total_de_paginas;

	// Iniciando cria??o da array que conter? a pagina??o:
	//   Inicializando contador:
	$j = 0;
	//   Definindo a volta para o "grupo" anterior:
	if($paginacao['pagina_atual'] > 1){
		$form_paginacao[$j]['key'] = 1;
		$form_paginacao[$j]['value'] = '<span style="font-size:6px">&lt;</span>';
		$form_paginacao[$j]['title'] = 'Primeira p?gina';
	}else{
		$form_paginacao[$j]['key'] = 0;
		$form_paginacao[$j]['value'] = '<span style="font-size:6px">&lt;</span>';
		$form_paginacao[$j]['title'] = '';
	}
	$j++;
	if(($paginacao['pagina_atual'] - $paginas_a_apresentar_a_esquerda) > 1){
		$form_paginacao[$j]['key'] = urlencode($paginacao['pagina_atual']-$paginacao['paginas_a_apresentar']);
		$form_paginacao[$j]['value'] = '<span style="font-size:6px">&lt;&lt;</span>';
		$form_paginacao[$j]['title'] = 'Grupo de p?ginas anterior';
	}else{
		$form_paginacao[$j]['key'] = 0;
		$form_paginacao[$j]['value'] = '<span style="font-size:6px">&lt;&lt;</span>';
		$form_paginacao[$j]['title'] = '';
	}
	//   Definindo as p?ginas a apresentar:
	$j++;
	for($i=$numero_de_pagina_minimo; $i<=$numero_de_pagina_maximo; $i++){
		$form_paginacao[$j]['key'] = "$i";
		$form_paginacao[$j]['value'] = "$i";
		if($i==$paginacao['pagina_atual']){
			$form_paginacao[$j]['title'] = 'atual';
		}else{
			$form_paginacao[$j]['title'] = "P?gina $i";
		}
		$j++;
	}
	//   Definindo o avan?o para o pr?ximo "grupo":
	if(($paginacao['pagina_atual'] + $paginas_a_apresentar_a_direita) < $numero_total_de_paginas){
		$form_paginacao[$j]['key'] = urlencode($paginacao['pagina_atual']+$paginacao['paginas_a_apresentar']);
		$form_paginacao[$j]['value'] = '<span style="font-size:6px">&gt;&gt;</span>';
		$form_paginacao[$j]['title'] = 'Pr?ximo grupo de p?ginas';
	}else{
		$form_paginacao[$j]['key'] = 0;
		$form_paginacao[$j]['value'] = '<span style="font-size:6px">&gt;&gt;</span>';
		$form_paginacao[$j]['title'] = '';
	}
	$j++;
	if($paginacao['pagina_atual']!=$numero_total_de_paginas){
		$form_paginacao[$j]['key'] = urlencode($numero_total_de_paginas);
		$form_paginacao[$j]['value'] = '<span style="font-size:6px">&gt;</span>';
		$form_paginacao[$j]['title'] = '?ltima p?gina';
	}else{
		$form_paginacao[$j]['key'] = 0;
		$form_paginacao[$j]['value'] = '<span style="font-size:6px">&gt;</span>';
		$form_paginacao[$j]['title'] = '';
	}

	// Retornando array com a pagina??o:
	return $form_paginacao;
}

function print_status($atual,$total){
	printf("
	<script>
	progresso(%s,%s);
	</script>
	",$atual,$total);
	flush();
}
?>