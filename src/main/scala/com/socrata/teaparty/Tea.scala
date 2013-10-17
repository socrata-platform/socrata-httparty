package com.socrata.teaparty

import com.rojoma.json.util.AutomaticJsonCodecBuilder


case class TeaType(variant:String, url:Option[String])
object TeaType{
  implicit val teaCodec = AutomaticJsonCodecBuilder[TeaType]
}


case class TeaPot(content:TeaType, temperature:Either[String,Int])

object TeaPot {
  implicit val teapotCodec = AutomaticJsonCodecBuilder[TeaPot]
}


