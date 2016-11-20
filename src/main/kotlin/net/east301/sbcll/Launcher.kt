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

import net.sourceforge.argparse4j.ArgumentParsers
import net.sourceforge.argparse4j.inf.ArgumentParser
import net.sourceforge.argparse4j.inf.ArgumentParserException
import net.sourceforge.argparse4j.inf.Namespace
import net.sourceforge.argparse4j.inf.Subparser
import net.sourceforge.argparse4j.inf.Subparsers
import org.springframework.boot.builder.SpringApplicationBuilder
import java.util.HashMap
import java.util.HashSet
import java.util.ServiceLoader


/**
 * Collects [Command]s, and parses command line arguments, and runs the specified command.
 *
 * @author Shu Tadaka
 */
open class Launcher {

    private class ParserFactoryImpl(private val subparsers: Subparsers) : ParserFactory {

        val commands = HashSet<String>()

        override fun addParser(command: String): Subparser {
            this.commands.add(command)
            return this.subparsers.addParser(command)
        }

    }

    companion object {

        private val KEY_COMMAND_NAME = "_command";

        @JvmStatic
        fun main(args: Array<String>) {
            main(System.getProperty("sun.java.command") ?: "(unknown)", args)
        }

        @JvmStatic
        fun main(name: String, args: Array<String>) {
            Launcher().run(name, args)
        }

    }

    /**
     * Runs the application.
     *
     * @param name  program name
     * @param args  command line arguments
     */
    fun run(name: String, args: Array<String>) {
        //
        val (parser, commands) = createParser()
        val (parsedArgs, command) = parseArguments(parser, commands, args) ?: return
        val builder = createSpringApplicationBuilder(command, parsedArgs)

        //
        val applicationContext = builder.run()
        command.run(applicationContext, parsedArgs)
    }

    private fun createParser(): Pair<ArgumentParser, Map<String, Command>> {
        val parser = ArgumentParsers.newArgumentParser("")
        val subparsers = parser.addSubparsers().dest(KEY_COMMAND_NAME)

        val commands = HashMap<String, Command>()
        ServiceLoader.load(Command::class.java).forEach { command ->
            val parserFactory = ParserFactoryImpl(subparsers)
            command.configureCommandLineParser(parserFactory)

            parserFactory.commands.forEach {
                commands[it] = command
            }
        }

        return Pair(parser, commands)
    }

    private fun parseArguments(
        parser: ArgumentParser, commands: Map<String, Command>, args: Array<String>)
        : Pair<Namespace, Command>? {

        //
        val parsedArgs: Namespace
        try {
            parsedArgs = parser.parseArgs(args)
        } catch (_: ArgumentParserException) {
            parser.printHelp()
            return null
        }

        //
        val command = commands[parsedArgs.getString(KEY_COMMAND_NAME)]
            ?: throw RuntimeException("Invalid state")

        return Pair(parsedArgs, command)
    }

    private fun createSpringApplicationBuilder(command: Command, parsedArgs: Namespace)
        : SpringApplicationBuilder {

        val builder = SpringApplicationBuilder()
        ApplicationPropertySetter.properties.clear()

        ServiceLoader.load(ApplicationEnvironment::class.java).forEach {
            it.configureApplicationBuilder(parsedArgs, builder)
            it.configureApplicationProperties(parsedArgs, ApplicationPropertySetter.properties)
        }

        command.configureApplicationBuilder(parsedArgs, builder)
        command.configureApplicationProperties(parsedArgs, ApplicationPropertySetter.properties)

        return builder
    }

}
