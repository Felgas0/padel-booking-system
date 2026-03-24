import {
    a, button, div, h1, img, p
} from "../../js/dom/domTags.js";

/**
 * Generic application error page (404, 409, ...)
 * Receives the object { name, description, extraInfo } from apiFetch.
 */
export default async function AppErrorPage(state, error) {

    const imgSrc = "/public/not-found.png";

    const extra = error.extraInfo?.url
        ? p({ class: "small text-muted mb-0" }, error.extraInfo.url)
        : null;

    return div(
        {
            class:
                "d-flex flex-column justify-content-center align-items-center " +
                "text-center bg-white text-dark vh-100"
        },

        img({
            src  : imgSrc,
            alt  : "Error illustration",
            style: "max-width: 280px; opacity:.9;"
        }),

        h1({ class: "display-4 mt-4" }, "Error ", error.name),
        p ({ class: "lead" }, error.description),

        extra,
        a(
            { href: "#", class: "mt-4" },
            button({ class: "btn btn-outline-primary btn-lg" }, "Back to Home")
        )
    );
}
