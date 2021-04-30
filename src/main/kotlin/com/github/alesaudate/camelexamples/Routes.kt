package com.github.alesaudate.camelexamples

import org.apache.camel.LoggingLevel
import org.apache.camel.builder.RouteBuilder
import org.apache.camel.component.aws2.kinesis.Kinesis2Constants
import org.apache.camel.model.dataformat.BindyType
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Component
import java.nio.ByteBuffer
import java.nio.charset.Charset
import java.util.UUID.randomUUID

const val INPUT_RAW_DATA_CHANNEL = "direct://input_raw_data"
const val INPUT_SPLITTER_DATA_CHANNEL = "direct://split_data"
const val INPUT_STRUCTURED_DATA_CHANNEL = "direct://structured_data"


@Component
class ReadFromFile(@Value("\${s3.bucket}") val s3Bucket: String) : RouteBuilder() {

    override fun configure() {
        from("aws2-s3://$s3Bucket")
            .log("Reading file \${file:name}")
            .to(INPUT_SPLITTER_DATA_CHANNEL)
    }
}


@Component
class SplitData() : RouteBuilder() {

    override fun configure() {
        from(INPUT_SPLITTER_DATA_CHANNEL)
            .split(body().tokenize("\n")).streaming()
            .log(LoggingLevel.DEBUG, "Splitted line: \${body}")
            .to(INPUT_RAW_DATA_CHANNEL)
    }

}


@Component
class DataUnmarshaller() : RouteBuilder() {

    override fun configure() {
        from(INPUT_RAW_DATA_CHANNEL)
            .log(LoggingLevel.DEBUG, "About to unmarshall data from CSV to Record")
            .unmarshal().bindy(BindyType.Csv, Record::class.java)
            .log(LoggingLevel.DEBUG, "Unmarshalling succeeded")
            .log(LoggingLevel.DEBUG, "Message body is of type \${body.getClass()} : \${body}")
            .to(INPUT_STRUCTURED_DATA_CHANNEL)
    }
}

@Component
class SendToKinesis(@Value("\${kinesis.stream}") val kinesisStream: String) : RouteBuilder() {

    override fun configure() {
        from(INPUT_STRUCTURED_DATA_CHANNEL)
            .process {
                val json = it.`in`.getBody(Record::class.java).toJSON()
                it.`in`.body = json.toByteBuffer()
                it.`in`.headers[Kinesis2Constants.PARTITION_KEY] = randomUUID()
            }
            .to("aws2-kinesis://$kinesisStream")
    }
}



@Component
class ReceiveFromKinesis(@Value("\${kinesis.stream}") val kinesisStream: String) : RouteBuilder() {
    override fun configure() {
        from("aws2-kinesis://$kinesisStream")
            .log("Received record from Kinesis: \${body}")
    }

}

fun String.toByteBuffer(charset: Charset = Charsets.UTF_8) = ByteBuffer.wrap(toByteArray(charset))

fun Record.toJSON() = """
    {
       "index": $index,
       "height": $height,
       "width": $width
    }
""".trimIndent()