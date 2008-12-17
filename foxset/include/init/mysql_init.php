<?php
if(!@extension_loaded('mysql')){
    if(!@dl('mysql.so')){
	exit ('Erro ao carregar extensao["mysql"] Impossivel continuar');
    }
}
?>