import { Router } from "./router.js";
import UsersPage from "../pages/users/UsersPage.js";
import UserPage from "../pages/users/UserPage.js";
import RentalsByUserPage from "../pages/rentals/RentalsByUserPage.js";
import NotFoundPage from "../pages/errors/NotFoundPage.js";

// Create a new router instance to handle user-related routes
const router = Router();

// Route for listing all rentals made by a specific user
router.addHandler('/:userId/rentals', RentalsByUserPage);

// Route for listing all users
router.addHandler('/', UsersPage);

// Route for viewing a specific user's details
router.addHandler('/:id', UserPage);

// Fallback route for unmatched paths (404 page)
router.addDefaultHandler(NotFoundPage);

// Export the router to be used in the application
export default router;
