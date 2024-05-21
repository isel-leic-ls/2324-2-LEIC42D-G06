import {div, p} from "../tags.js";
import {returnHomeButton} from "./returnHomeButton.js";


export function basicError(message, details) { //this is a generic error page
    const homeButton = returnHomeButton();
    return div(
    {class : 'error-page', id: "basicError"},
        p({}, message),
        p({}, details),
        homeButton
    );
}