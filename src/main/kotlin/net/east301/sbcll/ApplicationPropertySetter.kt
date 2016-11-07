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

import org.springframework.boot.context.event.ApplicationEnvironmentPreparedEvent
import org.springframework.context.ApplicationListener
import org.springframework.core.env.PropertiesPropertySource
import java.util.Properties
import java.util.concurrent.ConcurrentHashMap


/**
 * Sets application properties when an application environment is created.
 *
 * @author Shu Tadaka
 */
open class ApplicationPropertySetter : ApplicationListener<ApplicationEnvironmentPreparedEvent> {

    companion object {
        val properties = ConcurrentHashMap<String, Any>()
    }

    override fun onApplicationEvent(event: ApplicationEnvironmentPreparedEvent?) {
        //
        if (properties.isEmpty()) {
            return;
        }

        //
        val properties = Properties()
        properties.putAll(properties)

        event
            ?.environment
            ?.propertySources
            ?.addFirst(PropertiesPropertySource("sbcll", properties))
    }

}
