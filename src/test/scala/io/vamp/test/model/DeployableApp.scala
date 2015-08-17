package io.vamp.test.model

case class DeployableApp(name: String = "myTestApp",     // name of the application to be tested
                         filename: String,               // yaml file containing a blueprint
                         nrOfServices: Int,              // nr of services defined in the blueprint
                         deploymentWaitTime: Int = 15,   // # of seconds to wait for the app deployment to finish
                         undeployWaitTime: Int = 30,     // # of seconds to wait for the app to be undeployed
                         checkPort: Int,                 // endpoint on which the app will be deployed
                         checkUri: String,               // uri to check the deployed app
                         appWaitTime: Int = 2,        // # of seconds in which the app should respond
                         frontEnds: Seq[FrontEnd]
                          )

case class FrontEnd(appTitle: String,           // Title of the html file
                    checkResponsePart: String  // something which should be present in the response from the app, only one element must match
                     )