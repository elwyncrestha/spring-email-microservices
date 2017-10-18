package com.fractalpal.emailservice.model

data class SimpleEmail(val from: String, val to: String, val subject: String, val text: String = "") // rest for now omitted (like cc, bcc etc)