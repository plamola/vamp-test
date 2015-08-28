package io.vamp.test.common

import java.util

import io.vamp.core.model.artifact.Artifact
import io.vamp.core.model.serialization.CoreSerializationFormat
import org.json4s.native.Serialization._
import org.yaml.snakeyaml.DumperOptions.FlowStyle
import org.yaml.snakeyaml.Yaml
import org.yaml.snakeyaml.nodes.Tag

import scala.collection.JavaConverters._

trait YamlUtils {

  implicit val formats = CoreSerializationFormat.default

  protected def yamlArrayListToList(ls: String): List[String] = {
    new Yaml().load(ls).asInstanceOf[util.ArrayList[java.util.Map[String, Any]]].asScala.toList.map(a ⇒ new Yaml().dumpAs(a, Tag.MAP, FlowStyle.BLOCK))
  }

  protected def artifactToYaml(artifact: Artifact): String = {
    def toJson(any: Any) = {
      any match {
        case value: AnyRef ⇒ write(value)
        case valueOther    ⇒ write(valueOther.toString)
      }
    }
    new Yaml().dumpAs(new Yaml().load(toJson(artifact)), Tag.MAP, FlowStyle.BLOCK)
  }

}
