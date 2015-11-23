package com.alanbuckeridge.checkprs
/**
 * abuckeridge
 */


def config = new ConfigSlurper().parse(new File("config.properties").toURI().toURL())

def prs = new StashUserRepo(config).getPRs()
prs.each { pr ->
    println "${pr.title} (${pr.state})"
    pr.reviewers.each { r -> println "${r.name}: ${r.approved}" }
}

