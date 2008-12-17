<?php

$url = $_GET["url"];

if (!$url) {
	die("OOPS: You need to pass a URL!<br />");
}

require_once( "../include/FOXGrabber.php");

$fetcher = new FOXGrabber();
$fetcher->grab(base64_encode($url));
echo ($fetcher->return_response());
?>