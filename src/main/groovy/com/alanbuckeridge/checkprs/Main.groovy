package com.alanbuckeridge.checkprs
/**
 * abuckeridge
 */

def config = new ConfigSlurper().parse(new File("config.properties").toURI().toURL())
config.keySet().each {
    println it
}