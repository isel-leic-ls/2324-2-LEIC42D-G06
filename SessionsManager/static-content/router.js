const routes = []
let notFoundRouteHandler = () => { throw "Route handler for unknown routes not defined" }

function addRouteHandler(path, handler) {
    routes.push({ path, handler })
}
function addDefaultNotFoundRouteHandler(notFoundRH) {
    notFoundRouteHandler = notFoundRH
}

function getRouteHandler(path) {
    const route = routes.find(r => comparePaths(r.path, path))
    return route ? route.handler : notFoundRouteHandler
}

function comparePaths(rPath, path) {

    const qIndex = path.indexOf('?');
    const qPath = qIndex === -1 ? path : path.substring(0, qIndex);

    const routeParts = rPath.split("/");
    const pathParts = qPath.split("/");

    if (routeParts.length !== pathParts.length) return false;

    for (let i = 0; i < routeParts.length; i++) {
        if (routeParts[i].startsWith(":")) continue;
        if (routeParts[i] !== pathParts[i]) return false;
    }
    return true;
}

const router = {
    addRouteHandler,
    getRouteHandler,
    addDefaultNotFoundRouteHandler
}

export default router