var CLIPBOARD = null;
$(function () {  // on page load
    // Create the tree inside the <div id="tree"> element.
    var tree = $("#tree").fancytree({
        extensions: ["edit", "filter", "glyph"],
        source: [
            {title: "Node 1", key: "1"},
            {
                title: "Folder 2", key: "2", folder: true, children: [
                    {title: "Node 2.1", key: "3"},
                    {title: "Node 2.2", key: "4"}
                ]},
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
                case "ctrl+shift+n":
                case "meta+shift+n": // mac: cmd+shift+n
                    cmd = "addChild";
                    break;
                case "ctrl+c":
                case "meta+c": // mac
                    cmd = "copy";
                    break;
                case "ctrl+v":
                case "meta+v": // mac
                    cmd = "paste";
                    break;
                case "ctrl+x":
                case "meta+x": // mac
                    cmd = "cut";
                    break;
                case "ctrl+n":
                case "meta+n": // mac
                    cmd = "addSibling";
                    break;
                case "del":
                case "meta+backspace": // mac
                    cmd = "remove";
                    break;
                case "ctrl+up":
                case "ctrl+shift+up": // mac
                    cmd = "moveUp";
                    break;
                case "ctrl+down":
                case "ctrl+shift+down": // mac
                    cmd = "moveDown";
                    break;
                case "ctrl+right":
                case "ctrl+shift+right": // mac
                    cmd = "indent";
                    break;
                case "ctrl+left":
                case "ctrl+shift+left": // mac
                    cmd = "outdent";
            }
            if (cmd) {
                $(this).trigger("nodeCommand", {cmd: cmd});
                return false;
            }
        });

    $.contextMenu({
        selector: '#tree',
        callback: function (key, options) {
            var m = "clicked: " + key;
            $(this).trigger("nodeCommand", {cmd: options.commands[key].cmd});
        },
        items: {
            "New File": {name: "New File", icon: "fa-check", cmd:"addChild"},
            "New Folder": {name: "New Folder", icon: "fa-folder-o", cmd:"addSibling"},
            "sep1": "---------",
            "Rename": {name: "Rename", icon: "edit", cmd:"rename"},
            "Delete": {name: "Delete", icon: "delete", cmd:"remove"},

        }
    });


});
