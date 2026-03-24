import apiFetch from "../../js/apiFetch.js";
import {
    div, h1, form, input, button, p, a, label
} from "../../js/dom/domTags.js";

/**
 * "Update Rental" page component.
 *
 * Displays a form pre-filled with an existing rental's date and duration,
 * allowing the user to submit updated values.
 *
 * @param {Object} state - The current application state, containing route parameters.
 * @returns {Promise<HTMLElement>} - A promise resolving to the rendered update form.
 */
export default async function RentalUpdatePage(state) {
    const id = state.params.id;
    if (id === undefined) {
        throw new Error("Rental id must be defined");
    }

    // Fetch the rental by ID
    let rental;
    try {
        rental = await apiFetch(`/rentals/${id}`);
    } catch (err) {
        throw err;
    }

    // Container for displaying error messages
    const errorContainer = await p({ class: "text-danger mb-3 fw-bold fs-5" });

    /**
     * Handles form submission for rental updates.
     * Sends a PUT request with the updated date and duration.
     */
    async function onSubmit(ev) {
        ev.preventDefault();
        errorContainer.textContent = "";

        const date = ev.target.elements["date"].value;
        if (!date) {
            errorContainer.textContent = "Date cannot be empty";
            return;
        }

        const duration = ev.target.elements["duration"].value;
        if (!duration) {
            errorContainer.textContent = "Duration cannot be empty";
            return;
        }

        try {
            await apiFetch(`/rentals/${id}`, {
                method: "PUT",
                headers: { "Content-Type": "application/json" },
                body: JSON.stringify({ date, duration })
            });
            window.location.hash = `#rentals/${id}`;
        } catch (err) {
            errorContainer.textContent = err.description || err.message || "Unknown error";
        }
    }

    const title = await h1({ class: "mb-4" }, "Update Rental");

    const dateInput = await input({
        name: "date",
        type: "datetime-local",
        step: "3600", // prevents minute selection
        class: "form-control mb-2",
        required: true,
        value: rental.date.replace("Z", "")
    });

    const durationLabel = await label({ for: "duration" }, "Duration (hours):");

    const durationInput = await input({
        name: "duration",
        type: "number",
        id: "duration",
        min: "1",
        class: "form-control mb-2",
        required: true,
        value: rental.duration.toString()
    });

    const submitBtn = await button(
        { type: "submit", class: "btn btn-primary w-100 mb-3" },
        "Update"
    );

    const formEl = await form(
        {
            onSubmit,
            class: "w-100",
            style: "max-width:30rem;"
        },
        dateInput,
        durationLabel,
        durationInput,
        submitBtn
    );

    const backLink = await a(
        { href: `#rentals/${id}`, class: "btn btn-link" },
        "Back to Rental Details"
    );

    return div(
        { class: "container mt-5 d-flex flex-column justify-content-center align-items-center text-center" },
        title,
        formEl,
        errorContainer,
        await p(backLink)
    );
}
