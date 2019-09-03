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
            if (leftClickDown && currentObjects["x" + x + "_y" + y] == null)
                currentObjects["x" + x + "_y" + y] = [selectedObject, Resources[selectedObject].subtype, x, y, Resources[selectedObject].path, objectImage];
            else if (rightClickDown && currentObjects["x" + x + "_y" + y] != null)
                delete currentObjects["x" + x + "_y" + y];
        }

        Object.keys(currentObjects).forEach(function (key, index, array) {
            let value = currentObjects[key];
            graphics.drawImage(value[5], value[2], value[3]);
        });


        if (objectImage != null) {
            graphics.drawImage(objectImage, x, y);
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