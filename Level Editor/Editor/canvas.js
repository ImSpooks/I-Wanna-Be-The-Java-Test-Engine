var now;
var delta;
var interval;
var then = new Date().getTime();

var frames;
var oldTime = 0;

var last = 0;

var canvas = document.getElementById("canvas");

var objects = {};

var mousePosition = {x: 0, y: 0};

let mouseDown = false;

function loop(time){
    requestAnimationFrame(loop);

    interval = 1000 / 60;
    now = new Date().getTime();
    delta = now - then;

    if (last + 1000 < now) {
        frames = 0;
        last = now;
    }

    if (delta > interval) {
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
        graphics.fillRect(32, 32, 800, 608);

        graphics.lineWidth = 1;
        graphics.strokeStyle = "#509684";

        for (let i = 0; i < 22; i++) {
            graphics.beginPath();
            graphics.moveTo(0, 32 * i + 0.5);
            graphics.lineTo(canvas.width, 32 * i + 0.5);
            graphics.stroke();
        }

        for (let i = 0; i < 32; i++) {
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

        let x = Math.floor((mousePosition.x) / gridSize) * gridSize;
        let y = Math.floor((mousePosition.y) / gridSize) * gridSize;

        if (mouseDown && currentObjects["x" + x + "_y" + y] == null)
            currentObjects["x" + x + "_y" + y] = [selectedObject.subtype, x, y, selectedObject.path, objectImage];

        Object.keys(currentObjects).forEach(function (key, index, array) {
            let value = currentObjects[key];
            graphics.drawImage(value[4], value[1], value[2]);
        });


        if (objectImage != null) {
            graphics.drawImage(objectImage, x, y);
        }

        frames++;
    }
}


//TODO remove objects https://stackoverflow.com/questions/2405771/is-right-click-a-javascript-event
canvas.onmousedown = function() {
    mouseDown = true;
};

canvas.onmouseup = function() {
    mouseDown = false;
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