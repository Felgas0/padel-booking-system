import LoginForm from "../components/LoginForm.js";
import { alertBoxWithError } from "../js/utils.js";
import { setToken } from "../js/auth.js";
import apiFetch from "../js/apiFetch.js";

/**
 * Login page.
 *
 * @param {Object} state - application state
 * @returns {Promise<HTMLElement>}
 */
async function LoginPage(state) {

    /**
     * Logs in a user.
     * @param {Event} event - form submission event
     */
    async function login(event) {
        event.preventDefault();
        const form = event.target;

        const name = form.querySelector("#name").value;
        const password = form.querySelector("#password").value;

        if (name.length === 0) {
            await alertBoxWithError(state, form, "Name must exist.");
            return;
        }

        if (password.length < 5 || password.length > 25) {
            await alertBoxWithError(state, form, "Password must be between 5 and 25 characters long.");
            return;
        }

        try {
            const result = await apiFetch("/users/login", {
                method: "POST",
                headers: { 'Content-Type': 'application/json' },
                body: JSON.stringify({ name, password })
            });

            setToken(result.token);
            window.location.href = "#";

        } catch (e) {
            console.error("Login error:", e);
            await alertBoxWithError(state, form, e.description || "Login failed.");
        }
    }

    return LoginForm(state, { onSubmit: login });
}

export default LoginPage;
