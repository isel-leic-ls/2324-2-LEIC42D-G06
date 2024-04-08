import { div, ul, li, button } from "../tags.js"
import { returnHomeButton } from "../components/returnHomeButton.js"
import { CONSTS } from "../utils.js"


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
                        window.location.hash = "sessions/list?pid=" + player.id +
                            "&skip=" + CONSTS.SKIP_DEFAULT + "&limit=" + CONSTS.LIMIT_DEFAULT
                    }
                }, "Search sessions with this player"
            ),
            homeButton
        );

    return element;
}