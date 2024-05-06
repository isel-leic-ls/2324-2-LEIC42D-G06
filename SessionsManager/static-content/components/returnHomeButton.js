import { div, button } from "../tags.js"


export function returnHomeButton() {
    return (
        div(
            {},
            button(
                { onClick: () => window.location.href = "/" },
                "Return Home"
            )
        )
    );
}