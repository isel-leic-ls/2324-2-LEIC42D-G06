import {div} from "./tags.js"

function getHome(mainContent) {
    const element = div({}, "Hello LS");
    mainContent.replaceChildren(element);
}
export const handlers = {
    getHome
}

export default handlers
