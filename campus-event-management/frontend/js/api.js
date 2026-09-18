const API_BASE_URL = "http://localhost:8080";


// ===============================
// LOGIN
// ===============================
async function loginUser(email, password) {

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

    console.log("Login response status:", response.status);
    console.log("Login response status text:", response.statusText);

    const responseText = await response.text();

    console.log("Login response body:", responseText);

    if (!response.ok) {
        throw new Error(
            responseText || "Login failed."
        );
    }

    if (!responseText || responseText.trim() === "") {
        throw new Error(
            "Server returned an empty response. Please check the login API."
        );
    }

    try {

        return JSON.parse(responseText);

    } catch (error) {

        console.error(
            "Invalid JSON response:",
            responseText
        );

        throw new Error(
            "Server returned an invalid JSON response."
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

    const token = localStorage.getItem("token");

    return token !== null && token !== "";
}


// ===============================
// LOGOUT
// ===============================
function logout() {

    localStorage.removeItem("token");
    localStorage.removeItem("userId");
    localStorage.removeItem("userName");
    localStorage.removeItem("userEmail");
    localStorage.removeItem("userRole");

    window.location.href = "../login.html";
}


// ===============================
// COMMON API REQUEST
// ===============================
async function apiRequest(url, options = {}) {

    const token = getToken();

    // Create headers
    const headers = {
        "Content-Type": "application/json",
        ...(options.headers || {})
    };


    // =================================
    // ADD JWT TOKEN
    // =================================
    if (token) {

        headers["Authorization"] = `Bearer ${token}`;

    }


    // =================================
    // DEBUG LOGS
    // =================================
    console.log(
        "API URL:",
        `${API_BASE_URL}${url}`
    );

    console.log(
        "Token exists:",
        !!token
    );

    console.log(
        "Authorization header:",
        headers["Authorization"]
    );


    // =================================
    // SEND REQUEST
    // =================================
    const response = await fetch(
        `${API_BASE_URL}${url}`,
        {
            ...options,

            method: options.method || "GET",

            headers: headers
        }
    );


    // =================================
    // UNAUTHORIZED - 401
    // =================================
    if (response.status === 401) {

        localStorage.removeItem("token");
        localStorage.removeItem("userId");
        localStorage.removeItem("userName");
        localStorage.removeItem("userEmail");
        localStorage.removeItem("userRole");

        window.location.href = "../login.html";

        throw new Error(
            "Session expired. Please login again."
        );
    }


    // =================================
    // FORBIDDEN - 403
    // =================================
    if (response.status === 403) {
    const text = await response.text();

    if (
        text.toLowerCase().includes(
            "student is already registered for this event"
        )
    ) {
        throw new Error("You are already registered.");
    }

    throw new Error(
        text || "You are Already Registered."
    );
}


    // =================================
    // OTHER ERRORS
    // =================================
    if (!response.ok) {

        const text = await response.text();

        throw new Error(
            text || `Request failed with status ${response.status}`
        );
    }


    // =================================
    // NO CONTENT
    // =================================
    if (response.status === 204) {

        return null;
    }


    // =================================
    // READ RESPONSE
    // =================================
    const text = await response.text();


    if (!text || text.trim() === "") {

        return null;
    }


    // =================================
    // PARSE JSON
    // =================================
    try {

        return JSON.parse(text);

    } catch (error) {

        console.error(
            "Invalid JSON response:",
            text
        );

        throw new Error(
            "Server returned invalid JSON."
        );
    }
}