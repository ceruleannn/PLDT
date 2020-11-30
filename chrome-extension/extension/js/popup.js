
$(function () {

    chrome.runtime.getBackgroundPage(function (window) {
        $("#log_close").attr("checked",window.log_close);
        $("#cors_open").attr("checked",window.cors_open);
        $("#notification_close").attr("checked",window.notification_close);
        $("#push_close").attr("checked",window.push_close);
		$("#test_mode").attr("checked",window.test_mode);
        $("#location").val(window.window.localStorage.getItem("location"));

        $("#log_close").change(function() {
            window.logSwitch($("#log_close").is(":checked"));
        });
        $("#cors_open").change(function() {
            window.corsSwitch($("#cors_open").is(":checked"));
        });
        $("#notification_close").change(function() {
            window.notificationSwitch($("#notification_close").is(":checked"));
        });
        $("#push_close").change(function() {
            window.pushSwitch($("#push_close").is(":checked"));
        });
		$("#test_mode").change(function() {
            window.TestModeSwitch($("#test_mode").is(":checked"));
        });
        $("#location").blur(function() {
           var location = $("#location").val();
           window.window.localStorage.setItem("location", location);
           window.refreshLocation();
        });

    });
});
