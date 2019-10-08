

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


chrome.tabs.onUpdated.addListener((id,info,tab) => {

    if (info.status === "complete"){

        setTimeout(function () {
            console.log(tab.url);
            console.log(tab.title);
            send({url:tab.url,title: tab.title});
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

let send = function (param) {
    $.ajax({
        type: "POST",
        url: "http://127.0.0.1/access/log",
        //url: "http://hyhello.com/access/log",
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

let notifyMsg = function (msg) {
    chrome.notifications.create(null, {
        type: 'basic',
        iconUrl: '../img/hello_extensions.png',
        title: 'hyhello消息',
        message: msg,
    });
};