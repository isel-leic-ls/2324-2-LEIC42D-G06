import { div, ul, li } from "../tags.js"


export function playerDetailsPage(player) { //this is the details page for a specific player
    const element =
        div(
            {},
            "Player 1000 hard-coded", //HARD-CODED ID
            div(
                {},
                ul(
                    {},
                    li({}, "ID: " + player.id),
                    li({}, "Name: " + player.name),
                    li({}, "E-mail: " + player.email),
                )
            )
        );

    return element;
}