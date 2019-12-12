const propertiesDiv = document.getElementById("properties");
var propertyObject;

function applyProperties() {
    // remove every other node
    while (propertiesDiv.firstChild)
        propertiesDiv.removeChild(propertiesDiv.firstChild);

    /**
     * PROPERTIES
     */

    /**
     * EVENTS
     */
    propertiesDiv.appendChild(createElement("div", {id: "addEvent"}, function(addEventDiv) { // callback
        addEventDiv.appendChild(createElement("label", {for: "event_type"}));

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
                            eventDiv.appendChild(createElement("p", {innerText: "Note: "}));
                            eventDiv.appendChild(createElement("span", {innerText: value.note}));
                            eventDiv.appendChild(createElement("br"));
                        }
                        eventDiv.appendChild(createElement("br"));


                        Object.keys(value.parameters).forEach((parameter, index, array) => {
                            eventDiv.appendChild(createElement("span", {innerText: parameter + ": "}));
                            eventDiv.appendChild(createElement("label", {for: parameter}));
                            eventDiv.appendChild(createElement("input", {id: parameter}));
                            eventDiv.appendChild(createElement("br"));
                        });

                        eventDiv.appendChild(createElement("button", {innerText: "Add event", onclick: function () {
                                let params = {};
                                Object.keys(value.parameters).forEach((parameter, index, array) => {
                                     let paramValue = document.getElementById(parameter).value;
                                     let paramType = value.parameters[parameter];

                                     if (paramType.equalsIgnoreCase("number")) {
                                         if (paramValue.contains("."))
                                            paramValue = parseFloat(paramValue);
                                         else paramValue = parseInt(paramValue);
                                     }
                                     else if (parameter.equalsIgnoreCase("boolean")) {
                                         paramValue = paramValue.equalsIgnoreCase("true");
                                     }
                                     params[parameter] = paramValue;
                                });

                                propertyObject[7].push(new GameEvent(subValue, value, params));
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

    propertyObject[7].forEach(function (value, index, array) {
        propertiesDiv.appendChild(createElement("span", {innerText: value.reference.name}));
        propertiesDiv.appendChild(createElement("button", {innerText: "Details", onclick: function () {
                alert("Name: {0}\n Parameters: \n{1}".format(value.reference.name, JSON.stringify(value.arguments, undefined, 4)));
            }}));
        propertiesDiv.appendChild(createElement("button", {innerText: "Remove", onclick: function () {
                alert("TODO");
            }}));
        propertiesDiv.appendChild(createElement("br"));
    });

    propertiesDiv.appendChild(createElement("button", {innerText: "Save", onclick: function () {

        }}));
}


function selectProperties() {
    // remove every other node
    while (propertiesDiv.firstChild)
        propertiesDiv.removeChild(propertiesDiv.firstChild);

    if (propertyObject.length > 1) {
        propertyObject.forEach(function (value, index) {
            propertiesDiv.appendChild(createElement("img", {src: value[5].src, onclick: function () {
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