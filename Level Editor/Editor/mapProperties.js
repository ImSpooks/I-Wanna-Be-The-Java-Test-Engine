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


function exportMap() {
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

        let tile = {
            tile: value[0],
            type: value[1],
            x: value[2] - 32,
            y: value[3] - 32,
            custom_id: value[6]
        };

        data["objects"].push(tile);
    });

    let json = JSON.stringify(data);
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