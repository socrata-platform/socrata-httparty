package com.socrata.teaparty.components

import com.socrata.teaparty.{TeaPot, TeaType}
import com.socrata.teaparty.teadatabase.TeaDatabase

trait TeaReaderComponent {
  type TeaReader <: TeaReaderLike
  trait TeaReaderLike {
    def lookupTea(tea:TeaType):Option[TeaPot]
    def list:List[TeaType]

  }

  def TeaReader():TeaReader
}

trait TeaReaderFromDatabase extends TeaReaderComponent {
  self:TeaDatabase =>

  class TeaReader() extends TeaReaderLike {
    def lookupTea(tea: TeaType): Option[TeaPot] = {
      val temperature = self.lookup(tea)
      temperature.map(t => TeaPot(tea,t))
     }
    def list: List[TeaType] = {
      self.list
    }
  }
  def TeaReader() = new TeaReader()
}
