import apiFetch from "../../../js/apiFetch.js";
import { button, p } from "../../../js/dom/domTags.js";

/**
 * Renders the available rental slots for a given club, court, and date.
 *
 * Fetches data from the API and displays buttons for each time slot.
 * If a slot is open, the user can click to create a rental.
 * If a booking is successful, the user is redirected to the rental details page.
 *
 * @param {HTMLElement} resultContainer - The DOM element where the slots will be displayed.
 * @param {number|null} clubId - The selected club ID.
 * @param {number|null} courtId - The selected court ID.
 * @param {string|null} date - The selected date (YYYY-MM-DD format).
 */
export async function renderSlots(resultContainer, clubId, courtId, date) {
    resultContainer.innerHTML = "";

    if (!clubId || !courtId || !date) return;

    let data;
    try {
        data = await apiFetch(`/clubs/${clubId}/courts/${courtId}/available-hours?date=${date}`);
    } catch (err) {
        const msg = err.description || err.message;
        const errEl = await p({ class: "text-danger w-100 text-center" }, msg);
        resultContainer.append(errEl);
        return;
    }

    const slots = await Promise.all(data.map(async h => {
        const props = {
            class: h.status === "OPEN" ? "btn btn-outline-success m-1" : "btn btn-outline-secondary m-1",
            disabled: h.status !== "OPEN"
        };

        if (h.status === "OPEN") {
            // Handler for booking a slot
            props.onClick = async () => {
                try {
                    const rental = await apiFetch("/rentals", {
                        method: "POST",
                        headers: { "Content-Type": "application/json" },
                        body: JSON.stringify({
                            clubId,
                            courtId,
                            date: `${date}T${h.hour}:00`,
                            duration: 1
                        })
                    });
                    window.location.hash = `#rentals/${rental.rentalId}`;
                } catch (err) {
                    const msg = err.name === "HTTP 401"
                        ? "Need to log to book a slot!"
                        : err.description || err.message;
                    const errEl = await p({ class: "text-danger w-100 text-center" }, msg);
                    resultContainer.querySelectorAll(".text-danger").forEach(e => e.remove());
                    resultContainer.prepend(errEl);
                }
            };
        }

        return await button(props, h.hour);
    }));

    resultContainer.replaceChildren(...slots);
}