import apiFetch from "../../js/apiFetch.js";
import { div, h1, form, input, button, p, select, option, label, a } from "../../js/dom/domTags.js";

/**
 * Page component for creating a new court.
 *
 * Displays a form where the user can select a club and input a court name.
 * After submission, sends a POST request to create the court and redirects
 * to the court detail page on success.
 *
 * @param {Object} state - The current application state.
 * @returns {Promise<HTMLElement>} - The rendered page.
 */
export default async function CourtsCreatePage(state) {
    const errorContainer = await p({ class: "text-danger mb-3 fw-bold fs-5" });

    // Fetch available clubs to populate the dropdown
    const { clubs } = await apiFetch("/clubs");

    // Dropdown for club selection
    const clubSelect = await select({
        name   : "clubId",
        class  : "form-select mb-3",
        required: true
    });

    clubSelect.append(
        await option({ value: "" }, "Choose a club..."),
        ...(await Promise.all(
            clubs.map(c => option({ value: c.id }, c.name))
        ))
    );

    // Input field for the court name
    const nameInput = await input({
        name       : "courtName",
        type       : "text",
        class      : "form-control mb-3",
        placeholder: "Court name...",
        required   : true
    });

    // Submit button
    const submitBtn = await button(
        { type: "submit", class: "btn btn-success w-100" },
        "Create Court"
    );

    /**
     * Handles form submission.
     * Validates input and sends a request to create the court.
     */
    async function onSubmit(ev) {
        ev.preventDefault();
        errorContainer.textContent = "";

        const form = ev.target;
        const clubId = parseInt(form.elements["clubId"].value);
        const name = form.elements["courtName"].value.trim();

        if (!clubId || !name) {
            errorContainer.textContent = "Fill in all fields.";
            return;
        }

        try {
            const resp = await apiFetch(`/courts`, {
                method: "POST",
                headers: { "Content-Type": "application/json" },
                body: JSON.stringify({ name, clubId })
            });

            const courtId = resp.courtId;

            if (!courtId) {
                throw {
                    name: "InvalidResponse",
                    description: "Invalid Response: courtId is missing."
                };
            }

            window.location.hash = `#courts/${courtId}`;
        }
        catch (err) {
            errorContainer.textContent = err.description || err.message || "Erro desconhecido";
        }
    }

    // Assembles the court creation form
    const formEl = await form(
        { onSubmit, style: "max-width:30rem;", class: "w-100 mb-3" },
        label({ for: "clubId" }, "Clube:"),
        clubSelect,
        label({ for: "courtName" }, "Nome do Court:"),
        nameInput,
        submitBtn
    );

    // Link to go back to the clubs list
    const backLink = await a({ href: "#clubs", class: "btn btn-link fs-4" },
        "Show All Clubs");
    const backPara = await p(backLink);

    // Final page layout
    return await div(
        { class: "container mt-5 d-flex flex-column justify-content-center align-items-center text-center" },
        await h1({ class: "mb-4" }, "Create a New Court"),
        formEl,
        errorContainer,
        backPara
    );
}
