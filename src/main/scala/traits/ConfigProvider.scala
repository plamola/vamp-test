package traits

import com.typesafe.config.ConfigFactory

trait ConfigProvider {
  protected val config = ConfigFactory.load()
}
