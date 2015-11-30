check-pull-requests
===================

We use Atlassian Stash (now called [Bitbucket Server](https://www.atlassian.com/software/bitbucket/server)) at work. Each pull request needs a green build in [Bamboo](https://www.atlassian.com/software/bamboo/) and the code changes have to be approved by two reviewers.

Once you have more than one or two pull requests open, it becomes annoying to keep browsing Stash to see which of your pull requests:

- don't have a green build
- are still awaiting approval/merge etc.
- need to be rebased/merged with master

This becomes even more tedious once you're working with more than one repository. I want to write a tool that will automate this.


## Format

I thought the simplest thing would be a script that you'd run on demand and which would spit out a table to sysout containing the relevant details.

## Source

I started off envisioning this as a self-contained Groovy script that uses Grapes to download the dependencies as it didn't seem worthwhile to make a whole Maven/Gradle project for something this simple.

However it became apparent that things were getting complicated quickly and so the code had to be split up into separate classes. This seems like a good opportunity to get some practice using Gradle in a 'real' project.

## Atlassian APIs

Atlassian provides REST APIs for [Stash](https://developer.atlassian.com/static/rest/stash/) and [Bamboo](https://docs.atlassian.com/bamboo/REST/) (and it's other products), so I want to use these APIs to help retrieve this information I need.

Specifically:

- Call Stash to get the latest open pull requests for the user
- For each pull request:
    - Get the status (open, merged, declined)
    - Get the reviewers and whether they've approved or not
    - Find out if the pull request needs to be rebased/merged with master
    - Find out if the pull request has a green build

As it turned out, I didn't need to use the Bamboo APIs at all. These are the Stash APIs I used:

- [/rest/api/1.0/projects/{projectKey}/repos/{repositorySlug}/pull-requests](https://developer.atlassian.com/static/rest/stash/3.11.3/stash-rest.html#idp36368)
    - Substituting a username for `repositorySlug` allows all PRs for that user to be retrieved.
    - This gives you most of the important details about the PRs: id, title, state (OPEN or not), reviewers, etc.
- [/rest/api/1.0/projects/{projectKey}/repos/{repositorySlug}/pull-requests/{pullRequestId}/merge](https://developer.atlassian.com/static/rest/stash/3.11.3/stash-rest.html#idp109440)
    - Substitute `pullRequestId` with the IDs of the PRs retrieved by the previous API call
    - The response to this call reveals whether the PR has a merge conflict, or whether the PR is ready to be merged to master (along with a list of reasons why it isn't, such as `You cannot merge this pull request while it has failed builds` or `You need 2 more approvals before this pull request can be merged`
    
## Running

1. Clone the project
2. Edit `config.properties`
3. Run `./gradlew runScript` or `gradlew.bat runScript`

## Future?

Another possiblity would be some kind of daemon that every X minutes retrieves the data for you and pops up a notification if anything has changed. [This SO question](http://stackoverflow.com/questions/6499140/how-do-i-render-nice-desktop-notifications-from-java) has some pointers to Java libs that'll do the notification. Another consideration would be a SystemTray icon that pops up a tooltip (or something) populated with the latest details.
