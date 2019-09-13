var objects = {};

// map type change
var map_type = document.getElementById("map_type");
var map_properties = document.getElementById("map_properties");

map_type.onchange = function (event) {
    resetMap();

    switch (map_type.value) {
        default:
        case "normal": {
            normalProperties();
            break;
        }

        case "scrolling": {
            scrollingProperties();
            break;
        }

        case "shift": {
            shiftProperties();
            break;
        }
    }
};

map_type.onchange();

function importMap() {
    let x = document.getElementById("file");

    if ('files' in x) {
        if (x.files.length === 1) {
            let file = x.files[0];
            let name = file.name;

            const reader = new FileReader();

            reader.onload = function(event) {
                objects = {};

                let loaded = selectedCategory;

                let result = event.target.result;

                let json = JSON.parse(result);

                let array = json["objects"];

                for (let i = 0; i < array.length; i++) {
                    let object = array[i];

                    let selectedObject = object["tile"];
                    let type = object["type"];
                    let x = object["x"] + 32;
                    let y = object["y"] + 32;
                    let customId = object["custom_id"];

                    document.getElementById(type).onclick();

                    if (objects["x" + x + "_y" + y] == null)
                        objects["x" + x + "_y" + y] = [];

                    objects["x" + x + "_y" + y].push([selectedObject, type, x, y, Resources[selectedObject].path, document.getElementById(selectedObject), customId]);
                }

                document.getElementById(loaded).onclick();


                let map_type_json = json["map_type"];

                map_type.value = map_type_json["type"];
                map_type.onchange();

                if (map_type.value == "scrolling") {
                    document.getElementById("room_width").value = map_type_json["width"];
                    document.getElementById("room_height").value = map_type_json["width"];

                    document.getElementById("room_height").onchange();
                }
                else if (map_type.value == "shift") {
                    document.getElementById("rooms_h").value = map_type_json["rooms_horizontal"];
                    document.getElementById("rooms_v").value = map_type_json["rooms_vertical"];

                    document.getElementById("rooms_v").onchange();
                }

                x.value = "";
            };

            reader.readAsText(file);
        }
    }
}


function exportMap() {
    let json = exportJson();
    let blob = new Blob([json], {type: "octet/stream"});
    let url = window.URL.createObjectURL(blob);

    let a = document.createElement("a");
    a.style.display = "none";
    document.body.appendChild(a);
    a.href = url;
    a.download = document.getElementById("internalName").value + ".json";
    a.click();
    window.URL.revokeObjectURL(url);
    a.remove();
}

function set() {
    document.getElementById("exportTextOutput").removeAttribute("readonly");
    document.getElementById("exportTextOutput").value = exportJson();
    document.getElementById("exportTextOutput").setAttribute("readonly", true);
}

function exportText() {
    document.getElementById("exportTextOutput").style.display = "block";
    set();
}

function hideOutput() {
    document.getElementById("exportTextOutput").style.display = "none";
}


function copyToClipboard() {
    exportText();

    let element = document.getElementById("exportTextOutput");

    element.select();
    element.setSelectionRange(0, 99999);

    document.execCommand("copy");

    hideOutput();
}

function exportJson() {
    let data = {};

    switch (map_type.value) {
        default:
        case "normal": {
            data["map_type"] = {type: "normal"};
            break;
        }

        case "scrolling": {
            data["map_type"] = {type: "scrolling",
                width: parseInt(document.getElementById("room_width").value),
                height: parseInt(document.getElementById("room_height").value)};
            break;
        }

        case "shift": {
            data["map_type"] = {type: "shift",
                rooms_horizontal: parseInt(document.getElementById("rooms_h").value),
                rooms_vertical: parseInt(document.getElementById("rooms_v").value)};
            break;
        }
    }

    data["objects"] = [];

    Object.keys(objects).forEach(function (key, index, array) {
        let value = objects[key];

        for (let i = 0; i < value.length; i++) {
            let tileInfo = value[i];

            let tile = {
                tile: tileInfo[0],
                type: tileInfo[1],
                x: tileInfo[2] - 32,
                y: tileInfo[3] - 32,
                custom_id: tileInfo[6]
            };

            data["objects"].push(tile);
        }
    });

    return JSON.stringify(data);
}

function resetMap() {
    while (map_properties.children.length > 0)
        map_properties.children[0].remove();
}

function normalProperties() {
    canvas.width = 800 + 64;
    canvas.height = 610 + 64;
}


function scrollingProperties() {
    let width = addOption("Room width: ", "room_width", "number", 800);
    let height = addOption("Room height: ", "room_height", "number", 610);

    width.inputElement.onchange = height.inputElement.onchange = function () {
        canvas.width = parseInt(width.inputElement.value) + 64;
        canvas.height = parseInt(height.inputElement.value) + 64;
    };

    map_properties.appendChild(width.parent);
    map_properties.appendChild(height.parent);

    width.inputElement.onchange();
}

function shiftProperties() {
    let horizontal = addOption("Rooms horizontally: ", "rooms_h", "number", 2);
    let vertical = addOption("Room vertically: ", "rooms_v", "number", 1);

    horizontal.inputElement.onchange = vertical.inputElement.onchange = function () {
        canvas.width = parseInt(horizontal.inputElement.value * 800) + 64;
        canvas.height = parseInt(vertical.inputElement.value * 610) + 64;
    };

    map_properties.appendChild(horizontal.parent);
    map_properties.appendChild(vertical.parent);

    horizontal.inputElement.onchange();
}

function addOption(text, id, type, defaultValue) {
    let p = document.createElement("p");
    p.innerText = text;

    let input = document.createElement("input");
    input.id = id;
    input.type = type;
    input.value = defaultValue;

    p.appendChild(input);

    return {parent: p, inputElement: input};
}