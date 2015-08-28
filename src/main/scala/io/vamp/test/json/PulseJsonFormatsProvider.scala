package io.vamp.test.json

import io.vamp.common.json.OffsetDateTimeSerializer
import io.vamp.pulse.model.Aggregator
import org.json4s.ext.EnumNameSerializer
import org.json4s.{ DefaultFormats, Formats }

trait PulseJsonFormatsProvider extends JsonFormatsProvider {
  override implicit val formats: Formats = DefaultFormats + new OffsetDateTimeSerializer() + new EnumNameSerializer(Aggregator)
}
