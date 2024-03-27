const events = ['onClick', 'onChange', 'onInput', 'onSubmit']

function createElement(tag, attributes = {}, ...children) {
    // create dom element
    const element = document.createElement(tag);

    // set attributes
    for (const attribute in attributes) {
        const value = attributes[attribute];
        if (events.includes(attribute) && typeof value === "function") {
            element.addEventListener(attribute, value);
        }
        else element.setAttribute(attribute, value);
    }

    // append child nodes
    children.forEach(child => {
        const content = (typeof child === "string") ? document.createTextNode(child) : child;
        element.appendChild(content);
    });

    return element;
}

export function div(attributes = {}, ...children) {
    return createElement("div", attributes, ...children);
}