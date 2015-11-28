package com.alanbuckeridge.checkprs

/**
 * abuckeridge
 */
class MergeInfo {
    boolean readyForMerge
    boolean rebaseNeeded
    List<String> vetoes = []

    MergeInfo(Map data) {
        readyForMerge = Boolean.valueOf(data.canMerge)
        rebaseNeeded = Boolean.valueOf(data.conflicted)
        data.vetoes.each {
            vetoes << it.detailedMessage
        }
    }
}
