/**
 * 
 */
function postJSON(url, data, authenToken, onSuccess, onFail) {
	$.ajax({
		'type' : 'POST',
		'url' : url,
		'contentType' : 'application/json',
		'data' : JSON.stringify(data),
		'headers' : {
			"X-Auth-Token" : authenToken
		},
		'dataType' : 'json',
		'success' : onSuccess,
		'error' : onFail
	});
};
function postWithMultiplePart(url, data, onPostSuccess, onPostFail) {
	$.ajax({
		type: 'POST',
        enctype: 'multipart/form-data',
        url: url,
        data: data,
        processData: false,
        contentType: false,
        cache: false,
        timeout: 60000,
		success : onPostSuccess,
		error : onPostFail
	});
};
function getJSON(url, authenToken,  onGetSuccess, onGetFail) {
	$.ajax({
		'type' : 'GET',
		'url' : url,
		'contentType' : 'application/json',
		'dataType' : 'json',
		'headers' : {
			"X-Auth-Token" : authenToken
		},
		'success' : onGetSuccess,
		'error' : onGetFail
	});
};
function putJSON(url, data, authenToken, onUpdateSuccess, onUpdateFail) {
	$.ajax({
		'type' : 'POST',
		'url' : url,
		'contentType' : 'application/json',
		'data' : JSON.stringify(data),
		'dataType' : 'json',
		'headers' : {
			"X-Auth-Token" : authenToken
		},
		'success' : onUpdateSuccess,
		'error' : onUpdateFail
	});
};
function deleteItemJson(url, authenToken, onDelSuccess, onDelFail) {
	$.ajax({
		'type' : 'GET',
		'url' : url,
		'contentType' : 'application/json',
		'dataType' : 'json',
		'headers' : {
			"X-Auth-Token" : authenToken
		},
		'async' : false,
		'success' : onDelSuccess,
		'error' : onDelFail
	});
};