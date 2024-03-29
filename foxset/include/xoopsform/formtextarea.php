<?php
// $Id: formtextarea.php,v 1.7 2003/06/27 10:41:54 okazu Exp $
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
 * 
 * 
 * @package     kernel
 * @subpackage  form
 * 
 * @author	    Kazumi Ono	<onokazu@xoops.org>
 * @copyright	copyright (c) 2000-2003 XOOPS.org
 */
/**
 * A textarea
 * 
 * @author	Kazumi Ono	<onokazu@xoops.org>
 * @copyright	copyright (c) 2000-2003 XOOPS.org
 * 
 * @package     kernel
 * @subpackage  form
 */
class XoopsFormTextArea extends XoopsFormElement {
	/**
     * number of columns
	 * @var	int 
	 * @access  private
	 */
	var $_cols;

	/**
	 * number of rows
     * @var	int 
	 * @access  private
	 */
	var $_rows;

	/**
     * initial content
	 * @var	string  
	 * @access  private
	 */
	var $_value;

	/**
	 * Constuctor
	 * 
     * @param	string  $caption    caption
     * @param	string  $name       name
     * @param	string  $value      initial content
     * @param	int     $rows       number of rows
     * @param	int     $cols       number of columns   
	 */
	function XoopsFormTextArea($caption, $name, $value="", $rows=5, $cols=50, $description=""){
		$this->setCaption($caption);
		$this->setName($name);
		$this->_rows = intval($rows);
		$this->_cols = intval($cols);
		$this->setValue($value);
		if ($description!="") $this->setDescription($description);
	}

	/**
	 * get number of rows
	 * 
     * @return	int
	 */
	function getRows(){
		return $this->_rows;
	}

	/**
	 * Get number of columns
	 * 
     * @return	int
	 */
	function getCols(){
		return $this->_cols;
	}

	/**
	 * Get initial content
	 * 
     * @return	string
	 */
	function getValue(){
		return $this->_value;
	}

	/**
	 * Set initial content
	 * 
     * @param	$value	string
	 */
	function setValue($value){
		$this->_value = $value;
	}

	/**
	 * prepare HTML for output
	 * 
     * @return	sting HTML
	 */
	function render(){
		return "<textarea name='".$this->getName()."' id='".$this->getId()."' rows='".$this->getRows()."' cols='".$this->getCols()."'".$this->getExtra()." class='textarea'>".$this->getValue()."</textarea>";
	}
}
?>