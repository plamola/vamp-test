package io.vamp.test.model

/** Browsers used for testing
  * Currently nothing more than a name and a user agent string
  *
  */
object Browsers {
   val Chrome = BrowserDefinition(
     name = "Chrome",
     headers = List("User-Agent" -> "Mozilla/5.0 (Macintosh; Intel Mac OS X 10_10_4) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/44.0.2403.130 Safari/537.36")
   )

   val Safari = BrowserDefinition(
     name = "Safari",
     headers = List("User-Agent" -> "Mozilla/5.0 (iPad; U; CPU OS 3_2_1 like Mac OS X; en-us) AppleWebKit/531.21.10 (KHTML, like Gecko) Mobile/7B405")
   )

   val Firefox = BrowserDefinition(
     name = "Firefox",
     headers = List("User-Agent" -> "Mozilla/5.0 (Windows NT 6.3; rv:36.0) Gecko/20100101 Firefox/36.0")
   )

   val IE = BrowserDefinition(
     name = "IE",
     headers = List("User-Agent" -> "Mozilla/5.0 (compatible, MSIE 11, Windows NT 6.3; Trident/7.0; rv:11.0) like Gecko")

   )

 }
