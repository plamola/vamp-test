package traits

import io.vamp.pulse.model.Aggregator
import org.json4s.Formats
import org.json4s.DefaultFormats
import org.json4s.ext.EnumNameSerializer
import io.vamp.common.json.{OffsetDateTimeSerializer, SerializationFormat}


trait JsonFormatsProvider {
  implicit val formats: Formats
}

trait PulseJsonFormatsProvider extends JsonFormatsProvider {
  override implicit val formats: Formats = DefaultFormats + new OffsetDateTimeSerializer() + new EnumNameSerializer(Aggregator)
}



