import { div, ul, li, button, p, h2, label, input, a } from "../tags.js"
import { returnHomeButton } from "../components/returnHomeButton.js"
import { CONSTS } from "../utils.js"


export function playerDetailsPage(player) { //this is the details page for a specific player
    const homeButton = returnHomeButton();
    const searchSessionsButton = button(
        {
            class: 'generic-search-button',
            onClick: () => {
                window.location.hash = "sessions/list?pid=" + player.id +
                    "&skip=" + CONSTS.SKIP_DEFAULT + "&limit=" + CONSTS.LIMIT_DEFAULT
            }
        },
        "Search sessions with this player"
    );

    return div(
        { class: 'player-details-page' },
        div(
            { id: "playerDetails" },
            h2({}, "Player Details"),
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

export function playersSearchPage() {
    return div(
        { class: 'players-page' },
        p({}, "Search for players by entering name"),
        div(
            {class : 'search-section'},
            label({ for: "name" }, "Name:"),
            input({ id: "name", type: "text", placeHolder: "JP"})
        ),
        button(
            {
                class: 'generic-search-button',
                onClick: () => {
                    const name = document.getElementById("name").value;
                    window.location.hash = "players/list?name=" + name +
                        "&skip=" + CONSTS.SKIP_DEFAULT + "&limit=" + CONSTS.LIMIT_DEFAULT;
                }
            },
            "Search players"
        ),
        returnHomeButton()
    );
}

export function playersListPage(players, buttons) {
    const homeButton = returnHomeButton();
    return div(
        { class: 'players-page' },
        p({}, "This page displays the players that were queried."),
        div({}, buttons),
        p({}, "Here are the players that were queried:"),
        ...players.map((player) =>
            ul({}, li({}, a({ href: "#players/" + player.id }, player.name))),
        ),
        homeButton
    );
}