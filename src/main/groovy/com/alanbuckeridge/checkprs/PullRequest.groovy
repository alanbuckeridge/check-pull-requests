package com.alanbuckeridge.checkprs

/**
 * abuckeridge
 */
class PullRequest {
    String title
    State state
    boolean open
    String latestChangeset
    List<Reviewer> reviewers = new ArrayList<>()

    PullRequest(Map prJson) {
        title = prJson.title
        state = State.valueOf(prJson.state)
        open = Boolean.valueOf(prJson.open)
        latestChangeset = prJson.fromRef.latestChangeset
        prJson.reviewers.each {
            def reviewer = new Reviewer()
            reviewer.name = it.user.name
            reviewer.approved = Boolean.valueOf(it.approved)
            reviewers << reviewer
        }
    }
}
