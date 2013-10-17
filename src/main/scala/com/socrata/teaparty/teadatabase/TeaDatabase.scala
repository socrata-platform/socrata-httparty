package com.socrata.teaparty.teadatabase

import com.socrata.teaparty.TeaType


trait TeaDatabase {
  def lookup(tea:TeaType):Option[Int]
  def list:List[TeaType]
}


object NoSqlTeaDatabase extends TeaDatabase {
  val teas = Map("oolong" -> 190,
                 "green" -> 155,
                 "white" -> 180,
                 "rooibos" -> 212,
                 "black" -> 212)
  def lookup(tea:TeaType) = teas.get(tea.variant)
  def list = teas.keys.toList.map(TeaType(_))

}
