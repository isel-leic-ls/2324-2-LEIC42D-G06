import { div, a, ul } from "../tags.js"


export function homePage(player) {
    const element =
        div(
            {}, "Session Finder Home Page",
            ul(
                {},
                ""
            ),
            div(
                {},
                a({ href: "#games" }, "Search games")
            ),
            div(
                {},
                a({ href: "#sessions" }, "Search sessions")
            ),
            div(
                {},
                a({ href: "#players/" + player.id }, "My profile")
            )
        )
    return element;
}