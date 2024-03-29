import { div, button } from "../tags.js";
import { filterQueryParameters } from "../uriparsers.js";


export function pagingButtons(path) {

    const { skip, limit } = filterQueryParameters(path);

    const skipVal = parseInt(skip);
    const limitVal = parseInt(limit);

    const prevDisable = skipVal == 0
    const nextDisable = limitVal == 6 // This value is random and should be changed

    const previousButton = button(
            {
                onClick: () => {
                    window.location.hash = path.replace(/(skip=)\d+/, "$1" + (skipVal - limitVal))
                },
                //disabled: prevDisable // This is not working
            },
            "Previous"
        );

    const nextButton = button(
        {
            onClick: () => {
                window.location.hash = path.replace(/(skip=)\d+/, "$1" + (skipVal + limitVal))
            },
            //disabled: nextDisable // This is not working
        },
        "Next"
    );

    return div({}, previousButton, nextButton);
}

