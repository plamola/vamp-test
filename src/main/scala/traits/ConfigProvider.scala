package traits

import com.typesafe.config.ConfigFactory

/**
 * Created by lazycoder on 17/03/15.
 */
trait ConfigProvider {
  protected val config = ConfigFactory.load()
}
