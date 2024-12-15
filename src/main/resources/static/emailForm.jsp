<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Email Verification</title>
    <style>
        * {
            text-align: center;
        }
        body {
            font-family: Arial, sans-serif;
            text-align: center;
        }
        h1 {
            color: #4CAF50;
        }
        .code {
            font-size: 24px;
            font-weight: bold;
            color: #333;
        }
        p {
            font-size: 16px;
        }
        .footer {
            font-size: 14px;
            color: #999;
        }
    </style>
</head>
<body>
    <h1>이메일 인증</h1>
    <p>RandomChat 가입을 환영합니다!</p>
    <p>아래의 인증코드를 입력하여 회원가입을 완료해주세요.</p>
    <div class="code">${verificationCode}</div>
    <p class="footer">감사합니다.</p>
</body>
</html>