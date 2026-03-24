import { Router } from "./router.js"
import ClubsPage from "../pages/clubs/ClubsPage.js"
import ClubPage from "../pages/clubs/ClubPage.js"
import CourtsByClubPage from "../pages/courts/CourtsByClubPage.js";
import RentalsByCourtPage from "../pages/rentals/RentalsByCourtPage.js";
import NotFoundPage from "../pages/errors/NotFoundPage.js"
import ClubsSearchPage from "../pages/clubs/ClubsSearchPage.js";
import ClubsCreatePage from "../pages/clubs/ClubsCreatePage.js"

// Create a new router instance to handle client-side routes
const router = Router();

// Route for viewing rentals of a specific court in a specific club
router.addHandler("/:clubId/courts/:courtId/rentals", RentalsByCourtPage);

// Route for viewing all courts of a given club
router.addHandler("/:clubId/courts", CourtsByClubPage);

// Route for the "Create Club" form page
router.addHandler("/create", ClubsCreatePage);

// Route for searching clubs by name
router.addHandler("/search", ClubsSearchPage);

// Route for the main clubs list page
router.addHandler("/", ClubsPage);

// Route for viewing the details of a single club
router.addHandler("/:id", ClubPage);

// Fallback route for unmatched paths (404 page)
router.addDefaultHandler(NotFoundPage);

// Export the router to be used in the app
export default router
