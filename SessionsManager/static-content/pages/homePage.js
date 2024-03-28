import { div, a, ul, li, label, input, button } from "../tags.js"

export function homePage() {
    const element =
        div(
            {}, "Hello LS",
            div(
                {},
                a({ href: "#games" }, "Search games")
            ),
            div(
                {},
                a({ href: "#sessions" }, "Search sessions")
            ),
            div(
                {}, //This should redirect to "My details, we need to implement token first"
                a({ href: "#players/1000" }, "My details - TODO") //HARD-CODED ID
            )
        )
    return element;
}