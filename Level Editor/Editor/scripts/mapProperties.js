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

                let json = JSON.parse(event.target.result || new ArrayBuffer(0));

                let array = json["objects"];

                let pathToObject = {};
                Object.keys(RESOURCES).forEach(function (key) {
                    pathToObject[RESOURCES[key].path] = key;
                });

                for (let i = 0; i < array.length; i++) {
                    let object = array[i];

                    let selectedObject = pathToObject[object["tile"] || "sprites/blocks/default/sprBlock.png"];

                    if (selectedObject == null)
                        selectedObject = object["tile"] || "sprites/blocks/default/sprBlock.png";

                    let type = object["type"] || "BLOCKS";
                    let x = (object["x"] || 0) + 32;
                    let y = (object["y"] || 0) + 32;
                    let customId = object["custom_id"] || "";

                    let events = [];
                    (object["events"] || []).forEach(function (value) {
                        let conditions = [];
                        (value["conditions"] || []).forEach(function (condition) {
                            conditions.push(valuesToCondition(condition.internalName, condition.type, condition.parameters));
                        });

                        events.push(valuesToEvent(value.internalName, value.type, value.parameters, conditions));
                    });

                    let properties = [];
                    (object["properties"] || []).forEach(function (value) {
                        properties.push(valuesToProperty(value.internalName, value.parameters));
                    });

                    document.getElementById(RESOURCES[selectedObject].type).onclick();

                    if (objects["x" + x + "_y" + y] == null)
                        objects["x" + x + "_y" + y] = [];

                    let deserializedObject = [
                        selectedObject,                             // object
                        type,                                       // type
                        x,                                          // x
                        y,                                          // y
                        RESOURCES[selectedObject].path,             // sprite path
                        document.getElementById(selectedObject),    // rendered image
                        customId,                                   // custom id
                        events,                                     // events
                        properties,                                 // properties
                        object["originalProperties"] || {},         // local properties
                        {}                                          // temp properties
                    ];

                    properties.forEach((value, index) => {
                        let func = value.reference.callback || function (object, value) {};
                        func.call(func, deserializedObject, value.parameters);
                    })

                    objects["x" + x + "_y" + y].push(deserializedObject);
                }

                document.getElementById(loaded).onclick();


                let map_type_json = json["map_type"];

                map_type.value = map_type_json["type"];
                map_type.onchange();

                if (map_type.value === "scrolling") {
                    document.getElementById("room_width").value = map_type_json["width"];
                    document.getElementById("room_height").value = map_type_json["width"];

                    document.getElementById("room_height").onchange();
                }
                else if (map_type.value === "shift") {
                    document.getElementById("rooms_h").value = map_type_json["rooms_horizontal"];
                    document.getElementById("rooms_v").value = map_type_json["rooms_vertical"];

                    document.getElementById("rooms_v").onchange();
                }

                document.getElementById("mapVariables").value = (json["map_variables"] || []).join("|");

                x.value = "";
                framesRendered = 0;
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

    data["map_variables"] = document.getElementById("mapVariables").value.split("|");

    data["objects"] = [];

    Object.keys(objects).forEach(function (key, index, array) {
        let value = objects[key];

        for (let i = 0; i < value.length; i++) {
            let tileInfo = value[i];

            let events = [];
            tileInfo[7].forEach(function (value) {
                events.push(value.toJson());
            });

            let properties = [];
            tileInfo[8].forEach(function (value) {
                properties.push(value.toJson());
            });

            let tile = {
                tile: tileInfo[4],
                type: tileInfo[1],
                x: tileInfo[2] - 32,
                y: tileInfo[3] - 32,
                custom_id: tileInfo[6],
                events: events,
                properties: properties,
                originalProperties: {originalPath: tileInfo[9].originalPath}
            };

            data["objects"].push(tile);
        }
    });

    cleanArray(data, true);
    return JSON.stringify(data, undefined, 4);
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