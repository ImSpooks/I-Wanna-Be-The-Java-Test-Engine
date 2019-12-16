const parentPropertiesDiv = document.getElementById("properties");
var propertyObject;

var oldEventValue = {type: "OBJECT", value: "moveObject"};
var oldConditionValue = {type: "ROOM", value: "roomVariableEquals"};
var oldProperties = {value: "texture"};
var currentConditions = [];

function applyProperties() {
    // remove every other node
    while (parentPropertiesDiv.firstChild)
        parentPropertiesDiv.removeChild(parentPropertiesDiv.firstChild);

    /**
     * EVENTS
     */
    addEvent(parentPropertiesDiv);


    /**
     * PROPERTIES
     */
    addProperties(parentPropertiesDiv);

    /**
     * OTHER
     */
    parentPropertiesDiv.appendChild(createElement("p", {innerText: "Custom ID: ", style: {display: "inline"}}));
    parentPropertiesDiv.appendChild(createElement("input"));
}

function addEvent(div) {
    let eventDiv;
    div.appendChild(eventDiv = createElement("div", {style: {display: "inline-block"}, classes: ["property"]}));
    eventDiv.appendChild(createElement("div", {id: "addEvent"}, function(addEventDiv) { // callback
        addEventDiv.appendChild(createElement("label", {for: "eventType"}));

        addEventDiv.appendChild(createElement("select", {id: "eventType", name: "Event Type", onchange: function () {
                    if (document.getElementById("sub_type") != null)
                        document.getElementById("sub_type").remove();

                    if (document.getElementById("eventDiv") != null)
                        document.getElementById("eventDiv").remove();

                    let eventValue = document.getElementById("eventType").value;

                    addEventDiv.appendChild(createElement("label", {for: "sub_type"}));
                    addEventDiv.appendChild(createElement("select", {id: "sub_type", name: "Sub Event", onchange: function () {
                            let eventDiv = document.getElementById("eventDiv") || createElement("div", {id: "eventDiv", style: {marginLeft: "5px", padding: "3px"}});

                            while (eventDiv.firstChild)
                                eventDiv.removeChild(eventDiv.firstChild);

                            let subValue = document.getElementById("sub_type").value;

                            let value = EVENTS[eventValue][subValue];
                            if (value.hasOwnProperty("description")) {
                                eventDiv.appendChild(createElement("span", {innerText: value.description}));
                                eventDiv.appendChild(createElement("br"));
                            }

                            if (value.hasOwnProperty("note")) {
                                eventDiv.appendChild(createElement("p", {innerText: "Note: " + value.note}));
                                eventDiv.appendChild(createElement("br"));
                            }

                            Object.keys(value.parameters).forEach((parameter, index, array) => {
                                eventDiv.appendChild(createElement("span", {innerText: parameter + ": "}));
                                eventDiv.appendChild(createElement("label", {for: "event_" + parameter}));

                                if (value.parameters[parameter].constructor === ({}).constructor){ // check if is json
                                    eventDiv.appendChild(createElement("select", {id: "event_" + parameter, name: "Sub Event"}, function (select) {
                                        value.parameters[parameter].options.forEach(function (value) {
                                            select.appendChild(createElement("option", {value: value, innerText: (value + "").capitalize()}));
                                        });
                                    }));
                                } else {
                                    eventDiv.appendChild(createElement("input", {id: "event_" + parameter}));
                                }

                                eventDiv.appendChild(createElement("br"));
                            });

                            eventDiv.appendChild(createElement("br"));
                            currentConditions = [];
                            eventDiv.appendChild(createElement("span", {innerText: "Conditions:"}));
                            let conditionDivParent = createElement("div");
                            eventDiv.appendChild(conditionDivParent);

                            addConditions(conditionDivParent);
                            eventDiv.appendChild(createElement("br"));


                            oldEventValue.type = eventValue;
                            oldEventValue.value = subValue;

                            eventDiv.appendChild(createElement("button", {innerText: "Add event", onclick: function () {
                                    let params = {};
                                    Object.keys(value.parameters).forEach((parameter, index, array) => {
                                        let paramValue = document.getElementById("event_" + parameter).value;
                                        let paramType = (value.parameters[parameter].constructor !== ({}).constructor ? value.parameters[parameter] : value.parameters[parameter].type) || "string";

                                        params[parameter] = parseValue(paramType, paramValue);
                                    });

                                    propertyObject[7].push(new GameEvent(subValue, eventValue, value, params, currentConditions));
                                    applyProperties();
                                }}));

                            addEventDiv.appendChild(eventDiv);
                        }}, function (select) {
                        Object.keys(EVENTS[eventValue]).forEach((type, index, array) => {
                            select.appendChild(createElement("option", {value: type, innerText: EVENTS[eventValue][type].name}));
                        });
                    }));


                }},
            function (select) { // callback
                Object.keys(EVENTS).forEach((value, index, array) => {
                    select.appendChild(createElement("option", {value: value, innerText: value.capitalize()}));
                });
            }));
    }));

    document.getElementById("eventType").value = oldEventValue.type;
    document.getElementById("eventType").onchange(null);

    document.getElementById("sub_type").value = oldEventValue.value;
    document.getElementById("sub_type").onchange(null);

    propertyObject[7].forEach(function (value, index) {
        eventDiv.appendChild(createElement("span", {innerText: value.reference.name}));

        eventDiv.appendChild(createElement("button", {innerText: "Details", onclick: function () {
                let conditions = [];
                value.conditions.forEach(condition => {
                    console.log("value.conditions = " + JSON.stringify(condition, undefined, 4));
                    console.log("value.conditions.internalName = " + JSON.stringify(condition.internalName, undefined, 4));
                    console.log("value.conditions.reference = " + JSON.stringify(condition.reference, undefined, 4));
                    console.log("value.conditions.reference = " + JSON.stringify(condition.reference, undefined, 4));

                     conditions.push({name: condition.reference.name, parameters: condition.parameters});
                });

                alert("Name: {0}\n Parameters: \n{1}\nConditions:\n{2}".format(value.reference.name, JSON.stringify(value.parameters, undefined, 4), JSON.stringify(conditions)));
            }, style: {marginLeft: "5px"}}));

        eventDiv.appendChild(createElement("button", {innerText: "Change position", onclick: function () {
                let value;
                while (isNaN(value = parseInt(prompt("New position for event")))) {
                    alert("Input must be an integer.");
                }

                propertyObject[7].moveObject(index, value);
                applyProperties();
            }}));

        eventDiv.appendChild(createElement("button", {innerText: "Remove", onclick: function () {
                propertyObject[7].splice(index, 1);
                applyProperties();
            }}));
        eventDiv.appendChild(createElement("br"));
    });



    document.getElementById("conditionType").value = oldConditionValue.type;
    document.getElementById("conditionType").onchange(null);

    document.getElementById("sub_condition").value = oldConditionValue.value;
    document.getElementById("sub_condition").onchange(null);

    const conditionDivParent = document.getElementById("parentCondition") || document.createElement("div");

    currentConditions.forEach(function (value, index) {
        conditionDivParent.appendChild(createElement("span", {innerText: value.reference.name}));

        conditionDivParent.appendChild(createElement("button", {innerText: "Details", onclick: function () {
                alert("Name: {0}\n Parameters: \n{1}".format(value.reference.name, JSON.stringify(value.parameters, undefined, 4)));
            }, style: {marginLeft: "5px"}}));

        conditionDivParent.appendChild(createElement("button", {innerText: "Remove", onclick: function () {
                propertyObject[7].splice(index, 1);
                addConditions(div);
            }}));
        conditionDivParent.appendChild(createElement("br"));
    });
}

function addProperties(div) {
    let propertiesDiv;
    div.appendChild(propertiesDiv = createElement("div", {style: {display: "inline-block"}, classes: ["property"]}));

    propertiesDiv.appendChild(createElement("label", {for: "propertyType"}));
    propertiesDiv.appendChild(createElement("select", {id: "propertyType", name: "Property Type", onchange: function () {
            let initPropertyDiv = document.getElementById("initPropertyDiv") || createElement("div", {id: "initPropertyDiv", style: {marginLeft: "5px", padding: "3px"}});

            while (initPropertyDiv.firstChild)
                initPropertyDiv.removeChild(initPropertyDiv.firstChild);

            let subValue = document.getElementById("propertyType").value;

            let value = PROPERTIES[subValue];
            if (value.hasOwnProperty("description")) {
                initPropertyDiv.appendChild(createElement("span", {innerText: value.description}));
                initPropertyDiv.appendChild(createElement("br"));
            }

            if (value.hasOwnProperty("note")) {
                eventDiv.appendChild(createElement("p", {innerText: "Note: " + value.note}));
                initPropertyDiv.appendChild(createElement("br"));
            }

            Object.keys(value.parameters).forEach((parameter, index, array) => {
                initPropertyDiv.appendChild(createElement("span", {innerText: parameter + ": "}));
                initPropertyDiv.appendChild(createElement("label", {for: "property_" + parameter}));

                if (value.parameters[parameter].constructor === ({}).constructor){ // check if is json
                    initPropertyDiv.appendChild(createElement("select", {id: "property_" + parameter, name: "Sub Property"}, function (select) {
                        value.parameters[parameter].options.forEach(function (value) {
                            select.appendChild(createElement("option", {value: value, innerText: (value + "").capitalize()}));
                        });
                    }));
                } else {
                    initPropertyDiv.appendChild(createElement("input", {id: "property_" + parameter}));
                }

                initPropertyDiv.appendChild(createElement("br"));
            });

            oldProperties.value = subValue;

            initPropertyDiv.appendChild(createElement("button", {innerText: "Add property", onclick: function () {
                    let params = {};
                    Object.keys(value.parameters).forEach((parameter, index, array) => {
                        let paramValue = document.getElementById("property_" + parameter).value;
                        let paramType = (value.parameters[parameter].constructor !== ({}).constructor ? value.parameters[parameter] : value.parameters[parameter].type) || "string";

                        params[parameter] = parseValue(paramType, paramValue);
                    });

                    propertyObject[8].push(new Property(subValue, value, params));

                    let func = value.callback || function (object, value) {};
                    func.call(func, propertyObject, params);

                    applyProperties();
                }}));

            propertiesDiv.appendChild(initPropertyDiv);

        }}, function (select) {
        Object.keys(PROPERTIES).forEach((value, index, array) => {
            select.appendChild(createElement("option", {value: value, innerText: value.capitalize()}));
        });
    }));

    document.getElementById("propertyType").value = oldProperties.value;
    document.getElementById("propertyType").onchange(null);

    propertyObject[8].forEach(function (value, index) {
        propertiesDiv.appendChild(createElement("span", {innerText: value.reference.name}));

        propertiesDiv.appendChild(createElement("button", {innerText: "Details", onclick: function () {
                alert("Name: {0}\n Parameters: \n{1}".format(value.reference.name, JSON.stringify(value.parameters, undefined, 4)));
            }, style: {marginLeft: "5px"}}));

        propertiesDiv.appendChild(createElement("button", {innerText: "Remove", onclick: function () {
                let func = value.reference.removeCallback || function (object) {};
                func.call(func, propertyObject);

                propertyObject[8].splice(index, 1);
                applyProperties();
            }}));
        propertiesDiv.appendChild(createElement("br"));
    });
}

function addConditions(div) {
    while(div.firstChild) div.removeChild(div.firstChild);

    let conditionDiv;
    div.appendChild(conditionDiv = createElement("div", {style: {display: "inline-block"}, classes: ["property"]}));
    conditionDiv.appendChild(createElement("div", {id: "addCondition"}, function(addConditionDiv) { // callback
        addConditionDiv.appendChild(createElement("label", {for: "conditionType"}));

        addConditionDiv.appendChild(createElement("select", {id: "conditionType", name: "Condition Type", onchange: function () {
                    if (document.getElementById("sub_condition") != null)
                        document.getElementById("sub_condition").remove();

                    if (document.getElementById("conditionDiv") != null)
                        document.getElementById("conditionDiv").remove();

                    let conditionValue = document.getElementById("conditionType").value;

                    addConditionDiv.appendChild(createElement("label", {for: "sub_condition"}));
                    addConditionDiv.appendChild(createElement("select", {id: "sub_condition", name: "Sub Condition", onchange: function () {
                            let conditionDiv = document.getElementById("conditionDiv") || createElement("div", {id: "conditionDiv", style: {marginLeft: "5px", padding: "3px"}});

                            while (conditionDiv.firstChild)
                                conditionDiv.removeChild(conditionDiv.firstChild);

                            let subValue = document.getElementById("sub_condition").value;

                            let value = CONDITION[conditionValue][subValue];
                            if (value.hasOwnProperty("description")) {
                                conditionDiv.appendChild(createElement("span", {innerText: value.description}));
                                conditionDiv.appendChild(createElement("br"));
                            }

                            if (value.hasOwnProperty("note")) {
                                conditionDiv.appendChild(createElement("p", {innerText: "Note: " + value.note}));
                                conditionDiv.appendChild(createElement("br"));
                            }

                            Object.keys(value.parameters).forEach((parameter, index, array) => {
                                conditionDiv.appendChild(createElement("span", {innerText: parameter + ": "}));
                                conditionDiv.appendChild(createElement("label", {for: "condition_" + parameter}));

                                if (value.parameters[parameter].constructor === ({}).constructor){ // check if is json
                                    conditionDiv.appendChild(createElement("select", {id: "condition_" + parameter, name: "Sub Condition"}, function (select) {
                                        value.parameters[parameter].options.forEach(function (value) {
                                            select.appendChild(createElement("option", {value: value, innerText: (value + "").capitalize()}));
                                        });
                                    }));
                                } else {
                                    conditionDiv.appendChild(createElement("input", {id: "condition_" + parameter}));
                                }

                                conditionDiv.appendChild(createElement("br"));
                            });


                            oldConditionValue.type = conditionValue;
                            oldConditionValue.value = subValue;

                            conditionDiv.appendChild(createElement("button", {innerText: "Add condition", onclick: function () {
                                    let params = {};
                                    Object.keys(value.parameters).forEach((parameter, index, array) => {
                                        let paramValue = document.getElementById("condition_" + parameter).value;
                                        let paramType = (value.parameters[parameter].constructor !== ({}).constructor ? value.parameters[parameter] : value.parameters[parameter].type) || "string";

                                        params[parameter] = parseValue(paramType, paramValue);
                                    });

                                    console.log("value = [{0}][{1}]".format(conditionValue, subValue));
                                    console.log("value = " + JSON.stringify(value));

                                    currentConditions.push(new Condition(subValue, conditionValue, value, params));
                                    addConditions(div);
                                }}));

                            addConditionDiv.appendChild(conditionDiv);
                        }}, function (select) {
                        Object.keys(CONDITION[conditionValue]).forEach((type, index, array) => {
                            select.appendChild(createElement("option", {value: type, innerText: CONDITION[conditionValue][type].name}));
                        });
                    }));


                }},
            function (select) { // callback
                Object.keys(CONDITION).forEach((value, index, array) => {
                    select.appendChild(createElement("option", {value: value, innerText: value.capitalize()}));
                });
            }));
    }));

    try {
        document.getElementById("conditionType").value = oldConditionValue.type;
        document.getElementById("conditionType").onchange(null);

        document.getElementById("sub_condition").value = oldConditionValue.value;
        document.getElementById("sub_condition").onchange(null);
    } catch (ignored) {}

    try {
        div.appendChild(createElement("br"));
        currentConditions.forEach(function (value, index) {
            div.appendChild(createElement("span", {innerText: value.reference.name}));

            div.appendChild(createElement("button", {innerText: "Details", onclick: function () {
                    alert("Name: {0}\n Parameters: \n{1}".format(value.reference.name, JSON.stringify(value.parameters, undefined, 4)));
                }, style: {marginLeft: "5px"}}));

            div.appendChild(createElement("button", {innerText: "Remove", onclick: function () {
                    propertyObject[7].splice(index, 1);
                    addConditions(div);
                }}));
            div.appendChild(createElement("br"));
        });
    } catch (ignored) {}
}

function parseValue(type, value) {
    switch (type.toLowerCase()) {
        case "string":  return value;
        case"number":
            if (value.contains("."))
                return parseFloat(value);
            else return parseInt(value);
        case "byte":    return clamp(parseInt(value), -128, 127); // 8 bit signed
        case "short":   return clamp(parseInt(value), -32768, 32767); // 16 bit signed
        case "int":
        case "integer": return clamp(parseInt(value), -2147483648, 2147483647); // 32 bit signed
        case "long":    return clamp(parseInt(value), -9223372036854775808, 9223372036854775807); // 64 bit signed
        case "float":   return clamp(parseFloat(value), -8388608, 8388607); // 24 bit signed
        case "double":  return clamp(parseFloat(value), -9007199254740992, 9007199254740991); // 54 bit signed
        case "boolean": return  value.equalsIgnoreCase("true");
        case "array":
        case "json":
        case "object":
        default:        return JSON.parse(value);
    }
}

function selectProperties() {
    // remove every other node
    while (parentPropertiesDiv.firstChild)
        parentPropertiesDiv.removeChild(parentPropertiesDiv.firstChild);

    if (propertyObject.length > 1) {
        propertyObject.forEach(function (value, index) {
            parentPropertiesDiv.appendChild(createElement("img", {src: value[5].src, onclick: function () {
                    propertyObject = propertyObject[index];
                    applyProperties();
                }}));
        });
    }
    else {
        propertyObject = propertyObject[0];
        applyProperties();
    }
}