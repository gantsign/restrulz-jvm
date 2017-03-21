/*-
 * #%L
 * Restrulz
 * %%
 * Copyright (C) 2017 GantSign Ltd.
 * %%
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * #L%
 */
package com.gantsign.restrulz.json

import com.gantsign.restrulz.json.reader.JsonObjectReader
import com.gantsign.restrulz.json.reader.JsonObjectReaderFactory
import com.gantsign.restrulz.json.writer.JsonObjectWriter
import com.gantsign.restrulz.json.writer.JsonObjectWriterFactory
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import java.io.InputStream
import java.io.OutputStream
import java.lang.reflect.ParameterizedType
import java.lang.reflect.Type
import java.util.concurrent.ConcurrentHashMap

/**
 * Maps objects to/from JSON.
 */
@Suppress("unused")
class JsonObjectMapper {
    private val log: Logger = LoggerFactory.getLogger(JsonObjectMapper::class.java)
    private val readerUnsupportedTypes = ConcurrentHashMap.newKeySet<Type>()
    private val arrayReaderUnsupportedTypes = ConcurrentHashMap.newKeySet<Type>()
    private val readerMap = ConcurrentHashMap<Type, JsonObjectReader<*>>()
    private val arrayReaderMap = ConcurrentHashMap<Type, JsonObjectReader<*>>()
    private val writerUnsupportedTypes = ConcurrentHashMap.newKeySet<Type>()
    private val arrayWriterUnsupportedTypes = ConcurrentHashMap.newKeySet<Type>()
    private val writerMap = ConcurrentHashMap<Type, JsonObjectWriter<Any>>()
    private val arrayWriterMap = ConcurrentHashMap<Type, JsonObjectWriter<Any>>()
    private val typeMap = ConcurrentHashMap<Type, Type>()

    private fun mapType(type: Type): Type {
        val cachedType = typeMap[type]
        if (cachedType !== null) {
            return cachedType
        }
        if (type !is ParameterizedType && type is Class<*>
                && ArrayList::class.java == type.superclass) {
            val genericSuperclass = type.genericSuperclass
            if (genericSuperclass !== null) {
                // By sub-classing ArrayList you can pass the generic type for the list
                typeMap.put(type, genericSuperclass)
                return genericSuperclass
            }
        }
        typeMap.put(type, type)
        return type
    }

    private fun getReaderFactory(clazz: Class<*>): JsonObjectReaderFactory<*>? {
        val factoryName = "${clazz.`package`.name}.json.reader.${clazz.simpleName}ReaderFactory"
        val factoryClass: Class<*>
        try {
            factoryClass = Thread.currentThread().contextClassLoader.loadClass(factoryName)
        } catch(e: ClassNotFoundException) {
            readerUnsupportedTypes.add(clazz)
            log.trace("Unsupported type {}", clazz.name)
            return null
        }
        if (!JsonObjectReaderFactory::class.java.isAssignableFrom(factoryClass)) {
            readerUnsupportedTypes.add(clazz)
            log.warn("Expected that {} implement {}", factoryClass.name, JsonObjectReaderFactory::class.java.name)
            return null
        }
        return factoryClass.getField("INSTANCE").get(null) as JsonObjectReaderFactory<*>
    }

    private fun objectReaderFor(type: Type): JsonObjectReader<*>? {
        val cachedReader: JsonObjectReader<*>? = readerMap[type]
        if (cachedReader !== null) {
            return cachedReader
        }

        if (readerUnsupportedTypes.contains(type)) {
            return null
        }

        if (type !is Class<*>) {
            readerUnsupportedTypes.add(type)
            log.trace("Unsupported type {}", type.typeName)
            return null
        }

        val clazz = type

        val factory = getReaderFactory(clazz)
        if (factory === null) {
            return null
        }
        val jsonReader = factory.jsonReader

        readerMap.put(clazz, jsonReader)
        return jsonReader
    }

    private fun arrayReaderFor(type: ParameterizedType): JsonObjectReader<*>? {
        val cachedReader: JsonObjectReader<*>? = arrayReaderMap[type]
        if (cachedReader !== null) {
            return cachedReader
        }
        if (arrayReaderUnsupportedTypes.contains(type)) {
            return null
        }

        val listClass = type.rawType
        if (!(listClass is Class<*> && List::class.java.isAssignableFrom(listClass))) {
            arrayReaderUnsupportedTypes.add(type)
            log.trace("Unsupported array type {}", type.typeName)
            return null
        }

        val clazz = type.actualTypeArguments[0]
        val reader = objectReaderFor(clazz)
        if (reader === null) {
            arrayReaderUnsupportedTypes.add(type)
            log.trace("Unsupported array type {}", type.typeName)
            return null
        }
        arrayReaderMap.put(type, reader)
        return reader
    }

    private fun readerFor(type: Type): JsonObjectReader<*>? {
        if (type is ParameterizedType) {
            val reader = arrayReaderFor(type)
            if (reader !== null) {
                return reader
            }
        }
        return objectReaderFor(type)
    }

    /**
     * Returns `true` if the specified type can be read from JSON.
     *
     * @param type the type to check if it can be read.
     * @return `true` if the specified type can be read from JSON.
     */
    fun isSupportedForReading(type: Type): Boolean {
        return readerFor(mapType(type)) !== null
    }

    /**
     * Reads an instance of the type from the JSON encoded stream.
     *
     * @param type the type of the object to return.
     * @param inputStream the stream containing the JSON.
     * @return The object being read.
     */
    fun read(type: Type, inputStream: InputStream): Any {
        val mappedType = mapType(type)
        if (mappedType is ParameterizedType) {
            val arrayReader = arrayReaderFor(mappedType)
            if (arrayReader !== null) {
                return arrayReader.readArray(inputStream)
            }
        }
        val reader = objectReaderFor(mappedType)
        if (reader === null) {
            throw IllegalArgumentException("Unsupported type ${mappedType.typeName}")
        }
        return reader.readObject(inputStream)!!
    }

    private fun getWriterFactory(clazz: Class<*>): JsonObjectWriterFactory<*>? {
        val factoryName = "${clazz.`package`.name}.json.writer.${clazz.simpleName}WriterFactory"
        val factoryClass: Class<*>
        try {
            factoryClass = Thread.currentThread().contextClassLoader.loadClass(factoryName)
        } catch(e: ClassNotFoundException) {
            writerUnsupportedTypes.add(clazz)
            log.trace("Unsupported type {}", clazz.name)
            return null
        }
        if (!JsonObjectWriterFactory::class.java.isAssignableFrom(factoryClass)) {
            writerUnsupportedTypes.add(clazz)
            log.warn("Expected that {} implement {}", factoryClass.name, JsonObjectWriterFactory::class.java.name)
            return null
        }
        return factoryClass.getField("INSTANCE").get(null) as JsonObjectWriterFactory<*>
    }

    private fun objectWriterFor(type: Type): JsonObjectWriter<Any>? {
        val cachedWriter: JsonObjectWriter<Any>? = writerMap[type]
        if (cachedWriter !== null) {
            return cachedWriter
        }

        if (type !is Class<*>) {
            readerUnsupportedTypes.add(type)
            log.trace("Unsupported type {}", type.typeName)
            return null
        }

        val clazz = type

        val factory = getWriterFactory(clazz)
        if (factory === null) {
            return null
        }

        @Suppress("UNCHECKED_CAST")
        val jsonWriter = factory.jsonWriter as JsonObjectWriter<Any>

        writerMap.put(type, jsonWriter)
        return jsonWriter
    }

    private fun arrayWriterFor(type: ParameterizedType): JsonObjectWriter<Any>? {
        val cachedWriter: JsonObjectWriter<Any>? = arrayWriterMap[type]
        if (cachedWriter !== null) {
            return cachedWriter
        }
        if (arrayWriterUnsupportedTypes.contains(type)) {
            return null
        }

        val listClass = type.rawType
        if (!(listClass is Class<*> && List::class.java.isAssignableFrom(listClass))) {
            arrayWriterUnsupportedTypes.add(type)
            log.trace("Unsupported array type {}", type.typeName)
            return null
        }

        val clazz = type.actualTypeArguments[0]

        val writer = objectWriterFor(clazz)
        if (writer === null) {
            arrayWriterUnsupportedTypes.add(type)
            log.trace("Unsupported array type {}", type.typeName)
            return null
        }
        arrayWriterMap.put(type, writer)
        return writer
    }

    private fun writerFor(type: Type): JsonObjectWriter<Any>? {
        if (type is ParameterizedType) {
            val writer = arrayWriterFor(type)
            if (writer !== null) {
                return writer
            }
        }
        return objectWriterFor(type)
    }

    /**
     * Returns `true` if the specified type can be written to JSON.
     *
     * @param type the type to check if it can be written.
     * @return `true` if the specified type can be written to JSON.
     */
    fun isSupportedForWriting(type: Type): Boolean {
        return writerFor(mapType(type)) !== null
    }

    /**
     * Writes the specified object to the stream using JSON encoding.
     *
     * @param value the value to write out as JSON.
     * @param type the type of the object being written.
     * @param outputStream the destination for the JSON.
     */
    fun write(value: Any, type: Type?, outputStream: OutputStream) {
        val mappedType = mapType(type ?: value::class.java)
        if (mappedType is ParameterizedType) {
            if (value !is List<*>) {
                throw IllegalArgumentException("Expected ${List::class.java.name} but was ${value::class.java.name}")
            }

            @Suppress("UNCHECKED_CAST")
            val values = value as List<Any>

            val arrayWriter = arrayWriterFor(mappedType)
            if (arrayWriter !== null) {
                arrayWriter.writeAsJsonArray(outputStream, values)
                return
            }
        }

        val writer = objectWriterFor(mappedType)
        if (writer === null) {
            throw IllegalArgumentException("Unsupported type ${mappedType.typeName}")
        }
        writer.writeAsJson(outputStream, value)
    }
}
