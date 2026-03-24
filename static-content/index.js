import App from "./pages/App.js";
import { InvalidSearchParamsError, isAppError } from "./js/errorUtils.js";
import AppErrorPage from "./pages/errors/AppErrorPage.js";
import { createState, render } from "./js/compLib.js";
import InvalidSearchParamsErrorPage from "./pages/errors/InvalidSearchParamsErrorPage.js";

// Register event listeners to trigger routing logic on page load and hash changes
window.addEventListener("load", hashChangeHandler);
window.addEventListener("hashchange", hashChangeHandler);

/**
 * Handles errors thrown by components during rendering or data loading.
 *
 * Displays a specific error page depending on the error type:
 * - InvalidSearchParamsError: Shows user-friendly feedback about search query issues
 * - AppError (e.g. HTTP or API-related): Shows a generic error page
 * - Otherwise, rethrows the error (e.g. unexpected/unhandled)
 *
 * @param {Object} state - The current application state.
 * @param {Object} error - The error object thrown.
 */
function handleComponentError(state, error) {
    if (error instanceof InvalidSearchParamsError)
        InvalidSearchParamsErrorPage(state, error).then(render);
    else if (isAppError(error))
        AppErrorPage(state, error).then(render);
    else
        throw error;
}

/**
 * Called whenever the URL hash changes (or on initial page load).
 *
 * Parses the path from the hash (e.g. "#/clubs" becomes "/clubs"),
 * creates the state for the current route, and invokes the App entrypoint.
 * Any thrown error during this process is handled by `handleComponentError`.
 */
function hashChangeHandler() {
    const path = window.location.hash.replace("#", "/");

    const state = createState(path);

    App(state)
        .then(render)
        .catch((error) => handleComponentError(state, error));
}
