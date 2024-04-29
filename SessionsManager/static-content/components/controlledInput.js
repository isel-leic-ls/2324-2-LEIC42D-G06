import { input } from "../tags.js";


export function controlledInput(placeHolder) {
    const dateInput = input({ type: "date", id: "dateInput", placeholder: placeHolder, disabled: true });
    const dateCheckBox = input({
        type: "checkbox", id: "dateCheckBox", onChange: () => {
            dateInput.disabled = !dateInput.disabled;
            dateInput.value = ""
        }
    });

    return [dateCheckBox, dateInput]
}