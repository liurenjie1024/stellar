package io.stellar.catalog

import scala.reflect.ClassTag

import com.fasterxml.jackson.annotation.JsonAutoDetect
import com.fasterxml.jackson.annotation.PropertyAccessor
import com.fasterxml.jackson.core.JsonFactory
import com.fasterxml.jackson.databind.DeserializationFeature
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.databind.PropertyNamingStrategies.KebabCaseStrategy
import org.apache.iceberg.rest.RESTMessage
import org.apache.iceberg.rest.RESTSerializers
import org.apache.iceberg.rest.requests.UpdateTableRequest
import org.apache.iceberg.rest.responses.ConfigResponse
import org.apache.iceberg.rest.responses.LoadTableResponse
import sttp.tapir.Codec
import sttp.tapir.Codec.JsonCodec
import sttp.tapir.DecodeResult
import sttp.tapir.Schema

object Codecs {
  private lazy val mapper = {
    val mapper = new ObjectMapper(new JsonFactory())
    mapper.setVisibility(PropertyAccessor.FIELD, JsonAutoDetect.Visibility.ANY)
    mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false)
    mapper.setPropertyNamingStrategy(new KebabCaseStrategy)
    RESTSerializers.registerAll(mapper)
    mapper
  }

  private[catalog] def decode[T <: RESTMessage: ClassTag](s: String): DecodeResult[T] = {
    try {
      DecodeResult.Value(Codecs.mapper.readValue(s, implicitly[ClassTag[T]].runtimeClass.asInstanceOf[Class[T]]))
    } catch {
      case e: Exception =>
        DecodeResult.Error(s, e)
    }
  }

  private def encode[T <: RESTMessage](msg: T): String = mapper.writeValueAsString(msg)

  private def jsonCodec[T <: RESTMessage]: JsonCodec[T] = {
    implicit val schema: Schema[T] = Schema.string[T]
    Codec.json[T](decode)(encode)
  }

  implicit val loadTableRespCodec: JsonCodec[LoadTableResponse] = jsonCodec[LoadTableResponse]
  implicit val updateTableRequestCodec: JsonCodec[UpdateTableRequest] = jsonCodec[UpdateTableRequest]
  implicit val configResponseCodec: JsonCodec[ConfigResponse] = jsonCodec[ConfigResponse]
}
