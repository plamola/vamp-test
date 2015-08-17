package io.vamp.test.common

import org.scalatest.FeatureSpec
import org.scalatest.selenium.WebBrowser
import traits.FileAccess


trait VampTest extends FeatureSpec with FileAccess with WebBrowser {

}
