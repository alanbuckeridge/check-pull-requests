package com.alanbuckeridge.checkprs
/**
 * abuckeridge
 */


def config = new ConfigSlurper().parse(new File("config.properties").toURI().toURL())

println '-' * 80

def prs = new StashUserRepo(config).getOpenPRs()

prs.each { pr ->
    println "${pr.title}"
    println "State: ${pr.state}"
    if (State.OPEN == pr.state) {
        println "Approvals:"
        if (pr.reviewers) {
            pr.reviewers.each { r -> println "\t${r.name}: ${r.approved}" }
        }
        else {
            println "\t(No reviewers assigned)"
        }
        println "Needs rebase? ${pr.mergeInfo.rebaseNeeded}"
        println "Ready for merge? ${pr.mergeInfo.readyForMerge}"
        pr.mergeInfo.vetoes.each {
            println "* ${it}"
        }
    }

    println '-' * 80
}

