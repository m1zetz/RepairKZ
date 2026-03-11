package com.example.RepairKZ_Backend.service

import com.example.RepairKZ_Backend.model.EmailDTO
import jakarta.mail.internet.MimeMessage
import org.springframework.mail.MailSendException

import org.springframework.mail.javamail.JavaMailSender
import org.springframework.mail.javamail.MimeMessageHelper
import org.springframework.stereotype.Service

@Service
class MailSenderService(
    private val emailSender: JavaMailSender
) {
    fun sendMail(emailMessage: EmailDTO){
        try {
            val msg = createSimpleMessage(emailMessage)
            emailSender.send(msg)
        } catch (e: Exception) {
            print("Failed to send Email: ${e.message}")
            throw MailSendException("Failed to send email", e)
        }

    }
    private fun createSimpleMessage(emailMessage: EmailDTO): MimeMessage {
        val message: MimeMessage = emailSender.createMimeMessage()
        val helper = MimeMessageHelper(message)
        helper.setTo(emailMessage.to)
        helper.setSubject(emailMessage.subject)
        helper.setText(emailMessage.text)
        return message
    }
}