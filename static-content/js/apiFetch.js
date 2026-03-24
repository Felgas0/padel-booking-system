import { API_BASE_URL }   from "./config.js";
import { isAppError, LogError } from "./errorUtils.js";
import { getToken } from "./auth.js";

/**
 * API call that:
 *   • returns the body (JSON or text) for 2xx responses
 *   • throws { name, description, extraInfo } for any HTTP error
 *   • throws LogError for unexpected network/JSON failures
 *
 * @param {string} endpoint  e.g. "/clubs/55"
 * @param {RequestInit=} init  optional fetch options
 * @returns {Promise<any>}
 */
export default async function apiFetch(endpoint, init = {}) {
    const url = `${API_BASE_URL}${endpoint}`.replace(/([^:]\/)\/+/g, "$1");


    // Add token (if it exists)
    const token = getToken();
    if (token) {
        init.headers = init.headers || {};
        init.headers["Authorization"] = `Bearer ${token}`;
    }

    try {
        const res= await fetch(url, init);
        const contentType = res.headers.get("content-type") ?? "";
        const isJson = contentType.includes("application/json");
        const body= isJson ? await res.json() : await res.text();

        // 2xx success
        if (res.ok) return body;

        // HTTP error
        const message =
            isJson ? (body.message || JSON.stringify(body))
                : (body || res.statusText);

        throw {
            name       : `HTTP ${res.status}`,   // ex: HTTP 404
            description: message.trim(),
            extraInfo  : { url }
        };

    } catch (err) {
        if (isAppError(err)) throw err;
        throw new LogError(err);
    }
}
