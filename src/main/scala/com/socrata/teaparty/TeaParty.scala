package com.socrata.teaparty

import com.socrata.http.server.SocrataServerJetty
import com.socrata.teaparty.services.{CoffeeService, TeaService}
import com.socrata.teaparty.teadatabase.NoSqlTeaDatabase
import com.socrata.teaparty.handlers.TeaRouter
import com.netflix.curator.framework.CuratorFrameworkFactory
import com.netflix.curator.{retry => retryPolicies}
import com.netflix.curator.x.discovery.ServiceDiscoveryBuilder
import com.socrata.http.server.curator.CuratorBroker
import com.rojoma.simplearm.util._


object TeaServer extends App {
  val teaService = new TeaService(NoSqlTeaDatabase)
  val coffeeService = new CoffeeService()
  val router =  new TeaRouter(
    teas = teaService.listTeas,
    teaLookupResource = teaService.serviceLookup,
    coffeeResource = coffeeService.service
  )
  val handler = router.route _
  for {
    curator <- managed {
      CuratorFrameworkFactory.builder.
        connectString("localhost:2181").
        namespace("com.socrata/teaparty").
        retryPolicy(new retryPolicies.BoundedExponentialBackoffRetry(10,10,5)).
        build()
    }
    discovery <- managed {
      ServiceDiscoveryBuilder.builder(classOf[Void]).
        client(curator).
        basePath("/services").
        build()
    }
  } {
    curator.start()
    discovery.start()
    val server = new SocrataServerJetty(
      handler,
      port = 7345,
      broker = new CuratorBroker[Void](discovery, "localhost", "teaparty", None),
      deregisterWaitMS = 1000

    )
    server.run()
  }

}
