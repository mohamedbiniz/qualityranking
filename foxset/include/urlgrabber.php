<?php

class FoxGrabber{

	var $body_content;
	var $original_body_content;
	var $dlInfo;
	var $idurl;
	var $contentType;
	var $charset;
	var $size;
	var $script_url;
	var $http_host;
	var $headers;
	var $redirect_count;

	var $url;
	var $graburl;
	var $url_segments;
	var $base;
	var $flags = array();

	function FoxGrabber($url,$idurl){
		$this->url = $url;
		$this->idurl = $idurl;
		$this->seturl($url);

		$this->http_host  = isset($_SERVER['HTTP_HOST']) ? $_SERVER['HTTP_HOST'] : (isset($_SERVER['SERVER_NAME']) ? $_SERVER['SERVER_NAME'] : 'localhost');

		$this->script_url = 'http'
		. (isset($_SERVER['HTTPS']) && $_SERVER['HTTPS'] == 'on' ? 's' : '')
		. '://'
		. $this->http_host
		. $_SERVER['PHP_SELF'];
	}

	function encode_url($url)
	{
		return rawurlencode(base64_encode($url));
	}
	function decode_url($url)
	{
		return str_replace('&', '&', base64_decode(rawurldecode($url)));
	}

	/*
	function encode_url($url)
	{
	return rawurlencode($url);
	}
	function decode_url($url)
	{
	return str_replace('&', '&', rawurldecode($url));
	}
	*/
	function seturl($url){
		//decode

		//$this->url = decode_url($url);
		$this->url = $url;
		if (strpos($this->url,	'://') === false)
		{
			$this->url	= 'http://' . $this->url;
		}

		$this->graburl = $this->url;
		if ($this->parse_url($this->url, $this->url_segments))
		{
			$this->base = $this->url_segments;
		}else{
			return false;
		}
		return true;
	}


	function GrabAndShow(){
		$forcedl = false;
		if (!$this->restore() or $forcedl){
			$this->grab();
			$this->process();
			$this->save();
			//	    echo "baixou";
		}

		$this->show();
	}

	function show(){
		header('Content-type: ' . $this->contentType);
		echo ($this->body_content);
	}


	function process(){

		$contenttype = ereg_replace(";.*","",$this->contentType);
		//	if (!$this->isFirstUrl)return true;
		if (!in_array($contenttype, array('text/html', 'application/xml+xhtml', 'application/xhtml+xml', 'text/css'))) return true;


		if ($contenttype == 'text/css')
		{
			$this->body_content = $this->proxify_css($this->body_content);
		}

		else
		{
			$this->remove_scripts();
			$this->modify_urls();
		}

	}

	function restore(){

		$sql = sprintf(
		"select document_id, content_type, size, data from document_data where document_id = %s and url = '%s';",
		addslashes($this->idurl),
		addslashes($this->url));

		global $db;

		//die($sql);

		if (!$db->query($sql)){
			echo ($db->error());
			return false;
		}else{
			if ($db->rows()){
				$urlRecord = $db->fetch();
				$this->body_content = $urlRecord['data'];
				$this->contentType = $urlRecord['content_type'];
				$this->size = $urlRecord['size'];
				//getinfo
				return true;
			}else{
				return false;
			}
		}
	}

	function save(){
		global $db;
		$sql = sprintf(
		"delete from document_data where document_id = %s and url = '%s';",
		addslashes($this->idurl),
		addslashes($this->url)
		);
		if (!$db->query($sql)){
			echo ($db->error());
			return false;
		}

		$sql = sprintf(
		"Insert into document_data (document_id, url, content_type, size, data, original_data) values (%s,'%s','%s',%s,'%s','%s');",
		addslashes($this->idurl),
		addslashes($this->url),
		addslashes($this->contentType),
		$this->dlInfo['size_download'],
		addslashes($this->body_content),
		addslashes($this->original_body_content)
		);


		//die($sql);

		if (!$db->query($sql)){
			echo ($db->error());
			return false;
		}else{
			return true;
		}


	}


	function Grab(){

		// create a new curl resource
		$ch = curl_init();

		// set URL and other appropriate options
		curl_setopt($ch, CURLOPT_URL, $this->graburl);
		curl_setopt($ch, CURLOPT_RETURNTRANSFER, true);
		curl_setopt($ch, CURLOPT_HEADER, true);
		//curl_setopt($ch, CURLOPT_FOLLOWLOCATION, true);
		//    curl_setopt($ch, CURLOPT_USERAGENT, "Mozilla/5.0 (Windows; U; Windows NT 5.1; pt-BR; rv:1.8.1) FoxSet/2006");



		// grab URL, and return output

		$retorno = curl_exec($ch);

		$fimheaders = strpos($retorno,"\r\n\r\n");

		$this->headers = substr($retorno,0,$fimheaders);
		$this->body_content = $this->original_body_content =  substr($retorno,$fimheaders+4);

		$this->dlInfo = curl_getinfo($ch);
		$this->contentType = $this->dlInfo['content_type'];


		if ($this->follow_location())
		{
			if ($this->redirect_count > 10){
				die("too many redirects")	;
			}
			curl_close($ch);
			//echo $this->graburl;
			$this->Grab();
			return;
		}else{		// close curl resource, and free up system resources
			curl_close($ch);
		}


	}

	function follow_location()
	{
		if (preg_match("#(location|uri|content-location):([^\r\n]*)#i", $this->headers, $matches))
		{
			if (($url = trim($matches[2])) == '')
			{
				return false;
			}

			$this->graburl = $this->proxify_url($url, false);
			$this->redirect_count++;
			return true;
		}
		return false;
	}

	static function parse_url($url, & $container)
	{
		$temp = @parse_url($url);

		if (!empty($temp))
		{
			$temp['port']     = isset($temp['port']) ? $temp['port'] : 80;
			$temp['path']     = isset($temp['path']) ? $temp['path'] : '/';
			$temp['file']     = substr($temp['path'], strrpos($temp['path'], '/')+1);
			$temp['dir']      = substr($temp['path'], 0, strrpos($temp['path'], '/'));
			$temp['base']     = $temp['scheme'] . '://' . $temp['host'] . ($temp['port'] != 80 ?  ':' . $temp['port'] : '') . $temp['dir'];
			$temp['prev_dir'] = $temp['path'] != '/' ? substr($temp['base'], 0, strrpos($temp['base'], '/')+1) : $temp['base'] . '/';
			$container = $temp;

			return true;

			/*
			URL: http://username:password@www.example.com:80/dir/dir/page.php?foo=bar&foo2=bar2#bookmark
			scheme   // http
			host     // www.example.com
			port     // 80
			user     // username
			pass     // password
			path     // /dir/dir/page.php
			query    // foo=bar&foo2=bar2
			fragment // bookmark
			file     // page.php
			dir      // /dir/dir
			base     // http://www.example.com/dir/dir
			prev_dir // http://www.example.com/dir/
			*/
		}

		return false;
	}


	function proxify_url($url, $proxify = true)
	{
		if ($url == '') return $url;
		$url = trim($url);
		$fragment = ($hash_pos = strpos($url, '#') !== false) ? '#' . substr($url, $hash_pos) : '';
		if (!preg_match('#^[a-zA-Z]+://#', $url))
		{
			switch ($url{0})
			{
				case '/':
					$url = $this->base['scheme'] . '://' . $this->base['host'] . ($this->base['port'] != 80 ? ':' . $this->base['port'] : '') . $url;
					break;
				case '?':
					$url = $this->base['base'] . '/' . $this->base['file'] . $url;
					break;
				case '#':
					$proxify = false;
					break;
				case 'm':
					if (substr($url, 0, 7) == 'mailto:')
					{
						$proxify = false;
						break;
					}
				default:
					$url = $this->base['base'] . '/' . $url;
			}
		}

		//return $proxify ? "{$this->script_url}?idurl=".$this->idurl."&url=" . $this->encode_url($url) . $fragment : $url;
		return $proxify ? "{$this->script_url}?idurl=".$this->idurl."&furl=" . $this->encode_url($url) . $fragment : $url;

		}


		function proxify_css($css)
		{
			preg_match_all('#url\s*\(\s*(([^)]*(\\\))*[^)]*)(\)|$)?#i', $css, $matches, PREG_SET_ORDER);

			for ($i = 0, $count = count($matches); $i < $count; $i++)
			{
				$css = str_replace($matches[$i][0], 'url(' . $this->proxify_css_url($matches[$i][1]) . ')', $css);
			}

			preg_match_all("#@import\s*(?:\"([^\">]*)\"?|'([^'>]*)'?)([^;]*)(;|$)#i", $css, $matches, PREG_SET_ORDER);

			for ($i = 0, $count = count($matches); $i < $count; $i++)
			{
				$delim = '"';
				$url   = $matches[$i][2];

				if (isset($matches[$i][3]))
				{
					$delim = "'";
					$url = $matches[$i][3];
				}
				//era matches 1
				$css = str_replace($matches[$i][0], '@import ' . $delim . $this->proxify_css_url($matches[$i][2]) . $delim . (isset($matches[$i][4]) ? $matches[$i][4] : ''), $css);

			}

			return $css;
		}

		function proxify_css_url($url)
		{
			$url = trim($url);
			$delim = '';

			if (strpos($url, '"') === 0)
			{
				$delim = '"';
				$url   = trim($url, '"');
			}
			else if (strpos($url, "'") === 0)
			{
				$delim = "'";
				$url   = trim($url, "'");
			}

			$url = preg_replace('#\\\(.)#', '$1', $url);
			$url = trim($url);

			//echo "lalalaurl = " . trim($url) . " - para - ";

			$url = $this->proxify_url($url);
			//echo $url . "<br>";

			$url = preg_replace('#([\(\),\s\'"\\\])#', '\\$1', $url);

			return $delim . $url . $delim;
		}




		/*
		+-----------------+------------------------------------------------------------+
		|  Class          | PHProxy->modify_urls                                       |
		|  Author         | ultimategamer00 (Abdullah A.)                              |
		|  Last Modified  | 12:42 AM 9/8/2005                                          |
		+-----------------+------------------------------------------------------------+
		*/
		function modify_urls()
		{
			// this was a bitch to code
			// follows CGIProxy's logic of his HTML routine in some aspects

			/*
			*/

			$tags = array
			(
			'a'          => array('href'),
			'area'       => array('href'),
			'img'        => array('src', 'longdesc'),
			'image'      => array('src', 'longdesc'),
			'base'       => array('href'),
			'body'       => array('background'),
			'frame'      => array('src', 'longdesc'),
			'iframe'     => array('src', 'longdesc'),
			'head'       => array('profile'),
			'layer'      => array('src'),
			'input'      => array('src', 'usemap'),
			'form'       => array('action'),
			'link'       => array('href', 'src', 'urn'),
			'meta'       => array('content'),
			'param'      => array('value'),
			'applet'     => array('codebase', 'code', 'object', 'archive'),
			'object'     => array('usermap', 'codebase', 'classid', 'archive', 'data'),
			'script'     => array('src'),
			'select'     => array('src'),
			'hr'         => array('src'),
			'table'      => array('background'),
			'tr'         => array('background'),
			'th'         => array('background'),
			'td'         => array('background'),
			'bgsound'    => array('src'),
			'blockquote' => array('cite'),
			'del'        => array('cite'),
			'embed'      => array('src'),
			'fig'        => array('src', 'imagemap'),
			'ilayer'     => array('src'),
			'ins'        => array('cite'),
			'note'       => array('src'),
			'overlay'    => array('src', 'imagemap'),
			'q'          => array('cite'),
			'ul'         => array('src')
			);

			preg_match_all('#(<\s*style[^>]*>)(.*?)(<\s*/style[^>]*>)#is', $this->body_content, $matches, PREG_SET_ORDER);

			for ($i = 0, $count_i = count($matches); $i < $count_i; $i++)
			{
				$this->body_content = str_replace($matches[$i][0], $matches[$i][1]. $this->proxify_css($matches[$i][2]) .$matches[$i][3], $this->body_content);
			}

			preg_match_all("#<\s*([a-zA-Z]+)([^>]+)>#", $this->body_content, $matches);

			for ($i = 0, $count_i = count($matches[0]); $i < $count_i; $i++)
			{
				$tag = strtolower($matches[1][$i]);

				if (!isset($tags[$tag]) || !preg_match_all("#([a-zA-Z\-\/]+)\s*(?:=\s*(?:\"([^\">]*)\"?|'([^'>]*)'?|([^'\"\s]*)))?#", $matches[2][$i], $m, PREG_SET_ORDER))
				{
					continue;
				}

				$rebuild    = false;
				$extra_html = $temp = '';
				$attrs      = array();

				for ($j = 0, $count_j = count($m); $j < $count_j; $attrs[strtolower($m[$j][1])] = (isset($m[$j][4]) ? $m[$j][4] : (isset($m[$j][3]) ? $m[$j][3] : (isset($m[$j][2]) ? $m[$j][2] : false))), $j++);

				switch ($tag)
				{
					//remove os links para nao desfocar a avaliacao do usuario

					case 'a':
					case 'area':
						if (isset($attrs['href']))
						{
							//space-separated list of urls
							$rebuild = true;
							$attrs['href'] = "#";
						}
						break;

					case 'form':
						if (isset($attrs['action']))
						{
							//space-separated list of urls
							$rebuild = true;
							$attrs['action'] = "";
							$attrs['method'] = "POST";
							$attrs['onsubmit'] = "";

						}
						break;



					case 'base':
						if (isset($attrs['href']))
						{
							$rebuild = true;
							$this->parse_url($attrs['href'], $this->base);
							$attrs['href'] = $this->proxify_url($attrs['href']);
						}
						break;
					case 'body':
						if (isset($this->flags['include_form']) && $this->flags['include_form'])
						{
							$rebuild = true;
							ob_start();
							include_once 'url_form.inc';
							$extra_html = "\n" . ob_get_contents();
							ob_end_clean();
						}
					case 'meta':
						if (isset($this->flags['strip_meta']) && $this->flags['strip_meta'] && isset($attrs['name']) && preg_match('#(keywords|description)#i', $attrs['name']))
						{
							$this->body_content = str_replace($matches[0][$i], '', $this->body_content);
						}
						if (isset($attrs['http-equiv'], $attrs['content']) && strtolower($attrs['http-equiv']) === 'refresh')
						{
							if (preg_match('#^(\s*[0-9]+\s*;\s*url=)(.*)#i', $attrs['content'], $content))
							{
								$rebuild = true;
								$attrs['content'] =  $content[1] . $this->proxify_url($content[2]);
							}
						}
						break;
					case 'head':
						if (isset($attrs['profile']))
						{
							//space-separated list of urls
							$rebuild = true;
							$attrs['profile'] = implode(' ', array_map(array(&$this, 'proxify_url'), explode(' ', $attrs['profile'])));
						}
						break;
					case 'applet':
						if (isset($attrs['codebase']))
						{
							$rebuild = true;
							$temp = $this->base;
							$this->parse_url($this->proxify_url(rtrim($attrs['codebase'], '/') . '/', false), $this->base);
							unset($attrs['codebase']);
						}
						if (isset($attrs['code']) && strpos($attrs['code'], '/') !== false)
						{
							$rebuild = true;
							$attrs['code'] = $this->proxify_url($attrs['code']);
						}
						if (isset($attrs['object']))
						{
							$rebuild = true;
							$attrs['object'] = $this->proxify_url($attrs['object']);
						}
						if (isset($attrs['archive']))
						{
							$rebuild = true;
							$attrs['archive'] = implode(',', array_map(array(&$this, 'proxify_url'), preg_split('#\s*,\s*#', $attrs['archive'])));
						}
						if (!empty($temp))
						{
							$this->base = $temp;
						}
						break;
					case 'object':
						if (isset($attrs['usemap']))
						{
							$rebuild = true;
							$attrs['usemap'] = $this->proxify_url($attrs['usemap']);
						}
						if (isset($attrs['codebase']))
						{
							$rebuild = true;
							$temp = $this->base;
							$this->parse_url($this->proxify_url(rtrim($attrs['codebase'], '/') . '/', false), $this->base);
							unset($attrs['codebase']);
						}
						if (isset($attrs['data']))
						{
							$rebuild = true;
							$attrs['data'] = $this->proxify_url($attrs['data']);
						}
						if (isset($attrs['classid']) && !preg_match('#^clsid:#i', $attrs['classid']))
						{
							$rebuild = true;
							$attrs['classid'] = $this->proxify_url($attrs['classid']);
						}
						if (isset($attrs['archive']))
						{
							$rebuild = true;
							$attrs['archive'] = implode(' ', array_map(array(&$this, 'proxify_url'), explode(' ', $attrs['archive'])));
						}
						if (!empty($temp))
						{
							$this->base = $temp;
						}
						break;
					case 'param':
						if (isset($attrs['valuetype'], $attrs['value']) && strtolower($attrs['valuetype']) == 'ref' && preg_match('#^[\w.+-]+://#', $attrs['value']))
						{
							$rebuild = true;
							$attrs['value'] = $this->proxify_url($attrs['value']);
						}
						break;
					case 'form':
						if (isset($attrs['action']))
						{
							if (trim($attrs['action']) === '')
							{
								$rebuild = true;
								$attrs['action'] = $this->url_segments['path'];
							}
							if (!isset($attrs['method']) || strtolower($attrs['method']) === 'get')
							{
								$rebuild = true;
								$extra_html = '<input type="hidden" name="' . $this->config['get_form_name'] . '" value="' . $this->encode_url($this->proxify_url($attrs['action'], false)) . '" />';
								$attrs['action'] = '';
								break;
							}
						}
					default:
						foreach ($tags[$tag] as $attr)
						{
							if (isset($attrs[$attr]))
							{
								$rebuild = true;
								$attrs[$attr] = $this->proxify_url($attrs[$attr]);
							}
						}
						break;
				}

				if ($rebuild)
				{
					$new_tag = "<$tag";
					foreach ($attrs as $name => $value)
					{
						$delim = strpos($value, '"') && !strpos($value, "'") ? "'" : '"';
						$new_tag .= ' ' . $name . ($value !== false ? '=' . $delim . $value . $delim : '');
					}

					$this->body_content = str_replace($matches[0][$i], $new_tag . '>' . $extra_html, $this->body_content);
				}
			}
		}



		function remove_scripts()
		{
			$this->body_content = preg_replace('#<script[^>]*?>.*?</script>#si', '', $this->body_content); // Remove any scripts enclosed between <script />
			$this->body_content = preg_replace("#(\bon[a-z]+)\s*=\s*(?:\"([^\"]*)\"?|'([^']*)'?|([^'\"\s>]*))?#i", '', $this->body_content); // Remove javascript event handlers
			$this->body_content = preg_replace('#<noscript>(.*?)</noscript>#si', "$1", $this->body_content); //expose any html between <noscript />

		}



}


?>