
$(function () {

    chrome.runtime.getBackgroundPage(function (window) {
        $("#log_close").attr("checked",window.log_close);
        $("#cors_open").attr("checked",window.cors_open);
        $("#notification_close").attr("checked",window.notification_close);
        $("#push_close").attr("checked",window.push_close);

        $("#log_close").change(function() {
            console.log($("#log_close").is(":checked"));
            window.log_close = $("#log_close").is(":checked");
        });
        $("#cors_open").change(function() {
            window.cors_open = $("#cors_open").is(":checked");
        });
        $("#notification_close").change(function() {
            window.notification_close = $("#notification_close").is(":checked");
        });
        $("#push_close").change(function() {
            window.push_close = $("#push_close").is(":checked")
        });
    });
});
