// TODO array/json add/remove

const CONDITION = {
    ROOM: { // You can set room variables with the setter events
        // equals
        roomVariableEquals: {
            name: "Room Variable Equals To",
            description: "TODO",
            note: "Returns 'false' when variable couldn't be found.",
            parameters: {
                variableName: "string",
                value: "string",
                type: {
                    type: "string",
                    options: ["string", "byte", "short", "int", "long", "double", "float", "boolean"]
                }
            }
        },
        roomVariableArrayEquals: {
            name: "Room Variable Array Equals To",
            description: "TODO",
            note: "Returns 'false' when variable couldn't be found.",
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
        roomVariableJsonEquals: {
            name: "Room Variable Json Equals To",
            description: "TODO",
            note: "Returns 'false' when variable couldn't be found.",
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
        roomVariableArrayContains: {
            name: "Room Variable Array Contains",
            description: "TODO",
            note: "Returns 'false' when variable couldn't be found.",
            parameters: {
                variableName: "string",
                value: "string",
                type: {
                    type: "string",
                    options: ["string", "byte", "short", "int", "long", "double", "float", "boolean"]
                }
            }
        },
        roomVariableJsonContainsKey: {
            name: "Room Variable Json Contains Key",
            description: "TODO",
            note: "Returns 'false' when variable couldn't be found.",
            parameters: {
                variableName: "string",
                key: "string"
            }
        }
    },
    PLAYER: {
        playerLocationBetween: {
            name: "Player Location Between",
            description: "TODO",
            parameters: [{variableName: "x1", type: "int"}, {variableName: "y1", type: "int"}, {variableName: "x2", type: "int"}, {variableName: "y2", type: "int"}]
        }
    },
    
    GLOBAL: {
        // equals
        globalVariableEquals: {
            name: "Global Variable Equals To",
            description: "TODO",
            note: "Returns 'false' when variable couldn't be found.",
            parameters: {
                variableName: "string",
                value: "string",
                type: {
                    type: "string",
                    options: ["string", "byte", "short", "int", "long", "double", "float", "boolean"]
                }
            }
        },
        globalVariableArrayEquals: {
            name: "Global Variable Array Equals To",
            description: "TODO",
            note: "Returns 'false' when variable couldn't be found.",
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
        globalVariableJsonEquals: {
            name: "Global Variable Json Equals To",
            description: "TODO",
            note: "Returns 'false' when variable couldn't be found.",
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
        globalVariableArrayContains: {
            name: "Global Variable Array Contains",
            description: "TODO",
            note: "Returns 'false' when variable couldn't be found.",
            parameters: {
                variableName: "string",
                value: "string",
                type: {
                    type: "string",
                    options: ["string", "byte", "short", "int", "long", "double", "float", "boolean"]
                }
            }
        },
        globalVariableJsonContainsKey: {
            name: "Global Variable Json Contains Key",
            description: "TODO",
            note: "Returns 'false' when variable couldn't be found.",
            parameters: {
                variableName: "string",
                key: "string"
            }
        }
    }
};

var Condition = function (internalName, type, reference, parameters = []) {
    var self = this;
    var local = {};

    self.internalName = internalName;
    self.type = type;
    self.reference = reference;
    self.parameters = parameters;

    self.toJson = function () {
        return {type: self.type, internalName: self.internalName, parameters: self.parameters};
    };
};

function valuesToCondition(internalName, type, parameters) {
    return new Condition(internalName, type, CONDITION[type][internalName], parameters);
}