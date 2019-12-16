const EVENTS = {
    OBJECT: {
        moveObject: {
            name: "Move Object Event",
            description: "Move objects with a specific custom ID",
            note: "Can move multiple objects if they have the same ID",
            parameters: {
                objectId: "string",
                velocityX: "number",
                velocityY: "number"
            },
        },
        teleportObject: {
            name: "Teleport Object Event",
            description: "Teleport any object with a specific custom ID",
            note: "If there are any other objects with the same ID, it will teleport to the same location",
            parameters: {
                objectId: "string",
                x: "number",
                y: "number"
            }
        },
        hideObject: {
            name: "Hide Object Event",
            description: "Hide objects with a specific custom ID",
            note: "Can hide multiple objects if they have the same ID",
            parameters: {
                objectId: "string"
            }
        },
        showObject: {
            name: "Hide Object Event",
            description: "Hide objects with a specific custom ID",
            note: "Can hide multiple objects if they have the same ID",
            parameters: {
                objectId: "string"
            }
        },
        spawnObject: {
            name: "Spawn Object Event",
            description: "Spawn an object",
            parameters: {
                type: {
                    type: "string",
                    options: ["BLOCK", "KILLERS", "MISC", "PLATFORMS", "SAVES", "SLOPES", "TRIGGERS", "WARPS", "ENEMY"]
                },
                path: "string",
                x: "number",
                y: "number",
                customId: "string"
            }
        }
    },

    // Player
    PLAYER: {
        teleportPlayer: {
            name: "Teleport Player Event",
            description: "Teleport the player to a new location",
            parameters: {
                x: "number",
                y: "number"
            }
        },
        warpPlayer: {
            name: "Warp Player Event",
            description: "Teleport the player to another room",
            parameters: {
                roomId: "string"
            }
        }
    },

    // Audio
    AUDIO: {
        playAudio: {
            name: "Play Audio Event",
            description: "Play any audio file",
            parameters: {
                name: "string",
                path: "string"
            }
        },
        playSfx: {
            name: "Play Sfx Event",
            description: "Play any audio file",
            parameters: {
                path: "string"
            }
        }
    },

    // Variables
    VARIABLES: {
        setRoomVariable: {
            name: "Set Room Variable",
            description: "TODO",
            note: "Throws NullPointerException when variable couldn't be found.",
            parameters: {
                variableName: "string",
                value: "string",
                type: {
                    type: "string",
                    options: ["string", "byte", "short", "int", "long", "double", "float", "boolean", "array", "json"]
                }
            }
        },
        setRoomArrayVariable: {
            name: "Set Room Array Variable",
            description: "TODO",
            note: "Throws NullPointerException when variable couldn't be found.",
            parameters: {
                variableName: "string",
                index: "int",
                value: "string",
                type: {
                    type: "string",
                    options: ["string", "byte", "short", "int", "long", "double", "float", "boolean"]
                }
            }
        },
        setRoomJsonVariable: {
            name: "Set Room Json Variable",
            description: "TODO",
            note: "Throws NullPointerException when variable couldn't be found.",
            parameters: {
                variableName: "string",
                key: "string",
                value: "string",
                type: {
                    type: "string",
                    options: ["string", "byte", "short", "int", "long", "double", "float", "boolean"]
                }
            }
        },
        setGameVariable: {
            name: "Set Game Variable",
            description: "TODO",
            note: "Throws NullPointerException when variable couldn't be found. <i>Uses Java Reflection, so performance isnt optimal</i>",
            parameters: {
                variableName: "string",
                value: "string",
                type: {
                    type: "string",
                    options: ["string", "byte", "short", "int", "long", "double", "float", "boolean", "array", "json"]
                }
            }
        },
        setGameArrayVariable: {
            name: "Set Game Array Variable",
            description: "TODO",
            note: "Throws NullPointerException when variable couldn't be found. <i>Uses Java Reflection, so performance isnt optimal</i>",
            parameters: {
                variableName: "string",
                index: "int",
                value: "string",
                type: {
                    type: "string",
                    options: ["string", "byte", "short", "int", "long", "double", "float", "boolean"]
                }
            }
        },
        setGameJsonVariable: {
            name: "Set Game Json Variable",
            description: "TODO",
            note: "Throws NullPointerException when variable couldn't be found. <i>Uses Java Reflection, so performance isnt optimal</i>",
            parameters: {
                variableName: "string",
                key: "string",
                value: "string",
                type: {
                    type: "string",
                    options: ["string", "byte", "short", "int", "long", "double", "float", "boolean"]
                }
            }
        }
    }
};

var GameEvent = function (internalName, type, reference, parameters = [], conditions = []) {
    var self = this;
    var local = {};

    self.internalName = internalName;
    self.type = type;
    self.reference = reference;
    self.parameters = parameters;
    self.conditions = conditions;


    self.toJson = function () {
        let conditions = [];
        self.conditions.forEach(value => conditions.push(value.toJson()));
        return {type: self.type, internalName: self.internalName, parameters: self.parameters, conditions: conditions};
    };
};

function valuesToEvent(internalName, type, parameters, events, conditions) {
    return new GameEvent(internalName, type, EVENTS[type][internalName], parameters, events, conditions);
}