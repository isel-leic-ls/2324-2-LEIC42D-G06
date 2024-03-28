import { div, a, ul, li, label, input, button } from "../tags.js"

export function playerDetailsPage(player) {
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