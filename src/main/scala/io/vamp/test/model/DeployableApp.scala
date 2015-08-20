package io.vamp.test.model

/** *
  * DeployableApp describes the application
  * @param name                - name of the application to be tested
  * @param filename            - yaml file containing a blueprint
  * @param nrOfServices        - nr of services defined in the blueprint
  * @param deploymentWaitTime  - # of seconds to wait for the app deployment to finish
  * @param undeployWaitTime    - # of seconds to wait for the app to be undeployed
  * @param endpoint            - endpoint on which the app will be deployed
  * @param checkUri            - uri to check the deployed app
  * @param applicationTimeout  - # of seconds in which the app should respond
  * @param frontends           - FrontEnds deployed with this application
  */
case class DeployableApp(name: String,
                         filename: String,
                         nrOfServices: Int,
                         deploymentWaitTime: Int = 15,
                         undeployWaitTime: Int = 30,
                         endpoint: Int,
                         checkUri: String,
                         applicationTimeout: Int = 2,
                         frontends: Seq[Frontend]
                          )


