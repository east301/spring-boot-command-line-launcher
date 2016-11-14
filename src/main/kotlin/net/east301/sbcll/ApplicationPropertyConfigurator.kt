/*
 * Copyright 2016 Shu Tadaka.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and limitations under the License.
 */

package net.east301.sbcll

import net.sourceforge.argparse4j.inf.Namespace


/**
 * Builds Spring Application property from parsed command line arguments.
 *
 * @author Shu Tadaka
 */
interface ApplicationPropertyConfigurator {

    /**
     * Sets application properties.
     *
     * @param args          command line arguments
     * @param properties    application properties
     */
    fun configureApplicationProperties(args: Namespace, properties: MutableMap<String, Any>)

}
