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

    List<PullRequest> getPRs() {
        def client = new RESTClient("https://${config.STASH_BASE}/rest/api/1.0/users/${config.USERNAME}/repos/${config.REPO}/")
        // need pre-emptive authorization for Stash.
        client.headers['Authorization'] = "Basic " + "${config.USERNAME}:${config.PASSWORD}".getBytes().encodeBase64()
        def response = client.get(
                path: "pull-requests",
                query: [direction: "outgoing", state: "all"])
        def json = response.responseData
        def result = new ArrayList<PullRequest>()
        json.values.each {
            def pr = new PullRequest(it)
            result.add(pr)
        }
        return result
    }
}
