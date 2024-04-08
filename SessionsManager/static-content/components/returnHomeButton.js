import { div, button } from "../tags.js"


export function returnHomeButton() {
    const element =
        div(
            {},
            button({ onClick: () => window.location.href = "/" }, "Return Home")
        )
    return element;
}