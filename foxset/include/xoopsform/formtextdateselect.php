<?php
// $Id: formtextdateselect.php,v 1.1 2003/03/04 10:35:30 okazu Exp $
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
 * A text field with calendar popup
 * 
 * @package     kernel
 * @subpackage  form
 * 
 * @author	    Kazumi Ono	<onokazu@xoops.org>
 * @copyright	copyright (c) 2000-2003 XOOPS.org
 */

class XoopsFormTextDateSelect extends XoopsFormText
{

	function XoopsFormTextDateSelect($caption, $name, $size = 15, $value="s")
	{
		$value = !is_numeric($value) ? time() : intval($value);
		$this->XoopsFormText($caption, $name, $size, 25, $value);
	}

	function render()
	{
		//$jstime = formatTimestamp('F j Y H:i:s', $this->getValue());
		$jstime = formatTimestamp('F j Y, H:i:s', $this->getValue());
		include_once DIR_INCLUDES.'/include/calendarjs.php';
		//return "<input type='text' name='".$this->getName()."' id='".$this->getId()."' size='".$this->getSize()."' maxlength='".$this->getMaxlength()."' value='".date("Y-m-d", $this->getValue())."'".$this->getExtra()." /><input type='reset' value=' ... ' onclick='return showCalendar(\"".$this->getName()."\");'>";
		return "<input type='text' name='".$this->getName()."' id='".$this->getId()."' size='".$this->getSize()."' maxlength='".$this->getMaxlength()."' value='".date("d/m/Y", $this->getValue())."'".$this->getExtra()."  class='inputText' /><input type='image' value=' ... ' src='".XOOPS_URL."/images/calendar.gif' onclick='return showCalendar(\"".$this->getId()."\");' alt='calendário' title='calendário' align='middle'>";
	}
}
?>