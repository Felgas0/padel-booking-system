import {Router} from "../routers/router.js";
import {div, hr} from "../js/dom/domTags.js";
import NavBar from "../components/NavBar.js";

// Classic routers
import clubsRouter from "../routers/clubsRouter.js";
import usersRouter from "../routers/usersRouter.js";
import courtsRouter from "../routers/courtsRouter.js";
import rentalsRouter from "../routers/rentalsRouter.js";

import AvailableHoursPage from "./rentals/available-hours/AvailableHoursPage.js";
import ClubsCreatePage from "../pages/clubs/ClubsCreatePage.js";
import CourtsCreatePage from "./courts/CourtsCreatePage.js";

import HomePage from "./HomePage.js";
import RegisterPage from "./RegisterPage.js";
import LoginPage from "./LoginPage.js";
import NotFoundPage from "./errors/NotFoundPage.js";


const router = Router();

router.addHandler("/", HomePage);
router.addHandler("/register", RegisterPage);
router.addHandler("/login", LoginPage);
router.addHandler("/users", usersRouter);
router.addHandler("/clubs", clubsRouter);
router.addHandler("/courts", courtsRouter);
router.addHandler("/rentals", rentalsRouter);
router.addHandler("/available-hours", AvailableHoursPage);
router.addHandler("/courts/create", CourtsCreatePage);
router.addHandler("/clubs/create", ClubsCreatePage);

router.addDefaultHandler(NotFoundPage);

/**
 * Initializes the web application.
 *
 * @param {Object} state - global application state
 * @returns {Promise<HTMLElement>} - main application element
 */
export default async function App(state) {
    return div(
        await NavBar(state),
        hr(),
        await router(state)
    );
}
