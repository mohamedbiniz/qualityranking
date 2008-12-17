
function findObject(n, d){
	var p,i,x;
	if(!d) d=document;
	if((p=n.indexOf("?"))>0&&parent.frames.length) {
		d=parent.frames[n.substring(p+1)].document; n=n.substring(0,p);
	}
	if(!(x=d[n])&&d.all) x=d.all[n];
	for(i=0;!x&&i<d.forms.length;i++) x=d.forms[i][n];
	for(i=0;!x&&d.layers&&i<d.layers.length;i++) x=findObject(n,d.layers[i].document);
	if(!x && document.getElementById) x=document.getElementById(n);
	return x;
}

function submit_pagina(novapagina){
	var obj = findObject('start');
	obj.value = novapagina;
	submit_form('paginacao')
}

function popup(address,winname,sizew,sizeh){
	var ie4=(document.all)? true:false;
	var features = ',menubar=0,resizable=0,scrollbars=0,toolbar=0';
	if(ie4){
		features = 'height=' + sizeh + ',width=' + sizew + features;
	}else{
		features = 'innerHeight=' + sizeh + ',innerWidth=' + sizew + features;
	}
	window.open(address, winname, features);
}

function showHide(inID) {
	theObj = findObject(inID)
	theDisp = (theObj.style.display == "none") ? "block" : "none"
	theObj.style.display = theDisp
} 

function newWindow(url){
	window.open(url,url);
}
