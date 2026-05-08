package com.example.RepairKZ_Backend.service

import com.example.RepairKZ_Backend.model.EmailDTO
import com.resend.Resend
import com.resend.services.emails.model.CreateEmailOptions
import com.sun.tools.javac.util.Log
import jakarta.mail.internet.MimeMessage
import org.apache.naming.factory.SendMailFactory
import org.springframework.beans.factory.annotation.Value
import org.springframework.mail.MailSendException

import org.springframework.mail.javamail.JavaMailSender
import org.springframework.mail.javamail.MimeMessageHelper
import org.springframework.scheduling.annotation.Async
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
        helper.setText(emailMessage.code.toString())
        return message
    }
    fun generateCodeEmail(code: String): String {
        val digits = code.map {
            """<div style="width:44px;height:52px;border:1.5px solid #7F77DD;border-radius:8px;text-align:center;line-height:52px;font-size:22px;font-weight:500;color:#534AB7;font-family:monospace;display:inline-block;">$it</div>"""
        }.joinToString("")

        return """
        <html><body style="font-family:sans-serif;background:#f5f5f5;padding:40px;">
        <div style="max-width:520px;margin:0 auto;background:#fff;border-radius:12px;overflow:hidden;">
          <div style="background:#7F77DD;padding:32px;text-align:center;">
            <p style="color:#fff;font-size:20px;font-weight:500;margin:0;">RepairKZ</p>
          </div>
          <div style="padding:32px;text-align:center;">
            <p style="font-size:15px;font-weight:500;">Ваш код подтверждения</p>
            <p style="color:#666;font-size:13px;">Код действителен 10 минут.</p>
            <div style="margin:24px 0;">$digits</div>
            <p style="color:#999;font-size:12px;">Если не запрашивали — игнорируйте письмо.</p>
          </div>
        </div>
        </body></html>
    """.trimIndent()
    }
}