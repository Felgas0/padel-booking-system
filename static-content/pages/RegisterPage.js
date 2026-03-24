import RegisterForm from "../components/RegisterForm.js";
import { alertBoxWithError } from "../js/utils.js";
import apiFetch from "../js/apiFetch.js";

/**
 * Register page.
 *
 * @param {Object} state - application state
 * @returns {Promise<HTMLElement>}
 */
async function RegisterPage(state) {
    /**
     * Register a new user.
     * @param {Event} event - form event
     */
    async function register(event) {
        event.preventDefault();
        const form = event.target;

        const username = form.querySelector("#username").value;
        const email = form.querySelector("#email").value;
        const password = form.querySelector("#password").value;

        if (username.length < 3 || username.length > 60) {
            await alertBoxWithError(state, form, "Username must be between 3 and 60 characters long.");
            return;
        }

        if (email.length < 4 || email.length > 320) {
            await alertBoxWithError(state, form, "An email must be between 4 and 320 characters long.");
            return;
        }

        if (!email.match(/^[A-Za-z\d+_.-]+@(.+)$/)) {
            await alertBoxWithError(state, form, "Invalid email.");
            return;
        }

        if (password.length < 5 || password.length > 25) {
            await alertBoxWithError(state, form, "Password must be between 5 and 25 characters long.");
            return;
        }

        try {
            // Registers via apiFetch (which already returns the body)
            await apiFetch("/users", {
                method: "POST",
                headers: { 'Content-Type': 'application/json' },
                body: JSON.stringify({ name: username, email, password })
            });

            // If successful, redirects to login
            window.location.href = "#login";

        } catch (e) {
            console.error("Registration error:", e);
            await alertBoxWithError(state, form, e.description || "Registration failed.");
        }
    }

    return RegisterForm(state, { onSubmit: register });
}

export default RegisterPage;
