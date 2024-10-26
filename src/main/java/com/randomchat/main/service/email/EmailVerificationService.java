package com.randomchat.main.service.email;

import com.randomchat.main.domain.email.EmailVerification;
import com.randomchat.main.repository.EmailVerificationRepository;
import com.randomchat.main.repository.UsersRepository;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.security.SecureRandom;
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
        EmailVerification emailVerification = new EmailVerification();
        EmailVerification createEmailVerification = emailVerification.createEmailVerification(email, verificationCode);
        emailVerificationRepository.save(createEmailVerification);
    }

    public String renderJspToString(String verificationCode) throws IOException {
        // JSP 파일 내용을 String 으로 읽어오기
        Resource resource = resourceLoader.getResource("classpath:/templates/emailForm.jsp");
        String content = new String(Files.readAllBytes(resource.getFile().toPath()), StandardCharsets.UTF_8);

        // 인증 코드를 내용에 삽입하여 리턴
        return content.replace("${verificationCode}", verificationCode);
    }

    public void sendEmail(String email, String verificationCode) throws IOException, MessagingException {
        String jspPath = "/src/main/resources/templates/emailForm.jsp";
        String emailContent = renderJspToString(verificationCode);

        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");

        helper.setTo(email);
        helper.setSubject("랜덤 채팅의 이메일 인증 번호입니다.");
        helper.setText(emailContent, true);  // HTML 형식으로 보낼 때 true 설정
        helper.setFrom("youngho3358@gmail.com");

        mailSender.send(message);
    }
}
