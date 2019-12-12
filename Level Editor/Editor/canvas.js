var now;
var delta;
var interval;
var then = new Date().getTime();

var frames;
var oldTime = 0;

var last = 0;

var mousePosition = {x: 0, y: 0};
var oldMousePosition = mousePosition;

let leftClickDown = false;
let rightClickDown = false;

function loop(time){
    requestAnimationFrame(loop);

    interval = 1000 / 240;
    now = new Date().getTime();
    delta = now - then;

    if (last + 1000 < now) {
        frames = 0;
        last = now;
    }

    if (delta > 0) {
        // update time stuffs
        then = now - (delta % interval);

        // calculate the frames per second
        oldTime = time;


        // call the fn
        // and pass current fps to it

        let graphics = canvas.getContext("2d");

        graphics.imageSmoothingEnabled = false;
        graphics.mozImageSmoothingEnabled = false;
        graphics.webkitImageSmoothingEnabled = false;

        graphics.fillStyle = "#00b0b0";
        graphics.fillRect(0, 0, canvas.width, canvas.height);

        graphics.fillStyle = "#00ffff";
        if (map_type.value === "shift") {
            let roomsHorizontally = parseInt(document.getElementById("rooms_h").value);
            let roomsVertically = parseInt(document.getElementById("rooms_v").value);


            graphics.fillRect(32, 32, 800 * roomsHorizontally, (610 - 1) * roomsVertically);


            graphics.fillStyle = "#B6C176";

            for (let i = 0; i < roomsHorizontally; i++) {
                graphics.fillRect(32 + 800 * i, 32, 32, 609 * roomsVertically);
                graphics.fillRect(800 + 800 * i, 32, 32, 609 * roomsVertically);
            }

            for (let i = 0; i < roomsVertically; i++) {
                graphics.fillRect(32, 32 + (610 - 1) * i, 800 * roomsHorizontally, 32);
                graphics.fillRect(32, 609 + (610 - 1) * i, 800 * roomsHorizontally, 32);
            }//*/

            graphics.fillStyle = "#00ffff";
        }
        else {
            graphics.fillRect(32, 32, canvas.width - 65, canvas.height - 65);
        }


        graphics.lineWidth = 1;
        graphics.strokeStyle = "#509684";

        for (let i = 0; i < canvas.height / 32; i++) {
            graphics.beginPath();
            graphics.moveTo(0, 32 * i + 0.5);
            graphics.lineTo(canvas.width, 32 * i + 0.5);
            graphics.stroke();
        }

        for (let i = 0; i < canvas.width / 32; i++) {
            graphics.beginPath();
            graphics.moveTo(32 * i + 0.5, 0);
            graphics.lineTo(32 * i + 0.5, canvas.height);
            graphics.stroke();
        }

        /*graphics.beginPath();
        graphics.strokeStyle = "#bfad35";
        graphics.arc(95 + Math.random() * 600, 50 + Math.random() * 600, 40 + Math.random() * 70, 0, 2 * Math.PI);
        graphics.stroke();*/

        let gridSize = document.getElementById("gridSize").value;

        let x = Math.floor(mousePosition.x / gridSize) * gridSize;
        let y = Math.floor(mousePosition.y / gridSize) * gridSize;

        if (selectedObject != null) {
            if (leftClickDown) {
                if (objects["x" + x + "_y" + y] == null)
                    objects["x" + x + "_y" + y] = [];

                let add = true;
                for (let i = 0; i < objects["x" + x + "_y" + y].length; i++) {
                    let value = objects["x" + x + "_y" + y][i];

                    if (value[4] === Resources[selectedObject].path) {
                        add = false;
                        break;
                    }
                }
                if (add)
                    objects["x" + x + "_y" + y].push([selectedObject, Resources[selectedObject].type, x, y, Resources[selectedObject].path, objectImage, document.getElementById("custom_id").value]);

            }

            else if (rightClickDown && objects["x" + x + "_y" + y] != null)
                delete objects["x" + x + "_y" + y];
        }

        Object.keys(objects).forEach(function (key, index, array) {
            let value = objects[key];

            for (let i = 0; i < value.length; i++) {
                let tileInfo = value[i];
                if (tileInfo[1] == "SAVES") {
                    graphics.drawImage(tileInfo[5], 0, 0, 32, 32, tileInfo[2], tileInfo[3], 32, 32);
                }
                else {
                    graphics.drawImage(tileInfo[5], tileInfo[2], tileInfo[3]);
                }

            }
        });


        if (objectImage != null) {
            if (Resources[selectedObject].type == "SAVES") {
                graphics.drawImage(objectImage, 0, 0, 32, 32, x, y, 32, 32);
            }
            else {
                graphics.drawImage(objectImage, x, y);
            }

        }

        frames++;

        oldMousePosition = mousePosition;
    }
}


canvas.onmousedown = function(event) {
    var isRightMB;
    event = event || window.event;

    if ("which" in event)  // Gecko (Firefox), WebKit (Safari/Chrome) & Opera
        isRightMB = event.which == 3;
    else if ("button" in event)  // IE, Opera
        isRightMB = event.button == 2;

    if (isRightMB)
        rightClickDown = true;
    else
        leftClickDown = true;
};

canvas.onmouseup = function(event) {
    var isRightMB;
    event = event || window.event;

    if ("which" in event)  // Gecko (Firefox), WebKit (Safari/Chrome) & Opera
        isRightMB = event.which == 3;
    else if ("button" in event)  // IE, Opera
        isRightMB = event.button == 2;

    if (isRightMB)
        rightClickDown = false;
    else
        leftClickDown = false;
};

window.addEventListener('mousemove', function (event) {
    mousePosition = getMousePos(canvas, event);
}, false);


function getMousePos(canvas, evt) {
    let rect = canvas.getBoundingClientRect();
    return {
        x: evt.clientX - rect.left,
        y: evt.clientY - rect.top
    };
}

loop();