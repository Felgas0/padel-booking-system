import { Router } from "./router.js";
import CourtPage from "../pages/courts/CourtPage.js";
import RentalsByCourtPage from "../pages/rentals/RentalsByCourtPage.js";
import NotFoundPage from "../pages/errors/NotFoundPage.js";
import CourtsCreatePage from "../pages/courts/CourtsCreatePage.js";

// Create a new router instance to handle court-related routes
const router = Router();

// Route for the "Create Court" form page
router.addHandler("/create", CourtsCreatePage);

// Route for viewing a single court's details
router.addHandler("/:id", CourtPage);

// Route for viewing rentals associated with a specific court in a specific club
router.addHandler("/clubs/:clubId/courts/:courtId/rentals", RentalsByCourtPage);

// Fallback route for unmatched paths (404 page)
router.addDefaultHandler(NotFoundPage);

// Export the router to be used in the application
export default router;
