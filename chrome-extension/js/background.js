

// var filterURLs = {"urls": ["http://*/*", "https://*/*"]};
// var receivedListener = function (info) {
//     var response = info.responseHeaders.filter(e => e.name.toLowerCase() !== 'access-control-allow-origin' && e.name.toLowerCase() !== 'access-control-allow-methods');
//     response.push({'name': 'Access-Control-Allow-Origin','value': '*'});
//     response.push({'name': 'Access-Control-Allow-Methods', 'value': 'GET, PUT, POST, DEvarE, HEAD, OPTIONS'});
//
//     return {"responseHeaders": response};
// };
//
// chrome.webRequest.onHeadersReceived.addListener(receivedListener, filterURLs, ["blocking", "responseHeaders"]);

var log_close = false;
var cors_open = false;
var notification_close = false;
var push_close = true;

var whiteArray = [];


var postLog = function (param) {
    $.ajax({
        type: "POST",
        url: "http://49.232.170.71/accessLog",
        //url: "http://127.0.0.1/accessLog",
        data: {url:param.url, title:param.title},
        dataType: "json",
        success: function(data){
            console.log(data);
        },
        error: function (error) {
            console.log(error);
            notifyMsg("访问记录传输失败! (⇀‸↼‶) \n"  + param.url);
        }
    });
};

var getLogWhiteList = function(){
	$.ajax({
        type: "GET",
        url: "http://49.232.170.71/accessLog/whiteList",
        dataType: "json",
        success: function(data){
            console.log(data);
			whiteArray = data.list;
        },
        error: function (error) {
            console.log(error);
            notifyMsg("获取记录白名单失败! (⇀‸↼‶) \n");
        }
    });
	
};

var notifyMsg = function (msg) {
	if (notification_close){
		return;
	}
    chrome.notifications.create(null, {
        type: 'basic',
        iconUrl: '../img/hello_extensions.png',
        title: 'hyhello消息',
        message: msg,
    });
};

var getHost = function(url){
	
    var l = document.createElement("a");
    l.href = url;
    return l.hostname;
};

var filterInWhite = function(url, title){
	console.log(log_close);
	for(var j = 0,len = whiteArray.length; j < len; j++) {
	   var regular = whiteArray[j].regular;
	   var regType = whiteArray[j].regType;
	   var host = getHost(url);

	   switch(regType) {
		   
			 case 1:
				if (url.search(regular) !== -1){
					console.log(url + "-" + regular + "-" + regType);
					return true;
				}
				break;
			 case 2:
				if (host.search(regular) !== -1){
					console.log(url + "-" + regular + "-" + regType);
					return true;
				}
				break;	
			 default:
				notifyMsg("unsupported regularType, update extension may resolve this problem");
		} 
	}
	return false;
};

$(function(){
	notifyMsg("hyhello start successfully");
	getLogWhiteList();
	
	chrome.tabs.onUpdated.addListener((id,info,tab) => tabsUpdateListener);
	//chrome.tabs.onUpdated.removeListener()

	chrome.bookmarks.onCreated.addListener((id, bookmark) => {
		console.log(bookmark.title);
		console.log(bookmark.url);
	});

	chrome.bookmarks.onChanged.addListener((id, changeInfo) => {
		console.log(changeInfo);
	});

});

function tabsUpdateListener(id,info,tab) {
	if (info.status === "complete"){

		setTimeout(function () {
			//console.log(tab);
			chrome.tabs.get(id, function (activeTab) {

				if (!filterInWhite(activeTab.url, activeTab.title)){
					postLog({url:activeTab.url, title:activeTab.title});
				}
			})
		},3000);
	}
}


function logSwitch(log_close0) {
	log_close = log_close0;
}
function corsSwitch(cors_open0) {
	cors_open = cors_open0;
}
function notificationSwitch(notification_close0) {
	notification_close = notification_close0;
}
function pushSwitch(push_close0) {
	push_close = push_close0;
}



