package com.fractalpal.emailclient.model

 // just for showing that gateway can wrap/intercept services call
data class GateWayResponse(val response: ResponseMessage, val gateWayMessage: String = "Gateway additional message")