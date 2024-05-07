import { div, ul, li, button, p } from "../tags.js"
import { returnHomeButton } from "../components/returnHomeButton.js"
import { CONSTS } from "../utils.js"


export function playerDetailsPage(player) { //this is the details page for a specific player
    const homeButton = returnHomeButton();

    const searchSessionsButton = button(
        {
            class: 'search-sessions-button',
            onClick: () => {
                window.location.hash = "sessions/list?pid=" + player.id +
                    "&skip=" + CONSTS.SKIP_DEFAULT + "&limit=" + CONSTS.LIMIT_DEFAULT
            }
        },
        "Search sessions with this player"
    );

    return div(
        { class: 'player-details-page' },
        p({}, "This page provides details about a specific player."),
        div(
            { id: "playerDetails" },
            "Player Details",
            div(
                { class: 'details-section' },
                ul(
                    {},
                    li({}, "Name: " + player.name),
                    li({}, "E-mail: " + player.email)
                )
            ),
            searchSessionsButton,
            homeButton
        )
    );
}