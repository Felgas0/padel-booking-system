import apiFetch from "../../js/apiFetch.js"
import { div, h1, form, input, button, p, a } from "../../js/dom/domTags.js"

/**
 * Page component for creating a new club.
 *
 * Displays a form with a single input for the club name.
 * On submission, sends a POST request to create the club,
 * and redirects to the new club's detail page on success.
 *
 * @param {Object} state - The current application state.
 * @returns {Promise<HTMLElement>} - The rendered page element.
 */
export default async function ClubsCreatePage(state) {

    // Container to show validation or server error messages
    const errorContainer = await p({ class: "text-danger mb-3 fw-bold fs-2" })

    /**
     * Handles form submission.
     * Validates input and sends API request to create a club.
     */
    async function onSubmit(ev) {
        ev.preventDefault()
        errorContainer.textContent = ""

        const name = ev.target.elements["name"].value.trim()
        if (!name) {
            errorContainer.textContent = "O nome do clube não pode estar em branco."
            return
        }

        try {
            // triggers the POST /clubs (apiFetch already injects the token)
            const resp = await apiFetch("/clubs", {
                method:  "POST",
                headers: { "Content-Type":"application/json" },
                body:    JSON.stringify({ name })
            })
            window.location.hash = `#clubs/${resp.clubId}`
        }
        catch (err) {
            // show error inside the paragraph element
            errorContainer.textContent = err.description
                || err.message
                || "Erro desconhecido"
        }
    }

    const title = await h1({ class: "mb-4" }, "Create a New Club")

    // Input field for the club name
    const nameInput = await input({
        name       : "name",
        type       : "text",
        class      : "form-control mb-2",
        placeholder: "Club name…",
        required   : true
    })

    // Submit button
    const submitBtn = await button(
        { type: "submit", class: "btn btn-primary w-100" },
        "Create"
    )

    // Form element that wraps input and button
    const formEl = await form(
        {
            onSubmit,
            class: "mb-3 w-100",
            style: "max-width:30rem;"
        },
        nameInput,
        submitBtn
    )

    // Link back to the list of all clubs
    const backLink = await a(
        { href:"#clubs", class:"btn btn-link fs-3" },
        "Show All Clubs"
    )
    const backPara = await p(backLink)

    // Final page layout
    return await div(
        {
            class: "container mt-5 d-flex flex-column justify-content-center align-items-center text-center"
        },
        title,
        formEl,
        errorContainer,
        backPara
    )
}
