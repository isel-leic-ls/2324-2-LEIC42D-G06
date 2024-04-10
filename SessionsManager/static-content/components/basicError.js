import {div} from "../tags.js";
import {returnHomeButton} from "./returnHomeButton.js";


export function basicError(message) { //this is a generic error page
    const homeButton = returnHomeButton();
    return div({id: "basicError"}, message, homeButton);
}