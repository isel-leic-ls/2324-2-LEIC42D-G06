import { div, a, ul, li, label, input, button } from "../tags.js"
import { returnHomeButton } from "../components/returnHomeButton.js"


export function playerDetailsPage(player) {
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
                         window.location.hash = "sessions/list?pid=" + player.id + "&skip=0&limit=1" // The limit value is set to 1 for testing purposes
                     }
                 }, "Search sessions with this player"
             ),
            homeButton
        );
     return element;
}

export function playerDetailsId(id) {
    const homeButton = returnHomeButton();
    const element =
        div(
            {},
            id,
            homeButton
        );
    return element;
}