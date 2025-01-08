document.addEventListener("DOMContentLoaded", function () {

    // user-count 요소 선택
    const userCountElement = document.getElementById("user-count");

    // API 호출 함수
    function fetchUserCount() {
        // API 요청
        fetch('https://api.random-chat.site/admin/dashboard/get/randomchat-user-count')
            .then(response => {
                if (!response.ok) {
                    userCountElement.textContent = "error";
                }
                return response.json(); // JSON 형식으로 응답 읽기
            })
            .then(data => {
                console.log("data >> " + data);

                // 데이터가 유효하다면 업데이트
                if (data && data.count !== undefined) {
                    userCountElement.textContent = data.count;
                } else {
                    userCountElement.textContent = "error"; // 공백 설정
                }
            })
            .catch(error => {
                console.error('Error fetching user count:', error);
                userCountElement.textContent = "error";
            });
    }

    // 페이지 로드 시 호출
    fetchUserCount();

    // 2초마다 갱신
    setInterval(fetchUserCount, 2000);
});
