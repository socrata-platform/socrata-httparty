package com.socrata.teaparty.handlers

import java.net.URLDecoder

object  QueryExtractor {
  def apply(queryStringOrNull: String): Map[String, String] =
    if(queryStringOrNull == null) Map.empty
    else queryStringOrNull.split('&').map(_.split("=", 2)).collect { case Array(key, value) =>
      URLDecoder.decode(key, "UTF-8") -> URLDecoder.decode(value, "UTF-8")
    }.toMap
}


