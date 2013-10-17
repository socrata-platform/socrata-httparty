package com.socrata.teaparty.handlers

import com.socrata.http.server.implicits._
import com.socrata.http.server.responses._
import com.socrata.http.server.routing.SimpleRouteContext._
import com.socrata.http.server.{HttpResponse, HttpService}
import javax.servlet.http.HttpServletRequest
import com.socrata.http.server.routing.Extractor
import com.rojoma.json.util.JsonUtil.renderJson
import com.socrata.teaparty.TeaType

class TeaRouter(teas:HttpService,
                teaLookupResource:TeaType => HttpService,
                coffeeResource:HttpService) {

  private[this] implicit val teaExtractor = new Extractor[TeaType] {
    def extract(s:String):Option[TeaType] = Some(new TeaType(s, None))
  }

  val router = Routes(
    Route("/tea",teas),
    Route("/tea/{TeaType}", teaLookupResource),
    Route("/coffee", coffeeResource)
   )



  def route(req:HttpServletRequest): HttpResponse =
    router(req.requestPath) match {
      case Some(s) =>
        s(req)
      case None =>
       BadRequest ~> Content("Bad Request")
    }



}
