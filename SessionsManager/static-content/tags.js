const events = ['onClick', 'onChange', 'onInput', 'onSubmit']

function createElement(tag, attributes = {}, ...children) {
    //create dom element
    const element = document.createElement(tag);

    //set attributes
    for (const attribute in attributes) {
        const value = attributes[attribute];
        if (events.includes(attribute) && typeof value === "function")
            element.addEventListener(attribute.substring(2).toLowerCase(), value);
        else if (attribute == 'disabled' && value == false)
            element.removeAttribute(attribute);
        else element.setAttribute(attribute, value);
    }

    //append child nodes
    children.forEach(child => {
        const content = (typeof child === "string") ? document.createTextNode(child) : child;
        element.appendChild(content);
    });

    return element;
}

export function div(attributes = {}, ...children) {
    return createElement("div", attributes, ...children);
}

export function a(attributes = {}, ...children) {
    return createElement("a", attributes, ...children);
}

export function ul(attributes = {}, ...children) {
    return createElement("ul", attributes, ...children);
}

export function li(attributes = {}, ...children) {
    return createElement("li", attributes, ...children);
}

export function label(attributes = {}, ...children) {
    return createElement("label", attributes, ...children);
}

export function input(attributes = {}, ...children) {
    return createElement("input", attributes, ...children);
}

export function button(attributes = {}, ...children) {
    return createElement("button", attributes, ...children);
}

export function br(attributes = {}, ...children) {
    return createElement("br", attributes, ...children);
}

export function p(attributes = {}, ...children) {
    return createElement("p", attributes, ...children);
}

export function radioButton(attributes = {}, ...children) {
    return createElement("input", { type: "radio", ...attributes }, ...children);
}