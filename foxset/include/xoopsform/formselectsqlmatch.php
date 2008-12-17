<?php
// $Id: formselectcountry.php,v 1.6 2003/02/14 01:24:08 buennagel Exp $
//  ------------------------------------------------------------------------ //
//                XOOPS - PHP Content Management System                      //
//                    Copyright (c) 2000 XOOPS.org                           //
//                       <http://www.xoops.org/>                             //
//  ------------------------------------------------------------------------ //
//  This program is free software; you can redistribute it and/or modify     //
//  it under the terms of the GNU General Public License as published by     //
//  the Free Software Foundation; either version 2 of the License, or        //
//  (at your option) any later version.                                      //
//                                                                           //
//  You may not change or alter any portion of this comment or credits       //
//  of supporting developers from this source code or any supporting         //
//  source code which is considered copyrighted (c) material of the          //
//  original comment or credit authors.                                      //
//                                                                           //
//  This program is distributed in the hope that it will be useful,          //
//  but WITHOUT ANY WARRANTY; without even the implied warranty of           //
//  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the            //
//  GNU General Public License for more details.                             //
//                                                                           //
//  You should have received a copy of the GNU General Public License        //
//  along with this program; if not, write to the Free Software              //
//  Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307 USA //
//  ------------------------------------------------------------------------ //
// Author: Kazumi Ono (AKA onokazu)                                          //
// URL: http://www.myweb.ne.jp/, http://www.xoops.org/, http://jp.xoops.org/ //
// Project: The XOOPS Project                                                //
// ------------------------------------------------------------------------- //
/**
 * @package     kernel
 * @subpackage  form
 * 
 * @author	    Kazumi Ono	<onokazu@xoops.org>
 * @copyright	copyright (c) 2000-2003 XOOPS.org
 */


/**
 * Parent
 */
include_once DIR_INCLUDES."xoopsform/formselect.php";
include_once DIR_INCLUDES."xoopsform/themeform.php";

/**
 * A select field with sql match types
 * 
 * @package     kernel
 * @subpackage  form
 * 
 * @author	    Kazumi Ono	<onokazu@xoops.org>
 * @copyright	copyright (c) 2000-2003 XOOPS.org
 */
class XoopsFormSelectSqlMatch extends XoopsFormSelect
{

	function XoopsFormSelectSqlMatch($caption, $name, $value="=", $size=1)
	{
		$this->XoopsFormSelect($caption, $name, $value, $size);
		$this->addOption("=");
		$this->addOption("!=");
		$this->addOption("like");
		$this->addOption(">");
		$this->addOption("<");
	}
}
class XoopsFormSelectSqlAnd extends XoopsFormSelect
{

	function XoopsFormSelectSqlAnd($caption, $name, $value="Igual", $size=1)
	{
		$this->XoopsFormSelect($caption, $name, $value, $size);
		$this->addOption("AND");
		$this->addOption("OR");
	}
}

class XoopsFormSelectSqlOrder extends XoopsFormSelect
{

	function XoopsFormSelectSqlOrder($caption, $name, $value="Igual", $size=1)
	{
		$this->XoopsFormSelect($caption, $name, $value, $size);
		$this->addOption("ASC");
		$this->addOption("DESC");
	}
}

class XoopsThemeQueryForm extends XoopsThemeForm
{

	function XoopsThemeQueryForm($title, $name, $action, $method="post",$nome_pesquisa,$campos,$count=3,$campos2="", $count2=false){
		$this->XoopsForm($title, $name, $action, $method);

		if (! $campos2) $campos2 = $campos;
		if ($count2 === false) $count2 = $count;

		$this->addElement(new XoopsFormLabel("Pesquisa:"));
		for ($i=0;$i < $count;$i++){
			$buscaRow = new XoopsFormElementTray("&nbsp;&nbsp;&nbsp&nbsp;$i -->");
			$checkbox = new XoopsFormCheckBox("","where[$i][ativa]","true");
			$checkbox->addOption("Ativar");
			if ($i == 0)$checkbox->setValue("Ativar");
			$buscaRow->addElementNotRef($checkbox);
			$campo= new XoopsFormSelect("","where[$i][campo]","url");
			foreach ($campos as $key => $value) {
				$campo->addOption($value);
			}
			$buscaRow->addElementNotRef($campo);
			$buscaRow->addElementNotRef( new XoopsFormSelectSqlMatch("","where[$i][tipo]"));
			$buscaRow->addElementNotRef(new XoopsFormText("","where[$i][pesquisa]",20,100));
			$buscaRow->addElementNotRef( new XoopsFormSelectSqlAnd("","where[$i][and]"));
			$this->addElementNotRef($buscaRow);
		}
		if ($count2 > 0){
			$this->addElement(new XoopsFormLabel("Ordenação:"));
			for ($i=0; $i < $count2;$i++){
				$ordemRow = new XoopsFormElementTray("&nbsp;&nbsp;&nbsp&nbsp;$i -->");
				$checkbox2 = new XoopsFormCheckBox("","order[$i][ativa]","true");
				$checkbox2->addOption("Ativar");
				if ($i == 0)$checkbox2->setValue("Ativar");
				$ordemRow->addElementNotRef($checkbox2);
				$campo2= new XoopsFormSelect("","order[$i][campo]","url");
				foreach ($campos2 as $key => $value) {
					$campo2->addOption($value);
				}
				$ordemRow->addElementNotRef($campo2);
				$ordemRow->addElementNotRef( new XoopsFormSelectSqlOrder("","order[$i][tipo]"));
				$this->addElementNotRef($ordemRow);
			}
		}
		$checkbox3 = new XoopsFormCheckBox("Mostrar lista detalhada","detalhes","");
		$checkbox3->addOption("Sim","Sim");
		$this->addElement($checkbox3);
		$this->addElement(new XoopsFormButton(" ","pesquisa[$nome_pesquisa]","Pesquisar","submit",true));

	}
}

?>