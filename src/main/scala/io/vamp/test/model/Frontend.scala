package io.vamp.test.model

/**
 * *
 *
 * @param name                     - name of the front end, for reporting purposes
 * @param textRequiredInResponse   - something which should be present in the response from the frontend
 * @param weight                   - Filter weight
 */
case class Frontend(name: String, textRequiredInResponse: String, weight: Int = 100)
