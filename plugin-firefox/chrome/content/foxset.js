(function() {


var FoxSet = window.FoxSet = {};

var collaborator = null;

var datasets = null, selectedDataset = null;

var prefs = null;


function evalAsExpression(expression) {
	return eval('(' + expression + ')');
}


function getServerURL() {
	var url = prefs.getCharPref('serverURL');
	var i = url.lastIndexOf('/');
	if (i != url.length - 1) url = url + '/';
	return url;
}


function executeCommand(postVars, resultFunction) {
	objHTTP = new XMLHttpRequest();
	objHTTP.open('POST', getServerURL() + 'controller.php', false);
	objHTTP.setRequestHeader('Content-Type', 'application/x-www-form-urlencoded');
	try {
		objHTTP.send(postVars);
		if (objHTTP.status == 200) {
			strResult = objHTTP.responseText;
			try {
				var result = evalAsExpression(strResult);
				resultFunction(result);
			} catch (e) {
				alert('Response text error:\n\n' + e + '\n\nResponse text:\n\n' + strResult);
			}
		} else {
			alert('Server error.');
		}
	} catch (e) {
		alert('Connection error:\n\n' + e);
	}
}


function openTab(url) {
	var wm = Components.classes['@mozilla.org/appshell/window-mediator;1'].getService(Components.interfaces.nsIWindowMediator);
	for (var found = false, index = 0, tabbrowser = wm.getEnumerator('navigator:browser').getNext().getBrowser(); index < tabbrowser.mTabs.length && !found; index++) {
		var currentTab = tabbrowser.mTabs[index];
		if (currentTab.hasAttribute('foxset')) {
			var currentBrowser = tabbrowser.getBrowserAtIndex(index);
			currentBrowser.loadURI(url, null, null);
			tabbrowser.selectedTab = currentTab;
			tabbrowser.focus();
			found = true;
		}
	}
	if (!found) {
		var browserEnumerator = wm.getEnumerator('navigator:browser');
		var tabbrowser = browserEnumerator.getNext().getBrowser(); 
		var newTab = tabbrowser.addTab(url);
		newTab.setAttribute('foxset', 'foxset');
		tabbrowser.selectedTab = newTab;
		tabbrowser.focus();
	}
}


function replaceTabs(homeOnly) {
	var serverURL = getServerURL();
	var wm = Components.classes['@mozilla.org/appshell/window-mediator;1'].getService(Components.interfaces.nsIWindowMediator);
	for (var index = 0, tabbrowser = wm.getEnumerator('navigator:browser').getNext().getBrowser(); index < tabbrowser.mTabs.length; index++) {
		var currentTab = tabbrowser.mTabs[index];
		var currentBrowser = tabbrowser.getBrowserAtIndex(index);
		var url = currentBrowser.currentURI.spec;
		if (!homeOnly) url = url.substr(0, serverURL.length);
		if (url == serverURL) {
			currentBrowser.loadURI(serverURL, null, null);
		}
	}
}


function enableToolbar(enable) {
	var lblUsername = document.getElementById('lblUsername');
	var btnLogin = document.getElementById('btnLogin');
	var lblDatasets = document.getElementById('lblDatasets');
	var mnuDatasets = document.getElementById('mnuDatasets');
	var btnDatasets = document.getElementById('btnDatasets');
	var lblDataset = document.getElementById('lblDataset');
	var btnCoordinate = document.getElementById('btnCoordinate');
	var btnAdd = document.getElementById('btnAdd');
	var btnEvaluate  = document.getElementById('btnEvaluate');
	var lblRating = document.getElementById('lblRating');
	var mnuRating  = document.getElementById('mnuRating');
	var btnRating  = document.getElementById('btnRating');
	lblDatasets.disabled = !enable;
	mnuDatasets.disabled = !enable;
	btnDatasets.disabled = !enable;
	btnCoordinate.disabled = true;
	btnAdd.disabled = true;
	btnEvaluate.disabled = true;
	lblRating.disabled = true;
	mnuRating.disabled = true;
	btnRating.disabled = true;
	if (enable) {
		lblUsername.value = collaborator.username + ':';
		with (btnLogin) {
			label = 'Logout';
			image = 'chrome://foxset/skin/cancel16.png';
			oncommand = function() { FoxSet.logout(); };
		}
	} else {
		lblUsername.value = '';
		lblDataset.value = '';
		with (btnLogin) {
			label = 'Login';
			image = 'chrome://foxset/skin/cadeado16.png';
			oncommand = function() { FoxSet.login(); };
		}
	}
}


function getPrefs() {
	return Components.classes['@mozilla.org/preferences-service;1'].getService(Components.interfaces.nsIPrefService);
}


function getDatasetById(id) {
	for (var i in datasets) {
		var d = datasets[i];
		if (d.id == id) {
			return d;
		}
	}
	return null;
}


function startsWith(str1, str2) {
	return str1.substr(0, str2.length) == str2;
}


function reloadDatasets() {
	var mnuDatasets = document.getElementById('mnuDatasets');
	mnuDatasets.removeAllItems();
	mnuDatasets.appendItem('', '');
	for (var i in datasets) {
		var d = datasets[i];
		mnuDatasets.appendItem(d.context, d.id);
	}
}


function reloadDatasetButtons() {
	var btnCoordinate = document.getElementById('btnCoordinate');
	var btnAdd = document.getElementById('btnAdd');
	var btnEvaluate  = document.getElementById('btnEvaluate');
	var lblRating = document.getElementById('lblRating');
	var mnuRating  = document.getElementById('mnuRating');
	var btnRating  = document.getElementById('btnRating');
	btnCoordinate.disabled = selectedDataset == null || selectedDataset.role != 'C';
	btnAdd.disabled = selectedDataset == null || selectedDataset.status == 'F' || (selectedDataset.method != 'M' && selectedDataset.role != 'C') || (selectedDataset.method == 'M' && selectedDataset.role == 'U');
	btnEvaluate.disabled = selectedDataset == null || selectedDataset.status == 'F' || selectedDataset.role == 'U';
	lblRating.disabled = btnEvaluate.disabled;
	mnuRating.disabled = btnEvaluate.disabled;
	btnRating.disabled = btnEvaluate.disabled;
}


FoxSet.onPageLoad = function (event) {
	if (event.originalTarget instanceof HTMLDocument) {
		var doc = event.originalTarget;
		var serverURL = getServerURL();
		if (startsWith(doc.URL, serverURL + 'logout.php')) {
			FoxSet.logout();
		} else if (collaborator != null && (startsWith(doc.URL, serverURL + 'cadastros/datasets.php?acao=salvar') || startsWith(doc.URL, serverURL + 'cadastros/datasets.php?acao=finalizar'))) {
			executeCommand('cmd=datasets',
				function(result) {
					if (result.success) {
						var sid = selectedDataset != null ? selectedDataset.id : 0;
						datasets = result.datasets;
						if (sid != 0) selectedDataset = getDatasetById(sid);
						reloadDatasets();
						reloadDatasetButtons();
					}
				}
			);
		}
	}
}



FoxSet.startup = function() {
	prefs = getPrefs().getBranch('extensions.foxset.');
};


FoxSet.shutdown = function() {
};


FoxSet.home = function() {
	openTab(getServerURL());
};


FoxSet.login = function() {
	var prompts = Components.classes['@mozilla.org/embedcomp/prompt-service;1'].getService(Components.interfaces.nsIPromptService);
	var username = {value: prefs.getCharPref('username')};
	var password = {value: prefs.getCharPref('password')};
	var check = {value: prefs.getBoolPref('saveLogin')};
	if (!prompts.promptUsernameAndPassword(window, 'Login - FoxSet', 'Please, type in your username and password.', username, password, 'Save username and password', check)) {
		return;
	}
	if (check.value) {
		prefs.setCharPref('username', username.value);
		prefs.setCharPref('password', password.value);
		prefs.setBoolPref('saveLogin', check.value);
		getPrefs().savePrefFile(null);
	}
	executeCommand('cmd=login&username=' + username.value + '&password=' + password.value,
		function(result) {
			//alert(result.message);
			if (result.success) {
				collaborator = result.collaborator;
				datasets = result.datasets;
				reloadDatasets();
				replaceTabs(true);
			}
			enableToolbar(result.success);
		}
	);
};


FoxSet.logout = function() {
	executeCommand('cmd=logout',
		function(result) {
			//alert(result.message);
			collaborator = null;
			datasets = null;
			selectedDataset = null;
			enableToolbar(false);
			replaceTabs(false);
		}
	);
};


FoxSet.setDataSet = function() {
	var id = document.getElementById('mnuDatasets').value;
	if (!id) {
		alert('Please, select a dataset from the list.');
		return;
	}
	selectedDataset = getDatasetById(id);
	document.getElementById('lblDataset').value = selectedDataset.context + ':';
	executeCommand('cmd=setDataset&id=' + id,
		function(result) {
			if (!result.success) {
				alert(result.message);
			} else {
				openTab(getServerURL() + result.url);
				reloadDatasetButtons();
			}
		}
	);
};


FoxSet.coordinate = function() {
	executeCommand('cmd=coordinate',
		function(result) {
			if (!result.success) {
				alert(result.message);
			} else {
				openTab(getServerURL() + result.url);
			}
		}
	);
};


FoxSet.add = function() {
	var url = window.content.location.href;
	executeCommand('cmd=add&url=' + url,
		function(result) {
			alert(result.message);
		}
	);
};


FoxSet.evaluate = function() {
	executeCommand('cmd=evaluate',
		function(result) {
			if (!result.success) {
				alert(result.message);
			} else {
				openTab(getServerURL() + result.url);
				var mnuRating = document.getElementById('mnuRating');
				mnuRating.disabled = false;
				document.getElementById('btnRating').disabled = false;
				mnuRating.removeAllItems();
				mnuRating.appendItem('','');
				for (var i in result.scale) {
					var s = result.scale[i];
					mnuRating.appendItem(s.name, s.id);
				}
			}
		}
	);
};


FoxSet.saveEvaluation = function() {
	var url = window.content.location.href;
	var params = url.substr(url.lastIndexOf('?') + 1, url.length);
	var idEscala = document.getElementById('mnuRating').value;
	if (!idEscala) {
		alert('Please, select a rating from the list.');
		return;
	}
	executeCommand('cmd=save_evaluate&' + params + '&IdEscala=' + idEscala,
		function(result) {
			if (!result.success) {
				alert(result.message);
			} else {
				openTab(getServerURL() + result.url);
			}
		}
	);
};


addEventListener('load', function(e) { FoxSet.startup(); gBrowser.addEventListener('load', FoxSet.onPageLoad, true); }, false);
addEventListener('unload', function(e) { FoxSet.shutdown(); }, false);


})();
