function jump(page) {
	jump('searchForm', page);
}

/* Jump to new page by re-submitting search form with page parameter 
 * If no search form:  replace page in query parameter, then redirect page 
 * */
function jumpPage(form, page) {
	console.log("jumpPage()");
	if (form == '') {
		var loca = updateQueryStringParameter(window.location.href, "page", page);
		console.log("Going to " + loca);
		window.location.href =  loca;
	} else {
		$('#page').val(page);
		console.log("Submit form " + form + " with page=" + page);
		$("#"+form+"").submit();
	}
}

function getPageAndJump(form, pageInput) {
	var newPage = $('#pageInput').val();
	console.log("Get page number from input " + pageInput + " and jump to new page " + newPage);
	$('#page').val(newPage);
	$("#"+form+"").submit();
}

function updateQueryStringParameter(uri, key, value) {
	  var re = new RegExp("([?&])" + key + "=.*?(&|$)", "i");
	  var separator = uri.indexOf('?') !== -1 ? "&" : "?";
	  if (uri.match(re)) {
	    return uri.replace(re, '$1' + key + "=" + value + '$2');
	  }
	  else {
	    return uri + separator + key + "=" + value;
	  }
	}

// redirect URL to page
function changePageAndSize() {
	$('#pageSizeSelect').change(function(evt) {
		window.location.replace("/?pageSize=" + this.value + "&page=1");
	});
}