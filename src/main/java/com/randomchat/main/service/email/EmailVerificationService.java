package com.randomchat.main.service.email;

import com.randomchat.main.domain.email.EmailVerification;
import com.randomchat.main.repository.email.EmailVerificationRepository;
import com.randomchat.main.repository.users.UsersRepository;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.security.SecureRandom;
import java.util.Optional;
import java.util.Random;

@Service
@RequiredArgsConstructor
public class EmailVerificationService {

    private final UsersRepository usersRepository;
    private final EmailVerificationRepository emailVerificationRepository;
    private final ResourceLoader resourceLoader;
    private final JavaMailSender mailSender;
    private static final Random RANDOM = new SecureRandom();
    private static final String CHARACTERS = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789"; // 난수에 사용될 문자


    public String createCode() {
        StringBuilder stringBuilder = new StringBuilder();

        // 10 자리 난수를 생성할 것이므로 10회 반복
        for (int i = 0; i < 10; i++) {
            // 난수에 사용될 문자의 길이만큼의 랜덤한 값을 추출
            int randomIndex = RANDOM.nextInt(CHARACTERS.length());
            // 랜덤한 index 의 문자를 추출하여 문자열로 추가
            stringBuilder.append(CHARACTERS.charAt(randomIndex));
        }

        return stringBuilder.toString();
    }

    public void saveEmailVerification(String email, String verificationCode) {
        EmailVerification emailVerification = new EmailVerification(email, verificationCode);

        emailVerificationRepository.save(emailVerification);
    }

    public String renderJspToString(String verificationCode) throws IOException {
        // JSP 파일 내용을 String 으로 읽어오기
        Resource resource = resourceLoader.getResource("classpath:static/emailForm.jsp");
        StringBuilder content = new StringBuilder();

        try (InputStream inputStream = resource.getInputStream();
             BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream, StandardCharsets.UTF_8))) {
            String line;
            while ((line = reader.readLine()) != null) {
                content.append(line).append(System.lineSeparator());
            }
        }

        // 인증 코드를 내용에 삽입하여 리턴
        return content.toString().replace("${verificationCode}", verificationCode);
    }

    public void sendEmail(String email, String verificationCode) throws IOException, MessagingException {
        String emailContent = renderJspToString(verificationCode);

        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");

        helper.setTo(email);
        helper.setSubject("랜덤 채팅의 이메일 인증 번호입니다.");
        helper.setText(emailContent, true);  // HTML 형식으로 보낼 때 true 설정
        helper.setFrom("youngho3358@gmail.com");

        mailSender.send(message);
    }

    public boolean esExceededLimit(String email) {
        // 이메일 인증 시도 횟수가 5분 내에 5회 이상 요청되었다면 true 반환
        int countRecentAttempts = emailVerificationRepository.countRecentAttempts(email);

        return countRecentAttempts >= 5;
    }

    public boolean compareVerificationCode(String email, String verificationCode) {
        Optional<EmailVerification> recentVerification = emailVerificationRepository.findFirstByEmailOrderByIdDesc(email);

        // 이메일 인증 요청이 비어있는 경우 Controller 로 false 반환
        if(recentVerification.isEmpty()) return false;

        // 이메일 인증 코드가 DB 상 데이터와 일치하는 경우 isVerified 항목을 true 로 변경하여 저장한 뒤 Controller 로 true 반환
        if(recentVerification.get().getVerificationCode().equals(verificationCode)){
            EmailVerification successEmailVerification = recentVerification.get().changeIsVerified();
            emailVerificationRepository.save(successEmailVerification);
            return true;
        }else {
            // 코드가 일치하지 않는 경우 Controller 로 false 반환
            return false;
        }
    }
}
