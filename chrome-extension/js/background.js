

// let filterURLs = {"urls": ["http://*/*", "https://*/*"]};
// let receivedListener = function (info) {
//     let response = info.responseHeaders.filter(e => e.name.toLowerCase() !== 'access-control-allow-origin' && e.name.toLowerCase() !== 'access-control-allow-methods');
//     response.push({'name': 'Access-Control-Allow-Origin','value': '*'});
//     response.push({'name': 'Access-Control-Allow-Methods', 'value': 'GET, PUT, POST, DELETE, HEAD, OPTIONS'});
//
//     return {"responseHeaders": response};
// };
//
// chrome.webRequest.onHeadersReceived.addListener(receivedListener, filterURLs, ["blocking", "responseHeaders"]);

let whiteArray = [];

let postLog = function (param) {
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

let getLogWhiteList = function(){
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
            notifyMsg("获取记录白名单失败! (⇀‸↼‶) \n"  + param.url);
        }
    });
	
}

let notifyMsg = function (msg) {
    chrome.notifications.create(null, {
        type: 'basic',
        iconUrl: '../img/hello_extensions.png',
        title: 'hyhello消息',
        message: msg,
    });
};

let getHost = function(url){
	
    var l = document.createElement("a");
    l.href = url;
    return l.hostname;
};



let filterInWhite = function(url, title){	
	for(j = 0,len = whiteArray.length; j < len; j++) {
	   let regular = whiteArray[j].regular;
	   let regType = whiteArray[j].regType;
	   let host = getHost(url);

	   	console.log(url + "-" + regular + "-" + regType)
	   
	   switch(regType) {
		   
			 case 1:
				if (url.search(regular) != -1){
					return true;
				}
				break;
			 case 2:
				if (host.search(regular) != -1){
					return true;
				}
				break;	
			 default:
				notifyMsg("不支持的白名单类型, 请升级插件");
		} 
	}
	return false;
}

$(function(){
	notifyMsg("hyhello start successfully");
	
	getLogWhiteList();
	
	chrome.tabs.onUpdated.addListener((id,info,tab) => {

		//console.log(id);
		//console.log(info);
		if (info.status === "complete"){
			
			setTimeout(function () {
				//console.log(tab);
				chrome.tabs.get(id, function (activeTab) {
					
					if (!filterInWhite(activeTab.url, activeTab.title)){
						postLog({url:activeTab.url, title:activeTab.title});
					}	
				})
			},1000);
		}
	});

	chrome.bookmarks.onCreated.addListener((id, bookmark) => {
		console.log(bookmark.title);
		console.log(bookmark.url);
	});

	chrome.bookmarks.onChanged.addListener((id, changeInfo) => {
		console.log(changeInfo);
	});

})



