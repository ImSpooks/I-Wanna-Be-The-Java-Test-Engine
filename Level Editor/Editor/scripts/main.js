document.addEventListener('contextmenu', event => event.preventDefault());

var canvas = document.getElementById("canvas");

var selectedCategory = Types[0];
var selectedObject;
var objectImage;

var categoriesDiv = document.getElementById("categories");
var itemsDiv = document.getElementById("items");

var lightmode = true;

function initialize() {
    for (let i = 0; i < Types.length; i++) {
        let type = document.createElement("button");
        type.id = Types[i];
        type.innerText = Types[i].capitalize();

        type.onclick = function(event) {
            selectedCategory = Types[i];

            while (itemsDiv.children.length > 0)
                itemsDiv.children[0].remove();

            Object.keys(Resources).forEach(function (key, index, array) {
                let value = Resources[key];

                if (value.type === selectedCategory) {
                    let image = document.createElement("img");
                    image.alt = key;
                    image.id = key;
                    image.classList.add(Types[i]);
                    image.src = "../../Engine/src/main/resources/resources/" + value.path;

                    image.height = 32;
                    image.style.maxHeight = "32px";
                    image.style.maxWidth = "32px";
                    image.style.objectFit  = "cover";
                    image.style.objectPosition  = "left 50%";

                    image.onclick = function(event) {
                        selectedObject = key;
                        objectImage = image;
                    };

                    itemsDiv.appendChild(image);
                }
            });

            itemsDiv.appendChild(addOption("Custom Id (Optional): ", "custom_id", "text", "").parent);

        };

        categoriesDiv.appendChild(type);
    }

    document.getElementById(selectedCategory).onclick();

}

function toggleVisuals() {
    lightmode = !lightmode;
    applyVisuals();
}

function applyVisuals() {
    document.getElementById("mode").href = "css/" + (lightmode ? "lightmode" : "darkmode") + ".css";
}


initialize();
applyVisuals();