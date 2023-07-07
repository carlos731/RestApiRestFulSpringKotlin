package br.com.api.exceptions

import java.lang.*

class UnsupportedMathOperationException(exception: String?) : RuntimeException(exception) {}