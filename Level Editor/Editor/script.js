
var selectedCategory = Types[0];
var selectedObject;
var objectImage;

var categoriesDiv = document.getElementById("categories");
var itemsDiv = document.getElementById("items");

var currentObjects = {};

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

                if (value.subtype === selectedCategory) {
                    let image = document.createElement("img");
                    image.alt = key;
                    image.id = key;
                    image.classList.add(Types[i]);
                    image.src = "../../Resources/src/main/resources/resources/" + value.path;

                    image.onclick = function(event) {
                        selectedObject = value;
                        objectImage = image;
                    };

                    itemsDiv.appendChild(image);
                }
            });
        };

        categoriesDiv.appendChild(type);
    }

    document.getElementById(selectedCategory).onclick();


}


String.prototype.capitalize = function () {
    return this.substr(0, 1).toUpperCase() + this.substr(1).toLocaleLowerCase();
};


initialize();