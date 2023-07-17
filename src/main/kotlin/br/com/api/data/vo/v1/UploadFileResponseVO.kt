package br.com.api.data.vo.v1

import jakarta.persistence.Lob

class UploadFileResponseVO (
    var fileName: String = "",
    var fileDownloadUri: String = "",
    var fileType: String = "",
    var fileSize: Long = 0,
    @Lob
    var fileByte: ByteArray
)