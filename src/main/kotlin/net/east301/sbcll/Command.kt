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
import net.sourceforge.argparse4j.inf.Subparsers
import org.springframework.context.ApplicationContext


/**
 * Represents a command of an application.
 * Instances of this class are loaded through [java.util.ServiceLoader].
 *
 * @author Shu Tadaka
 */
interface Command : ApplicationBuilderConfigurator, ApplicationPropertyConfigurator {

    /**
     * Configures command line parser.
     *
     * @param subparsers    sub-parsers
     * @return registered command IDs
     */
    fun configureCommandLineParser(subparsers: Subparsers): Collection<String>

    /**
     * Runs application logic.
     *
     * @param applicationContext    application context
     * @param args                  command line arguments
     */
    fun run(applicationContext: ApplicationContext, args: Namespace)

}
