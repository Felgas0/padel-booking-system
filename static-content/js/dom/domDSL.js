import {LogError} from "../errorUtils.js";

/**
 * Creates an HTML element.
 *
 * @param {string} tag element tag
 * @param {Object | Promise<HTMLElement> | HTMLElement | string} [attributes] element attributes or an element child
 * @param {Promise<HTMLElement> | HTMLElement | string} [children] element children
 *
 * @returns Promise<HTMLElement>
 */
export async function createElement(tag, attributes, ...children) {
    /** @type {HTMLElement} */
    const element = document.createElement(tag);

    attributes = await attributes;

    if (isElement(attributes) || typeof attributes === "string")
        appendChild(element, attributes);

    else if (attributes != null && typeof attributes === "object")
        setAttributes(element, attributes);

    else if (attributes != null)
        throw new LogError("Invalid attributes for createElement");

    for (let child of children) {
        child = await child;

        if (child != null && (isElement(child) || typeof child === "string"))
            appendChild(element, child);

        else if (child != null)
            throw new LogError("Invalid child:", child, "for element:", element);
    }

    return element;
}

/**
 * @param {HTMLElement} element
 * @param {string | HTMLElement} child
 */
function appendChild(element, child) {
    if (typeof child === "string")
        element.appendChild(document.createTextNode(child));
    else
        element.appendChild(child);
}

/**
 * @param {HTMLElement} element
 * @param {Object} attributes
 */
function setAttributes(element, attributes) {
    for (const [attr, value] of Object.entries(attributes)) {
        if (value == null) continue;

        // onClick, onChange, onInput, onMouseover, etc
        if (typeof value === "function" && attr.startsWith("on") && attr.length > 2) {
            const eventName = attr.slice(2).toLowerCase(); // "onChange" -> "change"
            element.addEventListener(eventName, value);
            continue;
        }

        // boolean attributes (disabled, checked, etc)
        if (value === true) {
            element.setAttribute(attr, "");
            continue;
        }
        if (value === false) continue;

        if (attr === "class") {
            element.className = value;
            continue;
        }
        if (attr === "style" && typeof value === "object") {
            Object.assign(element.style, value);
            continue;
        }

        element.setAttribute(attr, value);
    }
}


/**
 * Checks if an object is a DOM element.
 * @param {any} obj object to check
 * @returns true if it is a DOM element; false otherwise
 */
function isElement(obj) {
    return (
        typeof HTMLElement === "object" ? obj instanceof HTMLElement :
            obj && typeof obj === "object" && obj.nodeType === 1 && typeof obj.nodeName === "string"
    );
}