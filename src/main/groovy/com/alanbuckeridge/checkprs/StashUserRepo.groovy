package com.alanbuckeridge.checkprs

import groovyx.net.http.RESTClient

/**
 * abuckeridge
 */
class StashUserRepo {
    ConfigObject config

    StashUserRepo(final ConfigObject configObject) {
        this.config = configObject
    }

    List<PullRequest> getOpenPRs() {
        return getPRs().findAll { pr -> pr.state == State.OPEN }
    }

    List<PullRequest> getPRs() {
        def result = new ArrayList<PullRequest>()
        def client = getClientForPullRequests()
        def response = client.get(
                query: [direction: "outgoing", state: "all"])
        response.responseData.values.each {
            def pr = new PullRequest(it)
            if (pr.isOpen()) {
                addMergeDataTo(pr)
            }
            result.add(pr)
        }
        result.sort {a, b ->
            a.state <=> b.state
        }
        return result
    }

    private RESTClient getClientForPullRequests() {
        final RESTClient client = new RESTClient("https://${config.STASH_BASE}/rest/api/1.0/users/${config.USERNAME}/repos/${config.REPO}/pull-requests")
        addAuthorisationTo(client)
        client
    }

    private void addAuthorisationTo(final RESTClient client) {
        // need pre-emptive authorization for Stash.
        client.headers['Authorization'] = "Basic " + "${config.USERNAME}:${config.PASSWORD}".getBytes().encodeBase64()
    }

    private void addMergeDataTo(final PullRequest pullRequest) {
        RESTClient mergeClient = getClientForMergeInfo(pullRequest)
        def data = mergeClient.get([:])
        def mergeInfo = new MergeInfo(data.responseData)
        pullRequest.mergeInfo = mergeInfo
    }

    private RESTClient getClientForMergeInfo(final PullRequest pullRequest) {
        final RESTClient client = new RESTClient(
                "https://${config.STASH_BASE}/rest/api/1.0/projects/${config.PROJECT}/repos/${config.REPO}/pull-requests/${pullRequest.id}/merge")
        addAuthorisationTo(client)
        client
    }

}
