document.getElementById("login-form").addEventListener("submit", async function (event) {
    event.preventDefault();

    const username = document.getElementById("username").value;
    const password = document.getElementById("password").value;

    try {
        const response = await fetch("https://api.random-chat.site/admin/login", {
            method: "POST",
            headers: { "Content-Type": "application/x-www-form-urlencoded" },
            body: `username=${username}&password=${password}`
        });

        if (response.ok) {
            const token = response.headers.get("Authorization").replace("Bearer ", "");
            localStorage.setItem("jwtToken", token); // JWT 저장
            window.location.href = "/admin/dashboard"; // 대시보드로 리디렉션
        } else {
            const errorMessage = document.getElementById("error-message");
            errorMessage.textContent = "Invalid username or password";
            errorMessage.style.display = "block";
        }
    } catch (error) {
        console.error("Error during login:", error);
    }
});