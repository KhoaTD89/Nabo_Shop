package com.example.demo.services.impl;

import com.example.demo.model.UserEntity;
import com.example.demo.repository.UserRepository;
import net.bytebuddy.utility.RandomString;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import java.io.UnsupportedEncodingException;

@Service
public class UserService {
    @Autowired
    private UserRepository repo;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private JavaMailSender mailSender;

    public boolean verify(String verificationCode){
        UserEntity user = repo.findByVerificationCode(verificationCode);
        if(user==null || user.isEnabled()){
            return false;
        }
        user.setVerificationCode(null);
        user.setEnabled(true);
        repo.save(user);
        return true;
    }

    public void register(UserEntity user, String siteURL) throws UnsupportedEncodingException, MessagingException {
        //saving user entity to database
        String encodedPassword = passwordEncoder.encode(user.getPassword());
        user.setPassword(encodedPassword);
        user.setRoles("USER");
        user.setPermissions("");
        String randomCode = RandomString.make(64);
        user.setVerificationCode(randomCode);
        user.setEnabled(false);
        repo.save(user);

        //send verification email
        sendVerificationEmail(user,siteURL);
    }

    private void sendVerificationEmail(UserEntity user, String siteURL) throws MessagingException, UnsupportedEncodingException {
        String toAddress = user.getEmail();
        String fromAddress = "khoa.trd18@gmail.com";
        String senderName = "Nabo shop";
        String subject = "Please verify your registration";
        String content = "Dear [[name]],<br>"
                + "please click the link below to verify your registration: <br>"
                + "<h3><a href=\"[[URL]]\" target=\"_self\">VERIFY</a></h3>"
                + "Thank you,<br>"
                + "Nabo shop.";
        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message);
        helper.setFrom(fromAddress,senderName);
        helper.setTo(toAddress);
        helper.setSubject(subject);

        content = content.replace("[[name]]",user.getFullName());
        String verifyUrl = siteURL + "/verify?code=" + user.getVerificationCode();
        content = content.replace("[[URL]]",verifyUrl);
        helper.setText(content,true);
        mailSender.send(message);
    }
}
