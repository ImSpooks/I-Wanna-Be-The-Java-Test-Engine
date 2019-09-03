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