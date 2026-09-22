const API_BASE_URL = "https://campuseventmanagementsystem-production.up.railway.app";


// ===============================
// CLEAR SESSION
// ===============================
function clearSession() {

    localStorage.removeItem("token");
    localStorage.removeItem("userId");
    localStorage.removeItem("userName");
    localStorage.removeItem("userEmail");
    localStorage.removeItem("userRole");
}


// ===============================
// LOGIN
// ===============================
async function loginUser(email, password) {

    try {

        const response = await fetch(
            `${API_BASE_URL}/api/users/login`,
            {
                method: "POST",
                headers: {
                    "Content-Type": "application/json"
                },
                body: JSON.stringify({
                    email: email,
                    password: password
                })
            }
        );

        console.log(
            "Login response status:",
            response.status
        );

        const responseText = await response.text();

        console.log(
            "Login response body:",
            responseText
        );


        // ===============================
        // LOGIN ERROR
        // ===============================
        if (!response.ok) {

            throw new Error(
                getFriendlyErrorMessage(
                    response.status,
                    responseText
                )
            );
        }


        // ===============================
        // EMPTY RESPONSE
        // ===============================
        if (
            !responseText ||
            responseText.trim() === ""
        ) {

            throw new Error(
                "Server returned an empty response. Please try again."
            );
        }


        // ===============================
        // PARSE JSON
        // ===============================
        try {

            return JSON.parse(responseText);

        } catch (error) {

            console.error(
                "Invalid JSON response:",
                responseText
            );

            throw new Error(
                "Server returned an invalid response. Please try again."
            );
        }

    } catch (error) {

        console.error(
            "Login error:",
            error
        );

        // Preserve friendly error
        if (error instanceof Error) {
            throw error;
        }

        throw new Error(
            "Unable to connect to the server. Please try again."
        );
    }
}


// ===============================
// GET TOKEN
// ===============================
function getToken() {

    return localStorage.getItem("token");
}


// ===============================
// GET USER ID
// ===============================
function getUserId() {

    return localStorage.getItem("userId");
}


// ===============================
// GET USER NAME
// ===============================
function getUserName() {

    return localStorage.getItem("userName");
}


// ===============================
// GET USER EMAIL
// ===============================
function getUserEmail() {

    return localStorage.getItem("userEmail");
}


// ===============================
// GET USER ROLE
// ===============================
function getUserRole() {

    return localStorage.getItem("userRole");
}


// ===============================
// CHECK LOGIN
// ===============================
function isLoggedIn() {

    const token = getToken();

    return token !== null && token !== "";
}


// ===============================
// LOGOUT
// ===============================
function logout() {

    clearSession();

    window.location.href = "../login.html";
}


// ===============================
// FRIENDLY ERROR MESSAGE
// ===============================
function getFriendlyErrorMessage(
    status,
    responseText = ""
) {

    const text = responseText
        ? responseText.toLowerCase()
        : "";


    // ===============================
    // NETWORK / SERVER
    // ===============================
    if (status === 0) {

        return "Unable to connect to the server. Please try again.";
    }


    // ===============================
    // 400 - BAD REQUEST
    // ===============================
    if (status === 400) {

        if (
            text.includes("already registered") ||
            text.includes("duplicate")
        ) {

            return "This request has already been processed.";
        }

        if (
            text.includes("deadline")
        ) {

            return "Registration deadline has passed.";
        }

        if (
            text.includes("capacity") ||
            text.includes("full")
        ) {

            return "This event is currently full.";
        }

        return (
            responseText ||
            "Please check your information and try again."
        );
    }


    // ===============================
    // 401 - UNAUTHORIZED
    // ===============================
    if (status === 401) {

        return "Your session has expired. Please login again.";
    }


    // ===============================
// 409 - CONFLICT
// ===============================
if (status === 409) {

    if (text.toLowerCase().includes("already registered")) {

        return "You are already registered for this event.";
    }

    return text || "This action could not be completed.";
}


    // ===============================
    // 403 - FORBIDDEN
    // ===============================
    if (status === 403) {

        if (
            text.includes(
                "already registered"
            )
        ) {

            return "You are already registered for this event.";
        }

        if (
            text.includes("access denied") ||
            text.includes("forbidden")
        ) {

            return "You do not have permission to perform this action.";
        }

        return "You do not have permission to perform this action.";
    }


    // ===============================
    // 404 - NOT FOUND
    // ===============================
    if (status === 404) {

        return (
            responseText ||
            "The requested information was not found."
        );
    }


    // ===============================
    // 409 - CONFLICT
    // ===============================
    if (status === 409) {

        return (
            responseText ||
            "This action conflicts with existing data."
        );
    }


    // ===============================
    // 500 - SERVER ERROR
    // ===============================
    if (status >= 500) {

        return "Something went wrong on the server. Please try again later.";
    }


    // ===============================
    // DEFAULT
    // ===============================
    return (
        responseText ||
        `Request failed with status ${status}.`
    );
}


// ===============================
// COMMON API REQUEST
// ===============================
async function apiRequest(
    url,
    options = {}
) {

    const token = getToken();


    // ===============================
    // CREATE HEADERS
    // ===============================
    const headers = {
        "Content-Type": "application/json",
        ...(options.headers || {})
    };


    // ===============================
    // ADD JWT TOKEN
    // ===============================
    if (token) {

        headers["Authorization"] =
            `Bearer ${token}`;
    }


    // ===============================
    // DEBUG LOGS
    // ===============================
    console.log(
        "API URL:",
        `${API_BASE_URL}${url}`
    );

    console.log(
        "Token exists:",
        !!token
    );


    // Do not print the actual JWT token
    console.log(
        "Authorization header exists:",
        !!headers["Authorization"]
    );


    try {

        // ===============================
        // SEND REQUEST
        // ===============================
        const response = await fetch(
            `${API_BASE_URL}${url}`,
            {
                ...options,

                method:
                    options.method || "GET",

                headers: headers
            }
        );


        // ===============================
        // READ RESPONSE
        // ===============================
        const responseText =
            await response.text();


        // ===============================
        // 401 - UNAUTHORIZED
        // ===============================
        if (response.status === 401) {

            clearSession();

            window.location.href =
                "../login.html";

            throw new Error(
                "Your session has expired. Please login again."
            );
        }


        // ===============================
        // 403 - FORBIDDEN
        // ===============================
        if (response.status === 403) {

            throw new Error(
                getFriendlyErrorMessage(
                    403,
                    responseText
                )
            );
        }


        // ===============================
        // OTHER ERRORS
        // ===============================
        if (!response.ok) {

            throw new Error(
                getFriendlyErrorMessage(
                    response.status,
                    responseText
                )
            );
        }


        // ===============================
        // NO CONTENT
        // ===============================
        if (response.status === 204) {

            return null;
        }


        // ===============================
        // EMPTY RESPONSE
        // ===============================
        if (
            !responseText ||
            responseText.trim() === ""
        ) {

            return null;
        }


        // ===============================
        // PARSE JSON
        // ===============================
        try {

            return JSON.parse(
                responseText
            );

        } catch (error) {

            console.error(
                "Invalid JSON response:",
                responseText
            );

            throw new Error(
                "Server returned an invalid response. Please try again."
            );
        }

    } catch (error) {

        console.error(
            "API request error:",
            error
        );


        // ===============================
        // NETWORK ERROR
        // ===============================
        if (
            error instanceof TypeError
        ) {

            throw new Error(
                "Unable to connect to the server. Please make sure the backend is running."
            );
        }


        // ===============================
        // PRESERVE FRIENDLY ERROR
        // ===============================
        if (
            error instanceof Error
        ) {

            throw error;
        }


        throw new Error(
            "Something went wrong. Please try again."
        );
    }
}