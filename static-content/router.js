const routes = []
let notFoundRouteHandler = () => {
    throw "Route handler for unknown routes not defined"
}

function addRouteHandler(path, handler) {
    routes.push({path, handler})
}

function addDefaultNotFoundRouteHandler(notFoundRH) {
    notFoundRouteHandler = notFoundRH
}

function getRouteHandler(path) {
    const route = routes.find(r => r.path === path)
    return route ? route.handler : notFoundRouteHandler
}

function getRouteHandlerWithParams(path) {
    const params = {}; // Defining the params variable to store the route parameters
    let pathParts = path.split('/');
    // Handle query parameters
    const lastPartIndex = pathParts.length - 1;
    if (pathParts[lastPartIndex].includes('?')) {
        const [pathPart, queryParams] = pathParts[lastPartIndex].split('?');
        pathParts[lastPartIndex] = pathPart;

        queryParams.split('&').forEach(param => {
            const [key, value] = param.split('=');
            params[key] = value;
        });
    }


    // Find the corresponding route
    const route = routes.find(r => {
        const routeParts = r.path.split('/');
        if (routeParts.length !== pathParts.length) {
            return false;
        }

        // Check if each part of the route matches the path
        for (let i = 0; i < routeParts.length; i++) {
            if (routeParts[i].startsWith('{')) {
                // If it's a parameter, store it in the params variable
                const paramName = routeParts[i].slice(1, -1); // Remove the braces
                params[paramName] = pathParts[i];
            } else if (routeParts[i] !== pathParts[i]) {
                return false;
            }
        }
        return true;
    });

    // If the route is found, return the handler and the parameters
    if (route) {
        return {handler: route.handler, params};
    } else {
        return {handler: notFoundRouteHandler, params: {}};
    }
}

const router = {
    addRouteHandler,
    getRouteHandler,
    addDefaultNotFoundRouteHandler,
    getRouteHandlerWithParams

}


export default router


