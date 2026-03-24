import { select, option, input, label, div } from "../../../js/dom/domTags.js";
import apiFetch from "../../../js/apiFetch.js";

/**
 * Dynamically creates form controls for selecting a club, court, and date.
 *
 * Fetches available clubs and (optionally) courts from the API,
 * initializes selects with values based on URL query parameters (if any),
 * and updates the state via the provided callback when user input changes.
 *
 * @param {Object} state - The application state, including query parameters.
 * @param {Function} updateCallback - Function called with updated selection state.
 * @returns {Promise<{ formControls: HTMLElement }>} The form controls wrapped in a container.
 */
export async function createFormControls(state, updateCallback) {
    const selectedClubIdFromUrl  = parseInt(state.query?.clubId);
    const selectedCourtIdFromUrl = parseInt(state.query?.courtId);

    const { clubs } = await apiFetch("/clubs");

    let courts = [];

    // Club <select> element with dynamic loading of associated courts
    const clubSelect = await select({
        class: "form-select mb-3",
        onChange: async e => {
            const selectedClubId = parseInt(e.target.value) || null;
            courtSelect.innerHTML = "";
            dateInput.value = "";
            updateCallback({ clubId: selectedClubId });

            if (!selectedClubId) return;

            const resp = await apiFetch(`/clubs/${selectedClubId}/courts`);
            courts = resp.courts ?? resp;

            courtSelect.append(
                await option({ value: "" }, "Choose a court"),
                ...(await Promise.all(
                    courts.map(c => option({ value: c.id }, c.name))
                ))
            );
            courtSelect.disabled = false;

            if (selectedCourtIdFromUrl) {
                courtSelect.value = selectedCourtIdFromUrl;
                updateCallback({ courtId: selectedCourtIdFromUrl });
            }
        }
    });

    // Court <select> element, enabled after a club is chosen
    const courtSelect = await select({
        disabled: true,
        class: "form-select mb-3",
        onChange: () => {
            const courtId = parseInt(courtSelect.value) || null;
            updateCallback({ courtId });
        }
    });

    // Date input field
    const dateInput = await input({
        type: "date",
        class: "form-control mb-3",
        onChange: () => {
            const date = dateInput.value || null;
            updateCallback({ date });
        }
    });

    // Populate club selector
    clubSelect.append(
        await option({ value: "" }, "Choose a club"),
        ...(await Promise.all(
            clubs.map(c => option({ value: c.id }, c.name))
        ))
    );

    // If a club was pre-selected via query params, trigger its loading
    if (selectedClubIdFromUrl) {
        clubSelect.value = selectedClubIdFromUrl;
        clubSelect.dispatchEvent(new Event("change"));
    }

    // Return the full form control block
    const formControls = div(
        { class: "d-flex flex-column gap-2", style: "min-width:30rem;" },
        label({ for: "club" }, "Club:"),    clubSelect,
        label({ for: "court" }, "Court:"),  courtSelect,
        label({ for: "date" }, "Date:"),    dateInput
    );

    return { formControls };
}
