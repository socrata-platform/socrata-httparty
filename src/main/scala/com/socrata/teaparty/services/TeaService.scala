package com.socrata.teaparty.services

import com.socrata.http.server.routing.SimpleResource
import com.socrata.http.server.responses._
import com.socrata.http.server.implicits._
import com.rojoma.json.util.JsonUtil.renderJson
import com.socrata.teaparty.teadatabase.TeaDatabase
import com.socrata.teaparty.{TeaPot, TeaType}
import com.socrata.teaparty.teadatabase.TeaDatabase
import javax.servlet.http.HttpServletResponse
import java.io.{InputStream, OutputStream}
import com.rojoma.simplearm.util._

class TeaService(teaDatabase:TeaDatabase) {
  def teaPotJson(content:String) = OK ~> ContentType("application/json") ~> Content(content)

  case class serviceLookup(tea:TeaType) extends SimpleResource {

    override def get = { req =>
     processTeaType
    }
    def copy(in:InputStream, out:OutputStream) = {
      val buffer = new Array[Byte](1024)
      Iterator.continually(in.read(buffer)).takeWhile(-1 != _).foreach(n => out.write(buffer,0,n))

    }
    def processTeaType = {
      if (tea.variant == "earlgrey") { (resp: HttpServletResponse) =>
        superSecretDontLookHere(resp)
      }
      else {
        teaDatabase.lookup(tea) match {
          case Some(i) => teaPotJson(renderJson(TeaPot(tea, Right(i))))
          case None => NotFound ~> Content("unknown tea type")
        }
      }
    }

    def superSecretDontLookHere(resp: HttpServletResponse) {
      resp.setStatus(HttpServletResponse.SC_OK)
      resp.setContentType("image/jpeg")
      for {
        in <- managed(classOf[TeaService].getResourceAsStream("Earl_Grey_tea_hot.jpg"))
        out <- managed(resp.getOutputStream)
      } copy(in = in, out = out)
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
