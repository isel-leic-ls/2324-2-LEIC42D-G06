import { div, button } from "../tags.js"


export function returnHomeButton() {
    return (
        div(
            {},
            button(
                {
                class: 'return-home-button',
                onClick: () => window.location.href = "/" },
                "Return Home"
            )
        )
    );
}