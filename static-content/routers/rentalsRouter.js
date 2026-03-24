import { Router } from "./router.js";
import RentalsByUserPage from "../pages/rentals/RentalsByUserPage.js";
import NotFoundPage from "../pages/errors/NotFoundPage.js";
import RentalPage from "../pages/rentals/RentalPage.js";
import RentalUpdatePage from "../pages/rentals/RentalUpdatePage.js";
import RentalDeletePage from "../pages/rentals/RentalDeletePage.js";

// Create a new router instance to handle rental-related routes
const router = Router();

// Route for listing all rentals made by a specific user
router.addHandler("/users/:userId/rentals", RentalsByUserPage);

// More specific routes must come before the general ":id" pattern
router.addHandler("/:id/update", RentalUpdatePage);   // Route to update a rental
router.addHandler("/:id/delete", RentalDeletePage);   // Route to delete a rental

// Generic route for viewing a single rental by ID
router.addHandler("/:id", RentalPage);

// Fallback route for unmatched paths (404 page)
router.addDefaultHandler(NotFoundPage);

// Export the router to be used in the application
export default router;
