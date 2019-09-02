
let selectedCategory = Types[0];
let selectedObject = Resources.sprBlock;

let categoriesDiv = document.getElementById("categories");
let itemsDiv = document.getElementById("items");

function initialize() {
    for (let i = 0; i < Types.length; i++) {
        let type = document.createElement("button");
        type.id = Types[i];
        type.innerText = Types[i].capitalize();

        type.onclick = function(event) {
            selectedCategory = Types[i];
            console.log("test1");

            while (itemsDiv.children.length > 0)
                itemsDiv.children[0].remove();

            Object.keys(Resources).forEach(function (key, index, array) {
                console.log("test2");
                let value = Resources[key];

                if (value.subtype === selectedCategory) {
                    console.log("test3");
                    let image = document.createElement("img");
                    image.alt = key;
                    image.id = key;
                    image.classList.add(Types[i]);
                    image.src = "../../Resources/src/main/resources/resources/" + value.path;

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