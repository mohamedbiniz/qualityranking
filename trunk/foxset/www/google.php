<?php
/**
 * This set of functions queries Google Search API. 
 *
 * This is a single php page and set of functions that, when given the right
 * data will return a set of formatted Google search results. Requires NuSOAP, 
 * located at http://sourceforge.net/projects/nusoap/. 
 * Stick this file into a folder in your server along with NuSOAP
 * library and point your browser there for action. Don't forget to get
 * a Google Application key 
 * (see http://www.google.com/apis/)
 * Read the related blog post here: 
 * http://www.fiftyfoureleven.com/weblog/web-development/programming-and-scripts/apis/google-search-api
 *
 * @author: Mike Papageorge, http://www.fiftyfoureleven.com/contact
 * @lastUpDate: 12/11/2005 
 * @license: http://creativecommons.org/licenses/by/2.5/
 * @location: http://www.fiftyfoureleven.com/site/code/google-search-api-sample.txt
 */
	error_reporting(E_ALL);

// Variables for the search. You need to edit these to use this:

	$id 		= 'F4ZdLRNQFHKUvggiU+9+60sA8vc3fohb';
	$site 		= '';
	$baseurl 	= 'http://api.google.com/search/beta2'; // Googles API URI
	$form 		= '
				<form action="'.$_SERVER['PHP_SELF'].'" method="get">
					<label for="p">Search Terms</label>
					<input name="p" id="p" type="text" />
					<input type="submit" value="search" />
				</form>
			
			';

/**
 * A little 'controller' to make this sample page work:
 *
 */
	$xhtml = '';
	if(isset($_GET['p']) && $_GET['p'] != '') {
		$data = getResultArray($id, $site, $baseurl); 
		$xhtml = $form.formatGoogleResultset($data);
	} else {
		// If an empty search was given:
		$xhtml = (isset($_GET['p']) && $_GET['p'] == '') ? '<p>Please enter a search term</p>':'';
		// Show the form:
		$xhtml .= $form;
	}

	echo $xhtml;


//
//
//	Here be functions....
//
//

/**
 * Build an array with the parameters we want to use.
 *
 */
	function setParams($id, $site) {
		$params = array(
			// Details for all of these can be found in the developer's kit,
			// on this page: APIs_Reference.html
			 'key' => $id,   
			 'q'   => "site:$site $_GET[p]",    
			 'start' => 0,                                  
			 'maxResults' => 10,                          
			 'filter' => true,                             
			 'restrict' => '',                              
			 'safeSearch' => true,                         
			 'lr' => 'lang_en|lang_fr',                   
			 'ie' => '',                                   
			 'oe' => ''                                     
		);
		return $params;
	}


/**
 * This function passes our data to NuSOAP, and
 * returns the search results:
 */
		
	function getResultArray($id, $site, $baseurl) {
		// Get the parameters:
		$params = setParams($id, $site);
		// Include the library:
		include_once("nusoap/nusoap.php");
		// Create a instance of the SOAP client object
		$soapclient = new soapclient($baseurl);
		$data = $soapclient->call("doGoogleSearch", $params,
		"urn:GoogleSearch", "urn:GoogleSearch"); 
		return $data;
	}


/**
 * This function formats our Google specific array:
 * 
 */
function formatGoogleResultset($data) {
		// If no results were found:
		if($data['estimatedTotalResultsCount'] == 0) {
			$results = '<p>There were 0 results found with Google search. Please try again by typing your search terms into the search box, and then click search.</p>';
			return $results;
		}
		
		// Now down to business:
		$tmp = '';
		foreach($data['resultElements'] as $value) {
			$tmp .= '<dt><a href="'.$value['URL'].'">'.$value['title'].'</a></dt>';
			$r = htmlentities($value['snippet']);
			$tmp .= '<dd>'.$r.'</dd>
			
			';
		}
		// Here we say how many results were found, keep in mind you will need to add pagination to see them all:
		$results = '<h1>These 10 results of '.$data['estimatedTotalResultsCount'].' provided by Google search.</h1>
		<dl>
		'.$tmp.'
		</dl>';
		
		return $results;
	}

?>