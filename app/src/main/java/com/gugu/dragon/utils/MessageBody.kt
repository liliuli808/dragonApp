package com.gugu.dragon.utils

fun extractImageUrls(text: String): List<String> {
    var imageUrlRegex = Regex("\\[img](.*?)\\[/img]")
    var matchResults = imageUrlRegex.findAll(text)
    val imageUrls = mutableListOf<String>()

    for (matchResult in matchResults) {
        val imageUrl = matchResult.groupValues[1]
        imageUrls.add(imageUrl)
    }

    imageUrlRegex = Regex("\\[face](.*?)\\[/face]")
    matchResults = imageUrlRegex.findAll(text)
    for (matchResult in matchResults) {
        val imageUrl = matchResult.groupValues[1]
        imageUrls.add(imageUrl)
    }

    println(imageUrls)
    return imageUrls
}

fun removeHtmlTags(input: String): String {
    // 使用正则表达式匹配 HTML 标签
    var regex = Regex("<.*?>")
    var res = regex.replace(input, "")

    regex = """\[face\].*?\[/face\]""".toRegex()
    res = regex.replace(res, "")

    regex = """\[img\].*?\[/img\]""".toRegex()

    res.replace("&nbsp;", "")
    return regex.replace(res, "")
}
