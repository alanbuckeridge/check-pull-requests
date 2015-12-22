package com.alanbuckeridge.checkprs

/**
 * abuckeridge
 */
class PullRequest {
    long id
    String title
    State state
    boolean open
    String latestChangeset
    List<Reviewer> reviewers = []
    MergeInfo mergeInfo

    PullRequest(Map prJson) {
        id = prJson.id
        title = prJson.title
        state = State.valueOf(prJson.state)
        open = Boolean.valueOf(prJson.open)
        latestChangeset = prJson.fromRef.latestChangeset
        prJson.reviewers.each {
            this.reviewers << new Reviewer(name: it.user.name, approved: it.approved)
        }
    }
}
