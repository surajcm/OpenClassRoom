package com.classroom

import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.ResultActions
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.status
import org.springframework.util.LinkedMultiValueMap
import org.springframework.mock.web.MockMultipartFile
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.multipart

fun MockMvc.getAndExpectOk(url: String): ResultActions =
    perform(get(url)).andExpect(status().isOk)

fun MockMvc.postForm(url: String, params: Map<String, String>): ResultActions =
    perform(
        post(url)
            .params(LinkedMultiValueMap(params.mapValues { listOf(it.value) }))
    )

fun MockMvc.postMultipart(
    url: String,
    file: MockMultipartFile,
    params: Map<String, String> = emptyMap()
): ResultActions {
    val builder = multipart(url).file(file)
    params.forEach { (key, value) -> builder.param(key, value) }
    return perform(builder)
}
