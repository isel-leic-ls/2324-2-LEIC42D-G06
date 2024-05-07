import {div} from "../tags.js";
import {returnHomeButton} from "./returnHomeButton.js";


export function basicError(message) { //this is a generic error page
    const homeButton = returnHomeButton();
    return div({class : 'game-search-page', id: "basicError"}, message, homeButton);
    // using class game-search-page to style is sloppy and should be replaced with a more generic class
}