
var EVENTS = {
    OBJECT: {
        moveObject: {
            name: "Move Object Event",
            description: "Move objects with a specific custom ID",
            note: "Can move multiple objects if they have the same ID",
            parameters: {
                objectId: "string",
                velocityX: "number",
                velocityY: "number"
            }
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
                type: ["BLOCK", "KILLERS", "MISC", "PLATFORMS", "SAVES", "SLOPES", "TRIGGERS", "WARPS"],
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
    }
};

var GameEvent = function (internalName, reference, arguments = []) {
    var self = this;
    var local = {};

    self.reference = reference;
    self.internalName = internalName;
    self.arguments = arguments;
};