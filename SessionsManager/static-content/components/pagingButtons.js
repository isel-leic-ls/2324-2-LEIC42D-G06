import { div, button } from "../tags.js";


export function pagingButtons(skip, limit, total, path) {
    function previousSessions() {
        window.location.hash = path.replace(/(skip=)\d+/, "$1" + (skip - limit))
    }

    function nextSessions() {
        window.location.hash = path.replace(/(skip=)\d+/, "$1" + (skip + limit))
    }

    return (
        div({},
            button({
                class: 'paging-button',
                onClick: () => { previousSessions() },
                disabled: skip <= 0
            },
                "Previous"
            ),
            button({
                class: 'paging-button',
                onClick: () => { nextSessions() },
                disabled: total <= skip + limit
            },
                "Next"
            )
        )
    );
}