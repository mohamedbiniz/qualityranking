<?php
/**
 * Smarty plugin
 * @package Smarty
 * @subpackage plugins
 */


function smarty_modifier_count_any($string, $sep)
{
    // split text by ' ',\r,\n,\f,\t
    $words = explode($sep,$string);
    // count matches that contain alphanumerics
    return count($words);
}

/* vim: set expandtab: */

?>
