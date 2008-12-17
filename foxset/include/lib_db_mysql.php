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
e Lu?s Paulo <allianortis@interlize.com.br>
------------------------------------------------------------------------
$Id: php_pear_headers,v 1.1 2002/04/22 09:51:27 alan_k Exp $
----------------------------------------------------------------------*/

/*????????????????????????????????????????????????????????????????????????????
Fun??es referentes a banco de dados:

Este arqivo carrega o conjunto b?sico de funções referentes ao servidos
de banco de dados MySQL.
????????????????????????????????????????????????????????????????????????????*/

if(!defined("LIB_DB_MYSQL")){
	define("LIB_DB_MYSQL", "1");
}else{
	exit;
}
class db_mysql{

	var $conexao;

	var $query;
	var $result;
	var $fetch;
	var $rows;
	var $free;

	var $db_time;
	var $last_db_time;
	var $last_result;

	/*
	Fun??o: abreConexao() 
	? Esta função abre conex?o com o MySQL Server.
	*/
	function abreConexao($server_id = 0, $DATABASE = DB_MAIN){
		global $servers;
		$time = microtime_float();
		if( $conexao = @mysql_connect($servers[$server_id]["addr"],$servers[$server_id]["user"],$servers[$server_id]["pass"])){
			if(mysql_select_db($DATABASE, $conexao)){
				$this->conexao = $conexao;
				$time2 = microtime_float();
				$this->update_query_time($time,$time2);
				return true;
			}
		}
		$time2 = microtime_float();
		$this->update_query_time($time,$time2);
		return false;
	}

	/*
	Fun??o: db_error()
	? Esta fun??o Retorna o erro do banco de dados.
	*/
	function error(){
		if (!$this->conexao) {
			return mysql_errno() .": ". mysql_error();
		}else {
			return mysql_errno($this->conexao) .": ". mysql_error($this->conexao);
		}
	}

	/*
	Fun??o: fechaConexao()
	? Esta fun??o abre conex?o com o MySQL Server.
	*/
	function fechaConexao(){
		if (!$this->conexao) return false;
		mysql_close($this->conexao);
	}

	function update_query_time($time1,$time2){
		$this->db_time += ($time2 - $time1);
		$this->last_db_time = ($time2 - $time1);
		$GLOBALS["db_time"] += ($time2 - $time1);
	}


	function query(&$query){
		if (!$this->conexao) return false;
		$this->query++;
		@$GLOBALS["db_stats"]["query"] ++;
		$time = microtime_float();
		$result = mysql_query($query,$this->conexao);
		$this->last_result = $result;
		$time2 = microtime_float();
		$this->update_query_time($time,$time2);
		return $result;
	}

	function query_unbuffered(&$query){
		if (!$this->conexao) return false;
		$this->query++;
		@$GLOBALS["db_stats"]["query"] ++;
		$time = microtime_float();
		$result = mysql_unbuffered_query($query,$this->conexao);
		$this->last_result = $result;
		$time2 = microtime_float();
		$this->update_query_time($time,$time2);
		return $result;
	}

	function fetch(&$res = ""){
		if (!$this->conexao) return false;
		$this->fetch++;
		@$GLOBALS["db_stats"]["fetch"] ++;
		$time = microtime_float();
		if ($res){
			$result = @mysql_fetch_array($res);
		}elseif($this->last_result){
			$result = @mysql_fetch_array($this->last_result);
		}else{
			$result = false;
		}
		$time2 = microtime_float();
		$this->update_query_time($time,$time2);
		return $result;
	}
	
		function fetch_assoc(&$res = ""){
		if (!$this->conexao) return false;
		$this->fetch++;
		@$GLOBALS["db_stats"]["fetch"] ++;
		$time = microtime_float();
		if ($res){
			$result = @mysql_fetch_assoc($res);
		}elseif($this->last_result){
			$result = @mysql_fetch_assoc($this->last_result);
		}else{
			$result = false;
		}
		$time2 = microtime_float();
		$this->update_query_time($time,$time2);
		return $result;
	}

	function result($param1, $param2, $res = ""){
		if (!$this->conexao) return false;
		$this->result++;
		@$GLOBALS["db_stats"]["result"] ++;
		$time = microtime_float();
		if ($res){
			$result = @mysql_result($res,$param1,$param2);
		}elseif($this->last_result){
			$result = @mysql_result($this->last_result,$param1,$param2);
		}else{
			$result = false;
		}
		$time2 = microtime_float();
		$this->update_query_time($time,$time2);
		return $result;
	}

	function rows(&$res = ""){
		if (!$this->conexao) return false;
		$this->rows++;
		@$GLOBALS["db_stats"]["rows"] ++;
		$time = microtime_float();
		if ($res){
			$result = @mysql_num_rows($res);
		}elseif($this->last_result){
			$result = @mysql_num_rows($this->last_result);
		}else{
			$result = false;
		}
		$time2 = microtime_float();
		$this->update_query_time($time,$time2);
		return $result;
	}
	
		function insert_id(&$res = ""){
		if (!$this->conexao) return false;
		return mysql_insert_id($this->conexao);
	}

	function free($res = ""){
		if (!$this->conexao) return false;
		if (is_resource($res)){
			$result = @mysql_free_result($res);
		}elseif(is_resource($this->last_result)){
			$result = @mysql_free_result($this->last_result);
		}else{
			$result = false;
		}
		return $result;
	}
}
?>