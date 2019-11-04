
var CLIPBOARD = null;
var tree = null;
var directory = {};
directory.save = function () {
    var dir = JSON.stringify(tree.toDict(false));
    $.ajax({
        headers: {
            'Content-Type': 'application/json'
        },
        type: "POST",
        url: "http://127.0.0.1/note/directory",
        data: dir,
        dataType: "json",
        success: function(data){
            console.log(data);
        },
        error: function (error) {
            console.log(error);
            notifyMsg("笔记目录保存失败!  (⇀‸↼‶) \n"  + param.url);
        }
    });
}

directory.load = function () {
    $.ajax({
        type: "GET",
        url: "http://127.0.0.1/note/directory",
        dataType: "json",
        success: function(data){
            tree.reload(data["directory"]);
            console.log(data);
        },
        error: function (error) {
            console.log(error);
            notifyMsg("笔记目录获取失败!  (⇀‸↼‶) \n"  + param.url);
        }
    });
};


$(function () {  // on page load
    // Create the tree inside the <div id="tree"> element.
    $("#tree").fancytree({
        extensions: ["edit", "filter", "glyph"],
        source: [
            {title: "Node 1", key: "1"},
            {
                title: "Folder 2", key: "2", folder: true, children: [
                    {title: "Node 2.1", key: "3", id: "3666"},
                    {title: "Node 2.2", key: "4"}
                ]
            },
            {
                title: "Folder 3", key: "5", folder: true, children: [
                    {title: "Node 2.3", key: "6"},
                    {title: "Node 2.4", key: "7"}
                ]
            }
        ],
        glyph: {
            preset: "awesome4",
            map: {}
        },
        edit: {
            save: function(event, data){
                // Only called when the text was modified and the user pressed enter or
                // the <input> lost focus.
                // Additional information is available (see `beforeClose`).
                // Return false to keep editor open, for example when validations fail.
                // Otherwise the user input is accepted as `node.title` and the <input>
                // is removed.
                // Typically we would also issue an Ajax request here to send the new data
                // to the server (and handle potential errors when the asynchronous request
                // returns).
                if (data.save){
                    console.log(data.isNew);
                    console.log(data.input.val());
                }
            }
        }
    }).on("nodeCommand", function (event, data) {
        // Custom event handler that is triggered by keydown-handler and
        // context menu:
        var refNode,
            moveMode,
            tree = $(this).fancytree("getTree"),
            node = tree.getActiveNode();

        switch (data.cmd) {
            case "addChild":
            case "addSibling":
            case "indent":
            case "moveDown":
            case "moveUp":
            case "outdent":
            case "remove":
                console.log("remove");
                // ajax remove
                tree.applyCommand(data.cmd, node);
                break;
            case "rename":
                tree.applyCommand(data.cmd, node);
                break;
            case "cut":
                CLIPBOARD = {mode: data.cmd, data: node};
                break;
            case "copy":
                CLIPBOARD = {
                    mode: data.cmd,
                    data: node.toDict(function (n) {
                        delete n.key;
                    }),
                };
                break;
            case "clear":
                CLIPBOARD = null;
                break;
            case "paste":
                if (CLIPBOARD.mode === "cut") {
                    // refNode = node.getPrevSibling();
                    CLIPBOARD.data.moveTo(node, "child");
                    CLIPBOARD.data.setActive();
                } else if (CLIPBOARD.mode === "copy") {
                    node.addChildren(
                        CLIPBOARD.data
                    ).setActive();
                }
                break;
            default:
                alert("Unhandled command: " + data.cmd);
                return;
        }
    }).on("keydown", function (e) {
        var cmd = null;

        switch ($.ui.fancytree.eventToString(e)) {

            case "ctrl+c":
            case "meta+c": // mac
                directory.save();
                break;
            case "ctrl+v":
            case "meta+v": // mac
                cmd = "paste";
                directory.load();
                break;
            case "ctrl+x":
            case "meta+x": // mac
                cmd = "cut";
                break;
            case "del":
            case "meta+backspace": // mac
                cmd = "remove";
                break;
            case "ctrl+s":
            case "meta+c": // mac
                break;
        }
        if (cmd) {
            $(this).trigger("nodeCommand", {cmd: cmd});
            return false;
        }
    });
    tree = $("#tree").fancytree("getTree");

    $.contextMenu({
        selector: '.fancytree-icon, .fancytree-title',
        callback: function (key, options) {
            if (key === "New File") {
                tree.getActiveNode().editCreateNode("child", "new note");
            } else if (key === "New Folder") {
                tree.getActiveNode().editCreateNode("child", {title: "new folder", folder: true});
            } else if (key === "Load"){
                directory.load();
            } else if (key === "Save"){
                directory.save();
            }

            else {
                $(this).trigger("nodeCommand", {cmd: options.commands[key].cmd});
            }
        },
        items: {
            "New File": {name: "New File", icon: "fa-check" ,disabled: disableFileTypeMenu},
            "New Folder": {name: "New Folder", icon: "fa-folder-o" , disabled: disableFileTypeMenu},
            "sep1": "---------",
            "Rename": {name: "Rename", icon: "edit", cmd: "rename"},
            "Delete": {name: "Delete", icon: "delete", cmd: "remove"},
            "Save": {name: "Save", icon: "edit"},
            "Load": {name: "Load", icon: "edit"}
        }
    });


});

/**
 *  当节点类型为文件时禁用创建子文件/文件夹的命令
 */
function  disableFileTypeMenu() {
    return !tree.getActiveNode().isFolder();
}

