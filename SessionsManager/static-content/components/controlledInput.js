import { input } from "../tags.js";


export function controlledInput(placeHolder) {
    const dateInput = input({ type: "text", placeholder: placeHolder, disabled: true });
    const dateCheckBox = input({
        type: "checkbox", onChange: () => {
            dateInput.disabled = !dateInput.disabled;
            dateInput.value = ""
        }
    });

    return [dateCheckBox, dateInput]
}