package com.socrata.teaparty

import com.socrata.http.server.SocrataServerJetty
import com.socrata.teaparty.services.{CoffeeService, TeaService}
import com.socrata.teaparty.teadatabase.NoSqlTeaDatabase
import com.socrata.teaparty.handlers.TeaRouter
import com.netflix.curator.framework.CuratorFrameworkFactory
import com.netflix.curator.{retry => retryPolicies}
import com.netflix.curator.x.discovery.ServiceDiscoveryBuilder
import com.socrata.http.server.curator.CuratorBroker


class TeaParty() {
  val teaService = new TeaService(NoSqlTeaDatabase)
  val coffeeService = new CoffeeService()
  val router =  new TeaRouter(teaService.listTeas, teaService.serviceLookup, coffeeService.service)
  val handler = router.route _

}

object TeaServer extends App {
  val teaParty = new TeaParty()
  val curator = CuratorFrameworkFactory.builder.
    connectString("localhost:2181").
    namespace("com.socrata/teaparty").
    retryPolicy(new retryPolicies.BoundedExponentialBackoffRetry(10,10,5)).
    build


 curator.start()
  val discovery = ServiceDiscoveryBuilder.builder(classOf[Void]).client(curator).basePath("/services").build
  discovery.start()
  val server = new SocrataServerJetty(teaParty.handler, port = 7345, broker = new CuratorBroker[Void](discovery, "localhost", "teaparty", None))
  server.run()


  discovery.close()
  curator.close()
}
