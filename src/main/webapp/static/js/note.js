
var CLIPBOARD = null;
var tree = null;
var editor = null;
var directory = {};
var note = {};

directory.saveDir = function () {
    var dir = JSON.stringify(tree.toDict(false));
    return $.ajax({
        headers: {
            'Content-Type': 'application/json'
        },
        type: "PUT",
        url: "http://127.0.0.1/note/directory",
        data: dir,
        dataType: "json",
    }).fail(function (error) {
        console.log(error);
        alert("error" + error);
    });
};

directory.deleteDir = function (nodeId) {
    return $.ajax({
        type: "DELETE",
        url: "http://127.0.0.1/note/directory/" + nodeId,
        dataType: "json",
    }).fail(function (error) {
        console.log(error);
        alert("error" + error);
    });
};

directory.loadDir = function () {
    $.ajax({
        type: "GET",
        url: "http://127.0.0.1/note/directory",
        dataType: "json",
        success: function(data){
            console.log(data);
            tree.reload(data["directory"]);
        },
        error: function (error) {
            console.log(error);
        }
    });
};

note.getNote = function(noteId){
    return $.ajax({
        type: "GET",
        url: "http://127.0.0.1/note/" + noteId,
        dataType: "json",
    }).fail(function (error) {
        console.log(error);
    })
};

note.saveNote = function(noteId, text){
    return $.ajax({
        type: "POST",
        url: "http://127.0.0.1/note/" + noteId,
        data: {text:text},
        dataType: "json",
    }).fail(function (error) {
        console.log(error);
    })
};

$(function () {  // on page loadDir
    // Create the tree inside the <div id="tree"> element.
    $("#tree").fancytree({
        extensions: ["edit", "filter", "glyph"],
        source:  [],
        glyph: {
            preset: "bootstrap3",
            map: {}
        },
        edit: {
            close: function(event, data){
                // Only called when the text was modified and the user pressed enter or
                // the <input> lost focus.
                // Additional information is available (see `beforeClose`).
                // Return false to keep editor open, for example when validations fail.
                // Otherwise the user input is accepted as `node.title` and the <input>
                // is removed.
                // Typically we would also issue an Ajax request here to send the new data
                // to the server (and handle potential errors when the asynchronous request
                // returns).

                //新数据 加到数据库之后reload
                console.log(data.isNew);

                directory.saveDir().done(() => directory.loadDir());
            }
        },
        activate: function(event, data){
            var node = data.node;
            note.getNote(node.key).done(function (data) {
                editor.txt.html(data["note"]["text"]);
            })
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
                directory.deleteDir(node.data.id).done(function (data) {

                    if (data["code"] < 500){
                        tree.applyCommand("remove", node);
                    }else {
                        console.log(data);
                        alert(data["code"] + " - " + data["msg"]);
                    }
                });
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
                directory.saveDir();
                break;
            case "ctrl+v":
            case "meta+v": // mac
                cmd = "paste";
                directory.loadDir();
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

        selector: '.fancytree-plain.fancytree-container span.fancytree-node',

        build: function($triggerElement, e){

            var hoverNode = $.ui.fancytree.getNode($triggerElement);

            var active = tree.getActiveNode();
            if (active != null){
                active.setActive(false);
                active.setFocus(false);
            }
            hoverNode.setActive(true);
            hoverNode.setFocus(true);

            return {
                callback: function (key, options) {
                    if (key === "New File") {
                        tree.getActiveNode().editCreateNode("child", "new note");
                    } else if (key === "New Folder") {
                        tree.getActiveNode().editCreateNode("child", {title: "new folder1", folder: true});
                    } else if (key === "Load"){
                        directory.loadDir();
                    } else if (key === "Save"){
                        directory.saveDir();
                        var text = editor.txt.html();
                    } else {
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
            };
        }
    });

    directory.loadDir();

    //---------- editor -----------

    var E = window.wangEditor;
    editor = new E('#div1', '#div2');
    editor.create();
});

/**
 *  当节点类型为文件时禁用创建子文件/文件夹的命令
 */
function  disableFileTypeMenu() {
    return !tree.getActiveNode().isFolder();
}

