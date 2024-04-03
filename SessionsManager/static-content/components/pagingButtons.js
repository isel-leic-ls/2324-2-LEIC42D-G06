import { div, button } from "../tags.js";
import { filterQueryParameters } from "../uriparsers.js";


export function pagingButtons(path) {
    const { skip, limit } = filterQueryParameters(path);

    const skipVal = parseInt(skip);
    const limitVal = parseInt(limit);

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
            //TODO disable if there are no more results
        },
        "Next"
    );

    const buttons = div({}, previousButton, nextButton);
    return buttons
}