package com.socrata.teaparty.services

import com.socrata.http.server.routing.SimpleResource

import com.socrata.http.server.responses._
import com.socrata.http.server.implicits._

import javax.servlet.http.HttpServletResponse

class CoffeeService {
  val paymentRequired = Status(HttpServletResponse.SC_PAYMENT_REQUIRED)
    case object service extends SimpleResource {
      override def get = { req =>
        ImATeapot ~> Content("Seriously, this is a tea party. Get your coffee out of here")
      }
    }
}
