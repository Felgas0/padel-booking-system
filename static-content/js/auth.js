/**
 * Stores the token in sessionStorage
 * @param {string} token
 */
export function setToken(token) {
    sessionStorage.setItem("token", token);
}

/**
 * Reads the token from sessionStorage (or null if it doesn't exist)
 * @returns {string|null}
 */
export function getToken() {
    return sessionStorage.getItem("token");
}

/**
 * Clears the token (e.g., on logout)
 */
export function clearToken() {
    sessionStorage.removeItem("token");
}
