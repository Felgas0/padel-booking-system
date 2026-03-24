import apiFetch from "../../js/apiFetch.js";
import { div, h1, p, button, a } from "../../js/dom/domTags.js";

/**
 * Rental deletion confirmation page.
 *
 * Displays a confirmation prompt before allowing the user to delete a rental.
 * If confirmed, sends a DELETE request and redirects back to the user's rentals.
 *
 * @param {Object} state - The current application state, including URL parameters.
 * @returns {Promise<HTMLElement>} - A promise resolving to the rendered delete confirmation page.
 */
export default async function RentalDeletePage(state) {
    const id = state.params.id;
    if (id === undefined) {
        throw new Error("Rental id must be defined");
    }

    // Container for error messages
    const errorContainer = await p({ class: "text-danger mb-3 fw-bold fs-2" });

    const rental = await apiFetch(`/rentals/${id}`);
    const userId = rental.userId;

    // Handlers for each button

    /**
     * Handles confirmation of rental deletion.
     * Sends a DELETE request and redirects back to the user's rental list.
     */
    async function onConfirm(ev) {
        ev.preventDefault();
        errorContainer.textContent = "";
        try {
            await apiFetch(`/rentals/${id}`, { method: "DELETE" });
            window.alert("Rental successfully deleted!");
            window.location.hash = `#users/${userId}/rentals`;
        } catch (err) {
            errorContainer.textContent = err.description || err.message || "Failed to delete rental";
        }
    }

    /**
     * Cancels the deletion and returns to the rental details page.
     */
    function onCancel(ev) {
        ev.preventDefault();
        window.location.hash = `#rentals/${id}`;
    }

    return div(
        { class: "container mt-5 text-center" },
        h1({ class: "app_icon mb-4" }, "Delete Rental"),
        p({ class: "mb-4" }, `Are you sure you want to delete this rental?`),
        div(
            { class: "mb-3" },
            button(
                { class: "btn btn-danger me-2", onClick: onConfirm },
                "Yes, delete"
            ),
            button(
                { class: "btn btn-secondary", onClick: onCancel },
                "Cancel"
            )
        ),
        errorContainer,
        // link to go back without deleting
        p(
            a({ href: `#rentals/${id}`, class: "btn btn-link" }, "Back to details")
        )
    );
}
