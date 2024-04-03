import { div, button } from "../tags.js";
import { filterQueryParameters } from "../uriparsers.js";


export function pagingButtons(path,total) {
    const { skip, limit } = filterQueryParameters(path);

    const skipVal = parseInt(skip);
    const limitVal = parseInt(limit);
    const nextDisable = total <= skipVal + limitVal

    const previousButton = button(
        {
            onClick: () => {
                window.location.hash = path.replace(/(skip=)\d+/, "$1" + (skipVal - limitVal))
            },
            disabled: skipVal <= 0
        },
        "Previous"
    );

    const nextButton = button(
        {
            onClick: () => {
                window.location.hash = path.replace(/(skip=)\d+/, "$1" + (skipVal + limitVal))
            },
            disabled : nextDisable
        },
        "Next"
    );

    const buttons = div({}, previousButton, nextButton);
    return buttons
}