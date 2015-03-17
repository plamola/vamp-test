package traits

import io.magnetic.vamp_common.json.Serializers
import org.json4s.Formats

/**
 * Created by lazycoder on 17/03/15.
 */
trait JsonFormatsProvider {
  implicit val formats: Formats
}

trait PulseJsonFormatsProvider extends JsonFormatsProvider {
  override implicit val formats: Formats = Serializers.formats
}