

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

//install
//homepage = function () {return chrome.runtime.getManifest().homepage_url};

var homepage = "http://www.hyhello.com";
var tab = {"open": function (url) {chrome.tabs.create({"url": url, "active": true})}};
chrome.runtime.setUninstallURL(homepage + "?v=1"+ "&type=uninstall", function () {});
chrome.runtime.onInstalled.addListener(function (e) {
    window.setTimeout(function () {
        if (e.reason === "install") {
            tab.open(homepage + '?v=1' + + "&type=" + e.reason);
        }
    }, 2000);
});


var logMap = new Map();
var test_mode = false;
var log_close = false;
var cors_open = false;
var notification_close = false;
var push_close = true;
var whiteArray = [];
var locationValue = window.localStorage.getItem("location");

function getConnHost(){
	var remotehost = "http://www.hyhello.com";
	var remoteAddr = "http://49.232.170.71";
	var localhost =  "http://127.0.0.1"
	return test_mode ? localhost : remotehost;
}


var postLog = function (param) {
    $.ajax({
        type: "POST",
        url: getConnHost() + "/accessLog",
        data: {url:param.url, title:param.title, location:locationValue},
        dataType: "json",
        success: function(data){
            console.log(data);
        },
        error: function (error) {
            console.log(error);
            notifyMsg("访问记录传输失败!  (⇀‸↼‶) \n"  + param.url);
        }
    });
};

var postFavorite = function(url) {
	$.ajax({
        type: "PUT",
        url: getConnHost() + "/favorite",
        data: {url:url},
        dataType: "json",
        success: function(data){
            console.log(data);
        },
        error: function (error) {
            console.log(error);
            notifyMsg("收藏传输失败!  (⇀‸↼‶) \n"  + url);
        }
    });
}

var getLogWhiteList = function(){
	$.ajax({
        type: "GET",
        url: getConnHost() + "/accessLog/whiteList",
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


var favoriteFilter = {}
var getFavoriteFilter = function(){
	return $.ajax({
		type: "GET",
		url: getConnHost() + "/favorite/filter",
		dataType: "json",
		success: function(data){
			console.log(data);
			favoriteFilter = data.list.map(function(e){
				return e.chromePattern;
			});
		},
		error: function (error) {
			console.log(error);
			notifyMsg("获取收藏过滤失败! (⇀‸↼‶) \n");
		}
	})
};


var createContextMenu = function(){
	getFavoriteFilter().then(function(res) {
        chrome.contextMenus.create({
			"title": "收藏...",
			"contexts":["page"],
			"onclick":favorContextListener,
			"documentUrlPatterns" : favoriteFilter
		});    
    })
}

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
	for(var j = 0,len = whiteArray.length; j < len; j++) {
	   var regular = whiteArray[j]['regular'];
	   var regType = whiteArray[j]['regType'];
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
	init();
});

function init(){
	notifyMsg("hyhello start successfully in " + getConnHost() + ", cur location:" + locationValue);
	getLogWhiteList();
	createContextMenu();

    logSwitch(false);
    addBookmarkListener();
}

function favorContextListener(info,tab) {

	console.log(info);
	postFavorite(info.pageUrl)
}

function tabsUpdateListener(id,info,tab) {
	if (info.status === "loading"){	
		logMap.set(id,tab.url);
	}
	if (info.title !== undefined){
	    var url = logMap.get(id);
		if (url !== undefined){
            if (!filterInWhite(url, info.title)){
                postLog({url:url, title:info.title});
            }
            logMap.delete(id);
        }
	}
}

function tabsRemoveListener(id, info) {
    logMap.delete(id);
}


function logSwitch(log_close0) {
	log_close = log_close0;
	if (log_close0){
        chrome.tabs.onUpdated.removeListener(tabsUpdateListener);
        chrome.tabs.onRemoved.removeListener(tabsRemoveListener);
    }else {
        chrome.tabs.onUpdated.addListener(tabsUpdateListener);
        chrome.tabs.onRemoved.addListener(tabsRemoveListener);
    }
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
function TestModeSwitch(test_mode0) {
	test_mode = test_mode0;
	init();
}

function refreshLocation(){
    var locationUpdate =  window.localStorage.getItem("location");
    locationValue = locationUpdate;
    notifyMsg("location update:" + locationUpdate);
}

function addBookmarkListener() {

    chrome.bookmarks.onCreated.addListener((id, bookmark) => {
        var msg = "创建书签:" + bookmark.title + " - " + bookmark.url
        notifyMsg(msg);
        console.info(msg);
    });

    chrome.bookmarks.onChanged.addListener((id, changeInfo) => {
        var msg = "书签变化:" + changeInfo.title + " - " + changeInfo.url
        notifyMsg(msg);
        console.info(msg);
        console.info(changeInfo);
    });

}

