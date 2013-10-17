package com.socrata.teaparty.services

import com.socrata.http.server.routing.SimpleResource
import com.socrata.http.server.responses._
import com.socrata.http.server.implicits._
import com.rojoma.json.util.JsonUtil.renderJson
import com.socrata.teaparty.teadatabase.TeaDatabase
import com.socrata.teaparty.{TeaPot, TeaType}
import com.socrata.teaparty.teadatabase.TeaDatabase


class TeaService(teaDatabase:TeaDatabase) {
  def teaPotJson(content:String) = OK ~> ContentType("application/json") ~> Content(content)

  case class serviceLookup(tea:TeaType) extends SimpleResource {

    override def get = { req =>
     processTeaType
    }

    def processTeaType = {
      if (tea.variant == "earlgray") {
        teaPotJson(renderJson(TeaPot(tea, Left("hot"))))
      }
      else {
        teaDatabase.lookup(tea) match {
          case Some(i) => teaPotJson(renderJson(TeaPot(tea, Right(i))))
          case None => NotFound ~> Content("unknown tea type")
        }
      }
    }
  }

  case object listTeas extends SimpleResource {
    override def get = {
      req =>
        val teas = teaDatabase.list
        OK ~> ContentType("application/json") ~> Content(renderJson(teas))
    }
  }

}
