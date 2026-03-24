import { div, h1 } from "../../../js/dom/domTags.js";
import { createFormControls } from "./AvailableHoursForm.js";
import { renderSlots } from "./AvailableHoursSlots.js";

/**
 * Page for viewing available rental hours.
 *
 * Displays a form to select a club, court, and date, and renders
 * the corresponding available time slots below it.
 *
 * @param {Object} state - The application state, including query parameters.
 * @returns {Promise<HTMLElement>} - The rendered "Available Hours" page.
 */
export default async function AvailableHoursPage(state) {
    // Keeps track of the current selection (club, court, and date)
    let selected = {
        clubId: null,
        courtId: null,
        date: null
    };

    // Container that will display the available time slots
    const resultContainer = await div({
        class: "d-flex flex-wrap justify-content-center gap-2 mt-4",
        style: "max-width: 40rem; margin-inline: auto;"
    });

    /**
     * Callback called when the form selection is updated.
     * Re-renders the available slots based on the selected values.
     */
    const onUpdate = update => {
        selected = { ...selected, ...update };
        renderSlots(resultContainer, selected.clubId, selected.courtId, selected.date);
    };

    // Create and retrieve form controls for selection
    const { formControls } = await createFormControls(state, onUpdate);

    // Render the complete page
    return div(
        h1({ class: "app_icon mb-4 text-center" }, "Rental Schedule"),
        div({ class: "d-flex flex-column align-items-center" }, formControls),
        resultContainer
    );
}
