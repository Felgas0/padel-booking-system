import {a, div, img, nav} from "../js/dom/domTags.js";
import LogoutDropdown from "./LogoutDropdown.js";
import {getToken} from "../js/auth.js";

/**
 * NavBar component.
 *
 * @param {Object} state - application state
 *
 * @returns {Promise<HTMLElement>} The navigation bar
 */
async function NavBar(state) {

    const hasToken = getToken() != null;

    const navbar = await div(
        nav(
            {class: "nav nav-pills"},
            a(
                {class: "navbar-brand", href: "#"},
                img({alt: "Padel API Icon", src: "public/padle-api-favicon_v2.png", width: "44"})
            ),
            a({class: "nav-link", href: "#"}, "Home"),
            a({class: "nav-link", href: "#available-hours"}, "Available Hours"),
            a({class: "nav-link", href: "#clubs/create"}, "Create Club"),
            a({class: "nav-link", href: "#courts/create"}, "Create Court"),

            // Right area with Login stuff
            hasToken
                ? div({class: "nav ms-auto"},
                    LogoutDropdown(state, {})
                )
                : div({class: "nav ms-auto"},
                    a({class: "nav-item nav-link", href: "#register"}, "Register"),
                    a({class: "nav-item nav-link", href: "#login"}, "Login")
                )

        )
    );

    let active = window.location.hash || "#";
    const selectedNav = navbar.querySelector(`a[href="${active}"]`);
    if (selectedNav != null)
        selectedNav.classList.add("active");

    return navbar;
}

export default NavBar;
