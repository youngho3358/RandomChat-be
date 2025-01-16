document.addEventListener("DOMContentLoaded", async function () {

    // 총 유저수를 나타내는 span 태그
    const totalUserCount = document.getElementById("totalUserCount");

    const token = localStorage.getItem("jwtToken");

    try {
        // 서버로부터 데이터 요청
        const response = await fetch('https://api.random-chat.site/admin/dashboard/search/all/member', {
            method: 'GET',
            headers: {
                'Authorization': `Bearer ${token}`,
                'Content-Type': 'application/json'
            }
        });

        if (!response.ok) {
            throw new Error('Failed to fetch data');
        }

        const result = await response.json();

        // 총 유저 수 업데이트
        const totalUserCount = result.totalUserCount;
        document.getElementById('totalUsersCount').textContent = totalUserCount;

        // 유저 목록 업데이트
        const users = result.userList;
        const tbody = document.getElementById('user-table').querySelector('tbody');
        tbody.innerHTML = ""; // 기존 데이터 초기화
        users.forEach(user => {
            const row = document.createElement('tr');
            row.innerHTML = `
                <td>${user.email}</td>
                <td>${user.nickname}</td>
                <td>${user.gender}</td>
                <td><button class="delete-button" data-email="${user.email}">삭제</button></td>
            `;
            tbody.appendChild(row);
        });

        // 삭제 버튼 이벤트 추가
        document.querySelectorAll('.delete-button').forEach((button) =>
            button.addEventListener('click', (event) => {
                const email = event.target.dataset.email;
                if (confirm(`${email} 회원을 삭제하시겠습니까?`)) {
                    deleteUser(email);
                }
            })
        );

    } catch (error) {
        console.error(error);
         document.getElementById('totalUsersCount').textContent = "데이터를 정상적으로 불러오지 못했습니다.";
         const tbody = document.getElementById('user-table').querySelector('tbody');
         tbody.innerHTML = '<tr><td colspan="4">데이터를 정상적으로 불러오지 못했습니다.</td></tr>';
    }

    // 유저 삭제 함수
    function deleteUser(email) {
        fetch("https://api.random-chat.site/admin/dashboard/delete", {
            method: 'DELETE',
            headers: {
                'Content-Type': 'application/json',
                'Authorization': `Bearer ${token}`,
            },
            body: JSON.stringify({ email }),
        })
            .then((response) => {
                if(response.status === 404) {
                    throw new Error(`회원이 존재하지 않습니다.`)
                }
                if(!response.ok) {
                    throw new Error(`서버와 통신 중 오류가 발생하였습니다.`);
                }
                alert(`${email} 회원이 삭제되었습니다.`);
                // 삭제 이후 다시 검색 기능으로 리다이렉트
                window.location.href = "/admin/dashboard/search/member";
            })
            .catch((error) => {
                console.error('삭제 오류:', error);
                alert(error);
                window.location.href = "/admin/dashboard/search/member";
            });
    }

});