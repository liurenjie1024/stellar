package io.stellar.catalog

import scala.concurrent.Future
import scala.language.reflectiveCalls

import io.stellar.MemoryCatalog
import org.apache.iceberg.rest.responses.ConfigResponse
import org.scalatest.funspec.AsyncFunSpec
import org.scalatest.matchers.should.Matchers
import sttp.client3.SttpBackend
import sttp.client3.UriContext
import sttp.client3.quickRequest
import sttp.client3.testing.SttpBackendStub
import sttp.tapir.DecodeResult.Value
import sttp.tapir.server.stub.TapirStubInterpreter

class RESTCatalogSuite extends AsyncFunSpec with Matchers {
  private def fixture =
    new {
      val backendStub: SttpBackend[Future, Any] = TapirStubInterpreter(SttpBackendStub.asynchronousFuture)
        .whenServerEndpointsRunLogic(
          Endpoints.serverEndpoints(new RestCatalogAdapter(new MemoryCatalog("test_catalog")))
        )
        .backend()
    }

  describe("getConfig endpoint") {
    it("should return empty config by default") {
      val f = fixture

      val response = quickRequest
        .get(uri"http://test.com/api/catalog/v1/config")
        .mapResponse(Codecs.decode[ConfigResponse])
        .send(f.backendStub)

      val expectedResp = ConfigResponse.builder().build()
      response.map { r =>
        r.body match {
          case Value(resp) => {
            resp.defaults() shouldBe expectedResp.defaults()
            resp.overrides() shouldBe expectedResp.overrides()
          }
          case failure => {
            fail(failure.toString)
          }
        }
      }
    }
  }
}
