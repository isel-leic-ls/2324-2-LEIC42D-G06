import { div, a, ul } from "../tags.js"


export function homePage(player) { //this is the home page (the first page the user sees when opening the app)
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