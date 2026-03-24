// export -> Makes this variable available for use in other files (we are "exporting" it).
// const -> Creates a constant variable, meaning its value cannot be changed later.
// API_BASE_URL -> This is the name of the variable. All uppercase with underscores usually means it's a fixed/global setting.
// window -> A built-in object in the browser that represents the current browser window.
//    .location -> A property of 'window' that gives info about the current URL.
//       .origin -> A sub-property of 'location' that gives just the origin (protocol + domain + port), like "https://example.com".

export const API_BASE_URL = window.location.origin;
