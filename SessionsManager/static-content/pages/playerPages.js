import { div, ul, li, button } from "../tags.js"
import { returnHomeButton } from "../components/returnHomeButton.js"


export function playerDetailsPage(player) { //this is the details page for a specific player
    const homeButton = returnHomeButton();

    const element =
        div(
            {},
            "Player Details",
            div(
                {},
                ul(
                    {},
                    li({}, "ID: " + player.id),
                    li({}, "Name: " + player.name),
                    li({}, "E-mail: " + player.email),
                )
            ),
            button(
                {
                    onClick: () => {
                        window.location.hash = "sessions/list?pid=" + player.id + "&skip=0&limit=5"
                    }
                }, "Search sessions with this player"
            ),
            homeButton
        );

    return element;
}

export function playerDetailsId(id) { //TODO: what is this?
    const homeButton = returnHomeButton();

    const element =
        div(
            {},
            id,
            homeButton
        );

    return element;
}