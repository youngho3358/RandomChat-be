// JWT 토큰을 localStorage에서 가져오기
const token = localStorage.getItem("jwtToken");

window.onload = async () => {
    try {
            const response = await fetch("https://api.random-chat.site/verification/jwt", {
            method: "POST",
            headers: {
                "Content-Type": "application/json", // 요청 본문이 JSON 형식임을 명시
                "Authorization": `Bearer ${token}` // Authorization 헤더에 JWT 포함
            }
        });

        if (response.ok) {

        } else {
            alert("관리자 계정으로 로그인이 필요합니다.");
            window.location.href = "/admin/login"; // 로그인 페이지로 리디렉션
        }
    } catch (error) {
        console.error("Error during login:", error);

        // 토큰 검증 에러 시 로그인 페이지로 돌려놓는 작업
         alert("토큰 정보 검증이 실패하였습니다. 관리자에게 문의 바랍니다.");
         window.location.href = "/admin/login"; // 로그인 페이지로 리디렉션
    }
}