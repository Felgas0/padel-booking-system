import {
    a, br, div, h1, img, p, strong,
    form, input, button
} from "../js/dom/domTags.js";
import { setToken, getToken } from "../js/auth.js";

/**
 * Home page component.
 * Displays a simple interface with a club search form,
 * a link to view all clubs, project and author info,
 * and optionally allows setting an auth token for testing.
 *
 * @param {Object} state - application state
 * @returns {Promise<HTMLElement>} - the rendered home page element
 */
export default async function HomePage(state) {

    // Just un-comment all the Token related code if you want to see it on the HomePage
    // (for testing)

    // If there is already a token
    // const existing = getToken();
    //
    // function onTokenSubmit(ev) {
    //     ev.preventDefault();
    //     const t = ev.target.elements["token"].value.trim();
    //     if (!t) return;
    //     setToken(t);
    //     window.alert("Token stored!");
    // }

    /**
     * Handles the club search form submission.
     * Navigates to the search results page using the entered term.
     */
    function onSearchSubmit(ev) {
        ev.preventDefault();

        const term = ev.target.elements["search"].value.trim();
        if (!term) return;

        window.location.hash =
            `clubs/search?name=${encodeURIComponent(term)}`;
    }

    return div(
        { class: "d-flex flex-column justify-content-center align-items-center text-center" },

        h1({ class: "app_icon mb-4" }, "Home"),

        // Token input (estilo input-group como a pesquisa)
        // form(
        //     {
        //         class: "input-group mb-4 justify-content-center",
        //         onSubmit: onTokenSubmit,
        //         style: "max-width: 20rem;"
        //     },
        //     input({
        //         name       : "token",
        //         type       : "text",
        //         class      : "form-control",
        //         placeholder: existing
        //             ? `Token actual: ${existing.substring(0,8)}...`
        //             : "Insert token here",
        //         required: true
        //     }),
        //     button({ class: "btn btn-success" }, "Save Token")
        // ),

        // Search box
        form(
            {
                class: "input-group mb-4 justify-content-center",
                onSubmit: onSearchSubmit,
                style: "max-width: 20rem;"
            },
            input({
                name       : "search",
                type       : "text",
                class      : "form-control",
                placeholder: "Search clubs…"
            }),
            button({ class: "btn btn-primary" }, "Search")
        ),

        p(
            a({ href: "#clubs", class: "btn btn-primary btn-lg mt-3 mb-4" },
                "Available Clubs")
        ),

        br(),
        strong(
            "Authors: ",
            a({ href: "https://github.com/Antgentil" }, "António Pimentel"),
            " and ",
            a({ href: "https://github.com/Felgas0"   }, "Martim Felgas")
        ),
        br(),
        p("Professors: Ana Rito Rebelo and Rodolfo Morgado"),
        p("Bachelor in Computer Science and Computer Engineering"),
        p("Software Laboratory – LEIC44D – Group 12"),
        p("Summer of 2025"),

        img({
            src  : "/public/isel-logo.png",
            class: "isel-logo rounded mx-auto d-block",
            alt  : "ISEL Logo"
        })
    );
}
