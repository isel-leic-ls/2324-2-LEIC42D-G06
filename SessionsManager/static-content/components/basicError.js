import { div } from "../tags.js";
import { returnHomeButton } from "./returnHomeButton.js";

export function basicError(message) {
    const homeButton = returnHomeButton();
    return div({}, message, homeButton);
}