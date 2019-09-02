let now;
let delta;
let interval;
let then = new Date().getTime();

let frames;
let oldTime = 0;

let last = 0;

let canvas = document.getElementById("canvas");

let objects = {};

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

        graphics.clearRect(0, 0, canvas.width, canvas.height);

        graphics.fillStyle = "#000000";
        graphics.fillRect(0, 0, 871, 701);
        graphics.fillStyle = "#bfad35";

        graphics.beginPath();
        graphics.strokeStyle = "#bfad35";
        graphics.arc(95 + Math.random() * 600, 50 + Math.random() * 600, 40 + Math.random() * 70, 0, 2 * Math.PI);
        graphics.stroke();

        frames++;
    }
}

loop();