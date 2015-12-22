package com.alanbuckeridge.checkprs
/**
 * abuckeridge
 */

ANSI_RESET = "\u001B[0m"
ANSI_RED = "\u001B[31m"
ANSI_GREEN = "\u001B[32m"

def config = new ConfigSlurper().parse(new File("config.properties").toURI().toURL())


new StashUserRepo(config).openPRs.each { pr ->
    println '-' * 80
    println "${pr.title}"
    println "Approvals:"

    if (pr.reviewers) {
        pr.reviewers.each { r -> print "\t${r.name}: ${greenIfTrue(r.approved)}   " }
        println()
    }
    else {
        println "\t(No reviewers assigned)"
    }
    println "Needs rebase? ${redIfTrue(pr.mergeInfo.rebaseNeeded)}"
    println "Ready for merge? ${pr.mergeInfo.readyForMerge}"
    pr.mergeInfo.vetoes.each {
        println "* ${it}"
    }

    println '-' * 80
}

private String redIfTrue(final boolean value) {
    return colourIfTrue(value, ANSI_RED)
}

private String greenIfTrue(final boolean value) {
    return colourIfTrue(value, ANSI_GREEN)
}

private String colourIfTrue(final boolean value, final String colour) {
    if (value)
        return colour + value.toString() + ANSI_RESET
    return value.toString()
}

