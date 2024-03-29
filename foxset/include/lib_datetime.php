<?php
/*----------------------------------------------------------------------
	PHP Version 4
	Copyright (c) 1997-2002 The PHP Group
------------------------------------------------------------------------
	This source file is subject to version 2.02 of the PHP license,
	that is bundled with this package in the file LICENSE, and is
	available at through the world-wide-web at
	http://www.php.net/license/2_02.txt.
	If you did not receive a copy of the PHP license and are unable to
	obtain it through the world-wide-web, please send a note to
	license@php.net so we can mail you a copy immediately.
------------------------------------------------------------------------
	Authors:	Paulo Cunha <pcunha@interlize.com.br>
				e Lu疄 Paulo <allianortis@interlize.com.br>
------------------------------------------------------------------------
	$Id: php_pear_headers,v 1.1 2002/04/22 09:51:27 alan_k Exp $
----------------------------------------------------------------------*/

/*中中中中中中中中中中中中中中中中中中中中中中中中中中中中中中中中中中中中中中
	� Dentro deste arquivo est緌 todas as fun踥es referentes a manipula誽o de
	data e hora.
中中中中中中中中中中中中中中中中中中中中中中中中中中中中中中中中中中中中中中*/

if(!defined("LIB_DATETIME")){
	define("LIB_DATETIME", "1");
}else{
	exit;
}

/*
	Fun誽o: incrementa_mes()
	� Uma vez entrado o dia o m瘰 e o ano, separadamente, esta fun誽o retorna
	o dia o m瘰 e o ano, separadamente,isso � obvio , so que com o mes incrementado pelo 1 parametro ...
*/
function incrementa_mes($qtd,$dia,$mes,$ano){
	for($i = 0; $i < $qtd; $i++){
		if($mes!="12"){
			$mes++;
		}else{
			$mes =  "1";
			$ano++;
		}
	}
	$retorno["dia"] = $dia;
	$retorno["mes"] = $mes;
	$retorno["ano"] = $ano;
	return $retorno;
}


/*
	Fun誽o: firstDay()
	� Retorna o primeiro dia de um m瘰.
*/
function firstDay($date){
	return date("Y-m-d", mktime(0, 0, 0, substr($date,5,2), 1, substr($date,0,4)));
}


/*
	Fun誽o: timestamp()
	� Uma vez entrado o dia o m瘰 e o ano, separadamente, esta fun誽o retorna
	esta data no formato timestamp.
*/
function timestamp($dia, $mes, $ano){
	$unixTime =  mktime(0,0,0,$mes,$dia,$ano);
	$timestamp = date("Ymd", $unixTime);
	return (int) $timestamp;
}


/*
	Fun誽o: dateexplode()
	� Uma vez entrado o dia o m瘰, o ano, no formato "aaaa-mm-dd hh:mm:ss", esta fun誽o retorna
	esta data em uma array com os campos "dia", "mes" e "ano".
*/
function dateexplode($date){
	$data["dia"] = substr($date,8,2);
	$data["mes"] = substr($date,5,2);
	$data["ano"] = substr($date,0,4);
	return $data;
}


/*
	Fun誽o: dateinplode()
	� Uma vez entrada a data em uma array com os campos "dia", "mes" e "ano" esta fun誽o retorna
	o dia o m瘰, o ano, no formato "aaaa-mm-dd hh:mm:ss", sendo a hora 00:00:00.
*/
function dateinplode($date){
	$unixTime =  mktime(0,0,0,$date["mes"],$date["dia"],$date["ano"]);
	$timestamp = date("Y-m-d H:i:s", $unixTime);
	return (string) $timestamp;
}


/*
	Fun誽o: date2data()
	� Formata uma data no formato "aaaa-mm-dd" em um formato "dd/mm/aaaa".
*/
function date2data($date){
	$data = substr($date,8,2) ."/". substr($date,5,2) ."/". substr($date,0,4);
	return $data;
}


/*
	Fun誽o: date2dataDot()
	� Formata uma data no formato "aaaa-mm-dd" em um formato "dd.mm.aaaa".
*/
function date2dataDot($date){
	$data = substr($date,8,2) .".". substr($date,5,2) .".". substr($date,0,4);
	return $data;
}


/*
	Fun誽o: date2dataMini()
	� Formata uma data no formato "aaaa-mm-dd" em um formato "dd/mm/aa".
*/
function date2dataMini($date){
	$data = substr($date,8,2) ."/". substr($date,5,2) ."/". substr($date,2,2);
	return $data;
}


/*
	Fun誽o: data2date()
	� Formata uma data no formato "dd/mm/aaaa" em um formato "aaaa-mm-dd".
*/
function data2date($date){
	$data = substr($date,6,2) ."-". substr($date,5,2) ."-". substr($date,0,4);
	return $data;
}


/*
	Fun誽o: dateformat()
	� Formata uma data em um formato espec璗ico.
*/
function dateformat($dia, $mes, $ano, $formato){
	$unixTime =  mktime(0,0,0,$mes,$dia,$ano);
	$timestamp = date($formato, $unixTime);
	return (string) $timestamp;
}


/*
	Fun誽o: data()
	� Esta fun誽o a datat atual da forma: "'dia, num廨ico' de 'm瘰' de 'ano'"
*/
function data(){
	$str_mes_red[1] = "Jan";
	$str_mes_red[2] = "Fev";
	$str_mes_red[3] = "Mar";
	$str_mes_red[4] = "Abr";
	$str_mes_red[5] = "Mai";
	$str_mes_red[6] = "Jun";
	$str_mes_red[7] = "Jul";
	$str_mes_red[8] = "Ago";
	$str_mes_red[9] = "Set";
	$str_mes_red[10] = "Out";
	$str_mes_red[11] = "Nov";
	$str_mes_red[12] = "Dez";
	$mes = $str_mes_red[date("n", time())];
	$ano = date("Y", time());
	$dia = date("j", time());

	return "$dia de $mes de $ano";
}


/*
	Fun誽o: dataMini()
	� Esta fun誽o a datat atual da forma: "dd/mm/yyyy"
*/
function dataMini(){
	return date("j/m/Y", time());
}


/*
	Fun誽o: dataExtensa()
	� Esta fun誽o a datat atual da forma:
	"'dia da semana', 'dia, num廨ico' de 'm瘰' de 'ano'"
*/
function dataExtensa(){
	$str_mes[1] = "janeiro";
	$str_mes[2] = "fevereiro";
	$str_mes[3] = "mar&ccedil;o";//Mar蔞
	$str_mes[4] = "abril";
	$str_mes[5] = "maio";
	$str_mes[6] = "junho";
	$str_mes[7] = "julho";
	$str_mes[8] = "agosto";
	$str_mes[9] = "setembro";
	$str_mes[10] = "outubro";
	$str_mes[11] = "novembro";
	$str_mes[12] = "dezembro";
	$mes = $str_mes[date("n", time())];
	$str_dia[0] = "domingo";
	$str_dia[1] = "segunda-feira";
	$str_dia[2] = "ter&ccedil;a-feira";
	$str_dia[3] = "quarta-feira";
	$str_dia[4] = "quinta-feira";
	$str_dia[5] = "sexta-feira";
	$str_dia[6] = "s&aacute;bado";
	$dia = $str_dia[date("w", time())];
	$dianum = date("j", time());
	$ano = date("Y", time());
	return "$dia, $dianum de $mes de $ano";
}


/*
	Fun誽o: dataehoraExtensa()
	� Esta fun誽o a datat atual da forma:
	"'dia da semana', 'dia, num廨ico' de 'm瘰' de 'ano' 跴 'hh':'mm':'ss'"
*/
function dataehoraExtensa($data){
	$unixTime =  mktime(substr($data, 11, 2), substr($data, 14, 2), substr($data, 17, 2), substr($data, 5, 2), substr($data, 8, 2), substr($data, 0, 4));

	$str_mes[1] = "janeiro";
	$str_mes[2] = "fevereiro";
	$str_mes[3] = "mar&ccedil;o";//Mar蔞
	$str_mes[4] = "abril";
	$str_mes[5] = "maio";
	$str_mes[6] = "junho";
	$str_mes[7] = "julho";
	$str_mes[8] = "agosto";
	$str_mes[9] = "setembro";
	$str_mes[10] = "outubro";
	$str_mes[11] = "novembro";
	$str_mes[12] = "dezembro";
	$mes = $str_mes[(int)substr($data, 5, 2)];
	$str_dia[0] = "domingo";
	$str_dia[1] = "segunda-feira";
	$str_dia[2] = "ter&ccedil;a-feira";
	$str_dia[3] = "quarta-feira";
	$str_dia[4] = "quinta-feira";
	$str_dia[5] = "sexta-feira";
	$str_dia[6] = "s&aacute;bado";
	$dia = $str_dia[date("w", $unixTime)];
	$dianum = date("j", $unixTime);
	$ano = substr($data, 0, 4);
	return "$dia, $dianum de $mes de $ano 跴 ". substr($data, 11, 8);
}


/*
	Fun誽o: tempoGasto()
	� Esta fun誽o retorna o tempo garto desde o momento "$start"
*/
function tempoGasto($start){
	$end = microtime();
	list($start2, $start1) = explode(" ", $start);
	list($end2, $end1) = explode(" ", $end);
	$diff1 = $end1 - $start1;
	$diff2 = $end2 - $start2;
	if($diff2 < 0){
		$diff1 -= 1;
		$diff2 += 1.0;
	}
	return $diff2 + $diff1;
}

?>
