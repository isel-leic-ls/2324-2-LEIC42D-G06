import { div, button } from "../tags.js";


export function pagingButtons(skip, limit, total, path) { //previous and next buttons
    const previousButton = button(
        {
            onClick: () => {
                window.location.hash = path.replace(/(skip=)\d+/, "$1" + (skip - limit))
            },
            disabled: skip <= 0
        },
        "Previous"
    );

    const nextButton = button(
        {
            onClick: () => {
                window.location.hash = path.replace(/(skip=)\d+/, "$1" + (skip + limit))
            },
            disabled: total <= skip + limit
        },
        "Next"
    );

    const buttons = div({}, previousButton, nextButton);
    return buttons
}