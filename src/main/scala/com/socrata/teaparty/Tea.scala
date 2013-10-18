package com.socrata.teaparty

import com.rojoma.json.util.AutomaticJsonCodecBuilder
import com.rojoma.json.codec.JsonCodec
import com.rojoma.json.ast.{JValue, JString, JObject}

trait ModelWithUrl[M] {
  def url:String
  def model:M
}


class UrlifiedCodec[M : JsonCodec, T <: ModelWithUrl[M]](ctor: (String, M) => T) extends JsonCodec[T] {
  def encode(x: T) = {
    val encodedThing = JsonCodec[M].encode(x.model).cast[JObject].get
    JObject(encodedThing.fields + ("url" -> JString(x.url)))
  }
  def decode(x: JValue) =
    for {
      asObj <- x.cast[JObject]
      thing <- JsonCodec[M].decode(asObj)
      jsonId <- asObj.get("url")
      jsonString <- jsonId.cast[JString]
    } yield ctor(jsonString.string, thing)


}

object UrlifiedCodec {
  def apply[M : JsonCodec, T <: ModelWithUrl[M]](f: (String, M) => T) = new UrlifiedCodec[M, T](f)
}


case class TeaType(variant:String)
object TeaType{
  implicit val teaCodec = AutomaticJsonCodecBuilder[TeaType]
}


case class TeaPot(content:TeaType, temperature:Either[String,Int])

object TeaPot {
  implicit val teapotCodec = AutomaticJsonCodecBuilder[TeaPot]
}


case class TeaReference(url:String, model:TeaType) extends ModelWithUrl[TeaType]

object TeaReference {
  implicit val teaRefCodec = UrlifiedCodec((url:String, model:TeaType) => new TeaReference(url,model))

}





