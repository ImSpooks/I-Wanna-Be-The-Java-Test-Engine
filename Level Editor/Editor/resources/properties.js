const PROPERTIES = {
    texture: {
        name: "Texture",
        description: "Change the texture of an object",
        parameters: {
            source: "string"
        },
        callback: function (object, value) {
            object[4] = value.source;
            Object.keys(RESOURCES).forEach((key, index, array) => {
                if (RESOURCES[key].path.equalsIgnoreCase(value.source)) {
                    if (document.getElementById(key) != null) {
                        object[5] = document.getElementById(key);
                    } else {
                        let image = new Image();
                        image.src = "../../Engine/src/main/resources/resources/" + RESOURCES[key].path;
                        object[5] = image;
                    }

                    if (hasProperty(object, "visible")) {
                        object[10].opacity = 0.5;
                    }
                }
            });
        },
        removeCallback: function (object) {
            object[4] = object[9].originalPath;

            if (!object[9].hasOwnProperty("originalImage")) {
                let image = new Image();
                image.src = "../../Engine/src/main/resources/resources/" + object[9].originalPath;
                object[9].originalImage = image;
            }
            object[5] = object[9].originalImage;

            if (hasProperty(object, "visible")) {
                object[10].opacity = 0.5;
            }
        }
    },
    visible: {
        name: "Visible",
        description: "Make an object visible or invisible.",
        parameters: {
            visible: "boolean"
        },
        callback: function (object, value) {
            if (!value.visible) {
                object[10].opacity = 0.5;
            } else {
                delete object[10].opacity;
            }
        },
        removeCallback: function (object) {
            delete object[10].opacity;
        }
    }
};

var Property = function (internalName, reference, parameters = []) {
    var self = this;
    var local = {};

    self.internalName = internalName;
    self.reference = reference;
    self.parameters = parameters;

    self.toJson = function () {
        return {internalName: self.internalName, parameters: self.parameters};
    };
};

function valuesToProperty(internalName, parameters) {
    return new Property(internalName, PROPERTIES[internalName], parameters);
}

function hasProperty(object, property) {
    for (let i = 0; i < object[8].length; i++) {
        if (object[8][i].internalName.equalsIgnoreCase(property)) {
            return true;
        }
    }
    return false;
}